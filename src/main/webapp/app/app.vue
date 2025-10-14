<template>
  <div id="app">
    <ribbon></ribbon>
    <div id="app-header">
      <jhi-navbar v-if="isAdmin"></jhi-navbar>
      <div v-else class="public-topbar">
        <router-link class="public-brand" to="/">Real Estate Hub</router-link>
        <div class="public-actions">
          <router-link v-if="isAuthenticated" class="btn btn-outline-light btn-sm mr-2" to="/agent/dashboard">Dashboard</router-link>
          <router-link v-else class="btn btn-primary btn-sm mr-2" to="/agent/register">Create workspace</router-link>
          <button v-if="isAuthenticated" type="button" class="btn btn-link btn-sm text-light" @click="logout">Sign out</button>
          <button v-else type="button" class="btn btn-link btn-sm text-light" @click="showLogin">Sign in</button>
        </div>
      </div>
    </div>
    <div class="container-fluid">
      <div class="card jh-card">
        <router-view></router-view>
      </div>
      <b-modal id="login-page" v-model="loginModalOpen" hide-footer lazy>
        <template #modal-title>
          <span data-cy="loginTitle" id="login-title" v-text="t$('login.title')"></span>
        </template>
        <login-form></login-form>
      </b-modal>

      <jhi-footer v-if="isAdmin"></jhi-footer>
    </div>
  </div>
</template>

<script lang="ts" src="./app.component.ts"></script>

<style scoped>
.public-topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem 1.5rem;
  background: #0d2346;
  color: #fff;
}
.public-brand {
  font-weight: 600;
  font-size: 1.1rem;
  color: #fff;
  text-decoration: none;
}
.public-brand:hover {
  color: #fff;
  text-decoration: none;
}
.public-actions {
  display: flex;
  align-items: center;
}
.public-actions .btn {
  margin-left: 0.5rem;
}
</style>
