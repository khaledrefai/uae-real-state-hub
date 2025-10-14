<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="mapPoint">
        <h2 class="jh-entity-heading" data-cy="mapPointDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.mapPoint.detail.title')"></span> {{ mapPoint.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mapPoint.name')"></span>
          </dt>
          <dd>
            <span>{{ mapPoint.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mapPoint.distanceKm')"></span>
          </dt>
          <dd>
            <span>{{ mapPoint.distanceKm }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mapPoint.property')"></span>
          </dt>
          <dd>
            <div v-if="mapPoint.property">
              <router-link :to="{ name: 'PropertyView', params: { propertyId: mapPoint.property.id } }">{{
                mapPoint.property.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="mapPoint.id" :to="{ name: 'MapPointEdit', params: { mapPointId: mapPoint.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./map-point-details.component.ts"></script>
