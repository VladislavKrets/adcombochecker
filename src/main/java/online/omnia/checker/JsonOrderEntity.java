package online.omnia.checker;

import java.util.Date;

/**
 * Created by lollipop on 20.11.2017.
 */
public class JsonOrderEntity {
    private Date created;
    private String status;
    private int orderId;
    private double price;
    private String offerName;
    private String clickid;
    private String data;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    @Override
    public String toString() {
        return "JsonOrderEntity{" +
                "created=" + created +
                ", status='" + status + '\'' +
                ", orderId=" + orderId +
                ", price=" + price +
                ", offerName='" + offerName + '\'' +
                ", clickid='" + clickid + '\'' +
                '}';
    }

    public String getClickid() {
        return clickid;
    }

    public void setClickid(String clickid) {
        this.clickid = clickid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
