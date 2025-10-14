<template>
  <div>
    <h2 id="page-heading" data-cy="UnitBlockHeading">
      <span v-text="t$('uaeRealStateHubApp.unitBlock.home.title')" id="unit-block-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.unitBlock.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'UnitBlockCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-unit-block"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && unitBlocks && unitBlocks.length === 0">
      <span v-text="t$('uaeRealStateHubApp.unitBlock.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="unitBlocks && unitBlocks.length > 0">
      <table class="table table-striped" aria-describedby="unitBlocks">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('normalizedType')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.normalizedType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'normalizedType'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unitType')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.unitType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unitType'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('bedroomsAmount')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.bedroomsAmount')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'bedroomsAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unitBedrooms')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.unitBedrooms')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unitBedrooms'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('areaUnit')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.areaUnit')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'areaUnit'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unitsAmount')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsAmount')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unitsAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unitsAreaFrom')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaFrom')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unitsAreaFrom'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unitsAreaTo')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaTo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unitsAreaTo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unitsAreaFromM2')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaFromM2')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unitsAreaFromM2'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unitsAreaToM2')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaToM2')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unitsAreaToM2'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unitsPriceFrom')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsPriceFrom')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unitsPriceFrom'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unitsPriceTo')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsPriceTo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unitsPriceTo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('priceCurrency')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.priceCurrency')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'priceCurrency'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('typicalImageUrl')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.typicalImageUrl')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'typicalImageUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('property.id')">
              <span v-text="t$('uaeRealStateHubApp.unitBlock.property')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'property.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="unitBlock in unitBlocks" :key="unitBlock.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'UnitBlockView', params: { unitBlockId: unitBlock.id } }">{{ unitBlock.id }}</router-link>
            </td>
            <td>{{ unitBlock.normalizedType }}</td>
            <td>{{ unitBlock.unitType }}</td>
            <td>{{ unitBlock.bedroomsAmount }}</td>
            <td>{{ unitBlock.unitBedrooms }}</td>
            <td>{{ unitBlock.areaUnit }}</td>
            <td>{{ unitBlock.unitsAmount }}</td>
            <td>{{ unitBlock.unitsAreaFrom }}</td>
            <td>{{ unitBlock.unitsAreaTo }}</td>
            <td>{{ unitBlock.unitsAreaFromM2 }}</td>
            <td>{{ unitBlock.unitsAreaToM2 }}</td>
            <td>{{ unitBlock.unitsPriceFrom }}</td>
            <td>{{ unitBlock.unitsPriceTo }}</td>
            <td>{{ unitBlock.priceCurrency }}</td>
            <td>{{ unitBlock.typicalImageUrl }}</td>
            <td>
              <div v-if="unitBlock.property">
                <router-link :to="{ name: 'PropertyView', params: { propertyId: unitBlock.property.id } }">{{
                  unitBlock.property.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'UnitBlockView', params: { unitBlockId: unitBlock.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'UnitBlockEdit', params: { unitBlockId: unitBlock.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(unitBlock)"
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
          id="uaeRealStateHubApp.unitBlock.delete.question"
          data-cy="unitBlockDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-unitBlock-heading" v-text="t$('uaeRealStateHubApp.unitBlock.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-unitBlock"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeUnitBlock()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="unitBlocks && unitBlocks.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./unit-block.component.ts"></script>
