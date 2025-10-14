<template>
  <div>
    <h2 id="page-heading" data-cy="BillingSubscriptionHeading">
      <span v-text="t$('uaeRealStateHubApp.billingSubscription.home.title')" id="billing-subscription-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.billingSubscription.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'BillingSubscriptionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-billing-subscription"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.billingSubscription.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && billingSubscriptions && billingSubscriptions.length === 0">
      <span v-text="t$('uaeRealStateHubApp.billingSubscription.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="billingSubscriptions && billingSubscriptions.length > 0">
      <table class="table table-striped" aria-describedby="billingSubscriptions">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status')">
              <span v-text="t$('uaeRealStateHubApp.billingSubscription.status')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('startDate')">
              <span v-text="t$('uaeRealStateHubApp.billingSubscription.startDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('endDate')">
              <span v-text="t$('uaeRealStateHubApp.billingSubscription.endDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'endDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('stripeCustomerId')">
              <span v-text="t$('uaeRealStateHubApp.billingSubscription.stripeCustomerId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'stripeCustomerId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('stripeSubscriptionId')">
              <span v-text="t$('uaeRealStateHubApp.billingSubscription.stripeSubscriptionId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'stripeSubscriptionId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('cancelAtPeriodEnd')">
              <span v-text="t$('uaeRealStateHubApp.billingSubscription.cancelAtPeriodEnd')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cancelAtPeriodEnd'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isActive')">
              <span v-text="t$('uaeRealStateHubApp.billingSubscription.isActive')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isActive'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('site.id')">
              <span v-text="t$('uaeRealStateHubApp.billingSubscription.site')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'site.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="billingSubscription in billingSubscriptions" :key="billingSubscription.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'BillingSubscriptionView', params: { billingSubscriptionId: billingSubscription.id } }">{{
                billingSubscription.id
              }}</router-link>
            </td>
            <td>{{ billingSubscription.status }}</td>
            <td>{{ formatDateShort(billingSubscription.startDate) || '' }}</td>
            <td>{{ formatDateShort(billingSubscription.endDate) || '' }}</td>
            <td>{{ billingSubscription.stripeCustomerId }}</td>
            <td>{{ billingSubscription.stripeSubscriptionId }}</td>
            <td>{{ billingSubscription.cancelAtPeriodEnd }}</td>
            <td>{{ billingSubscription.isActive }}</td>
            <td>
              <div v-if="billingSubscription.site">
                <router-link :to="{ name: 'AgentSiteView', params: { agentSiteId: billingSubscription.site.id } }">{{
                  billingSubscription.site.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'BillingSubscriptionView', params: { billingSubscriptionId: billingSubscription.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'BillingSubscriptionEdit', params: { billingSubscriptionId: billingSubscription.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(billingSubscription)"
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
          id="uaeRealStateHubApp.billingSubscription.delete.question"
          data-cy="billingSubscriptionDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-billingSubscription-heading"
          v-text="t$('uaeRealStateHubApp.billingSubscription.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-billingSubscription"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeBillingSubscription()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="billingSubscriptions && billingSubscriptions.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./billing-subscription.component.ts"></script>
