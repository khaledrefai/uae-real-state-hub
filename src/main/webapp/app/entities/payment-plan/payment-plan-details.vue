<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="paymentPlan">
        <h2 class="jh-entity-heading" data-cy="paymentPlanDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.paymentPlan.detail.title')"></span> {{ paymentPlan.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.paymentPlan.name')"></span>
          </dt>
          <dd>
            <span>{{ paymentPlan.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.paymentPlan.monthsAfterHandover')"></span>
          </dt>
          <dd>
            <span>{{ paymentPlan.monthsAfterHandover }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.paymentPlan.property')"></span>
          </dt>
          <dd>
            <div v-if="paymentPlan.property">
              <router-link :to="{ name: 'PropertyView', params: { propertyId: paymentPlan.property.id } }">{{
                paymentPlan.property.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="paymentPlan.id"
          :to="{ name: 'PaymentPlanEdit', params: { paymentPlanId: paymentPlan.id } }"
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

<script lang="ts" src="./payment-plan-details.component.ts"></script>
