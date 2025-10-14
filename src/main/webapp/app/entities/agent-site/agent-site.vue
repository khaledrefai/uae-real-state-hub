<template>
  <div>
    <h2 id="page-heading" data-cy="AgentSiteHeading">
      <span v-text="t$('uaeRealStateHubApp.agentSite.home.title')" id="agent-site-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.agentSite.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'AgentSiteCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-agent-site"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.agentSite.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && agentSites && agentSites.length === 0">
      <span v-text="t$('uaeRealStateHubApp.agentSite.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="agentSites && agentSites.length > 0">
      <table class="table table-striped" aria-describedby="agentSites">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('slug')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.slug')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'slug'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('displayName')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.displayName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'displayName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('theme')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.theme')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'theme'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('primaryColor')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.primaryColor')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'primaryColor'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('secondaryColor')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.secondaryColor')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'secondaryColor'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('logoUrl')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.logoUrl')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'logoUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('contactEmail')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.contactEmail')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'contactEmail'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('contactPhone')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.contactPhone')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'contactPhone'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('contactWhatsapp')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.contactWhatsapp')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'contactWhatsapp'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('domain')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.domain')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'domain'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isActive')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.isActive')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isActive'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('owner.id')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.owner')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'owner.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('plan.id')">
              <span v-text="t$('uaeRealStateHubApp.agentSite.plan')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'plan.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="agentSite in agentSites" :key="agentSite.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AgentSiteView', params: { agentSiteId: agentSite.id } }">{{ agentSite.id }}</router-link>
            </td>
            <td>{{ agentSite.slug }}</td>
            <td>{{ agentSite.displayName }}</td>
            <td>{{ agentSite.theme }}</td>
            <td>{{ agentSite.primaryColor }}</td>
            <td>{{ agentSite.secondaryColor }}</td>
            <td>{{ agentSite.logoUrl }}</td>
            <td>{{ agentSite.contactEmail }}</td>
            <td>{{ agentSite.contactPhone }}</td>
            <td>{{ agentSite.contactWhatsapp }}</td>
            <td>{{ agentSite.domain }}</td>
            <td>{{ agentSite.isActive }}</td>
            <td>
              <div v-if="agentSite.owner">
                <router-link :to="{ name: 'AgentProfileView', params: { agentProfileId: agentSite.owner.id } }">{{
                  agentSite.owner.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="agentSite.plan">
                <router-link :to="{ name: 'SubscriptionPlanView', params: { subscriptionPlanId: agentSite.plan.id } }">{{
                  agentSite.plan.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AgentSiteView', params: { agentSiteId: agentSite.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AgentSiteEdit', params: { agentSiteId: agentSite.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(agentSite)"
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
          id="uaeRealStateHubApp.agentSite.delete.question"
          data-cy="agentSiteDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-agentSite-heading" v-text="t$('uaeRealStateHubApp.agentSite.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-agentSite"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeAgentSite()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="agentSites && agentSites.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./agent-site.component.ts"></script>
