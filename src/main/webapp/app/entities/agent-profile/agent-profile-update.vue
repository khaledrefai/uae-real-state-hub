<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="uaeRealStateHubApp.agentProfile.home.createOrEditLabel"
          data-cy="AgentProfileCreateUpdateHeading"
          v-text="t$('uaeRealStateHubApp.agentProfile.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="agentProfile.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="agentProfile.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.agentProfile.externalUserId')"
              for="agent-profile-externalUserId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="externalUserId"
              id="agent-profile-externalUserId"
              data-cy="externalUserId"
              :class="{ valid: !v$.externalUserId.$invalid, invalid: v$.externalUserId.$invalid }"
              v-model="v$.externalUserId.$model"
              required
            />
            <div v-if="v$.externalUserId.$anyDirty && v$.externalUserId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.externalUserId.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentProfile.fullName')" for="agent-profile-fullName"></label>
            <input
              type="text"
              class="form-control"
              name="fullName"
              id="agent-profile-fullName"
              data-cy="fullName"
              :class="{ valid: !v$.fullName.$invalid, invalid: v$.fullName.$invalid }"
              v-model="v$.fullName.$model"
              required
            />
            <div v-if="v$.fullName.$anyDirty && v$.fullName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fullName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('uaeRealStateHubApp.agentProfile.companyName')"
              for="agent-profile-companyName"
            ></label>
            <input
              type="text"
              class="form-control"
              name="companyName"
              id="agent-profile-companyName"
              data-cy="companyName"
              :class="{ valid: !v$.companyName.$invalid, invalid: v$.companyName.$invalid }"
              v-model="v$.companyName.$model"
              required
            />
            <div v-if="v$.companyName.$anyDirty && v$.companyName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.companyName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentProfile.email')" for="agent-profile-email"></label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="agent-profile-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
              required
            />
            <div v-if="v$.email.$anyDirty && v$.email.$invalid">
              <small class="form-text text-danger" v-for="error of v$.email.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentProfile.phone')" for="agent-profile-phone"></label>
            <input
              type="text"
              class="form-control"
              name="phone"
              id="agent-profile-phone"
              data-cy="phone"
              :class="{ valid: !v$.phone.$invalid, invalid: v$.phone.$invalid }"
              v-model="v$.phone.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentProfile.whatsapp')" for="agent-profile-whatsapp"></label>
            <input
              type="text"
              class="form-control"
              name="whatsapp"
              id="agent-profile-whatsapp"
              data-cy="whatsapp"
              :class="{ valid: !v$.whatsapp.$invalid, invalid: v$.whatsapp.$invalid }"
              v-model="v$.whatsapp.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentProfile.country')" for="agent-profile-country"></label>
            <input
              type="text"
              class="form-control"
              name="country"
              id="agent-profile-country"
              data-cy="country"
              :class="{ valid: !v$.country.$invalid, invalid: v$.country.$invalid }"
              v-model="v$.country.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentProfile.city')" for="agent-profile-city"></label>
            <input
              type="text"
              class="form-control"
              name="city"
              id="agent-profile-city"
              data-cy="city"
              :class="{ valid: !v$.city.$invalid, invalid: v$.city.$invalid }"
              v-model="v$.city.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentProfile.website')" for="agent-profile-website"></label>
            <input
              type="text"
              class="form-control"
              name="website"
              id="agent-profile-website"
              data-cy="website"
              :class="{ valid: !v$.website.$invalid, invalid: v$.website.$invalid }"
              v-model="v$.website.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('uaeRealStateHubApp.agentProfile.active')" for="agent-profile-active"></label>
            <input
              type="checkbox"
              class="form-check"
              name="active"
              id="agent-profile-active"
              data-cy="active"
              :class="{ valid: !v$.active.$invalid, invalid: v$.active.$invalid }"
              v-model="v$.active.$model"
              required
            />
            <div v-if="v$.active.$anyDirty && v$.active.$invalid">
              <small class="form-text text-danger" v-for="error of v$.active.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
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
<script lang="ts" src="./agent-profile-update.component.ts"></script>
