<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.subscriptionPlan.home.createOrEditLabel"
          data-cy="SubscriptionPlanCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.subscriptionPlan.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="subscriptionPlan.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="subscriptionPlan.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.subscriptionPlan.code')" for="subscription-plan-code"></label>
            <input
              type="text"
              class="form-control"
              name="code"
              id="subscription-plan-code"
              data-cy="code"
              :class="{ valid: !v$.code.$invalid, invalid: v$.code.$invalid }"
              v-model="v$.code.$model"
              required
            />
            <div v-if="v$.code.$anyDirty && v$.code.$invalid">
              <small class="form-text text-danger" v-for="error of v$.code.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.subscriptionPlan.name')" for="subscription-plan-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="subscription-plan-name"
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
              v-text="t$('uaeRealStateHubApp.subscriptionPlan.priceMonthly')"
              for="subscription-plan-priceMonthly"
            ></label>
            <input
              type="number"
              class="form-control"
              name="priceMonthly"
              id="subscription-plan-priceMonthly"
              data-cy="priceMonthly"
              :class="{ valid: !v$.priceMonthly.$invalid, invalid: v$.priceMonthly.$invalid }"
              v-model.number="v$.priceMonthly.$model"
              required
            />
            <div v-if="v$.priceMonthly.$anyDirty && v$.priceMonthly.$invalid">
              <small class="form-text text-danger" v-for="error of v$.priceMonthly.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.subscriptionPlan.priceYearly')"
              for="subscription-plan-priceYearly"
            ></label>
            <input
              type="number"
              class="form-control"
              name="priceYearly"
              id="subscription-plan-priceYearly"
              data-cy="priceYearly"
              :class="{ valid: !v$.priceYearly.$invalid, invalid: v$.priceYearly.$invalid }"
              v-model.number="v$.priceYearly.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.subscriptionPlan.currency')"
              for="subscription-plan-currency"
            ></label>
            <select
              class="form-control"
              name="currency"
              :class="{ valid: !v$.currency.$invalid, invalid: v$.currency.$invalid }"
              v-model="v$.currency.$model"
              id="subscription-plan-currency"
              data-cy="currency"
              required
            >
              <option
                v-for="currency in currencyValues"
                :key="currency"
                :value="currency"
                :label="t$('uaeRealStateHubApp.Currency.' + currency)"
              >
                {{ currency }}
              </option>
            </select>
            <div v-if="v$.currency.$anyDirty && v$.currency.$invalid">
              <small class="form-text text-danger" v-for="error of v$.currency.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.subscriptionPlan.stripePriceMonthlyId')"
              for="subscription-plan-stripePriceMonthlyId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="stripePriceMonthlyId"
              id="subscription-plan-stripePriceMonthlyId"
              data-cy="stripePriceMonthlyId"
              :class="{ valid: !v$.stripePriceMonthlyId.$invalid, invalid: v$.stripePriceMonthlyId.$invalid }"
              v-model="v$.stripePriceMonthlyId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.subscriptionPlan.stripePriceYearlyId')"
              for="subscription-plan-stripePriceYearlyId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="stripePriceYearlyId"
              id="subscription-plan-stripePriceYearlyId"
              data-cy="stripePriceYearlyId"
              :class="{ valid: !v$.stripePriceYearlyId.$invalid, invalid: v$.stripePriceYearlyId.$invalid }"
              v-model="v$.stripePriceYearlyId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.subscriptionPlan.maxListings')"
              for="subscription-plan-maxListings"
            ></label>
            <input
              type="number"
              class="form-control"
              name="maxListings"
              id="subscription-plan-maxListings"
              data-cy="maxListings"
              :class="{ valid: !v$.maxListings.$invalid, invalid: v$.maxListings.$invalid }"
              v-model.number="v$.maxListings.$model"
              required
            />
            <div v-if="v$.maxListings.$anyDirty && v$.maxListings.$invalid">
              <small class="form-text text-danger" v-for="error of v$.maxListings.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.subscriptionPlan.features')"
              for="subscription-plan-features"
            ></label>
            <textarea
              class="form-control"
              name="features"
              id="subscription-plan-features"
              data-cy="features"
              :class="{ valid: !v$.features.$invalid, invalid: v$.features.$invalid }"
              v-model="v$.features.$model"
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
<script lang="ts" src="./subscription-plan-update.component.ts"></script>
