<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.mediaAsset.home.createOrEditLabel"
          data-cy="MediaAssetCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.mediaAsset.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="mediaAsset.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="mediaAsset.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.kind')" for="media-asset-kind"></label>
            <select
              class="form-control"
              name="kind"
              :class="{ valid: !v$.kind.$invalid, invalid: v$.kind.$invalid }"
              v-model="v$.kind.$model"
              id="media-asset-kind"
              data-cy="kind"
              required
            >
              <option
                v-for="mediaKind in mediaKindValues"
                :key="mediaKind"
                :value="mediaKind"
                :label="t$('uaeRealStateHubApp.MediaKind.' + mediaKind)"
              >
                {{ mediaKind }}
              </option>
            </select>
            <div v-if="v$.kind.$anyDirty && v$.kind.$invalid">
              <small class="form-text text-danger" v-for="error of v$.kind.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.url')" for="media-asset-url"></label>
            <input
              type="text"
              class="form-control"
              name="url"
              id="media-asset-url"
              data-cy="url"
              :class="{ valid: !v$.url.$invalid, invalid: v$.url.$invalid }"
              v-model="v$.url.$model"
              required
            />
            <div v-if="v$.url.$anyDirty && v$.url.$invalid">
              <small class="form-text text-danger" v-for="error of v$.url.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.mimeType')" for="media-asset-mimeType"></label>
            <input
              type="text"
              class="form-control"
              name="mimeType"
              id="media-asset-mimeType"
              data-cy="mimeType"
              :class="{ valid: !v$.mimeType.$invalid, invalid: v$.mimeType.$invalid }"
              v-model="v$.mimeType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.width')" for="media-asset-width"></label>
            <input
              type="number"
              class="form-control"
              name="width"
              id="media-asset-width"
              data-cy="width"
              :class="{ valid: !v$.width.$invalid, invalid: v$.width.$invalid }"
              v-model.number="v$.width.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.height')" for="media-asset-height"></label>
            <input
              type="number"
              class="form-control"
              name="height"
              id="media-asset-height"
              data-cy="height"
              :class="{ valid: !v$.height.$invalid, invalid: v$.height.$invalid }"
              v-model.number="v$.height.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.mediaSize')" for="media-asset-mediaSize"></label>
            <input
              type="number"
              class="form-control"
              name="mediaSize"
              id="media-asset-mediaSize"
              data-cy="mediaSize"
              :class="{ valid: !v$.mediaSize.$invalid, invalid: v$.mediaSize.$invalid }"
              v-model.number="v$.mediaSize.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.source')" for="media-asset-source"></label>
            <input
              type="text"
              class="form-control"
              name="source"
              id="media-asset-source"
              data-cy="source"
              :class="{ valid: !v$.source.$invalid, invalid: v$.source.$invalid }"
              v-model="v$.source.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.title')" for="media-asset-title"></label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="media-asset-title"
              data-cy="title"
              :class="{ valid: !v$.title.$invalid, invalid: v$.title.$invalid }"
              v-model="v$.title.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.altText')" for="media-asset-altText"></label>
            <input
              type="text"
              class="form-control"
              name="altText"
              id="media-asset-altText"
              data-cy="altText"
              :class="{ valid: !v$.altText.$invalid, invalid: v$.altText.$invalid }"
              v-model="v$.altText.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.sortOrder')" for="media-asset-sortOrder"></label>
            <input
              type="number"
              class="form-control"
              name="sortOrder"
              id="media-asset-sortOrder"
              data-cy="sortOrder"
              :class="{ valid: !v$.sortOrder.$invalid, invalid: v$.sortOrder.$invalid }"
              v-model.number="v$.sortOrder.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.mediaAsset.property')" for="media-asset-property"></label>
            <select class="form-control" id="media-asset-property" data-cy="property" name="property" v-model="mediaAsset.property">
              <option :value="null"></option>
              <option
                :value="mediaAsset.property && propertyOption.id === mediaAsset.property.id ? mediaAsset.property : propertyOption"
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
<script lang="ts" src="./media-asset-update.component.ts"></script>
