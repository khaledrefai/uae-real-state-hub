<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="paymentPlanItem">
        <h2 class="jh-entity-heading" data-cy="paymentPlanItemDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.detail.title')"></span> {{ paymentPlanItem.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.orderNo')"></span>
          </dt>
          <dd>
            <span>{{ paymentPlanItem.orderNo }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.paymentTime')"></span>
          </dt>
          <dd>
            <span>{{ paymentPlanItem.paymentTime }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.percentOfPayment')"></span>
          </dt>
          <dd>
            <span>{{ paymentPlanItem.percentOfPayment }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.paymentPlanItem.plan')"></span>
          </dt>
          <dd>
            <div v-if="paymentPlanItem.plan">
              <router-link :to="{ name: 'PaymentPlanView', params: { paymentPlanId: paymentPlanItem.plan.id } }">{{
                paymentPlanItem.plan.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="paymentPlanItem.id"
          :to="{ name: 'PaymentPlanItemEdit', params: { paymentPlanItemId: paymentPlanItem.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./payment-plan-item-details.component.ts"></script>
