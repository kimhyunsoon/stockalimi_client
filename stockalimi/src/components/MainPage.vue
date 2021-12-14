<template>
  <div class="mainComponentWrap">
    <div class="pushModalWrap" v-bind:class="{ 'on':pushModal }">
      <div class="pushModalBox">
        <div class="icon">
          <img src="../assets/img/notification.svg"/>
        </div>
        <div class="text">
          <p class="tit">[ 급등주알림 : {{ pushData.title }} ]</p>
          <p class="body">{{ pushData.body }}</p>
        </div>
      </div>
    </div>
    <div class="refreshBtn" @click="refreshClick($event)">
      <img src="../assets/img/refresh.svg"/>
    </div>
    <div class="headerBar">
      <div class="mainTitle">
        <p>{{ APP_NAME }}</p>
        <div class="icon">
          <img src="../assets/img/notification.svg"/>
        </div>
      </div>
      <div
        class="alarmStatus"
        @click="alarmSet"
      >
        <p v-bind:class="[ alarmStatus ? 'on' : 'off' ]">
          {{`${alarmStatus?'알림 ON':'알림 OFF'}`}}
        </p>

        <v-container
          class="px-0"
          fluid
        >
          <v-switch
            v-model="alarmStatus"
          ></v-switch>
        </v-container>
      </div>
    </div>
    <div v-if="stockData==false" class="mainProgressWrap">
      <v-progress-circular
        :size="40"
        :width="7"
        color="blue"
        indeterminate
      ></v-progress-circular>
      <p class="loadingMsg">주식정보를 불러오는중입니다.</p>
    </div>
    <div v-else-if="stockData=='err'" class="mainProgressWrap">
      <div class="errorWrap">
        <img src="../assets/img/errorIcon.svg"/>
        <p class="errorTitle">오류가 발생하였습니다.</p>
        <p class="errorSub">불편을 드려 죄송합니다. 다시 시도해주세요.</p>
        <button class="backBtn" @click="refreshClick($event)">다시 시도</button>
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
    <v-bottom-sheet v-model="alramOffMsg" persistent class="noAlarmWrap">
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
            @click="alarmOffEvent"
          >
            알림 끄기
          </v-btn>
          <v-btn
            class="cancle"
            @click="alramOffMsg = false, alarmStatus = true"
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
import { Storage } from '@capacitor/storage';
import { FCM } from '@capacitor-community/fcm';
import { io } from 'socket.io-client';
import appInformation from '../assets/app_information.json';

export default {
  name: 'Main',
  data: () => ({
    alarmStatus: false,
    stockData: false,
    kospiDnUp: '',
    kosdaqDnUp: '',
    alramOffMsg: false,
    pushModal: false,
    pushData: { title: '', body: '' },
    APP_NAME: appInformation.name,
    APP_PACKAGE: appInformation.package,
    APP_SERVER: appInformation.server,
  }),
  methods: {
    refresh() {
      this.stockData = false;
      axios.get(`${this.APP_SERVER}/stock`, {
        headers: {
          /* 크롤링 데이터 요청의 경우는 간단히 앱-서버 간 약속된 문자열 확인 후 처리함 */
          appInformation: 'stockalimi',
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
    },
    refreshClick(e) {
      this.stockData = false;
      e.target.classList.add('on');
      setTimeout(() => {
        this.refresh();
        e.target.classList.remove('on');
      }, 800);
    },
    alarmSet() {
      if (!this.alarmStatus) {
        this.alramOffMsg = true;
      } else {
        this.alarmOnEvent();
      }
    },
    alarmOnEvent() {
      FCM.subscribeTo({ topic: this.APP_PACKAGE })
        .then(() => {
          console.log('subscribed to topic');
          this.alarmStatus = true;
          this.localSaveAlarm(true);
        });
    },
    /* 로컬스토리지에 알람상태 저장 */
    async localSaveAlarm(bool) {
      await Storage.set({
        key: 'alarmstatus',
        value: String(bool),
      });
    },
    /* 로컬스토리지 확인 */
    async localCheckAlarm() {
      const ret = await Storage.get({ key: 'alarmstatus' });
      const status = ret.value;
      if (status === 'true') {
        this.alarmStatus = true;
      } else {
        this.alarmStatus = false;
      }
    },
    alarmOffEvent() {
      FCM.unsubscribeFrom({ topic: this.APP_PACKAGE })
        .then(() => {
          console.log('unsubscribed from topic');
          this.alramOffMsg = false;
          this.alarmStatus = false;
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
    this.localCheckAlarm();
    this.refresh();
    const socket = io.connect(this.APP_SERVER);
    socket.on(this.APP_PACKAGE, (data) => {
      if (this.alarmStatus === true && this.pushData.title === '') {
        this.pushData.title = data.title;
        this.pushData.body = data.body;
        this.pushAnimate();
      }
    });
  },
};
</script>
