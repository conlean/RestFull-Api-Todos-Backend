package com.mavha.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "mavha//upload-dir";

    public String getLocation() {
        return location;
    }

}
