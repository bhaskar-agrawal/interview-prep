package org.example.entities;

public class Request {
    private String endpoint;
    private String clientId;

    public Request(String endpoint, String clientId) {
        this.clientId = clientId;
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return  this.endpoint;
    }

    public String getClientId() {
        return  this.clientId;
    }
}
