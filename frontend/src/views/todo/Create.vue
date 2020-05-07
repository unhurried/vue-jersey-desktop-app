<template>
  <div>
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center mb-3">
      <h2>Create a new Todo item</h2>
      <b-button variant="secondary" :to="{ name: 'todo/list' }">Back to List</b-button>
    </div>
    <Form :item="todo" @submit="onSubmit"/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Form from '@/components/todo/Form.vue';
import { Todo, TodoApi, TodoCategoryEnum } from '@/client-axios';

@Component({
  name: 'Create',
  components: {
    Form,
  },
})
export default class Create extends Vue {
  private todo: Todo = { title: '', category: TodoCategoryEnum.One };

  private async onSubmit(item: Todo) {
    const todoApi: TodoApi = await this.$store.dispatch('auth/getApi');
    const result = await todoApi.todosPost({
      title: item.title,
      category: item.category,
      content: item.content,
    });
    this.$router.push({ name: 'todo/update', params: { userId: result.data.id as string } });
  }
}
</script>
