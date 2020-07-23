package io.github.cottonmc.libcd.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.minecraft.class_117;
import net.minecraft.class_131;
import net.minecraft.class_217;
import net.minecraft.class_40;
import net.minecraft.class_42;
import net.minecraft.class_44;
import net.minecraft.class_4570;
import net.minecraft.class_47;
import net.minecraft.class_52;
import net.minecraft.class_55;
import net.minecraft.class_61;
import net.minecraft.class_75;
import net.minecraft.class_79;
import net.minecraft.loot.*;

public class Gsons {
    public static final JsonParser PARSER = new JsonParser();

    public static final Gson LOOT_TABLE = new GsonBuilder()
            .registerTypeAdapter(class_61.class, new class_61.class_62())
            .registerTypeAdapter(class_40.class, new class_40.class_41())
            .registerTypeAdapter(class_44.class, new class_44.class_45())
            .registerTypeAdapter(class_42.class, new class_42.class_43())
            .registerTypeAdapter(class_55.class, new class_55.class_57())
            .registerTypeAdapter(class_52.class, new class_52.class_54())
            .registerTypeHierarchyAdapter(class_79.class, new class_75.class_76())
            .registerTypeHierarchyAdapter(class_117.class, new class_131.class_132())
            .registerTypeHierarchyAdapter(class_4570.class, new class_217.class_218())
            .registerTypeHierarchyAdapter(class_47.class_50.class, new class_47.class_50.class_51())
            .create();
}
