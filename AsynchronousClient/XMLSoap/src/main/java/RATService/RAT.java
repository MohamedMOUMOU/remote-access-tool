package RATService;
import javax.jws.WebService;
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;


@WebService
public class RAT {

    public String getProcesses() {
        String line;
        String processes = "";
        try {
            Process p = Runtime.getRuntime().exec("ps");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // Reading all teh processes them and adding them to the processes string
            while ((line = input.readLine()) != null) {
                processes = processes + line + "\n";
            }
            input.close();
        }catch (Exception e){
            return "An error occured while reading the processes of the remote system";
        }
        return processes;
    }

    public byte[] takeScreenshot(String screenshotName) {
        // Taking the screenshot
        try {
            String pathToImage = "./screenshots/" + screenshotName + ".jpg";
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            ImageIO.write(capture, "jpg", new File(pathToImage));
            FileInputStream fileIn = new FileInputStream(pathToImage);
	    int fileSize = fileIn.available();
            byte[] bytes = new byte[fileSize];
	    fileIn.read(bytes);
	    fileIn.close();
	    return bytes;
        } catch (Exception e) {
        }
        byte[] empty = {};
        return empty;
    }

    public String reboot(String waitingTime) {
        try {
            // Invoking the reboot command
            Runtime r = Runtime.getRuntime();
            r.exec("shutdown -r -t " + waitingTime);
        } catch(Exception e) {
            return "An error occured while executing the reboot command";
        }
        return "The remote system will reboot after " + waitingTime + " seconds";
    }
}
