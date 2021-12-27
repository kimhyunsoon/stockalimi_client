import Vue from 'vue';
import Vuex from 'vuex';
import appConfig from '../config/config.json';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    APP_INFO: appConfig,
    pageStatus: 'loading',
    globalMsgModal: false,
    globalMsgContent: '',
  },
  mutations: {
    SET_APP_INFO_ROW(state, arr) {
      state.APP_INFO = {
        ...state.APP_INFO,
        [arr[0]]: arr[1],
      };
    },
    pageStatusCng(state, pageName) {
      state.pageStatus = pageName;
    },
    globalMsgModalCng(state, bool) {
      state.globalMsgModal = bool;
    },
    globalMsgContentCng(state, text) {
      state.globalMsgContent = text;
    },
    globalMsgAnimation(state, text) {
      if (state.globalMsgModal === false) {
        state.globalMsgContent = text;
        state.globalMsgModal = true;
        setTimeout(() => {
          state.globalMsgModal = false;
        }, 4000);
        setTimeout(() => {
          state.globalMsgContent = '';
        }, 4500);
      }
    },
  },
});
