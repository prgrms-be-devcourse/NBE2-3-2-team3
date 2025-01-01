// static/scripts/community/modify.js
document.addEventListener("DOMContentLoaded", function () {
    console.log("커뮤니티 수정 페이지 스크립트")

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
            const subject = document.getElementById("subject");
            // const category = document.getElementById("category");
            const content = document.getElementById("content");
            const file = document.getElementById("file");

            // 제목 삽입
            subject.value = post.subject;
            // 카테고리 삽입
            // category.innerHTML = post.category;
            // 내용 삽입
            content.value = post.content;

            // 파일 정보 추가 UI
            if (post.imagename) {
                const fileInfoWrapper = document.createElement("div");
                fileInfoWrapper.id = "file-info-wrapper";

                const fileInfo = document.createElement("span");
                fileInfo.textContent = `업로드된 파일: ${post.imagename}`;
                fileInfo.style.marginRight = "10px";

                const deleteButton = document.createElement("button");
                deleteButton.textContent = "X";
                deleteButton.style.color = "red";
                deleteButton.style.border = "none";
                deleteButton.style.background = "none";
                deleteButton.style.cursor = "pointer";

                deleteButton.addEventListener("click", function () {
                    fileInfoWrapper.remove(); // 파일 정보 UI 삭제
                    file.value = ""; // 새 파일 입력 가능
                    post.imagename = null; // 기존 파일 데이터 제거
                });

                fileInfoWrapper.appendChild(fileInfo);
                fileInfoWrapper.appendChild(deleteButton);
                file.insertAdjacentElement("beforebegin", fileInfoWrapper);
            }

        })
        .catch((error) => {
            console.error("Error fetching result data:", error);
            const errorMessage = document.createElement("p");
            errorMessage.textContent = "게시물 데이터를 불러오는 데 실패했습니다. 나중에 다시 시도해주세요.";
            main.appendChild(errorMessage);
        });
});

async function modifySubmit() {

    const boardId = main.getAttribute("data-board-id");

    // 폼 데이터 수집
    const subject = document.getElementById("subject").value;
    const content = document.getElementById("content").value;
    const category = "BASIC"; // 카테고리 기본값 설정
    const fileInput = document.getElementById("file");
    const fileName = document.getElementById("file-info-wrapper") ? document.getElementById("file-info-wrapper")
        .textContent.replace("업로드된 파일: ", "") : null;

    if (subject === '') {
        alert('제목을 입력해 주세요.');
        return;
    }
    if (content === '') {
        alert('내용을 입력해 주세요.');
        return;
    }

    // `RequestWriteDTO` 데이터 준비
    const requestDTO = {
        boardId: boardId,
        subject: subject,
        content: content,
        category: category,
        imagename: fileName // 기존 파일이 있으면 이름 전달
    };

    // FormData 생성
    const formData = new FormData();
    formData.append("to", new Blob([JSON.stringify(requestDTO)], { type: "application/json" }));

    if (fileInput.files.length > 0) {
        formData.append("file", fileInput.files[0]);
    }

    try {
        const response = await fetch("http://localhost:8080/api/community/update", {
            method: "PUT",
            headers: {
                Authorization: localStorage.getItem("Authorization")
            },
            body: formData
        });

        const data = await response.json();

        if (response.ok) {
            const result = data;
            console.log("게시물 수정 성공:", result);
            alert("게시물이 성공적으로 수정되었습니다!");
            const post = result.data;
            if (post && post.boardId) {
                window.location.href = `/community/detail/${post.boardId}`;
            }
        } else {
            // 오류 메시지 처리
            console.error("게시물 수정 실패:", data.message);
            alert("게시물 수정에 실패했습니다: " + data.message);  // 오류 메시지를 사용자에게 표시
        }
    } catch (error) {
        console.error("API 호출 중 오류 발생:", error);
        alert("네트워크 오류가 발생했습니다.");
    }
}