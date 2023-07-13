package com.blog.email.payload;

public class EmailDetails {
    private String to;
    private String[] cc;
    private String [] bcc;
    private String msgBody;
    private String subject;
    private String attachment;

    public EmailDetails(String to, String msgBody, String subject, String attachment) {
        this.to = to;
        this.msgBody = msgBody;
        this.subject = subject;
        this.attachment = attachment;
    }

    public EmailDetails() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }
}
