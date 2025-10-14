<template>
  <div>
    <h2 id="page-heading" data-cy="ContactLeadHeading">
      <span v-text="t$('uaeRealStateHubApp.contactLead.home.title')" id="contact-lead-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.contactLead.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ContactLeadCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-contact-lead"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.contactLead.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && contactLeads && contactLeads.length === 0">
      <span v-text="t$('uaeRealStateHubApp.contactLead.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="contactLeads && contactLeads.length > 0">
      <table class="table table-striped" aria-describedby="contactLeads">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('email')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.email')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('phone')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.phone')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'phone'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('whatsapp')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.whatsapp')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'whatsapp'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('message')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.message')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'message'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('source')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.source')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'source'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('utm')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.utm')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'utm'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('createdAt')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.createdAt')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.status')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('site.id')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.site')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'site.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('property.id')">
              <span v-text="t$('uaeRealStateHubApp.contactLead.property')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'property.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="contactLead in contactLeads" :key="contactLead.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ContactLeadView', params: { contactLeadId: contactLead.id } }">{{ contactLead.id }}</router-link>
            </td>
            <td>{{ contactLead.name }}</td>
            <td>{{ contactLead.email }}</td>
            <td>{{ contactLead.phone }}</td>
            <td>{{ contactLead.whatsapp }}</td>
            <td>{{ contactLead.message }}</td>
            <td>{{ contactLead.source }}</td>
            <td>{{ contactLead.utm }}</td>
            <td>{{ formatDateShort(contactLead.createdAt) || '' }}</td>
            <td v-text="t$('uaeRealStateHubApp.LeadStatus.' + contactLead.status)"></td>
            <td>
              <div v-if="contactLead.site">
                <router-link :to="{ name: 'AgentSiteView', params: { agentSiteId: contactLead.site.id } }">{{
                  contactLead.site.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="contactLead.property">
                <router-link :to="{ name: 'PropertyView', params: { propertyId: contactLead.property.id } }">{{
                  contactLead.property.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ContactLeadView', params: { contactLeadId: contactLead.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ContactLeadEdit', params: { contactLeadId: contactLead.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(contactLead)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="uaeRealStateHubApp.contactLead.delete.question"
          data-cy="contactLeadDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-contactLead-heading" v-text="t$('uaeRealStateHubApp.contactLead.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-contactLead"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeContactLead()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="contactLeads && contactLeads.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./contact-lead.component.ts"></script>
