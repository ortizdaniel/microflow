package org.daniel.microflow.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.awt.geom.Ellipse2D;
import java.lang.reflect.Type;

public class Ellipse2DFloatAdapter implements JsonSerializer<Ellipse2D.Float>, JsonDeserializer<Ellipse2D.Float> {

    @Override
    public JsonElement serialize(Ellipse2D.Float ellipse, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", ellipse.x);
        obj.addProperty("y", ellipse.y);
        obj.addProperty("width", ellipse.width);
        obj.addProperty("height", ellipse.height);
        return obj;
    }

    @Override
    public Ellipse2D.Float deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        float x = obj.get("x").getAsFloat();
        float y = obj.get("y").getAsFloat();
        float width = obj.get("width").getAsFloat();
        float height = obj.get("height").getAsFloat();
        return new Ellipse2D.Float(x, y, width, height);
    }
}