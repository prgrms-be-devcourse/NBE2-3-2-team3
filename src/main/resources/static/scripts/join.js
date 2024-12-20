const joinForm = document.getElementById('join-form');

const passwordLabel= joinForm.querySelector('.password > .text-label');
const passwordCheckLabel = joinForm.querySelector('.password-check > .text-label');

passwordLabel.querySelector('button').onclick = () => {
    if (passwordLabel.querySelector('button > i').classList.contains('fa-eye')) {
        passwordLabel.querySelector('input').removeAttribute('type');
        passwordLabel.querySelector('input').setAttribute('type', 'password');
        passwordLabel.querySelector('button > i').classList.remove('fa-eye');
        passwordLabel.querySelector('button > i').classList.add('fa-eye-slash');
    } else {
        passwordLabel.querySelector('input').removeAttribute('type');
        passwordLabel.querySelector('input').setAttribute('type', 'text');
        passwordLabel.querySelector('button > i').classList.remove('fa-eye-slash');
        passwordLabel.querySelector('button > i').classList.add('fa-eye');
    }
}

passwordCheckLabel.querySelector('button').onclick = () => {
    if (passwordCheckLabel.querySelector('button > i').classList.contains('fa-eye')) {
        passwordCheckLabel.querySelector('input').removeAttribute('type');
        passwordCheckLabel.querySelector('input').setAttribute('type', 'password');
        passwordCheckLabel.querySelector('button > i').classList.remove('fa-eye');
        passwordCheckLabel.querySelector('button > i').classList.add('fa-eye-slash');
    } else {
        passwordCheckLabel.querySelector('input').removeAttribute('type');
        passwordCheckLabel.querySelector('input').setAttribute('type', 'text');
        passwordCheckLabel.querySelector('button > i').classList.remove('fa-eye-slash');
        passwordCheckLabel.querySelector('button > i').classList.add('fa-eye');
    }
}

joinForm.onsubmit = (e) => {
    e.preventDefault();

    const joinModal = new ModalObj();
    joinModal.createSimpleModal('알림', '회원가입했어요');
}