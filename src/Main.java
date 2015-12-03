import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
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


public class Main extends JFrame implements ActionListener {

    private JPanel panel;
    private JPanel drawing;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem resetMenuItem;

    private BufferedImage rocket;

    private Timer timer = new Timer(2500, this);


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
        drawing.setBackground(Color.BLACK);
        drawing.setSize(getWidth(), getHeight());
        panel.add(drawing);

        panel.setSize(getWidth(), getHeight());



        this.add(panel);
        this.setJMenuBar(menuBar);
        this.validate();

        this.setTitle("Fireworks Pro");


    }

    public void updateFields() {
        repaint();
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == resetMenuItem) {

        }
        updateFields();
    }

    public class GraphPanel extends JPanel {

        private boolean setup = false;
        private Rocket ship;

        public void setupEnvironment(Graphics g) {
            if (!setup) {

                ship = new Rocket(g);

            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            setupEnvironment(g);

            // Offset for padding
            // I'm using a hard value here because I need a minimum of 30px. It should look fine on most screens.
            int offset = 35;



            // Draw some fake stars
            Random rand = new Random();
            for (int i = 0; i < getWidth() / 20; i++) {
                g.setColor(Color.WHITE);
                int max = getWidth() - offset;
                int randomIntX = rand.nextInt((max - offset) + 1) + offset;
                int randomIntY = rand.nextInt(((max - offset) + 1) + offset);
                g.drawLine(randomIntX, randomIntY, randomIntX, randomIntY);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), offset);
                g.setColor(Color.WHITE);
            }
            timer.start();
            System.out.println(g);
            ship.rotate(5);
            ship.rotate(5);



        }

    }
}



