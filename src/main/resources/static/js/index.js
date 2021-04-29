let indexPage;

document.addEventListener("DOMContentLoaded", function () {
    indexPage = new IndexPage();
    indexPage.initIndexPage();
});

document.querySelector(".make-room-button").addEventListener("click", function () {
    let newRoomTitle = document.querySelector(".newRoomId").value;
    fetch(indexPage.postRoomUrl + '?title=' + newRoomTitle, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(function (response) {
        if (response.ok) {
            return response.json().then(data => location.href = 'chess/' + data);
        }
        response.text().then(function (data) {
            alert(data);
        })
        location.reload();

    });
});

function IndexPage() {
    this.getRoomsUrl = window.location.origin + "/api/rooms";
    this.getRoomUrl = window.location.origin + "/api/room";
    this.postRoomUrl = window.location.origin + "/api/room";
}

IndexPage.prototype.initIndexPage = function () {
    const roomList = document.querySelector(".room-list");

    fetch(indexPage.getRoomsUrl, {
        method: 'GET'
    }).then(res => res.json())
        .then(async function (data) {
            for (let i = 0; i < data.roomNames.length; i++) {
                let sd = await getRoomId(data.roomNames[i]);
                roomList.innerHTML +=
                    `<li class="room">
                        <button class="room-button" onclick="location.href = 'chess/' + ${sd}">
                        ${data.roomNames[i]}
                        </button>
                    </li>`;
            }
        });
}

async function getRoomId(roomTitle) {
    return await fetch(indexPage.getRoomUrl + '?title=' + roomTitle, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(res => res.json());
}