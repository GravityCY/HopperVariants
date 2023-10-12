package me.gravityio.varhopper.block;

public interface Hopper {
    int getDefaultCooldown();
    void setCooldown(int cooldown);
    boolean push();
    boolean pullFromContainer();
    boolean pullFromWorld();
    boolean hasInputContainer();
    default boolean pull() {
        if (this.hasInputContainer()) {
            return this.pullFromContainer();
        } else {
            return this.pullFromWorld();
        }
    }

    default void pushAndPull() {
        var applyCooldown = false;
        applyCooldown |= this.push();
        applyCooldown |= this.pull();
        if (!applyCooldown) return;
        this.setCooldown(this.getDefaultCooldown());
    }

    /**
     * Run when the Hopper managed to push or when the Hopper managed to pull.
     */
    default void onPushPull() {}

}
