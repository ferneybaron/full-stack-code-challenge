package com.fbaron.tracker.core.repository;

public interface FileStorageRepository {

    String saveToDisk(String isrCode, byte[] content);

}
