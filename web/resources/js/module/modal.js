// General Modal
// MODAL_CONTAINER
let ALERT_CONTAINER, MODAL_CONTAINER;
document.addEventListener('DOMContentLoaded', function () {
    if (!ALERT_CONTAINER) {
        let alert_container_elem = document.createElement('div');
        alert_container_elem.setAttribute('id', 'alert-container');
        alert_container_elem.classList.add('alert-container');
        document.querySelector('body').append(alert_container_elem);
        ALERT_CONTAINER = $('body .alert-container');
    }
    if (!MODAL_CONTAINER) {
        let modal_container_elem = document.createElement('div');
        modal_container_elem.setAttribute('id', 'modal-container');
        modal_container_elem.classList.add('modal-container');
        document.querySelector('body').append(modal_container_elem);
        MODAL_CONTAINER = $('body .modal-container');
    }
});
/**
 * ViewAlert
 * @requires [tokenGenerator]
 * @param {string} id Alert 메세지에서 설정될 ID 값, default = 8자리 랜덤토큰
 * @param {string} type Alert 메세지의 타입 ('success','failed','info','danger','warning'), default = 'success'
 * @param {string | InnerHTML} content Alert 메세지에서 보여줄 내용
 * @param {number} fadeTimeOut Alert 메세지가 사라지는데 걸리는 시간 (ms), default = 400
 * @param {number} timeOut Alert 메세지가 유지되는 시간 (ms), default = 2000
 * @example
 * viewAlert({id: 'alert-1', type: 'failed', content: 'Login Failed', fadeTimeOut: 400, timeOut: 2000});
 * */
const viewAlert = ({
                       id = tokenGenerator(8),
                       content,
                       fadeTimeOut = 400,
                       timeOut = 2000,
                   }) => {
    const alert = document.createElement('div');
    alert.setAttribute('id', id);
    alert.classList.add('alert');
    alert.style.display = 'none';
    alert.innerHTML = `${content}`;
    ALERT_CONTAINER.append(alert);
    $('#' + id).fadeIn(fadeTimeOut);
    setTimeout(() => {
        $('#' + id).fadeOut(fadeTimeOut, () => {
            $('#' + id).remove();
        });
    }, timeOut);
};

/**
 * ViewModal
 * @param {string} id 모달 아이디
 * @param {string} title 모달 타이틀
 * @param {string} content 모달 내용(HTML String 호환)
 * @param {string} close 모달 종료 버튼 텍스트
 * @param {string} confirm 모달 확인 버튼 텍스트
 * @param {boolean} vCenter 모달 버티컬 센터 포지션 설정
 * @param {boolean} wCenter 모달내 텍스트 호라이즌털 포지션 설정
 * @param {function} onConfirm 모달 확인 버튼 클릭시 콜백함수
 * @param {function} onCancel 모달 확인 버튼 클릭시 콜백함수
 * @param {function} onShow 모달 생성시 콜백함수
 * @param {function} onShown 모달 생성 완료후 콜백함수
 * @param {function} onHide 모달 닫기시 콜백함수
 * @param {function} onHidden 모달 닫기 완료후 콜백함수
 * */
function viewModal({
                       title = 'Modal Title',
                       content = `<p>Modal body text goes here.</p>`,
                       is_cancel = true,
                       close = '취소',
                       confirm = '확인',
                       vCenter = true,
                       wCenter = true,
                       id = tokenGenerator(8),
                       onConfirm = () => {

                       },
                       onCancel = () => {

                       },
                       onShow = () => {

                       },
                       onShown = () => {

                       },
                       onHide = () => {

                       },
                       onHidden = () => {

                       },
                   }) {
    console.log('MODAL_CONTAINER', MODAL_CONTAINER);

    function __buildModalInnerHTML(options) {
        let buttons_html = ``;
        if (options.is_cancel) {
            buttons_html = `<button type="button" class="button_cancel btn btn-secondary ml-auto" data-dismiss="modal">${options.close}</button>
                            <button type="button" class="button_confirm btn btn-primary mr-auto">${options.confirm}</button>`;
        } else {
            buttons_html = `<button type="button" class="button_confirm btn btn-primary mx-auto">${options.confirm}</button>`;
        }
        return `<div class="modal-dialog ${options.vCenter ? `modal-dialog-centered` : ``}">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title w-100 ${options.wCenter ? `text-center` : ``}">${options.title}</h5>
                    </div>
                    <div class="modal-body ${options.wCenter ? `text-center` : ``}">
                      ${content}
                    </div>
                    <div class="modal-footer">
                      ${buttons_html}
                    </div>
                  </div>
                </div>`;
    }

    /**
     * ModalHiddenEvent,
     * 모달이 제거됬을때의 이벤트
     * @param {Event} event
     * */
    const modalHiddenEvent = (event) => {
        if ($('#modal-container .general-modal.show').length === 0) {
            if ($('body').hasClass('general-modal-open')) {
                $('body').removeClass('general-modal-open');
            }
        }
        event.currentTarget.remove();
    };

    /**
     * ModalShownEvent,
     * 모달이 생성됬을때의 이벤트
     * @param {Event} event
     * */
    const modalShownEvent = (event) => {
        if ($('body').hasClass('modal-open')) {
            $('body').addClass('general-modal-open');
        }
    };

    let modal = document.createElement('div');
    modal.setAttribute('id', id);
    modal.classList.add('modal', 'fade', 'general-modal');
    modal.setAttribute('tabindex', '-1');
    modal.innerHTML = __buildModalInnerHTML({title, confirm, close, content, vCenter, wCenter, is_cancel});

    if (onConfirm) {
        modal.querySelector('.button_confirm').addEventListener('click', onConfirm);
    }

    if (onCancel) {
        modal.querySelector('.button_cancel').addEventListener('click', onCancel);
    }

    modal.querySelector('.button_confirm').addEventListener('click', function (event) {
        let modal = this.closest('.general-modal');
        $(modal).modal('hide');
    });

    MODAL_CONTAINER.append(modal);

    if (onShown) {
        MODAL_CONTAINER.find('#' + id).on('shown.bs.modal', onShown);
    }
    MODAL_CONTAINER.find('#' + id).on('shown.bs.modal', modalShownEvent);
    if (onHidden) {
        MODAL_CONTAINER.find('#' + id).on('hidden.bs.modal', onHidden);
    }
    MODAL_CONTAINER.find('#' + id).on('hidden.bs.modal', modalHiddenEvent);
    if (onShow) {
        MODAL_CONTAINER.find('#' + id).on('show.bs.modal', onShow);
    }
    if (onHide) {
        MODAL_CONTAINER.find('#' + id).on('hide.bs.modal', onHide);
    }
    setTimeout(function () {
        $('#' + id).modal('show');
    }, 200);
}
