<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.unitAvailability.home.createOrEditLabel"
          data-cy="UnitAvailabilityCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.unitAvailability.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="unitAvailability.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="unitAvailability.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitAvailability.buildingName')"
              for="unit-availability-buildingName"
            ></label>
            <input
              type="text"
              class="form-control"
              name="buildingName"
              id="unit-availability-buildingName"
              data-cy="buildingName"
              :class="{ valid: !v$.buildingName.$invalid, invalid: v$.buildingName.$invalid }"
              v-model="v$.buildingName.$model"
              required
            />
            <div v-if="v$.buildingName.$anyDirty && v$.buildingName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.buildingName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitAvailability.areaFrom')"
              for="unit-availability-areaFrom"
            ></label>
            <input
              type="number"
              class="form-control"
              name="areaFrom"
              id="unit-availability-areaFrom"
              data-cy="areaFrom"
              :class="{ valid: !v$.areaFrom.$invalid, invalid: v$.areaFrom.$invalid }"
              v-model.number="v$.areaFrom.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitAvailability.areaUnit')"
              for="unit-availability-areaUnit"
            ></label>
            <input
              type="text"
              class="form-control"
              name="areaUnit"
              id="unit-availability-areaUnit"
              data-cy="areaUnit"
              :class="{ valid: !v$.areaUnit.$invalid, invalid: v$.areaUnit.$invalid }"
              v-model="v$.areaUnit.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitAvailability.bedroomType')"
              for="unit-availability-bedroomType"
            ></label>
            <input
              type="text"
              class="form-control"
              name="bedroomType"
              id="unit-availability-bedroomType"
              data-cy="bedroomType"
              :class="{ valid: !v$.bedroomType.$invalid, invalid: v$.bedroomType.$invalid }"
              v-model="v$.bedroomType.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitAvailability.lastUpdated')"
              for="unit-availability-lastUpdated"
            ></label>
            <div class="d-flex">
              <input
                id="unit-availability-lastUpdated"
                data-cy="lastUpdated"
                type="datetime-local"
                class="form-control"
                name="lastUpdated"
                :class="{ valid: !v$.lastUpdated.$invalid, invalid: v$.lastUpdated.$invalid }"
                :value="convertDateTimeFromServer(v$.lastUpdated.$model)"
                @change="updateInstantField('lastUpdated', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitAvailability.priceCurrency')"
              for="unit-availability-priceCurrency"
            ></label>
            <input
              type="text"
              class="form-control"
              name="priceCurrency"
              id="unit-availability-priceCurrency"
              data-cy="priceCurrency"
              :class="{ valid: !v$.priceCurrency.$invalid, invalid: v$.priceCurrency.$invalid }"
              v-model="v$.priceCurrency.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitAvailability.priceFrom')"
              for="unit-availability-priceFrom"
            ></label>
            <input
              type="number"
              class="form-control"
              name="priceFrom"
              id="unit-availability-priceFrom"
              data-cy="priceFrom"
              :class="{ valid: !v$.priceFrom.$invalid, invalid: v$.priceFrom.$invalid }"
              v-model.number="v$.priceFrom.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitAvailability.priceTo')"
              for="unit-availability-priceTo"
            ></label>
            <input
              type="number"
              class="form-control"
              name="priceTo"
              id="unit-availability-priceTo"
              data-cy="priceTo"
              :class="{ valid: !v$.priceTo.$invalid, invalid: v$.priceTo.$invalid }"
              v-model.number="v$.priceTo.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitAvailability.unitsAvailable')"
              for="unit-availability-unitsAvailable"
            ></label>
            <input
              type="number"
              class="form-control"
              name="unitsAvailable"
              id="unit-availability-unitsAvailable"
              data-cy="unitsAvailable"
              :class="{ valid: !v$.unitsAvailable.$invalid, invalid: v$.unitsAvailable.$invalid }"
              v-model.number="v$.unitsAvailable.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitAvailability.property')"
              for="unit-availability-property"
            ></label>
            <select
              class="form-control"
              id="unit-availability-property"
              data-cy="property"
              name="property"
              v-model="unitAvailability.property"
            >
              <option :value="null"></option>
              <option
                :value="
                  unitAvailability.property && propertyOption.id === unitAvailability.property.id
                    ? unitAvailability.property
                    : propertyOption
                "
                v-for="propertyOption in properties"
                :key="propertyOption.id"
              >
                {{ propertyOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./unit-availability-update.component.ts"></script>
