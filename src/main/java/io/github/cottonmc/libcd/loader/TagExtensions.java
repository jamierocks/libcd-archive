package io.github.cottonmc.libcd.loader;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import io.github.cottonmc.libcd.LibCD;
import io.github.cottonmc.libcd.api.CDCommons;
import io.github.cottonmc.libcd.api.CDSyntaxError;
import io.github.cottonmc.libcd.api.condition.ConditionalData;
import javax.annotation.Nullable;
import net.minecraft.class_2960;
import net.minecraft.class_3494;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class TagExtensions {
    private TagExtensions() {
    }

    /**
     * Loads the tag extensions for a given tag from the JSON object.
     *
     * @param getter a getter converting from value ID to optional value
     * @param json the JSON object
     * @param <T> the tag value type
     */
    public static <T> ExtensionResult<T> load(Function<class_2960, Optional<T>> getter, JsonObject json) {
        boolean shouldReplace = false;
        List<class_3494.class_3496<T>> entries = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (json.containsKey("replace")) {
            shouldReplace = testCondition(json.get("replace"), warnings);
        }

        if (json.containsKey("entries")) {
            JsonElement rawEntries = json.get("entries");
            if (!(rawEntries instanceof JsonArray)) {
                throw new IllegalArgumentException("'entries' tag in LibCD tag extensions is not an array: " + rawEntries);
            }

            JsonArray entryArray = (JsonArray) rawEntries;
            for (JsonElement rawEntry : entryArray) {
                if (!(rawEntry instanceof JsonObject)) {
                    warnings.add("Tag extension entry '" + rawEntry + "' is not a JsonObject! Skipping...");
                    continue;
                }

                JsonObject entry = (JsonObject) rawEntry;
                if (testCondition(entry.get("when"), warnings)) {
                    JsonElement rawValues = entry.get("values");
                    if (!(rawValues instanceof JsonArray)) {
                        warnings.add("'values' of tag extension entry '" + rawEntry + "' is not a JsonArray! Skipping...");
                        continue;
                    }

                    JsonArray values = (JsonArray) rawValues;
                    for (int i = 0; i < values.size(); i++) {
                        @Nullable String value = values.get(String.class, i);

                        if (value == null) {
                            warnings.add("Could not convert JSON element '" + values.get(i) + "' to a string in tag extensions! Skipping...");
                        } else if (value.startsWith("#")) {
                            entries.add(new class_3494.class_3497<>(new class_2960(value.substring(1))));
                        } else {
                            @Nullable T tagEntry = getter.apply(new class_2960(value)).orElse(null);
                            if (tagEntry == null) {
                                warnings.add("Unknown tag value '" + value + "' in LibCD tag extensions! Skipping...");
                                continue;
                            }

                            entries.add(new class_3494.class_3498<>(Collections.singleton(tagEntry)));
                        }
                    }
                }
            }
        }

        return new ExtensionResult<>(shouldReplace, entries, warnings);
    }

    private static boolean testCondition(JsonElement condition, List<String> warnings) {
        if (condition instanceof JsonArray) {
            for (JsonElement child : (JsonArray) condition) {
                if (!testCondition(child, warnings)) return false;
            }
            return true;
        } else if (!(condition instanceof JsonObject)) {
            warnings.add("Error parsing tag extensions: item '" + condition + "' in condition list not a JsonObject");
            return false;
        }

        JsonObject obj = (JsonObject) condition;
        for (String key : obj.keySet()) {
            class_2960 id = key.equals("or") ? new class_2960(CDCommons.MODID, "or") : class_2960.method_12829(key);
            if (id == null || !ConditionalData.hasCondition(id)) {
                warnings.add("Found unknown condition: " + key);
            }

            try {
                if (!ConditionalData.testCondition(id, ConditionalData.parseElement(obj.get(key)))) return false;
            } catch (CDSyntaxError e) {
                warnings.add("Error parsing tag extensions: item '" + condition + "' in condition list errored: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    public static final class ExtensionResult<T> {
        private final boolean shouldReplace;
        private final List<class_3494.class_3496<T>> entries;
        private final List<String> warnings;

        public ExtensionResult(boolean shouldReplace, List<class_3494.class_3496<T>> entries, List<String> warnings) {
            this.shouldReplace = shouldReplace;
            this.entries = entries;
            this.warnings = warnings;
        }

        public boolean shouldReplace() {
            return shouldReplace;
        }

        public List<class_3494.class_3496<T>> getEntries() {
            return entries;
        }

        public List<String> getWarnings() {
            return warnings;
        }
    }
}
