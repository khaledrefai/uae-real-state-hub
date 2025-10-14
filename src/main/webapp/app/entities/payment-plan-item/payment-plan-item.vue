<template>
  <div>
    <h2 id="page-heading" data-cy="PaymentPlanItemHeading">
      <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.home.title')" id="payment-plan-item-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PaymentPlanItemCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-payment-plan-item"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && paymentPlanItems && paymentPlanItems.length === 0">
      <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="paymentPlanItems && paymentPlanItems.length > 0">
      <table class="table table-striped" aria-describedby="paymentPlanItems">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('orderNo')">
              <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.orderNo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'orderNo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('paymentTime')">
              <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.paymentTime')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('percentOfPayment')">
              <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.percentOfPayment')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'percentOfPayment'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('plan.id')">
              <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.plan')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'plan.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="paymentPlanItem in paymentPlanItems" :key="paymentPlanItem.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PaymentPlanItemView', params: { paymentPlanItemId: paymentPlanItem.id } }">{{
                paymentPlanItem.id
              }}</router-link>
            </td>
            <td>{{ paymentPlanItem.orderNo }}</td>
            <td>{{ paymentPlanItem.paymentTime }}</td>
            <td>{{ paymentPlanItem.percentOfPayment }}</td>
            <td>
              <div v-if="paymentPlanItem.plan">
                <router-link :to="{ name: 'PaymentPlanView', params: { paymentPlanId: paymentPlanItem.plan.id } }">{{
                  paymentPlanItem.plan.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PaymentPlanItemView', params: { paymentPlanItemId: paymentPlanItem.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PaymentPlanItemEdit', params: { paymentPlanItemId: paymentPlanItem.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(paymentPlanItem)"
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
          id="uaeRealStateHubApp.paymentPlanItem.delete.question"
          data-cy="paymentPlanItemDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-paymentPlanItem-heading" v-text="t$('uaeRealStateHubApp.paymentPlanItem.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-paymentPlanItem"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removePaymentPlanItem()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="paymentPlanItems && paymentPlanItems.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./payment-plan-item.component.ts"></script>
