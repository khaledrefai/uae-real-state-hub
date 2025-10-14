<template>
  <div>
    <h2 id="page-heading" data-cy="FacilityHeading">
      <span v-text="t$('uaeRealStateHubApp.facility.home.title')" id="facility-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.facility.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'FacilityCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-facility"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.facility.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && facilities && facilities.length === 0">
      <span v-text="t$('uaeRealStateHubApp.facility.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="facilities && facilities.length > 0">
      <table class="table table-striped" aria-describedby="facilities">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('uaeRealStateHubApp.facility.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('source')">
              <span v-text="t$('uaeRealStateHubApp.facility.source')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'source'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('imageUrl')">
              <span v-text="t$('uaeRealStateHubApp.facility.imageUrl')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'imageUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('property.id')">
              <span v-text="t$('uaeRealStateHubApp.facility.property')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'property.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="facility in facilities" :key="facility.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FacilityView', params: { facilityId: facility.id } }">{{ facility.id }}</router-link>
            </td>
            <td>{{ facility.name }}</td>
            <td>{{ facility.source }}</td>
            <td>{{ facility.imageUrl }}</td>
            <td>
              <div v-if="facility.property">
                <router-link :to="{ name: 'PropertyView', params: { propertyId: facility.property.id } }">{{
                  facility.property.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'FacilityView', params: { facilityId: facility.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'FacilityEdit', params: { facilityId: facility.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(facility)"
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
          id="uaeRealStateHubApp.facility.delete.question"
          data-cy="facilityDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-facility-heading" v-text="t$('uaeRealStateHubApp.facility.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-facility"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeFacility()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="facilities && facilities.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./facility.component.ts"></script>
