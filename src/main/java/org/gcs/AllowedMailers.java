package org.gcs;

import java.time.LocalDateTime;

public class AllowedMailers {
    private String email;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    public AllowedMailers() {
    }

    public AllowedMailers(String email, LocalDateTime validFrom, LocalDateTime validUntil) {
        this.email = email;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    @Override
    public String toString() {
        return "AllowedMailers{" +
                "email='" + email + '\'' +
                ", validFrom=" + validFrom +
                ", validUntil=" + validUntil +
                '}';
    }
}
