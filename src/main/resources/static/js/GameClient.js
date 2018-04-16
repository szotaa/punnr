import ChatHandler from '/static/js/ChatHandler.js';
import CanvasHandler from '/static/js/CanvasHandler.js';

window.onload = function gameClient() {

    let stompClient = null;
    let gameId = document.getElementById('gameId').innerHTML;

    let canvasHandler = null;
    let chatHandler = null;

    (function connect() {
        let socket = new SockJS('http://localhost:8080/game');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/user/queue/' + gameId, function (message) {
                receiveFromServer(JSON.parse(message.body));
            });
        });
        canvasHandler = new CanvasHandler(sendToServer);
        chatHandler = new ChatHandler(sendToServer);
    })();

    function sendToServer(path, message){
        stompClient.send('/game/' + gameId + path, {}, message);
    }

    function receiveFromServer(message) {
        if(message.hasOwnProperty('line')){
            canvasHandler.draw(message['line']);
        }
        else if(message.hasOwnProperty('chatMessage')){
            chatHandler.appendMessageToChat(message['chatMessage']);
        }
        else if(message.hasOwnProperty('event')){
            processEvent(message['event']);
        }
    }

    function processEvent(event) {
        if(event['eventType'] === 'ROUND_WON'){
            chatHandler.appendEventToChat(event.author + ' won this round! correct answer was: ' + event.message);
            canvasHandler.clearDrawing();
        }
    }
};