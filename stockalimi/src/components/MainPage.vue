<template>
  <div class="mainComponentWrap">
    <div class="pushModalWrap" v-bind:class="{ 'on':pushModal }">
      <div class="pushModalBox" :style="{ borderColor: APP_INFO.app_color+80 }">
        <div class="top">
          <div class="icon" :style="{ backgroundColor: APP_INFO.app_color }">
            <img src="../assets/img/notification.svg"/>
          </div>
          <p class="tit">{{ pushData.title }}</p>
        </div>
        <p class="body">{{ pushData.body }}</p>
      </div>
    </div>
    <div class="headerBar">
      <div class="mainTitle">
        <p>{{ APP_INFO.app_name }}</p>
        <div class="icon" :style="{ backgroundColor : APP_INFO.app_color }">
          <img src="../assets/img/notification.svg"/>
        </div>
      </div>
      <div
        class="alarmStatus"
        @click="setNotification"
      >
        <p v-bind:class="[ APP_INFO.notification ? 'on' : 'off' ]">
          {{`${APP_INFO.notification?'알림 ON':'알림 OFF'}`}}
        </p>

        <v-container
          class="px-0"
          fluid
        >
          <v-switch
            v-model="APP_INFO.notification"
          ></v-switch>
        </v-container>
      </div>
    </div>
    <!--토글 버튼: 주식정보-->
    <div class="toggleBtn"
        :style="{ backgroundColor : APP_INFO.app_color }"
        :class="{ 'on': getMainPageStatus('stockData') }"
        @click="gotoNotiList()"
    >
      <img src="../assets/img/notiWhite.svg"/>
    </div>
    <!--토글 버튼: 알림목록-->
    <div class="toggleBtn"
        :style="{ backgroundColor : APP_INFO.app_color }"
        :class="{ 'on': getMainPageStatus('notiList') }"
        @click="gotoStockData()"
    >
      <img src="../assets/img/stockWhite.svg"/>
    </div>
    <!--페이지상태: 주식정보-->
    <div v-if="mainPageStatus==='stockData'">
      <div class="refreshBtn" @click="refreshClick($event)">
        <img src="../assets/img/refresh.svg"/>
      </div>
      <div v-if="stockData==false" class="mainProgressWrap">
        <v-progress-circular
          :size="40"
          :width="7"
          :color="APP_INFO.app_color"
          indeterminate
        ></v-progress-circular>
        <p class="loadingMsg">주식정보를 불러오는중입니다.</p>
      </div>
      <div v-else-if="stockData=='err'" class="mainProgressWrap">
        <div class="errorWrap">
          <img src="../assets/img/errorIcon.svg"/>
          <p class="errorTitle">오류가 발생하였습니다.</p>
          <p class="errorSub">불편을 드려 죄송합니다. 다시 시도해주세요.</p>
          <button class="backBtn" :style="{ backgroundColor : APP_INFO.app_color }" @click="refreshClick($event)">다시 시도</button>
        </div>
      </div>
      <div v-else-if="stockData" class="cardWrap">
        <div class="card" v-bind:class="kospiDnUp">
          <div class="cardTitle">
            <p class="tit">코스피<b>KOSPI</b></p>
            <p class="price">
              {{stockData.kospiData.price}}
            </p>
          </div>
          <div class="graphWrap">
            <img :src="stockData.kospiData.graph"/>
            <div class="data">
              <p class="priceNum">
                <b v-if="kospiDnUp=='dn'">▼</b>
                <b v-else-if="kospiDnUp=='up'">▲</b>
                {{stockData.kospiData.daytodayPrice}}
              </p>
              <p class="per">
                {{stockData.kospiData.daytodayPer}}
              </p>
            </div>
          </div>
        </div>
        <div class="card" v-bind:class="kosdaqDnUp">
          <div class="cardTitle">
            <p class="tit">코스닥<b>KOSDAQ</b></p>
            <p class="price">
              {{stockData.kosdaqData.price}}
            </p>
          </div>
          <div class="graphWrap">
            <img :src="stockData.kosdaqData.graph"/>
            <div class="data">
              <p class="priceNum">
                <b v-if="kosdaqDnUp=='dn'">▼</b>
                <b v-else-if="kosdaqDnUp=='up'">▲</b>
                {{stockData.kosdaqData.daytodayPrice}}
              </p>
              <p class="per">
                {{stockData.kosdaqData.daytodayPer}}
              </p>
            </div>
          </div>
        </div>
        <div class="upStockRacking">
          <p class="tit">거래상위</p>
          <div class="listCard"
            v-for="(value, key) in stockData.stockList" v-bind:key="key"
            v-bind:class="[ value.daytodayType == '하락' ? 'dn' : 'up' ]"
          >
            <div class="listCardTit">
              <b>{{key}}. </b> {{value.name}}
            </div>
            <div class="listInfo">
              <p class="price">{{value.price}}</p>
              <p class="daytodayPrice">
                <b v-if="value.daytodayType=='하락'">▼</b>
                <b v-else-if="value.daytodayType=='상승'">▲</b>
                {{value.daytodayPrice}}
              </p>
              <p class="daytodayPer">{{value.daytodayPer}}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--페이지상태: 알림목록-->
    <div v-else-if="mainPageStatus==='notiList'">
      <div class="refreshBtn" @click="refreshClick($event)">
        <img src="../assets/img/refresh.svg"/>
      </div>
      <div v-if="notiList==false" class="mainProgressWrap">
        <v-progress-circular
          :size="40"
          :width="7"
          :color="APP_INFO.app_color"
          indeterminate
        ></v-progress-circular>
        <p class="loadingMsg">알림기록을 불러오는중입니다.</p>
      </div>
      <div v-else-if="notiList=='err'" class="mainProgressWrap">
        <div class="errorWrap">
          <img src="../assets/img/errorIcon.svg"/>
          <p class="errorTitle">오류가 발생하였습니다.</p>
          <p class="errorSub">불편을 드려 죄송합니다. 다시 시도해주세요.</p>
          <button class="backBtn" :style="{ backgroundColor : APP_INFO.app_color }" @click="refreshClick($event)">다시 시도</button>
        </div>
      </div>
      <div v-else-if="notiList">
        <div class="notiListWrap">
          <p class="h1">알림기록</p>
          <div v-if="Object.keys(notiListData).length == 0">
            <p class="noListMsg">아직 수신된 알림이 없습니다.</p>
          </div>
          <div v-else>
            <div v-for="(value, key) in notiListData" :key="key" class="row">
              <div class="top">
                <p class="tit">{{value.title}}</p>
                <p class="date">{{ dateToMoment(value.push_date) }}</p>
              </div>
              <pre class="content">{{value.body}}</pre>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--알림 끄기 확인 모달-->
    <v-bottom-sheet v-model="notificationOffConfirm" persistent class="noAlarmWrap">
      <v-sheet
        class="noAlarmCon"
        height="auto"
        width="100vw"
      >
        <div class="noAlarmMsg">
          급등주에 대한 푸쉬알림을 거부합니다.<br>
          정말 알림을 끄시겠습니까?
        </div>
        <div class="noAlarmBtnWrap">
          <v-btn
            class="off"
            @click="notificationOffConfirm = false, notificationOffEvent()"
          >
            알림 끄기
          </v-btn>
          <v-btn
            class="cancle"
            @click="notificationOffConfirm = false, notificationStatusCng(true)"
          >
            취소
          </v-btn>
        </div>
      </v-sheet>
    </v-bottom-sheet>
  </div>
</template>

<script>
import axios from 'axios';
import { FCM } from '@capacitor-community/fcm';
import { io } from 'socket.io-client';
import moment from 'moment';

export default {
  name: 'Main',
  computed: {
    /* vuex : 앱 정보 */
    APP_INFO() {
      return this.$store.state.APP_INFO;
    },
  },
  data: () => ({
    mainPageStatus: 'stockData',
    stockData: false,
    notiList: false,
    notiListData: [],
    kospiDnUp: '',
    kosdaqDnUp: '',
    notificationOffConfirm: false,
    pushModal: false,
    pushData: { title: '', body: '' },
  }),
  methods: {
    /* vuex : 페이지상태전환 */
    /* eslint-disable-next-line */
    pageStatusCng: function (pageName) {
      this.$store.commit('pageStatusCng', pageName);
    },
    /* vuex : 앱 정보 업데이트 (알람용) */
    /* eslint-disable-next-line */
    SET_APP_INFO_ROW: function (arr) {
      this.$store.commit('SET_APP_INFO_ROW', arr);
    },
    /* vuex : 전역 메시지 모달 애니메이션 */
    /* eslint-disable-next-line */
    globalMsgAnimation: function (text) {
      this.$store.commit('globalMsgAnimation', text);
    },
    /* 주식정보 - 알림기록 화면상태 가져오기 */
    getMainPageStatus(status) {
      return this.mainPageStatus === status;
    },
    /* 주식정보 - 알림기록 화면상태 전환 */
    setMainPageStatus(status) {
      this.mainPageStatus = status;
    },
    /* 알림 ON OFF */
    notificationStatusCng(bool) {
      this.SET_APP_INFO_ROW(['notification', bool]);
    },
    /* 알림기록 가져오기 */
    getNotiList() {
      this.notiList = false;
      setTimeout(() => {
        axios.get(`${this.APP_INFO.server}/notification/${this.APP_INFO.phone}`, {
          headers: {
            apikey: this.APP_INFO.api_key,
            appcode: this.APP_INFO.app_code,
          },
        })
          .then((r) => {
            this.notiList = true;
            this.notiListData = [];
            const keys = Object.keys(JSON.parse(JSON.stringify(r.data)));
            const values = Object.values(JSON.parse(JSON.stringify(r.data)));
            for (let i = 0; i < keys.length; i += 1) {
              this.notiListData = [
                ...this.notiListData,
                values[i],
              ];
            }
          })
          .catch((e) => {
            this.notiList = 'err';
            console.log(e);
          });
      }, 600);
    },
    /* 주식정보 가져오기 */
    getStockData() {
      this.stockData = false;
      setTimeout(() => {
        axios.get(`${this.APP_INFO.server}/stock`, {
          headers: {
            apikey: this.APP_INFO.api_key,
            appcode: this.APP_INFO.app_code,
          },
        })
          .then((r) => {
            this.stockData = r.data;
            if (this.stockData.kospiData.daytodayType === '하락') {
              this.kospiDnUp = 'dn';
            } else if (this.stockData.kospiData.daytodayType === '상승') {
              this.kospiDnUp = 'up';
            } else {
              this.kospiDnUp = '';
            }
            if (this.stockData.kosdaqData.daytodayType === '하락') {
              this.kosdaqDnUp = 'dn';
            } else if (this.stockData.kosdaqData.daytodayType === '상승') {
              this.kosdaqDnUp = 'up';
            } else {
              this.kosdaqDnUp = '';
            }
          })
          .catch((e) => {
            this.stockData = 'err';
            console.log(e);
          });
      }, 600);
    },
    /* 알림기록 화면으로 이동 */
    gotoNotiList() {
      axios.get(`${this.APP_INFO.server}/expiration/${this.APP_INFO.phone}`, {
        headers: {
          appcode: this.APP_INFO.app_code,
          apikey: this.APP_INFO.api_key,
        },
      })
        .then((r) => {
          if (r.data === 403) {
            this.pageStatusCng('403');
            this.globalMsgAnimation('등록되지 않은 앱입니다.');
          } else if (r.data === 400) {
            this.pageStatusCng('error');
            this.globalMsgAnimation('유효하지 않은 유저입니다.');
          } else if (r.data.result === false) {
            this.pageStatusCng('expiration');
          } else if (r.data.result === true) {
            this.mainPageStatus = 'notiList';
            this.getNotiList();
          } else {
            this.pageStatusCng('error');
            this.globalMsgAnimation('예기치 못한 오류가 발생하였습니다.');
          }
        })
        .catch((e) => {
          this.pageStatusCng('error');
          console.log(e);
        });
    },
    /* 주식정보 화면으로 이동 */
    gotoStockData() {
      axios.get(`${this.APP_INFO.server}/expiration/${this.APP_INFO.phone}`, {
        headers: {
          appcode: this.APP_INFO.app_code,
          apikey: this.APP_INFO.api_key,
        },
      })
        .then((r) => {
          if (r.data === 403) {
            this.pageStatusCng('403');
            this.globalMsgAnimation('등록되지 않은 앱입니다.');
          } else if (r.data === 400) {
            this.pageStatusCng('error');
            this.globalMsgAnimation('유효하지 않은 유저입니다.');
          } else if (r.data.result === false) {
            this.pageStatusCng('expiration');
          } else if (r.data.result === true) {
            this.mainPageStatus = 'stockData';
            this.getStockData();
          } else {
            this.pageStatusCng('error');
            this.globalMsgAnimation('예기치 못한 오류가 발생하였습니다.');
          }
        })
        .catch((e) => {
          this.pageStatusCng('error');
          console.log(e);
        });
    },
    /* 새로고침 이벤트 */
    refresh() {
      axios.get(`${this.APP_INFO.server}/expiration/${this.APP_INFO.phone}`, {
        headers: {
          appcode: this.APP_INFO.app_code,
          apikey: this.APP_INFO.api_key,
        },
      })
        .then((r) => {
          if (r.data === 403) {
            this.pageStatusCng('403');
            this.globalMsgAnimation('등록되지 않은 앱입니다.');
          } else if (r.data === 400) {
            this.pageStatusCng('error');
            this.globalMsgAnimation('유효하지 않은 유저입니다.');
          } else if (r.data.result === false) {
            this.pageStatusCng('expiration');
          } else if (r.data.result === true) {
            if (this.mainPageStatus === 'notiList') {
              this.getNotiList();
            } else if (this.mainPageStatus === 'stockData') {
              this.getStockData();
            }
          } else {
            this.pageStatusCng('error');
            this.globalMsgAnimation('예기치 못한 오류가 발생하였습니다.');
          }
        })
        .catch((e) => {
          this.pageStatusCng('error');
          console.log(e);
        });
    },
    /* 새로고침 버튼 클릭 */
    refreshClick(e) {
      e.target.classList.add('on');
      this.refresh();
      setTimeout(() => {
        e.target.classList.remove('on');
      }, 600);
    },
    /* 날짜 변환 */
    dateToMoment(date) {
      const momentDate = moment(date).format('YYYY-MM-DD HH:mm');
      return momentDate;
    },
    setNotification() {
      if (!this.APP_INFO.notification) {
        this.notificationOffConfirm = true;
      } else {
        this.notificationOnEvent();
      }
    },
    notificationOnEvent() {
      axios.put(`${this.APP_INFO.server}/user/notification`, {
        phone: this.APP_INFO.phone,
        bool: 'true',
      },
      {
        headers: {
          apikey: this.APP_INFO.api_key,
          appcode: this.APP_INFO.app_code,
        },
      })
        .then((r) => {
          if (r.data === 400) {
            this.globalMsgAnimation('오류가 발생하였습니다. 다시 시도해주세요.');
            this.notificationStatusCng(false);
          } else {
            FCM.subscribeTo({ topic: this.APP_INFO.app_code })
              .then(() => {
                console.log('subscribed to topic');
                this.notificationStatusCng(true);
              })
              .catch(() => {
                this.globalMsgAnimation('오류가 발생하였습니다. 다시 시도해주세요.');
                this.notificationStatusCng(false);
              });
          }
        })
        .catch((e) => {
          this.globalMsgAnimation('오류가 발생하였습니다. 다시 시도해주세요.');
          this.notificationStatusCng(false);
          console.log(e);
        });
    },
    notificationOffEvent() {
      axios.put(`${this.APP_INFO.server}/user/notification`, {
        phone: this.APP_INFO.phone,
        bool: 'false',
      },
      {
        headers: {
          apikey: this.APP_INFO.api_key,
          appcode: this.APP_INFO.app_code,
        },
      })
        .then((r) => {
          if (r.data === 400) {
            this.globalMsgAnimation('오류가 발생하였습니다. 다시 시도해주세요.');
            this.notificationStatusCng(true);
          } else {
            FCM.unsubscribeFrom({ topic: this.APP_INFO.app_code })
              .then(() => {
                console.log('unsubscribed from topic');
                this.notificationStatusCng(false);
              })
              .catch(() => {
                this.globalMsgAnimation('오류가 발생하였습니다. 다시 시도해주세요.');
                this.notificationStatusCng(true);
              });
          }
        })
        .catch((e) => {
          this.globalMsgAnimation('오류가 발생하였습니다. 다시 시도해주세요.');
          this.notificationStatusCng(true);
          console.log(e);
        });
    },
    pushAnimate() {
      this.pushModal = true;
      setTimeout(() => {
        this.pushModal = false;
      }, 5000);
      setTimeout(() => {
        this.pushData.title = '';
        this.pushData.body = '';
      }, 5500);
    },
  },
  created() {
    this.refresh();
    const socket = io.connect(this.APP_INFO.server);
    socket.on(this.APP_INFO.app_code, (data) => {
      if (this.APP_INFO.notification === true && this.pushData.title === '') {
        this.refresh();
        this.pushData.title = data.title;
        this.pushData.body = data.body;
        this.pushAnimate();
      }
    });
  },
};
</script>
