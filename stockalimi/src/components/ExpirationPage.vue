<template>
  <div class="landingOuterWrap">
    <div class="mainProgressWrap">
      <div class="errorWrap">
        <img src="../assets/img/expirationIcon.svg"/>
        <p class="errorTitle">사용기간이 만료되었습니다.</p>
        <p class="errorSub">연장을 원하시면 아래 버튼을 통해 문의요청을 남겨주세요.</p>
        <v-btn @click="contactSend()" class="backBtn">문의요청 남기기</v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Expiration',
  computed: {
    /* vuex : 앱 정보 */
    APP_INFO() {
      return this.$store.state.APP_INFO;
    },
  },
  data: () => ({
  }),
  methods: {
    /* vuex : 전역 메시지 모달 애니메이션 */
    /* eslint-disable-next-line */
    globalMsgAnimation: function (text) {
      this.$store.commit('globalMsgAnimation', text);
    },
    contactSend() {
      const data = {
        name: this.APP_INFO.name,
        phone: this.APP_INFO.phone,
      };
      axios.post(`${this.APP_INFO.server}/contact`, data, {
        headers: {
          appcode: this.APP_INFO.app_code,
          apikey: this.APP_INFO.api_key,
        },
      })
        .then((r) => {
          if (r.data === true) {
            this.globalMsgAnimation(`${this.APP_INFO.name}(${this.APP_INFO.phone})님,<br>문의 요청이 등록되었습니다.<br>빠른 시일안에 연락드리겠습니다.`);
          } else if (r.data === 403) {
            this.globalMsgAnimation('오류가 발생하였습니다. 다시 시도해주세요.');
          } else {
            this.globalMsgAnimation('오류가 발생하였습니다. 다시 시도해주세요.');
          }
        })
        .catch((e) => {
          this.globalMsgAnimation('오류가 발생하였습니다. 다시 시도해주세요.');
          console.log(e);
        });
    },
  },
  created() {
  },
};
</script>
