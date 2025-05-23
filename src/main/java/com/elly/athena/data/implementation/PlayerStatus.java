package com.elly.athena.data.implementation;

import com.elly.athena.data.interfaceType.attachment.IPlayerStatus;
import com.elly.athena.data.types.JobType;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class PlayerStatus implements IPlayerStatus, INBTSerializable<CompoundTag> {
    private int Coin = 0;
    private JobType Job = JobType.NEWBIE;
    private int Level = 1;
    private int Exp = 0;
    private int MaxHealth = 20;
    private int MaxMana = 10;
    private int Str = 1;
    private int Dex = 1;
    private int Int = 1;
    private int Luk = 1;
    private int Point = 0;
    private int Skill = 0;
    private int Mode = 0;
    private int LastHP = 20;
    private int LastMP = 10;
    public boolean Dirty = true;

    @Override public int getCoin() { return this.Coin; }
    @Override public boolean HaveCoin(int value) { return this.Coin >= value; }
    @Override public void setCoin(int value) { this.Coin = value; }
    @Override public void addCoin(int value) { this.Coin += value; }
    @Override public void spendCoin(int value) { this.Coin -= value; }

    @Override public float getExpProgress(int level) { return (float)this.Exp / (float)getExpMaximum(level); }
    @Override public int getExp() { return this.Exp; }
    @Override public int getExpMaximum(int level) { return (int)(Math.pow((Math.pow(2, level) * 100) + 10, (double)1.1F)); }
    @Override public void addExp(int value) { this.Exp += value; }
    @Override public void setExp(int value) { this.Exp = value; }
    @Override public boolean isLevelUp(int level) { return this.Exp >= this.getExpMaximum(level); }

    @Override public JobType getJob() { return this.Job; }
    @Override public void setJob(JobType value) { this.Job = value; }

    @Override public int getHealthMaximum() { return this.MaxHealth; }
    @Override public void setMaxHealth(int value) { this.MaxHealth = value; }
    @Override public void addMaxHealth(int value) { this.MaxHealth += value; }

    @Override public int getManaMaximum() { return MaxMana; }
    @Override public void setManaMaximum(int value) { MaxMana = value; }
    @Override public void addManaMaximum(int value) { MaxMana += value; }

    @Override public int getDex() { return this.Dex; }
    @Override public void setDex(int value) { this.Dex = value; }
    @Override public void addDex(int value) { this.Dex += value; }

    @Override public int getInt() { return this.Int;  }
    @Override public void setInt(int value) { this.Int = value; }
    @Override public void addInt(int value) { this.Int += value; }

    @Override public int getLevel() { return this.Level; }
    @Override public void setLevel(int value) { this.Level = value; }
    @Override public void addLevel(int value) { this.Level += value; }

    @Override public int getLuk() { return this.Luk; }
    @Override public void setLuk(int value) { this.Luk = value; }
    @Override public void addLuk(int value) { this.Luk += value; }

    @Override public int getPoint() { return this.Point; }
    @Override public void setPoint(int value) { this.Point = value; }
    @Override public void addPoint(int value) { this.Point += value; }
    @Override public void consumer(int value) { this.Point -= value; }

    @Override public int getSkillPoint() { return this.Skill; }
    @Override public void setSkillPoint(int value) { this.Skill = value; }
    @Override public void addSkillPoint(int value) { this.Skill += value; }
    @Override public void consumerSkill(int value) { this.Skill -= value; }

    @Override public int getStr() { return this.Str; }
    @Override public void setStr(int value) { this.Str = value; }
    @Override public void addStr(int value) { this.Str += value; }

    @Override public int getMode() { return Mode; }
    @Override public void setMode(int value) { Mode = value; }

    @Override public int getLastLoginHP() { return LastHP; }
    @Override public void setLastLoginHP(int value) { LastHP = value;}
    @Override public int getLastLoginMP() { return LastMP; }
    @Override public void setLastLoginMP(int value) { LastMP = value; }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag elementTag = new CompoundTag();

        elementTag.putInt("coin", this.Coin);
        elementTag.putInt("job", this.Job.id);
        elementTag.putInt("level", this.Level);
        elementTag.putInt("exp", this.Exp);
        elementTag.putInt("max_health", this.MaxHealth);
        elementTag.putInt("max_mana", this.MaxMana);
        elementTag.putInt("str", this.Str);
        elementTag.putInt("dex", this.Dex);
        elementTag.putInt("int", this.Int);
        elementTag.putInt("luk", this.Luk);
        elementTag.putInt("point", this.Point);
        elementTag.putInt("skill", this.Skill);
        elementTag.putInt("mode", this.Mode);
        elementTag.putInt("last_hp", this.LastHP);
        elementTag.putInt("last_mp", this.LastMP);

        CompoundTag nbt = new CompoundTag();
        nbt.put("status", elementTag);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag compoundTag) {
        CompoundTag elementTag = compoundTag.getCompound("status");

        this.Coin = elementTag.getInt("coin");
        this.Job = JobType.getEnumFromId(elementTag.getInt("job"));
        this.Level = elementTag.getInt("level");
        this.Exp = elementTag.getInt("exp");
        this.MaxHealth = elementTag.getInt("max_health");
        this.MaxMana = elementTag.getInt("max_mana");
        this.Str = elementTag.getInt("str");
        this.Dex = elementTag.getInt("dex");
        this.Int = elementTag.getInt("int");
        this.Luk = elementTag.getInt("luk");
        this.Point = elementTag.getInt("point");
        this.Skill = elementTag.getInt("skill");
        this.Mode = elementTag.getInt("mode");
        this.LastHP = elementTag.getInt("last_hp");
        this.LastMP = elementTag.getInt("last_mp");
    }
}
