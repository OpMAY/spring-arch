'use strict';
$(document).ready(function () {
    console.log('utility.js execute');
});

/**
 * CheckImageType,
 * 파일 이름이 이미지인지 아닌지 파악하는 함수
 *
 * @return {boolean}
 *
 * @example
 * checkImageType(FILE_NAME);
 * */
function checkImageType(file_name) {
    let result = false;
    let ext = file_name.substring(file_name.lastIndexOf('.') + 1);
    if (!ext) {
        return result;
    }
    ext = ext.toLocaleLowerCase();
    ['gif', 'jpg', 'jpeg', 'png', 'bmp', 'ico', 'apng', 'svg'].forEach(function (element) {
        if (ext == element) {
            result = true;
        }
    });
    return result;
}

/**
 * AddComma,
 * 숫자에 콤마를 찍어주는 함수
 *
 * @param {number} number
 * @return {boolean} 1000 -> 1,000
 * @example
 * addComma(1000);
 * */
function addComma(number) {
    let len;
    let point;
    let str;
    const number_string = number + '';
    point = number_string.length % 3;
    len = number_string.length;
    str = number_string.substring(0, point);
    while (point < len) {
        if (str !== '') str += ',';
        str += number_string.substring(point, point + 3);
        point += 3;
    }
    return str;
}

/**
 * MoveToScroll,
 * 종단 스크롤을 동적으로 특정 타겟까지 이동시켜주는 함수
 *
 * @param {string} move_id 타겟 아이디
 * @param {number} top 특정 타겟에서 더 나아갈 값 (px), default = 0
 * @param {number} speed 스크롤되는 속도 값 (ms), default = 400
 * @param {boolean} isClass 특정 타겟을 클래스로 찾을지 결정하는 값, TRUE면 ID가 selector, default = false
 * @example
 * moveToScroll({move_id:SELECTOR, top:100, speed:800, isClass:true});
 * */
function moveToScroll({move_id, top = 0, speed = 400, isClass = false}) {
    if (move_id !== undefined && move_id != null) {
        let offset;
        if (!isClass) {
            offset = $('#' + move_id).offset();
        } else {
            offset = $('.' + move_id).offset();
        }
        $('html, body').animate({scrollTop: offset.top + top}, speed);
    } else {
        throw new Error(`${move_id} is not exist`);
        console.log('move id is not exist');
    }
}

/**
 * ReplaceAll,
 * 스트링에서 특정 스트링을 찾아 타겟 스트링으로 바꿔는 함수
 *
 * @param {string} str 전체 스트링
 * @param {string} search 찾고자 하는 특정 스트링
 * @param {string} replace 바꾸고자 하는 타겟 스트링
 * @return {string} {str}에서 {search}를 찾고 {replace}로 바꾼 전체 스트링
 *
 * @example
 * replaceAll('str is search string and replace all in replace string', 'string', 'str');
 * */
function replaceAll(str, search, replace) {
    if (search !== undefined && replace !== undefined && str !== undefined) {
        return str.split(search).join(replace);
    } else {
        throw new Error(`parameter is undefined str : ${str}, search : ${search}, replace : ${replace}`);
    }
}

/**
 * TokenGenerator,
 * 토큰을 생성해주는 함수
 * @param {number} length 만들려는 토큰의 자릿수, default = 10
 * @return {String} 자릿수 만큼의 String을 리턴, default = 10
 *
 * @sample
 * tokenGenerator(10);
 * */
const tokenGenerator = (length = 11) => {
    return Math.random().toString(36).substring(2, length); // "twozs5xfni"
};

/**
 * FetchGetURLBuilder,
 * URL에 Object를 GET 요청 url로 변환해주는 함수
 *
 * @param {string} baseUrl URL
 * @param {Object} object GET방식으로 요청할 데이터
 * @return {string} URL + Object 변수가 포함된 통합 URL (http://localhost:8080?name=kimwoosik&type=test)
 *
 * @sample
 * fetchGetURLBuilder('http://localhost:8080',{name:'kimwoosik',type:'test'});
 */
function fetchGetURLBuilder(baseUrl, object) {
    let result = baseUrl + '?';
    Object.keys(object).forEach((key) => {
        result += key + '=' + object[key] + '&';
    });
    return result.substring(0, result.length - 1);
}

/**
 * DeleteChild,
 * 엘리먼트 안에 모든 하위 엘리먼트 제거하는 함수
 *
 * @param {HTMLElement | Node} element 하위 엘리먼트를 제거할 상위 엘리먼트
 * @sample deleteChild(document.querySelector('.test'));
 * */
const deleteChild = (element) => {
    if (element) {
        element.innerHTML = '';
        return;
    }
    throw new Error('element is undefined, please element check before execute this function');
};

/**
 * ToFileSizeString,
 * 파일 사이즈를 [KB, MB, B]로 바꿔주는 함수
 *
 * @param {number} size Byte 단위 크기
 * @return {string}
 * @sample toFileSizeString(10000);
 * */
function changeFileSize(size) {
    let file_size = size / 1024 / 1024;
    if (file_size < 1) {
        file_size *= 1024;
        file_size = Math.ceil(file_size);
        file_size += 'KB';
    } else {
        file_size = Math.ceil(file_size);
        file_size += 'MB';
    }
    return file_size;
}

/**
 * GetUUID,
 * 랜덤 UUID를 얻는 함수
 *
 * @return {string} uuid 16자의 uuid create
 * @sample
 * let str = getUUID();
 */
function getUUID() { // UUID v4 generator in JavaScript (RFC4122 compliant)
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
        const r = Math.random() * 16 | 0;
        const v = c === 'x' ? r : (r & 3 | 8);
        return v.toString(16);
    });
}

/**
 * InspectNumberInput,
 * 엘리먼트의 값을 숫자로 변경, 세자리마다 Comma Formatting, max 값 세팅시 최대값 제한하는 함수
 * @requires [addComma]
 * @param {HTMLElement | Element} elem - input element
 * @sample
 * inspectNumberInput(input_element);
 */
function inspectNumberInput(elem) {
    elem.value = elem.value.replace(/\D/g, ''); // replace non digit
    elem.value = isNaN(Number(elem.value)) ? 0 : addComma(Number(elem.value));
    if (elem.value.length === 0) elem.value = 0;

    const max = elem.dataset.max ? Number(elem.dataset.max) : 9_999_999_999;
    if (max < Number(elem.value.replace(/\D/g, ''))) // max 값보다 value 가 크면
    {
        elem.value = addComma(max);
    } // 최대값 설정
}

/**
 * InspectPureNumberInput,
 * Max 값 세팅시 최대값 제한하는 함수
 * @param {HTMLElement} elem - input element
 * @sample
 * inspectPureNumberInput(input_element);
 */
function inspectPureNumberInput(elem) {
    elem.value = elem.value.replace(/\D/g, ''); // replace non digit
    elem.value = isNaN(Number(elem.value)) ? 0 : Number(elem.value);
    if (elem.value.length === 0) elem.value = 0;

    const max = elem.dataset.max ? Number(elem.dataset.max) : 9_999_999_999;
    if (max < Number(elem.value.replace(/\D/g, ''))) // max 값보다 value 가 크면
    {
        elem.value = max;
    } // 최대값 설정
}

/**
 * ToNumberToMoneyString,
 * 숫자를 한국 돈으로 바꿔주는 함수,
 * 10000 -> 1만원
 * @requires [numberFormat]
 * @param {number} number - input element
 * @sample
 * inspectPureNumberInput(input_element);
 */
function toNumberToMoneyString(number) {
    function numberFormat(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }

    const inputNumber = number < 0 ? false : number;
    const unitWords = ['', '만', '억', '조', '경'];
    const splitUnit = 10000;
    const splitCount = unitWords.length;
    const resultArray = [];
    let resultString = '';

    for (var i = 0; i < splitCount; i++) {
        let unitResult =
            (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0) {
            resultArray[i] = unitResult;
        }
    }
    for (var i = 0; i < resultArray.length; i++) {
        if (!resultArray[i]) continue;
        resultString = String(numberFormat(resultArray[i])) + unitWords[i] + resultString;
    }
    return resultString.length === 0 ? '0원' : resultString + '원';
}

/**
 * phoneNumFormatter,
 * 특정 값을 휴대폰 번호 형식에 맞춰서 반환해주는 함수,
 * @param {string} value 휴대폰 번호, 00000000000
 * @return {string} 010-0000-0000
 * @sample
 * autoDashPhoneNumber('00000000000');
 * */
const phoneNumFormatter = (str) => {
    let RegNotNum = /[^0-9]/g;
    let RegPhoneNum = "";
    let DataForm = "";

    // return blank

    if (str === "" || str == null) return "";

    // delete not number
    str = str.replace(RegNotNum, '');

    /* 4자리 이하일 경우 아무런 액션도 취하지 않음. */

    if (str.length < 4) return str;

    if (str.substring(0, 2) === "02" && str.length > 10) {
        /* 지역번호 02일 경우 10자리 이상입력 못하도록 제어함. */
        str = str.substring(0, 10);
    } else {
        /* 그 외의 경우 11자리 이상입력 못하도록 제어함. */
        str = str.substring(0, 11);
    }

    if (str.length > 3 && str.length < 7) {
        if (str.substring(0, 2) === "02") {
            DataForm = "$1-$2";

            RegPhoneNum = /([0-9]{2})([0-9]+)/;

        } else {
            DataForm = "$1-$2";

            RegPhoneNum = /([0-9]{3})([0-9]+)/;
        }
    } else if (str.length === 7) {
        if (str.substring(0, 2) === "02") {
            DataForm = "$1-$2-$3";

            RegPhoneNum = /([0-9]{2})([0-9]{3})([0-9]+)/;
        } else {
            DataForm = "$1-$2";

            RegPhoneNum = /([0-9]{3})([0-9]{4})/;
        }
    } else if (str.length === 9) {
        if (str.substring(0, 2) === "02") {
            DataForm = "$1-$2-$3";

            RegPhoneNum = /([0-9]{2})([0-9]{3})([0-9]+)/;
        } else {
            DataForm = "$1-$2-$3";

            RegPhoneNum = /([0-9]{3})([0-9]{3})([0-9]+)/;
        }
    } else if (str.length === 10) {
        if (str.substring(0, 2) === "02") {
            DataForm = "$1-$2-$3";

            RegPhoneNum = /([0-9]{2})([0-9]{4})([0-9]+)/;
        } else {
            DataForm = "$1-$2-$3";

            RegPhoneNum = /([0-9]{3})([0-9]{3})([0-9]+)/;
        }
    } else if (str.length > 10) {
        if (str.substring(0, 2) === "02") {
            DataForm = "$1-$2-$3";

            RegPhoneNum = /([0-9]{2})([0-9]{4})([0-9]+)/;
        } else {
            DataForm = "$1-$2-$3";

            RegPhoneNum = /([0-9]{3})([0-9]{4})([0-9]+)/;
        }
    } else {
        if (str.substring(0, 2) === "02") {
            DataForm = "$1-$2-$3";

            RegPhoneNum = /([0-9]{2})([0-9]{3})([0-9]+)/;
        } else {
            DataForm = "$1-$2-$3";

            RegPhoneNum = /([0-9]{3})([0-9]{3})([0-9]+)/;
        }
    }

    while (RegPhoneNum.test(str)) {
        str = str.replace(RegPhoneNum, DataForm);
    }

    return str;
}

/**
 * GetURLParameter,
 * URL에서 특정 파라미터의 값을 가지고 오는 함수
 *
 * @param {string} name 가지고올 파라미터 이름
 * @return {string}
 * @sample
 * let name = getURLParameter('name');
 * */
const getParameter = (name) => {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    const results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
};

/**
 * IsMobileCheck,
 * 모바일 기기 체크하는 함수
 *
 * @return {boolean}
 *
 * @sample
 * let mobile_check = isMobileCheck();
 * */
function isMobileCheck() {
    return /(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(navigator.userAgent.substr(0, 4)) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0, 4));
}

/**
 * MobilePlaceHolderChanger,
 * 모바일 버전의 Placeholder를 설정해주는 함수
 *
 * @requires [isMobileCheck]
 * @param {string} name 대체하고자 하는 Attribute Name
 * @sample
 * <input type="text" placeholder="placeholder" data-mobile-placeholder="mobile placeholder"/>
 * mobilePlaceHolderChanger('data-mobile-placeholder');
 * */
function mobilePlaceHolderChanger(name) {
    if (isMobileCheck()) {
        const placeholders = $(`[${name}]`);
        placeholders.each(function (i, e) {
            $(this).attr('placeholder', this.dataset.name);
            // $('[data-mobile-holder-text]').attr('placeholder', '');
        });
    }
}

/**
 * IsResponseSize,
 * 특정 사이즈가 화면 사이즈인지 아닌지 파악하는 함수
 *
 * @param {number} size 화면사이즈가 특정 사이즈인지 아닌지 파악할 값
 * @return {boolean}
 * @sample
 * screen.width = 1200
 * let is_response = isResponseSize(800);
 * */
const isResponseSize = (size) => {
    const s = (size !== undefined && size != null) ? size : 1200;
    const w = screen.width;
    if (w <= s) {
        return true;
    }
    return false;
};

/* 3글자 이상 된다고 할 때*/
/**
 * ToMaskingString,
 * 문자열에 마스킹 처리하는 함수
 *
 * @param {string} string 마스킹 처리할 문자열
 * @sample
 * let masked_string = toMaskingString('masking name');
 * */
const toMaskingString = function (string) {
    if (string.length > 2) {
        const originName = string.split('');
        originName.forEach(function (name, i) {
            if (i === 0 || i === originName.length - 1) return;
            originName[i] = '*';
        });
        const joinName = originName.join();
        return joinName.replace(/,/g, '');
    } else {
        const pattern = /.$/; // 정규식
        return string.replace(pattern, '*');
    }
};

/**
 * IsEmpty,
 * 자료형에 상관없이 빈값인지 확인하는 함수
 * @param {HTMLElement | Object | Node | NodeList | Object[] | Node[] | HTMLElement[] | jQuery | Element | number | boolean | string | function} value
 */
function isEmpty(value) {
    if (value !== null && typeof value == 'object') {
        const attrs = [];
        for (const attr in value) {
            attrs.push(attr);
        }
        return attrs.length === 0;
    }
    return value === null || value === undefined || (typeof value === 'string' && value.replace(/\s/g, '') === '');
}

class Time {

}

function copyText(target, callback) {
    let range;
    let select;
    if (document.createRange) {
        range = document.createRange();
        range.selectNode(target);
        select = window.getSelection();
        select.removeAllRanges();
        select.addRange(range);
        document.execCommand('copy');
        select.removeAllRanges();
    } else {
        range = document.body.createTextRange();
        range.moveToElementText(target);
        range.select();
        document.execCommand('copy');
    }
    callback();
}

const getURLParamByPrevAndNext = (find_first_slash_string, find_last_slash_string) => {
    let path_name = location.pathname;
    return path_name.substring(path_name.indexOf(find_first_slash_string) + (find_first_slash_string.length + 1), path_name.lastIndexOf(find_last_slash_string) - 1);
}

function debounce(callback, limit = 100) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => {
            callback.apply(this, args);
        }, limit);
    };
}

function downloadFileFromUrl(url, filename) {
    fetch(url)
        .then(response => response.blob())
        .then(blob => {
            const link = document.createElement("a");
            link.href = URL.createObjectURL(blob);
            link.download = filename;
            link.click();
        })
        .catch(console.error);
}
