<template>
  <div class="mainComponentWrap">
    <div class="mainProgressWrap">
      <div class="errorWrap">
        <img src="../assets/img/errorIcon.svg"/>
          <p class="errorTitle">오류가 발생하였습니다.</p>
          <p class="errorSub">불편을 드려 죄송합니다. 다시 시도해주세요.<br></p>
          <button class="backBtn" :style="{ backgroundColor: APP_INFO.app_color }" @click="reTryClick()">다시시도</button>
      </div>
    </div>
  </div>
</template>

<script>
import { Storage } from '@capacitor/storage';
import axios from 'axios';

export default {
  name: 'ErrorPage',
  computed: {
    /* vuex : 앱 정보 */
    APP_INFO() {
      return this.$store.state.APP_INFO;
    },
  },
  data: () => ({
  }),
  methods: {
    reTryClick() {
      this.pageStatusCng('loading');
      setTimeout(() => {
        this.getAppInformation();
      }, 500);
    },
    /* vuex : 앱 정보 업데이트 */
    /* eslint-disable-next-line */
    SET_APP_INFO_ROW: function (arr) {
      this.$store.commit('SET_APP_INFO_ROW', arr);
    },
    /* vuex : 페이지상태전환 */
    /* eslint-disable-next-line */
    pageStatusCng: function (pageName) {
      this.$store.commit('pageStatusCng', pageName);
    },
    /* vuex : 전역 메시지 모달 애니메이션 */
    /* eslint-disable-next-line */
    globalMsgAnimation: function (text) {
      this.$store.commit('globalMsgAnimation', text);
    },
    /* 앱 정보 불러오기 */
    getAppInformation() {
      axios.get(`${this.APP_INFO.server}/app`, {
        headers: {
          apikey: this.APP_INFO.api_key,
          appcode: this.APP_INFO.app_code,
        },
      })
        .then((r) => {
          if (r.data === 403) {
            this.pageStatusCng('403');
            this.globalMsgAnimation('등록되지 않은 앱입니다.');
          } else {
            const resultJSON = r.data[0];
            const keys = Object.keys(resultJSON);
            const values = Object.values(resultJSON);
            for (let i = 0; i < keys.length; i += 1) {
              this.SET_APP_INFO_ROW([keys[i], values[i]]);
            }
            this.setPageStatus();/* 유저 정보 저장하기 (로컬스토리지) */
          }
        })
        .catch((e) => {
          this.pageStatusCng('err');
          console.log(e);
        });
    },
    /* 유저 정보 저장하기 (로컬스토리지) */
    async setPageStatus() {
      const ret = await Storage.get({ key: `${this.APP_INFO.app_code}_auth` });
      const val = JSON.parse(ret.value);
      if (val === null) {
        this.pageStatusCng('landing');
      } else {
        /* 로컬스토리지에 정보가 있으면 번호 저장 + 사용자 만료 및 알림 여부 검사 */
        this.SET_APP_INFO_ROW(['phone', val.phone]);
        axios.get(`${this.APP_INFO.server}/expiration/${val.phone}`, {
          headers: {
            appcode: this.APP_INFO.app_code,
            apikey: this.APP_INFO.api_key,
          },
        })
          .then((r) => {
            this.SET_APP_INFO_ROW(['name', r.data.name]);
            this.SET_APP_INFO_ROW(['exp_date', r.data.exp_date]);
            this.SET_APP_INFO_ROW(['dday_cnt', r.data.dday_cnt]);
            this.SET_APP_INFO_ROW(['notification', r.data.notification]);
            if (r.data === 403) {
              this.pageStatusCng('403');
              this.globalMsgAnimation('등록되지 않은 앱입니다.');
            } else if (r.data === 400) {
              this.pageStatusCng('err');
              this.globalMsgAnimation('유효하지 않은 유저입니다.');
            } else if (r.data.result === false) {
              this.pageStatusCng('expiration');
            } else if (r.data.result === true) {
              this.pageStatusCng('main');
              this.globalMsgAnimation(`${r.data.name}(${val.phone})님, 반갑습니다.`);
            } else {
              this.pageStatusCng('err');
              this.globalMsgAnimation('예기치 못한 오류가 발생하였습니다.');
            }
          })
          .catch((e) => {
            this.pageStatusCng('err');
            console.log(e);
          });
      }
    },
  },
};
</script>
