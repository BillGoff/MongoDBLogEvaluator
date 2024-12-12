function msToTime(duration) {
	negative = false;
			
	if (duration < 0)
	{
		duration = Math.abs(duration);
		negative = true;
	}
				
	var milliseconds = Math.floor((duration % 1000) / 1),
		seconds = Math.floor((duration / 1000) % 60),
		minutes = Math.floor((duration / (1000 * 60)) % 60),
		hours = Math.floor((duration / (1000 * 60 * 60)) % 24);
			
	hours = (hours < 10) ? "0" + hours : hours;
	minutes = (minutes < 10) ? "0" + minutes : minutes;
	seconds = (seconds < 10) ? "0" + seconds : seconds;
			
	if (negative == true )
	{
		return "-" + hours + ":" + minutes + ":" + seconds + "." + milliseconds;
	}
	else
	{
		return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
	}
}

function applyMsToTimeElements() {
	const elementsToFormat = document.getElementsByClassName("msToTimeElement");			
	for(var i = 0; i < elementsToFormat.length; i++)
	{
		const originalValue = elementsToFormat[i].textContent;
		const formattedValue = msToTime(originalValue);
		elementsToFormat[i].textContent = formattedValue;
	}
}