package com.example.NotificationService.Utils;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class FileProcessor {
    String filepath;
    public FileProcessor() {
        filepath = "./data.txt";
    }

    public synchronized void processMessage(String message)  throws IOException {
        try (FileWriter writer = new FileWriter(filepath, true)) {
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(message);
            bufferedWriter.write(System.lineSeparator());
            bufferedWriter.flush();
        }
    }
}
