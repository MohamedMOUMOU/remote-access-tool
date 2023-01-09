import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class Remote {
	public static void main(String[] args) throws Exception {
		try(ServerSocket ss = new ServerSocket(12323)) {
			while(true) {
				System.out.println("Waiting for incoming connections...");
				Socket clientConnection = ss.accept();
				System.out.println("Connection established with the client whose port is " + clientConnection.getPort() + ".");
				try {
					InputStream in = clientConnection.getInputStream();
					OutputStream out = clientConnection.getOutputStream();

					BufferedReader headerReader = new BufferedReader(new InputStreamReader(in));
					BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(out));

					DataOutputStream dataOut = new DataOutputStream(out);

					// retrieving the header from the client and tokenizing it
					String request = headerReader.readLine();
					StringTokenizer strk = new StringTokenizer(request, " ");
					String command = strk.nextToken();

					// Error messages
					String errorMessage = "ERROR\n";
					String clientProtocolMismatch = "The client you are connected to is not using the same protocol.";

					if(command.equals("PROCESSES")) {
						try {
						    String line;
						    String processes = "";
						    Process p = Runtime.getRuntime().exec("ps");
						    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

							// Reading all teh processes them and adding them to the processes string
						    while ((line = input.readLine()) != null) {
						    	processes = processes + line + "\n";
						    }
						    input.close();

							// Success Reply header
							String response = "OK\n";
							headerWriter.write(response, 0, response.length());
							headerWriter.flush();

							// Reply with processes
							headerWriter.write(processes);
							headerWriter.flush();
						} catch (Exception e) {
							headerWriter.write(errorMessage, 0, errorMessage.length());
							headerWriter.flush();
							e.printStackTrace();
						    e.printStackTrace();
						} finally {
							clientConnection.close();
						}
					} else if(command.equals("SCREENSHOT")) {
						try {
							// The screenshot name is the second token in the header
							String screenshotName = strk.nextToken();

							// Taking the screenshot
							Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
							BufferedImage capture = new Robot().createScreenCapture(screenRect);
							ImageIO.write(capture, "bmp", new File("./screenshots/" + screenshotName));

							// Storing the file on the machine (remote system)
							FileInputStream fileIn = new FileInputStream("./screenshots/" + screenshotName);
							int fileSize = fileIn.available();

							// Success reply with the size of the screenshot
							String response = "OK " + fileSize + "\n";
							headerWriter.write(response, 0, response.length());
							headerWriter.flush();

							// Sending the screenshot as bytes
							byte[] bytes = new byte[fileSize];
							fileIn.read(bytes);
							fileIn.close();
							dataOut.write(bytes, 0, fileSize);

						} catch (Exception e) {
							headerWriter.write(errorMessage, 0, errorMessage.length());
							headerWriter.flush();
							e.printStackTrace();
						} finally {
							clientConnection.close();
						}
					} else if(command.equals("REBOOT")) {
						try {
							// The waiting time is the second token in the header
							String waitingTime = strk.nextToken();

							// Invoking the reboot command
							Runtime r = Runtime.getRuntime();
         					r.exec("shutdown -r -t " + waitingTime);

							// success reply header
							String response = "OK\n";
							headerWriter.write(response, 0, response.length());
							headerWriter.flush();
						} catch (Exception e) {
							headerWriter.write(errorMessage, 0, errorMessage.length());
							headerWriter.flush();
							e.printStackTrace();
						} finally {
							clientConnection.close();
						}
					} else {
						System.out.println(clientProtocolMismatch);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
