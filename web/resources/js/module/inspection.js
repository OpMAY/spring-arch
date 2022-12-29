/**
 * FindRegex,
 * regex 형태를 찾아주는 함수
 *
 * @param {string} type 찾을 regex 타입
 * */
function findRegex(type) {
    switch (type) {
        case 'name':
        case 'nickname': {
            return /^[가-힣a-zA-Z\d\s]{2,20}$/; // 2~20자 영어, 한글, 숫자, 띄어쓰기
        }
        case 'password': {
            return /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&^])[A-Za-z\d@$!%*#?&^]{8,20}$/; // 8자 이상 20자 이하, 영문, 숫자, 특수문자(@$!%*#?&^) 모두 포함
        }
        case 'phone': {
            return /^([\d]{2,3})[\s|-]?([\d]{3,4})[\s|-]?([\d]{4})$/igm; // 3자리, 3~4자리, 4자리, 띄어쓰기나 - 입력가능
        }
        case 'price': {
            return /^(?!0+$)[,0-9]{1,13}$/; // 0 제외, 1~13자리, 최대 9,999,999,999
        }
        case '0price': {
            return /^[,0-9]{1,13}$/; // 0 허용, 1~13자리, 최대 9,999,999,999
        }
        case 'yyyy.MM.dd': {
            return /^[\d]{4}.[\d]{2}.[\d]{2}$/; // yyyy.MM.dd 만 허용
        }
        case 'yyyy-MM-dd': {
            return /^[\d]{4}-[\d]{2}-[\d]{2}$/; // yyyy.MM.dd 만 허용
        }
        case 'address': {
            return /^[가-힣a-zA-Z\d\s]{4,20}$/; // 0~20자 영어, 한글, 숫자, 띄어쓰기
        }
        case 'birthday': {
            return /([\d]{2}(0[1-9]|1[0-2])(0[1-9]|[1,2][\d]|3[0,1]))/gm; // ex) 000000 6자리
        }
        case 'business': {
            return /^(\d{3})+[-]+(\d{2})+[-]+(\d{5})/gm; // 사업자 등록 번호
        }
        case 'company': {
            return /^(\d{6})+[-]+(\d{7})/gm; // 법인 사업자 등록 번호
        }
        case '0~200': {
            return /^.{0,200}$/gm; // 0~200글자 내외 모든 글자
        }
        case '5~200': {
            return /^.{5,200}$/gm; // 5~200글자 내외 모든 글자
        }
        case '0~2000': {
            return /^.{0,2000}$/gm; // 0~2000글자 내외 모든 글자
        }
        default:
            throw new Error(`${type} is not define`);
    }
}

function inspection(options = {
    element: undefined,
    type: undefined,
    empty_text: '입력값이 없습니다.',
    failed_text: '잘못 된 입력입니다.',
    isFocus: true,
    onEmpty: (empty_text) => {
        viewAlert({content: empty_text});
    },
    onFailed: (failed_text) => {
        viewAlert({content: failed_text});
    },
    onSuccess: () => {

    }
}) {
    this.options = {
        element: undefined,
        type: undefined,
        empty_text: '입력값이 없습니다.',
        failed_text: '잘못 된 입력입니다.',
        isFocus: true,
        onEmpty: undefined,
        onFailed: undefined,
        onSuccess: undefined
    };
    console.log('options.element', options.element);
    if (options.element) {
        if (options.element.nodeType !== 1) {
            this.options.element = document.querySelector(options.element);
        } else {
            this.options.element = options.element;
        }
    } else {
        throw new Error(`element is not defined`);
    }
    this.options.type = options.hasOwnProperty('type') ? options.type : undefined;
    if (!this.options.type) {
        throw new Error(`type is not defined`);
    }
    this.options.empty_text = options.hasOwnProperty('empty_text') ? options.empty_text : '입력값이 없습니다.';
    this.options.failed_text = options.hasOwnProperty('failed_text') ? options.failed_text : '잘못 된 입력입니다.';
    this.options.isFocus = options.hasOwnProperty('isFocus') ? options.isFocus : true;
    this.options.onEmpty = options.hasOwnProperty('onEmpty') ? options.onEmpty : (element, text) => {
        console.log('onEmpty', element, text);
        viewAlert({content: text});
    };
    this.options.onFailed = options.hasOwnProperty('onFailed') ? options.onFailed : (element, text) => {
        console.log('onFailed', element, text);
        viewAlert({content: text});
    };
    this.options.onSuccess = options.hasOwnProperty('onSuccess') ? options.onSuccess : (element) => {
        console.log('onSuccess', element);
    };
    let _options = this.options, _this = this;
    if (_options.element) {
        let value = this.options.element.value;
        try {
            value = value.trim();
            value = value.replace(/\n/g, '');// 행바꿈제거
            value = value.replace(/\r/g, '');// 엔터제거
        } catch (e) {
            /** TextArea Value Trim Error Fix*/
            value = $.trim(value);
            value = value.replace(/\n/g, '');// 행바꿈제거
            value = value.replace(/\r/g, '');// 엔터제거
        }
        if (isEmpty(value)) {
            if (_options.element && _options.isFocus) {
                _options.element.focus();
            }
            _options.onEmpty(_options.element, _options.empty_text);
            return false;
        }
        if (!findRegex(_options.type).test(value)) { // 정규식 불합격 (길이, 특수문자 등)
            if (_options.element && _options.isFocus) {
                _options.element.focus();
            }
            _options.onFailed(_options.element, _options.failed_text);
            return false;
        }
        _options.onSuccess(_options.element);
        return true;
    } else {
        throw new Error(`element value is not defined`);
    }
}
