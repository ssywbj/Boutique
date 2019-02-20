package ying.jie.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

public class GsonUtil {
    private static GsonUtil instance = new GsonUtil();
    private Gson gson;

    private GsonUtil() {
        gson = new GsonBuilder().serializeNulls()/*.setPrettyPrinting()*/.excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
    }

    public <T> T jsonToObject(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public <T> T jsonToObject(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public String objectToJson(Object object) {
        return gson.toJson(object);
    }

    public static GsonUtil getInstance() {
        if (instance == null) {
            instance = new GsonUtil();
        }
        return instance;
    }

    private final class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Date date = null;
            try {
                date = new Date(json.getAsLong());
            } catch (Exception e) {

            }
            return date;
        }
    }

}

