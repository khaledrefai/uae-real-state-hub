package com.yarmook.realstate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yarmook.realstate.domain.enumeration.LeadStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A ContactLead.
 */
@Document(collection = "contact_lead")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactLead implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "contact_lead_seq";

    @Id
    private Long id;

    @NotNull
    private String name;

    private String email;

    private String phone;

    private String whatsapp;

    private String message;

    private String source;

    private String utm;

    @NotNull
    private Instant createdAt;

    @NotNull
    private LeadStatus status;

    @DocumentReference(lazy = true)
    @JsonIgnoreProperties(value = { "owner", "plan" }, allowSetters = true)
    private AgentSite site;

    @DocumentReference(lazy = true)
    @JsonIgnoreProperties(value = { "marker" }, allowSetters = true)
    private Property property;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContactLead id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ContactLead name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public ContactLead email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public ContactLead phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWhatsapp() {
        return this.whatsapp;
    }

    public ContactLead whatsapp(String whatsapp) {
        this.setWhatsapp(whatsapp);
        return this;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getMessage() {
        return this.message;
    }

    public ContactLead message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return this.source;
    }

    public ContactLead source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUtm() {
        return this.utm;
    }

    public ContactLead utm(String utm) {
        this.setUtm(utm);
        return this;
    }

    public void setUtm(String utm) {
        this.utm = utm;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ContactLead createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public LeadStatus getStatus() {
        return this.status;
    }

    public ContactLead status(LeadStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(LeadStatus status) {
        this.status = status;
    }

    public AgentSite getSite() {
        return this.site;
    }

    public void setSite(AgentSite agentSite) {
        this.site = agentSite;
    }

    public ContactLead site(AgentSite agentSite) {
        this.setSite(agentSite);
        return this;
    }

    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public ContactLead property(Property property) {
        this.setProperty(property);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactLead)) {
            return false;
        }
        return getId() != null && getId().equals(((ContactLead) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactLead{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", whatsapp='" + getWhatsapp() + "'" +
            ", message='" + getMessage() + "'" +
            ", source='" + getSource() + "'" +
            ", utm='" + getUtm() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
