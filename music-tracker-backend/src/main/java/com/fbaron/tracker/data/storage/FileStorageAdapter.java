package com.fbaron.tracker.data.storage;

import com.fbaron.tracker.core.repository.FileStorageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStorageAdapter implements FileStorageRepository {

    @Value("${app.storage.location}")
    private String storageLocation;

    @PostConstruct
    public void init() {
        try {
            Path rootPath = Paths.get(storageLocation);
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    @Override
    public String saveToDisk(String isrCode, byte[] content) {
        try {
            Path targetFile = Paths.get(storageLocation).resolve(isrCode + ".jpg");

            Files.createDirectories(targetFile.getParent());

            Files.write(targetFile, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("Saved cover to disk: isrCode={}, path={}", isrCode, targetFile.toAbsolutePath());
            return targetFile.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not storage the image", e);
        }
    }

}
