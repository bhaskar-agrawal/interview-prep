package org.example.model;

import java.util.List;

public class ProviderConfig {
    private String name;
    private String apiKey;
    private int weightage;

    private int maxRpm;
    private List<String> supportedModels;

    public ProviderConfig(String name, String apiKey, int weightage, int maxRpm, List<String> supportedModels) {
        this.name = name;
        this.apiKey =apiKey;
        this.weightage = weightage;
        this.maxRpm = maxRpm;
        this.supportedModels = supportedModels;
    }
}
