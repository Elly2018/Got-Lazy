package com.elly.athena.event.server;

import com.elly.athena.Athena;
import com.elly.athena.Config;
import com.elly.athena.data.Attachment_Register;
import com.elly.athena.data.Attribute_Register;
import com.elly.athena.data.implementation.BattleHotbar;
import com.elly.athena.data.implementation.PlayerEquipment;
import com.elly.athena.data.implementation.PlayerSkill;
import com.elly.athena.data.implementation.PlayerStatus;
import com.elly.athena.data.interfaceType.attachment.IPlayerStatus;
import com.elly.athena.event.ServerHandler;
import com.elly.athena.gui.menu.Equipment_Menu;
import com.elly.athena.gui.menu.Skill_Menu;
import com.elly.athena.network.general.*;
import com.elly.athena.sound.Sound_Register;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static com.elly.athena.keymap.KeyMap_Register.EQUIPMENT_MAPPING;
import static com.elly.athena.keymap.KeyMap_Register.SKILL_MAPPING;

@EventBusSubscriber(modid = Athena.MODID)
public class RPG_ServerTickEvent {
    private static int tick = 0;

    @SubscribeEvent
    public static void update(ServerTickEvent.Pre event){
        healevent(event.getServer().getPlayerList().getPlayers());
        event.getServer().getPlayerList().getPlayers().forEach( player -> {
            LastStatueSave(player);
            PlayerStateUpdate(player);
            PlayerMenuUpdate(player);
            PlayerNetworkUpdate(player);
        });
        ArrayList<Runnable> all = new ArrayList<>();
        while (!ServerHandler.event_worker.isEmpty()){
            try {
                all.add(ServerHandler.event_worker.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (Runnable runnable : all) {
            runnable.run();
        }
    }

    private static void LastStatueSave(Player player){
        PlayerStatus ps = player.getData(Attachment_Register.PLAYER_STATUS);
        ps.setLastLoginHP((int)player.getHealth());
        ps.setLastLoginMP((int) Objects.requireNonNull(player.getAttribute(Attribute_Register.MANA)).getValue());
        player.setData(Attachment_Register.PLAYER_STATUS, ps);
    }

    private static void PlayerNetworkUpdate(ServerPlayer player){
        PlayerStatus ps = player.getData(Attachment_Register.PLAYER_STATUS);
        PlayerSkill pss = player.getData(Attachment_Register.PLAYER_SKILL);
        PlayerEquipment pe = player.getData(Attachment_Register.PLAYER_EQUIPMENT);
        BattleHotbar bh = player.getData(Attachment_Register.BATTLE_HOTBAR);
        PacketDistributor.sendToPlayer(player, new StatusPayload.StatusData(ps.serializeNBT(player.registryAccess())));
        PacketDistributor.sendToPlayer(player, new SkillPayload.SkillData(pss.serializeNBT(player.registryAccess())));
        PacketDistributor.sendToPlayer(player, new EquipmentPayload.EquipmentData(pe.serializeNBT(player.registryAccess())));
        PacketDistributor.sendToPlayer(player, new HotbarPayload.HotbarData(bh.serializeNBT(player.registryAccess())));
        PacketDistributor.sendToPlayer(player, AttributePayload.SelfTag(player.getAttributes()));
    }

    private static void PlayerMenuUpdate(ServerPlayer player){
        while (EQUIPMENT_MAPPING.get().consumeClick()){
            player.openMenu(new SimpleMenuProvider(
                    Equipment_Menu::new,
                    Component.empty()
            ));
        }
        while (SKILL_MAPPING.get().consumeClick()) {
            com.elly.athena.Athena.LOGGER.debug(String.format("%s is trying to check skill", player.getName().getString()));
            player.openMenu(new SimpleMenuProvider(
                    Skill_Menu::new,
                    Component.empty()
            ));
        }
    }

    private static void PlayerStateUpdate(ServerPlayer player){
        PlayerStatus ps = player.getData(Attachment_Register.PLAYER_STATUS);
        PlayerSkill pss = player.getData(Attachment_Register.PLAYER_SKILL);

        if(ps.isLevelUp(ps.getLevel())){
            ps.setExp(0);
            ps.addLevel(1);
            ps.addPoint(Config.level_point);
            ps.addSkillPoint(Config.level_skill);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), Sound_Register.LEVELUP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            Attribute_Register.ApplyChange(player);
        }
        pss.UpdateCooldown();
    }

    private static void healevent(Collection<ServerPlayer> players){
        tick++;
        if(tick > Config.heal_countdown){
            tick = 0;

            players.forEach(player -> {
                IPlayerStatus ps = player.getData(Attachment_Register.PLAYER_STATUS);
                double maxvalue = Objects.requireNonNull(player.getAttributes().getInstance(Attributes.MAX_HEALTH)).getValue();
                float heal = (float) (maxvalue * 0.05F);
                player.heal(heal);

                int maxMana = ps.getManaMaximum();
                heal = (float) (maxMana * 0.05F);
                AttributeMap map = player.getAttributes();
                var instance = map.getInstance(Attribute_Register.MANA);
                var instance_max = map.getInstance(Attribute_Register.MANA_MAX);
                assert instance != null;
                assert instance_max != null;
                instance.setBaseValue(Math.min(instance.getValue() + (int) Math.ceil(heal), instance_max.getValue()));
            });
        }
    }
}
