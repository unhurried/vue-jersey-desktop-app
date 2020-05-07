<template>
  <div>
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center mb-3">
      <h2>Export as Excel file</h2>
    </div>
    <b-form @submit.prevent="onSubmit">
      <b-form-group>
        <b-row class="justify-content-center">
          <b-button class="col-sm-4" type="submit" variant="primary">Export</b-button>
          <a id="exportLink" class="d-none" download="out.xlsx" :href="exportHref" />
        </b-row>
      </b-form-group>
    </b-form>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Axios from 'axios';

@Component({
  name: 'Export',
  components: {},
})
export default class Export extends Vue {
  private exportHref: string | null = null;

  /* eslint class-methods-use-this: 0 */
  private async onSubmit() {
    const res = await Axios.get('/api/export/', { responseType: 'blob' });
    const blob = new Blob([res.data], { type: 'blob' });
    if (window.navigator.msSaveBlob) {
      window.navigator.msSaveBlob(blob, 'out.xlsx');
    } else {
      this.exportHref = window.URL.createObjectURL(blob);
      this.$nextTick(() => {
        const a = document.getElementById('exportLink');
        if (a) {
          a.click();
        }
      });
    }
  }
}
</script>
