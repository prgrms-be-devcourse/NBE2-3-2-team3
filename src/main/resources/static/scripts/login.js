const loginForm = document.getElementById('login-form');
const passwordLabel= loginForm.querySelector('.password > .text-label');


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

loginForm.onsubmit = (e) => {
    e.preventDefault();


    const loginModal = new ModalObj();
    loginModal.createSimpleModal('알림', '로그인했어요');
}