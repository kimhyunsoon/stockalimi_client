import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    certificationStatus: false,
    joinSuccess: false,
  },
  mutations: {
    certificationStatusCng(state, bool) {
      state.certificationStatus = bool;
    },
    joinSuccessCng(state, bool) {
      state.joinSuccess = bool;
    },
  },
});
