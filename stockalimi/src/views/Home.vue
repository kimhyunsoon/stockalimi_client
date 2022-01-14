<template>
  <div class="homeWrap" v-bind:class="pageStatus">
    <!-- 전역 메시지 -->
    <div class="globalMsgWrap" v-bind:class="pageStatus">
      <div class="globalMsg" v-bind:class="{ 'on':globalMsgModal }" v-html="globalMsgContent"></div>
    </div>
    <!-- 로딩 화면 -->
    <div v-if="pageStatus === 'loading'">
      <loading-page/>
    </div>
    <!-- 인증되지 않은 경우 (랜딩)-->
    <div v-else-if="pageStatus === 'landing'">
      <landing-page/>
    </div>
    <!-- 인증된 사용자일 경우 (메인)-->
    <div v-else-if="pageStatus === 'main'">
      <main-page/>
    </div>
    <!-- 만료된 사용자일 경우-->
    <div v-else-if="pageStatus === 'expiration'">
      <expiration-page/>
    </div>
    <!-- 에러가 발생했을 경우 -->
    <div v-else-if="pageStatus === 'err'">
      <error-page/>
    </div>
    <!-- 인증되지 않은 앱일 경우 -->
    <div v-else-if="pageStatus === '403'">
      <forbidden-page/>
    </div>
    <!-- <v-btn @click="localLog()">스토리지 로그</v-btn>
    <v-btn @click="localDel()">스토리지 삭제</v-btn> -->
  </div>
</template>

<script>
import { Storage } from '@capacitor/storage';
import axios from 'axios';
import LandingPage from '../components/LandingPage.vue';
import MainPage from '../components/MainPage.vue';
import ExpirationPage from '../components/ExpirationPage.vue';
import ErrorPage from '../components/ErrorPage.vue';
import ForbiddenPage from '../components/ForbiddenPage.vue';
import LoadingPage from '../components/LoadingPage.vue';

export default {
  name: 'Home',
  components: {
    LandingPage,
    MainPage,
    ExpirationPage,
    ErrorPage,
    LoadingPage,
    ForbiddenPage,
  },
  data: () => ({
  }),
  created() {
    this.getAppInformation();
  },
  computed: {
    /* vuex : 앱 정보 */
    APP_INFO() {
      return this.$store.state.APP_INFO;
    },
    /* vuex : 페이지상태 */
    pageStatus() {
      return this.$store.state.pageStatus;
    },
    /* vuex : 전역 메시지 모달 */
    globalMsgModal() {
      return this.$store.state.globalMsgModal;
    },
    /* vuex : 전역 메시지 모달 내용 */
    globalMsgContent() {
      return this.$store.state.globalMsgContent;
    },
  },
  methods: {
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
<style lang="scss" src="../assets/scss/common.scss"></style>
