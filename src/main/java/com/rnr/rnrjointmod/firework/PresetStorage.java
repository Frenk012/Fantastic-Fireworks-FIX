package com.rnr.rnrjointmod.firework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rnr.rnrjointmod.RnRJointMod;
import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import net.minecraft.nbt.TagParser;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Client-side storage of user-made firework presets as SNBT in
 * config/rnrjointmod-presets.json. A saved preset can be applied to any cake.
 */
public class PresetStorage {
    private static final Path FILE = FMLPaths.CONFIGDIR.get().resolve("rnrjointmod-presets.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type TYPE = new TypeToken<Map<String, Map<String, String>>>() {}.getType();

    private static Map<String, Map<String, String>> read() {
        if (!Files.exists(FILE)) {
            return new LinkedHashMap<>();
        }
        try {
            Map<String, Map<String, String>> map = GSON.fromJson(Files.readString(FILE), TYPE);
            return map != null ? map : new LinkedHashMap<>();
        } catch (Exception e) {
            RnRJointMod.LOGGER.error("Could not read firework presets", e);
            return new LinkedHashMap<>();
        }
    }

    private static void write(Map<String, Map<String, String>> map) {
        try {
            Files.writeString(FILE, GSON.toJson(map, TYPE));
        } catch (IOException e) {
            RnRJointMod.LOGGER.error("Could not save firework presets", e);
        }
    }

    public static List<String> names() {
        return new ArrayList<>(read().keySet());
    }

    public static void save(String name, FireworkCakeEntity cake) {
        Map<String, Map<String, String>> map = read();
        Map<String, String> preset = new LinkedHashMap<>();
        preset.put("rocket", cake.rocket.save().toString());
        preset.put("explosion", cake.explosion.save().toString());
        map.put(name, preset);
        write(map);
    }

    /** @return true if the preset existed and was applied */
    public static boolean apply(String name, FireworkCakeEntity cake) {
        Map<String, String> preset = read().get(name);
        if (preset == null) {
            return false;
        }
        try {
            cake.rocket.load(TagParser.parseTag(preset.get("rocket")));
            cake.explosion.load(TagParser.parseTag(preset.get("explosion")));
            return true;
        } catch (Exception e) {
            RnRJointMod.LOGGER.error("Could not apply firework preset '{}'", name, e);
            return false;
        }
    }

    public static void delete(String name) {
        Map<String, Map<String, String>> map = read();
        if (map.remove(name) != null) {
            write(map);
        }
    }
}
