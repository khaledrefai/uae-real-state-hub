<template>
  <div>
    <h2 id="page-heading" data-cy="UnitAvailabilityHeading">
      <span v-text="t$('uaeRealStateHubApp.unitAvailability.home.title')" id="unit-availability-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.unitAvailability.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'UnitAvailabilityCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-unit-availability"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.unitAvailability.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && unitAvailabilities && unitAvailabilities.length === 0">
      <span v-text="t$('uaeRealStateHubApp.unitAvailability.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="unitAvailabilities && unitAvailabilities.length > 0">
      <table class="table table-striped" aria-describedby="unitAvailabilities">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('buildingName')">
              <span v-text="t$('uaeRealStateHubApp.unitAvailability.buildingName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'buildingName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('areaFrom')">
              <span v-text="t$('uaeRealStateHubApp.unitAvailability.areaFrom')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'areaFrom'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('areaUnit')">
              <span v-text="t$('uaeRealStateHubApp.unitAvailability.areaUnit')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'areaUnit'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('bedroomType')">
              <span v-text="t$('uaeRealStateHubApp.unitAvailability.bedroomType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'bedroomType'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('lastUpdated')">
              <span v-text="t$('uaeRealStateHubApp.unitAvailability.lastUpdated')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastUpdated'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('priceCurrency')">
              <span v-text="t$('uaeRealStateHubApp.unitAvailability.priceCurrency')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priceCurrency'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('priceFrom')">
              <span v-text="t$('uaeRealStateHubApp.unitAvailability.priceFrom')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priceFrom'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('priceTo')">
              <span v-text="t$('uaeRealStateHubApp.unitAvailability.priceTo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priceTo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unitsAvailable')">
              <span v-text="t$('uaeRealStateHubApp.unitAvailability.unitsAvailable')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unitsAvailable'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('property.id')">
              <span v-text="t$('uaeRealStateHubApp.unitAvailability.property')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'property.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="unitAvailability in unitAvailabilities" :key="unitAvailability.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'UnitAvailabilityView', params: { unitAvailabilityId: unitAvailability.id } }">{{
                unitAvailability.id
              }}</router-link>
            </td>
            <td>{{ unitAvailability.buildingName }}</td>
            <td>{{ unitAvailability.areaFrom }}</td>
            <td>{{ unitAvailability.areaUnit }}</td>
            <td>{{ unitAvailability.bedroomType }}</td>
            <td>{{ formatDateShort(unitAvailability.lastUpdated) || '' }}</td>
            <td>{{ unitAvailability.priceCurrency }}</td>
            <td>{{ unitAvailability.priceFrom }}</td>
            <td>{{ unitAvailability.priceTo }}</td>
            <td>{{ unitAvailability.unitsAvailable }}</td>
            <td>
              <div v-if="unitAvailability.property">
                <router-link :to="{ name: 'PropertyView', params: { propertyId: unitAvailability.property.id } }">{{
                  unitAvailability.property.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'UnitAvailabilityView', params: { unitAvailabilityId: unitAvailability.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'UnitAvailabilityEdit', params: { unitAvailabilityId: unitAvailability.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(unitAvailability)"
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
          id="uaeRealStateHubApp.unitAvailability.delete.question"
          data-cy="unitAvailabilityDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-unitAvailability-heading"
          v-text="t$('uaeRealStateHubApp.unitAvailability.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-unitAvailability"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeUnitAvailability()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="unitAvailabilities && unitAvailabilities.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./unit-availability.component.ts"></script>
