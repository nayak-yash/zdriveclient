package views;


import controller.ZDriveClient;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterScreen extends JFrame implements ActionListener {
    private JPanel signUpPanel;
    private JLabel headingLabel;
    private JLabel nameLabel;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JLabel confirmPasswordLabel;
    private JLabel loginSwitch;
    private JTextField nameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signButton;
    RegisterScreen(){
        setResizable(false);
        setSize(600,400);
        setLocationRelativeTo(null);
        setTitle("Sign Up");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        signUpPanel=new JPanel(null);
        headingLabel=new JLabel("Register");
        usernameLabel=new JLabel("Username");
        passwordLabel=new JLabel("Password");
        confirmPasswordLabel=new JLabel("Confirm Password");
        nameLabel=new JLabel("Name");
        emailLabel=new JLabel("Email");
        loginSwitch =new JLabel("Already have account? Login");
        usernameField=new JTextField(15);
        passwordField=new JPasswordField(15);
        nameField=new JTextField(20);
        confirmPasswordField=new JPasswordField(15);
        emailField=new JTextField(15);
        signButton=new JButton("Register");
        signUpPanel.add(headingLabel);
        headingLabel.setBounds(250,10,300,30);
        headingLabel.setFont(new Font("Cascadia Code", Font.BOLD, 25));
        signUpPanel.add(nameLabel);
        nameLabel.setBounds(100,50,70,30);
        nameLabel.setFont(new Font("Cascadia Code", Font.PLAIN, 15));
        signUpPanel.add(nameField);
        nameField.setBounds(300,50,200,30);
        signUpPanel.add(emailLabel);
        emailLabel.setBounds(100,90,70,30);
        emailLabel.setFont(new Font("Cascadia Code", Font.PLAIN, 15));
        signUpPanel.add(emailField);
        emailField.setBounds(300,90,200,30);
        signUpPanel.add(usernameLabel);
        usernameLabel.setBounds(100,130,70,30);
        usernameLabel.setFont(new Font("Cascadia Code", Font.PLAIN, 15));
        signUpPanel.add(usernameField);
        usernameField.setBounds(300,130,200,30);
        signUpPanel.add(passwordLabel);
        passwordLabel.setBounds(100,170,70,30);
        passwordLabel.setFont(new Font("Cascadia Code", Font.PLAIN, 15));
        signUpPanel.add(passwordField);
        passwordField.setBounds(300,170,200,30);
        signUpPanel.add(confirmPasswordLabel);
        confirmPasswordLabel.setBounds(100,210,150,30);
        confirmPasswordLabel.setFont(new Font("Cascadia Code", Font.PLAIN, 14));
        signUpPanel.add(confirmPasswordField);
        confirmPasswordField.setBounds(300,210,200,30);
        signUpPanel.add(signButton);
        signButton.setBounds(225,260,150,40);
        signUpPanel.add(loginSwitch);
        loginSwitch.setBounds(210,310,200,30);
        add(signUpPanel);
        setVisible(true);

        signButton.addActionListener(this::actionPerformed);
        loginSwitch.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                dispose();
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String password=String.valueOf(passwordField.getPassword()).trim();
        String confirmPassword=String.valueOf(confirmPasswordField.getPassword()).trim();
        if(!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(null,"Password and Confirm Password are not same!!","Alert",JOptionPane.WARNING_MESSAGE);
        }
        else{
            User user = new User();
            user.setName(nameField.getText().trim());
            user.setUserName(usernameField.getText().trim());
            user.setEmail(emailField.getText().trim());
            user.setPassword(password);
            boolean result = new ZDriveClient().registerPrompt(user);
            if(result){
                dispose();
            }
            else{
                nameField.setText("");
                emailField.setText("");
                usernameField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                JOptionPane.showMessageDialog(null,"Couldn't Registered!!","Alert",JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}