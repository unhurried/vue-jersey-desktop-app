import Vue from 'vue';
import VueRouter from 'vue-router';
import Dashboard from '../views/Dashboard.vue';
import Proxy from '../views/Proxy.vue';
import Export from '../views/Export.vue';
import TodoIndex from '../views/todo/Index.vue';
import TodoList from '../views/todo/List.vue';
import TodoCreate from '../views/todo/Create.vue';
import TodoUpdate from '../views/todo/Update.vue';

Vue.use(VueRouter);

const routes = [
  { path: '/', redirect: { name: 'dashboard' } },
  { name: 'dashboard', path: '/dashboard', component: Dashboard },
  {
    path: '/todo',
    component: TodoIndex,
    children: [
      { name: 'todo/list', path: '', component: TodoList },
      { name: 'todo/create', path: 'create', component: TodoCreate },
      { name: 'todo/update', path: 'update/:userId', component: TodoUpdate },
    ],
  },
  { name: 'proxy', path: '/proxy', component: Proxy },
  { name: 'export', path: '/export', component: Export },
];

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes,
});

export default router;
