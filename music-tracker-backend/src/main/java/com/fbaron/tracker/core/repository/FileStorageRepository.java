package com.fbaron.tracker.core.repository;

import com.fbaron.tracker.core.exception.FileReadException;
import com.fbaron.tracker.core.exception.StorageException;

/**
 * Port for storing and reading binary files (e.g. cover images) on disk.
 */
public interface FileStorageRepository {

    /**
     * Writes content to disk, keyed by ISRC (e.g. {@code isrCode + ".jpg"}).
     *
     * @param isrCode  identifier used to derive the file path
     * @param content  raw bytes to write
     * @return the absolute path of the written file
     * @throws StorageException if write fails
     */
    String saveToDisk(String isrCode, byte[] content);

    /**
     * Reads file content from the given path.
     *
     * @param path absolute path to the file
     * @return the file bytes
     * @throws FileReadException if read fails
     */
    byte[] readFromDisk(String path);

}