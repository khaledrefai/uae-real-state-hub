<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.propertyMarker.home.createOrEditLabel"
          data-cy="PropertyMarkerCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.propertyMarker.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="propertyMarker.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="propertyMarker.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.propertyMarker.extId')" for="property-marker-extId"></label>
            <input
              type="number"
              class="form-control"
              name="extId"
              id="property-marker-extId"
              data-cy="extId"
              :class="{ valid: !v$.extId.$invalid, invalid: v$.extId.$invalid }"
              v-model.number="v$.extId.$model"
              required
            />
            <div v-if="v$.extId.$anyDirty && v$.extId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.extId.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.propertyMarker.area')" for="property-marker-area"></label>
            <input
              type="text"
              class="form-control"
              name="area"
              id="property-marker-area"
              data-cy="area"
              :class="{ valid: !v$.area.$invalid, invalid: v$.area.$invalid }"
              v-model="v$.area.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.propertyMarker.completionDate')"
              for="property-marker-completionDate"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="property-marker-completionDate"
                  v-model="v$.completionDate.$model"
                  name="completionDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="property-marker-completionDate"
                data-cy="completionDate"
                type="text"
                class="form-control"
                name="completionDate"
                :class="{ valid: !v$.completionDate.$invalid, invalid: v$.completionDate.$invalid }"
                v-model="v$.completionDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.propertyMarker.latitude')"
              for="property-marker-latitude"
            ></label>
            <input
              type="number"
              class="form-control"
              name="latitude"
              id="property-marker-latitude"
              data-cy="latitude"
              :class="{ valid: !v$.latitude.$invalid, invalid: v$.latitude.$invalid }"
              v-model.number="v$.latitude.$model"
              required
            />
            <div v-if="v$.latitude.$anyDirty && v$.latitude.$invalid">
              <small class="form-text text-danger" v-for="error of v$.latitude.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.propertyMarker.longitude')"
              for="property-marker-longitude"
            ></label>
            <input
              type="number"
              class="form-control"
              name="longitude"
              id="property-marker-longitude"
              data-cy="longitude"
              :class="{ valid: !v$.longitude.$invalid, invalid: v$.longitude.$invalid }"
              v-model.number="v$.longitude.$model"
              required
            />
            <div v-if="v$.longitude.$anyDirty && v$.longitude.$invalid">
              <small class="form-text text-danger" v-for="error of v$.longitude.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.propertyMarker.name')" for="property-marker-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="property-marker-name"
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
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.propertyMarker.developer')"
              for="property-marker-developer"
            ></label>
            <input
              type="text"
              class="form-control"
              name="developer"
              id="property-marker-developer"
              data-cy="developer"
              :class="{ valid: !v$.developer.$invalid, invalid: v$.developer.$invalid }"
              v-model="v$.developer.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.propertyMarker.status')" for="property-marker-status"></label>
            <input
              type="text"
              class="form-control"
              name="status"
              id="property-marker-status"
              data-cy="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.propertyMarker.saleStatus')"
              for="property-marker-saleStatus"
            ></label>
            <input
              type="text"
              class="form-control"
              name="saleStatus"
              id="property-marker-saleStatus"
              data-cy="saleStatus"
              :class="{ valid: !v$.saleStatus.$invalid, invalid: v$.saleStatus.$invalid }"
              v-model="v$.saleStatus.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.propertyMarker.minPrice')"
              for="property-marker-minPrice"
            ></label>
            <input
              type="number"
              class="form-control"
              name="minPrice"
              id="property-marker-minPrice"
              data-cy="minPrice"
              :class="{ valid: !v$.minPrice.$invalid, invalid: v$.minPrice.$invalid }"
              v-model.number="v$.minPrice.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.propertyMarker.coverUrl')"
              for="property-marker-coverUrl"
            ></label>
            <input
              type="text"
              class="form-control"
              name="coverUrl"
              id="property-marker-coverUrl"
              data-cy="coverUrl"
              :class="{ valid: !v$.coverUrl.$invalid, invalid: v$.coverUrl.$invalid }"
              v-model="v$.coverUrl.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.propertyMarker.coverJson')"
              for="property-marker-coverJson"
            ></label>
            <textarea
              class="form-control"
              name="coverJson"
              id="property-marker-coverJson"
              data-cy="coverJson"
              :class="{ valid: !v$.coverJson.$invalid, invalid: v$.coverJson.$invalid }"
              v-model="v$.coverJson.$model"
            ></textarea>
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
<script lang="ts" src="./property-marker-update.component.ts"></script>
