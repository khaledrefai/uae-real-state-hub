<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="propertyMarker">
        <h2 class="jh-entity-heading" data-cy="propertyMarkerDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.propertyMarker.detail.title')"></span> {{ propertyMarker.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.extId')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.extId }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.area')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.area }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.completionDate')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.completionDate }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.latitude')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.latitude }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.longitude')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.longitude }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.name')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.developer')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.developer }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.status')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.status }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.saleStatus')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.saleStatus }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.minPrice')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.minPrice }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.coverUrl')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.coverUrl }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.coverJson')"></span>
          </dt>
          <dd>
            <span>{{ propertyMarker.coverJson }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="propertyMarker.id"
          :to="{ name: 'PropertyMarkerEdit', params: { propertyMarkerId: propertyMarker.id } }"
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

<script lang="ts" src="./property-marker-details.component.ts"></script>
