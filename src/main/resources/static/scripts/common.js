HTMLElement.prototype.show = function () {
    this.classList.add('-visible');
    return this;
}

HTMLElement.prototype.hide = function () {
    this.classList.remove('-visible');
    return this;
}

// 쿠키 다루기
function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function setCookie(name, value, options = {}) {

    options = {
        path: '/', // 경로 지정
        ...options // 아규먼트로 옵션을 넘겨줬을경우 전개연산자로 추가 갱신
    };

    if (options.expires instanceof Date) {
        options.expires = options.expires.toUTCString(); // 생 Date 객체라면 형식에 맞게 인코딩
    }

    let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);

    for (let optionKey in options) {
        updatedCookie += "; " + optionKey;
        let optionValue = options[optionKey];
        if (optionValue !== true) { // 밸류가 없다면
            updatedCookie += "=" + optionValue;
        }
    }

    document.cookie = updatedCookie; // 새로 갱신
}


function deleteCookie(name) { // 해당 쿠키 요소만 삭제
    setCookie(name, "", {
        'max-age': -1
    })
}

// Authorization 이 있으면 로그인을 했다. - true
// Authorization 이 없으면 로그인을 하지 않았다. - false
function checkLogin() {
    return !(localStorage.getItem('Authorization') === null)
}

function setLoginBox() {
    const loginBox = document.getElementById('header').querySelector('.header-box.login');
    loginBox.innerHTML = '';
    if (!checkLogin()) {
        const userBox = new DOMParser().parseFromString(`
        <div class="user-box">
            <button class="login" type="button" onclick="location.href = '/login'">Log in</button>
            <button class="sign-up" type="button" onclick="location.href = '/join'">Sign up</button>
        </div>
        `, 'text/html').querySelector('.user-box');
        loginBox.append(userBox);
    } else {
        const userBox = new DOMParser().parseFromString(`
        <div class="user-box">
            <button class="logout" type="button" onclick="logout()">Log out</button>
        </div>
        `, 'text/html').querySelector('.user-box');
        loginBox.append(userBox);
    }
}

function logout() {
    const logoutModal = new ModalObj();
    logoutModal.createModal('알림', '로그아웃 하시겠습니까?', [{
        title: '확인',
        onclick: () => {
            localStorage.removeItem('Authorization');

            fetch('/api/logout')
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    if (data.success === true) {
                        logoutModal.createModal('알림', '로그아웃에 성공하였습니다.', [{
                            title: '확인',
                            onclick: () => {
                                logoutModal.delete();
                                location.href = '/'
                            }
                        }]);
                    }
                    if (data.success === false) {
                        const resetModal = new ModalObj();
                        resetModal.createSimpleModal('알림', data.message);
                    }
                }).catch(error => console.error('Error:', error));
        }
    }, {
        title: '취소',
        onclick: () => logoutModal.delete()
    }])
}

// 정상적인 형식이면 true 를 반환
function checkBirth(input) {

    const month = parseInt(input.substring(2, 4));
    const day = parseInt(input.substring(4, 6));

    if (month === 0 || month > 12) {
        return false;
    } else if (day === 0 || day > 31) {
        return false;
    }
    return true;
}

function refresh() {
    fetch('/api/refresh', {}).then(response => response.json())
        .then(data => {
            if (data.success === true) {
                const Authorization = data.data;
                localStorage.setItem("Authorization", Authorization);
            }
            if (data.success === false) {
                logout();
                const refreshModal = new ModalObj();
                refreshModal.createModal('알림', '로그인 기간이 만료되었습니다.', [{
                    title: '확인',
                    onclick : () => location.href = "/login"
                }])
            }
        })
        .catch(error => console.error('Error:', error));
}

setLoginBox();