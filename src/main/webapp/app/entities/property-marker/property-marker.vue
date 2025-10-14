<template>
  <div>
    <h2 id="page-heading" data-cy="PropertyMarkerHeading">
      <span v-text="t$('uaeRealStateHubApp.propertyMarker.home.title')" id="property-marker-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.propertyMarker.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PropertyMarkerCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-property-marker"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.propertyMarker.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && propertyMarkers && propertyMarkers.length === 0">
      <span v-text="t$('uaeRealStateHubApp.propertyMarker.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="propertyMarkers && propertyMarkers.length > 0">
      <table class="table table-striped" aria-describedby="propertyMarkers">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('extId')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.extId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'extId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('area')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.area')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'area'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('completionDate')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.completionDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'completionDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('latitude')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.latitude')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'latitude'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('longitude')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.longitude')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'longitude'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('developer')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.developer')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'developer'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.status')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('saleStatus')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.saleStatus')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'saleStatus'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('minPrice')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.minPrice')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'minPrice'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('coverUrl')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.coverUrl')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'coverUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('coverJson')">
              <span v-text="t$('uaeRealStateHubApp.propertyMarker.coverJson')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'coverJson'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="propertyMarker in propertyMarkers" :key="propertyMarker.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PropertyMarkerView', params: { propertyMarkerId: propertyMarker.id } }">{{
                propertyMarker.id
              }}</router-link>
            </td>
            <td>{{ propertyMarker.extId }}</td>
            <td>{{ propertyMarker.area }}</td>
            <td>{{ propertyMarker.completionDate }}</td>
            <td>{{ propertyMarker.latitude }}</td>
            <td>{{ propertyMarker.longitude }}</td>
            <td>{{ propertyMarker.name }}</td>
            <td>{{ propertyMarker.developer }}</td>
            <td>{{ propertyMarker.status }}</td>
            <td>{{ propertyMarker.saleStatus }}</td>
            <td>{{ propertyMarker.minPrice }}</td>
            <td>{{ propertyMarker.coverUrl }}</td>
            <td>{{ propertyMarker.coverJson }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PropertyMarkerView', params: { propertyMarkerId: propertyMarker.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PropertyMarkerEdit', params: { propertyMarkerId: propertyMarker.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(propertyMarker)"
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
          id="uaeRealStateHubApp.propertyMarker.delete.question"
          data-cy="propertyMarkerDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-propertyMarker-heading" v-text="t$('uaeRealStateHubApp.propertyMarker.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-propertyMarker"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removePropertyMarker()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="propertyMarkers && propertyMarkers.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./property-marker.component.ts"></script>
