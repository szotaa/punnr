export default class CanvasHandler {

    constructor(sendCallback){
        this.canvas = document.getElementById('canvas');
        this.ctx = this.canvas.getContext('2d');
        this.isPainting = false;
        this.previousX = 0;
        this.previousY = 0;
        this.currentX = 0;
        this.currentY = 0;
        this.sendCallback = sendCallback;
        this.canvas.addEventListener('mousedown', this.mousedown.bind(this));
        this.canvas.addEventListener('mousemove', this.mousemove.bind(this));
        this.canvas.addEventListener('mouseup', this.mouseup.bind(this));
    }

    mousedown(e){
        this.isPainting = true;
        this.previousX = e.pageX - this.canvas.offsetLeft;
        this.previousY = e.pageY - this.canvas.offsetTop;
    };

    mousemove(e){
        if(this.isPainting){
            this.currentX = e.pageX - this.canvas.offsetLeft;
            this.currentY = e.pageY - this.canvas.offsetTop;
            this.sendCallback('/draw' ,JSON.stringify({'fromX': this.previousX, 'fromY': this.previousY, 'toX': this.currentX, 'toY': this.currentY}));
            this.previousX = this.currentX;
            this.previousY = this.currentY;
        }
    }

    mouseup(e){
        this.isPainting = false;
    }

    draw(line){
        this.ctx.beginPath();
        this.ctx.moveTo(line.fromX, line.fromY);
        this.ctx.lineTo(line.toX, line.toY);
        this.ctx.strokeStyle = 'red';
        this.ctx.lineWidth = 5;
        this.ctx.stroke();
        this.ctx.closePath();
    }


    clearDrawing(){
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
};