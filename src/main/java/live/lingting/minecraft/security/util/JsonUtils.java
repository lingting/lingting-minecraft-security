package live.lingting.minecraft.security.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author lingting 2024-06-28 14:15
 */
public class JsonUtils {
    private static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        GSON = builder.create();
    }

    public static <T> T toObj(String json, Class<T> cls) {
        return GSON.fromJson(json, cls);
    }

    public static <T> T toObj(InputStream stream, Class<T> cls) {
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        return GSON.fromJson(reader, cls);
    }

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

}
