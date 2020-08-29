package pl.euvic.squash.model.scheduling;

import java.util.Date;


public class MailDataScheduleHelper {
    private Date sendDate;
    private String email;

    public MailDataScheduleHelper() {
    }

    public MailDataScheduleHelper(Date sendDate, String email) {
        this.sendDate = sendDate;
        this.email = email;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
