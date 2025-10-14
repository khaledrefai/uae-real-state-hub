<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="contactLead">
        <h2 class="jh-entity-heading" data-cy="contactLeadDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.contactLead.detail.title')"></span> {{ contactLead.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.name')"></span>
          </dt>
          <dd>
            <span>{{ contactLead.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.email')"></span>
          </dt>
          <dd>
            <span>{{ contactLead.email }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.phone')"></span>
          </dt>
          <dd>
            <span>{{ contactLead.phone }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.whatsapp')"></span>
          </dt>
          <dd>
            <span>{{ contactLead.whatsapp }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.message')"></span>
          </dt>
          <dd>
            <span>{{ contactLead.message }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.source')"></span>
          </dt>
          <dd>
            <span>{{ contactLead.source }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.utm')"></span>
          </dt>
          <dd>
            <span>{{ contactLead.utm }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.createdAt')"></span>
          </dt>
          <dd>
            <span v-if="contactLead.createdAt">{{ formatDateLong(contactLead.createdAt) }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.status')"></span>
          </dt>
          <dd>
            <span v-text="t$('uaeRealStateHubApp.LeadStatus.' + contactLead.status)"></span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.site')"></span>
          </dt>
          <dd>
            <div v-if="contactLead.site">
              <router-link :to="{ name: 'AgentSiteView', params: { agentSiteId: contactLead.site.id } }">{{
                contactLead.site.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.contactLead.property')"></span>
          </dt>
          <dd>
            <div v-if="contactLead.property">
              <router-link :to="{ name: 'PropertyView', params: { propertyId: contactLead.property.id } }">{{
                contactLead.property.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="contactLead.id"
          :to="{ name: 'ContactLeadEdit', params: { contactLeadId: contactLead.id } }"
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

<script lang="ts" src="./contact-lead-details.component.ts"></script>
