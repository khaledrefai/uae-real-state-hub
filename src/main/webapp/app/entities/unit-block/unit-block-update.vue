<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.unitBlock.home.createOrEditLabel"
          data-cy="UnitBlockCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.unitBlock.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="unitBlock.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="unitBlock.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitBlock.normalizedType')"
              for="unit-block-normalizedType"
            ></label>
            <input
              type="text"
              class="form-control"
              name="normalizedType"
              id="unit-block-normalizedType"
              data-cy="normalizedType"
              :class="{ valid: !v$.normalizedType.$invalid, invalid: v$.normalizedType.$invalid }"
              v-model="v$.normalizedType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.unitBlock.unitType')" for="unit-block-unitType"></label>
            <input
              type="text"
              class="form-control"
              name="unitType"
              id="unit-block-unitType"
              data-cy="unitType"
              :class="{ valid: !v$.unitType.$invalid, invalid: v$.unitType.$invalid }"
              v-model="v$.unitType.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitBlock.bedroomsAmount')"
              for="unit-block-bedroomsAmount"
            ></label>
            <input
              type="text"
              class="form-control"
              name="bedroomsAmount"
              id="unit-block-bedroomsAmount"
              data-cy="bedroomsAmount"
              :class="{ valid: !v$.bedroomsAmount.$invalid, invalid: v$.bedroomsAmount.$invalid }"
              v-model="v$.bedroomsAmount.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitBlock.unitBedrooms')"
              for="unit-block-unitBedrooms"
            ></label>
            <input
              type="text"
              class="form-control"
              name="unitBedrooms"
              id="unit-block-unitBedrooms"
              data-cy="unitBedrooms"
              :class="{ valid: !v$.unitBedrooms.$invalid, invalid: v$.unitBedrooms.$invalid }"
              v-model="v$.unitBedrooms.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.unitBlock.areaUnit')" for="unit-block-areaUnit"></label>
            <input
              type="text"
              class="form-control"
              name="areaUnit"
              id="unit-block-areaUnit"
              data-cy="areaUnit"
              :class="{ valid: !v$.areaUnit.$invalid, invalid: v$.areaUnit.$invalid }"
              v-model="v$.areaUnit.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.unitBlock.unitsAmount')" for="unit-block-unitsAmount"></label>
            <input
              type="number"
              class="form-control"
              name="unitsAmount"
              id="unit-block-unitsAmount"
              data-cy="unitsAmount"
              :class="{ valid: !v$.unitsAmount.$invalid, invalid: v$.unitsAmount.$invalid }"
              v-model.number="v$.unitsAmount.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaFrom')"
              for="unit-block-unitsAreaFrom"
            ></label>
            <input
              type="number"
              class="form-control"
              name="unitsAreaFrom"
              id="unit-block-unitsAreaFrom"
              data-cy="unitsAreaFrom"
              :class="{ valid: !v$.unitsAreaFrom.$invalid, invalid: v$.unitsAreaFrom.$invalid }"
              v-model.number="v$.unitsAreaFrom.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaTo')" for="unit-block-unitsAreaTo"></label>
            <input
              type="number"
              class="form-control"
              name="unitsAreaTo"
              id="unit-block-unitsAreaTo"
              data-cy="unitsAreaTo"
              :class="{ valid: !v$.unitsAreaTo.$invalid, invalid: v$.unitsAreaTo.$invalid }"
              v-model.number="v$.unitsAreaTo.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaFromM2')"
              for="unit-block-unitsAreaFromM2"
            ></label>
            <input
              type="text"
              class="form-control"
              name="unitsAreaFromM2"
              id="unit-block-unitsAreaFromM2"
              data-cy="unitsAreaFromM2"
              :class="{ valid: !v$.unitsAreaFromM2.$invalid, invalid: v$.unitsAreaFromM2.$invalid }"
              v-model="v$.unitsAreaFromM2.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaToM2')"
              for="unit-block-unitsAreaToM2"
            ></label>
            <input
              type="text"
              class="form-control"
              name="unitsAreaToM2"
              id="unit-block-unitsAreaToM2"
              data-cy="unitsAreaToM2"
              :class="{ valid: !v$.unitsAreaToM2.$invalid, invalid: v$.unitsAreaToM2.$invalid }"
              v-model="v$.unitsAreaToM2.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitBlock.unitsPriceFrom')"
              for="unit-block-unitsPriceFrom"
            ></label>
            <input
              type="number"
              class="form-control"
              name="unitsPriceFrom"
              id="unit-block-unitsPriceFrom"
              data-cy="unitsPriceFrom"
              :class="{ valid: !v$.unitsPriceFrom.$invalid, invalid: v$.unitsPriceFrom.$invalid }"
              v-model.number="v$.unitsPriceFrom.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitBlock.unitsPriceTo')"
              for="unit-block-unitsPriceTo"
            ></label>
            <input
              type="number"
              class="form-control"
              name="unitsPriceTo"
              id="unit-block-unitsPriceTo"
              data-cy="unitsPriceTo"
              :class="{ valid: !v$.unitsPriceTo.$invalid, invalid: v$.unitsPriceTo.$invalid }"
              v-model.number="v$.unitsPriceTo.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitBlock.priceCurrency')"
              for="unit-block-priceCurrency"
            ></label>
            <input
              type="text"
              class="form-control"
              name="priceCurrency"
              id="unit-block-priceCurrency"
              data-cy="priceCurrency"
              :class="{ valid: !v$.priceCurrency.$invalid, invalid: v$.priceCurrency.$invalid }"
              v-model="v$.priceCurrency.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.unitBlock.typicalImageUrl')"
              for="unit-block-typicalImageUrl"
            ></label>
            <input
              type="text"
              class="form-control"
              name="typicalImageUrl"
              id="unit-block-typicalImageUrl"
              data-cy="typicalImageUrl"
              :class="{ valid: !v$.typicalImageUrl.$invalid, invalid: v$.typicalImageUrl.$invalid }"
              v-model="v$.typicalImageUrl.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.unitBlock.property')" for="unit-block-property"></label>
            <select class="form-control" id="unit-block-property" data-cy="property" name="property" v-model="unitBlock.property">
              <option :value="null"></option>
              <option
                :value="unitBlock.property && propertyOption.id === unitBlock.property.id ? unitBlock.property : propertyOption"
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
<script lang="ts" src="./unit-block-update.component.ts"></script>
