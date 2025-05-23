package com.elly.athena.keymap;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.jarjar.nio.util.Lazy;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

import static com.elly.athena.Athena.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class KeyMap_Register {
    public static Lazy<KeyMapping> EQUIPMENT_MAPPING = Lazy.of(() -> new KeyMapping("key.athena.equipment", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.athena"));
    public static Lazy<KeyMapping> SKILL_MAPPING = Lazy.of(() -> new KeyMapping("key.athena.skill", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, "key.categories.athena"));
    public static Lazy<KeyMapping> STATUS_MAPPING = Lazy.of(() -> new KeyMapping("key.athena.status", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.categories.athena"));
    public static Lazy<KeyMapping> SWITCH_MAPPING = Lazy.of(() -> new KeyMapping("key.athena.switch", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.categories.athena"));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(EQUIPMENT_MAPPING.get());
        event.register(SKILL_MAPPING.get());
        event.register(STATUS_MAPPING.get());
        event.register(SWITCH_MAPPING.get());
    }
}
