import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
    private boolean isRotatingLeft2 = false;
    private boolean isRotatingRight2 = false;
    private boolean isMovingForward2 = false;

    private int screenHeight;
    //    int screenHeight = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight() / 2;
//    int screenWidth = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() / 2;
    private int screenWidth;
    private int originX = 30;
    private int originY;
    private int originX2 = 30;
    private int originY2;
    private int cordX = originX;
    private int cordY = originY;
    private int cordX2 = originX;
    private int cordY2 = originY;
    private double currentAngle;
    private  double currentAngle2;
    private final int GLOBALSCALE = 5;
    private int velocity = 7;
    int level = 1;
    private int timeElapsed = 0;
    private boolean startedGame;

    private boolean singleplayer = true;
    private Color player1Color = new Color(227, 41, 41);
    private Color player2Color = new Color(3, 169, 244);
    BufferedImage ship;
    BufferedImage ship1;
    BufferedImage ship2;
    // The user will actually have 3 lives because when the game starts the ship is initially out of bounds
    private int lives = 4;
    private int lives2 = 4;
    ArrayList<Integer> topHeight = new ArrayList<>();
    ArrayList<Integer> botHeight = new ArrayList<>();

    int leftCornerX;
    int rightCornerX;
    int topCornerY;
    int bottomCornerY;

    int leftCornerX2;
    int rightCornerX2;
    int topCornerY2;
    int bottomCornerY2;

    private Timer graphicsTimer = new Timer(15, this);
    private Timer secondsTimer = new Timer(1000, this);


    public Game() {

        screenHeight = (int) getScreenBounds(this).getHeight();
        screenWidth = (int) getScreenBounds(this).getWidth();

        startedGame = false;

/*
         * Set the dimensions of the window to the size of the inhabitable screen. Basically it the window will take
		 * up as much screen space as is available without covering things.
		 */


        this.setSize(screenWidth, screenHeight);
        this.setMinimumSize(new Dimension(610, 350));


        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.requestFocusInWindow();
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
        this.addKeyListener(this);

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

        int currentQuadrant;

        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);


            if (startedGame) {
                if (singleplayer) {
                    ship = loadImage("rocket.png");
                } else {
                    ship = loadImage("rocket1.png");
                    ship2 = loadImage("rocket2.png");
                }

                int difficulty = getWidth() / (2 * ship.getWidth());

                leftCornerX = cordX;
                rightCornerX = cordX + ship.getWidth(this);
                topCornerY = cordY;
                bottomCornerY = cordY + ship.getHeight(this);

                if (!singleplayer) {
                    leftCornerX2 = cordX;
                    rightCornerX2 = cordX + ship.getWidth(this);
                    topCornerY2 = cordY;
                    bottomCornerY2 = cordY + ship.getHeight(this);
                }

                int timeRemaining = (getWidth() / 100) + 3;

                if (lives > 0) {


                    // Top rocks
                    char[] livesMessage = ("Lives Remaining: " + lives).toCharArray();
                    char[] levelMessage = ("Level: " + level).toCharArray();
                    char[] livesMessage2 = ("Lives Remaining: " + lives2).toCharArray();

                    ArrayList<Character> timeRemainingText = new ArrayList<>();


                    for (int i = 0; i < timeRemaining - timeElapsed; i++) {
                        timeRemainingText.add('•');
                    }

                    char[] timeRemainingCharArray = new char[timeRemainingText.size()];
                    for (int i = 0; i < timeRemainingCharArray.length; i++) {
                        timeRemainingCharArray[i] = timeRemainingText.get(i);
                    }

                    if (!singleplayer) {
                        g.setColor(player1Color);
                    }
                    g.drawChars(livesMessage, 0, livesMessage.length, 5, 15);
                    if (!singleplayer) {
                        g.setColor(player2Color);
                        g.drawChars(livesMessage2, 0, livesMessage2.length, 5, 30);
                        g.setColor(Color.WHITE);
                        g.drawChars(levelMessage, 0, levelMessage.length, 5, 45);
                    } else {
                        g.drawChars(levelMessage, 0, levelMessage.length, 5, 30);
                    }


                    g.setColor(Color.WHITE);
                    g.drawChars(timeRemainingCharArray, 0, timeRemainingText.size(), 150, 15);


                    while (topHeight.size() <= difficulty) {
                        int min = (level - 1) * 50;
                        int max = level * getHeight() / 10;

                        if (max > (getHeight() / 2) - ship.getHeight()) {
                            max = (getHeight() / 2) - ship.getHeight();
                        }
                        if (min >= max) {
                            min = max - 70;
                        }

                        if (topHeight.size() == 0 && level == 1) {
                            min = 60;
                            g.setColor(Color.CYAN);
                        } else if (level == 1) {
                            min = 20;
                        }
                        topHeight.add(ThreadLocalRandom.current().nextInt(min, max + 1));
                    }
                    while (botHeight.size() <= difficulty) {
                        int min = (level - 1) * 50;
                        int max = (level * getHeight() / 10);

                        if (max > (getHeight() / 2) - ship.getHeight()) {
                            max = (getHeight() / 2) - ship.getHeight();
                        }
                        if (min >= max) {
                            min = max - 70;
                        }
                        botHeight.add(ThreadLocalRandom.current().nextInt(min, max + 1));
                    }

                    originY = getHeight() / 2;
                    originY2 = originY + ship.getHeight();

                    // Draw the top rocks
                    for (int i = 0; i < difficulty; i++) {
                        g.drawLine(i * (getWidth() / difficulty), topHeight.get(i), (i + 1) * (getWidth() / difficulty), topHeight.get(i));
                        g.drawLine((i + 1) * (getWidth() / difficulty), topHeight.get(i), (i + 1) * (getWidth() / difficulty), topHeight.get(i + 1));
                    }

                    // Bottom rocks
                    for (int i = 0; i < difficulty; i++) {
                        if (i >= difficulty - 1) {
                            g.setColor(Color.GREEN);
                        }

                        g.drawLine(i * (getWidth() / difficulty), getHeight() - botHeight.get(i), (i + 1) * (getWidth() / difficulty), getHeight() - botHeight.get(i));
                        g.drawLine((i + 1) * (getWidth() / difficulty), getHeight() - botHeight.get(i), (i + 1) * (getWidth() / difficulty), getHeight() - botHeight.get(i + 1));

                    }

                    // Check if the ship hits one of the rocks.
                    for (int i = 0; i < difficulty; i++) {
                        if (leftCornerX >= i * (getWidth() / difficulty) && leftCornerX < (i + 1) * (getWidth() / difficulty)) {
                            currentQuadrant = i;
                            if (topCornerY <= topHeight.get(i)) {
//                            System.out.println("Hit!");
                                lives -= 1;

                                // I want the ship to always start facing upwards
                                currentAngle = 270;

                                // Send the user back to the beginning of the level
                                cordX = 30;
                                cordY = originY;

                                timeElapsed = 0;
                                g.drawOval(cordX, cordY, 1, 1);
                            }
                            if (!singleplayer && topCornerY2 <= topHeight.get(i)) {
                                lives2 -= 1;

                                // I want the ship to always start facing upwards
                                currentAngle = 270;

                                // Send the user back to the beginning of the level
                                cordX = 30;
                                cordY = originY;

                                timeElapsed = 0;
                                g.drawOval(cordX, cordY, 1, 1);
                            }

                            if (bottomCornerY >= getHeight() - botHeight.get(i)) {
                                lives -= 1;

                                // I want the ship to always start facing upwards
                                currentAngle = 270;

                                // Send the user back to the beginning of the level
                                cordX = 30;
                                cordY = originY;

                                timeElapsed = 0;
                                g.drawOval(cordX, cordY + ship.getHeight(), 1, 1);
                            }
                        }
                        if (rightCornerX >= i * (getWidth() / difficulty) && rightCornerX < (i + 1) * (getWidth() /
                                difficulty)) {
                            if (topCornerY <= topHeight.get(i)) {

                                g.drawOval(cordX + ship.getWidth(), cordY, 1, 1);
                            }

                            if (bottomCornerY >= getHeight() - botHeight.get(i)) {
                                g.drawOval(cordX + ship.getWidth(), cordY + ship.getHeight(), 1, 1);
                            }
                        }
                    }

                    // If the player lands on the platform
                    if (leftCornerX >= (difficulty - 1) * (getWidth() / difficulty)) {
                        if (currentAngle >= 180) {
                            if (bottomCornerY >= getHeight() - botHeight.get(difficulty - 1)) {
                                level += 1;
                                currentAngle = 270;
                                cordX = originX;
                                cordY = originY;
                                topHeight.clear();
                                botHeight.clear();
                                timeElapsed = 0;
                                g.drawOval(cordX + ship.getWidth(), cordY + ship.getHeight(), 1, 1);
                            }
                        }
                    }

                    // If the player ran out of time
                    if (timeRemaining == timeElapsed) {
                        currentAngle = 270;
                        lives -= 1;
                        cordX = originX;
                        cordY = originY;
                        topHeight.clear();
                        botHeight.clear();
                        timeElapsed = 0;
                    }
                }


                // Make sure the ship doesn't leave the area
                if (cordX < 0) {
                    cordX = 0;
                }

                if (cordX + ship.getWidth() > getWidth()) {
                    cordX = getWidth() - ship.getWidth();
                }

                if (cordY < 0) {
                    cordY = 0;
                }

                if (cordY + ship.getHeight() > getHeight()) {
                    cordY = getHeight() - ship.getHeight();
                }

                if (!singleplayer) {
                    if (cordX2 < 0) {
                        cordX2 = 0;
                    }

                    if (cordX2 + ship2.getWidth() > getWidth()) {
                        cordX2 = getWidth() - ship2.getWidth();
                    }

                    if (cordY2 < 0) {
                        cordY2 = 0;
                    }

                    if (cordY2 + ship2.getHeight() > getHeight()) {
                        cordY2 = getHeight() - ship2.getHeight();
                    }
                }


                // Rotate the ship
                Graphics2D g2d = (Graphics2D) g;
                AffineTransform origXform = g2d.getTransform();

                origXform.rotate(Math.toRadians(currentAngle), cordX + ship.getWidth(this) / 2, cordY + ship.getHeight(this) / 2);
                g2d.setTransform(origXform);
                g2d.drawImage(ship, cordX, cordY, this);

                if (!singleplayer) {
                    Graphics2D g2d2 = (Graphics2D) g;
                    AffineTransform origXform2 = g2d2.getTransform();

                    origXform2.rotate(Math.toRadians(currentAngle2), cordX2 + ship2.getWidth(this) / 2, cordY2 + ship2.getHeight(this) / 2);
                    g2d2.setTransform(origXform2);
                    g2d2.drawImage(ship2, cordX2, cordY2, this);
                }

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

                if (!singleplayer) {
                    if (isRotatingRight2) {
                        //rotateRight 5 degrees at a time
                        currentAngle2 += 3.0;
                        if (currentAngle2 >= 360.0) {
                            currentAngle2 = 0;
                        }
                    }

                    if (isRotatingLeft2) {
                        //rotateRight 5 degrees at a time
                        currentAngle2 -= 3.0;
                        if (currentAngle2 <= 0) {
                            currentAngle2 = 360.0;
                        }
                    }

                    if (isMovingForward2) {

                        cordX2 += (int) (velocity * Math.cos(Math.toRadians(currentAngle2)));
                        cordY2 += (int) (velocity * Math.sin(Math.toRadians(currentAngle2)));
                    }
                }

                secondsTimer.start();
                graphicsTimer.start();

            } else {
                String text = "Drone Pilot";
                FontMetrics fm = g.getFontMetrics();
                int totalWidth = (fm.stringWidth(text) * 2) + 4;

                // Baseline
                int x = (getWidth() - totalWidth) / 2;
                int y = (getHeight() - fm.getHeight()) / 2;
                g.setColor(Color.WHITE);
                g.setFont(g.getFont().deriveFont(g.getFont().getSize() * 1.8F));
                g.drawString(text, x, y + ((fm.getDescent() + fm.getAscent()) / 2));

                text = "Press 1 for single player. Press 2 for duo-play.";
                totalWidth = (fm.stringWidth(text) * 2) + 4;
                x = (getWidth() - totalWidth) / 2;
                y = (getHeight() - fm.getHeight()) / 2;
                y += 1.5 * fm.getHeight();

                g.drawString(text, x, y + ((fm.getDescent() + fm.getAscent()) / 2));
            }
        }
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

    public void startRotatingRight2() {
        isRotatingRight2 = true;
    }

    public void startRotatingLeft2() {
        isRotatingLeft2 = true;
    }

    public void stopRotatingRight2() {
        isRotatingRight2 = false;
    }

    public void stopRotatingLeft2() {
        isRotatingLeft2 = false;
    }

    public void startMoving2() {
        isMovingForward2 = true;
    }

    public void stopMoving2() {
        isMovingForward2 = false;
    }


    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_1: {
                startedGame = true;
                singleplayer = true;
                repaint();
            }
            break;
            case KeyEvent.VK_2: {
                startedGame = true;
                singleplayer = false;
                repaint();
            }
            break;
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
            case KeyEvent.VK_D: {
                startRotatingRight2();
            }
            break;
            case KeyEvent.VK_A: {
                startRotatingLeft2();
            }
            break;
            case KeyEvent.VK_S: {

            }
            break;
            case KeyEvent.VK_W: {
                startMoving2();
            }
            break;
        }
    }

    public void keyTyped(KeyEvent ke) {

    }

    public void keyReleased(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_1: {
                startedGame = true;
            }
            break;
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
            case KeyEvent.VK_D: {
                stopRotatingRight2();
            }
            break;
            case KeyEvent.VK_A: {
                stopRotatingLeft2();
            }
            break;
            case KeyEvent.VK_S: {

            }
            break;
            case KeyEvent.VK_W: {
                stopMoving2();
            }
            break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == graphicsTimer) {
            cordY += 10 / GLOBALSCALE;
        }

        if (e.getSource() == secondsTimer) {
            timeElapsed += 1;
            System.out.println(timeElapsed);
        }

        if (e.getSource() == resetMenuItem) {
            cordX = originX;
            cordY = originY;
            lives = 3;
            level = 1;
            topHeight.clear();
            botHeight.clear();
        }
        updateFields();
        repaint();
    }

    // Methods getScreenInsets and getScreenBounds taken from: http://stackoverflow.com/questions/1936566/how-do-you-get-the-screen-width-in-java
    static public Rectangle getScreenBounds(Window wnd) {
        Rectangle sb;
        Insets si = getScreenInsets(wnd);

        if (wnd == null) {
            sb = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration()
                    .getBounds();
        } else {
            sb = wnd
                    .getGraphicsConfiguration()
                    .getBounds();
        }

        sb.x += si.left;
        sb.y += si.top;
        sb.width -= si.left + si.right;
        sb.height -= si.top + si.bottom;
        return sb;
    }

    static public Insets getScreenInsets(Window wnd) {
        Insets si;

        if (wnd == null) {
            si = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration());
        } else {
            si = wnd.getToolkit().getScreenInsets(wnd.getGraphicsConfiguration());
        }
        return si;
    }
}



