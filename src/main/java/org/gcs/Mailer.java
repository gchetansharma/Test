package org.gcs;

import java.util.List;

public class Mailer {

    private String url;
    private List<String> recipients;
    private List<String> cc;
    private List<String> bcc;
    private List<AllowedMailers> allowedMailers;

    // Default constructor
    public Mailer() {
    }

    // Parameterized constructor
    public Mailer(String url, List<String> recipients, List<String> cc, List<String> bcc, List<AllowedMailers> allowedMailers) {
        this.url = url;
        this.recipients = recipients;
        this.cc = cc;
        this.bcc = bcc;
        this.allowedMailers = allowedMailers;
    }

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public List<AllowedMailers> getAllowedMailers() {
        return allowedMailers;
    }

    public void setAllowedMailers(List<AllowedMailers> allowedMailers) {
        this.allowedMailers = allowedMailers;
    }

    @Override
    public String toString() {
        return "Mailer{" +
                "url='" + url + '\'' +
                ", recipients=" + recipients +
                ", cc=" + cc +
                ", bcc=" + bcc +
                ", allowedMailers=" + allowedMailers +
                '}';
    }
}
