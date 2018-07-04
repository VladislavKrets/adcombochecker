package online.omnia.checker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by lollipop on 19.11.2017.
 */
public class Main {
    public static void main(String[] args) throws ParseException {
        List<AdvertsEntity> advertsEntities = MySQLDaoImpl.getInstance().getAdverts("Adcombo");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.parse(dateFormat.format(date));
        String answer;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(List.class, new JsonOrderListDeserializer());
        Gson gson = builder.create();
        List<JsonOrderEntity> jsonOrderEntities;
        List<PostBackEntity> postBackEntities;
        LeadsIncorrectEntity leadsIncorrectEntity;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        LeadsIncorrectEntity incorrectEntity = null;
        for (AdvertsEntity advertsEntity : advertsEntities) {
            answer = HttpMethodUtils.getMethod("https://api.adcombo.com/api/v2/order/status?api_key=" + advertsEntity.getApiKey() + "&ts=" + (date.getTime() / 1000 - 2592000) + "&te=" + date.getTime() / 1000 + "&currency_code=USD", null);
            jsonOrderEntities = gson.fromJson(answer, List.class);
            System.out.println(jsonOrderEntities);
            for (JsonOrderEntity jsonOrderEntity : jsonOrderEntities) {
                postBackEntities = MySQLDaoImpl.getInstance().getPostbacks(jsonOrderEntity.getClickid(), jsonOrderEntity.getCreated());
                if (!postBackEntities.isEmpty()) {
                    for (PostBackEntity postBackEntity : postBackEntities) {
                        if (postBackEntity.getTime().getTime() + 900000 >= jsonOrderEntity.getCreated().getTime() + 10800000
                                || postBackEntity.getTime().getTime() - 900000 <= jsonOrderEntity.getCreated().getTime() + 10800000) {

                            if (!postBackEntity.getDate().equals(new java.sql.Date(dateFormat.parse(dateFormat.format(jsonOrderEntity.getCreated())).getTime() + 10800000))) {
                                System.out.println(new Date(postBackEntity.getDate().getTime()));
                                System.out.println(new Date(dateFormat.parse(dateFormat.format(jsonOrderEntity.getCreated())).getTime() + 10800000));
                                if (postBackEntity.getSum() == jsonOrderEntity.getPrice()) {
                                    postBackEntity.setDate(new java.sql.Date(jsonOrderEntity.getCreated().getTime()));
                                    postBackEntity.setTime(new java.sql.Time(jsonOrderEntity.getCreated().getTime()));
                                    System.out.println("Updating");
                                    MySQLDaoImpl.getInstance().updatePostback(postBackEntity);
                                } else {
                                    leadsIncorrectEntity = new LeadsIncorrectEntity();
                                    leadsIncorrectEntity.setAdvertId(advertsEntity.getId());
                                    leadsIncorrectEntity.setLeadId(jsonOrderEntity.getOrderId());
                                    leadsIncorrectEntity.setLeadDate(simpleDateFormat.parse(simpleDateFormat.format(jsonOrderEntity.getCreated())));
                                    leadsIncorrectEntity.setLeadSum((int) jsonOrderEntity.getPrice());
                                    leadsIncorrectEntity.setLeadData(jsonOrderEntity.getData());
                                    System.out.println("adding leads incorrect");
                                    incorrectEntity = MySQLDaoImpl.getInstance().getLeadFromDb(leadsIncorrectEntity.getLeadDate(),
                                            leadsIncorrectEntity.getAdvertId(), leadsIncorrectEntity.getLeadSum());
                                    if (incorrectEntity == null) {
                                        MySQLDaoImpl.getInstance().addLeadsIncorrect(leadsIncorrectEntity);
                                    }
                                    else {
                                        leadsIncorrectEntity.setId(incorrectEntity.getId());
                                        MySQLDaoImpl.getInstance().updateLeadsIncorrect(leadsIncorrectEntity);
                                    }
                                }
                            }
                        } else {

                            leadsIncorrectEntity = new LeadsIncorrectEntity();
                            leadsIncorrectEntity.setAdvertId(advertsEntity.getId());
                            leadsIncorrectEntity.setLeadId(jsonOrderEntity.getOrderId());
                            leadsIncorrectEntity.setLeadDate(simpleDateFormat.parse(simpleDateFormat.format(jsonOrderEntity.getCreated())));
                            leadsIncorrectEntity.setLeadSum((int) jsonOrderEntity.getPrice());
                            leadsIncorrectEntity.setLeadData(jsonOrderEntity.getData());
                            System.out.println("adding leads incorrect");
                            incorrectEntity = MySQLDaoImpl.getInstance().getLeadFromDb(leadsIncorrectEntity.getLeadDate(),
                                    leadsIncorrectEntity.getAdvertId(), leadsIncorrectEntity.getLeadSum());
                            if (incorrectEntity == null) {
                                MySQLDaoImpl.getInstance().addLeadsIncorrect(leadsIncorrectEntity);
                            }
                            else {
                                leadsIncorrectEntity.setId(incorrectEntity.getId());
                                MySQLDaoImpl.getInstance().updateLeadsIncorrect(leadsIncorrectEntity);
                            }
                        }

                    }
                } else {

                    leadsIncorrectEntity = new LeadsIncorrectEntity();
                    leadsIncorrectEntity.setAdvertId(advertsEntity.getId());
                    leadsIncorrectEntity.setLeadId(jsonOrderEntity.getOrderId());
                    leadsIncorrectEntity.setLeadDate(simpleDateFormat.parse(simpleDateFormat.format(jsonOrderEntity.getCreated())));
                    leadsIncorrectEntity.setLeadSum((int) jsonOrderEntity.getPrice());
                    leadsIncorrectEntity.setLeadData(jsonOrderEntity.getData());
                    System.out.println("adding leads incorrect");
                    incorrectEntity = MySQLDaoImpl.getInstance().getLeadFromDb(leadsIncorrectEntity.getLeadDate(),
                            leadsIncorrectEntity.getAdvertId(), leadsIncorrectEntity.getLeadSum());
                    if (incorrectEntity == null) {
                        MySQLDaoImpl.getInstance().addLeadsIncorrect(leadsIncorrectEntity);
                    }
                    else {
                        leadsIncorrectEntity.setId(incorrectEntity.getId());
                        MySQLDaoImpl.getInstance().updateLeadsIncorrect(leadsIncorrectEntity);
                    }
                }
            }
        }
        MySQLDaoImpl.getSessionFactory().close();
    }
}
