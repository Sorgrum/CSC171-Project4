import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Rocket extends JPanel {

    private BufferedImage rocket;
    Graphics2D g2d;
    // The required drawing location
    int drawLocationX = 300;
    int drawLocationY = 300;

    public Rocket(Graphics graphics) {
        this.g2d = (Graphics2D) graphics;

        try {
            rocket = ImageIO.read(new File("rocket.png"));
        } catch (IOException ex) {
            System.out.println("Image not found");
        }
    }


    public void rotate(int angle) {
        // Rotation information

        double rotationRequired = Math.toRadians(angle);
        double locationX = rocket.getWidth() / 2;
        double locationY = rocket.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        // Drawing the rotated image at the required drawing locations
        g2d.drawImage(op.filter(rocket, null), drawLocationX, drawLocationY, null);

    }
}