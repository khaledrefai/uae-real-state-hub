<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.agentSite.home.createOrEditLabel"
          data-cy="AgentSiteCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.agentSite.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="agentSite.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="agentSite.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentSite.slug')" for="agent-site-slug"></label>
            <input
              type="text"
              class="form-control"
              name="slug"
              id="agent-site-slug"
              data-cy="slug"
              :class="{ valid: !v$.slug.$invalid, invalid: v$.slug.$invalid }"
              v-model="v$.slug.$model"
              required
            />
            <div v-if="v$.slug.$anyDirty && v$.slug.$invalid">
              <small class="form-text text-danger" v-for="error of v$.slug.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentSite.displayName')" for="agent-site-displayName"></label>
            <input
              type="text"
              class="form-control"
              name="displayName"
              id="agent-site-displayName"
              data-cy="displayName"
              :class="{ valid: !v$.displayName.$invalid, invalid: v$.displayName.$invalid }"
              v-model="v$.displayName.$model"
              required
            />
            <div v-if="v$.displayName.$anyDirty && v$.displayName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.displayName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentSite.theme')" for="agent-site-theme"></label>
            <input
              type="text"
              class="form-control"
              name="theme"
              id="agent-site-theme"
              data-cy="theme"
              :class="{ valid: !v$.theme.$invalid, invalid: v$.theme.$invalid }"
              v-model="v$.theme.$model"
              required
            />
            <div v-if="v$.theme.$anyDirty && v$.theme.$invalid">
              <small class="form-text text-danger" v-for="error of v$.theme.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.agentSite.primaryColor')"
              for="agent-site-primaryColor"
            ></label>
            <input
              type="text"
              class="form-control"
              name="primaryColor"
              id="agent-site-primaryColor"
              data-cy="primaryColor"
              :class="{ valid: !v$.primaryColor.$invalid, invalid: v$.primaryColor.$invalid }"
              v-model="v$.primaryColor.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.agentSite.secondaryColor')"
              for="agent-site-secondaryColor"
            ></label>
            <input
              type="text"
              class="form-control"
              name="secondaryColor"
              id="agent-site-secondaryColor"
              data-cy="secondaryColor"
              :class="{ valid: !v$.secondaryColor.$invalid, invalid: v$.secondaryColor.$invalid }"
              v-model="v$.secondaryColor.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentSite.logoUrl')" for="agent-site-logoUrl"></label>
            <input
              type="text"
              class="form-control"
              name="logoUrl"
              id="agent-site-logoUrl"
              data-cy="logoUrl"
              :class="{ valid: !v$.logoUrl.$invalid, invalid: v$.logoUrl.$invalid }"
              v-model="v$.logoUrl.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.agentSite.contactEmail')"
              for="agent-site-contactEmail"
            ></label>
            <input
              type="text"
              class="form-control"
              name="contactEmail"
              id="agent-site-contactEmail"
              data-cy="contactEmail"
              :class="{ valid: !v$.contactEmail.$invalid, invalid: v$.contactEmail.$invalid }"
              v-model="v$.contactEmail.$model"
              required
            />
            <div v-if="v$.contactEmail.$anyDirty && v$.contactEmail.$invalid">
              <small class="form-text text-danger" v-for="error of v$.contactEmail.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.agentSite.contactPhone')"
              for="agent-site-contactPhone"
            ></label>
            <input
              type="text"
              class="form-control"
              name="contactPhone"
              id="agent-site-contactPhone"
              data-cy="contactPhone"
              :class="{ valid: !v$.contactPhone.$invalid, invalid: v$.contactPhone.$invalid }"
              v-model="v$.contactPhone.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.agentSite.contactWhatsapp')"
              for="agent-site-contactWhatsapp"
            ></label>
            <input
              type="text"
              class="form-control"
              name="contactWhatsapp"
              id="agent-site-contactWhatsapp"
              data-cy="contactWhatsapp"
              :class="{ valid: !v$.contactWhatsapp.$invalid, invalid: v$.contactWhatsapp.$invalid }"
              v-model="v$.contactWhatsapp.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentSite.domain')" for="agent-site-domain"></label>
            <input
              type="text"
              class="form-control"
              name="domain"
              id="agent-site-domain"
              data-cy="domain"
              :class="{ valid: !v$.domain.$invalid, invalid: v$.domain.$invalid }"
              v-model="v$.domain.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentSite.isActive')" for="agent-site-isActive"></label>
            <input
              type="checkbox"
              class="form-check"
              name="isActive"
              id="agent-site-isActive"
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
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentSite.owner')" for="agent-site-owner"></label>
            <select class="form-control" id="agent-site-owner" data-cy="owner" name="owner" v-model="agentSite.owner">
              <option :value="null"></option>
              <option
                :value="agentSite.owner && agentProfileOption.id === agentSite.owner.id ? agentSite.owner : agentProfileOption"
                v-for="agentProfileOption in agentProfiles"
                :key="agentProfileOption.id"
              >
                {{ agentProfileOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentSite.plan')" for="agent-site-plan"></label>
            <select class="form-control" id="agent-site-plan" data-cy="plan" name="plan" v-model="agentSite.plan">
              <option :value="null"></option>
              <option
                :value="agentSite.plan && subscriptionPlanOption.id === agentSite.plan.id ? agentSite.plan : subscriptionPlanOption"
                v-for="subscriptionPlanOption in subscriptionPlans"
                :key="subscriptionPlanOption.id"
              >
                {{ subscriptionPlanOption.id }}
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
<script lang="ts" src="./agent-site-update.component.ts"></script>
