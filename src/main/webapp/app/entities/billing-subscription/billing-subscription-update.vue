<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.billingSubscription.home.createOrEditLabel"
          data-cy="BillingSubscriptionCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.billingSubscription.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="billingSubscription.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="billingSubscription.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.billingSubscription.status')"
              for="billing-subscription-status"
            ></label>
            <input
              type="text"
              class="form-control"
              name="status"
              id="billing-subscription-status"
              data-cy="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              required
            />
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.billingSubscription.startDate')"
              for="billing-subscription-startDate"
            ></label>
            <div class="d-flex">
              <input
                id="billing-subscription-startDate"
                data-cy="startDate"
                type="datetime-local"
                class="form-control"
                name="startDate"
                :class="{ valid: !v$.startDate.$invalid, invalid: v$.startDate.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.startDate.$model)"
                @change="updateInstantField('startDate', $event)"
              />
            </div>
            <div v-if="v$.startDate.$anyDirty && v$.startDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.startDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.billingSubscription.endDate')"
              for="billing-subscription-endDate"
            ></label>
            <div class="d-flex">
              <input
                id="billing-subscription-endDate"
                data-cy="endDate"
                type="datetime-local"
                class="form-control"
                name="endDate"
                :class="{ valid: !v$.endDate.$invalid, invalid: v$.endDate.$invalid }"
                :value="convertDateTimeFromServer(v$.endDate.$model)"
                @change="updateInstantField('endDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.billingSubscription.stripeCustomerId')"
              for="billing-subscription-stripeCustomerId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="stripeCustomerId"
              id="billing-subscription-stripeCustomerId"
              data-cy="stripeCustomerId"
              :class="{ valid: !v$.stripeCustomerId.$invalid, invalid: v$.stripeCustomerId.$invalid }"
              v-model="v$.stripeCustomerId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.billingSubscription.stripeSubscriptionId')"
              for="billing-subscription-stripeSubscriptionId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="stripeSubscriptionId"
              id="billing-subscription-stripeSubscriptionId"
              data-cy="stripeSubscriptionId"
              :class="{ valid: !v$.stripeSubscriptionId.$invalid, invalid: v$.stripeSubscriptionId.$invalid }"
              v-model="v$.stripeSubscriptionId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.billingSubscription.cancelAtPeriodEnd')"
              for="billing-subscription-cancelAtPeriodEnd"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="cancelAtPeriodEnd"
              id="billing-subscription-cancelAtPeriodEnd"
              data-cy="cancelAtPeriodEnd"
              :class="{ valid: !v$.cancelAtPeriodEnd.$invalid, invalid: v$.cancelAtPeriodEnd.$invalid }"
              v-model="v$.cancelAtPeriodEnd.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.billingSubscription.isActive')"
              for="billing-subscription-isActive"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="isActive"
              id="billing-subscription-isActive"
              data-cy="isActive"
              :class="{ valid: !v$.isActive.$invalid, invalid: v$.isActive.$invalid }"
              v-model="v$.isActive.$model"
              required
            />
            <div v-if="v$.isActive.$anyDirty && v$.isActive.$invalid">
              <small class="form-text text-danger" v-for="error of v$.isActive.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.billingSubscription.site')"
              for="billing-subscription-site"
            ></label>
            <select class="form-control" id="billing-subscription-site" data-cy="site" name="site" v-model="billingSubscription.site">
              <option :value="null"></option>
              <option
                :value="
                  billingSubscription.site && agentSiteOption.id === billingSubscription.site.id
                    ? billingSubscription.site
                    : agentSiteOption
                "
                v-for="agentSiteOption in agentSites"
                :key="agentSiteOption.id"
              >
                {{ agentSiteOption.id }}
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
<script lang="ts" src="./billing-subscription-update.component.ts"></script>
