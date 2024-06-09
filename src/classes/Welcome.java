package classes;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

class welcomeWindow implements ActionListener {

    welcomeWindow() {

        GlobalVariable.welcomeFrame = new Frame();

        GlobalVariable.welcomeFrame.setSize(400, 400);
        GlobalVariable.welcomeFrame.setLocationRelativeTo(null);
        GlobalVariable.welcomeFrame.setAlwaysOnTop(true);
        GlobalVariable.welcomeFrame.setUndecorated(true);
        GlobalVariable.welcomeFrame.setLayout(null);
        GlobalVariable.welcomeFrame.setBackground(Color.black);
        GlobalVariable.welcomeFrame.setResizable(false);
        GlobalVariable.welcomeFrame.setTitle("Welcome");

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/logo.png"));
        GlobalVariable.welcomeFrame.setIconImage(icon);

        // logo
        Panel iconPanel = new Panel() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.drawImage(icon, GlobalVariable.welcomeFrame.getWidth() / 2 - 90, GlobalVariable.welcomeFrame.getHeight() / 2 - 120, 180, 180, this);

            }
        };
        iconPanel.setBounds(0, 0, GlobalVariable.welcomeFrame.getWidth(), GlobalVariable.welcomeFrame.getHeight());

        // about
        Panel about = new Panel(new FlowLayout(FlowLayout.CENTER));
        about.setBounds(10, iconPanel.getY() + 250, GlobalVariable.welcomeFrame.getWidth(), 40);

        Label msg = new Label();
        msg.setText("Welcome to MyCal");
        msg.setFont(new Font("Courier New", Font.ITALIC, 20));
        msg.setForeground(Color.white);

        about.add(msg);

        // add panels to frame
        GlobalVariable.welcomeFrame.add(about);
        GlobalVariable.welcomeFrame.add(iconPanel);

        GlobalVariable.welcomeFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

public class Welcome {

    public static void main(String[] args) {
        new welcomeWindow();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                GlobalVariable.welcomeFrame.dispose();
                
                Calculator calculator = new Calculator();
                calculator.calculatorDisplay();

            }
        };
        long screenDelay = 3000;
        timer.schedule(task, screenDelay);
    }

}
