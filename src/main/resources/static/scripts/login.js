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


    let requestObject = {
        email : loginForm.email.value,
        password : loginForm.password.value
    }

    fetch('/api/login', {
        method : 'POST',
        headers : {
            'Content-Type': 'application/json'
        },
        body : JSON.stringify(requestObject)
    }).then(response => response.json())
        .then(data => {
            const loginModal = new ModalObj();
            if (data.code === 200) {
                loginModal.createModal('알림', data.message, [{
                    title : '확인',
                    onclick : () => location.href = '/login'
                }]);
            }
            if (data.code === 401) {
                loginModal.createSimpleModal('알림', data.message);
            }
            console.log(data);
        })
        .catch(error => console.error('Error:', error));
}