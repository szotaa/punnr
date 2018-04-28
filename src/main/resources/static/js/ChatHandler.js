export default class ChatHandler {

    constructor(sendCallback){
        this.sendCallback = sendCallback;
        this.messages = document.getElementById('messages');
        document.getElementById('sendButton').addEventListener('click', this.sendMessage.bind(this));
        this.scroll = document.getElementById('chat');
    }

    appendMessageToChat(message){
        let paragraph = document.createElement('P');
        let content = document.createTextNode(message.author + ": " + message.content);
        paragraph.appendChild(content);
        this.messages.appendChild(paragraph);
        this.scrollDown();
    }

    appendEventToChat(eventMessage){
        let paragraph = document.createElement('P');
        let content = document.createTextNode("event: " + eventMessage);
        paragraph.appendChild(content);
        this.messages.appendChild(paragraph);
        this.scrollDown();
    }

    sendMessage(){
        let content = document.getElementById('textInput').value;
        this.sendCallback('/chat', JSON.stringify({'author': null, 'content': content}));
        document.getElementById('textInput').value = "";
    }

    scrollDown(){
        this.scroll.scrollTop = this.scroll.scrollHeight;
    }
};