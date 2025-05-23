package com.elly.athena.item.weapon.warrior;

import com.elly.athena.item.Item_Register;
import com.elly.athena.item.weapon.RPGMelee_Base;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class Sword implements Item_Register.ItemRegisterData_Upgrade {

    static class Sword_Item extends RPGMelee_Base {
        public Sword_Item(Properties properties) {
            super(properties);
        }
    }

    @Override
    public int size() {
        return 11;
    }

    @Override
    public String get_key() {
        return "weapon_sword";
    }

    @Override
    public Item.Properties get_behaviour() {
        return new Item.Properties().stacksTo(1).durability(get_durability(0)).attributes(get_attribute(0));
    }

    @Override
    public ItemAttributeModifiers get_attribute(int index) {
        return switch(index){
                case 1 ->  RPGMelee_Base.GetModify(6.2F, -2.4F);
                case 2 ->  RPGMelee_Base.GetModify(6.4F, -2.4F);
                case 3 ->  RPGMelee_Base.GetModify(6.6F, -2.4F);
                case 4 ->  RPGMelee_Base.GetModify(6.8F, -2.4F);
                case 5 ->  RPGMelee_Base.GetModify(7.0F, -2.4F);
                case 6 ->  RPGMelee_Base.GetModify(7.3F, -2.4F);
                case 7 ->  RPGMelee_Base.GetModify(7.6F, -2.4F);
                case 8 ->  RPGMelee_Base.GetModify(7.9F, -2.2F);
                case 9 ->  RPGMelee_Base.GetModify(8.5F, -2.0F);
                case 10 -> RPGMelee_Base.GetModify(9.0F, -1.8F);
                default -> RPGMelee_Base.GetModify(6.0f, -2.4F);
        };
    }

    @Override
    public int get_durability(int index) {
        return switch(index) {
            case 1 -> 150;
            case 2 -> 180;
            case 3 -> 210;
            case 4 -> 240;
            case 5 -> 270;
            case 6 -> 300;
            case 7 -> 330;
            case 8 -> 360;
            case 9 -> 390;
            case 10 -> 420;
            default -> 450;
        };
    }

    @Override
    public Item get_binding(Item.Properties props) {
        return new Item( props);
    }
}