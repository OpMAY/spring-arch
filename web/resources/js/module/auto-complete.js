/**
 * AutoComplete,
 * 테이블의 컬럼타입에 관한 선택리스트를 자동으로 생성하는 로직을 등록시켜주는 함수
 *
 * @param {Object} options 오토 컴플리트 설정
 * @param {string} options.position Position
 * @param {BigInteger} options.top Top
 * @param {BigInteger} options.left left
 * @param {string} options.width Width Option String (100%, 100px, etc...)
 * @param {MouseEvent} options.clickEvent Suggest 된 Element를 클릭 또는 엔터를 눌렀을때 후처리 이벤트
 * @param {HTMLElement | Node | HTMLInputElement} options.inp 이벤트를 등록할 엘리먼트
 * @param {string[]} options.arr 선택리스트의 데이터 목록들
 * */
function autocomplete(options = {
    position,
    zIndex,
    top,
    left,
    width,
    inp,
    arr,
    clickEvent
}) {
    this.options = {
        position: 'absolute',
        zIndex: 1,
        top: 'auto',
        left: 'auto',
        width: '100%',
        inp: undefined,
        arr: [],
        clickEvent: function (event) {
            console.log('clickEvent', this);
        }
    };

    this.options.position = options.hasOwnProperty('position') ? options.position : `absolute`;
    this.options.zIndex = options.hasOwnProperty('zIndex') ? options.zIndex : 1;
    this.options.top = options.hasOwnProperty('top') ? options.top : `auto`;
    this.options.left = options.hasOwnProperty('left') ? options.left : `auto`;
    this.options.width = options.hasOwnProperty('width') ? options.width : `100%`;
    this.options.inp = options.hasOwnProperty('inp') ? options.inp : undefined;
    this.options.arr = options.hasOwnProperty('arr') ? options.arr : [];
    this.options.clickEvent = options.hasOwnProperty('clickEvent') ? options.clickEvent : function (event) {
        console.log('clickEvent', this);
    };
    let _options = this.options;
    let _this = this;
    /* the autocomplete function takes two arguments,
            the text field element and an array of possible autocompleted values:*/
    let currentFocus;
    /* execute a function when someone writes in the text field:*/
    _options.inp.addEventListener('input', function (e) {
        let a;
        let b;
        let i;
        const val = this.value;
        /* close any already open lists of autocompleted values*/
        closeAllLists();
        if (!val) {
            // TODO in this case, Text length is 0 then open list all of them(arr)
            return false;
        }
        currentFocus = -1;
        /* create a DIV element that will contain the items (values):*/
        a = document.createElement('div');
        a.setAttribute('id', this.id + 'autocomplete-list');
        a.setAttribute('class', 'autocomplete-items');
        console.log('options', options);
        a.style.position = _options.position;
        a.style.zIndex = _options.zIndex;
        if (_options.top === 'auto') {
            a.style.top = `${_options.inp.getBoundingClientRect().height}px`;
        } else {
            a.style.top = _options.top;
        }
        if (_options.left === 'auto') {
            a.style.left = _options.left;
        } else {
            a.style.left = _options.left;
        }
        a.style.width = _options.width;
        /* append the DIV element as a child of the autocomplete container:*/
        this.parentNode.appendChild(a);
        /* for each item in the array...*/
        for (i = 0; i < _options.arr.length; i++) {
            /* check if the item starts with the same letters as the text field value:*/
            if (_options.arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                /* create a DIV element for each matching element:*/
                b = document.createElement('div');
                /* make the matching letters bold:*/
                b.innerHTML = '<strong>' + _options.arr[i].substr(0, val.length) + '</strong>';
                b.innerHTML += _options.arr[i].substr(val.length);
                /* insert a input field that will hold the current array item's value:*/
                b.innerHTML += '<input type=\'hidden\' value=\'' + _options.arr[i] + '\'>';
                /* execute a function when someone clicks on the item value (DIV element):*/
                b.addEventListener('click', function (e) {
                    /* insert the value for the autocomplete text field:*/
                    _options.inp.value = this.getElementsByTagName('input')[0].value;
                    /* close the list of autocompleted values,
                                                            (or any other open lists of autocompleted values:*/
                    closeAllLists();
                });
                a.appendChild(b);
            }
        }
    });
    /* execute a function presses a key on the keyboard:*/
    _options.inp.addEventListener('keydown', function (e) {
        let x = document.getElementById(this.id + 'autocomplete-list');
        if (x) x = x.getElementsByTagName('div');
        if (e.keyCode == 40) {
            /* If the arrow DOWN key is pressed,
                                    increase the currentFocus variable:*/
            currentFocus++;
            /* and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 38) { // up
            /* If the arrow UP key is pressed,
                                    decrease the currentFocus variable:*/
            currentFocus--;
            /* and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 13) {
            /* If the ENTER key is pressed, prevent the form from being submitted,*/
            e.preventDefault();
            if (currentFocus > -1) {
                /* and simulate a click on the "active" item:*/
                if (x) x[currentFocus].click();
            }
        }
    });

    function addActive(x) {
        /* a function to classify an item as "active":*/
        if (!x || x.length === 0) return false;
        /* start by removing the "active" class on all items:*/
        removeActive(x);
        if (currentFocus >= x.length) currentFocus = 0;
        if (currentFocus < 0) currentFocus = (x.length - 1);
        /* add class "autocomplete-active":*/
        x[currentFocus].classList.add('autocomplete-active');
    }

    function removeActive(x) {
        /* a function to remove the "active" class from all autocomplete items:*/
        for (let i = 0; i < x.length; i++) {
            x[i].classList.remove('autocomplete-active');
        }
    }

    function closeAllLists(elmnt) {
        /* close all autocomplete lists in the document,
                        except the one passed as an argument:*/
        const x = document.getElementsByClassName('autocomplete-items');
        for (let i = 0; i < x.length; i++) {
            if (elmnt != x[i] && elmnt != _options.inp) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }

    /* execute a function when someone clicks in the document:*/
    document.addEventListener('click', function (e) {
        closeAllLists(e.target);
    });
    if (_options.clickEvent !== undefined && _options.clickEvent !== null) {
        document.addEventListener('click', _options.clickEvent);
    }
}
