// static/scripts/community/delete.js
document.addEventListener("DOMContentLoaded", function () {
    console.log("커뮤니티 삭제 페이지 스크립트")

    const main = document.getElementById("main");
    const boardId = main.getAttribute("data-board-id");

    // 게시물 목록 API 호출
    fetch(`http://localhost:8080/api/community/detail/${boardId}`, {
        method: 'GET',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);

            // 게시물 데이터 추출
            const post = data.data;
            // html 각 요소 준비
            const nickname = document.getElementById("nickname");
            const subject = document.getElementById("subject");

            // 작성자 삽입
            nickname.value = post.nickname;
            // 제목 삽입
            subject.value = post.subject;

        })
        .catch((error) => {
            console.error("Error fetching result data:", error);
            const errorMessage = document.createElement("p");
            errorMessage.textContent = "게시물 데이터를 불러오는 데 실패했습니다. 나중에 다시 시도해주세요.";
            main.appendChild(errorMessage);
        });
});

async function deleteSubmit() {

    const boardId = main.getAttribute("data-board-id");
    const password = document.getElementById("password").value;

    if (password === '') {
        alert('비밀번호를 입력해 주세요.');
        return;
    }

    // `RequestWriteDTO` 데이터 준비
    const requestDTO = {
        boardId: boardId,
        password: password
    };

    refresh();
    try {
        const response = await fetch("http://localhost:8080/api/community/delete", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                Authorization: localStorage.getItem("Authorization")
            },
            body: JSON.stringify(requestDTO)
        });

        const data = await response.json();

        if (response.ok) {
            const post = data.data;
            console.log("게시물 삭제 성공:", post);
            const nickname = post.nickname;
            const subject = post.subject;
            alert(`게시물이 성공적으로 삭제되었습니다!\n제목: ${subject}\n작성자: ${nickname}`);
            window.location.href = `/community`;

        } else {
            // 오류 메시지 처리
            console.error("게시물 삭제 실패:", data.message);
            alert("게시물 삭제에 실패했습니다: " + data.message);  // 오류 메시지를 사용자에게 표시
        }
    } catch (error) {
        console.error("API 호출 중 오류 발생:", error);
        alert("네트워크 오류가 발생했습니다.");
    }
}