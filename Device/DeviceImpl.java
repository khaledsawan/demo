package Device;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Base64;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

public class DeviceImpl extends UnicastRemoteObject implements Device {

    public DeviceImpl() throws RemoteException {
        super();
        //  this.deviceAddress = deviceAddress;
    }
    

    public String captureScreenshot() throws AWTException, IOException {
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenshot = robot.createScreenCapture(screenRect);     
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenshot, "png", baos);
        byte[] screenshotBytes = baos.toByteArray();
        String screenshotBase64 = Base64.getEncoder().encodeToString(screenshotBytes);
        return  screenshotBase64;
    }

    public String captureImage() throws IOException {
        VideoCapture capture = new VideoCapture(0);
        Mat frame = new Mat();
        if (capture.isOpened()) {
            capture.read(frame); 
            capture.release();
        }
        BufferedImage bufferedImage =  matToBufferedImage(frame);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] screenshotBytes = baos.toByteArray();
        String screenshotBase64 = Base64.getEncoder().encodeToString(screenshotBytes);
        return  screenshotBase64;
    }

    @Override
    public void startChat() throws RemoteException {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 8080);
                System.out.println("Manager Wants To Chat you !!");
                OutputStream outputStream = socket.getOutputStream();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(outputStream, true);

                new Thread(() -> {
                    String inputLine;
                    try {
                        while ((inputLine = reader.readLine()) != null) {
                            System.out.println("Server: " + inputLine);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                String clientInput;
                while ((clientInput = consoleReader.readLine()) != null) {
                    writer.println(clientInput);
                }

                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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


    // public String getDeviceAddress() throws RemoteException {
    //     return deviceAddress;
    // }


}
