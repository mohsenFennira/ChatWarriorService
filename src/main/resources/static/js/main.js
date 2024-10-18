'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let nickname = null;
let fullname = null;
let selectedUserId = null;

function connect(event) {
    nickname = document.querySelector('#nickname').value.trim();
    fullname = document.querySelector('#fullname').value.trim();

    if (nickname && fullname) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    stompClient.subscribe(`/user/${nickname}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);

    // register the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'ONLINE'})
    );
    document.querySelector('#connected-user-fullname').textContent = fullname;
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();
    connectedUsers = connectedUsers.filter(user => user.nickName !== nickname);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.nickName;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.fullName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.fullName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';

}
function displayMessage(senderId, content, imageBytes,mediaType) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');

    if (senderId === nickname) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }


    if (content) {
        const message = document.createElement('p');
        message.textContent = content;
        messageContainer.appendChild(message);
    }
    console.log(mediaType);
    if (imageBytes) {
        if(mediaType==='image'){
            const image = document.createElement('img');
            image.src = `data:image/png;base64,${imageBytes}`;
            image.alt = 'Image message';
            image.classList.add('chat-image');
            messageContainer.appendChild(image);
        }
        else{
            const video = document.createElement('video');
            video.src = `data:video/mp4;base64,${imageBytes}`;
            video.controls = true;
            video.classList.add('chat-video');
            messageContainer.appendChild(video);
        }

    }
    chatArea.appendChild(messageContainer);
    chatArea.scrollTop = chatArea.scrollHeight;
}


async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${nickname}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content,chat.imageBytes,chat.type);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    const imageFile = document.querySelector('#imageInput').files[0];

    if (stompClient) {
        if (messageContent || imageFile) {
            let chatMessage = {
                senderId: nickname,
                recipientId: selectedUserId,
                content: messageContent,
                timestamp: new Date()
            };


            if (imageFile) {
                const reader = new FileReader();
                reader.readAsDataURL(imageFile);

                reader.onload = () => {
                    const base64Image = reader.result.split(',')[1];
                    let mediaType = null;
                    if (imageFile.type.startsWith('image/')) {
                        mediaType = 'image';
                    } else if (imageFile.type.startsWith('video/')) {
                        mediaType = 'video';
                    }
                    chatMessage.type = mediaType;
                    chatMessage.imageBytes = base64Image;
                    stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
                    displayMessage(nickname, messageContent, base64Image, mediaType );
                };
            } else {
                stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
                displayMessage(nickname, messageContent);
            }

            messageInput.value = '';
            document.querySelector('#imageInput').value = '';
        }
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}



async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    const imageBytes = message.imageBytes ? message.imageBytes : null;
    const mediaType = message.type ? message.type : null;
    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content,imageBytes,mediaType);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'OFFLINE'})
    );
    window.location.reload();
}

usernameForm.addEventListener('submit', connect, true); // step 1
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();
