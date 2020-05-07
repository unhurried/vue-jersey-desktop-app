import {
  ActionTree, GetterTree, Module, MutationTree,
} from 'vuex';
import Axios from 'axios';
import { TodoApi } from '@/client-axios';
import { AuthState, RootState } from '../type';

const actions: ActionTree<AuthState, RootState> = {
  // Create API client instance in an auth action so that it can be integrated with user
  // authentication in the future.
  getApi: async ({ state }) => new TodoApi({
    basePath: process.env.VUE_APP_BASE_URI,
  }),
  getAxios: async ({ state }) => Axios.create({
    baseURL: process.env.VUE_APP_BASE_URI,
  }),
};

const AuthModule: Module<AuthState, RootState> = {
  namespaced: true,
  actions,
  modules: {},
};
export default AuthModule;
