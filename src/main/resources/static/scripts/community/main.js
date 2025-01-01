// static/scripts/community/main.js
document.addEventListener("DOMContentLoaded", function() {
    console.log("커뮤니티 메인 페이지 스크립트")

    const accessToken = localStorage.getItem('Authorization')
    console.log("accessToken: " + accessToken.toString())
    const buttonBody = document.getElementById("buttons");

    // 버튼 HTML 동적 생성
    if (accessToken) {
        buttonBody.innerHTML = `
            <div class="align_right">
            <input type="button" value="글 작성" class="btn_write btn_txt01" style="cursor: pointer;" onclick="location.href='/community/write'" />
            </div>
        `;
    }

    // 게시물 목록 API 호출
    fetch('http://localhost:8080/api/community', {
        method: 'GET',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(`게시물 목록 데이터: ${data}`);
            console.log(data);

            // 게시물 데이터 추출
            const posts = data.data.content;
            const tableBody = document.getElementById("post-list");

            // 현재 날짜 계산
            const today = new Date();
            const todayDate = `${today.getFullYear()}-${(today.getMonth() + 1).toString().padStart(2, '0')}-${today.getDate().toString().padStart(2, '0')}`;

            // 게시물 데이터를 HTML 테이블에 추가
            posts.forEach((post) => {
                const newRow = document.createElement("tr");

                // createdAt 날짜를 비교 가능한 형식으로 변환
                const createdAtDate = new Date(post.createdAt);
                const formattedCreatedAt = `${createdAtDate.getFullYear()}-${(createdAtDate.getMonth() + 1).toString().padStart(2, '0')}-${createdAtDate.getDate().toString().padStart(2, '0')}`;

                // icon_new.gif 표시 여부 결정
                const isNew = formattedCreatedAt === todayDate;
                const newIconHtml = isNew ? '<img src="/imgs/community/icon_new.gif" alt="NEW">' : '';

                // links 배열에서 href 추출
                const hrefLink = post.links.find(link => link.rel === 'self')?.href || '#';

                newRow.innerHTML = `
                    <td>&nbsp;</td>
                    <td>${post.boardId}</td>
                    <td class="left">
                        <a href="${hrefLink}">${post.subject}</a>&nbsp;
                        ${newIconHtml}
                    </td>
                    <td>${post.nickname}</td>
                    <td>${formattedCreatedAt}</td>
                    <td>${post.view}</td>
                    <td>&nbsp;</td>
                `;

                tableBody.appendChild(newRow);
            });

            // 데이터 총 갯수 id
            const boardCount = document.getElementById("boards-count");
            boardCount.innerHTML = `<div class="bold">총 <span class="txt_orange">${data.data.page.totalElements}</span>건</div>`;

            // 페이지 및 링크 데이터 추출
            const pages = data.data.page;
            const links = data.data.links;
            // 페이지 표시 id
            const pageNav = document.getElementById("page-nav");

            const currentPage = pages.number + 1; // 현재 페이지
            const totalPages = pages.totalPages; // 총 페이지 수
            const maxVisiblePages = 5; // 한 번에 표시할 페이지 번호 개수

            // 현재 페이지 그룹 계산 ( 서버에 가지고 있는 DTO 값을 추가해서 반환 - 나중에 )
            const currentGroup = Math.floor((currentPage - 1) / maxVisiblePages);
            const groupStartPage = currentGroup * maxVisiblePages + 1; // 현재 그룹의 첫 페이지 번호
            const groupEndPage = Math.min(groupStartPage + maxVisiblePages - 1, totalPages); // 현재 그룹의 마지막 페이지 번호

            let paginationHtml = '<div align="absmiddle">';

            // << 버튼 (firstPageOfPrevGroup 링크 사용)
            const firstPageOfPrevGroup = links.find(link => link.rel === "firstPageOfPrevGroup");
            paginationHtml += firstPageOfPrevGroup
                ? `<span><a href="${firstPageOfPrevGroup.href}">&lt;&lt;</a></span>`
                : '<span><a class="disabled">&lt;&lt;</a></span>';

            // < 버튼 (nextPage 링크 사용)
            const prevPage = links.find(link => link.rel === "prevPage");
            paginationHtml += prevPage
                ? `<span><a href="${prevPage.href}">&lt;</a></span>`
                : '<span><a class="disabled">&lt;</a></span>';

            // 페이지 번호
            for (let i = groupStartPage; i <= groupEndPage; i++) {
                if (i === currentPage) {
                    paginationHtml += `<span><a>[ ${i} ]</a></span>`;
                } else {
                    paginationHtml += `<span><a href="/community/${i}">${i}</a></span>`;
                }
            }

            paginationHtml += '&nbsp;&nbsp;';

            // > 버튼 (nextPage 링크 사용)
            const nextPage = links.find(link => link.rel === "nextPage");
            paginationHtml += nextPage
                ? `<span><a href="${nextPage.href}">&gt;</a></span>`
                : '<span><a class="disabled">&gt;</a></span>';

            paginationHtml += '&nbsp;';

            // >> 버튼 (lastPageOfNextGroup 링크 사용)
            const lastPageOfNextGroup = links.find(link => link.rel === "lastPageOfNextGroup");
            paginationHtml += lastPageOfNextGroup
                ? `<span><a href="${lastPageOfNextGroup.href}">&gt;&gt;</a></span>`
                : '<span><a class="disabled">&gt;&gt;</a></span>';

            paginationHtml += '</div>';

            // 페이지 네비게이션 HTML 삽입
            pageNav.innerHTML = paginationHtml;

        })
        .catch((error) => {
            console.error("Error fetching result data:", error);
            const tableBody = document.getElementById("post-list");
            tableBody.innerHTML = `<tr><td colspan="7">데이터를 로드할 수 없습니다.</td></tr>`;
        });
});


