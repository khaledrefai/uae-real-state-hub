<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="agentProfile">
        <h2 class="jh-entity-heading" data-cy="agentProfileDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.agentProfile.detail.title')"></span> {{ agentProfile.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.externalUserId')"></span>
          </dt>
          <dd>
            <span>{{ agentProfile.externalUserId }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.fullName')"></span>
          </dt>
          <dd>
            <span>{{ agentProfile.fullName }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.companyName')"></span>
          </dt>
          <dd>
            <span>{{ agentProfile.companyName }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.email')"></span>
          </dt>
          <dd>
            <span>{{ agentProfile.email }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.phone')"></span>
          </dt>
          <dd>
            <span>{{ agentProfile.phone }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.whatsapp')"></span>
          </dt>
          <dd>
            <span>{{ agentProfile.whatsapp }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.country')"></span>
          </dt>
          <dd>
            <span>{{ agentProfile.country }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.city')"></span>
          </dt>
          <dd>
            <span>{{ agentProfile.city }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.website')"></span>
          </dt>
          <dd>
            <span>{{ agentProfile.website }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.agentProfile.active')"></span>
          </dt>
          <dd>
            <span>{{ agentProfile.active }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="agentProfile.id"
          :to="{ name: 'AgentProfileEdit', params: { agentProfileId: agentProfile.id } }"
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

<script lang="ts" src="./agent-profile-details.component.ts"></script>
