from zeep import Client

RAT = Client('/home/mohamed/Desktop/Paradigms-projects/Project-2/app/src/main/resources/RATService.wsdl').service

ans = True
while ans:
	print ("1. Get processes of the remote system\n2. Take a screenshot of the remote system and store it in your device\n3. Reboot the remote system\n4. Exit")
	ans=input("What would you like to execute?")
	if ans=="1": 
		result = RAT.getProcesses()
		print(result)
	elif ans=="2":
		screenshotName = input("Please give the screenshot's name: ")
		with open("screenshots/" + screenshotName + ".jpg", "wb") as binary_file:
			binary_file.write(RAT.takeScreenshot(screenshotName))
		print('Image Saved')
	elif ans=="3":
		waitingTime = input("Please give the waiting time before reboot: ")
		result = RAT.reboot(int(waitingTime))
		print(result)
	elif ans=="4":
		print("Exited successfully!")
		break
	elif ans !="":
		print("Not Valid Choice, please try again") 
