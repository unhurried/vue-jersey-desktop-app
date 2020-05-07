<template>
  <div>
    <b-navbar type="dark" variant="dark" toggleable="lg">
      <b-navbar-brand to="/" class="mr-auto">Vue.js + Jersey Desktop App</b-navbar-brand>

      <b-button class="navbar-toggler d-md-none" v-b-toggle.menu>
        <span class="navbar-toggler-icon"></span>
      </b-button>
    </b-navbar>

    <b-container fluid style="max-width:1024px; margin-left:0;">
      <b-row>
        <b-collapse
          class="col-md-3 navbar
          d-md-block bg-light align-items-start sidebar" id="menu">
          <b-nav class="flex-column">
            <b-nav-item :to="{ name: 'todo/list' }">Todo</b-nav-item>
            <b-nav-item :to="{ name: 'proxy' }">API Proxy</b-nav-item>
            <b-nav-item :to="{ name: 'export' }">Export</b-nav-item>
          </b-nav>
        </b-collapse>
        <b-col class="m-3">
          <router-view />
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component({
  name: 'App',
  components: {},
})
export default class App extends Vue {}
</script>

<style lang="scss">
  /* Set the height of sidebar to fit to the viewport height. */
  @media (min-width: 768px) {
    .sidebar {
      min-height: calc(100vh - 56px);
      overflow: hidden;
    }
  }
</style>
