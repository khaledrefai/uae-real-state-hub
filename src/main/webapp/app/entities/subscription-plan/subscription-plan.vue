<template>
  <div>
    <h2 id="page-heading" data-cy="SubscriptionPlanHeading">
      <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.home.title')" id="subscription-plan-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SubscriptionPlanCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-subscription-plan"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && subscriptionPlans && subscriptionPlans.length === 0">
      <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="subscriptionPlans && subscriptionPlans.length > 0">
      <table class="table table-striped" aria-describedby="subscriptionPlans">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('code')">
              <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.code')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'code'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('priceMonthly')">
              <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.priceMonthly')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priceMonthly'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('priceYearly')">
              <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.priceYearly')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priceYearly'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('currency')">
              <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.currency')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'currency'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('stripePriceMonthlyId')">
              <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.stripePriceMonthlyId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'stripePriceMonthlyId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('stripePriceYearlyId')">
              <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.stripePriceYearlyId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'stripePriceYearlyId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxListings')">
              <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.maxListings')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxListings'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('features')">
              <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.features')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'features'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="subscriptionPlan in subscriptionPlans" :key="subscriptionPlan.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SubscriptionPlanView', params: { subscriptionPlanId: subscriptionPlan.id } }">{{
                subscriptionPlan.id
              }}</router-link>
            </td>
            <td>{{ subscriptionPlan.code }}</td>
            <td>{{ subscriptionPlan.name }}</td>
            <td>{{ subscriptionPlan.priceMonthly }}</td>
            <td>{{ subscriptionPlan.priceYearly }}</td>
            <td v-text="t$('uaeRealStateHubApp.Currency.' + subscriptionPlan.currency)"></td>
            <td>{{ subscriptionPlan.stripePriceMonthlyId }}</td>
            <td>{{ subscriptionPlan.stripePriceYearlyId }}</td>
            <td>{{ subscriptionPlan.maxListings }}</td>
            <td>{{ subscriptionPlan.features }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SubscriptionPlanView', params: { subscriptionPlanId: subscriptionPlan.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SubscriptionPlanEdit', params: { subscriptionPlanId: subscriptionPlan.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(subscriptionPlan)"
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
          id="uaeRealStateHubApp.subscriptionPlan.delete.question"
          data-cy="subscriptionPlanDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-subscriptionPlan-heading"
          v-text="t$('uaeRealStateHubApp.subscriptionPlan.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-subscriptionPlan"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeSubscriptionPlan()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="subscriptionPlans && subscriptionPlans.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./subscription-plan.component.ts"></script>
