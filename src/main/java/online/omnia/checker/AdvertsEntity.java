package online.omnia.checker;

import javax.persistence.*;

/**
 * Created by lollipop on 20.11.2017.
 */
@Entity
@Table(name = "adverts")
public class AdvertsEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "advshortname")
    private String advShortName;
    @Column(name = "advname")
    private String advName;
    @Column(name = "secretkey")
    private String secretKey;
    @Column(name = "url")
    private String url;
    @Column(name = "risk")
    private int risk;
    @Column(name = "api_key")
    private String apiKey;
   // @Column(name = "timezone")
    @Transient
    private Integer timezone;

    public int getId() {
        return id;
    }

    public String getAdvShortName() {
        return advShortName;
    }

    public String getAdvName() {
        return advName;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getUrl() {
        return url;
    }

    public int getRisk() {
        return risk;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Integer getTimezone() {
        return timezone;
    }
}
