export default class ScoreboardHandler {
    constructor(){
        this.scoreboard = new Map();
        this.htmlScoreboard = document.getElementById('scoreboard');
    }

    onJoin(scoreboardFromServer){
        Object.keys(scoreboardFromServer).forEach(key =>{
            console.log('adding ' + key + ': ' + scoreboardFromServer[key]);
            this.scoreboard.set(key, scoreboardFromServer[key]);
            });
        this.updateHtmlScoreboard();
    }

    addEntry(nickname, score){
        console.log('adding: ' + nickname + 'to the scoreboard with: ' + score);
        this.scoreboard.set(nickname, score);
        this.updateHtmlScoreboard();
    }

    rewardWinners(guesserNickname, drawerNickname){
        let currentGuesserScore = parseInt(this.scoreboard.get(guesserNickname));
        let currentDrawerScore = parseInt(this.scoreboard.get(drawerNickname));
        console.log("score: " + currentGuesserScore + " " + typeof currentGuesserScore);
        this.scoreboard.set(guesserNickname, (currentGuesserScore + 10));
        this.scoreboard.set(drawerNickname, (currentDrawerScore + 10));
        this.updateHtmlScoreboard();
    }

    updateHtmlScoreboard(){
        this.clearScoreboard();
        for(let [key, value] of this.scoreboard){
            let tableRow = document.createElement('TR');
            let tdNickname = document.createElement('TD');
            let tdScore = document.createElement('TD');
            let nickname = document.createTextNode(key);
            tdNickname.appendChild(nickname);
            let score = document.createTextNode(value);
            tdScore.appendChild(score);
            tableRow.appendChild(tdNickname);
            tableRow.appendChild(tdScore);
            this.htmlScoreboard.appendChild(tableRow);
        }
    }

    clearScoreboard(){
        while (this.htmlScoreboard.firstChild){
            console.log('clearing element');
            this.htmlScoreboard.removeChild(this.htmlScoreboard.firstChild);
        }
    }
}