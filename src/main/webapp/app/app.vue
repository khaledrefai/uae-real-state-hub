<template>
  <div id="app">
    <ribbon></ribbon>
    <div id="app-header">
      <jhi-navbar v-if="isAdmin"></jhi-navbar>
      <div v-else-if="!isStorefrontRoute" class="public-topbar">
        <router-link class="public-brand" to="/">Real Estate Hub</router-link>
        <div class="public-actions">
          <router-link v-if="isAuthenticated" class="btn btn-outline-light btn-sm mr-2" to="/agent/dashboard">Dashboard</router-link>
          <router-link v-else class="btn btn-primary btn-sm mr-2" to="/agent/register">Create workspace</router-link>
          <button v-if="isAuthenticated" type="button" class="btn btn-link btn-sm text-light" @click="logout">Sign out</button>
          <button v-else type="button" class="btn btn-link btn-sm text-light" @click="showLogin">Sign in</button>
        </div>
      </div>
    </div>
    <div
      class="app-shell"
      :class="{ 'admin-shell': isAdmin, 'public-shell': !isAdmin && !isStorefrontRoute, 'storefront-shell': isStorefrontRoute }"
    >
      <div class="card jh-card app-route-card" :class="{ 'is-public-route': !isAdmin }">
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
  position: sticky;
  top: 0;
  z-index: 1020;
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 4.25rem;
  padding: 0.75rem clamp(1rem, 4vw, 3rem);
  background: rgba(10, 31, 50, 0.96);
  color: #fff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 12px 32px rgba(7, 20, 34, 0.16);
  backdrop-filter: blur(14px);
}
.public-brand {
  display: inline-flex;
  align-items: center;
  gap: 0.65rem;
  font-weight: 800;
  font-size: 1rem;
  color: #fff;
  text-decoration: none;
  letter-spacing: 0;
}
.public-brand::before {
  content: '';
  width: 2rem;
  height: 2rem;
  border-radius: 0.65rem;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.72), rgba(255, 255, 255, 0.18)), linear-gradient(135deg, #0f766e, #c8a95a);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.45);
}
.public-brand:hover {
  color: #fff;
  text-decoration: none;
}
.public-actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.public-actions .btn {
  margin-left: 0;
  border-radius: 0.6rem;
  font-weight: 700;
  padding: 0.48rem 0.9rem;
}
.public-actions .btn-primary {
  background: #0f766e;
  border-color: #0f766e;
  box-shadow: 0 10px 24px rgba(15, 118, 110, 0.26);
}
.public-actions .btn-outline-light {
  border-color: rgba(255, 255, 255, 0.38);
  color: #fff;
}
.app-shell.public-shell,
.app-shell.storefront-shell {
  background: #f7f4ed;
}
.app-route-card.is-public-route {
  padding: 0;
  margin: 0;
  border: 0;
  border-radius: 0;
  background: transparent;
  box-shadow: none;
}

@media (max-width: 575.98px) {
  .public-topbar {
    align-items: flex-start;
    flex-direction: column;
    gap: 0.8rem;
  }

  .public-actions {
    width: 100%;
    justify-content: space-between;
  }

  .public-actions .btn {
    flex: 1 1 auto;
  }
}
</style>
