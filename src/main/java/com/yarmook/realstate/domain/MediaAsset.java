package com.yarmook.realstate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yarmook.realstate.domain.enumeration.MediaKind;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A MediaAsset.
 */
@Document(collection = "media_asset")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MediaAsset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "media_asset_seq";

    @Id
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

    @DocumentReference(lazy = true)
    @JsonIgnoreProperties(value = { "marker" }, allowSetters = true)
    private Property property;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MediaAsset id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaKind getKind() {
        return this.kind;
    }

    public MediaAsset kind(MediaKind kind) {
        this.setKind(kind);
        return this;
    }

    public void setKind(MediaKind kind) {
        this.kind = kind;
    }

    public String getUrl() {
        return this.url;
    }

    public MediaAsset url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public MediaAsset mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getWidth() {
        return this.width;
    }

    public MediaAsset width(Integer width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public MediaAsset height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getMediaSize() {
        return this.mediaSize;
    }

    public MediaAsset mediaSize(Long mediaSize) {
        this.setMediaSize(mediaSize);
        return this;
    }

    public void setMediaSize(Long mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getSource() {
        return this.source;
    }

    public MediaAsset source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return this.title;
    }

    public MediaAsset title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAltText() {
        return this.altText;
    }

    public MediaAsset altText(String altText) {
        this.setAltText(altText);
        return this;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public MediaAsset sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public MediaAsset property(Property property) {
        this.setProperty(property);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MediaAsset)) {
            return false;
        }
        return getId() != null && getId().equals(((MediaAsset) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaAsset{" +
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
            "}";
    }
}
