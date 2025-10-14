<template>
  <div>
    <h2 id="page-heading" data-cy="PropertyHeading">
      <span v-text="t$('uaeRealStateHubApp.property.home.title')" id="property-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.property.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PropertyCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-property"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.property.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && properties && properties.length === 0">
      <span v-text="t$('uaeRealStateHubApp.property.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="properties && properties.length > 0">
      <table class="table table-striped" aria-describedby="properties">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('extId')">
              <span v-text="t$('uaeRealStateHubApp.property.extId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'extId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('slug')">
              <span v-text="t$('uaeRealStateHubApp.property.slug')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'slug'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('uaeRealStateHubApp.property.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('developer')">
              <span v-text="t$('uaeRealStateHubApp.property.developer')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'developer'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('area')">
              <span v-text="t$('uaeRealStateHubApp.property.area')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'area'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('city')">
              <span v-text="t$('uaeRealStateHubApp.property.city')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'city'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('country')">
              <span v-text="t$('uaeRealStateHubApp.property.country')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'country'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('listingType')">
              <span v-text="t$('uaeRealStateHubApp.property.listingType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'listingType'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status')">
              <span v-text="t$('uaeRealStateHubApp.property.status')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('saleStatus')">
              <span v-text="t$('uaeRealStateHubApp.property.saleStatus')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'saleStatus'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('readiness')">
              <span v-text="t$('uaeRealStateHubApp.property.readiness')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'readiness'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('serviceCharge')">
              <span v-text="t$('uaeRealStateHubApp.property.serviceCharge')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'serviceCharge'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('furnishing')">
              <span v-text="t$('uaeRealStateHubApp.property.furnishing')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'furnishing'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('hasEscrow')">
              <span v-text="t$('uaeRealStateHubApp.property.hasEscrow')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'hasEscrow'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('postHandover')">
              <span v-text="t$('uaeRealStateHubApp.property.postHandover')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'postHandover'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('completionDateTime')">
              <span v-text="t$('uaeRealStateHubApp.property.completionDateTime')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'completionDateTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('minPrice')">
              <span v-text="t$('uaeRealStateHubApp.property.minPrice')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'minPrice'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxPrice')">
              <span v-text="t$('uaeRealStateHubApp.property.maxPrice')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxPrice'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('minPriceAed')">
              <span v-text="t$('uaeRealStateHubApp.property.minPriceAed')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'minPriceAed'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('priceCurrency')">
              <span v-text="t$('uaeRealStateHubApp.property.priceCurrency')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priceCurrency'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('minArea')">
              <span v-text="t$('uaeRealStateHubApp.property.minArea')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'minArea'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxArea')">
              <span v-text="t$('uaeRealStateHubApp.property.maxArea')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxArea'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('areaUnit')">
              <span v-text="t$('uaeRealStateHubApp.property.areaUnit')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'areaUnit'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('latitude')">
              <span v-text="t$('uaeRealStateHubApp.property.latitude')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'latitude'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('longitude')">
              <span v-text="t$('uaeRealStateHubApp.property.longitude')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'longitude'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('coverUrl')">
              <span v-text="t$('uaeRealStateHubApp.property.coverUrl')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'coverUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('coverJson')">
              <span v-text="t$('uaeRealStateHubApp.property.coverJson')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'coverJson'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('videoUrl')">
              <span v-text="t$('uaeRealStateHubApp.property.videoUrl')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'videoUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('brochureUrl')">
              <span v-text="t$('uaeRealStateHubApp.property.brochureUrl')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'brochureUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('layoutsPdfUrl')">
              <span v-text="t$('uaeRealStateHubApp.property.layoutsPdfUrl')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'layoutsPdfUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('website')">
              <span v-text="t$('uaeRealStateHubApp.property.website')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'website'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('overviewMd')">
              <span v-text="t$('uaeRealStateHubApp.property.overviewMd')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'overviewMd'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('raw')">
              <span v-text="t$('uaeRealStateHubApp.property.raw')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'raw'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('publishedAt')">
              <span v-text="t$('uaeRealStateHubApp.property.publishedAt')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'publishedAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('updatedAt')">
              <span v-text="t$('uaeRealStateHubApp.property.updatedAt')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'updatedAt'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('marker.id')">
              <span v-text="t$('uaeRealStateHubApp.property.marker')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'marker.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="property in properties" :key="property.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PropertyView', params: { propertyId: property.id } }">{{ property.id }}</router-link>
            </td>
            <td>{{ property.extId }}</td>
            <td>{{ property.slug }}</td>
            <td>{{ property.name }}</td>
            <td>{{ property.developer }}</td>
            <td>{{ property.area }}</td>
            <td>{{ property.city }}</td>
            <td>{{ property.country }}</td>
            <td v-text="t$('uaeRealStateHubApp.ListingType.' + property.listingType)"></td>
            <td v-text="t$('uaeRealStateHubApp.PropertyStatus.' + property.status)"></td>
            <td>{{ property.saleStatus }}</td>
            <td>{{ property.readiness }}</td>
            <td>{{ property.serviceCharge }}</td>
            <td>{{ property.furnishing }}</td>
            <td>{{ property.hasEscrow }}</td>
            <td>{{ property.postHandover }}</td>
            <td>{{ formatDateShort(property.completionDateTime) || '' }}</td>
            <td>{{ property.minPrice }}</td>
            <td>{{ property.maxPrice }}</td>
            <td>{{ property.minPriceAed }}</td>
            <td>{{ property.priceCurrency }}</td>
            <td>{{ property.minArea }}</td>
            <td>{{ property.maxArea }}</td>
            <td>{{ property.areaUnit }}</td>
            <td>{{ property.latitude }}</td>
            <td>{{ property.longitude }}</td>
            <td>{{ property.coverUrl }}</td>
            <td>{{ property.coverJson }}</td>
            <td>{{ property.videoUrl }}</td>
            <td>{{ property.brochureUrl }}</td>
            <td>{{ property.layoutsPdfUrl }}</td>
            <td>{{ property.website }}</td>
            <td>{{ property.overviewMd }}</td>
            <td>{{ property.raw }}</td>
            <td>{{ formatDateShort(property.publishedAt) || '' }}</td>
            <td>{{ formatDateShort(property.updatedAt) || '' }}</td>
            <td>
              <div v-if="property.marker">
                <router-link :to="{ name: 'PropertyMarkerView', params: { propertyMarkerId: property.marker.id } }">{{
                  property.marker.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PropertyView', params: { propertyId: property.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PropertyEdit', params: { propertyId: property.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(property)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="uaeRealStateHubApp.property.delete.question"
          data-cy="propertyDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-property-heading" v-text="t$('uaeRealStateHubApp.property.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-property"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeProperty()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="properties && properties.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./property.component.ts"></script>
