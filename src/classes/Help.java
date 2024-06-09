package classes;

import java.awt.Color;
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

class closeWindow extends WindowAdapter {

    public void windowClosing(WindowEvent e) {
        GlobalVariable.helpFrame.dispose();
        GlobalVariable.helpFrameCount = 0;
    }
}

class helpWindow implements ActionListener{

    helpWindow() {

        GlobalVariable.helpFrame = new Frame();

        GlobalVariable.helpFrame.setSize(500, 450);
        GlobalVariable.helpFrame.setLocationRelativeTo(null);
        GlobalVariable.helpFrame.setBackground(Color.black);
        GlobalVariable.helpFrame.setAlwaysOnTop(true);
        GlobalVariable.helpFrame.setLayout(null);
        GlobalVariable.helpFrame.setResizable(false);
        GlobalVariable.helpFrame.setTitle("Help");

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/logo.png"));
        GlobalVariable.helpFrame.setIconImage(icon);

        // logo
        Panel iconPanel = new Panel() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.drawImage(icon, (GlobalVariable.helpFrame.getWidth() / 2) - 65, 40, 120, 120, this);

            }
        };
        iconPanel.setBounds(0, 0, GlobalVariable.helpFrame.getWidth(), 150);

        // warning
        Image warningIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/warning.png"));
        Panel warning = new Panel() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.drawImage(warningIcon, 10, 10, 25, 25, this);

            }
        };
        warning.setBounds(20, iconPanel.getY() + iconPanel.getHeight() + 20, 50, 50);

        Panel warningMsg = new Panel();
        warningMsg.setBounds(warning.getX() + warning.getWidth() + 5, warning.getY(), GlobalVariable.helpFrame.getWidth() - (warning.getX() + warning.getWidth() + 5), warning.getHeight());
        GridLayout gl = new GridLayout(1, 1, 0, 0);
        warningMsg.setLayout(gl);

        Label msg = new Label("You can enter only one numer ofter \u221A   Eg :- \u221A 3");
        msg.setForeground(Color.yellow);
        msg.setFont(new Font("Courier New", Font.ITALIC, 15));
        msg.setAlignment(Label.LEFT);

        warningMsg.add(msg);

        // contact
        Panel contact = new Panel();
        contact.setBounds(warning.getX() + 15, warning.getY() + warning.getHeight() + 50, GlobalVariable.helpFrame.getWidth() - warning.getX(), 20);
        GridLayout cL = new GridLayout(1, 1, 0, 0);
        contact.setLayout(cL);

        Label cLbl = new Label("Operator Assistance  :");
        cLbl.setForeground(Color.white);
        cLbl.setFont(new Font("Courier New", Font.PLAIN, 20));
        cLbl.setAlignment(Label.LEFT);

        contact.add(cLbl);

        Panel contactDetails = new Panel();
        contactDetails.setBounds(contact.getX() + 50, contact.getY() + contact.getHeight() + 20, GlobalVariable.helpFrame.getWidth() - (contact.getX() + 50), 50);
        GridLayout cLD = new GridLayout(2, 1, 0, 0);
        contactDetails.setLayout(cLD);

        Label email = new Label("Email  :  pasindubathiya28@gmail");
        email.setForeground(Color.white);
        email.setFont(new Font("Courier New", Font.PLAIN, 15));
        email.setAlignment(Label.LEFT);

        Label mobile = new Label("Mobile  :  +94 740-211-671");
        mobile.setForeground(Color.white);
        mobile.setFont(new Font("Courier New", Font.PLAIN, 15));
        mobile.setAlignment(Label.LEFT);

        contactDetails.add(email);
        contactDetails.add(mobile);

        // add panels to frame
        GlobalVariable.helpFrame.add(iconPanel);
        GlobalVariable.helpFrame.add(warning);
        GlobalVariable.helpFrame.add(warningMsg);
        GlobalVariable.helpFrame.add(contact);
        GlobalVariable.helpFrame.add(contactDetails);

        GlobalVariable.helpFrame.addWindowListener(new closeWindow());
        GlobalVariable.helpFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

public class Help {

    public static void design(){
        new helpWindow();
    }

}
