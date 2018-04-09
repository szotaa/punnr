window.onload = function (ev) {
    'use strict';

    var stompClient = null;
    var gameId = document.getElementById('gameId').innerHTML;
    var sendButton = document.getElementById('sendButton');

    (function connect() {
        var socket = new SockJS('http://localhost:8080/game');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/' + gameId, function (message) {
                console.log('recieved from socket: ' + message);
            });
        });
    })();

    sendButton.addEventListener('click', function () {
        var content = document.getElementById('textInput').value;
        console.log('sent to socket: ' + textInput);
        stompClient.send("/game/" + gameId + "/chat", {}, JSON.stringify({'author': 'szotaa', 'content': content}));
    })
};