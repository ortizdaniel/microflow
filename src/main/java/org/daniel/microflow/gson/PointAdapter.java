package org.daniel.microflow.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.awt.Point;
import java.lang.reflect.Type;

public class PointAdapter implements JsonSerializer<Point>, JsonDeserializer<Point> {

    @Override
    public JsonElement serialize(Point point, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", point.x);
        obj.addProperty("y", point.y);
        return obj;
    }

    @Override
    public Point deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        int x = obj.get("x").getAsInt();
        int y = obj.get("y").getAsInt();
        return new Point(x, y);
    }
}
