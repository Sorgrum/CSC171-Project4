import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Random;

/**
 * Student Name: Marcelo Gheiler
 * Filename: Fireworks
 * Date: 10/28/15
 * TA Name: Colin Pronovost
 * Assignment: 12
 * Lab Day: Monday
 * Lab Time: 5PM
 * Lab Location: CSB 703
 * I affirm that I have not given or received any unauthorized help on this assignment, and that this work is my own
 */


public class Main extends JFrame implements ActionListener, KeyListener {

    private JPanel panel;
    private JPanel drawing;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem resetMenuItem;

    private static Image rocket;
    private BufferedImage bf;

    private boolean isRotatingLeft = false;
    private boolean isRotatingRight = false;
    private boolean isMovingForward = false;

    private int cordX = 100;
    private int cordY = 100;
    private double currentAngle;
    int screenWidth = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() / 2;
    int screenHeight = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight() / 2;
    private final int GLOBALSCALE = 5;
    private int velocity = 7;

    int leftCornerX;
    int rightCornerX;
    int topCornerY;
    int bottomCornerY;

    private Timer timer = new Timer(15, this);


    public Main() {

/*
		 * Set the dimensions of the window to the size of the inhabitable screen. Basically it the window will take
		 * up as much screen space as is available without covering things.
		 */

        double screenWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth();
        double screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight();

        this.setSize((int) screenWidth, (int) screenHeight);
        this.setMinimumSize(new Dimension(610, 350));


        panel = new JPanel();
        panel.setLayout(new BorderLayout());

/*
		 * First, create the menu
		 */

        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menuBar.add(menu);

        resetMenuItem = new JMenuItem("Reset");
        resetMenuItem.addActionListener(this);
        menu.add(resetMenuItem);


        drawing = new GraphPanel();
        drawing.setSize(getWidth(), getHeight());
        panel.add(drawing);

        panel.setSize(getWidth(), getHeight());
        imageLoader();
        this.add(panel);
        this.setJMenuBar(menuBar);
        this.validate();
        this.setTitle("Fireworks Pro");


    }

    public void updateFields() {
        repaint();
    }

    public void imageLoader() {
        try {
            rocket = ImageIO.read(new File("rocket.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        addKeyListener(this);
    }
    public static void main(String[] args) {
        Main app = new Main();
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.setVisible(true);
    }



    public class GraphPanel extends JPanel {

        private boolean setup = false;
        private Rocket ship;

        public void setupEnvironment(Graphics g) {
            if (!setup) {
                g.setColor(Color.CYAN);
                g.fillRect(0, 0, getWidth(), getHeight());
                Random rand = new Random();
                HashMap<Integer, Integer> topBlocks = new HashMap<>();


                // Offset for padding
                // I'm using a hard value here because I need a minimum of 30px. It should look fine on most screens.
                int offset = 35;



                // Draw some fake stars
                for (int i = 0; i < getWidth() / 20; i++) {
                    g.setColor(Color.WHITE);
                    int max = getWidth() - offset;
                    int randomIntX = rand.nextInt(max - offset) + 1;
                    int randomIntY = rand.nextInt((max - offset) + 1);
                    g.drawLine(randomIntX, randomIntY, randomIntX, randomIntY);
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), offset);
                    g.setColor(Color.WHITE);
                }
                for (int i = 0; i < getWidth() / 100; i++) {
                    g.setColor(Color.WHITE);
                    int randomIntY = rand.nextInt(getHeight()) + 1;
                    topBlocks.put(i, randomIntY);
                }


                System.out.println(topBlocks);
                setup = true;
            }

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);





            bf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);


            animation(bf.getGraphics());
            g.drawImage(bf,0,0,null);

            setupEnvironment(g);

        }

    }

    public void animation(Graphics g) {

        leftCornerX = cordX;
        rightCornerX = cordX + rocket.getWidth(this);
        topCornerY = cordY;
        bottomCornerY = cordY + rocket.getHeight(this);

        if (leftCornerX <= 0) {
            cordX = 1;
        } else if (topCornerY <= 0) {
            cordY = 1;
        } else if (rightCornerX >= getWidth()) {
            cordX = getWidth() - rocket.getWidth(this);
        } else if (topCornerY >= getHeight()) {
            cordY = getHeight() - rocket.getHeight(this);
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
                startRotatingRight();
            }
            break;
            case KeyEvent.VK_LEFT: {
//                ship.rotateLeft();
                startRotatingLeft();
            }
            break;
            case KeyEvent.VK_DOWN: {
//                cordY += 5;
            }
            break;
            case KeyEvent.VK_UP: {
                startMoving();
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
                stopRotatingRight();
//                ship.rotateRight();
            }
            break;
            case KeyEvent.VK_LEFT: {
//                ship.rotateLeft();
                stopRotatingLeft();
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
        if (e.getSource() == resetMenuItem) {

        }
        updateFields();
    }

}



