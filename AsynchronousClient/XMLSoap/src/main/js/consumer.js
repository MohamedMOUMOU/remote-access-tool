let soap = require('soap-as-promised');
let fs = require('fs').promises;

let url = '/home/mohamed/Desktop/Paradigms-projects/Project-3/XMLSoap/app/src/main/resources/RATService.wsdl';

const clientPromise = soap.createClient(url);

const getProcessesPromise = clientPromise.then(RAT => RAT.getProcesses()).then(result => console.log(result.return)).catch(err => console.log(err));

let screenshotName = "screenshot1";

const takeScreenshotPromise = clientPromise.then(RAT => {
	return RAT.takeScreenshot({arg0: screenshotName});
}).then(result => {
	var buf = Buffer.from(result.return, 'base64');
	return fs.writeFile("screenshots/" + screenshotName + ".jpg", buf);
}).then(result => {
	console.log('Image Saved');
}).catch(err => {
	console.log(err);
});

let waitingTime = 180;

Promise.all([getProcessesPromise, takeScreenshotPromise]).then(values => clientPromise)
.then (RAT => RAT.reboot({arg0: waitingTime}))
.then(result => {
	console.log(result.return);
}).catch(err => {
	console.log(err);
});
