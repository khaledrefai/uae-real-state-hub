<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.paymentPlan.home.createOrEditLabel"
          data-cy="PaymentPlanCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.paymentPlan.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="paymentPlan.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="paymentPlan.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.paymentPlan.name')" for="payment-plan-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="payment-plan-name"
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
              v-text="t$('uaeRealStateHubApp.paymentPlan.monthsAfterHandover')"
              for="payment-plan-monthsAfterHandover"
            ></label>
            <input
              type="number"
              class="form-control"
              name="monthsAfterHandover"
              id="payment-plan-monthsAfterHandover"
              data-cy="monthsAfterHandover"
              :class="{ valid: !v$.monthsAfterHandover.$invalid, invalid: v$.monthsAfterHandover.$invalid }"
              v-model.number="v$.monthsAfterHandover.$model"
              required
            />
            <div v-if="v$.monthsAfterHandover.$anyDirty && v$.monthsAfterHandover.$invalid">
              <small class="form-text text-danger" v-for="error of v$.monthsAfterHandover.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.paymentPlan.property')" for="payment-plan-property"></label>
            <select class="form-control" id="payment-plan-property" data-cy="property" name="property" v-model="paymentPlan.property">
              <option :value="null"></option>
              <option
                :value="paymentPlan.property && propertyOption.id === paymentPlan.property.id ? paymentPlan.property : propertyOption"
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
<script lang="ts" src="./payment-plan-update.component.ts"></script>
