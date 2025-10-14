<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="agentSite">
        <h2 class="jh-entity-heading" data-cy="agentSiteDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.agentSite.detail.title')"></span> {{ agentSite.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.slug')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.slug }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.displayName')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.displayName }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.theme')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.theme }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.primaryColor')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.primaryColor }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.secondaryColor')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.secondaryColor }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.logoUrl')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.logoUrl }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.contactEmail')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.contactEmail }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.contactPhone')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.contactPhone }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.contactWhatsapp')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.contactWhatsapp }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.domain')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.domain }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.isActive')"></span>
          </dt>
          <dd>
            <span>{{ agentSite.isActive }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.owner')"></span>
          </dt>
          <dd>
            <div v-if="agentSite.owner">
              <router-link :to="{ name: 'AgentProfileView', params: { agentProfileId: agentSite.owner.id } }">{{
                agentSite.owner.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentSite.plan')"></span>
          </dt>
          <dd>
            <div v-if="agentSite.plan">
              <router-link :to="{ name: 'SubscriptionPlanView', params: { subscriptionPlanId: agentSite.plan.id } }">{{
                agentSite.plan.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="agentSite.id"
          :to="{ name: 'AgentSiteEdit', params: { agentSiteId: agentSite.id } }"
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

<script lang="ts" src="./agent-site-details.component.ts"></script>
