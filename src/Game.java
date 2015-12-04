import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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


public class Game extends JFrame implements ActionListener, KeyListener {

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

	int screenHeight = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight() / 2;
	int screenWidth = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() / 2;
	private int cordX = 30;
	private int cordY = screenHeight /3;
	private double currentAngle;
    private final int GLOBALSCALE = 5;
    private int velocity = 7;

    int leftCornerX;
    int rightCornerX;
    int topCornerY;
    int bottomCornerY;

    private Timer timer = new Timer(15, this);


    public Game() {

/*
         * Set the dimensions of the window to the size of the inhabitable screen. Basically it the window will take
		 * up as much screen space as is available without covering things.
		 */


        this.setSize(screenWidth, screenHeight);
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
        drawing.setBackground(Color.BLACK);

        this.add(drawing);
        this.setJMenuBar(menuBar);
        this.validate();
        this.setTitle("Game");


    }

    public void updateFields() {
        repaint();
    }

    public BufferedImage loadImage(String fileName) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        addKeyListener(this);
        return image;
    }

    public static void main(String[] args) {
        Game app = new Game();
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.setVisible(true);
    }


    public class GraphPanel extends JPanel {

        private boolean setup;

        public GraphPanel() {
            setup = false;
        }

        int level = 1;
        int currentQuadrant;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int difficulty = 8;
            BufferedImage ship = loadImage("rocket.png");

            leftCornerX = cordX;
            rightCornerX = cordX + ship.getWidth(this);
            topCornerY = cordY;
            bottomCornerY = cordY + ship.getHeight(this);

            if (level == 1) {
                // Top rocks
                int[] topHeight = {100, 50, 65, 100, 30, 20, 110, 80, 50, 100, 80, 40, 30, 75};
                int[] botHeight = {75, 160, 130, 80, 100, 50, 80, 110, 20, 30, 100, 65, 50, 100};

                g.setColor(Color.WHITE);
                for (int i = 0; i < 8; i++) {
                    g.drawLine(i * (getWidth() / difficulty), topHeight[i], (i + 1) * (getWidth() / difficulty), topHeight[i]);
                    g.drawLine((i + 1) * (getWidth() / difficulty), topHeight[i], (i + 1) * (getWidth() / difficulty), topHeight[i + 1]);
                }

                // Bottom rocks
                for (int i = 0; i < difficulty; i++) {
                    if (i == difficulty - 1) {
                        g.setColor(Color.GREEN);
                    }
                    g.drawLine(i * (getWidth() / difficulty), getHeight() - botHeight[i], (i + 1) * (getWidth() / difficulty), getHeight() - botHeight[i]);
                    g.drawLine((i + 1) * (getWidth() / difficulty), getHeight() - botHeight[i], (i + 1) * (getWidth() / difficulty), getHeight() - botHeight[i + 1]);
                }

                // Check if the ship hits one of the rocks.
                for (int i = 0; i < difficulty; i++) {
                    if (leftCornerX >= i * (getWidth() / difficulty) && leftCornerX < (i + 1) * (getWidth() / difficulty)) {
                        currentQuadrant = i;
                        if (topCornerY <= topHeight[i]) {
                            System.out.println("Hit!");
                            g.drawOval(cordX, cordY, 1, 1);
                        }

                        if (bottomCornerY >= getHeight() - botHeight[i]) {
                            System.out.println("Bot Hit!");
                            g.drawOval(cordX, cordY + ship.getHeight(), 1, 1);
                        }
                    }
                    if (rightCornerX >= i * (getWidth() / difficulty) && rightCornerX < (i + 1) * (getWidth() /
                            difficulty)) {
                        if (topCornerY <= topHeight[i]) {
                            System.out.println("Hit!");
                            g.drawOval(cordX + ship.getWidth(), cordY, 1, 1);
                        }

	                    if (bottomCornerY >= getHeight() - botHeight[i]) {
		                    System.out.println("Bot Hit!");
		                    g.drawOval(cordX + ship.getWidth(), cordY + ship.getHeight(), 1, 1);
	                    }
                    }
                }
                
            }



            System.out.println(currentQuadrant);


            Graphics2D g2d = (Graphics2D) g;
            AffineTransform origXform = g2d.getTransform();

            origXform.rotate(Math.toRadians(currentAngle), cordX + ship.getWidth(this) / 2, cordY + ship.getHeight(this) / 2);
            g2d.setTransform(origXform);
            g2d.drawImage(ship, cordX, cordY, this);
            g2d.setColor(Color.RED);
            g2d.drawRect(cordX, cordY, ship.getWidth(this), ship.getHeight(this));





            if (isRotatingRight) {
                //rotateRight 5 degrees at a time
                currentAngle += 3.0;
                if (currentAngle >= 360.0) {
                    currentAngle = 0;
                }
            }

            if (isRotatingLeft) {
                //rotateRight 5 degrees at a time
                currentAngle -= 3.0;
                if (currentAngle <= 0) {
                    currentAngle = 360.0;
                }
            }

            if (isMovingForward) {

                cordX += (int) (velocity * Math.cos(Math.toRadians(currentAngle)));
                cordY += (int) (velocity * Math.sin(Math.toRadians(currentAngle)));
            }


            timer.start();
        }

    }

    public void animation(Graphics g) {

/*        leftCornerX = cordX;
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

*//*        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());*//*

        origXform.rotate(Math.toRadians(currentAngle), cordX + rocket.getWidth(this)/2, cordY + rocket.getHeight(this)/2);
        g2d.setTransform(origXform);
        g2d.drawImage(rocket, cordX, cordY, this);
        g2d.setColor(Color.RED);
        g2d.drawRect(cordX, cordY, rocket.getWidth(this), rocket.getHeight(this));

        g.setColor(Color.YELLOW);



        timer.start();*/
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
//                cordY += 1;
            }
            break;
            case KeyEvent.VK_UP: {
                startMoving();
            }
            break;
        }
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cordY += 10 / GLOBALSCALE;
        if (e.getSource() == resetMenuItem) {

        }
        updateFields();
        repaint();
    }

}



