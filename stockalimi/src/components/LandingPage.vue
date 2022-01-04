<template>
  <div class="landingOuterWrap" v-bind:class="pageState">
    <!-- con 1 -->
    <div class="landingContent con1">
      <div class="topWrap">
        <p class="tit">
          {{ APP_INFO.app_name }}를
          <br/>시작합니다.
        </p>
      </div>
      <div class="bottomWrap">
        <div class="confirmCheckWrap">
          <label for="allConfirm" class="confirmLabel" @click="allConfirmClick()">
            <div class="checkIconBox" :style=" allConfirm ? `backgroundColor: ${APP_INFO.app_color};`:''">
              <svg class="checkIcon" viewBox="0 0 512 512" fill="none">
                <path d="M173.898 439.404L7.49799 273.004C-2.49901 263.007 -2.49901 246.798 7.49799 236.8L43.701 200.596C53.698 190.598 69.908 190.598 79.905 200.596L192 312.69L432.095 72.5959C442.092 62.5989 458.302 62.5989 468.299 72.5959L504.502 108.8C514.499 118.797 514.499 135.006 504.502 145.004L210.102 439.405C200.104 449.402 183.895 449.402 173.898 439.404V439.404Z"/>
              </svg>
            </div>
            <p class="msg">모든 약관에 동의합니다.</p>
          </label>
          <div class="subLabelWrap">
            <label for="confirm_1" class="confirmLabel sub" @click="confirmClick()">
              <input type="checkbox" id="confirm_1" v-model="confirm_1" hidden/>
              <svg class="checkIcon" viewBox="0 0 512 512" fill="none">
                <path d="M173.898 439.404L7.49799 273.004C-2.49901 263.007 -2.49901 246.798 7.49799 236.8L43.701 200.596C53.698 190.598 69.908 190.598 79.905 200.596L192 312.69L432.095 72.5959C442.092 62.5989 458.302 62.5989 468.299 72.5959L504.502 108.8C514.499 118.797 514.499 135.006 504.502 145.004L210.102 439.405C200.104 449.402 183.895 449.402 173.898 439.404V439.404Z" :style=" confirm_1 ? `fill: ${APP_INFO.app_color};`:''"/>
              </svg>
              <p class="msgSub">서비스 약관 동의</p>
            </label>
            <b @click="confirmModalOpen(1)" class="modalOpenBtn">상세보기</b>
          </div>
          <div class="subLabelWrap">
            <label for="confirm_2" class="confirmLabel sub" @click="confirmClick()">
              <input type="checkbox" id="confirm_2" v-model="confirm_2" hidden/>
              <svg class="checkIcon" viewBox="0 0 512 512" fill="none">
                <path d="M173.898 439.404L7.49799 273.004C-2.49901 263.007 -2.49901 246.798 7.49799 236.8L43.701 200.596C53.698 190.598 69.908 190.598 79.905 200.596L192 312.69L432.095 72.5959C442.092 62.5989 458.302 62.5989 468.299 72.5959L504.502 108.8C514.499 118.797 514.499 135.006 504.502 145.004L210.102 439.405C200.104 449.402 183.895 449.402 173.898 439.404V439.404Z" :style=" confirm_2 ? `fill: ${APP_INFO.app_color};`:''"/>
              </svg>
              <p class="msgSub">개인정보 수집 및 이용 동의</p>
            </label>
            <b class="modalOpenBtn" @click="confirmModalOpen(2)">상세보기</b>
          </div>
          <div class="subLabelWrap">
            <label for="confirm_3" class="confirmLabel sub" @click="confirmClick()">
              <input type="checkbox" id="confirm_3" v-model="confirm_3" hidden/>
              <svg class="checkIcon" viewBox="0 0 512 512" fill="none">
                <path d="M173.898 439.404L7.49799 273.004C-2.49901 263.007 -2.49901 246.798 7.49799 236.8L43.701 200.596C53.698 190.598 69.908 190.598 79.905 200.596L192 312.69L432.095 72.5959C442.092 62.5989 458.302 62.5989 468.299 72.5959L504.502 108.8C514.499 118.797 514.499 135.006 504.502 145.004L210.102 439.405C200.104 449.402 183.895 449.402 173.898 439.404V439.404Z" :style=" confirm_3 ? `fill: ${APP_INFO.app_color};`:''"/>
              </svg>
              <p class="msgSub">개인정보 제3자 제공 동의</p>
            </label>
            <b class="modalOpenBtn" @click="confirmModalOpen(3)">상세보기</b>
          </div>
        </div>
        <button class="nextBtn" v-bind:class="{ 'on':allConfirm }" v-bind:style=" allConfirm ? `backgroundColor: ${APP_INFO.app_color};`:''" @click="pageStateCng('con2', $event)">시작하기</button>
      </div>
      <v-bottom-sheet v-model="confirmModal_1" duration="20000">
        <v-sheet
          class="serviceConfirmModal"
        >
          <div class="serviceConfirmContent">
            <p class="head">서비스 약관 동의<b @click="confirmModalClose()">×</b></p>
            <div v-html="APP_INFO.terms_use_1" class="body"></div>
          </div>
        </v-sheet>
      </v-bottom-sheet>
      <v-bottom-sheet v-model="confirmModal_2" duration="20000">
        <v-sheet
          class="serviceConfirmModal"
        >
          <div class="serviceConfirmContent">
            <p class="head">개인정보 수집 및 이용 동의<b @click="confirmModalClose()">×</b></p>
            <div v-html="APP_INFO.terms_use_2" class="body"></div>
          </div>
        </v-sheet>
      </v-bottom-sheet>
      <v-bottom-sheet v-model="confirmModal_3" duration="20000">
        <v-sheet
          class="serviceConfirmModal"
        >
          <div class="serviceConfirmContent">
            <p class="head">개인정보 제3자 제공 동의<b @click="confirmModalClose()">×</b></p>
            <div v-html="APP_INFO.terms_use_3" class="body"></div>
          </div>
        </v-sheet>
      </v-bottom-sheet>
    </div>

    <!-- con 2 -->
    <div class="landingContent con2">
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
          :color=APP_INFO.app_color
        ></v-text-field>
        <v-text-field
          v-model="userPhone"
          @input="userInformationCheckFunc()"
          label="휴대전화번호"
          hint="'-'을 제외한 숫자만 입력해주세요."
          type="number"
          outlined
          :color=APP_INFO.app_color
        ></v-text-field>
      </div>
      <div class="bottomWrap">
        <p class="infoMsg">{{infoMsg}}</p>
        <button class="nextBtn"
                v-bind:class="{ 'on':userInformationCheck }"
                @click="[pageStateCng('con3', $event), smsCodeSendCheck($event)]"
                v-bind:style=" userInformationCheck ? `backgroundColor: ${APP_INFO.app_color};`:''"
        >인증번호 발송</button>
      </div>
    </div>

    <!-- con 3 -->
    <div class="landingContent con3">

      <!-- con 3 : error -->
      <div class="con3InnerWrap" v-if="con3PageStage == 'error'">
        <div class="errorWrap">
          <img src="../assets/img/errorIcon.svg"/>
          <p class="errorTitle">오류가 발생하였습니다.</p>
          <p class="errorSub">불편을 드려 죄송합니다. 다시 시도해주세요.</p>
          <button class="backBtn" :style="{ backgroundColor: APP_INFO.app_color }" @click="backEvent()">회원정보 입력으로 돌아가기</button>
        </div>
      </div>

      <!-- con 3 : loading -->
      <div class="con3InnerWrap" v-else-if="con3PageStage == 'loading'">
        <div class="loadingWrap">
          <v-progress-circular
            :size="40"
            :width="7"
            :color="APP_INFO.app_color"
            indeterminate
          ></v-progress-circular>
          <p class="loadingMsg">회원정보를 확인중입니다.</p>
        </div>
      </div>

      <!-- con 3 : contact -->
      <div class="con3InnerWrap" v-else-if="con3PageStage == 'contact'">
        <div class="loadingWrap">
          <v-progress-circular
            :size="40"
            :width="7"
            :color="APP_INFO.app_color"
            indeterminate
          ></v-progress-circular>
          <p class="loadingMsg">문의를 등록중입니다.</p>
        </div>
      </div>

      <!-- con 3 : reConfirm -->
      <div class="con3InnerWrap" v-else-if="con3PageStage == 'reConfirm'">
        <div class="reConfirmWrap">
          <img src="../assets/img/userDupIcon.svg"/>
          <p class="reConfirmTitle">입력하신 번호 <b :style="{ color: APP_INFO.app_color }">{{userPhone}}</b> 는<br>이미 등록된 번호입니다.</p>
          <p class="reConfirmSub">입력하신 정보가 맞는지 확인해주세요.</p>
          <div class="btnWrap">
            <button
              v-if="reconfirmState == 'expiration'"
              :style="{ backgroundColor: APP_INFO.app_color }"
              class="reConfirmBtn"
              @click="contactSend()"
            >번호가 맞습니다.<br><b>기간연장 등 문의 신청</b>
            </button>
            <button
              v-if="reconfirmState == 'reauth'"
              :style="{ backgroundColor: APP_INFO.app_color }"
              class="reConfirmBtn"
              @click="reAuthEvent()"
            >번호가 맞습니다.<br><b>재인증 후 로그인</b>
            </button>
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
            :color="APP_INFO.app_color"
          ></v-otp-input>
          <div class="timeCntWrap">
            <div v-if="smsCodeResendBtn == true" class="btnWrap">
              <button class="reSendBtn" :style="{ backgroundColor: APP_INFO.app_color }" @click="smsResend()">인증코드 재발송</button>
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
          <button class="nextBtn" v-bind:class="{ 'on':smsCodeCheck }" @click="codeCheckFirebase($event)" v-bind:style=" smsCodeCheck ? `backgroundColor: ${APP_INFO.app_color};`:''">인증</button>
        </div>
      </div>
    </div>

    <!-- con 4 -->
    <div class="landingContent con4">
      <div class="loadingWrap">
        <v-progress-circular
          :size="40"
          :width="7"
          :color="APP_INFO.app_color"
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

const Echo = registerPlugin('Echo');

export default {
  name: 'LandingPage',
  computed: {
    /* vuex : 앱 정보 */
    APP_INFO() {
      return this.$store.state.APP_INFO;
    },
  },
  data: () => ({
    pageState: 'con1',
    allConfirm: false,
    confirm_1: false,
    confirm_2: false,
    confirm_3: false,
    confirmModal_1: false,
    confirmModal_2: false,
    confirmModal_3: false,
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
    reconfirmState: 'reauth',
    reauthState: false,
  }),
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
    /* 페이지 슬라이드 위치 */
    pageStateCng(page, e) {
      if (e.target.classList.contains('on')) {
        this.pageState = page;
      }
    },
    /* 이용약관 모달 닫기 */
    confirmModalClose() {
      this.confirmModal_1 = false;
      this.confirmModal_2 = false;
      this.confirmModal_3 = false;
    },
    /* 이용약관 모달 열기 */
    confirmModalOpen(number) {
      switch (number) {
        case 1:
          this.confirmModal_1 = true;
          break;
        case 2:
          this.confirmModal_2 = true;
          break;
        case 3:
          this.confirmModal_3 = true;
          break;
        default:
          console.log(number);
      }
    },
    /* 모두동의 클릭 */
    allConfirmClick() {
      if (this.allConfirm === true) {
        this.allConfirm = false;
        this.confirm_1 = false;
        this.confirm_2 = false;
        this.confirm_3 = false;
      } else {
        this.allConfirm = true;
        this.confirm_1 = true;
        this.confirm_2 = true;
        this.confirm_3 = true;
      }
    },
    /* 각 항목 동의 클릭 */
    confirmClick() {
      setTimeout(() => {
        if (this.confirm_1 === true && this.confirm_2 === true && this.confirm_3 === true) {
          this.allConfirm = true;
        } else {
          this.allConfirm = false;
        }
      }, 0);
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
          /* 중복 번호 확인 */
          axios.get(`${this.APP_INFO.server}/phone/${this.userPhone}`, {
            headers: {
              appcode: this.APP_INFO.app_code,
              apikey: this.APP_INFO.api_key,
            },
          })
            .then((r) => {
              if (r.data === true) { /* 번호가 없으면 */
                this.sendSms();
              } else if (r.data === 'expiration') { /* 번호가 있고 만료일이 지났으면 */
                this.con3PageStage = 'reConfirm';
                this.reconfirmState = 'expiration';
              } else if (r.data === 'reauth') { /* 번호가 있고 만료일이 남았으면 */
                this.con3PageStage = 'reConfirm';
                this.reconfirmState = 'reauth';
              } else if (r.data === 403) {
                this.initialState();
                this.pageStateCng('403');
                this.globalMsgAnimation('등록되지 않은 앱입니다.');
              } else {
                this.con3PageStage = 'error';
              }
            })
            .catch((err) => {
              this.con3PageStage = 'error';
              console.log(err);
            });
        }, 1500);
      }
    },
    /* 번호 중복이며 만료되었을 경우 문의 등록 */
    contactSend() {
      this.con3PageStage = 'contact';
      setTimeout(() => {
        const data = {
          name: this.userName,
          phone: this.userPhone,
        };
        axios.post(`${this.APP_INFO.server}/contact`, data, {
          headers: {
            appcode: this.APP_INFO.app_code,
            apikey: this.APP_INFO.api_key,
          },
        })
          .then((r) => {
            if (r.data === true) {
              this.pageState = 'con1';
              this.con3PageStage = 'loading';
              this.globalMsgAnimation(`${this.userName}(${this.userPhone})님,<br>문의 요청이 등록되었습니다.<br>빠른 시일안에 연락드리겠습니다.`);
            } else if (r.data === 403) {
              this.initialState();
              this.pageStateCng('403');
              this.globalMsgAnimation('등록되지 않은 앱입니다.');
            } else {
              this.con3PageStage = 'error';
            }
          })
          .catch((e) => {
            this.con3PageStage = 'error';
            console.log(e);
          });
      }, 1500);
    },
    /* 번호 중복이며 만료되지 않았을 경우 재인증 후 로그인 */
    reAuthEvent() {
      this.reauthState = true;
      this.sendSms();
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
            app_token: result.value,
            app_valid: this.APP_INFO.default_valid,
          };
          const headers = {
            headers: {
              appcode: this.APP_INFO.app_code,
              apikey: this.APP_INFO.api_key,
            },
          };
          /* 재인증일 경우 이름과 앱토큰만 업데이트 */
          if (this.reauthState === true) {
            axios.put(`${this.APP_INFO.server}/user`, data, headers)
              .then((r) => {
                if (r.data === true) {
                  this.fcmSubcribeTopic();
                } else if (r.data === 403) {
                  this.initialState();
                  this.pageStateCng('403');
                  this.globalMsgAnimation('등록되지 않은 앱입니다.');
                } else {
                  this.con3PageStage = 'error';
                }
              })
              .catch((e) => {
                this.con3PageStage = 'error';
                console.log(e);
              });
          } else {
            axios.post(`${this.APP_INFO.server}/user`, data, headers)
              .then((r) => {
                if (r.data === true) {
                  this.fcmSubcribeTopic();
                } else if (r.data === 403) {
                  this.initialState();
                  this.pageStateCng('403');
                  this.globalMsgAnimation('등록되지 않은 앱입니다.');
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
      this.smsCodeMsg = '';
      this.reauthState = false;
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
    /* FCM 알림 구독 */
    fcmSubcribeTopic() {
      FCM.subscribeTo({ topic: this.APP_INFO.app_code })
        .then(() => {
          console.log('구독완료');
          this.joinComplete();
        })
        .catch((e) => {
          console.log(e);
          this.con3PageStage = 'error';
        });
    },
    /* 로컬스토리지에 인증 저장 + 전역 변수에 전화번호, 알림상태, 이름 저장 */
    async setStorageAuth() {
      this.SET_APP_INFO_ROW(['name', this.userName]);
      this.SET_APP_INFO_ROW(['phone', this.userPhone]);
      this.SET_APP_INFO_ROW(['notification', true]);
      const data = {
        phone: this.userPhone,
      };
      await Storage.set({
        key: `${this.APP_INFO.app_code}_auth`,
        value: JSON.stringify(data),
      });
    },
    /* 가입 완료 */
    joinComplete() {
      this.setStorageAuth();
      this.pageStatusCng('main');
      this.globalMsgAnimation(`${this.userName}(${this.APP_INFO.phone})님, 환영합니다!`);
      this.initialState();
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
      this.smsCodeMsg = '';
      this.reauthState = false;
    },
  },
  created() {
    this.initialState();
  },
};
</script>
