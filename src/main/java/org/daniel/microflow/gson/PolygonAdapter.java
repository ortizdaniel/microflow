package org.daniel.microflow.gson;


import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.awt.Polygon;
import java.lang.reflect.Type;

public class PolygonAdapter implements JsonSerializer<Polygon>, JsonDeserializer<Polygon> {

    @Override
    public JsonElement serialize(Polygon polygon, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        JsonArray xPointsArr = new JsonArray();
        JsonArray yPointsArr = new JsonArray();
        for (int i = 0; i < polygon.npoints; i++) {
            xPointsArr.add(new JsonPrimitive(polygon.xpoints[i]));
            yPointsArr.add(new JsonPrimitive(polygon.ypoints[i]));
        }
        obj.add("xpoints", xPointsArr);
        obj.add("ypoints", yPointsArr);
        obj.addProperty("npoints", polygon.npoints);
        return obj;
    }

    @Override
    public Polygon deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonArray xPointsArr = obj.getAsJsonArray("xpoints");
        JsonArray yPointsArr = obj.getAsJsonArray("ypoints");
        int[] xPoints = new int[xPointsArr.size()];
        int[] yPoints = new int[yPointsArr.size()];
        for (int i = 0; i < xPointsArr.size(); i++) {
            xPoints[i] = xPointsArr.get(i).getAsInt();
            yPoints[i] = yPointsArr.get(i).getAsInt();
        }
        int nPoints = obj.get("npoints").getAsInt();
        return new Polygon(xPoints, yPoints, nPoints);
    }
}

