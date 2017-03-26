var context;
var sprite;
var width;
var height;
var streak = "10 20 30";
var cur_off1 = 0, cur_off2 = 0, cur_off3 = 0;
var spin_sound = loadAudio(['/resources/game/fruits/audio/spin.wav']);
var theme = loadAudio(['/resources/game/fruits/audio/main-theme.mp3'], 0.4);
var isSpinning = false;
var isLinesShown = false;



context = $('canvas')[0].getContext('2d');
sprite = document.createElement('img');
sprite.src = "/resources/game/fruits/img/reel.png";
sprite.onload = function () {
	width = sprite.width;
	height = sprite.height-300;
	drawReel(cur_off1, cur_off2, cur_off3);
};
drawDelims();

function drawReel(offset1, offset2, offset3) {
	context.drawImage(sprite, 70, offset1);
	context.drawImage(sprite, 230, offset2);
	context.drawImage(sprite, 390, offset3);
}

function drawDelims() {
	context.beginPath();
	context.moveTo(65, 0);
	context.lineTo(65, 450);
	context.moveTo(225, 0);
	context.lineTo(225, 450);
	context.moveTo(385, 0);
	context.lineTo(385, 450);
	context.moveTo(545, 0);
	context.lineTo(545, 450);
	context.lineWidth = 10;
	context.strokeStyle = "grey";
	context.stroke();
}

var drawLine = function (startX, startY, endX, endY, colorLine, number) {
	context.beginPath();
	context.moveTo(startX, startY);
	context.lineTo(endX, endY);
	context.lineWidth = 7;
	context.strokeStyle = colorLine;
	context.stroke();

	context.beginPath();
	context.arc(startX-25, startY, 10, 0, 2*Math.PI, false);
	context.fillStyle = colorLine;
	context.fill();
	context.stroke();

	context.beginPath();
	context.fillStyle = "white";
	context.font = "normal 12pt Arial";
	context.textAlign = "center";
	context.textBaseline = "middle";
	context.fillText(number, startX-25, startY);
	context.stroke();
};

var line1 = function() {
	drawLine(65, 225, 545, 225, "red", "1");
};

var line2 = function() {
	drawLine(65, 75, 545, 75, "blue", "2");
};

var line3 = function() {
	drawLine(65, 375, 545, 375, "green", "3");
};

var line4 = function() {
	drawLine(65, 20, 545, 430, "purple", "4");
};

var line5 = function() {
	drawLine(65, 430, 545, 20, "gold", "5");
};

function showLines() {
	isLinesShown = true;
	line5();
	line4();
	line3();
	line2();
	line1();
};

function hideLines() {
	isLinesShown = false;
	context.save();
	context.setTransform(1, 0, 0, 1, 0, 0);
	context.clearRect(0, 0, 600, 450);
	context.restore();
	drawReel(cur_off1, cur_off2, cur_off3);
	drawDelims();
};

function lines() {
	if (isLinesShown) {
		hideLines();
	} else {
		showLines();
	}
}

function spin() {
	if (!isSpinning) {
		isSpinning = true;
		spin_sound.play();
		// var roll = streak;
		// var roll_arr = roll.split(" ");

		var a1 = Math.floor(Math.random()*60)+1;
		var a2 = Math.floor(Math.random()*60)+1;
		var a3 = Math.floor(Math.random()*60)+1;
		var offset = [a1,a2,a3];
		console.log(offset);
		offset[0] = -(offset[0]-1)*150;
		offset[1] = -(offset[1]-1)*150;
		offset[2] = -(offset[2]-1)*150;

		var spinning = setInterval(function() {
			var stop1 = cur_off1==offset[0];
			var stop2 = cur_off2==offset[1];
			var stop3 = cur_off3==offset[2];
			if (!stop1) {
				cur_off1-=10;
				if (cur_off1<=-height) {
					cur_off1=0;
				}
			}
			if (!stop2) {
				cur_off2-=10;
				if (cur_off2<=-height) {
					cur_off2=0;
				}
			}
			if (!stop3) {
				cur_off3-=10;
				if (cur_off3<=-height) {
					cur_off3=0;
				}
			}
			if (stop1 && stop2 && stop3) {
				spin_sound.stop();
				clearInterval(spinning);
				isSpinning = false;
			}
			drawReel(cur_off1, cur_off2, cur_off3);
		}, 1);
	}
};




// мирцание рамки
setInterval(function(){
	var r = Math.floor(Math.random() * (256));
	var g = Math.floor(Math.random() * (256));
	var b = Math.floor(Math.random() * (256));
	var c = '#' + r.toString(16) + g.toString(16) + b.toString(16);
	$('#canvas')[0].style.borderColor = c;
}, 400);

function loadAudio(arr, vol) {
	var audio = document.createElement('audio');
	var o = {
		dom : false,
		state : 'stop',
		play : function () {
			this.dom.currentTime = 0;
			this.dom.play();
			this.state = 'play';
		},
		pause : function () {
			this.dom.pause();
			this.state = 'pause';
		},
		stop : function() {
			this.dom.pause();
			this.dom.currentTime = 0;
			this.state = 'stop';
		},
		setVolume : function (vol) {
			this.dom.volume = vol;
		}
	};
	audio.src = arr;
	for (var i = 0; i < arr.length; i++){
		var source = document.createElement('source');
		source.src = arr[i];
		audio.appendChild(source);
	}
	audio.volume = vol || 1;
	o.dom = audio;
	return o;
}

var a;
theme.play();

setInterval(function () {
	if (a == 27) {
		theme.stop();
	}
	a=0;
}, 200);

$(document).ready(function () {
	var body = document.getElementById('body');
	body.onkeyup = function (e) {
		a = e.keyCode;
		console.log(e.keyCode);
	};
});



