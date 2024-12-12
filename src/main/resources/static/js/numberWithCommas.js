function numberWithCommas(x) {
	x = x.toString();
	var pattern = /(-?\d+)(\d{3})/;
	while (pattern.test(x))
		x = x.replace(pattern, "$1,$2");
	return x;
}

function applyNumberWithCommas() {
	const elementsToFormat = document.getElementsByClassName("numberWithCommas");			
	for(var i = 0; i < elementsToFormat.length; i++)
	{
		const originalValue = elementsToFormat[i].textContent;
		const formattedValue = numberWithCommas(originalValue);
		elementsToFormat[i].textContent = formattedValue;
	}
}