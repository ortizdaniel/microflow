package org.daniel.microflow.gson;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import java.awt.Rectangle;

public class RectangleAdapter implements JsonSerializer<Rectangle>, JsonDeserializer<Rectangle> {

    @Override
    public JsonElement serialize(Rectangle rectangle, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", rectangle.x);
        obj.addProperty("y", rectangle.y);
        obj.addProperty("width", rectangle.width);
        obj.addProperty("height", rectangle.height);
        return obj;
    }

    @Override
    public Rectangle deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        int x = obj.get("x").getAsInt();
        int y = obj.get("y").getAsInt();
        int width = obj.get("width").getAsInt();
        int height = obj.get("height").getAsInt();
        return new Rectangle(x, y, width, height);
    }
}

