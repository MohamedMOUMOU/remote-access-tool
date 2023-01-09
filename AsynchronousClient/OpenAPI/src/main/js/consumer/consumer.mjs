import OpenApiDefinition from "open_api_definition";
import {writeFile} from 'fs/promises';

const clientPromise = Promise.resolve(new OpenApiDefinition.RatApi());

const getProcessesPromise = clientPromise.then(RAT => RAT.getProcesses()).then(result => console.log(result)).catch(err => console.log(err));

let screenshotName = "screenshot1";

const takeScreenshotPromise = clientPromise.then(RAT => {
	return RAT.takeScreenshot(screenshotName);
}).then(result => {
	return writeFile("screenshots/" + screenshotName + ".jpg", result,  "binary");
})
.then(result => {
	console.log('Image Saved');
})
.catch(err => {
	console.log(err);
});

let waitingTime = 180;

Promise.all([getProcessesPromise, takeScreenshotPromise]).then(values => clientPromise)
.then (RAT => RAT.reboot(waitingTime))
.then(result => {
	console.log(result);
}).catch(err => {
	console.log(err);
});
