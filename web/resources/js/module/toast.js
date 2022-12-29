'use strict'
const TOAST_COLOR = {
    TYPE: (type) => {
        switch (type) {
            case this.PRIMARY.name: {
                return this.PRIMARY;
            }
            case this.SECONDARY.name: {
                return this.SECONDARY;
            }
            case this.IMPACT_PRIMARY.name: {
                return this.IMPACT_PRIMARY;
            }
            case this.IMPACT_SECONDARY.name: {
                return this.IMPACT_SECONDARY;
            }
            default:
                return null;
        }
    },
    PRIMARY: {
        name: 'PRIMARY',
        color: '--brand-primary',
    },
    SECONDARY: {
        name: 'SECONDARY',
        color: '--brand-secondary',
    },
    IMPACT_PRIMARY: {
        name: '--impact-primary',
        color: '',
    },
    IMPACT_SECONDARY: {
        name: '--impact-secondary',
    }
}
Object.freeze(TOAST_COLOR);

/**
 * Toast,
 * 토스트를 설정하는 함수
 *
 * @param {Object} options 토스트 옵션
 * @param {Array[Array[]]} options.position 토스트의 생성 위치
 * */
const Toast = function (options = {
    container: undefined,
    position: [['bottom', 'left'], [0, 0]],
}) {
    this.options = {toasts: [], container: undefined, position: [['bottom', 'left'], [0, 0]]}

    let _options = this.options;
    let _this = this;

    Object.keys(this.options).forEach(function (prop) {
        if (options.hasOwnProperty(prop)) {
            Object.defineProperty(_options, prop, {
                value: options[prop],
                writable: true,
                configurable: true,
                enumerable: true,
            });
        }
    });

    function createToastElement(toast) {
        function __buildToastInnerHTML(toast) {
            return `<div class="toast-header">
                  <div class="rounded mr-1" style="width: 24px; height: 24px; background-color: var(--brand-primary);"></div>
                  <strong class="mr-auto">${toast.title}</strong>
                  <small>${toast.date}</small>
                  ${toast.is_close ? '<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close"><span aria-hidden="true">×</span></button>' : ''}
                </div>
                <div class="toast-body">
                  ${toast.content}
                </div>`;
        }

        let toast_elem = document.createElement('div');
        toast_elem.style.minWidth = `${240}px`;
        toast_elem.classList.add('toast', 'show');
        let attr = ['alert', 'assertive', true, 2000, toast.id];
        ['role', 'aria-live', 'aria-atomic', 'data-delay', 'data-id'].forEach(function (key, index) {
            toast_elem.setAttribute(key, attr[index]);
        });
        toast.supports.forEach(function (support_prop) {
            toast_elem.setAttribute(support_prop.key, support_prop.value);
        });
        toast_elem.innerHTML = __buildToastInnerHTML(toast);
        if (toast.is_close) {
            if (toast.close) {
                toast_elem.querySelector('button.close[data-dismiss]').addEventListener('click', function (event) {
                    let toast_elem = this.closest('.toast');
                    toast.close(toast_elem, toast.supports);
                    toast_elem.remove();
                });
            } else {
                throw new Error(`toast.close is not defined`);
            }
        }
        return toast_elem;
    }

    function createToastContainer() {
        let container = document.createElement('div');
        container.classList.add('toast-container', 'p-3');
        container.style.position = 'fixed';
        container.style.zIndex = 5;
        _options.position[0].forEach(function (direct, index) {
            container.style[direct] = `${_options.position[1][index]}px`;
        });
        return container;
    }

    /**
     * Toast.create
     * 토스트 생성
     *
     * @param {Object} toast 토스트 데이터
     * @param {string} toast.id 토스트 아이디
     * @param {string} toast.title 토스트 제목
     * @param {string} toast.date 토스트 시간
     * @param {string || TOAST_COLOR} toast.color 토스트 색
     * @param {string} toast.content 토스트 내용
     * @param {boolean} toast.is_close 토스트 종료 버튼 여부
     * @param {Array[Object]} toast.supports 토스트 서포트 내용 (data-[attr], [attr])
     * @param {BigInteger} toast.auto_close 토스트 자동 종료 여부
     * */
    this.create = (toast) => {
        if (!_options.container) {
            let container = createToastContainer();
            document.body.append(container);
            _options.container = container;
        }
        if (toast.id === undefined || toast.id === null) {
            toast.id = tokenGenerator(8);
        }
        if (toast.supports === undefined || toast.supports === null) {
            toast.supports = [];
        }
        if (toast.auto_close === undefined || toast.auto_close === null) {
            toast.auto_close = 0;
        }
        let toast_elem = createToastElement(toast);
        _options.container.append(toast_elem);
        _options.toasts.push(toast_elem);
        if (toast.auto_close !== 0) {
            setTimeout(() => {
                _this.remove(toast.id);
            }, toast.auto_close);
        }
        return toast_elem;
    }

    this.remove = (toast) => {
        let id;
        if (toast) {
            if (toast.nodeType !== 1) {
                _options.toasts.forEach(function (e, i) {
                    if (e.dataset.id === toast) {
                        let toast_elem = _options.toasts[i];
                        toast_elem.remove();
                        id = e.dataset.id;
                    }
                });
            } else {
                _options.toasts.forEach(function (e, i) {
                    if (e.dataset.id === toast.dataset.id) {
                        let toast_elem = _options.toasts[i];
                        toast_elem.remove();
                        id = e.dataset.id;
                    }
                });
            }
        } else {
            if (_options.toasts.length !== 0) {
                id = _options.toasts[0].dataset.id;
                _options.toasts[0].remove();
                _options.toasts.shift();
            }
        }
        if (_options.toasts.length === 0) {
            _options.container.remove();
            _options.container = undefined;
        }
        _options.toasts = _options.toasts.filter((toast) => {
            if (id !== toast.dataset.id) {
                return true;
            }
            return false;
        });
        console.log('timeout _options.toasts', _options.toasts);
    }

    function build() {
        return _this;
    }

    (() => {
        // Event functions
        return build.call(this);
    })()
}
