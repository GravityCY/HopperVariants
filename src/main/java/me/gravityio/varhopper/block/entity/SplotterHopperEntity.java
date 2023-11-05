package me.gravityio.varhopper.block.entity;

import me.gravityio.varhopper.ModConfig;
import me.gravityio.varhopper.block.AbstractHopperBlock;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.gnomecraft.cooldowncoordinator.CooldownCoordinator;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// Splitter Hopper == Splotter
public class SplotterHopperEntity extends AbstractHopperEntity {
    protected boolean intoFirst = true;

    public SplotterHopperEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SPLOTTER_HOPPER, blockPos, blockState);
    }

    @Override
    public int getDefaultCooldown() {
        return ModConfig.INSTANCE.splotterCooldown;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.varhopper.splotter_hopper");
    }

    @Override
    public boolean pushIntoStorage(World world, BlockPos pos, BlockState state) {
        Direction dir;
        if (intoFirst) {
            dir = state.get(AbstractHopperBlock.FACING);
        } else {
            dir = state.get(AbstractHopperBlock.FACING).getOpposite();
        }
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
        this.intoFirst = !this.intoFirst;
        return true;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.intoFirst = nbt.getBoolean("IntoFirst");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("IntoFirst", this.intoFirst);
    }
}
