package com.electrohub.dto;

/**
 * DTO for external resource link responses.
 */
public class ResourceLinkDto {

    private String key;
    private String title;
    private String description;
    private String url;

    public ResourceLinkDto() {
    }

    public ResourceLinkDto(String key, String title, String description, String url) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
