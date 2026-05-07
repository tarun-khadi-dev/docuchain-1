package com.dapp.docuchain.dto;

import java.util.List;

public class EmailRequest {
    private List<String> to;
    private String subject;
    private String body;

    public List<String> getTo() { return to; }
    public void setTo(List<String> to) { this.to = to; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}



