<template>
  <div v-if="certificationStatus === true">
    <main-page/>
    <div class="footerMsg" v-bind:class="{ 'on' : joinSuccess }">
      <p>등록이 완료되었습니다.</p>
    </div>
  </div>
  <div v-else>
    <randing-page joinSuccess="joinSuccess"/>
  </div>
</template>

<script>
import { Storage } from '@capacitor/storage';
import RandingPage from '../components/RandingPage.vue';
import MainPage from '../components/MainPage.vue';

export default {
  name: 'Home',
  components: {
    RandingPage,
    MainPage,
  },
  created() {
    this.localCheck();
  },
  computed: {
    certificationStatus() {
      return this.$store.state.certificationStatus;
    },
    joinSuccess() {
      return this.$store.state.joinSuccess;
    },
  },
  methods: {
    /* vuex : 인증여부전환 (랜딩-메인) */
    /* eslint-disable-next-line */
    certificationStatusCng: function (bool) {
      this.$store.commit('certificationStatusCng', bool);
    },
    /* vuex : 등록 메시지 */
    /* eslint-disable-next-line */
    joinSuccessCng: function (bool) {
      this.$store.commit('joinSuccessCng', bool);
    },
    async localCheck() {
      const ret = await Storage.get({ key: 'certification' });
      const certification = ret.value;
      if (certification === 'confirmed') {
        this.certificationStatusCng(true);
      } else {
        this.certificationStatusCng(false);
      }
    },
    async localDel() {
      await Storage.remove({ key: 'certification' });
    },
  },
};
</script>
<style lang="scss" src="../assets/scss/common.scss"></style>
