<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.mapPoint.home.createOrEditLabel"
          data-cy="MapPointCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.mapPoint.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="mapPoint.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="mapPoint.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mapPoint.name')" for="map-point-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="map-point-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mapPoint.distanceKm')" for="map-point-distanceKm"></label>
            <input
              type="number"
              class="form-control"
              name="distanceKm"
              id="map-point-distanceKm"
              data-cy="distanceKm"
              :class="{ valid: !v$.distanceKm.$invalid, invalid: v$.distanceKm.$invalid }"
              v-model.number="v$.distanceKm.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mapPoint.property')" for="map-point-property"></label>
            <select class="form-control" id="map-point-property" data-cy="property" name="property" v-model="mapPoint.property">
              <option :value="null"></option>
              <option
                :value="mapPoint.property && propertyOption.id === mapPoint.property.id ? mapPoint.property : propertyOption"
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
<script lang="ts" src="./map-point-update.component.ts"></script>
