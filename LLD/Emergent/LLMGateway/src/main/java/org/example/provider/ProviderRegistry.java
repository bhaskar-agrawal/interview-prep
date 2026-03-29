package org.example.provider;

import org.example.model.ProviderConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProviderRegistry {
    private final Map<String, List<LLMProvider>> modelNameProviderMap;

    public ProviderRegistry() {
        this.modelNameProviderMap = new ConcurrentHashMap<>();
    }

    public void register(String modelName, LLMProvider provider) {
        this.modelNameProviderMap.computeIfAbsent(modelName, k-> new CopyOnWriteArrayList<>())
                .add(provider);
    }

    public List<LLMProvider> getProviders(String modelName) {
        if(!this.modelNameProviderMap.containsKey(modelName)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.modelNameProviderMap.get(modelName));
    }
}
