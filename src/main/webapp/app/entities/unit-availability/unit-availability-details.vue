<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="unitAvailability">
        <h2 class="jh-entity-heading" data-cy="unitAvailabilityDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.unitAvailability.detail.title')"></span> {{ unitAvailability.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.buildingName')"></span>
          </dt>
          <dd>
            <span>{{ unitAvailability.buildingName }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.areaFrom')"></span>
          </dt>
          <dd>
            <span>{{ unitAvailability.areaFrom }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.areaUnit')"></span>
          </dt>
          <dd>
            <span>{{ unitAvailability.areaUnit }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.bedroomType')"></span>
          </dt>
          <dd>
            <span>{{ unitAvailability.bedroomType }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.lastUpdated')"></span>
          </dt>
          <dd>
            <span v-if="unitAvailability.lastUpdated">{{ formatDateLong(unitAvailability.lastUpdated) }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.priceCurrency')"></span>
          </dt>
          <dd>
            <span>{{ unitAvailability.priceCurrency }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.priceFrom')"></span>
          </dt>
          <dd>
            <span>{{ unitAvailability.priceFrom }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.priceTo')"></span>
          </dt>
          <dd>
            <span>{{ unitAvailability.priceTo }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.unitsAvailable')"></span>
          </dt>
          <dd>
            <span>{{ unitAvailability.unitsAvailable }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.property')"></span>
          </dt>
          <dd>
            <div v-if="unitAvailability.property">
              <router-link :to="{ name: 'PropertyView', params: { propertyId: unitAvailability.property.id } }">{{
                unitAvailability.property.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="unitAvailability.id"
          :to="{ name: 'UnitAvailabilityEdit', params: { unitAvailabilityId: unitAvailability.id } }"
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

<script lang="ts" src="./unit-availability-details.component.ts"></script>
