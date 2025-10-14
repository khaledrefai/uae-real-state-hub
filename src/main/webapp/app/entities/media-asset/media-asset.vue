<template>
  <div>
    <h2 id="page-heading" data-cy="MediaAssetHeading">
      <span v-text="t$('uaeRealStateHubApp.mediaAsset.home.title')" id="media-asset-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('uaeRealStateHubApp.mediaAsset.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'MediaAssetCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-media-asset"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && mediaAssets && mediaAssets.length === 0">
      <span v-text="t$('uaeRealStateHubApp.mediaAsset.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="mediaAssets && mediaAssets.length > 0">
      <table class="table table-striped" aria-describedby="mediaAssets">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('kind')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.kind')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'kind'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('url')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.url')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'url'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('mimeType')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.mimeType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mimeType'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('width')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.width')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'width'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('height')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.height')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'height'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('mediaSize')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.mediaSize')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mediaSize'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('source')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.source')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'source'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('title')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.title')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('altText')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.altText')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'altText'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('sortOrder')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.sortOrder')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sortOrder'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('property.id')">
              <span v-text="t$('uaeRealStateHubApp.mediaAsset.property')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'property.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="mediaAsset in mediaAssets" :key="mediaAsset.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MediaAssetView', params: { mediaAssetId: mediaAsset.id } }">{{ mediaAsset.id }}</router-link>
            </td>
            <td v-text="t$('uaeRealStateHubApp.MediaKind.' + mediaAsset.kind)"></td>
            <td>{{ mediaAsset.url }}</td>
            <td>{{ mediaAsset.mimeType }}</td>
            <td>{{ mediaAsset.width }}</td>
            <td>{{ mediaAsset.height }}</td>
            <td>{{ mediaAsset.mediaSize }}</td>
            <td>{{ mediaAsset.source }}</td>
            <td>{{ mediaAsset.title }}</td>
            <td>{{ mediaAsset.altText }}</td>
            <td>{{ mediaAsset.sortOrder }}</td>
            <td>
              <div v-if="mediaAsset.property">
                <router-link :to="{ name: 'PropertyView', params: { propertyId: mediaAsset.property.id } }">{{
                  mediaAsset.property.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MediaAssetView', params: { mediaAssetId: mediaAsset.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MediaAssetEdit', params: { mediaAssetId: mediaAsset.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(mediaAsset)"
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
          id="uaeRealStateHubApp.mediaAsset.delete.question"
          data-cy="mediaAssetDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-mediaAsset-heading" v-text="t$('uaeRealStateHubApp.mediaAsset.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-mediaAsset"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeMediaAsset()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="mediaAssets && mediaAssets.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./media-asset.component.ts"></script>
