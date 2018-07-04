package online.omnia.checker;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lollipop on 19.11.2017.
 */
@Entity
@Table(name = "leads_incorrect")
public class LeadsIncorrectEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "lead_date")
    private Date leadDate;
    @Column(name = "advert_id")
    private int advertId;
    @Column(name = "lead_id")
    private int leadId;
    @Column(name = "lead_sum")
    private int leadSum;
    @Column(name = "lead_data")
    private String leadData;

    public void setId(int id) {
        this.id = id;
    }

    public void setLeadDate(Date leadDate) {
        this.leadDate = leadDate;
    }

    public void setAdvertId(int advertId) {
        this.advertId = advertId;
    }

    public void setLeadId(int leadId) {
        this.leadId = leadId;
    }

    public void setLeadSum(int leadSum) {
        this.leadSum = leadSum;
    }

    public void setLeadData(String leadData) {
        this.leadData = leadData;
    }

    public int getId() {
        return id;
    }

    public Date getLeadDate() {
        return leadDate;
    }

    public int getAdvertId() {
        return advertId;
    }

    public int getLeadId() {
        return leadId;
    }

    public int getLeadSum() {
        return leadSum;
    }

    public String getLeadData() {
        return leadData;
    }
}
