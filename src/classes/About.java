package classes;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class close extends WindowAdapter {

    public void windowClosing(WindowEvent e) {
        GlobalVariable.aboutFrame.dispose();
        GlobalVariable.aboutFrameCount = 0;
    }

}

class aboutWindow implements ActionListener {

    aboutWindow() {

        GlobalVariable.aboutFrame = new Frame();

        GlobalVariable.aboutFrame.setBounds(600, 250, 500, 500);
        GlobalVariable.aboutFrame.setAlwaysOnTop(true);
        GlobalVariable.aboutFrame.setLayout(null);
        GlobalVariable.aboutFrame.setResizable(false);
        GlobalVariable.aboutFrame.setTitle("About MyCal");

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/logo.png"));
        GlobalVariable.aboutFrame.setIconImage(icon);

        // logo
        Panel iconPanel = new Panel() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.drawImage(icon, (GlobalVariable.aboutFrame.getWidth() / 2) - 65, 40, 120, 120, this);

            }
        };
        iconPanel.setBounds(0, 0, GlobalVariable.aboutFrame.getWidth(), 150);

        // about
        Panel about = new Panel(new FlowLayout(FlowLayout.CENTER));
        about.setBounds(0, iconPanel.getY() + iconPanel.getHeight() + 10, GlobalVariable.aboutFrame.getWidth(), 40);

        Label msg = new Label();
        msg.setText("Welcome to MyCal");
        msg.setFont(new Font("Courier New", Font.PLAIN, 20));

        about.add(msg);

        Panel about2 = new Panel(new FlowLayout(FlowLayout.CENTER));
        about2.setBounds(0, about.getY() + about.getHeight() + 15, GlobalVariable.aboutFrame.getWidth(), 100);

        Label msg2 = new Label();
        msg2.setText("calculator built using AWT in Java that allows you to perform");
        msg2.setFont(new Font("Courier New", Font.ITALIC, 15));

        Label msg3 = new Label();
        msg3.setText("addition, subtraction, multiplication, division, modulus, square root");
        msg3.setFont(new Font("Courier New", Font.ITALIC, 15));

        Label msg4 = new Label();
        msg4.setText("and power operations on numbers");
        msg4.setFont(new Font("Courier New", Font.ITALIC, 15));

        about2.add(msg2);
        about2.add(msg3);
        about2.add(msg4);

        // contact
        Panel contact = new Panel();
        contact.setBounds(20, about2.getY() + about2.getHeight() + 30, GlobalVariable.aboutFrame.getWidth() - 20, 20);
        GridLayout cL = new GridLayout(1, 1, 0, 0);
        contact.setLayout(cL);

        Label cLbl = new Label("Contact Developer  :");
        cLbl.setFont(new Font("Courier New", Font.PLAIN, 20));
        cLbl.setAlignment(Label.LEFT);

        contact.add(cLbl);

        Panel contactDetails = new Panel();
        contactDetails.setBounds(contact.getX() + 50, contact.getY() + contact.getHeight() + 20, GlobalVariable.aboutFrame.getWidth() - (contact.getX() + 50), 50);
        GridLayout cLD = new GridLayout(2, 1, 0, 0);
        contactDetails.setLayout(cLD);

        Label email = new Label("Email  :  pasindubathiya28@gmail");
        email.setFont(new Font("Courier New", Font.PLAIN, 15));
        email.setAlignment(Label.LEFT);

        Label mobile = new Label("Mobile  :  +94 740-211-671");
        mobile.setFont(new Font("Courier New", Font.PLAIN, 15));
        mobile.setAlignment(Label.LEFT);

        contactDetails.add(email);
        contactDetails.add(mobile);

        // add panels to frame
        GlobalVariable.aboutFrame.add(iconPanel);
        GlobalVariable.aboutFrame.add(about);
        GlobalVariable.aboutFrame.add(about2);
        GlobalVariable.aboutFrame.add(contact);
        GlobalVariable.aboutFrame.add(contactDetails);

        GlobalVariable.aboutFrame.addWindowListener(new close());
        GlobalVariable.aboutFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

public class About {

    public static void aboutDesign() {
        new aboutWindow();
    }
}
