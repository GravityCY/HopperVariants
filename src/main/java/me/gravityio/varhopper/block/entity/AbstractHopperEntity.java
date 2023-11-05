package me.gravityio.varhopper.block.entity;

import me.gravityio.varhopper.VarHopperMod;
import me.gravityio.varhopper.block.AbstractHopperBlock;
import me.gravityio.varhopper.block.ModProperties;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.gnomecraft.cooldowncoordinator.CooldownCoordinator;
import net.gnomecraft.cooldowncoordinator.CoordinatedCooldown;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.BooleanSupplier;

@SuppressWarnings("UnstableApiUsage")
public abstract class AbstractHopperEntity extends LockableContainerBlockEntity implements Hopper, CoordinatedCooldown {
    protected final DefaultedList<ItemStack> inventory;
    protected final InventoryStorage inventoryWrapper;
    public int transferCooldown = -1;
    public AbstractHopperEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(this.getInventorySize(), ItemStack.EMPTY);
        this.inventoryWrapper = InventoryStorage.of(this, null);
        this.transferCooldown = this.getDefaultCooldown();
    }
    public abstract int getDefaultCooldown();
    public int getInventorySize() {
        return 5;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient) {
            return;
        }

        if (this.transferCooldown == 1) {
            this.transferCooldown = 0;
        } else if (this.transferCooldown > 0) {
            this.transferCooldown--;
            return;
        }

        this.pushAndPull(world, pos, state);
    }

    // Pushes from the hopper into an Inventory and then pulls from an Inventory above or from an ItemEntity
    public void pushAndPull(World world, BlockPos pos, BlockState state) {
        this.pushAndThen(world, pos, state, () -> this.pull(world, state, pos));
    }

    // Pushes from the Hopper into an Inventory and then runs a BooleanSupplier
    public void pushAndThen(World world, BlockPos pos, BlockState state, BooleanSupplier thenRun) {
        if (!state.get(AbstractHopperBlock.ENABLED) || this.transferCooldown != 0) {
            return;
        }
        var applyCooldown = false;
        applyCooldown |= this.push(world, pos, state);
        applyCooldown |= thenRun.getAsBoolean();
        if (!applyCooldown) return;
        this.transferCooldown = this.getDefaultCooldown();
        this.markDirty();
    }

    // Push from the Hopper into an Inventory
    public boolean push(World world, BlockPos pos, BlockState state) {
        return this.pushIntoStorage(world, pos, state);
    }

    // Push from the Hopper into a Inventory
    public boolean pushIntoStorage(World world, BlockPos pos, BlockState state) {
        var dir = state.get(AbstractHopperBlock.FACING);
        var outputPos = pos.offset(dir);
        Storage<ItemVariant> outputStorage = ItemStorage.SIDED.find(world, outputPos, dir.getOpposite());
        if (outputStorage == null) return false;
        boolean isOutputEmpty = CooldownCoordinator.isStorageEmpty(outputStorage);
        var itemsMoved = StorageUtil.move(inventoryWrapper, outputStorage, i -> true, 1, null);
        if (itemsMoved == 0) return false;
        var outputEntity = world.getBlockEntity(outputPos);
        if (isOutputEmpty) {
            CooldownCoordinator.notify(outputEntity);
        }
        return true;
    }

    // Pull from the world or from inventories into the Hopper
    public boolean pull(World world, BlockState state, BlockPos pos) {
        if (!state.get(ModProperties.LID_OPENED))
            return false;

        if (this.hasInputStorage(world, pos)) {
            return this.pullFromStorage(world, pos);
        } else {
            return this.pullFromWorld(world);
        }
    }

    // Pull from Inventories into the Hopper
    public boolean pullFromStorage(World world, BlockPos pos) {
        var target = pos.offset(Direction.UP);
        Storage<ItemVariant> input = ItemStorage.SIDED.find(world, target, Direction.UP.getOpposite());
        if (input == null) return false;
        var itemsMoved = StorageUtil.move(input, inventoryWrapper, i -> true, 1, null);
        return itemsMoved > 0;
    }

    // Pull from the World into the Hopper
    public boolean pullFromWorld(World world) {
        var boxes = this.getInputAreaShape().getBoundingBoxes();
        for (Box box : boxes) {
            var target = box.offset(this.pos.getX(), this.pos.getY(), this.pos.getZ());
            List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, target, EntityPredicates.VALID_ENTITY);
            for (ItemEntity item : items) {
                if (!this.pullItemEntity(item)) {
                    continue;
                }

                return true;
            }
        }
        return false;
    }

    // Pull a specific ItemEntity
    public boolean pullItemEntity(ItemEntity item) {
        var stack = item.getStack();
        try (Transaction transaction = Transaction.openOuter()) {
            var itemsInserted = this.inventoryWrapper.insert(ItemVariant.of(stack), stack.getCount(), transaction);
            stack.decrement((int) itemsInserted);
            transaction.commit();
        }

        if (stack.isEmpty()) {
            item.discard();
            return true;
        }

        return false;
    }

    // Whether the Hopper has an Inventory above it to pull from
    private boolean hasInputStorage(World world, BlockPos pos) {
        var target = pos.offset(Direction.UP);
        return ItemStorage.SIDED.find(world, target, Direction.UP.getOpposite()) != null;
    }

    // When an ItemEntity collides with the hopper
    public void onItemCollision(BlockState state, World world, BlockPos pos, ItemEntity entity) {
        if (world.isClient || !state.get(ModProperties.LID_OPENED)) return;
        this.pushAndThen(world, pos, state, () -> this.pullItemEntity(entity));
    }

    @Override
    public void notifyCooldown() {
        if (this.world == null) {
            return;
        }

        this.transferCooldown = this.getDefaultCooldown();
        this.markDirty();
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory.clear();
        Inventories.readNbt(nbt, this.inventory);
        this.transferCooldown = nbt.getInt("TransferCooldown");
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("TransferCooldown", this.transferCooldown);
    }
    @Override
    protected abstract Text getContainerName();
    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new HopperScreenHandler(syncId, playerInventory, this);
    }
    @Override
    public double getHopperX() {
        return this.pos.getX() + 0.5;
    }
    @Override
    public double getHopperY() {
        return this.pos.getY() + 0.5;
    }
    @Override
    public double getHopperZ() {
        return this.pos.getZ() + 0.5;
    }
    @Override
    public int size() {
        return this.inventory.size();
    }
    @Override
    public boolean isEmpty() {
        return this.inventory.stream().allMatch(ItemStack::isEmpty);
    }
    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }
    @Override
    public ItemStack removeStack(int slot, int amount) {
        var newStack = Inventories.splitStack(this.inventory, slot, amount);
        if (!newStack.isEmpty()) {
            this.markDirty();
        }
        return newStack;
    }
    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }
    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        this.markDirty();
    }
    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }
    @Override
    public void clear() {
        this.inventory.clear();
    }
}
