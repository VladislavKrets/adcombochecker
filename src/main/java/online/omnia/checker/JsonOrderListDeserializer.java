package online.omnia.checker;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lollipop on 20.11.2017.
 */
public class JsonOrderListDeserializer implements JsonDeserializer<List<JsonOrderEntity>>{
    @Override
    public List<JsonOrderEntity> deserialize(JsonElement jsonElement, Type type,
                                       JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        JsonArray array = object.get("data").getAsJsonArray();
        List<JsonOrderEntity> jsonOrderEntities = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        JsonOrderEntity jsonOrderEntity;
        for (JsonElement element : array) {
            jsonOrderEntity = new JsonOrderEntity();
            if (element.getAsJsonObject().get("status").getAsString().equals("confirmed")) {
                try {
                    jsonOrderEntity.setCreated(simpleDateFormat.parse(element.getAsJsonObject().get("created").getAsString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                jsonOrderEntity.setOrderId(element.getAsJsonObject().get("order_id").getAsInt());
                jsonOrderEntity.setOfferName(element.getAsJsonObject().get("offer_name").getAsString());
                jsonOrderEntity.setPrice(element.getAsJsonObject().get("price").getAsDouble());
                jsonOrderEntity.setStatus(element.getAsJsonObject().get("status").getAsString());
                jsonOrderEntity.setClickid(element.getAsJsonObject().get("subacc_1").getAsString().contains("_") ? element.getAsJsonObject()
                        .get("subacc_1").getAsString().split("_")[1] : element.getAsJsonObject().get("subacc_1").getAsString());
                jsonOrderEntity.setData(element.toString().replaceAll(":", "=").replaceAll(",", "_").replaceAll("(\\{\\})", ""));
                jsonOrderEntities.add(jsonOrderEntity);
            }
        }
        return jsonOrderEntities;
    }
}
