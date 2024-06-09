package classes;

import java.awt.Button;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.datatransfer.StringSelection;

// design & add listeners
class normal_cal implements ActionListener {

    private int x, y; // for draging

    Label displayLabel1, displayLabel2, bl1, bl2, bl3, bl4, bl5, bl6, bl7, bl8, bl9, bl10, bl14, bl15, bl19, bl20, bl24, bl25, bl29, bl30;
    Button b11, b12, b13, b16, b17, b18, b21, b22, b23, b26, b27, b28;

    int minimizeIconX = 425;
    int minimizeIconY = 5;
    int minimizeIconW = 20;
    int minimizeIconH = 25;

    int plusIconX = 395;
    int plusIconY = 10;
    int plusIconW = 18;
    int plusIconH = 18;

    int closeIconX = 455;
    int closeIconY = 8;
    int closeIconW = 22;
    int closeIconH = 22;

    String op = null; // +,- so on...
    boolean bracket = false; // ()
    boolean started = false; // entered number
    double caculated_value = 0;
    double first_value = 0;
    double second_value = 0;

    double number1 = 0;
    double number2 = 0;
    double value = 0;
    String operator = "";

    List<Double> numbersWithinBrackets = new ArrayList<>();
    List<String> operatorsWithinBrackets = new ArrayList<>();

    List<Double> numbers = new ArrayList<>();
    List<String> operators = new ArrayList<>();

    Double[] numbersWithinBracketsArray = numbers.toArray(new Double[0]);
    String[] operatorsWithinBracketsArray = operators.toArray(new String[0]);

    Double[] numbersArray = numbers.toArray(new Double[0]);
    String[] operatorsArray = operators.toArray(new String[0]);

    double memory = 0;
    boolean memoryStatus = false;

    Frame frame;

    normal_cal() {

        /* frame */
        frame = new Frame();
        frame.setSize(480, 420);// center frame
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true); // remove default design

        /* gradient panel */
        Panel mainGradientPanel = new Panel() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                int w = getWidth();
                int h = getHeight();

                // Define the start and end points of the gradient
                Point startPoint = new Point(0, h / 2);
                Point endPoint = new Point(w, h / 2);

                // Create the gradient paint
                GradientPaint gp = new GradientPaint(startPoint, Color.BLUE, endPoint, Color.CYAN);

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainGradientPanel.setSize(480, 420);

        // white panel        
        Panel mainWhitePanel = new Panel();
        mainWhitePanel.setBackground(Color.white);
        mainWhitePanel.setBounds(7, 0, mainGradientPanel.getWidth() - 14, mainGradientPanel.getHeight() - 7);

        // control panel
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/logo.png"));
        Image minimuzeIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/minus.png"));
        Image plus = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/plus.png"));
        Image closeIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/close.png"));

        Panel controls = new Panel() {
            public void paint(Graphics g) {

                Graphics2D g2d = (Graphics2D) g;

                g2d.drawImage(icon, 0, -10, 50, 60, this);
                g2d.drawImage(minimuzeIcon, minimizeIconX, minimizeIconY, minimizeIconW, minimizeIconH, this);
                g2d.drawImage(plus, plusIconX, plusIconY, plusIconW, plusIconH, this);
                g2d.drawImage(closeIcon, closeIconX, closeIconY, closeIconW, closeIconH, this);

            }
        };
        controls.setSize(mainGradientPanel.getWidth(), 35);
        controls.setBackground(Color.black);

        // add mouse listener to the panel
        controls.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (e.getX() >= minimizeIconX && e.getX() < minimizeIconX + 15 && e.getY() >= 5 && e.getY() < 30) {
                    // minimize image clicked
                    frame.setState(Frame.ICONIFIED);

                } else if (e.getX() >= closeIconX && e.getX() < closeIconX + 15 && e.getY() >= 8 && e.getY() < 30) {
                    // close image clicked
                    frame.dispose();
                    if (GlobalVariable.claCount > 0) {
                        GlobalVariable.claCount = GlobalVariable.claCount - 1;
                    }

                    if (GlobalVariable.claCount == 0) {
                        System.exit(0);
                    }

                } else if (e.getX() >= plusIconX && e.getX() < plusIconX + 15 && e.getY() >= 8 && e.getY() < 30) {
                    // new image clicked
                    GlobalVariable.claCount = GlobalVariable.claCount + 1;
                    new normal_cal();
                }
            }

            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

        });

        // drag frame
        controls.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point point = frame.getLocation();// current location before move
                frame.setLocation(point.x + e.getX() - x, point.y + e.getY() - y);
            }
        });

        // menu
        Panel menuBarPanel1 = new Panel();
        menuBarPanel1.setBounds(mainWhitePanel.getX(), 40, 100, 30);

        // option btn popup
        PopupMenu optionPopup = new PopupMenu();
        CheckboxMenuItem option1 = new CheckboxMenuItem("Operators");
        option1.setState(true);// chacked or not
        optionPopup.add(option1);

        CheckboxMenuItem option2 = new CheckboxMenuItem("Advanced Operators");
        option2.setState(true);
        optionPopup.add(option2);

        Button option = new Button("Option");
        option.addActionListener((e) -> {
            optionPopup.show(option, option.getX(), option.getY() + option.getHeight());
        });

        // edit btn popup
        PopupMenu editPop = new PopupMenu();
        MenuItem copy = new MenuItem("Copy");
        MenuItem cut = new MenuItem("Cut");
        editPop.add(copy);
        editPop.add(cut);

        Button edit = new Button("Edit");
        edit.addActionListener((e) -> {
            editPop.show(edit, option.getX(), option.getY() + edit.getHeight());
        });

        // help
        Panel menuBarPanel2 = new Panel();
        menuBarPanel2.setBounds(422, 40, 50, 30);

        // help btn popup
        PopupMenu helpPop = new PopupMenu();
        MenuItem viewHelp = new MenuItem("View Help");
        MenuItem aboutCal = new MenuItem("About Calculator");

        helpPop.add(viewHelp);
        helpPop.add(aboutCal);

        Image helpIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/help.png"));
        Label help = new Label() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.drawImage(helpIcon, 0, 0, 23, 23, this);

            }
        };
        Dimension size = new Dimension(28, 28); // Increase btn size
        help.setPreferredSize(size);
        help.setBackground(menuBarPanel2.getBackground());

        help.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {

                helpPop.show(help, option.getX() - 130, option.getY() + option.getHeight());

            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

        menuBarPanel1.add(option);
        menuBarPanel1.add(edit);
        menuBarPanel2.add(help);

        // display
        Panel display = new Panel();
        GridLayout displayLayout = new GridLayout(2, 1, 0, 0);
        display.setLayout(displayLayout);

        display.setBounds(mainWhitePanel.getX() + 10, menuBarPanel1.getY() + 50, mainWhitePanel.getWidth() - 20, 80);
        display.setBackground(new Color(0xC5EAFF));

        // first display label
        displayLabel1 = new Label();
        displayLabel1.setFont(new Font("MONOSPACED", Font.ITALIC, 15));
        displayLabel1.setAlignment(Label.RIGHT);
        display.add(displayLabel1);

        // second display label        
        displayLabel2 = new Label("00");
        displayLabel2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        displayLabel2.setAlignment(Label.RIGHT);
        display.add(displayLabel2);

        // keyboard
        // first line
        Panel keyboard1 = new Panel();
        keyboard1.setBounds(mainWhitePanel.getX(), display.getY() + display.getHeight() + 20, mainWhitePanel.getWidth(), 30);
        GridLayout keyboardLayout1 = new GridLayout(1, 5, 3, 3);
        keyboard1.setLayout(keyboardLayout1);

        bl1 = new Label("MC");
        bl1.setBackground(Color.black);
        bl1.setForeground(Color.white);
        bl1.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl1.setAlignment(Label.CENTER);

        bl2 = new Label("MR");
        bl2.setBackground(Color.black);
        bl2.setForeground(Color.white);
        bl2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl2.setAlignment(Label.CENTER);

        bl3 = new Label("MS");
        bl3.setBackground(Color.black);
        bl3.setForeground(Color.white);
        bl3.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl3.setAlignment(Label.CENTER);

        bl4 = new Label("M+");
        bl4.setBackground(Color.black);
        bl4.setForeground(Color.white);
        bl4.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl4.setAlignment(Label.CENTER);

        bl5 = new Label("M-");
        bl5.setBackground(Color.black);
        bl5.setForeground(Color.white);
        bl5.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl5.setAlignment(Label.CENTER);

        keyboard1.add(bl1);
        keyboard1.add(bl2);
        keyboard1.add(bl3);
        keyboard1.add(bl4);
        keyboard1.add(bl5);

        // from second line
        // 1 panel
        Panel keyboard2 = new Panel();
        keyboard2.setBounds(mainWhitePanel.getX() + 2, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 3 - 3, keyboard1.getHeight());
        GridLayout keyboardLayout2 = new GridLayout(1, 5, 3, 3);
        keyboard2.setLayout(keyboardLayout2);

        bl6 = new Label("<--");
        bl6.setBackground(Color.GRAY);
        bl6.setForeground(Color.white);
        bl6.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl6.setAlignment(Label.CENTER);

        bl7 = new Label("CE");
        bl7.setBackground(Color.GRAY);
        bl7.setForeground(Color.white);
        bl7.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl7.setAlignment(Label.CENTER);

        bl8 = new Label("C");
        bl8.setBackground(Color.GRAY);
        bl8.setForeground(Color.white);
        bl8.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl8.setAlignment(Label.CENTER);

        keyboard2.add(bl6);
        keyboard2.add(bl7);
        keyboard2.add(bl8);

        // 2 panel
        Panel keyboard3 = new Panel();
        keyboard3.setBounds(keyboard2.getX() + keyboard2.getWidth() + 3, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 1 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
        GridLayout keyboardLayout3 = new GridLayout(4, 1, 3, 3);
        keyboard3.setLayout(keyboardLayout3);

        bl9 = new Label("%");
        bl9.setBackground(Color.decode("#493c85"));
        bl9.setForeground(Color.white);
        bl9.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl9.setAlignment(Label.CENTER);

        bl14 = new Label("\u221A"); // SQRT
        bl14.setBackground(Color.decode("#493c85"));
        bl14.setForeground(Color.white);
        bl14.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl14.setAlignment(Label.CENTER);

        bl19 = new Label("()");
        bl19.setBackground(Color.decode("#493c85"));
        bl19.setForeground(Color.white);
        bl19.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl19.setAlignment(Label.CENTER);

        bl24 = new Label("^");
        bl24.setBackground(Color.decode("#493c85"));
        bl24.setForeground(Color.white);
        bl24.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl24.setAlignment(Label.CENTER);

        keyboard3.add(bl9);
        keyboard3.add(bl14);
        keyboard3.add(bl19);
        keyboard3.add(bl24);

        // 3 panel
        Panel keyboard4 = new Panel();
        keyboard4.setBounds(keyboard3.getX() + keyboard3.getWidth() + 3, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 1 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
        GridLayout keyboardLayout4 = new GridLayout(4, 1, 3, 3);
        keyboard4.setLayout(keyboardLayout4);

        bl10 = new Label("/");
        bl10.setBackground(Color.decode("#8C52FF"));
        bl10.setForeground(Color.white);
        bl10.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl10.setAlignment(Label.CENTER);

        bl15 = new Label("*");
        bl15.setBackground(Color.decode("#8C52FF"));
        bl15.setForeground(Color.white);
        bl15.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl15.setAlignment(Label.CENTER);

        bl20 = new Label("-");
        bl20.setBackground(Color.decode("#8C52FF"));
        bl20.setForeground(Color.white);
        bl20.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl20.setAlignment(Label.CENTER);

        bl25 = new Label("+");
        bl25.setBackground(Color.decode("#8C52FF"));
        bl25.setForeground(Color.white);
        bl25.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl25.setAlignment(Label.CENTER);

        keyboard4.add(bl10);
        keyboard4.add(bl15);
        keyboard4.add(bl20);
        keyboard4.add(bl25);

        // 4 panel
        Panel keyboard5 = new Panel();
        keyboard5.setBounds(mainWhitePanel.getX() + 1, keyboard2.getY() + keyboard2.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 3 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
        GridLayout keyboardLayout5 = new GridLayout(4, 3, 3, 3);
        keyboard5.setLayout(keyboardLayout5);

        b11 = new Button("1");
        b11.setBackground(Color.decode("#004AAD"));
        b11.setForeground(Color.white);
        b11.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b12 = new Button("2");
        b12.setBackground(Color.decode("#004AAD"));
        b12.setForeground(Color.white);
        b12.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b13 = new Button("3");
        b13.setBackground(Color.decode("#004AAD"));
        b13.setForeground(Color.white);
        b13.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b16 = new Button("4");
        b16.setBackground(Color.decode("#004AAD"));
        b16.setForeground(Color.white);
        b16.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b17 = new Button("5");
        b17.setBackground(Color.decode("#004AAD"));
        b17.setForeground(Color.white);
        b17.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b18 = new Button("6");
        b18.setBackground(Color.decode("#004AAD"));
        b18.setForeground(Color.white);
        b18.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b21 = new Button("7");
        b21.setBackground(Color.decode("#004AAD"));
        b21.setForeground(Color.white);
        b21.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b22 = new Button("8");
        b22.setBackground(Color.decode("#004AAD"));
        b22.setForeground(Color.white);
        b22.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b23 = new Button("9");
        b23.setBackground(Color.decode("#004AAD"));
        b23.setForeground(Color.white);
        b23.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b26 = new Button(".");
        b26.setBackground(Color.decode("#004AAD"));
        b26.setForeground(Color.white);
        b26.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b27 = new Button("0");
        b27.setBackground(Color.decode("#004AAD"));
        b27.setForeground(Color.white);
        b27.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        b28 = new Button("00");
        b28.setBackground(Color.decode("#004AAD"));
        b28.setForeground(Color.white);
        b28.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        keyboard5.add(b11);
        keyboard5.add(b12);
        keyboard5.add(b13);
        keyboard5.add(b16);
        keyboard5.add(b17);
        keyboard5.add(b18);
        keyboard5.add(b21);
        keyboard5.add(b22);
        keyboard5.add(b23);
        keyboard5.add(b26);
        keyboard5.add(b27);
        keyboard5.add(b28);

        // 5 panel
        Panel keyboard6 = new Panel();
        keyboard6.setBounds(keyboard3.getX(), keyboard3.getY() + keyboard3.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());
        GridLayout keyboardLayout6 = new GridLayout(1, 2, 3, 3);
        keyboard6.setLayout(keyboardLayout6);

        bl30 = new Label("=");
        bl30.setBackground(Color.black);
        bl30.setForeground(Color.white);
        bl30.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        bl30.setAlignment(Label.CENTER);

        keyboard6.add(bl30);

        //listeners for menu items
        // option ->1 listeners
        option1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (option1.getState()) { // operators available

                    if (option2.getState()) { // advanced operator available

                        frame.setBounds(250, 250, 480, 420);
                        mainGradientPanel.setSize(480, 420);
                        mainWhitePanel.setBounds(7, 0, mainGradientPanel.getWidth() - 14, mainGradientPanel.getHeight() - 7);

                        keyboard1.setVisible(true);

                        keyboard1.setBounds(mainWhitePanel.getX(), display.getY() + display.getHeight() + 20, mainWhitePanel.getWidth(), 30);
                        keyboard2.setBounds(mainWhitePanel.getX() + 2, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 3 - 3, keyboard1.getHeight() + 1);
                        keyboard3.setBounds(keyboard2.getX() + keyboard2.getWidth() + 3, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 1 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard4.setBounds(keyboard3.getX() + keyboard3.getWidth() + 3, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 1 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard5.setBounds(mainWhitePanel.getX() + 1, keyboard2.getY() + keyboard2.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 3 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard6.setBounds(keyboard3.getX(), keyboard3.getY() + keyboard3.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());

                        display.setBounds(mainWhitePanel.getX() + 10, menuBarPanel1.getY() + 50, mainWhitePanel.getWidth() - 20, 80);
                        menuBarPanel2.setBounds(422, 40, 50, 30);

                        updateControlPosition(425, 5, 20, 25, 395, 10, 18, 18, 455, 8, 22, 22);

                    } else { // no advanced

                        frame.setBounds(250, 250, 480, 420);
                        mainGradientPanel.setSize(480, 420);
                        mainWhitePanel.setBounds(7, 0, mainGradientPanel.getWidth() - 14, mainGradientPanel.getHeight() - 7);

                        keyboard3.setVisible(false);
                        keyboard1.setVisible(true);

                        keyboard2.setBounds(mainWhitePanel.getX() + 2, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 3 - 3, keyboard1.getHeight());
                        keyboard4.setBounds(keyboard2.getX() + keyboard2.getWidth() + 3, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 2 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard5.setBounds(mainWhitePanel.getX() + 1, keyboard2.getY() + keyboard2.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 3 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard6.setBounds(keyboard4.getX(), keyboard4.getY() + keyboard4.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());

                        bl10.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());
                        bl15.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());
                        bl20.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());
                        bl25.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());
                        bl30.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());

                        display.setBounds(mainWhitePanel.getX() + 10, menuBarPanel1.getY() + 50, mainWhitePanel.getWidth() - 20, 80);
                        menuBarPanel2.setBounds(422, 40, 50, 30);

                        updateControlPosition(425, 5, 20, 25, 395, 10, 18, 18, 455, 8, 22, 22);

                    }

                } else { // no operators

                    if (option2.getState()) { // advanced operator available

                        frame.setBounds(250, 250, 480, 400);
                        mainGradientPanel.setSize(480, 400);
                        mainWhitePanel.setBounds(7, 0, mainGradientPanel.getWidth() - 14, mainGradientPanel.getHeight() - 7);

                        keyboard2.setBounds(mainWhitePanel.getX() + 2, display.getY() + display.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 3 - 3, keyboard1.getHeight());
                        keyboard3.setBounds(keyboard2.getX() + keyboard2.getWidth() + 3, display.getY() + display.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 1 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 74));
                        keyboard4.setBounds(keyboard3.getX() + keyboard3.getWidth() + 3, display.getY() + display.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 1 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 74));
                        keyboard5.setBounds(mainWhitePanel.getX() + 1, keyboard2.getY() + keyboard2.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 3 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard6.setBounds(keyboard3.getX(), keyboard3.getY() + keyboard3.getHeight() + 5, mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight() + 30);

                    } else { // no advanced

                        keyboard1.setVisible(false);

                        frame.setBounds(250, 250, 300, 360);
                        mainGradientPanel.setSize(300, 360);
                        mainWhitePanel.setBounds(7, 0, mainGradientPanel.getWidth() - 14, mainGradientPanel.getHeight() - 7);

                        keyboard3.setVisible(false);

                        keyboard2.setBounds(mainWhitePanel.getX() + 2, display.getY() + display.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 3 + 33, keyboard1.getHeight());
                        keyboard4.setBounds(keyboard2.getX() + keyboard2.getWidth() + 3, keyboard2.getY(), mainWhitePanel.getWidth() / 5 * 1 + 18, mainWhitePanel.getHeight() - (keyboard2.getY() + 33));
                        keyboard5.setBounds(mainWhitePanel.getX() + 1, keyboard2.getY() + keyboard2.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 3 + 33, mainWhitePanel.getHeight() - (keyboard2.getY() + 33));
                        keyboard6.setBounds(keyboard4.getX(), keyboard4.getY() + keyboard4.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 1 + 18, keyboard2.getHeight() - 2);

                        bl10.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl15.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl20.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl25.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl30.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());

                        display.setBounds(mainWhitePanel.getX() + 10, menuBarPanel1.getY() + 50, keyboard2.getX() + keyboard2.getWidth() + keyboard4.getWidth() - 20, 80);
                        menuBarPanel2.setBounds(display.getX() + display.getWidth() - 25, 40, 30, 30);

                        updateControlPosition(230, 5, 20, 25, 200, 10, 18, 18, 260, 8, 22, 22);

                    }

                }
            }

        });

        // option ->2 listeners
        option2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (option1.getState()) { //operators available

                    if (option2.getState()) { // advanced avaliable

                        frame.setBounds(250, 250, 480, 420);
                        mainGradientPanel.setSize(480, 420);
                        mainWhitePanel.setBounds(7, 0, mainGradientPanel.getWidth() - 14, mainGradientPanel.getHeight() - 7);

                        keyboard3.setVisible(true);

                        keyboard2.setBounds(mainWhitePanel.getX() + 2, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 3 - 3, keyboard1.getHeight());
                        keyboard3.setBounds(keyboard2.getX() + keyboard2.getWidth() + 3, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 1 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard4.setBounds(keyboard3.getX() + keyboard3.getWidth() + 3, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 1 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard5.setBounds(mainWhitePanel.getX() + 1, keyboard2.getY() + keyboard2.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 3 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard6.setBounds(keyboard3.getX(), keyboard3.getY() + keyboard3.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());

                        bl10.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl15.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl20.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl25.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl30.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());

                        display.setBounds(mainWhitePanel.getX() + 10, menuBarPanel1.getY() + 50, mainWhitePanel.getWidth() - 20, 80);
                        menuBarPanel2.setBounds(422, 40, 50, 30);

                        updateControlPosition(425, 5, 20, 25, 395, 10, 18, 18, 455, 8, 22, 22);

                    } else { // no advanced

                        frame.setBounds(250, 250, 480, 420);
                        mainGradientPanel.setSize(480, 420);
                        mainWhitePanel.setBounds(7, 0, mainGradientPanel.getWidth() - 14, mainGradientPanel.getHeight() - 7);

                        keyboard3.setVisible(false);

                        keyboard2.setBounds(mainWhitePanel.getX() + 2, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 3 - 3, keyboard1.getHeight());
                        keyboard4.setBounds(keyboard2.getX() + keyboard2.getWidth() + 3, keyboard1.getY() + keyboard1.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 2 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard5.setBounds(mainWhitePanel.getX() + 1, keyboard2.getY() + keyboard2.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 3 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard6.setBounds(keyboard4.getX(), keyboard4.getY() + keyboard4.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());

                        bl10.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());
                        bl15.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());
                        bl20.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());
                        bl25.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());
                        bl30.setSize(mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight());

                        display.setBounds(mainWhitePanel.getX() + 10, menuBarPanel1.getY() + 50, mainWhitePanel.getWidth() - 20, 80);
                        menuBarPanel2.setBounds(422, 40, 50, 30);

                        updateControlPosition(425, 5, 20, 25, 395, 10, 18, 18, 455, 8, 22, 22);

                    }

                } else { // no operators

                    if (option2.getState()) { // advanced available

                        frame.setBounds(250, 250, 480, 400);
                        mainGradientPanel.setSize(480, 400);
                        mainWhitePanel.setBounds(7, 0, mainGradientPanel.getWidth() - 14, mainGradientPanel.getHeight() - 7);

                        keyboard2.setBounds(mainWhitePanel.getX() + 2, display.getY() + display.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 3 - 3, keyboard1.getHeight());
                        keyboard3.setBounds(keyboard2.getX() + keyboard2.getWidth() + 3, display.getY() + display.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 1 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 74));
                        keyboard4.setBounds(keyboard3.getX() + keyboard3.getWidth() + 3, display.getY() + display.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 1 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 74));
                        keyboard5.setBounds(mainWhitePanel.getX() + 1, keyboard2.getY() + keyboard2.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 3 - 3, mainWhitePanel.getHeight() - (keyboard2.getY() + 40));
                        keyboard6.setBounds(keyboard3.getX(), keyboard3.getY() + keyboard3.getHeight() + 5, mainWhitePanel.getWidth() / 5 * 2 - 3, keyboard2.getHeight() + 30);

                        keyboard3.setVisible(true);

                        display.setBounds(mainWhitePanel.getX() + 10, menuBarPanel1.getY() + 50, mainWhitePanel.getWidth() - 20, 80);
                        menuBarPanel2.setBounds(422, 40, 50, 30);

                        updateControlPosition(425, 5, 20, 25, 395, 10, 18, 18, 455, 8, 22, 22);

                    } else { // no advanced

                        keyboard1.setVisible(false);

                        frame.setBounds(250, 250, 300, 360);
                        mainGradientPanel.setSize(300, 360);
                        mainWhitePanel.setBounds(7, 0, mainGradientPanel.getWidth() - 14, mainGradientPanel.getHeight() - 7);

                        keyboard3.setVisible(false);

                        keyboard2.setBounds(mainWhitePanel.getX() + 2, display.getY() + display.getHeight() + 20, mainWhitePanel.getWidth() / 5 * 3 + 33, keyboard1.getHeight());
                        keyboard4.setBounds(keyboard2.getX() + keyboard2.getWidth() + 3, keyboard2.getY(), mainWhitePanel.getWidth() / 5 * 1 + 18, mainWhitePanel.getHeight() - (keyboard2.getY() + 33));
                        keyboard5.setBounds(mainWhitePanel.getX() + 1, keyboard2.getY() + keyboard2.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 3 + 33, mainWhitePanel.getHeight() - (keyboard2.getY() + 33));
                        keyboard6.setBounds(keyboard4.getX(), keyboard4.getY() + keyboard4.getHeight() + 3, mainWhitePanel.getWidth() / 5 * 1 + 18, keyboard2.getHeight() - 2);

                        bl10.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl15.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl20.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl25.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());
                        bl30.setSize(mainWhitePanel.getWidth() / 5 * 1 - 3, keyboard2.getHeight());

                        display.setBounds(mainWhitePanel.getX() + 10, menuBarPanel1.getY() + 50, keyboard2.getX() + keyboard2.getWidth() + keyboard4.getWidth() - 20, 80);
                        menuBarPanel2.setBounds(display.getX() + display.getWidth() - 25, 40, 30, 30);

                        updateControlPosition(230, 5, 20, 25, 200, 10, 18, 18, 260, 8, 22, 22);

                    }

                }
            }
        });

        copy.addActionListener((e) -> {
            String copyText = displayLabel2.getText();

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selectedText = new StringSelection(copyText);
            clipboard.setContents(selectedText, null);

            String dlbl1 = displayLabel1.getText();
            displayLabel1.setText("Coppied To Clipboard");
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    displayLabel1.setText(dlbl1);
                }
            };
            long wait = 3000;
            timer.schedule(task, wait);
        });

        cut.addActionListener((e) -> {
            String copyText = displayLabel2.getText();

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selectedText = new StringSelection(copyText);
            clipboard.setContents(selectedText, null);

            displayLabel2.setText("");

            String dlbl1 = displayLabel1.getText();
            displayLabel1.setText("Cut To Clipboard");
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    displayLabel1.setText(dlbl1);
                }
            };
            long wait = 3000;
            timer.schedule(task, wait);
        });

        viewHelp.addActionListener((e) -> {

            if (GlobalVariable.helpFrameCount == 0) {
                Help helpWindow = new Help();
                helpWindow.design();

                GlobalVariable.helpFrameCount = 1;
            }

        });

        aboutCal.addActionListener((e) -> {

            if (GlobalVariable.aboutFrameCount == 0) {
                About aboutWindow = new About();
                aboutWindow.aboutDesign();

                GlobalVariable.aboutFrameCount = 1;
            }

        });

        // mouse listeners for keybord lables
        bl1.addMouseListener(mouseListener);
        bl2.addMouseListener(mouseListener);
        bl3.addMouseListener(mouseListener);
        bl4.addMouseListener(mouseListener);
        bl5.addMouseListener(mouseListener);
        bl6.addMouseListener(mouseListener);
        bl7.addMouseListener(mouseListener);
        bl8.addMouseListener(mouseListener);
        bl9.addMouseListener(mouseListener);
        bl10.addMouseListener(mouseListener);
        bl14.addMouseListener(mouseListener);
        bl15.addMouseListener(mouseListener);
        bl19.addMouseListener(mouseListener);
        bl20.addMouseListener(mouseListener);
        bl24.addMouseListener(mouseListener);
        bl25.addMouseListener(mouseListener);
        bl30.addMouseListener(mouseListener);

        // action listeners for keyboard btn
        b11.addActionListener(this); // 1
        b12.addActionListener(this); // 2 
        b13.addActionListener(this); // 3
        b16.addActionListener(this); // 4
        b17.addActionListener(this); // 5
        b18.addActionListener(this); // 6
        b21.addActionListener(this); // 7
        b22.addActionListener(this); // 8
        b23.addActionListener(this); // 9
        b26.addActionListener(this); // .
        b27.addActionListener(this); // 0
        b28.addActionListener(this); // 00

        // adding panels
        frame.add(keyboard6);
        frame.add(keyboard5);
        frame.add(keyboard4);
        frame.add(keyboard3);
        frame.add(keyboard2);
        frame.add(keyboard1);
        frame.add(display);
        frame.add(helpPop);
        frame.add(editPop);
        frame.add(optionPopup);
        frame.add(menuBarPanel2);
        frame.add(menuBarPanel1);
        frame.add(controls);
        frame.add(mainWhitePanel);
        frame.add(mainGradientPanel);

        frame.setIconImage(icon); // icon
        frame.setVisible(true); // display frame
        frame.setAlwaysOnTop(true); // display top

    }

    public void updateControlPosition(int newMinimizeIconX, int newMinimizeIconY, int newMinimizeIconW, int newMinimizeIconH, int newPlusIconX, int newPlusIconY, int newPlusIconW, int newPlusIconH, int newCloseIconX, int newCloseIconY, int newCloseIconW, int newCloseIconH) {

        minimizeIconX = newMinimizeIconX;
        minimizeIconY = newMinimizeIconY;
        minimizeIconW = newMinimizeIconW;
        minimizeIconH = newMinimizeIconH;

        plusIconX = newPlusIconX;
        plusIconY = newPlusIconY;
        plusIconW = newPlusIconW;
        plusIconH = newPlusIconH;

        closeIconX = newCloseIconX;
        closeIconY = newCloseIconY;
        closeIconW = newCloseIconW;
        closeIconH = newCloseIconH;

        frame.repaint();

    }

    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            long delay = 3000;

            Object o = e.getSource();

            if (o.equals(bl1)) { // mc => memory clear

                memory = 0;
                memoryStatus = false;

            } else if (o.equals(bl2)) { // mr => memory recall

                if (memoryStatus) { // has stored
                    displayLabel2.setText(String.valueOf(memory));
                } else {
                    String dlbl1 = displayLabel1.getText();
                    displayLabel1.setText("Memory is Empty");
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            displayLabel1.setText(dlbl1);
                        }
                    };
                    timer.schedule(task, delay);
                }

            } else if (o.equals(bl3)) { // ms=> memory store

                memory = Double.valueOf(displayLabel2.getText());
                memoryStatus = true;

                String dlbl1 = displayLabel1.getText();
                displayLabel1.setText("Stored in memory");
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        displayLabel1.setText(dlbl1);
                    }
                };
                timer.schedule(task, delay);

            } else if (o.equals(bl4)) { // m+ =>  memory add

                String text = displayLabel2.getText();
                if (text.matches("\\d+(\\.\\d+)?")) {

                    memory = memory + Double.valueOf(text);
                    displayLabel2.setText(String.valueOf(memory));

                } else {

                    String text2 = displayLabel1.getText();
                    displayLabel1.setText("Needs to be Integer OR double");

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            displayLabel1.setText(text2);
                        }
                    };
                    timer.schedule(task, delay);

                }

            } else if (o.equals(bl5)) { // m- => memory subtract

                String text = displayLabel2.getText();
                if (text.matches("\\d+(\\.\\d+)?")) {

                    memory = memory - Double.valueOf(text);
                    displayLabel2.setText(String.valueOf(memory));

                } else {

                    String text2 = displayLabel1.getText();
                    displayLabel1.setText("Needs to be Integer OR double");

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            displayLabel1.setText(text2);
                        }
                    };
                    timer.schedule(task, delay);

                }

            } else if (o.equals(bl6)) { // <--

                String text = displayLabel2.getText();
                if (text.charAt(text.length() - 1) == '(') {
                    bracket = false;
                }
                displayLabel2.setText(text.substring(0, text.length() - 1));

            } else if (o.equals(bl7)) { // ce => clear entry

                displayLabel2.setText("00");

            } else if (o.equals(bl8)) { // c => clear

                memory = 0;
                memoryStatus = false;
                displayLabel2.setText("00");
                displayLabel1.setText("");
                bracket = false;
                started = false;
                op = null;

                caculated_value = 0;
                first_value = 0;
                second_value = 0;

                number1 = 0;
                number2 = 0;
                value = 0;
                operator = "";

            } else if (o.equals(bl9)) { // %

                if (!started) {

                    String text2 = displayLabel1.getText();
                    displayLabel1.setText("Select Number First");

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            displayLabel1.setText(text2);
                        }
                    };
                    timer.schedule(task, delay);
                } else {

                    String displayText = displayLabel2.getText();
                    char lastChar = displayText.charAt(displayText.length() - 1);

                    if (lastChar == '(' || lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/' || lastChar == '^'
                            || lastChar == '%' || lastChar == '\u221A' || lastChar == '.') {

                        String text2 = displayLabel1.getText();
                        displayLabel1.setText("Invalid Expression");

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            public void run() {
                                displayLabel1.setText(text2);
                            }
                        };
                        timer.schedule(task, delay);

                    } else {
                        displayLabel2.setText(displayLabel2.getText() + "%");
                        op = "%";
                    }

                }

            } else if (o.equals(bl10)) { // "/"

                if (!started) {
                    String text2 = displayLabel1.getText();
                    displayLabel1.setText("Select Number First");

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            displayLabel1.setText(text2);
                        }
                    };
                    timer.schedule(task, delay);
                } else {

                    String displayText = displayLabel2.getText();
                    char lastChar = displayText.charAt(displayText.length() - 1);

                    if (lastChar == '(' || lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/' || lastChar == '^'
                            || lastChar == '%' || lastChar == '\u221A' || lastChar == '.') {

                        String text2 = displayLabel1.getText();
                        displayLabel1.setText("Invalid Expression");

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            public void run() {
                                displayLabel1.setText(text2);
                            }
                        };
                        timer.schedule(task, delay);

                    } else {
                        displayLabel2.setText(displayLabel2.getText() + "/");
                        op = "/";
                    }
                }

            } else if (o.equals(bl14)) { // sqrt

                String displayText = displayLabel2.getText();
                char lastChar = displayText.charAt(displayText.length() - 1);

                if (lastChar == '1' || lastChar == '2' || lastChar == '3' || lastChar == '4'
                        || lastChar == '5' || lastChar == '6' || lastChar == '7' || lastChar == '8'
                        || lastChar == '9' || lastChar == '0' || lastChar == ')') {
                    displayLabel2.setText(displayLabel2.getText() + "*");
                } else if (lastChar == '.') {
                    displayLabel2.setText(displayLabel2.getText() + "0*");
                }

                displayLabel2.setText(displayLabel2.getText() + "\u221A");
                op = "\u221A";

            } else if (o.equals(bl15)) { // *

                if (!started) {
                    String text2 = displayLabel1.getText();
                    displayLabel1.setText("Select Number First");

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            displayLabel1.setText(text2);
                        }
                    };
                    timer.schedule(task, delay);
                } else {
                    String displayText = displayLabel2.getText();
                    char lastChar = displayText.charAt(displayText.length() - 1);

                    if (lastChar == '(' || lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/' || lastChar == '^'
                            || lastChar == '%' || lastChar == '\u221A' || lastChar == '.') {

                        String text2 = displayLabel1.getText();
                        displayLabel1.setText("Invalid Expression");

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            public void run() {
                                displayLabel1.setText(text2);
                            }
                        };
                        timer.schedule(task, delay);

                    } else {
                        displayLabel2.setText(displayLabel2.getText() + "*");
                        op = "*";
                    }
                }

            } else if (o.equals(bl19)) { // ()

                if (bracket) {

                    displayLabel2.setText(displayLabel2.getText() + ")");
                    bracket = false;
                    op = ")";

                } else {

                    if (displayLabel2.getText() == "00") {
                        displayLabel2.setText("(");
                        bracket = true;
                        op = "(";
                    } else {

                        String displayText = displayLabel2.getText();
                        char lastChar = displayText.charAt(displayText.length() - 1);

                        if (started && (lastChar == '1' || lastChar == '2' || lastChar == '3' || lastChar == '4'
                                || lastChar == '5' || lastChar == '6' || lastChar == '7' || lastChar == '8'
                                || lastChar == '9' || lastChar == '0' || lastChar == ')')) {
                            displayLabel2.setText(displayLabel2.getText() + "*");
                        } else if (lastChar == '.') {
                            displayLabel2.setText(displayLabel2.getText() + "0*");
                        }

                        displayLabel2.setText(displayLabel2.getText() + "(");
                        bracket = true;
                        op = "(";
                    }
                }

            } else if (o.equals(bl20)) { // -

                if (!started) {
                    String text2 = displayLabel1.getText();
                    displayLabel1.setText("Select Number First");

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            displayLabel1.setText(text2);
                        }
                    };
                    timer.schedule(task, delay);
                } else {
                    String displayText = displayLabel2.getText();
                    char lastChar = displayText.charAt(displayText.length() - 1);

                    if (lastChar == '(' || lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/' || lastChar == '^'
                            || lastChar == '%' || lastChar == '\u221A' || lastChar == '.') {

                        String text2 = displayLabel1.getText();
                        displayLabel1.setText("Invalid Expression");

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            public void run() {
                                displayLabel1.setText(text2);
                            }
                        };
                        timer.schedule(task, delay);

                    } else {
                        displayLabel2.setText(displayLabel2.getText() + "-");
                        op = "-";
                    }
                }

            } else if (o.equals(bl24)) { // ^

                if (!started) {
                    String text2 = displayLabel1.getText();
                    displayLabel1.setText("Select Number First");

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            displayLabel1.setText(text2);
                        }
                    };
                    timer.schedule(task, delay);
                } else {
                    String displayText = displayLabel2.getText();
                    char lastChar = displayText.charAt(displayText.length() - 1);

                    if (lastChar == '(' || lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/' || lastChar == '^'
                            || lastChar == '%' || lastChar == '\u221A' || lastChar == '.') {

                        String text2 = displayLabel1.getText();
                        displayLabel1.setText("Invalid Expression");

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            public void run() {
                                displayLabel1.setText(text2);
                            }
                        };
                        timer.schedule(task, delay);

                    } else {
                        displayLabel2.setText(displayLabel2.getText() + "^");
                        op = "^";
                    }
                }

            } else if (o.equals(bl25)) { // +

                if (!started) {
                    String text2 = displayLabel1.getText();
                    displayLabel1.setText("Select Number First");

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            displayLabel1.setText(text2);
                        }
                    };
                    timer.schedule(task, delay);
                } else {
                    String displayText = displayLabel2.getText();
                    char lastChar = displayText.charAt(displayText.length() - 1);

                    if (lastChar == '(' || lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/' || lastChar == '^'
                            || lastChar == '%' || lastChar == '\u221A' || lastChar == '.') {

                        String text2 = displayLabel1.getText();
                        displayLabel1.setText("Invalid Expression");

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            public void run() {
                                displayLabel1.setText(text2);
                            }
                        };
                        timer.schedule(task, delay);

                    } else {
                        displayLabel2.setText(displayLabel2.getText() + "+");
                        op = "+";
                    }
                }

            } else if (o.equals(bl30)) { // =

                if (!started) {
                    String text2 = displayLabel1.getText();
                    displayLabel1.setText("Select Number First");

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        public void run() {
                            displayLabel1.setText(text2);
                        }
                    };
                    timer.schedule(task, delay);
                } else {

                    String displayText = displayLabel2.getText();
                    char lastChar = displayText.charAt(displayText.length() - 1);

                    if (lastChar == '.' || lastChar == '+' || lastChar == '-' || lastChar == '*'
                            || lastChar == '/' || lastChar == '%' || lastChar == '^' || lastChar == '(' || lastChar == '\u221A') {

                        String text2 = displayLabel1.getText();
                        displayLabel1.setText("Incomplete Expression !");

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            public void run() {
                                displayLabel1.setText(text2);
                            }
                        };
                        timer.schedule(task, delay);
                    } else {
                        caculated_value = caculate(displayLabel2.getText());
                        displayLabel1.setText(displayLabel2.getText());
                        displayLabel2.setText(String.valueOf(caculated_value));
                        op = "=";
                        first_value = caculated_value;
                        second_value = 0.0;
                    }

                }

            }

        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

    };

    @Override
    public void actionPerformed(ActionEvent g) {

        Object o = g.getSource();

        if (o.equals(b11)) { // 1

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "1");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("1");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "1");
                }
            }
            started = true;

        } else if (o.equals(b12)) { // 2

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "2");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("2");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "2");
                }
            }
            started = true;

        } else if (o.equals(b13)) { // 3

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "3");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("3");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "3");
                }
            }
            started = true;

        } else if (o.equals(b16)) { // 4

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "4");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("4");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "4");
                }
            }
            started = true;

        } else if (o.equals(b17)) { // 5

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "5");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("5");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "5");
                }
            }
            started = true;

        } else if (o.equals(b18)) { // 6

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "6");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("6");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "6");
                }
            }
            started = true;

        } else if (o.equals(b21)) { // 7

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "7");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("7");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "7");
                }
            }
            started = true;

        } else if (o.equals(b22)) { // 8

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "8");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("8");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "8");
                }
            }
            started = true;

        } else if (o.equals(b23)) { // 9

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "9");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("9");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "9");
                }
            }
            started = true;

        } else if (o.equals(b26)) { // "."

            if (displayLabel2.getText().length() == 0) {
                displayLabel2.setText("0.");
            } else {
                displayLabel2.setText(displayLabel2.getText() + ".");
            }

            if (op != null) {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("0.");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + ".");
                }
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("0.");
                }
            }
            started = true;

        } else if (o.equals(b27)) { // 0

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "0");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("0");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "0");
                }
            }
            started = true;

        } else if (o.equals(b28)) { // 00

            if (op != null) {
                displayLabel2.setText(displayLabel2.getText() + "00");
            } else {
                if (displayLabel2.getText().equals("00")) {
                    displayLabel2.setText("00");
                } else {
                    displayLabel2.setText(displayLabel2.getText() + "00");
                }
            }
            started = true;

        }

    }

    public double caculate(String x) {

        boolean firstTime = true;

        String expression = x;
        String[] tokens = expression.split("(?<=[-+*/()^%\u221A])|(?=[-+*/()^%\u221A])");

        numbers = new ArrayList<>();
        operators = new ArrayList<>();

        for (String token : tokens) {

            if (token.matches("\\d+(\\.\\d+)?")) {
                numbers.add(Double.valueOf(token));
            } else {
                operators.add(token);
            }
        }

        // Convert lists to arrays
        numbersArray = numbers.toArray(new Double[0]);
        operatorsArray = operators.toArray(new String[0]);

        if (operators.contains("(") && operators.contains(")")) {

            int openIndex = operators.indexOf("(");
            int endIndex = operators.indexOf(")");

            numbersWithinBrackets = new ArrayList<>();
            operatorsWithinBrackets = new ArrayList<>();

            for (int i = openIndex + 1; i < endIndex; i++) {
                operatorsWithinBrackets.add(operatorsArray[i]);
            }
            for (int z = endIndex; z >= openIndex; z--) {
                operators.remove(z);
            }

            boolean sqrtStar = false;
            int starIndex = 0;
            int sqrtIndex = 0;
            if (expression.contains("*\u221A")) {
                sqrtStar = true;
                for (int i = 0; i < operatorsArray.length; i++) {
                    if (operatorsArray[i].equals("*") && i > openIndex && i < endIndex) {
                        if (operatorsArray[i + 1].equals("\u221A")) {
                            starIndex = i;
                            sqrtIndex = i + 1;
                        }
                    }
                }
            }
            if (sqrtStar && starIndex > openIndex && sqrtIndex > openIndex && starIndex < endIndex && sqrtIndex < endIndex && starIndex + 1 == sqrtIndex) {
                for (int i = openIndex; i < endIndex - 1; i++) {
                    numbersWithinBrackets.add(numbersArray[i]);
                }
                for (int z = endIndex - 2; z >= openIndex; z--) {
                    numbers.remove(z);
                }
            } else {
                if (expression.contains("(\u221A")) {
                    for (int i = openIndex; i < endIndex - 1; i++) {
                        numbersWithinBrackets.add(numbersArray[i]);
                    }
                    for (int z = endIndex - 2; z >= openIndex; z--) {
                        numbers.remove(z);
                    }
                } else {
                    for (int i = openIndex; i < endIndex; i++) {
                        numbersWithinBrackets.add(numbersArray[i]);
                    }
                    for (int z = endIndex - 1; z >= openIndex; z--) {
                        numbers.remove(z);
                    }
                }
            }

            numbersWithinBracketsArray = numbersWithinBrackets.toArray(new Double[0]);
            operatorsWithinBracketsArray = operatorsWithinBrackets.toArray(new String[0]);

            int multiplePlace = 0;
            int divisionPlace = 0;

            int plusPlace = 0;
            int minusPlace = 0;

            for (String c : operatorsWithinBracketsArray) {

                if (operatorsWithinBrackets.contains("^")) {

                    int opid = operatorsWithinBrackets.indexOf("^");
                    operator = "^";

                    if (firstTime) {

                        innerCal(opid);
                        firstTime = false;

                    } else {
                        innerCal(opid);
                    }

                } else if (operatorsWithinBrackets.contains("\u221A")) {

                    int opid = operatorsWithinBrackets.indexOf("\u221A");
                    operator = "\u221A";

                    if (firstTime) {

                        innerCal(opid);
                        firstTime = false;

                    } else {
                        innerCal(opid);
                    }

                } else if (operatorsWithinBrackets.contains("%")) {

                    int opid = operatorsWithinBrackets.indexOf("%");
                    operator = "%";

                    if (firstTime) {

                        innerCal(opid);
                        firstTime = false;

                    } else {
                        innerCal(opid);
                    }

                } else if (operatorsWithinBrackets.contains("*") && operatorsWithinBrackets.contains("/")) {

                    multiplePlace = operatorsWithinBrackets.indexOf("*");
                    divisionPlace = operatorsWithinBrackets.indexOf("/");

                    if (multiplePlace < divisionPlace) {
                        if (operatorsWithinBrackets.contains("*")) {

                            int opid = operatorsWithinBrackets.indexOf("*");
                            operator = "*";

                            if (firstTime) {

                                innerCal(opid);
                                firstTime = false;

                            } else {
                                innerCal(opid);
                            }

                        }

                        if (operatorsWithinBrackets.contains("/")) {

                            int opid = operatorsWithinBrackets.indexOf("/");
                            operator = "/";

                            if (firstTime) {

                                innerCal(opid);
                                firstTime = false;

                            } else {
                                innerCal(opid);
                            }

                        }
                    } else {

                        if (operatorsWithinBrackets.contains("/")) {

                            int opid = operatorsWithinBrackets.indexOf("/");
                            operator = "/";

                            if (firstTime) {

                                innerCal(opid);
                                firstTime = false;

                            } else {
                                innerCal(opid);
                            }

                        }

                        if (operatorsWithinBrackets.contains("*")) {

                            int opid = operatorsWithinBrackets.indexOf("*");
                            operator = "*";

                            if (firstTime) {

                                innerCal(opid);
                                firstTime = false;

                            } else {
                                innerCal(opid);
                            }

                        }

                    }

                } else if (operatorsWithinBrackets.contains("*")) {

                    int opid = operatorsWithinBrackets.indexOf("*");
                    operator = "*";

                    if (firstTime) {

                        innerCal(opid);
                        firstTime = false;

                    } else {
                        innerCal(opid);
                    }

                } else if (operatorsWithinBrackets.contains("/")) {

                    int opid = operatorsWithinBrackets.indexOf("/");
                    operator = "/";

                    if (firstTime) {

                        innerCal(opid);
                        firstTime = false;

                    } else {
                        innerCal(opid);
                    }

                } else if (operatorsWithinBrackets.contains("+") && operatorsWithinBrackets.contains("-")) {

                    plusPlace = operatorsWithinBrackets.indexOf("+");
                    minusPlace = operatorsWithinBrackets.indexOf("-");

                    if (plusPlace < minusPlace) {
                        if (operatorsWithinBrackets.contains("+")) {

                            int opid = operatorsWithinBrackets.indexOf("+");
                            operator = "+";

                            if (firstTime) {

                                innerCal(opid);
                                firstTime = false;

                            } else {
                                innerCal(opid);
                            }

                        }

                        if (operatorsWithinBrackets.contains("-")) {

                            int opid = operatorsWithinBrackets.indexOf("-");
                            operator = "-";

                            if (firstTime) {

                                innerCal(opid);
                                firstTime = false;

                            } else {
                                innerCal(opid);
                            }

                        }
                    } else {

                        if (operatorsWithinBrackets.contains("-")) {

                            int opid = operatorsWithinBrackets.indexOf("-");
                            operator = "-";

                            if (firstTime) {

                                innerCal(opid);
                                firstTime = false;

                            } else {
                                innerCal(opid);
                            }

                        }

                        if (operatorsWithinBrackets.contains("+")) {

                            int opid = operatorsWithinBrackets.indexOf("+");
                            operator = "+";

                            if (firstTime) {

                                innerCal(opid);
                                firstTime = false;

                            } else {
                                innerCal(opid);
                            }

                        }

                    }

                } else if (operatorsWithinBrackets.contains("+")) {

                    int opid = operatorsWithinBrackets.indexOf("+");
                    operator = "+";

                    if (firstTime) {

                        innerCal(opid);
                        firstTime = false;

                    } else {
                        innerCal(opid);
                    }

                } else if (operatorsWithinBrackets.contains("-")) {

                    int opid = operatorsWithinBrackets.indexOf("-");
                    operator = "-";

                    if (firstTime) {

                        innerCal(opid);
                        firstTime = false;

                    } else {
                        innerCal(opid);
                    }

                }

            }

            numbers.add(value);
            numbersArray = numbers.toArray(new Double[0]);
            operatorsArray = operators.toArray(new String[0]);

        }

        if (!operators.contains("(") && !operators.contains(")")) {

            int multiplePlace = 0;
            int divisionPlace = 0;

            int plusPlace = 0;
            int minusPlace = 0;

            for (String c : operatorsArray) {

                if (operators.contains("^")) {

                    int opid = operators.indexOf("^");
                    operator = "^";

                    if (firstTime) {

                        innerCal2(opid);
                        firstTime = false;

                    } else {
                        innerCal2(opid);
                    }

                } else if (operators.contains("\u221A")) {

                    int opid = operators.indexOf("\u221A");
                    operator = "\u221A";

                    if (firstTime) {

                        innerCal2(opid);
                        firstTime = false;

                    } else {
                        innerCal2(opid);
                    }

                } else if (operators.contains("%")) {

                    int opid = operators.indexOf("%");
                    operator = "%";

                    if (firstTime) {

                        innerCal2(opid);
                        firstTime = false;

                    } else {
                        innerCal2(opid);
                    }

                } else if (operators.contains("*") && operators.contains("/")) {

                    multiplePlace = operators.indexOf("*");
                    divisionPlace = operators.indexOf("/");

                    if (multiplePlace < divisionPlace) {

                        if (operators.contains("*")) {

                            int opid = operators.indexOf("*");
                            operator = "*";

                            if (firstTime) {

                                innerCal2(opid);
                                firstTime = false;

                            } else {
                                innerCal2(opid);
                            }

                        }

                        if (operators.contains("/")) {

                            int opid = operators.indexOf("/");
                            operator = "/";

                            if (firstTime) {

                                innerCal2(opid);
                                firstTime = false;

                            } else {
                                innerCal2(opid);
                            }

                        }

                    } else {

                        if (operators.contains("/")) {

                            int opid = operators.indexOf("/");
                            operator = "/";

                            if (firstTime) {

                                innerCal2(opid);
                                firstTime = false;

                            } else {
                                innerCal2(opid);
                            }

                        }

                        if (operators.contains("*")) {

                            int opid = operators.indexOf("*");
                            operator = "*";

                            if (firstTime) {

                                innerCal2(opid);
                                firstTime = false;

                            } else {
                                innerCal2(opid);
                            }

                        }

                    }

                } else if (operators.contains("*")) {

                    int opid = operators.indexOf("*");
                    operator = "*";

                    if (firstTime) {

                        innerCal2(opid);
                        firstTime = false;

                    } else {
                        innerCal2(opid);
                    }

                } else if (operators.contains("/")) {

                    int opid = operators.indexOf("/");
                    operator = "/";

                    if (firstTime) {

                        innerCal2(opid);
                        firstTime = false;

                    } else {
                        innerCal2(opid);
                    }

                } else if (operators.contains("+") && operators.contains("-")) {

                    plusPlace = operators.indexOf("+");
                    minusPlace = operators.indexOf("-");

                    if (plusPlace < minusPlace) {

                        if (operators.contains("+")) {

                            int opid = operators.indexOf("+");
                            operator = "+";

                            if (firstTime) {

                                innerCal2(opid);
                                firstTime = false;

                            } else {
                                innerCal2(opid);
                            }

                        }

                        if (operators.contains("-")) {

                            int opid = operators.indexOf("-");
                            operator = "-";

                            if (firstTime) {

                                innerCal2(opid);
                                firstTime = false;

                            } else {
                                innerCal2(opid);
                            }

                        }

                    } else {

                        if (operators.contains("-")) {

                            int opid = operators.indexOf("-");
                            operator = "-";

                            if (firstTime) {

                                innerCal2(opid);
                                firstTime = false;

                            } else {
                                innerCal2(opid);
                            }

                        }

                        if (operators.contains("+")) {

                            int opid = operators.indexOf("+");
                            operator = "+";

                            if (firstTime) {

                                innerCal2(opid);
                                firstTime = false;

                            } else {
                                innerCal2(opid);
                            }

                        }

                    }

                } else if (operators.contains("+")) {

                    int opid = operators.indexOf("+");
                    operator = "+";

                    if (firstTime) {

                        innerCal2(opid);
                        firstTime = false;

                    } else {
                        innerCal2(opid);
                    }

                } else if (operators.contains("-")) {

                    int opid = operators.indexOf("-");
                    operator = "-";

                    if (firstTime) {

                        innerCal2(opid);
                        firstTime = false;

                    } else {
                        innerCal2(opid);
                    }

                }

            }

        }

        return value;

    }

    public void innerCal(int opid) {

        if (operator != "\u221A") {
            number1 = numbersWithinBracketsArray[opid];
            number2 = numbersWithinBracketsArray[opid + 1];
        } else {
            number1 = 0;
            number2 = numbersWithinBracketsArray[opid];
        }

        value = valueOf(number1, number2, String.valueOf(operator));

        if (operator != "\u221A") {
            numbersWithinBrackets.remove(opid + 1);
            numbersWithinBrackets.remove(opid);
            numbersWithinBrackets.add(opid, value);
        } else {
            numbersWithinBrackets.remove(opid);
            numbersWithinBrackets.add(opid, value);
        }

        operatorsWithinBrackets.remove(opid);

        numbersWithinBracketsArray = numbersWithinBrackets.toArray(new Double[0]);
        operatorsWithinBracketsArray = operatorsWithinBrackets.toArray(new String[0]);

    }

    public void innerCal2(int opid) {

        if (operator != "\u221A") {
            number1 = numbersArray[opid];
            number2 = numbersArray[opid + 1];
        } else {
            number1 = 0;
            number2 = numbersArray[opid];
        }

        value = valueOf(number1, number2, String.valueOf(operator));

        if (operator != "\u221A") {
            numbers.remove(opid + 1);
            numbers.remove(opid);
            numbers.add(opid, value);
        } else {
            numbers.remove(opid);
            numbers.add(opid, value);
        }

        operators.remove(opid);

        numbersArray = numbers.toArray(new Double[0]);
        operatorsArray = operators.toArray(new String[0]);

    }

    public static double valueOf(double num_1, double num_2, String operator) {

        double out = 0;
        switch (operator) {
            case "+":
                out = num_1 + num_2;
                break;
            case "-":
                out = num_1 - num_2;
                break;
            case "/":
                out = num_1 / num_2;
                break;
            case "*":
                out = num_1 * num_2;
                break;
            case "^":
                out = Math.pow(num_1, num_2);
                break;
            case "%":
                out = (num_1 / 100) * num_2;
                break;
            case "\u221A":
                out = Math.sqrt(num_2);
                break;
            default:
                break;
        }

        return out;

    }

}

public class Calculator {

    public static void calculatorDisplay() {
        new normal_cal();
    }

}
