<template>
  <ValidationObserver ref="observer" v-slot="{ invalid, passes }">
    <b-form novalidate @submit.prevent="passes(onSubmit)">
      <ValidationProvider name="title"
        rules="required|max:10" v-slot="{ errors, valid, validated }">
        <b-form-group label-cols-sm="3" label="Title" label-for="title">
          <b-form-input v-model="item.title" type="text"
            :state="validated ? valid : null"></b-form-input>
          <b-form-invalid-feedback>{{ errors[0] }}</b-form-invalid-feedback>
        </b-form-group>
      </ValidationProvider>

      <ValidationProvider name="category" rules="required" v-slot="{ errors, valid, validated }">
        <b-form-group label-cols-sm="3" label="Category" label-for="category">
          <b-form-select v-model="item.category" :state="validated ? valid : null">
            <option value="one">one</option>
            <option value="two">two</option>
            <option value="three">three</option>
          </b-form-select>
          <b-form-invalid-feedback>{{ errors[0] }}</b-form-invalid-feedback>
        </b-form-group>
      </ValidationProvider>

      <ValidationProvider name="content" rules="min:1|max:255"
        v-slot="{ errors, valid, validated }">
        <b-form-group label-cols-sm="3" label="Content" label-for="content">
          <b-form-textarea rows="5"
              v-model="item.content" :state="validated ? valid : null"></b-form-textarea>
          <b-form-invalid-feedback>{{ errors[0] }}</b-form-invalid-feedback>
        </b-form-group>
      </ValidationProvider>

      <b-form-group>
        <b-row class="justify-content-center">
          <b-col sm=4>
            <b-overlay :show="isSubmitting" opacity="0.6"
                rounded spinner-small spinner-variant="primary">
              <b-button block type="submit" variant="primary">Submit</b-button>
            </b-overlay>
          </b-col>
        </b-row>
      </b-form-group>
    </b-form>
  </ValidationObserver>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { Todo, TodoCategoryEnum } from '@/client-axios';

@Component({
  name: 'Form',
  components: {},
  props: ['item', 'is-submitting'],
})
export default class Form extends Vue {
  @Prop() item!: Todo;
  @Prop() isSubmitting!: boolean;

  private onSubmit(): void {
    this.$emit('submit', this.item);
  }
}
</script>

<style scoped lang="scss">
</style>
