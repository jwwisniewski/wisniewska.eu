document.getElementById('om').innerHTML = new_om(document.getElementById('om').innerHTML);

var om = document.getElementById('om');

addListener(om, 'click', function() {
  ga('send', 'event', 'email', 'click');
});

function new_om(m) {
	t = '';
	for (i = m.length - 1; i >= 0; i--)
		if (m.charAt(i) == '*')
			t += '@';
		else
			t += m.charAt(i);
	return'<a href="mailto:' + t + '" title="' + t + '">' + t + '</a>';
}

function addListener(element, type, callback) {
 if (element.addEventListener) element.addEventListener(type, callback);
 else if (element.attachEvent) element.attachEvent('on' + type, callback);
}
