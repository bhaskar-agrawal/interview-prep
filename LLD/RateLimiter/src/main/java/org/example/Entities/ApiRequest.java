package org.example.Entities;


import lombok.Data;

@Data
public class ApiRequest {
    String clientId;
    String endpoint;

    public ApiRequest(String clientId, String endpoint) {
        this.clientId = clientId;
        this.endpoint = endpoint;
    }
}
