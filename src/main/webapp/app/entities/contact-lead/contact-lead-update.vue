<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.contactLead.home.createOrEditLabel"
          data-cy="ContactLeadCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.contactLead.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="contactLead.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="contactLead.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.name')" for="contact-lead-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="contact-lead-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.email')" for="contact-lead-email"></label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="contact-lead-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.phone')" for="contact-lead-phone"></label>
            <input
              type="text"
              class="form-control"
              name="phone"
              id="contact-lead-phone"
              data-cy="phone"
              :class="{ valid: !v$.phone.$invalid, invalid: v$.phone.$invalid }"
              v-model="v$.phone.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.whatsapp')" for="contact-lead-whatsapp"></label>
            <input
              type="text"
              class="form-control"
              name="whatsapp"
              id="contact-lead-whatsapp"
              data-cy="whatsapp"
              :class="{ valid: !v$.whatsapp.$invalid, invalid: v$.whatsapp.$invalid }"
              v-model="v$.whatsapp.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.message')" for="contact-lead-message"></label>
            <input
              type="text"
              class="form-control"
              name="message"
              id="contact-lead-message"
              data-cy="message"
              :class="{ valid: !v$.message.$invalid, invalid: v$.message.$invalid }"
              v-model="v$.message.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.source')" for="contact-lead-source"></label>
            <input
              type="text"
              class="form-control"
              name="source"
              id="contact-lead-source"
              data-cy="source"
              :class="{ valid: !v$.source.$invalid, invalid: v$.source.$invalid }"
              v-model="v$.source.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.utm')" for="contact-lead-utm"></label>
            <textarea
              class="form-control"
              name="utm"
              id="contact-lead-utm"
              data-cy="utm"
              :class="{ valid: !v$.utm.$invalid, invalid: v$.utm.$invalid }"
              v-model="v$.utm.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.createdAt')" for="contact-lead-createdAt"></label>
            <div class="d-flex">
              <input
                id="contact-lead-createdAt"
                data-cy="createdAt"
                type="datetime-local"
                class="form-control"
                name="createdAt"
                :class="{ valid: !v$.createdAt.$invalid, invalid: v$.createdAt.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.createdAt.$model)"
                @change="updateInstantField('createdAt', $event)"
              />
            </div>
            <div v-if="v$.createdAt.$anyDirty && v$.createdAt.$invalid">
              <small class="form-text text-danger" v-for="error of v$.createdAt.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.status')" for="contact-lead-status"></label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="contact-lead-status"
              data-cy="status"
              required
            >
              <option
                v-for="leadStatus in leadStatusValues"
                :key="leadStatus"
                :value="leadStatus"
                :label="t$('uaeRealStateHubApp.LeadStatus.' + leadStatus)"
              >
                {{ leadStatus }}
              </option>
            </select>
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.site')" for="contact-lead-site"></label>
            <select class="form-control" id="contact-lead-site" data-cy="site" name="site" v-model="contactLead.site">
              <option :value="null"></option>
              <option
                :value="contactLead.site && agentSiteOption.id === contactLead.site.id ? contactLead.site : agentSiteOption"
                v-for="agentSiteOption in agentSites"
                :key="agentSiteOption.id"
              >
                {{ agentSiteOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.contactLead.property')" for="contact-lead-property"></label>
            <select class="form-control" id="contact-lead-property" data-cy="property" name="property" v-model="contactLead.property">
              <option :value="null"></option>
              <option
                :value="contactLead.property && propertyOption.id === contactLead.property.id ? contactLead.property : propertyOption"
                v-for="propertyOption in properties"
                :key="propertyOption.id"
              >
                {{ propertyOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./contact-lead-update.component.ts"></script>
