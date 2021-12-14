<template>
  <div class="randingOuterWrap" v-bind:class="pageState">
    <!-- con 1 -->
    <div class="randingContent con1">
      <div class="topWrap">
        <p class="tit">{{ APP_NAME }}를<br/>시작합니다.</p>
      </div>
      <div class="bottomWrap">
        <div class="confirmCheckWrap" @click="confirmModalOpen();">
          <v-checkbox
            v-model="confirmCheck"
          ></v-checkbox>
          <p class="msg">서비스 이용을 위한 <b>개인정보수집 약관</b>에 동의합니다.</p>
        </div>
        <button class="nextBtn" v-bind:class="{ 'on':confirmCheck }" @click="pageStateCng('con2', $event)">시작하기</button>
      </div>
      <v-bottom-sheet v-model="confirmModal" duration="20000">
        <v-sheet
          class="serviceConfirmModal"
          height="500px"
        >
          <div class="serviceConfirmContent">
            <p class="head">서비스 이용약관</p>
            <p class="body">{{ APP_AGREED }}</p>
            <div class="btnWrap">
              <button class="confirm" @click="confirmCheckAgree()">동의하기</button>
            </div>
          </div>
        </v-sheet>
      </v-bottom-sheet>
    </div>

    <!-- con 2 -->
    <div class="randingContent con2">
      <div class="topWrap">
        <p class="tit">회원정보를<br>입력해주세요.</p>
        <p class="sub">SMS로 인증문자가 발송됩니다.</p>
      </div>
      <div class="midWrap">
        <v-text-field
          v-model="userName"
          label="이름"
          outlined
          @input="userInformationCheckFunc()"
        ></v-text-field>
        <v-text-field
          v-model="userPhone"
          @input="userInformationCheckFunc()"
          label="휴대전화번호"
          hint="'-'을 제외한 숫자만 입력해주세요."
          type="number"
          outlined
        ></v-text-field>
      </div>
      <div class="bottomWrap">
        <p class="infoMsg">{{infoMsg}}</p>
        <button class="nextBtn"
                v-bind:class="{ 'on':userInformationCheck }"
                @click="[pageStateCng('con3', $event), smsCodeSendCheck($event)]"
        >인증번호 발송</button>
      </div>
    </div>

    <!-- con 3 -->
    <div class="randingContent con3">

      <!-- con 3 : loading -->
      <div class="con3InnerWrap" v-if="con3PageStage == 'loading'">
        <div class="loadingWrap">
          <v-progress-circular
            :size="40"
            :width="7"
            color="blue"
            indeterminate
          ></v-progress-circular>
          <p class="loadingMsg">회원정보를 확인중입니다.</p>
        </div>
      </div>

      <!-- con 3 : error -->
      <div class="con3InnerWrap" v-if="con3PageStage == 'error'">
        <div class="errorWrap">
          <img src="../assets/img/errorIcon.svg"/>
          <p class="errorTitle">오류가 발생하였습니다.</p>
          <p class="errorSub">불편을 드려 죄송합니다. 다시 시도해주세요.</p>
          <button class="backBtn" @click="backEvent()">회원정보 입력으로 돌아가기</button>
        </div>
      </div>

      <!-- con 3 : reConfirm -->
      <div class="con3InnerWrap" v-else-if="con3PageStage == 'reConfirm'">
        <div class="reConfirmWrap">
          <img src="../assets/img/userDupIcon.svg"/>
          <p class="reConfirmTitle">입력하신 번호 <b>{{userPhone}}</b> 는<br>이미 등록된 번호입니다.</p>
          <p class="reConfirmSub">입력하신 정보가 맞는지 확인해주세요. </p>
          <div class="btnWrap">
            <button class="reConfirmBtn" @click="sendSms()">번호가 맞습니다.<br><b>SMS 인증 후 재등록</b></button>
            <button class="backBtn" @click="backEvent()">잘못 입력했어요.<br><b>정보 다시 입력</b></button>
          </div>
        </div>
      </div>

      <!-- con 3 : code -->
      <div class="con3InnerWrap" v-else-if="con3PageStage == 'code'">
        <div class="topWrap">
          <p class="tit">인증코드 입력</p>
          <p class="sub">{{userPhone}} 번호로 발송된<br>6자리 코드를 입력해주세요.</p>
        </div>
        <div class="midWrap">
          <v-otp-input
            length="6"
            type="number"
            v-model="smsCode"
            @input="codeCheck()"
          ></v-otp-input>
          <div class="timeCntWrap">
            <div v-if="smsCodeResendBtn == true" class="btnWrap">
              <button class="reSendBtn" @click="smsResend()">인증코드 재발송</button>
              <button class="reTypeBtn" @click="backEvent()">정보 다시입력</button>
            </div>
            <p class="timeCnt"><b>유효시간 : </b>{{smsTimeCnt}}</p>
            <v-snackbar
              v-model="smsCodeResendMsg"
              timeout="3000"
            >
              인증번호를 다시 발송하였습니다.
            </v-snackbar>
          </div>
        </div>
        <div class="bottomWrap">
          <p class="infoMsg">{{smsCodeMsg}}</p>
          <button class="nextBtn" v-bind:class="{ 'on':smsCodeCheck }" @click="codeCheckFirebase($event)">인증</button>
        </div>
      </div>
    </div>

    <!-- con 4 -->
    <div class="randingContent con4">
      <div class="loadingWrap">
        <v-progress-circular
          :size="40"
          :width="7"
          color="blue"
          indeterminate
        ></v-progress-circular>
        <p class="loadingMsg">인증코드를 확인중입니다.</p>
      </div>
    </div>
  </div>
</template>

<script>
import { FCM } from '@capacitor-community/fcm';
import { registerPlugin } from '@capacitor/core';
import axios from 'axios';
import moment from 'moment';
import { Storage } from '@capacitor/storage';
import appInformation from '../assets/app_information.json';

const Echo = registerPlugin('Echo');

export default {
  name: 'RandingPage',
  computed: {
    certificationStatus() {
      return this.$store.state.certificationStatus;
    },
    joinSuccess() {
      return this.$store.state.joinSuccess;
    },
  },
  data: () => ({
    pageState: 'con1',
    confirmCheck: false,
    confirmModal: false,
    userName: '',
    userPhone: '',
    userInformationCheck: false,
    infoMsg: '',
    con3PageStage: 'loading',
    smsCode: '',
    smsCodeCheck: false,
    smsCodeMsg: '',
    smsCodeResendBtn: false,
    smsCodeResendMsg: false,
    smsTimeCnt: '03:00',
    updateUserInformaion: false,
    APP_NAME: appInformation.name,
    APP_PACKAGE: appInformation.package,
    APP_SERVER: appInformation.server,
    APP_AGREED: appInformation.agreed,
  }),
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
    /* 페이지 슬라이드 위치 */
    pageStateCng(page, e) {
      if (e.target.classList.contains('on')) {
        this.pageState = page;
      }
    },
    /* 개인정보수집 모달 */
    confirmModalOpen() {
      this.confirmCheck = false;
      this.confirmModal = true;
    },
    /* 개인정보수집 동의 */
    confirmCheckAgree() {
      this.confirmCheck = true;
      this.confirmModal = false;
    },
    /* 회원정보 유효성 체크 */
    userInformationCheckFunc() {
      const phone = this.userPhone.replaceAll(/[^0-9]/g, '').replaceAll(/ /g, '');
      const name = this.userName.replaceAll(/ /g, '');
      const regPhone = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
      /* eslint-disable-next-line */
      const regName = /[a-z0-9]|[ \[\]{}()<>?|`~!@#$%^&*-_+=,.;:\"'\\]/g;
      this.userName = name;
      if (name === '') {
        this.userInformationCheck = false;
        this.infoMsg = '이름을 입력해주세요.';
      } else if (regName.test(name) || name.length < 2) {
        this.userInformationCheck = false;
        this.infoMsg = '이름을 정확히 입력해주세요.';
      } else if (phone === '') {
        this.userInformationCheck = false;
        this.infoMsg = '전화번호를 입력해주세요.';
      } else if (!regPhone.test(phone)) {
        this.userInformationCheck = false;
        this.infoMsg = '전화번호 형식을 확인해주세요.';
      } else {
        this.userInformationCheck = true;
        this.infoMsg = '';
      }
    },
    /* 회원정보입력(con2) 후 인증번호 발송 클릭 이벤트 */
    smsCodeSendCheck(e) {
      if (e.target.classList.contains('on')) {
        this.con3PageStage = 'loading';
        setTimeout(() => {
          axios.get(`${this.APP_SERVER}/phone/${this.userPhone}/${this.APP_NAME}`, {
            headers: {
              appInformation: 'stockalimi',
            },
          })
            .then((r) => {
              if (r.data === true) { /* 번호가 없으면 */
                this.sendSms();
              } else if (r.data === false) { /* 번호가 있으면 */
                this.con3PageStage = 'reConfirm';
                this.updateUserInformaion = true;
              } else { /* 에러 */
                this.con3PageStage = 'error';
              }
            })
            .catch((err) => {
              this.con3PageStage = 'error';
              console.log(err);
            });
        }, 2000);
      }
    },
    /* sms 발송 */
    sendSms() {
      const phoneNumber = `+82${this.userPhone.slice(1)}`;
      Echo.sendSms({ phone: phoneNumber })
        .then((result) => {
          if (result.value === 'complete') {
            this.con3PageStage = 'code';
            this.timerStartFcunc();
          } else {
            this.con3PageStage = 'error';
          }
        })
        .catch((err) => {
          this.con3PageStage = 'error';
          console.log(err);
        });
    },
    /* sms 재발송 */
    smsResend() {
      this.smsCode = '';
      this.smsCodeCheck = false;
      this.smsCodeResendMsg = true;
      this.smsCodeResendBtn = false;
      this.sendSms();
    },
    /* sms 인증번호 firebase 체크 */
    codeCheckFirebase(e) {
      if (e.target.classList.contains('on')) {
        this.pageState = 'con4';
        setTimeout(() => {
          Echo.codeCheck({ code: this.smsCode }).then((result) => {
            if (result.value === 'success') {
              clearInterval(this.timer);
              this.joinUser();
            } else {
              this.smsCodeCheck = false;
              this.pageState = 'con3';
              this.con3PageStage = 'code';
              this.smsCodeMsg = '인증코드가 일치하지 않습니다.';
            }
          });
        }, 1000);
      }
    },
    /* sms 발송 후 유효시간 카운트 */
    timerStartFcunc() {
      this.smsTimeCnt = '03:00';
      clearInterval(this.timer);
      this.timer = setInterval(() => {
        const timeCnt = moment(this.smsTimeCnt, 'mm:ss').subtract(1, 's').format('mm:ss');
        this.smsTimeCnt = timeCnt;
        if (this.smsTimeCnt === '00:00') {
          clearInterval(this.timer);
        }
        if (this.smsTimeCnt === '01:00') {
          this.smsCodeResendBtn = true;
        }
      }, 1000);
    },
    /* 회원가입 메쏘드 */
    joinUser() {
      Echo.getTokenEcho()
        .then((result) => {
          const data = {
            name: this.userName,
            phone: this.userPhone,
            app_name: this.APP_NAME,
          };
          const headers = {
            headers: {
              'Content-Type': 'application/json',
              apptoken: result.value,
            },
          };
          /* 업데이트인지 확인 후 진행 */
          if (this.updateUserInformaion === true) {
            axios.put(`${this.APP_SERVER}/user`, data, headers)
              .then((r) => {
                if (r.data === true) {
                  this.alarmOnEvent();
                } else {
                  this.con3PageStage = 'error';
                }
              })
              .catch((e) => {
                this.con3PageStage = 'error';
                console.log(e);
              });
          } else {
            axios.post(`${this.APP_SERVER}/user`, data, headers)
              .then((r) => {
                if (r.data === true) {
                  this.alarmOnEvent();
                } else {
                  this.con3PageStage = 'error';
                }
              })
              .catch((e) => {
                this.con3PageStage = 'error';
                console.log(e);
              });
          }
        })
        .catch((err) => {
          this.con3PageStage = 'error';
          console.log(err);
        });
    },
    /* 회원정보 입력 (con2) 화면으로 돌아가기 */
    backEvent() {
      this.pageState = 'con2';
      this.userName = '';
      this.userPhone = '';
      this.infoMsg = '';
      this.userInformationCheck = false;
      this.smsCodeMsg = '';
      this.smsCodeResendBtn = '';
      this.smsCodeResendMsg = false;
      this.smsTimeCnt = '03:00';
      this.updateUserInformaion = false;
      this.smsCodeMsg = '';
    },
    /* sms 인증번호 유효성검사 */
    codeCheck() {
      if (this.smsCode.length === 6) {
        this.smsCodeCheck = true;
        this.smsCodeMsg = '';
      } else {
        this.smsCodeMsg = '';
        this.smsCodeCheck = false;
      }
    },
    /* 푸쉬알림 켜기 */
    alarmOnEvent() {
      FCM.subscribeTo({ topic: this.APP_PACKAGE })
        .then(() => {
          this.joinComplete();
          console.log('subscribed to topic');
        })
        .catch((e) => {
          console.log(e);
          this.con3PageStage = 'error';
        });
    },
    /* 로컬스토리지에 인증 저장 */
    async localSave() {
      await Storage.set({
        key: 'certification',
        value: 'confirmed',
      });
    },
    /* 로컬스토리지에 알람상태 저장 */
    async localSaveAlarm(bool) {
      await Storage.set({
        key: 'alarmstatus',
        value: String(bool),
      });
    },
    /* 가입 완료 */
    joinComplete() {
      this.localSave();
      this.localSaveAlarm(true);
      this.initialState();
      this.certificationStatusCng(true);
      setTimeout(() => {
        this.joinSuccessCng(true);
      }, 0);
      setTimeout(() => {
        this.joinSuccessCng(false);
      }, 3000);
    },
    /* 초기화 */
    initialState() {
      this.pageState = 'con1';
      this.confirmCheck = false;
      this.confirmModal = false;
      this.userName = '';
      this.userPhone = '';
      this.userInformationCheck = false;
      this.infoMsg = '';
      this.con3PageStage = 'loading';
      this.smsCode = '';
      this.smsCodeCheck = false;
      this.smsCodeMsg = '';
      this.smsCodeResendBtn = false;
      this.smsCodeResendMsg = false;
      this.smsTimeCnt = '03:00';
      this.updateUserInformaion = false;
      this.smsCodeMsg = '';
    },
  },
  created() {
    this.initialState();
  },
};
</script>
