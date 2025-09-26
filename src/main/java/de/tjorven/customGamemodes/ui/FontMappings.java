package de.tjorven.customGamemodes.ui;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Material;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class FontMappings {
    private static Map<String, String> materialToUnicode;

    public static void init(){
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        FontMappings.materialToUnicode = gson.fromJson(MappingJsonString.mappingString, mapType);
    }

    /**
     * Gibt den Unicode-String für ein Material zurück.
     * @param material_enum Material (egal ob uppercase oder lowercase)
     * @return Unicode-Zeichen als String oder null, wenn nicht gefunden.
     */
    public static String getUnicodeString(Material material_enum) {
        String material = material_enum.name();
        String upper = material.toUpperCase();
        String code = materialToUnicode.get(upper);
        if (code == null) return null;

        // z.B. "U+E001" → 0xE001
        if (code.startsWith("U+")) {
            int codePoint = Integer.parseInt(code.substring(2), 16);
            return new String(Character.toChars(codePoint));
        }
        return null;
    }

    /**
     * Gibt direkt das erste Zeichen als char zurück.
     */
    public static char getUnicodeChar(Material material) {
        String str = getUnicodeString(material);
        if (str != null && !str.isEmpty()) {
            return str.charAt(0);
        }
        return '\u0000'; // Null-Char wenn nicht gefunden
    }

    /**
     * Optional: Die komplette Map holen
     */
    public static Map<String, String> getAllMappings() {
        return materialToUnicode;
    }
}
