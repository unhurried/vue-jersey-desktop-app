<template>
  <div>
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center mb-3">
      <h2>Call External API</h2>
    </div>
    <b-form @submit.prevent="onSubmit">
      <b-form-row class="justify-content-center">
        <b-form-group class="col-sm-9">
            <b-select v-model="target">
              <b-select-option value="httpbin">
                httpbin.com - without SSH port forwarding
              </b-select-option>
              <b-select-option value="httpbin_pf">
                httpbin.com - with SSH port forwarding
              </b-select-option>
            </b-select>
        </b-form-group>
        <b-form-group class="col-sm-3">
            <b-button block type="submit" variant="primary">Call API</b-button>
        </b-form-group>
      </b-form-row>
    </b-form>
    <h3>Result</h3>
    <b-textarea class="code" :disabled="true" rows="10" v-model="result"></b-textarea>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Axios, { AxiosStatic } from 'axios';

@Component({
  name: 'Poroxy',
  components: {},
})
export default class Proxy extends Vue {
  private result: string = '';
  private target: string = 'httpbin';

  private async onSubmit() {
    const client: AxiosStatic = await this.$store.dispatch('auth/getAxios');
    const res = await client.get(`/proxy/${this.target}/anything`);
    this.result = JSON.stringify(res.data, null, 2);
  }
}
</script>

<style scoped>
.code {
  font-size: 90%;
  font-family: 'Consolas', monospace;
}
</style>
