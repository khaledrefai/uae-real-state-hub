<template>
  <div>
    <h2 id="page-heading" data-cy="AgentProfileHeading">
      <span v-text="t$('uaeRealStateHubApp.agentProfile.home.title')" id="agent-profile-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.agentProfile.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'AgentProfileCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-agent-profile"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && agentProfiles && agentProfiles.length === 0">
      <span v-text="t$('uaeRealStateHubApp.agentProfile.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="agentProfiles && agentProfiles.length > 0">
      <table class="table table-striped" aria-describedby="agentProfiles">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('externalUserId')">
              <span v-text="t$('uaeRealStateHubApp.agentProfile.externalUserId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'externalUserId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fullName')">
              <span v-text="t$('uaeRealStateHubApp.agentProfile.fullName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fullName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('companyName')">
              <span v-text="t$('uaeRealStateHubApp.agentProfile.companyName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'companyName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('email')">
              <span v-text="t$('uaeRealStateHubApp.agentProfile.email')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('phone')">
              <span v-text="t$('uaeRealStateHubApp.agentProfile.phone')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'phone'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('whatsapp')">
              <span v-text="t$('uaeRealStateHubApp.agentProfile.whatsapp')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'whatsapp'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('country')">
              <span v-text="t$('uaeRealStateHubApp.agentProfile.country')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'country'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('city')">
              <span v-text="t$('uaeRealStateHubApp.agentProfile.city')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'city'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('website')">
              <span v-text="t$('uaeRealStateHubApp.agentProfile.website')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'website'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('active')">
              <span v-text="t$('uaeRealStateHubApp.agentProfile.active')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'active'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="agentProfile in agentProfiles" :key="agentProfile.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AgentProfileView', params: { agentProfileId: agentProfile.id } }">{{
                agentProfile.id
              }}</router-link>
            </td>
            <td>{{ agentProfile.externalUserId }}</td>
            <td>{{ agentProfile.fullName }}</td>
            <td>{{ agentProfile.companyName }}</td>
            <td>{{ agentProfile.email }}</td>
            <td>{{ agentProfile.phone }}</td>
            <td>{{ agentProfile.whatsapp }}</td>
            <td>{{ agentProfile.country }}</td>
            <td>{{ agentProfile.city }}</td>
            <td>{{ agentProfile.website }}</td>
            <td>{{ agentProfile.active }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AgentProfileView', params: { agentProfileId: agentProfile.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AgentProfileEdit', params: { agentProfileId: agentProfile.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(agentProfile)"
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
          id="uaeRealStateHubApp.agentProfile.delete.question"
          data-cy="agentProfileDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-agentProfile-heading" v-text="t$('uaeRealStateHubApp.agentProfile.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-agentProfile"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeAgentProfile()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="agentProfiles && agentProfiles.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./agent-profile.component.ts"></script>
