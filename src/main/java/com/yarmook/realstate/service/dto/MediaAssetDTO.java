package com.yarmook.realstate.service.dto;

import com.yarmook.realstate.domain.enumeration.MediaKind;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.MediaAsset} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MediaAssetDTO implements Serializable {

    private Long id;

    @NotNull
    private MediaKind kind;

    @NotNull
    private String url;

    private String mimeType;

    private Integer width;

    private Integer height;

    private Long mediaSize;

    private String source;

    private String title;

    private String altText;

    private Integer sortOrder;

    private PropertyDTO property;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaKind getKind() {
        return kind;
    }

    public void setKind(MediaKind kind) {
        this.kind = kind;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(Long mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public PropertyDTO getProperty() {
        return property;
    }

    public void setProperty(PropertyDTO property) {
        this.property = property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MediaAssetDTO)) {
            return false;
        }

        MediaAssetDTO mediaAssetDTO = (MediaAssetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mediaAssetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaAssetDTO{" +
            "id=" + getId() +
            ", kind='" + getKind() + "'" +
            ", url='" + getUrl() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", mediaSize=" + getMediaSize() +
            ", source='" + getSource() + "'" +
            ", title='" + getTitle() + "'" +
            ", altText='" + getAltText() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", property=" + getProperty() +
            "}";
    }
}
