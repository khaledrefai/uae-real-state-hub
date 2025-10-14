<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="billingSubscription">
        <h2 class="jh-entity-heading" data-cy="billingSubscriptionDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.billingSubscription.detail.title')"></span> {{ billingSubscription.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.billingSubscription.status')"></span>
          </dt>
          <dd>
            <span>{{ billingSubscription.status }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.billingSubscription.startDate')"></span>
          </dt>
          <dd>
            <span v-if="billingSubscription.startDate">{{ formatDateLong(billingSubscription.startDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.billingSubscription.endDate')"></span>
          </dt>
          <dd>
            <span v-if="billingSubscription.endDate">{{ formatDateLong(billingSubscription.endDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.billingSubscription.stripeCustomerId')"></span>
          </dt>
          <dd>
            <span>{{ billingSubscription.stripeCustomerId }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.billingSubscription.stripeSubscriptionId')"></span>
          </dt>
          <dd>
            <span>{{ billingSubscription.stripeSubscriptionId }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.billingSubscription.cancelAtPeriodEnd')"></span>
          </dt>
          <dd>
            <span>{{ billingSubscription.cancelAtPeriodEnd }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.billingSubscription.isActive')"></span>
          </dt>
          <dd>
            <span>{{ billingSubscription.isActive }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.billingSubscription.site')"></span>
          </dt>
          <dd>
            <div v-if="billingSubscription.site">
              <router-link :to="{ name: 'AgentSiteView', params: { agentSiteId: billingSubscription.site.id } }">{{
                billingSubscription.site.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="billingSubscription.id"
          :to="{ name: 'BillingSubscriptionEdit', params: { billingSubscriptionId: billingSubscription.id } }"
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

<script lang="ts" src="./billing-subscription-details.component.ts"></script>
