<template>
  <div>
    <b-dropdown-item to="/agent-profile">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.agentProfile')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/agent-site">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.agentSite')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/subscription-plan">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.subscriptionPlan')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/billing-subscription">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.billingSubscription')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/property">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.property')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/property-marker">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.propertyMarker')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/media-asset">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.mediaAsset')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/facility">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.facility')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/map-point">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.mapPoint')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/payment-plan">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.paymentPlan')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/payment-plan-item">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.paymentPlanItem')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/unit-availability">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.unitAvailability')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/unit-block">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.unitBlock')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/contact-lead">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.contactLead')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
