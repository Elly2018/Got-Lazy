package com.elly.athena.entity.spell;

import com.elly.athena.data.Attribute_Register;
import com.elly.athena.entity.Entity_Register;
import com.elly.athena.item.Item_Register;
import com.elly.athena.sound.Sound_Register;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;

import java.util.Objects;

public class MagicBall extends ThrowableItemProjectile {

    int tick;

    public MagicBall(EntityType<? extends MagicBall> entityType, Level level) {
        super(entityType, level);
        init();
    }

    public MagicBall(Level level, LivingEntity owner, ItemStack item) {
        super(Entity_Register.MAGICBALL, owner, level, item);
        init();
    }

    public MagicBall(Level level, double x, double y, double z, ItemStack item) {
        super(Entity_Register.MAGICBALL, x, y, z, level, item);
        init();
    }

    private void init(){
        this.setNoGravity(true);
        this.refreshDimensions();
    }

    @Override
    public void tick() {
        super.tick();
        tick += 1;
        if(tick > 2){
            tick = 0;
            for(int i = 0; i < 4; i++){
                this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(),
                        this.random.nextIntBetweenInclusive(-1, 1) * 0.2F,
                        this.random.nextIntBetweenInclusive(-1, 1) * 0.2F,
                        this.random.nextIntBetweenInclusive(-1, 1) * 0.2F);
            }
        }

        if(isInWater() || isInLava()){
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public int getDimensionChangingDelay() {
        return 6;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Item_Register.ENTITY_MAGICBALL.get();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (this.level() instanceof ServerLevel sl) {
            for(var en: sl.getAllEntities()){
                if(en instanceof LivingEntity target && en.isAlive()){
                    if(en.position().distanceTo(this.position()) < 3F){
                        HitEntity(target);
                    }
                }
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level() instanceof ServerLevel sl) {
            for(var en: sl.getAllEntities()){
                if(en instanceof LivingEntity target && en.isAlive()){
                    if(en.position().distanceTo(this.position()) < 3F){
                        HitEntity(target);
                    }
                }
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    private void HitEntity(Entity target){
        if(this.getOwner() instanceof Player player){
            int magic_attack = (int) Objects.requireNonNull(player.getAttribute(Attribute_Register.MAGIC_ATTACK)).getValue();
            int magic_attack_max = (int) Objects.requireNonNull(player.getAttribute(Attribute_Register.MAGIC_ATTACK_MAX)).getValue();
            int d = this.getRandom().nextIntBetweenInclusive(magic_attack, Math.max(magic_attack + magic_attack_max, magic_attack + 1));
            target.hurt(this.damageSources().thrown(this, this.getOwner()), (float)d);
            if(player.level() instanceof ServerLevel sl){
                sl.playSound(null, target.getX(), target.getY(), target.getZ(), Sound_Register.HIT0.get(), SoundSource.HOSTILE, 1F, 1F);
            }
        }
    }
}
