//circle start
let progressBar = document.querySelector('.e-c-progress');
let indicator = document.getElementById('e-indicator');
let pointer = document.getElementById('e-pointer');
let length = Math.PI * 2 * 100;
progressBar.style.strokeDasharray = length;
function update(value, timePercent) {
	var offset = - length - length * value / (timePercent);
	progressBar.style.strokeDashoffset = offset;
	pointer.style.transform = `rotate(${360 * value / (timePercent)}deg)`;
};
//circle ends
const displayOutput = document.querySelector('.display-remain-time')
const setterBtns = document.querySelectorAll('button[data-setter]');
let intervalTimer;
let timeLeft;
let wholeTime = 0.5 * 60; // manage this to set the whole time 
let isPaused = false;
let isStarted = false;

update(wholeTime, wholeTime); //refreshes progress bar
displayTimeLeft(wholeTime);
function changeWholeTime(seconds) {
	if ((wholeTime + seconds) > 0) {
		wholeTime += seconds;
		update(wholeTime, wholeTime);
	}
}

function pauseTimer(event){

    this.classList.remove('pause');
    this.classList.add('play');
    clearInterval(intervalTimer);
  
}

function timer(seconds) { //counts time, takes seconds
	let remainTime = Date.now() + (seconds * 1000);
	displayTimeLeft(seconds);

	intervalTimer = setInterval(function() {
		timeLeft = Math.round((remainTime - Date.now()) / 1000);
		if (timeLeft < 0) {
			clearInterval(intervalTimer);
			isStarted = false;
			setterBtns.forEach(function(btn) {
				btn.disabled = false;
				btn.style.opacity = 1;
			});
			displayTimeLeft(wholeTime);
			displayOutput.textContent = "00:00";
			return;
		}
		displayTimeLeft(timeLeft);
	}, 1000);
}
window.onload = function() {
	if (isStarted === false) {
		timer(wholeTime);
		isStarted = true;
		this.classList.remove('play');
		this.classList.add('pause');

		setterBtns.forEach(function(btn) {
			btn.disabled = true;
			btn.style.opacity = 0.5;
		});
	} else if (isPaused) {
		timer(timeLeft);
		isPaused = isPaused ? false : true
	} else {
		clearInterval(intervalTimer);
		isPaused = isPaused ? false : true;
	}
}
function displayTimeLeft(timeLeft) {
	let minutes = Math.floor(timeLeft / 60);
	let seconds = timeLeft % 60;
	let displayString = `${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
	displayOutput.textContent = displayString;
	update(timeLeft, wholeTime);
}
const pauseBtn = document.getElementById('play');
pauseBtn.addEventListener('click', pauseTimer);