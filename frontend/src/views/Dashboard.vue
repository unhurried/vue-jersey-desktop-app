<template>
  <div>
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center mb-3">
      <h2>Port Forwarding Config</h2>
    </div>
    <b-overlay :show="isLoading" no-fade>
      <ValidationObserver ref="observer" v-slot="{ invalid, passes }">
        <b-form novalidate @submit.prevent="passes(onSubmit)" ref="formContainer">
          <ValidationProvider name="host"
            rules="required|max:64" v-slot="{ errors, valid, validated }">
            <b-form-group label-cols-sm="3" label="Host" label-for="host">
              <b-form-input v-model="config.host" type="text"
                :state="validated ? valid : null"></b-form-input>
              <b-form-invalid-feedback>{{ errors[0] }}</b-form-invalid-feedback>
            </b-form-group>
          </ValidationProvider>
        </b-form>
        <b-form novalidate @submit.prevent="passes(onSubmit)">
          <ValidationProvider name="port"
            rules="required|max:64" v-slot="{ errors, valid, validated }">
            <b-form-group label-cols-sm="3" label="Port" label-for="port">
              <b-form-input v-model="config.port" type="number"
                :state="validated ? valid : null"></b-form-input>
              <b-form-invalid-feedback>{{ errors[0] }}</b-form-invalid-feedback>
            </b-form-group>
          </ValidationProvider>
        </b-form>
        <b-form novalidate @submit.prevent="passes(onSubmit)">
          <ValidationProvider name="username"
            rules="required|max:64" v-slot="{ errors, valid, validated }">
            <b-form-group label-cols-sm="3" label="Username" label-for="username">
              <b-form-input v-model="config.username" type="text"
                :state="validated ? valid : null"></b-form-input>
              <b-form-invalid-feedback>{{ errors[0] }}</b-form-invalid-feedback>
            </b-form-group>
          </ValidationProvider>
          <ValidationProvider name="password"
            rules="required|max:64" v-slot="{ errors, valid, validated }">
            <b-form-group label-cols-sm="3" label="Password" label-for="password">
              <b-form-input v-model="config.password" type="password"
                :state="validated ? valid : null"></b-form-input>
              <b-form-invalid-feedback>{{ errors[0] }}</b-form-invalid-feedback>
            </b-form-group>
          </ValidationProvider>

          <b-form-group>
            <b-row class="justify-content-center">
              <b-button class="col-sm-4" type="submit" variant="primary">Submit</b-button>
            </b-row>
          </b-form-group>
        </b-form>
      </ValidationObserver>
    </b-overlay>
  </div>
</template>

<script lang="ts">
import { Component, Mixins, Vue } from 'vue-property-decorator';
import Axios, { AxiosStatic } from 'axios';
import Toast from '@/mixins/Toast';

@Component({
  name: 'Dashboard',
  components: {},
})
export default class Proxy extends Mixins(Toast) {
  private config = {
    host: '',
    port: 0,
    username: '',
    password: '',
  };
  private message: string = '';
  private isLoading: boolean = true;

  private async created(): Promise<void> {
    await this.refresh();
    this.isLoading = false;
  }

  private async onSubmit(item: any) {
    const client: AxiosStatic = await this.$store.dispatch('auth/getAxios');
    try {
      const res = await client.put('/pf_config', this.config);
      this.toast('Successfully updated.', 'success');
      this.refresh();
    } catch (error) {
      this.toast('Failed to update data.', 'danger');
    }
  }
  private async refresh(): Promise<void> {
    const client: AxiosStatic = await this.$store.dispatch('auth/getAxios');
    try {
      const res = await client.get('/pf_config');
      this.config = res.data;
    } catch (error) {
      this.toast('Failed to load data.', 'danger');
    }
  }
}
</script>

<style scoped>
</style>
