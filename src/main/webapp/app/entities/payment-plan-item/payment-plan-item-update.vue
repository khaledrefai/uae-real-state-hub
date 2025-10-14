<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.paymentPlanItem.home.createOrEditLabel"
          data-cy="PaymentPlanItemCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.paymentPlanItem.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="paymentPlanItem.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="paymentPlanItem.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.paymentPlanItem.orderNo')"
              for="payment-plan-item-orderNo"
            ></label>
            <input
              type="number"
              class="form-control"
              name="orderNo"
              id="payment-plan-item-orderNo"
              data-cy="orderNo"
              :class="{ valid: !v$.orderNo.$invalid, invalid: v$.orderNo.$invalid }"
              v-model.number="v$.orderNo.$model"
              required
            />
            <div v-if="v$.orderNo.$anyDirty && v$.orderNo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.orderNo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.paymentPlanItem.paymentTime')"
              for="payment-plan-item-paymentTime"
            ></label>
            <input
              type="text"
              class="form-control"
              name="paymentTime"
              id="payment-plan-item-paymentTime"
              data-cy="paymentTime"
              :class="{ valid: !v$.paymentTime.$invalid, invalid: v$.paymentTime.$invalid }"
              v-model="v$.paymentTime.$model"
              required
            />
            <div v-if="v$.paymentTime.$anyDirty && v$.paymentTime.$invalid">
              <small class="form-text text-danger" v-for="error of v$.paymentTime.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.paymentPlanItem.percentOfPayment')"
              for="payment-plan-item-percentOfPayment"
            ></label>
            <input
              type="text"
              class="form-control"
              name="percentOfPayment"
              id="payment-plan-item-percentOfPayment"
              data-cy="percentOfPayment"
              :class="{ valid: !v$.percentOfPayment.$invalid, invalid: v$.percentOfPayment.$invalid }"
              v-model="v$.percentOfPayment.$model"
              required
            />
            <div v-if="v$.percentOfPayment.$anyDirty && v$.percentOfPayment.$invalid">
              <small class="form-text text-danger" v-for="error of v$.percentOfPayment.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.paymentPlanItem.plan')" for="payment-plan-item-plan"></label>
            <select class="form-control" id="payment-plan-item-plan" data-cy="plan" name="plan" v-model="paymentPlanItem.plan">
              <option :value="null"></option>
              <option
                :value="paymentPlanItem.plan && paymentPlanOption.id === paymentPlanItem.plan.id ? paymentPlanItem.plan : paymentPlanOption"
                v-for="paymentPlanOption in paymentPlans"
                :key="paymentPlanOption.id"
              >
                {{ paymentPlanOption.id }}
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
<script lang="ts" src="./payment-plan-item-update.component.ts"></script>
