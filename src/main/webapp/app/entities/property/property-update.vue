<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.property.home.createOrEditLabel"
          data-cy="PropertyCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.property.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="property.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="property.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.extId')" for="property-extId"></label>
            <input
              type="number"
              class="form-control"
              name="extId"
              id="property-extId"
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
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.slug')" for="property-slug"></label>
            <input
              type="text"
              class="form-control"
              name="slug"
              id="property-slug"
              data-cy="slug"
              :class="{ valid: !v$.slug.$invalid, invalid: v$.slug.$invalid }"
              v-model="v$.slug.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.name')" for="property-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="property-name"
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
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.developer')" for="property-developer"></label>
            <input
              type="text"
              class="form-control"
              name="developer"
              id="property-developer"
              data-cy="developer"
              :class="{ valid: !v$.developer.$invalid, invalid: v$.developer.$invalid }"
              v-model="v$.developer.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.area')" for="property-area"></label>
            <input
              type="text"
              class="form-control"
              name="area"
              id="property-area"
              data-cy="area"
              :class="{ valid: !v$.area.$invalid, invalid: v$.area.$invalid }"
              v-model="v$.area.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.city')" for="property-city"></label>
            <input
              type="text"
              class="form-control"
              name="city"
              id="property-city"
              data-cy="city"
              :class="{ valid: !v$.city.$invalid, invalid: v$.city.$invalid }"
              v-model="v$.city.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.country')" for="property-country"></label>
            <input
              type="text"
              class="form-control"
              name="country"
              id="property-country"
              data-cy="country"
              :class="{ valid: !v$.country.$invalid, invalid: v$.country.$invalid }"
              v-model="v$.country.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.listingType')" for="property-listingType"></label>
            <select
              class="form-control"
              name="listingType"
              :class="{ valid: !v$.listingType.$invalid, invalid: v$.listingType.$invalid }"
              v-model="v$.listingType.$model"
              id="property-listingType"
              data-cy="listingType"
            >
              <option
                v-for="listingType in listingTypeValues"
                :key="listingType"
                :value="listingType"
                :label="t$('uaeRealStateHubApp.ListingType.' + listingType)"
              >
                {{ listingType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.status')" for="property-status"></label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="property-status"
              data-cy="status"
            >
              <option
                v-for="propertyStatus in propertyStatusValues"
                :key="propertyStatus"
                :value="propertyStatus"
                :label="t$('uaeRealStateHubApp.PropertyStatus.' + propertyStatus)"
              >
                {{ propertyStatus }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.saleStatus')" for="property-saleStatus"></label>
            <input
              type="text"
              class="form-control"
              name="saleStatus"
              id="property-saleStatus"
              data-cy="saleStatus"
              :class="{ valid: !v$.saleStatus.$invalid, invalid: v$.saleStatus.$invalid }"
              v-model="v$.saleStatus.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.readiness')" for="property-readiness"></label>
            <input
              type="text"
              class="form-control"
              name="readiness"
              id="property-readiness"
              data-cy="readiness"
              :class="{ valid: !v$.readiness.$invalid, invalid: v$.readiness.$invalid }"
              v-model="v$.readiness.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.serviceCharge')" for="property-serviceCharge"></label>
            <input
              type="text"
              class="form-control"
              name="serviceCharge"
              id="property-serviceCharge"
              data-cy="serviceCharge"
              :class="{ valid: !v$.serviceCharge.$invalid, invalid: v$.serviceCharge.$invalid }"
              v-model="v$.serviceCharge.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.furnishing')" for="property-furnishing"></label>
            <input
              type="text"
              class="form-control"
              name="furnishing"
              id="property-furnishing"
              data-cy="furnishing"
              :class="{ valid: !v$.furnishing.$invalid, invalid: v$.furnishing.$invalid }"
              v-model="v$.furnishing.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.hasEscrow')" for="property-hasEscrow"></label>
            <input
              type="checkbox"
              class="form-check"
              name="hasEscrow"
              id="property-hasEscrow"
              data-cy="hasEscrow"
              :class="{ valid: !v$.hasEscrow.$invalid, invalid: v$.hasEscrow.$invalid }"
              v-model="v$.hasEscrow.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.postHandover')" for="property-postHandover"></label>
            <input
              type="checkbox"
              class="form-check"
              name="postHandover"
              id="property-postHandover"
              data-cy="postHandover"
              :class="{ valid: !v$.postHandover.$invalid, invalid: v$.postHandover.$invalid }"
              v-model="v$.postHandover.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.property.completionDateTime')"
              for="property-completionDateTime"
            ></label>
            <div class="d-flex">
              <input
                id="property-completionDateTime"
                data-cy="completionDateTime"
                type="datetime-local"
                class="form-control"
                name="completionDateTime"
                :class="{ valid: !v$.completionDateTime.$invalid, invalid: v$.completionDateTime.$invalid }"
                :value="convertDateTimeFromServer(v$.completionDateTime.$model)"
                @change="updateInstantField('completionDateTime', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.minPrice')" for="property-minPrice"></label>
            <input
              type="number"
              class="form-control"
              name="minPrice"
              id="property-minPrice"
              data-cy="minPrice"
              :class="{ valid: !v$.minPrice.$invalid, invalid: v$.minPrice.$invalid }"
              v-model.number="v$.minPrice.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.maxPrice')" for="property-maxPrice"></label>
            <input
              type="number"
              class="form-control"
              name="maxPrice"
              id="property-maxPrice"
              data-cy="maxPrice"
              :class="{ valid: !v$.maxPrice.$invalid, invalid: v$.maxPrice.$invalid }"
              v-model.number="v$.maxPrice.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.minPriceAed')" for="property-minPriceAed"></label>
            <input
              type="number"
              class="form-control"
              name="minPriceAed"
              id="property-minPriceAed"
              data-cy="minPriceAed"
              :class="{ valid: !v$.minPriceAed.$invalid, invalid: v$.minPriceAed.$invalid }"
              v-model.number="v$.minPriceAed.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.priceCurrency')" for="property-priceCurrency"></label>
            <input
              type="text"
              class="form-control"
              name="priceCurrency"
              id="property-priceCurrency"
              data-cy="priceCurrency"
              :class="{ valid: !v$.priceCurrency.$invalid, invalid: v$.priceCurrency.$invalid }"
              v-model="v$.priceCurrency.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.minArea')" for="property-minArea"></label>
            <input
              type="number"
              class="form-control"
              name="minArea"
              id="property-minArea"
              data-cy="minArea"
              :class="{ valid: !v$.minArea.$invalid, invalid: v$.minArea.$invalid }"
              v-model.number="v$.minArea.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.maxArea')" for="property-maxArea"></label>
            <input
              type="number"
              class="form-control"
              name="maxArea"
              id="property-maxArea"
              data-cy="maxArea"
              :class="{ valid: !v$.maxArea.$invalid, invalid: v$.maxArea.$invalid }"
              v-model.number="v$.maxArea.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.areaUnit')" for="property-areaUnit"></label>
            <input
              type="text"
              class="form-control"
              name="areaUnit"
              id="property-areaUnit"
              data-cy="areaUnit"
              :class="{ valid: !v$.areaUnit.$invalid, invalid: v$.areaUnit.$invalid }"
              v-model="v$.areaUnit.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.latitude')" for="property-latitude"></label>
            <input
              type="number"
              class="form-control"
              name="latitude"
              id="property-latitude"
              data-cy="latitude"
              :class="{ valid: !v$.latitude.$invalid, invalid: v$.latitude.$invalid }"
              v-model.number="v$.latitude.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.longitude')" for="property-longitude"></label>
            <input
              type="number"
              class="form-control"
              name="longitude"
              id="property-longitude"
              data-cy="longitude"
              :class="{ valid: !v$.longitude.$invalid, invalid: v$.longitude.$invalid }"
              v-model.number="v$.longitude.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.coverUrl')" for="property-coverUrl"></label>
            <input
              type="text"
              class="form-control"
              name="coverUrl"
              id="property-coverUrl"
              data-cy="coverUrl"
              :class="{ valid: !v$.coverUrl.$invalid, invalid: v$.coverUrl.$invalid }"
              v-model="v$.coverUrl.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.coverJson')" for="property-coverJson"></label>
            <textarea
              class="form-control"
              name="coverJson"
              id="property-coverJson"
              data-cy="coverJson"
              :class="{ valid: !v$.coverJson.$invalid, invalid: v$.coverJson.$invalid }"
              v-model="v$.coverJson.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.videoUrl')" for="property-videoUrl"></label>
            <input
              type="text"
              class="form-control"
              name="videoUrl"
              id="property-videoUrl"
              data-cy="videoUrl"
              :class="{ valid: !v$.videoUrl.$invalid, invalid: v$.videoUrl.$invalid }"
              v-model="v$.videoUrl.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.brochureUrl')" for="property-brochureUrl"></label>
            <input
              type="text"
              class="form-control"
              name="brochureUrl"
              id="property-brochureUrl"
              data-cy="brochureUrl"
              :class="{ valid: !v$.brochureUrl.$invalid, invalid: v$.brochureUrl.$invalid }"
              v-model="v$.brochureUrl.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.layoutsPdfUrl')" for="property-layoutsPdfUrl"></label>
            <input
              type="text"
              class="form-control"
              name="layoutsPdfUrl"
              id="property-layoutsPdfUrl"
              data-cy="layoutsPdfUrl"
              :class="{ valid: !v$.layoutsPdfUrl.$invalid, invalid: v$.layoutsPdfUrl.$invalid }"
              v-model="v$.layoutsPdfUrl.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.website')" for="property-website"></label>
            <input
              type="text"
              class="form-control"
              name="website"
              id="property-website"
              data-cy="website"
              :class="{ valid: !v$.website.$invalid, invalid: v$.website.$invalid }"
              v-model="v$.website.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.overviewMd')" for="property-overviewMd"></label>
            <textarea
              class="form-control"
              name="overviewMd"
              id="property-overviewMd"
              data-cy="overviewMd"
              :class="{ valid: !v$.overviewMd.$invalid, invalid: v$.overviewMd.$invalid }"
              v-model="v$.overviewMd.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.raw')" for="property-raw"></label>
            <textarea
              class="form-control"
              name="raw"
              id="property-raw"
              data-cy="raw"
              :class="{ valid: !v$.raw.$invalid, invalid: v$.raw.$invalid }"
              v-model="v$.raw.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.publishedAt')" for="property-publishedAt"></label>
            <div class="d-flex">
              <input
                id="property-publishedAt"
                data-cy="publishedAt"
                type="datetime-local"
                class="form-control"
                name="publishedAt"
                :class="{ valid: !v$.publishedAt.$invalid, invalid: v$.publishedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.publishedAt.$model)"
                @change="updateInstantField('publishedAt', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.updatedAt')" for="property-updatedAt"></label>
            <div class="d-flex">
              <input
                id="property-updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                class="form-control"
                name="updatedAt"
                :class="{ valid: !v$.updatedAt.$invalid, invalid: v$.updatedAt.$invalid }"
                :value="convertDateTimeFromServer(v$.updatedAt.$model)"
                @change="updateInstantField('updatedAt', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.property.marker')" for="property-marker"></label>
            <select class="form-control" id="property-marker" data-cy="marker" name="marker" v-model="property.marker">
              <option :value="null"></option>
              <option
                :value="property.marker && propertyMarkerOption.id === property.marker.id ? property.marker : propertyMarkerOption"
                v-for="propertyMarkerOption in propertyMarkers"
                :key="propertyMarkerOption.id"
              >
                {{ propertyMarkerOption.id }}
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
<script lang="ts" src="./property-update.component.ts"></script>
