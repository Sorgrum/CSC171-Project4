import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class RotateImage extends JFrame implements KeyListener, ActionListener {
    private static final long serialVersionUID = 1L;
    private static Image rocket;

    private static RotateImage ship;
    private BufferedImage bf;
    private BufferedImage nbf;
    private int cordX = 100;
    private int cordY = 100;
    private double currentAngle;
    int screenWidth = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() / 2;
    int screenHeight = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight() / 2;
    private final int GLOBALSCALE = 5;
    private int velocity = 5;
    private Timer timer = new Timer(15, this);
    private boolean isRotatingLeft = false;
    private boolean isRotatingRight = false;
    private boolean isMovingForward = false;

    int leftCornerX;
    int rightCornerX;
    int topCornerY;
    int bottomCornerY;


    public static void main(String[] args) {
        ship = new RotateImage(rocket);
        ship.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public RotateImage(Image rocket) {
        this.rocket = rocket;
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(rocket, 0);
        try {
            mt.waitForID(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Testing....");
        setSize(screenWidth, screenHeight);
        GraphPanel panel = new GraphPanel();
        panel.setBackground(Color.BLUE);
        add(panel);
        imageLoader();
        setVisible(true);
    }

    public class GraphPanel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.CYAN);
            g.drawLine(0,0, getWidth(), getHeight());

//            g.drawImage(rocket, 30, 30, null);
            nbf = new BufferedImage( this.getWidth(),this.getHeight(), BufferedImage.TYPE_INT_RGB);
            nbfAnimation(nbf.getGraphics());
            g.drawImage(nbf, 30, 30 ,null);
            timer.start();
        }

    }
    public void imageLoader() {
        try {
            String testPath = "test.png";
            rocket = ImageIO.read(new File("rocket.png"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        addKeyListener(this);
    }

    public void paint(Graphics g){

        bf = new BufferedImage( this.getWidth(),this.getHeight(), BufferedImage.TYPE_INT_RGB);


        animation(bf.getGraphics());
        g.drawImage(bf,0,0,null);

    }

    public void nbfAnimation(Graphics g) {

        leftCornerX = cordX;
        rightCornerX = cordX + rocket.getWidth(this);
        topCornerY = cordY;
        bottomCornerY = cordY + rocket.getHeight(this);

        if (leftCornerX <= 0) {
            cordX = 0;
        } else if (topCornerY <= 0) {
            cordY = 0;
        } else if (rightCornerX >= getWidth()) {
            cordX = getWidth() - rocket.getWidth(this);
        } else if (topCornerY >= getHeight()) {
            cordY = getHeight();
        }

        if (isRotatingRight) {
            //rotateRight 5 degrees at a time
            currentAngle += 5.0;
            if (currentAngle >= 360.0) {
                currentAngle = 0;
            }
        }

        if (isRotatingLeft) {
            //rotateRight 5 degrees at a time
            currentAngle -= 5.0;
            if (currentAngle <= 0) {
                currentAngle = 360.0;
            }
        }

        if (isMovingForward) {

            cordX += (int) (velocity * Math.cos(Math.toRadians(currentAngle)));
            cordY += (int) (velocity * Math.sin(Math.toRadians(currentAngle)));
        }

        if (cordY + (rocket.getHeight(this)) >= screenHeight) {
            cordY = screenHeight - rocket.getHeight(this);
        }


        Graphics2D g2d = (Graphics2D)g;
        AffineTransform origXform = g2d.getTransform();

/*        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());*/

        origXform.rotate(Math.toRadians(currentAngle), cordX + rocket.getWidth(this)/2, cordY + rocket.getHeight(this)/2);
        g2d.setTransform(origXform);
        g2d.drawImage(rocket, cordX, cordY, this);
        g2d.setColor(Color.RED);
        g2d.drawRect(cordX, cordY, rocket.getWidth(this), rocket.getHeight(this));

        g.setColor(Color.YELLOW);
    }
    public void animation(Graphics g) {

        leftCornerX = cordX;
        rightCornerX = cordX + rocket.getWidth(this);
        topCornerY = cordY;
        bottomCornerY = cordY + rocket.getHeight(this);

        if (leftCornerX <= 0) {
            cordX = 0;
        } else if (topCornerY <= 0) {
            cordY = 0;
        } else if (rightCornerX >= getWidth()) {
            cordX = getWidth() - rocket.getWidth(this);
        } else if (topCornerY >= getHeight()) {
            cordY = getHeight();
        }

        if (isRotatingRight) {
            //rotateRight 5 degrees at a time
            currentAngle += 5.0;
            if (currentAngle >= 360.0) {
                currentAngle = 0;
            }
        }

        if (isRotatingLeft) {
            //rotateRight 5 degrees at a time
            currentAngle -= 5.0;
            if (currentAngle <= 0) {
                currentAngle = 360.0;
            }
        }

        if (isMovingForward) {

            cordX += (int) (velocity * Math.cos(Math.toRadians(currentAngle)));
            cordY += (int) (velocity * Math.sin(Math.toRadians(currentAngle)));
        }

        if (cordY + (rocket.getHeight(this)) >= screenHeight) {
            cordY = screenHeight - rocket.getHeight(this);
        }


        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform origXform = g2d.getTransform();

/*        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());*/

        origXform.rotate(Math.toRadians(currentAngle), cordX + rocket.getWidth(this)/2, cordY + rocket.getHeight(this)/2);
        g2d.setTransform(origXform);
        g2d.drawImage(rocket, cordX, cordY, this);
        g2d.setColor(Color.RED);
        g2d.drawRect(cordX, cordY, rocket.getWidth(this), rocket.getHeight(this));

        g.setColor(Color.YELLOW);

        timer.start();
    }























    public void startRotatingRight() {
        isRotatingRight = true;
    }

    public void startRotatingLeft() {
        isRotatingLeft = true;
    }

    public void stopRotatingRight() {
        isRotatingRight = false;
    }

    public void stopRotatingLeft() {
        isRotatingLeft = false;
    }

    public void startMoving() {
        isMovingForward = true;
    }

    public void stopMoving() {
        isMovingForward = false;
    }

    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_RIGHT: {
//                ship.rotateRight();
                ship.startRotatingRight();
            }
            break;
            case KeyEvent.VK_LEFT: {
//                ship.rotateLeft();
                ship.startRotatingLeft();
            }
            break;
            case KeyEvent.VK_DOWN: {
//                cordY += 5;
            }
            break;
            case KeyEvent.VK_UP: {
                ship.startMoving();
            }
            break;
        }
        repaint();
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyReleased(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_RIGHT: {
                ship.stopRotatingRight();
//                ship.rotateRight();
            }
            break;
            case KeyEvent.VK_LEFT: {
//                ship.rotateLeft();
                ship.stopRotatingLeft();
            }
            break;
            case KeyEvent.VK_DOWN: {
//                cordY += 5;
            }
            break;
            case KeyEvent.VK_UP: {
//                cordY -= 5;
                stopMoving();
            }
            break;
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        cordY += 10/GLOBALSCALE;
    }
}