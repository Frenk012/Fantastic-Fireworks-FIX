package com.rnr.rnrjointmod.firework;

import net.minecraft.core.GlobalPos;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Server-side lookup of loaded firework cakes by their user-visible id. */
public class FireworkCakeRegistry {
    private static final Map<String, GlobalPos> BY_ID = new ConcurrentHashMap<>();

    public static void register(String id, GlobalPos pos) {
        if (!id.isEmpty()) {
            BY_ID.put(id, pos);
        }
    }

    public static void unregister(String id, GlobalPos pos) {
        if (!id.isEmpty()) {
            BY_ID.remove(id, pos);
        }
    }

    public static @Nullable GlobalPos get(String id) {
        return BY_ID.get(id);
    }

    public static Map<String, GlobalPos> all() {
        return Map.copyOf(BY_ID);
    }
}
