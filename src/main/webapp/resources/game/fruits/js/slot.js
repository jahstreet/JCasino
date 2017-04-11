var context = $('#canvas')[0].getContext('2d'),
    sprite = document.createElement('img'),
    width,
    height,
    pressedKey,
    spin_sound = loadAudio(['/resources/game/fruits/audio/spin.wav']),
    theme = loadAudio(['/resources/game/fruits/audio/main-theme.mp3'], 0.4),
    win1 = loadAudio(['/resources/game/fruits/audio/win1.mp3']),
    win2 = loadAudio(['/resources/game/fruits/audio/win2.mp3']),
    stopReel = loadAudio(['/resources/game/fruits/audio/stop_reel.mp3']),
    isSpinning = false,
    isLinesShown = false,
    cur_off1 = -Math.ceil(Math.random() * 59) * 150,
    cur_off2 = -Math.ceil(Math.random() * 59) * 150,
    cur_off3 = -Math.ceil(Math.random() * 59) * 150,
    money = $('#money-info'),
    streakInfo = $('#streak-info-text'),
    rollNumber = $('#roll-number-text'),
    winTimeout = 2000,
    musicDiv = $('#music')[0];

sprite.src = "/resources/game/fruits/img/reel.png";
sprite.onload = function () {
    width = sprite.width;
    height = sprite.height - 300;
    drawReel(cur_off1, cur_off2, cur_off3);
    drawDelims();
};

setInterval(function () {
    if (pressedKey == 27) {
        theme.stop();
        musicDiv.innerHTML = '&#9836;';
    }
    if (pressedKey == 32) {
        spin();
    }
    pressedKey = 0;
}, 200);


$(document).ready(function () {
    theme.play();
    document.getElementById('body').onkeyup = function (e) {
        pressedKey = e.keyCode;
    };
    musicDiv.onclick = function () {
        if (theme.state == 'play') {
            theme.stop();
            this.innerHTML = '&#9836;';
        } else {
            theme.play();
            this.innerHTML = '&#9835;';
        }
    }
});

function loadAudio(arr, vol) {
    var audio = document.createElement('audio');
    var o = {
        dom: false,
        state: 'stop',
        play: function () {
            this.dom.currentTime = 0;
            this.dom.play();
            this.state = 'play';
        },
        pause: function () {
            this.dom.pause();
            this.state = 'pause';
        },
        stop: function () {
            this.dom.pause();
            this.dom.currentTime = 0;
            this.state = 'stop';
        },
        setVolume: function (vol) {
            this.dom.volume = vol;
        }
    };
    audio.src = arr;
    for (var i = 0; i < arr.length; i++) {
        var source = document.createElement('source');
        source.src = arr[i];
        audio.appendChild(source);
    }
    audio.volume = vol || 1;
    o.dom = audio;
    return o;
}

//рисуем барабаны
function drawReel(pos1, pos2, pos3) {
    context.drawImage(sprite, 70, pos1);
    context.drawImage(sprite, 230, pos2);
    context.drawImage(sprite, 390, pos3);
}
// рисуем разделители барабанов
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
//рисуем линию
var drawLine = function (startX, startY, endX, endY, colorLine, number) {
    context.beginPath();
    context.moveTo(startX, startY);
    context.lineTo(endX, endY);
    context.lineWidth = 7;
    context.strokeStyle = colorLine;
    context.stroke();

    context.beginPath();
    context.arc(startX - 25, startY, 10, 0, 2 * Math.PI, false);
    context.fillStyle = colorLine;
    context.fill();
    context.stroke();

    context.beginPath();
    context.fillStyle = "white";
    context.font = "normal 12pt Arial";
    context.textAlign = "center";
    context.textBaseline = "middle";
    context.fillText(number, startX - 25, startY);
    context.stroke();
};

var showLine = function (lineNumber) {
    switch (lineNumber) {
        case 1:
            line1();
            break;
        case 2:
            line2();
            break;
        case 3:
            line3();
            break;
        case 4:
            line4();
            break;
        case 5:
            line5();
            break
    }
};

var line1 = function () {
    drawLine(65, 225, 545, 225, "red", "1");
};

var line2 = function () {
    drawLine(65, 75, 545, 75, "blue", "2");
};

var line3 = function () {
    drawLine(65, 375, 545, 375, "green", "3");
};

var line4 = function () {
    drawLine(65, 20, 545, 430, "purple", "4");
};

var line5 = function () {
    drawLine(65, 430, 545, 20, "gold", "5");
};

function showLines() {
    if (!isSpinning) {
        isLinesShown = true;
        line5();
        line4();
        line3();
        line2();
        line1();
    }
}

function hideLines() {
    isLinesShown = false;
    context.save();
    context.setTransform(1, 0, 0, 1, 0, 0);
    context.clearRect(0, 0, 600, 450);
    context.restore();
    drawReel(cur_off1, cur_off2, cur_off3);
    drawDelims();
}

function lines() {
    if (isLinesShown) {
        hideLines();
    } else {
        showLines();
    }
}

function spin() {
    if (isLinesShown) {
        hideLines();
    }
    var spinForm = $('#spin'),
        result,
        offset1,
        offset2,
        offset3,
        lineResult,
        bet = parseFloat($('#bet').val()),
        l1 = $('#line1')[0].checked,
        l2 = $('#line2')[0].checked,
        l3 = $('#line3')[0].checked,
        l4 = $('#line4')[0].checked,
        l5 = $('#line5')[0].checked;

    var l = [l1, l2, l3, l4, l5],
        counter = 0;
    l.forEach(function (item) {
        if (item) {
            counter++;
        }
    });
    var totalBet = bet * counter;

    if (totalBet > parseFloat(money.text())) {
        alert('Not enough money.');
        return;
    }

    if (!isSpinning) {
        isSpinning = true;
        spin_sound.play();
        changeMoney(-totalBet, winTimeout / 4);
        var spinning = setInterval(function () {
            var stop1 = cur_off1 == offset1;
            var stop2 = cur_off2 == offset2;
            var stop3 = cur_off3 == offset3;
            if (!stop1) {
                cur_off1 -= 10;
                if (cur_off1 <= -height) {
                    cur_off1 = 0;
                }
                if (cur_off1 == offset1) {
                    stopReel.play();
                }
            }

            if (!stop2) {
                cur_off2 -= 10;
                if (cur_off2 <= -height) {
                    cur_off2 = 0;
                }
                if (cur_off2 == offset2) {
                    stopReel.play();
                }
            }
            if (!stop3) {
                cur_off3 -= 10;
                if (cur_off3 <= -height) {
                    cur_off3 = 0;
                }
                if (cur_off3 == offset3) {
                    stopReel.play();
                }
            }
            drawReel(cur_off1, cur_off2, cur_off3);
            if (stop1 && stop2 && stop3) {
                spin_sound.stop();
                clearInterval(spinning);
                checkWin(result, lineResult);
            }
        }, 1);

        $.ajax({
            url: "/ajax",
            type: "POST",
            data: spinForm.serialize(),
            dataType: "json",
            cache: false,
            error: function () {
                offset1 = -Math.ceil(Math.random() * 59) * 150;
                offset2 = -Math.ceil(Math.random() * 59) * 150;
                offset3 = -Math.ceil(Math.random() * 59) * 150;
                changeMoney(totalBet, winTimeout / 4);
                alert('Internet connection error.');
            },
            success: function (data) {
                var error = data.errorMessage;
                if (!error) {
                    var offset = data.offsets;
                    offset1 = -(offset[0] - 1) * 150;
                    offset2 = -(offset[1] - 1) * 150;
                    offset3 = -(offset[2] - 1) * 150;
                    result = parseFloat(data.winResult);
                    console.log(result);
                    lineResult = data.lines;
                    streakInfo.html(data.streakInfo);
                    rollNumber.html(data.rollNumber);

                } else {
                    offset1 = -(Math.ceil(Math.random() * 60) - 1) * 150;
                    offset2 = -(Math.ceil(Math.random() * 60) - 1) * 150;
                    offset3 = -(Math.ceil(Math.random() * 60) - 1) * 150;
                    result = totalBet;
                    lineResult = null;
                    alert(error);
                }
            }
        });
    }

    function checkWin(result, lineResult) {
        if (!result) {
            isSpinning = false;
            return;
        }
        var index = 0,
            timeout = 0,
            numberLinesWin = 0;

        lineResult.forEach(function (item) {
            if (item) {
                numberLinesWin++;
            }
        });

        if (!numberLinesWin) {
            isSpinning = false;
            return;
        }
        var winTrackNumber = Math.ceil(Math.random() * 2),
            winTrack;
        if (winTrackNumber == 1) {
            winTrack = win1;
        } else {
            winTrack = win2;
        }
        winTrack.play();
        // мирцание рамки
        var miracle = setInterval(function () {
            var r = Math.floor(Math.random() * (256));
            var g = Math.floor(Math.random() * (256));
            var b = Math.floor(Math.random() * (256));
            var c = '#' + r.toString(16) + g.toString(16) + b.toString(16);
            $('#canvas')[0].style.borderColor = c;
        }, 400);

        changeMoney(result, winTimeout * numberLinesWin - winTimeout / 8);

        (function check() {
            if (lineResult[index]) {
                timeout = winTimeout;
                var j = 0,
                    curIndex = index,
                    winning = setInterval(function () {
                        showLine(curIndex + 1);
                        setTimeout(hideLines, winTimeout / 8);
                        j++;
                        if (j > 2) {
                            clearInterval(winning);
                        }
                    }, winTimeout / 4);
            } else {
                timeout = 0;
            }
            index++;
            if (index < 5) {
                setTimeout(check, timeout);
            } else {
                setTimeout(function () {
                    isSpinning = false;
                    winTrack.stop();
                    clearInterval(miracle);
                }, winTimeout);
            }
        })();
    }
}

function changeMoney(amount, duration) {
    var currentAmount = parseFloat(money.text());
    $(function () {
        money.countTo({
            from: currentAmount,
            to: currentAmount + amount,
            speed: duration,
            refreshInterval: 50,
            decimals: 2
        });
    });
}

(function ($) {
    $.fn.countTo = function (options) {
        // merge the default plugin settings with the custom options
        options = $.extend({}, $.fn.countTo.defaults, options || {});

        // how many times to update the value, and how much to increment the value on each update
        var loops = Math.ceil(options.speed / options.refreshInterval),
            increment = (options.to - options.from) / loops;

        return $(this).each(function () {
            var _this = this,
                loopCount = 0,
                value = options.from,
                interval = setInterval(updateTimer, options.refreshInterval);

            function updateTimer() {
                value += increment;
                loopCount++;
                $(_this).html(value.toFixed(options.decimals));

                if (typeof(options.onUpdate) == 'function') {
                    options.onUpdate.call(_this, value);
                }

                if (loopCount >= loops) {
                    clearInterval(interval);
                    value = options.to;

                    if (typeof(options.onComplete) == 'function') {
                        options.onComplete.call(_this, value);
                    }
                }
            }
        });
    };

    $.fn.countTo.defaults = {
        from: 0,  // the number the element should start at
        to: 100,  // the number the element should end at
        speed: 1000,  // how long it should take to count between the target numbers
        refreshInterval: 100,  // how often the element should be updated
        decimals: 0,  // the number of decimal places to show
        onUpdate: null,  // callback method for every time the element is updated,
        onComplete: null  // callback method for when the element finishes updating
    };
})(jQuery);



