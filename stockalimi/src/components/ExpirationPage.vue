<template>
  <div class="mainComponentWrap">
    <div class="headerBar">
      <div class="mainTitle">
        <p>{{ APP_INFO.app_name }}</p>
        <div class="icon">
          <img src="../assets/img/notification.svg"/>
        </div>
      </div>
    </div>
    <div class="mainProgressWrap">
      <div class="errorWrap">
        <img src="../assets/img/expirationIcon.svg"/>
        <p class="errorTitle">사용기간이 만료되었습니다.</p>
        <p class="errorSub">사용기간 연장 문의는 고객센터로 부탁드립니다.</p>
        <a v-bind:href="`tel:${APP_INFO.customer_service}`" class="backBtn">고객센터로 전화하기</a>
      </div>
    </div>
  </div>
</template>

<script>
import appInformation from '../config/config.json';

export default {
  name: 'Expiration',
  data: () => ({
    APP_INFO: appInformation,
  }),
  methods: {
  },
  created() {
  },
};
</script>
