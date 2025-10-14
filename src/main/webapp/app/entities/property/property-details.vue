<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="property">
        <h2 class="jh-entity-heading" data-cy="propertyDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.property.detail.title')"></span> {{ property.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.extId')"></span>
          </dt>
          <dd>
            <span>{{ property.extId }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.slug')"></span>
          </dt>
          <dd>
            <span>{{ property.slug }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.name')"></span>
          </dt>
          <dd>
            <span>{{ property.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.developer')"></span>
          </dt>
          <dd>
            <span>{{ property.developer }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.area')"></span>
          </dt>
          <dd>
            <span>{{ property.area }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.city')"></span>
          </dt>
          <dd>
            <span>{{ property.city }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.country')"></span>
          </dt>
          <dd>
            <span>{{ property.country }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.listingType')"></span>
          </dt>
          <dd>
            <span v-text="t$('uaeRealStateHubApp.ListingType.' + property.listingType)"></span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.status')"></span>
          </dt>
          <dd>
            <span v-text="t$('uaeRealStateHubApp.PropertyStatus.' + property.status)"></span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.saleStatus')"></span>
          </dt>
          <dd>
            <span>{{ property.saleStatus }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.readiness')"></span>
          </dt>
          <dd>
            <span>{{ property.readiness }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.serviceCharge')"></span>
          </dt>
          <dd>
            <span>{{ property.serviceCharge }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.furnishing')"></span>
          </dt>
          <dd>
            <span>{{ property.furnishing }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.hasEscrow')"></span>
          </dt>
          <dd>
            <span>{{ property.hasEscrow }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.postHandover')"></span>
          </dt>
          <dd>
            <span>{{ property.postHandover }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.completionDateTime')"></span>
          </dt>
          <dd>
            <span v-if="property.completionDateTime">{{ formatDateLong(property.completionDateTime) }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.minPrice')"></span>
          </dt>
          <dd>
            <span>{{ property.minPrice }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.maxPrice')"></span>
          </dt>
          <dd>
            <span>{{ property.maxPrice }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.minPriceAed')"></span>
          </dt>
          <dd>
            <span>{{ property.minPriceAed }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.priceCurrency')"></span>
          </dt>
          <dd>
            <span>{{ property.priceCurrency }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.minArea')"></span>
          </dt>
          <dd>
            <span>{{ property.minArea }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.maxArea')"></span>
          </dt>
          <dd>
            <span>{{ property.maxArea }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.areaUnit')"></span>
          </dt>
          <dd>
            <span>{{ property.areaUnit }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.latitude')"></span>
          </dt>
          <dd>
            <span>{{ property.latitude }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.longitude')"></span>
          </dt>
          <dd>
            <span>{{ property.longitude }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.coverUrl')"></span>
          </dt>
          <dd>
            <span>{{ property.coverUrl }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.coverJson')"></span>
          </dt>
          <dd>
            <span>{{ property.coverJson }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.videoUrl')"></span>
          </dt>
          <dd>
            <span>{{ property.videoUrl }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.brochureUrl')"></span>
          </dt>
          <dd>
            <span>{{ property.brochureUrl }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.layoutsPdfUrl')"></span>
          </dt>
          <dd>
            <span>{{ property.layoutsPdfUrl }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.website')"></span>
          </dt>
          <dd>
            <span>{{ property.website }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.overviewMd')"></span>
          </dt>
          <dd>
            <span>{{ property.overviewMd }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.raw')"></span>
          </dt>
          <dd>
            <span>{{ property.raw }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.publishedAt')"></span>
          </dt>
          <dd>
            <span v-if="property.publishedAt">{{ formatDateLong(property.publishedAt) }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.updatedAt')"></span>
          </dt>
          <dd>
            <span v-if="property.updatedAt">{{ formatDateLong(property.updatedAt) }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.property.marker')"></span>
          </dt>
          <dd>
            <div v-if="property.marker">
              <router-link :to="{ name: 'PropertyMarkerView', params: { propertyMarkerId: property.marker.id } }">{{
                property.marker.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="property.id" :to="{ name: 'PropertyEdit', params: { propertyId: property.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./property-details.component.ts"></script>
