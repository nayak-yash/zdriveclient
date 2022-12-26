package views;


import controller.ZDriveClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class LoginScreen extends JFrame implements ActionListener {
    private JPanel signInPanel;
    private JLabel headingLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel registerSwitch;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signButton;
    public LoginScreen(){
        setResizable(false);
        setSize(600,400);
        setLocationRelativeTo(null);
        setTitle("Sign In");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        signInPanel=new JPanel(null);
        headingLabel=new JLabel("Login");
        usernameLabel=new JLabel("Username");
        passwordLabel=new JLabel("Password");
        registerSwitch =new JLabel("Don't have account? Register");
        usernameField=new JTextField(15);
        passwordField=new JPasswordField(15);
        signButton=new JButton("Login");
        signInPanel.add(headingLabel);
        headingLabel.setBounds(250,10,300,30);
        headingLabel.setFont(new Font("Cascadia Code", Font.BOLD, 25));
        signInPanel.add(usernameLabel);
        usernameLabel.setBounds(100,100,70,30);
        usernameLabel.setFont(new Font("Cascadia Code", Font.PLAIN, 15));
        signInPanel.add(usernameField);
        usernameField.setBounds(300,100,200,30);
        signInPanel.add(passwordLabel);;
        passwordLabel.setBounds(100,150,70,30);
        passwordLabel.setFont(new Font("Cascadia Code", Font.PLAIN, 15));
        signInPanel.add(passwordField);
        passwordField.setBounds(300,150,200,30);
        signInPanel.add(signButton);
        signButton.setBounds(225,200,150,40);
        signInPanel.add(registerSwitch);
        registerSwitch.setBounds(210,250,200,30);
        add(signInPanel);
        setVisible(true);

        signButton.addActionListener(this::actionPerformed);
        registerSwitch.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                RegisterScreen register=new RegisterScreen();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File root =new ZDriveClient().loginPrompt(usernameField.getText().toString(), String.valueOf(passwordField.getPassword()).trim());
        if(root!=null){
            MainScreen mainScreen=new MainScreen(root);
            dispose();
        }
        else{
            usernameField.setText("");
            passwordField.setText("");
            JOptionPane.showMessageDialog(null,"Login Failed!!","Alert",JOptionPane.WARNING_MESSAGE);
        }
    }
    public static void main(String[] args) {
        LoginScreen loginScreen= new LoginScreen();
    }
}