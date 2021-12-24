<template>
  <div class="mainComponentWrap">
    <div class="mainProgressWrap">
      <v-progress-circular
        :size="40"
        :width="7"
        :color="APP_INFO.app_color"
        indeterminate
      ></v-progress-circular>
      <p class="loadingMsg">사용자 정보를 확인중입니다.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Loading',
  computed: {
    /* vuex : 앱 정보 */
    APP_INFO() {
      return this.$store.state.APP_INFO;
    },
  },
  data: () => ({
  }),
  methods: {
  },
  created() {
  },
};
</script>
