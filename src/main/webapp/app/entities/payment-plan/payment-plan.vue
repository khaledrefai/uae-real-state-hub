<template>
  <div>
    <h2 id="page-heading" data-cy="PaymentPlanHeading">
      <span v-text="t$('uaeRealStateHubApp.paymentPlan.home.title')" id="payment-plan-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.paymentPlan.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PaymentPlanCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-payment-plan"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.paymentPlan.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && paymentPlans && paymentPlans.length === 0">
      <span v-text="t$('uaeRealStateHubApp.paymentPlan.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="paymentPlans && paymentPlans.length > 0">
      <table class="table table-striped" aria-describedby="paymentPlans">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('uaeRealStateHubApp.paymentPlan.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('monthsAfterHandover')">
              <span v-text="t$('uaeRealStateHubApp.paymentPlan.monthsAfterHandover')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'monthsAfterHandover'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('property.id')">
              <span v-text="t$('uaeRealStateHubApp.paymentPlan.property')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'property.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="paymentPlan in paymentPlans" :key="paymentPlan.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PaymentPlanView', params: { paymentPlanId: paymentPlan.id } }">{{ paymentPlan.id }}</router-link>
            </td>
            <td>{{ paymentPlan.name }}</td>
            <td>{{ paymentPlan.monthsAfterHandover }}</td>
            <td>
              <div v-if="paymentPlan.property">
                <router-link :to="{ name: 'PropertyView', params: { propertyId: paymentPlan.property.id } }">{{
                  paymentPlan.property.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PaymentPlanView', params: { paymentPlanId: paymentPlan.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PaymentPlanEdit', params: { paymentPlanId: paymentPlan.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(paymentPlan)"
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
          id="uaeRealStateHubApp.paymentPlan.delete.question"
          data-cy="paymentPlanDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-paymentPlan-heading" v-text="t$('uaeRealStateHubApp.paymentPlan.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-paymentPlan"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removePaymentPlan()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="paymentPlans && paymentPlans.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./payment-plan.component.ts"></script>
