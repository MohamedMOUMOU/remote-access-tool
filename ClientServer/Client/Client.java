import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
    public static void main(String[] args) throws Exception {
		String command = args[0];

		try (Socket remoteConnection = new Socket("localhost", 12323)) {
            InputStream in = remoteConnection.getInputStream();
            OutputStream out = remoteConnection.getOutputStream();

            BufferedReader headerReader = new BufferedReader(new InputStreamReader(in));
			BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(out));
            
            DataInputStream dataIn = new DataInputStream(in);

            // Error messages
            String wrongRemoteSystemError = "The remote system is not using the same protocol.";
            String unkownError = "Something wrong happened.";
            String remoteprocessesError = "Something wrong happend while getting the processes of the remote system.";
            String savingScreenshotError = "Something wrong happend while saving the screenshot on your device.";
            String remoteScreenshotError = "An error happened while taking or saving the screenshot on the remote device.";
            String remoteRebootError = "The remote system could not be rebooted.";

            
            // Success messages
            String screenshotSuccess = "Completed Successfully! You can find the screenshot at this location: ";
            String rebootSuccess = "Remote System will reboot after ";

			if (command.equals("ps")) {
                try {
                    // request header
                    String header = "PROCESSES\n";
                    headerWriter.write(header, 0, header.length());
                    headerWriter.flush();
                    
                    // Reponse header
                    String response = headerReader.readLine();
                    StringTokenizer strk = new StringTokenizer(response, " ");
                    String status = strk.nextToken();

                    if(status.equals("Error")){
                        System.out.println(remoteprocessesError);
                    } else if(status.equals("OK")) {
                        // Receiving the processes of the remote system as a string
                        String line = "";
                        while((line = headerReader.readLine()) != null) System.out.println(line);;
                    } else {
                        System.out.println(wrongRemoteSystemError);
                    }
                } catch(Exception e) {
                    System.out.println(unkownError);
                }
            } else if(command.equals("screenshot")) {
                try {
                    String location = args[1];

                    // Getting the screenshot name to send to teh remote system
                    String[] temp1 = location.split("/");
                    String screenchotName = temp1[temp1.length - 1];

                    // Sending the request header with the name of the screenshot
                    String request = "SCREENSHOT " + screenchotName + "\n";
                    headerWriter.write(request, 0, request.length());
                    headerWriter.flush();
                    
                    // Response header
                    String response = headerReader.readLine();
                    StringTokenizer strk = new StringTokenizer(response, " ");
                    String status = strk.nextToken();

                    if (status.equals("ERROR")){
                        System.out.println(remoteScreenshotError);
                    }
                    else if (status.equals("OK")) {
                        // Retrieving the bytes of the screenshot
                        String temp2 = strk.nextToken();
                        int size = Integer.parseInt(temp2);
                        byte[] space = new byte[size];
                        dataIn.readFully(space);

                        // saving the screenshot on the client's machine
                        try (FileOutputStream fileOut = new FileOutputStream(location)) {
                            fileOut.write(space, 0, size);
                            System.out.println(screenshotSuccess + location);
                        } catch(Exception e) {
                            System.out.println(savingScreenshotError);
                        }
                    } else {
                        System.out.println(wrongRemoteSystemError);
                    }
                } catch(Exception e) {
                    System.out.println(unkownError);
                }
            } else if(command.equals("reboot")) {
                try {
                    String waitingTime = args[1];

                    // Request header containing the waiting time to reboot the remote system
                    String request = "REBOOT " + waitingTime + "\n";
                    headerWriter.write(request, 0, request.length());
                    headerWriter.flush();
                    
                    // Reply header
                    String response = headerReader.readLine();
                    StringTokenizer strk = new StringTokenizer(response, " ");
                    String status = strk.nextToken();

                    if (status.equals("ERROR")){
                        System.out.println(remoteRebootError);
                    }
                    else if (status.equals("OK")) {
                        System.out.println(rebootSuccess + waitingTime + ".");
                    } else {
                        System.out.println(wrongRemoteSystemError);
                    }
                } catch(Exception e) {
                    System.out.println(unkownError);
                }
            } else {
                System.out.println(wrongRemoteSystemError);
            }
		}
	}
}