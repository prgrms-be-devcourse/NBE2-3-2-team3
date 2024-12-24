// 모달 관련
class ModalObj {
    cover = null;

    constructor() {
        if (this.cover === null) {
            const cover = document.createElement('div');
            cover.setAttribute('id', 'cover');
            document.body.prepend(cover);
            this.cover = cover;
        }
    }

    createModal(title, content, buttonOptions) {
        this.cover.innerHTML = '';

        const modal = new DOMParser().parseFromString(`
        <div class="modal">
            <div class="modal-head">${title}</div>
            <div class="modal-main">${content}</div>
            <div class="button-container"></div>
        </div>
        `, 'text/html').querySelector('.modal');

        const buttonContainer = modal.querySelector('.button-container');
        for (const buttonOption of buttonOptions) {
            const button = this.createButton(buttonOption.title, buttonOption.onclick);
            buttonContainer.append(button);
        }

        this.cover.append(modal);
    }

    createSimpleModal(title, content) {
        this.cover.innerHTML = '';

        const simpleModal = new DOMParser().parseFromString(`
        <div class="modal">
            <div class="modal-head">${title}</div>
            <div class="modal-main">${content}</div>
            <div class="button-container">
                <button type="button">닫기</button>
            </div>
        </div>
        `, 'text/html').querySelector('.modal');

        simpleModal.querySelector('button').onclick = () => {
            this.delete();
        }

        this.cover.append(simpleModal);
    }

    createButton(text, onclick) {
        const button = document.createElement('button');
        button.innerText = text;
        if (typeof onclick === 'function') {
            button.onclick = () => {
                onclick();
            }
        }
        return button;
    }

    delete() {
        this.cover.remove();
    }
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

            logoutModal.createModal('알림', '로그아웃에 성공하였습니다.', [{
                title: '확인',
                onclick: () => {
                    logoutModal.delete();
                    location.href = '/'
                }
            }])
        }
    }, {
        title: '취소',
        onclick: () => logoutModal.delete()
    }])
}

setLoginBox();