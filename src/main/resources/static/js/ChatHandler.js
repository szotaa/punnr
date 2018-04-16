export default class ChatHandler {

    constructor(sendCallback){
        this.sendCallback = sendCallback;
        this.messages = document.getElementById('messages');
        document.getElementById('sendButton').addEventListener('click', this.sendMessage.bind(this));
    }

    appendMessageToChat(message){
        let paragraph = document.createElement('P');
        let content = document.createTextNode(message.author + ": " + message.content);
        paragraph.appendChild(content);
        this.messages.appendChild(paragraph);
    }

    appendEventToChat(eventMessage){
        let paragraph = document.createElement('P');
        let content = document.createTextNode("event: " + eventMessage);
        paragraph.appendChild(content);
        this.messages.appendChild(paragraph);
    }

    sendMessage(){
        let content = document.getElementById('textInput').value;
        this.sendCallback('/chat', JSON.stringify({'author': null, 'content': content}));
    }
};