var cheetahChart=new Object();
var initializeCheetahChart = function() {
	generateChart("cpu");
	generateChart("memory");
	generateChart("disk");
}

var generateChart=function(name){
	var lineData = {
			labels : [],
			datasets : [ {
				label : "CPU",
				fillColor : "rgba(220,220,220,0.2)",
				strokeColor : "rgba(220,220,220,1)",
				pointColor : "rgba(220,220,220,1)",
				pointStrokeColor : "#fff",
				pointHighlightFill : "#fff",
				pointHighlightStroke : "rgba(220,220,220,1)",
				data : []
			} ]
		};

		var id = document.getElementById(name).getContext("2d");
		var line = new Chart(id).Line(lineData, {
			responsive : true,
			bezierCurve : false,
			pointDot : false,
			scaleShowGridLines : false,
			scaleBeginAtZero : true
		});

		var timer = window.setInterval("updateChart('"+name+"')", 2000);
		cheetahChart[name]={"axisX":0,"line":line,"timer":timer};
}

var updateChart = function(name) {
	var xhr = getXHR();
	xhr.open("GET", "/CheetahWeb/servlet/getData", true);
	xhr.setRequestHeader("If-Modified-Since", "0");
	xhr.send();
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var content = JSON.parse(xhr.responseText);
			var increment = 0;
			var currentAxisX = cheetahChart[name].axisX;
			while (content.data[increment]) {
				cheetahChart[name].line.addData([ content.data[increment] ],
						currentAxisX + increment);
				increment++;
			}
			cheetahChart[name].axisX += increment;
			cheetahChart[name].line.update();
		}
	}
}

var stopUpdateChart = function(timer) {
	clearTimeout(timer);
}

var stopAllUpdateChart=function(){
	for(var name in cheetahChart){
		stopUpdateChart(cheetahChart[name].timer);
	}
}

var getXHR = function() {
	var xhr;
	if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	} else {
		// code for IE6, IE5
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xhr;
}