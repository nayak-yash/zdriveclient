package views;

import controller.ZDriveClient;
import models.File;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainScreen extends JFrame implements ActionListener {
    private JPanel itemPanel;
    private JFileChooser fileChooser;
    private long userId;
    private JButton uploadButton;
    private ZDriveClient zDriveClient;

    public MainScreen(long userId){
        zDriveClient=new ZDriveClient();
        this.userId=userId;
        setTitle("ZDrive");
        setSize(800,500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        itemPanel=new JPanel();
        uploadButton=new JButton("Upload");
        uploadButton.addActionListener(this::actionPerformed);
        itemPanel.add(uploadButton);
        uploadButton.setBounds(350,220,100,40);
        add(itemPanel);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(this);
        if (response == JFileChooser.APPROVE_OPTION) {
            java.io.File file = new java.io.File(fileChooser.getSelectedFile().getAbsolutePath());
            byte data[]=new byte[(int) file.length()];
            try {
                FileInputStream fileReader=new FileInputStream(file);
                fileReader.read(data);
                File model=new File();
                model.setName(file.getName());
                model.setData(data);
                model.setCreatedAt(System.currentTimeMillis());
                model.setType(model.getName().substring(model.getName().lastIndexOf('.')+1));
                boolean result = zDriveClient.uploadPrompt(userId,model);
                if(result){
                    JOptionPane.showMessageDialog(null,"Upload Successfull!!","Alert",JOptionPane.WARNING_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null,"Upload Failed!!","Alert",JOptionPane.WARNING_MESSAGE);
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
