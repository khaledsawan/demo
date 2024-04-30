package server;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SaveImageExample {
    public static void main(String[] args) {
        try {
            // Capture a screenshot
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenshot = robot.createScreenCapture(screenRect);

            // Specify the folder to save the image
            String folderPath = "C:/Users/hornet/screenshots"; // Folder where images will be saved
            File folder = new File(folderPath);

            // Create the folder if it doesn't exist
            if (!folder.exists()) {
                folder.mkdirs(); // Create all necessary parent directories
            }

            // Specify the file name and path
            String fileName = "screenshot_" + System.currentTimeMillis() + ".jpg"; // Use a timestamp to ensure unique names
            File outputFile = new File(folder, fileName);

            // Save the image to the specified folder
            ImageIO.write(screenshot, "jpg", outputFile);

            System.out.println("Screenshot saved to: " + outputFile.getAbsolutePath());

        } catch (AWTException | IOException e) {
            e.printStackTrace(); // Handle any exceptions
        }
    }
}
