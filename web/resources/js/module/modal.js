// General Modal
// MODAL_CONTAINER
let ALERT_CONTAINER, MODAL_CONTAINER;
document.addEventListener('DOMContentLoaded', function () {

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
                       type = 'success',
                       content,
                       fadeTimeOut = 400,
                       timeOut = 2000,
                   }) => {
    const alert = document.createElement('div');
    alert.setAttribute('id', id);
    alert.classList.add('alert');
    alert.style.display = 'none';
    if (type === 'success') {
        alert.classList.add('alert-success');
    } else if (type === 'failed') {
        alert.classList.add('alert-failed');
    } else if (type === 'info') {
        alert.classList.add('alert-info');
    }
    alert.innerHTML = `${content}`;
    ALERT_CONTAINER.append(alert);
    $('#' + id).fadeIn(fadeTimeOut);
    setTimeout(() => {
        $('#' + id).fadeOut(fadeTimeOut, () => {
            $('#' + id).remove();
        });
    }, timeOut);
};