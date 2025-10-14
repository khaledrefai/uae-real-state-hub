<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="facility">
        <h2 class="jh-entity-heading" data-cy="facilityDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.facility.detail.title')"></span> {{ facility.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.facility.name')"></span>
          </dt>
          <dd>
            <span>{{ facility.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.facility.source')"></span>
          </dt>
          <dd>
            <span>{{ facility.source }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.facility.imageUrl')"></span>
          </dt>
          <dd>
            <span>{{ facility.imageUrl }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.facility.property')"></span>
          </dt>
          <dd>
            <div v-if="facility.property">
              <router-link :to="{ name: 'PropertyView', params: { propertyId: facility.property.id } }">{{
                facility.property.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="facility.id" :to="{ name: 'FacilityEdit', params: { facilityId: facility.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./facility-details.component.ts"></script>
