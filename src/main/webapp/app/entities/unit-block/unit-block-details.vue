<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="unitBlock">
        <h2 class="jh-entity-heading" data-cy="unitBlockDetailsHeading">
          <span v-text="t$('uaeRealStateHubApp.unitBlock.detail.title')"></span> {{ unitBlock.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.normalizedType')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.normalizedType }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.unitType')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.unitType }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.bedroomsAmount')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.bedroomsAmount }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.unitBedrooms')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.unitBedrooms }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.areaUnit')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.areaUnit }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsAmount')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.unitsAmount }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaFrom')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.unitsAreaFrom }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaTo')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.unitsAreaTo }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaFromM2')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.unitsAreaFromM2 }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsAreaToM2')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.unitsAreaToM2 }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsPriceFrom')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.unitsPriceFrom }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.unitsPriceTo')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.unitsPriceTo }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.priceCurrency')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.priceCurrency }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.typicalImageUrl')"></span>
          </dt>
          <dd>
            <span>{{ unitBlock.typicalImageUrl }}</span>
          </dd>
          <dt>
            <span v-text="t$('uaeRealStateHubApp.unitBlock.property')"></span>
          </dt>
          <dd>
            <div v-if="unitBlock.property">
              <router-link :to="{ name: 'PropertyView', params: { propertyId: unitBlock.property.id } }">{{
                unitBlock.property.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="unitBlock.id"
          :to="{ name: 'UnitBlockEdit', params: { unitBlockId: unitBlock.id } }"
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

<script lang="ts" src="./unit-block-details.component.ts"></script>
