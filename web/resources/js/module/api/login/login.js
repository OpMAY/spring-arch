'use strict'
const LOGIN_TYPE = {
    KAKAO: {
        name: 'KAKAO',
    },
    GOOGLE: {
        name: 'GOOGLE',
    },
    NAVER: {
        name: 'NAVER',
    }
}
Object.freeze(LOGIN_TYPE);

/**
 * APILogin,
 * 로그인을 이니셜라이징 시켜주는 함수
 *
 * @param {Object} options 로그인 옵션
 * @param {LOGIN_TYPE} options.type 로그인 타입 (LOGIN_TYPE -> KAKAO, GOOGLE, NAVER)
 * @param {HTMLButtonElement || string} options.button 로그인 버튼
 * @param {function} options.initialize 초기화 세팅 완료 시점 콜백 이벤트
 * @param {string} options.key Login API Key, 기본적으로 시스템으로부터 가져옴
 * @param {string} options.redirect_url Login 진행시 Callback URL
 * @param {string} options.get_key_url Login 진행시 Key를 서버에서 가져오는 URL
 * */

const APILogin = function (options = {
    type,
    button,
    get_key_url,
    key,
    initialize,
    redirect_url: '/auth/oauth/callback'
}) {
    this.options = {
        type: undefined,
        button: undefined,
        get_key_url: undefined,
        key: undefined,
        redirect_url: `/auth/oauth/callback`,
        initialize: function (options) {
            console.log('initialize', options);
        },
    };
    this.options.type = options.hasOwnProperty('type') ? options.type : undefined;
    if (!options.button) {
        this.options.button = document.querySelector(options.button);
    } else {
        if (options.button.nodeType !== 1) {
            this.options.button = document.querySelector(options.button);
        } else {
            this.options.button = options.button;
        }
    }
    this.options.key = options.hasOwnProperty('key') ? options.key : undefined;
    this.options.initialize = options.hasOwnProperty('initialize') ? options.initialize : function (options) {
        console.log('initialize', options);
    };
    this.options.redirect_url = options.hasOwnProperty('redirect_url') ? options.redirect_url : `/auth/oauth/callback`;
    this.options.get_key_url = options.hasOwnProperty('get_key_url') ? options.get_key_url : `/web/resources/assets/datas/getKey.json`;

    let _options = this.options;

    console.log('_options', _options);
    if (this.options.type.name) {
        getKey(this.options.type.name).then((result) => {
            console.log('getKey', result);
            if (result.status === 'OK') {
                _options.key = result.data.key;
                if (_options.type == LOGIN_TYPE.KAKAO) {
                    if (typeof Kakao === 'undefined') {
                        // CALL kakao login script
                        $.getScript('https://developers.kakao.com/sdk/js/kakao.js', function () {
                            // Stuff to do after someScript has loaded
                            Kakao.init(_options.key);
                            _options.initialize(_options);
                        });
                    }
                }
                _options.button.addEventListener('click', buttonClickEventListener);
            } else {
                throw new Error(`API key is not defined`);
            }
        });
    } else {
        throw new Error(`type is not defined`);
    }

    this.naver = (key) => {
        const h = 'https://nid.naver.com/oauth2.0/authorize';
        const r_t = 'code';
        const r = encodeURI(`${window.location.protocol}//${window.location.host}${_options.redirect_url}`);
        const c = key;
        const s = `${tokenGenerator(8)}-${tokenGenerator(4)}-${tokenGenerator(4)}-${tokenGenerator(4)}-${tokenGenerator(12)}`;
        location.href = `${h}?response_type=${r_t}&client_id=${c}&state=${s}&redirect_uri=${r}`;
    };
    this.kakao = (key) => {
        const h = 'https://kauth.kakao.com/oauth/authorize';
        const c = key;
        const r = encodeURI(`${window.location.protocol}//${window.location.host}${_options.redirect_url}`);
        const t = 'code';
        location.href = `${h}?client_id=${c}&redirect_uri=${r}&response_type=${t}`;
    };
    this.google = (key) => {
        const h = 'https://accounts.google.com/o/oauth2/v2/auth';
        const c = key;
        const r = encodeURI(`${window.location.protocol}//${window.location.host}${_options.redirect_url}`);
        const t = 'code';
        const s = 'email,name,profile'; // 받아올 정보 목록
        location.href = `${h}?client_id=${c}&redirect_uri=${r}&response_type=${t}&scope=${s.replace(/,/g, '&20')}`;
    };
    let _this = this;

    function buttonClickEventListener(event) {
        switch (_options.type) {
            case LOGIN_TYPE.GOOGLE: {
                _this.google(_options.key);
                break;
            }
            case LOGIN_TYPE.NAVER: {
                _this.naver(_options.key);
                break;
            }
            case LOGIN_TYPE.KAKAO: {
                _this.kakao(_options.key);
                break;
            }
            default: {
                throw new Error(`type is recognize type, please type is ${LOGIN_TYPE}`);
            }
        }
        event.stopPropagation();
        event.preventDefault();
    }

    async function getKey(type) {
        function apiGetKey(type) {
            const myHeaders = new Headers();
            myHeaders.append('Content-Type', 'application/json');
            let raw = JSON.stringify({type});
            const requestOptions = {
                method: 'POST',
                headers: myHeaders,
                body: raw,
            };
            if (_options.get_key_url == '/web/resources/assets/datas/getKey.json') {
                const response = fetch(`${_options.get_key_url}`);
                return response.then((res) => res.json());
            } else {
                const response = fetch(`${_options.get_key_url}`, requestOptions);
                return response.then((res) => res.json());
            }
        }

        let result;
        try {
            result = await apiGetKey(type);
            return result;
        } catch (error) {
            console.log(error);
        }
    }
};
