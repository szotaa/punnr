window.onload = function (ev) {
    'use strict';

    var stompClient = null;
    var gameId = document.getElementById('gameId').innerHTML;
    var sendButton = document.getElementById('sendButton');
    var canvas = document.getElementById('canvas');
    var ctx = canvas.getContext('2d');
    var previousX, previousY, currentX, currentY;
    var isPainting;

    (function connect() {
        var socket = new SockJS('http://localhost:8080/game');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/user/queue/' + gameId, function (message) {
                var body = JSON.parse(message.body);

                if(body.hasOwnProperty('line')){
                    processLine(body['line']);
                }
                else if(body.hasOwnProperty('chatMessage')){
                    var chatMessage = body['chatMessage'];
                    addToChatMessage(chatMessage.author + ": " + chatMessage.content);
                }
                else if(body.hasOwnProperty('event')){
                    processEvent(body['event']);
                }
            });
        });
    })();


    var mousedown = function (e){
        isPainting = true;
        previousX = e.pageX - this.offsetLeft;
        previousY = e.pageY - this.offsetTop;
    };

    var mousemove = function (e){
        if(isPainting){
            currentX = e.pageX - this.offsetLeft;
            currentY = e.pageY - this.offsetTop;
            sendLine(previousX, previousY, currentX, currentY);
            previousX = currentX;
            previousY = currentY;
        }
    };

    function sendLine(fromX, fromY, toX, toY) {
        stompClient.send('/game/' + gameId + '/draw', {}, JSON.stringify({'fromX': fromX, 'fromY': fromY, 'toX': toX, 'toY': toY}));
    }

    var mouseup = function (e){
        isPainting = false;
    };

    function draw(fromX, fromY, toX, toY){
        ctx.beginPath();
        ctx.moveTo(fromX, fromY);
        ctx.lineTo(toX, toY);
        ctx.strokeStyle = 'red';
        ctx.lineWidth = 5;
        ctx.stroke();
        ctx.closePath();
    }

    function processEvent(event) {
        if(event['eventType'] === 'ROUND_WON'){
            addToChatMessage(event['author'] + " won this round!!! Correct answer was: " + event['message']);
            clearDrawing();
            rewardPlayers(event['author']);
        }
        else if(event['eventType'] === 'ROUND_STARTED'){
            addToChatMessage('new round started')
        }
        else if(event['eventType'] === 'PLAYER_JOINED'){
            addToChatMessage(event['author'] + ' joined the game')
        }
    }

    function addToChatMessage(message) {
        var messages = document.getElementById('messages');
        var paragraph = document.createElement('P');
        var content = document.createTextNode(message);
        paragraph.appendChild(content);
        messages.appendChild(paragraph);
    }

    function processLine(line) {
        draw(line.fromX, line.fromY, line.toX, line.toY);
    }

    function clearDrawing(){
        ctx.clearRect(0, 0, canvas.width, canvas.height);
    }

    function rewardPlayers(username) {
        //var scores = document.getElementById('scores');
        console.log(username + ' rewarded with 10 points');
    }

    sendButton.addEventListener('click', function () {
        var content = document.getElementById('textInput').value;
        console.log('sent to socket: ' + content);
        stompClient.send("/game/" + gameId + "/chat", {}, JSON.stringify({'author': 'szotaa', 'content': content}));
    });

    canvas.addEventListener("mousedown", mousedown);
    canvas.addEventListener("mousemove", mousemove);
    canvas.addEventListener("mouseup", mouseup);
};