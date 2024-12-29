
document.addEventListener("DOMContentLoaded", function() {
    event.preventDefault();
    const accessToken = localStorage.getItem('Authorization')


    const guideSection = document.getElementById("color-guide");
    const nonMemberSection = document.getElementById("non-member");
    if (!checkLogin()) {
        const spanElement1 = document.getElementById("guide-d1");
        const spanElement2 = document.getElementById("guide-d2");
        guideSection.style.display = 'none';
        nonMemberSection.style.display = 'flex';
        spanElement1.style.display = 'none';
        spanElement2.style.display = 'none';
        document.getElementById("non-member-login").addEventListener("click", function () {
            location.href = "/login"; // 진단 내역 페이지로 이동
        });

    } else {
        guideSection.style.display = 'flex';
        nonMemberSection.style.display = 'none';

        fetch('http://localhost:8080/api/results/color', {
            method: 'GET',
            headers: {
                'Authorization': accessToken
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                const spanElement1 = document.getElementById("guide-c1");
                const spanElement2 = document.getElementById("guide-c2");
                const imageElement = document.getElementById("color-image");
                if (data.data.colorId === 1) {
                    spanElement1.innerHTML = "봄 웜톤";
                    spanElement2.innerHTML = "봄 웜톤";
                    spanElement1.style.color = "#f65882"; // 밝은 주황색 계열
                    imageElement.src = "/imgs/spring.JPG";
                } else if (data.data.colorId === 2) {
                    spanElement1.innerHTML = "여름 쿨톤";
                    spanElement2.innerHTML = "여름 쿨톤";
                    spanElement1.style.color = "#358ccd"; // 밝은 파란색 계열
                    imageElement.src = "/imgs/summer.JPG";
                } else if (data.data.colorId === 3) {
                    spanElement1.innerHTML = "가을 웜톤";
                    spanElement2.innerHTML = "가을 웜톤";
                    spanElement1.style.color = "#c54603"; // 따뜻한 갈색 계열
                    imageElement.src = "/imgs/autumn.JPG";
                } else if (data.data.colorId === 4) {
                    spanElement1.innerHTML = "겨울 쿨톤";
                    spanElement2.innerHTML = "겨울 쿨톤";
                    spanElement1.style.color = "#081b63"; // 진한 파란색 계열
                    imageElement.src = "/imgs/winter.JPG";
                } else {
                    document.getElementById("guide-d1").innerHTML = "진단을 내기에 결과가 충분하지 않습니다.";
                    document.getElementById("guide-d2").innerHTML = "진단을 여러번 더 진행해주세요";
                }
            })
            .catch((error) => {
                console.error("Error fetching result data:", error);
            });


        fetch('http://localhost:8080/api/guide', {
            method: 'GET',
            headers: {
                'Authorization': accessToken
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log(`가이드데이터: ${data}`);
                console.log(data);
                const hairGuide = document.getElementById("hair-guide");
                const fashionGuide = document.getElementById("fashion-guide");
                const makeupGuide = document.getElementById("makeup-guide");
                for(let i = 0; i<data.data.length; i++){
                    const row = document.createElement("p");

                    row.innerHTML = `<div class="guide-detail">- ${data.data[i].description}</div>`;
                    if (data.data[i].categoryId === 1 || data.data[i].categoryId === 4) {
                        hairGuide.appendChild(row);
                    } else if (data.data[i].categoryId === 2 || data.data[i].categoryId === 5 || data.data[i].categoryId === 9 || data.data[i].categoryId === 6 || data.data[i].categoryId === 7 || data.data[i].categoryId === 8 || data.data[i].categoryId === 10 || data.data[i].categoryId === 11 || data.data[i].categoryId === 12) {
                        fashionGuide.appendChild(row);
                    } else if (data.data[i].categoryId === 3 || data.data[i].categoryId === 13 || data.data[i].categoryId === 14 || data.data[i].categoryId === 15) {
                        makeupGuide.appendChild(row);
                    }
                }
            })
            .catch((error) => {
                console.error("Error fetching result data:", error);
            });
    }
});



