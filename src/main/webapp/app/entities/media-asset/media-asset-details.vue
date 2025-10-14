<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="mediaAsset">
        <h2 class="jh-entity-heading" data-cy="mediaAssetDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.mediaAsset.detail.title')"></span> {{ mediaAsset.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.kind')"></span>
          </dt>
          <dd>
            <span v-text="t$('uaeRealStateHubApp.MediaKind.' + mediaAsset.kind)"></span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.url')"></span>
          </dt>
          <dd>
            <span>{{ mediaAsset.url }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.mimeType')"></span>
          </dt>
          <dd>
            <span>{{ mediaAsset.mimeType }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.width')"></span>
          </dt>
          <dd>
            <span>{{ mediaAsset.width }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.height')"></span>
          </dt>
          <dd>
            <span>{{ mediaAsset.height }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.mediaSize')"></span>
          </dt>
          <dd>
            <span>{{ mediaAsset.mediaSize }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.source')"></span>
          </dt>
          <dd>
            <span>{{ mediaAsset.source }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.title')"></span>
          </dt>
          <dd>
            <span>{{ mediaAsset.title }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.altText')"></span>
          </dt>
          <dd>
            <span>{{ mediaAsset.altText }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.sortOrder')"></span>
          </dt>
          <dd>
            <span>{{ mediaAsset.sortOrder }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.mediaAsset.property')"></span>
          </dt>
          <dd>
            <div v-if="mediaAsset.property">
              <router-link :to="{ name: 'PropertyView', params: { propertyId: mediaAsset.property.id } }">{{
                mediaAsset.property.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="mediaAsset.id"
          :to="{ name: 'MediaAssetEdit', params: { mediaAssetId: mediaAsset.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./media-asset-details.component.ts"></script>
