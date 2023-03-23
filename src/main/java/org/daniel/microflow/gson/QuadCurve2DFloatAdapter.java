package org.daniel.microflow.gson;

import java.awt.geom.QuadCurve2D;
import java.lang.reflect.Type;

import com.google.gson.*;

public class QuadCurve2DFloatAdapter implements JsonSerializer<QuadCurve2D.Float>, JsonDeserializer<QuadCurve2D.Float> {

    @Override
    public JsonElement serialize(QuadCurve2D.Float curve, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x1", curve.x1);
        obj.addProperty("y1", curve.y1);
        obj.addProperty("ctrlx", curve.ctrlx);
        obj.addProperty("ctrly", curve.ctrly);
        obj.addProperty("x2", curve.x2);
        obj.addProperty("y2", curve.y2);
        return obj;
    }

    @Override
    public QuadCurve2D.Float deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        float x1 = obj.get("x1").getAsFloat();
        float y1 = obj.get("y1").getAsFloat();
        float ctrlx = obj.get("ctrlx").getAsFloat();
        float ctrly = obj.get("ctrly").getAsFloat();
        float x2 = obj.get("x2").getAsFloat();
        float y2 = obj.get("y2").getAsFloat();
        return new QuadCurve2D.Float(x1, y1, ctrlx, ctrly, x2, y2);
    }
}