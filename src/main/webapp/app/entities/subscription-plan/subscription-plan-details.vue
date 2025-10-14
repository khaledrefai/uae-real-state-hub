<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="subscriptionPlan">
        <h2 class="jh-entity-heading" data-cy="subscriptionPlanDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.detail.title')"></span> {{ subscriptionPlan.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.code')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionPlan.code }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.name')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionPlan.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.priceMonthly')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionPlan.priceMonthly }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.priceYearly')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionPlan.priceYearly }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.currency')"></span>
          </dt>
          <dd>
            <span v-text="t$('uaeRealStateHubApp.Currency.' + subscriptionPlan.currency)"></span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.stripePriceMonthlyId')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionPlan.stripePriceMonthlyId }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.stripePriceYearlyId')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionPlan.stripePriceYearlyId }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.maxListings')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionPlan.maxListings }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.subscriptionPlan.features')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionPlan.features }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="subscriptionPlan.id"
          :to="{ name: 'SubscriptionPlanEdit', params: { subscriptionPlanId: subscriptionPlan.id } }"
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

<script lang="ts" src="./subscription-plan-details.component.ts"></script>
