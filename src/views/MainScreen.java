package views;

import controller.ZDriveClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class MainScreen extends JFrame implements ActionListener {
    private JPanel itemPanel;
    private JFileChooser fileChooser;
    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem upload;
    private JMenuItem createFolder;
    private JMenuItem exit;
    private File curDirectory;
    private static ImageIcon iconFile=new ImageIcon("C:\\Users\\shark\\IdeaProjects\\ZDriveClient\\src\\ic_file.png");
    private static ImageIcon iconFolder=new ImageIcon("C:\\Users\\shark\\IdeaProjects\\ZDriveClient\\src\\ic_folder.png");
    private static Font font=new Font("Cascadia Code", Font.BOLD, 15);


    public MainScreen(File file){
        setTitle("ZDrive");
        setSize(800,500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        itemPanel=new JPanel(new GridLayout(8,3,5,5));
        menuBar=new JMenuBar();
        fileChooser = new JFileChooser();
        menu=new JMenu("Options");
        upload=new JMenuItem("Upload");
        createFolder=new JMenuItem("Create Folder");
        exit=new JMenuItem("Exit");
        menu.add(upload);
        menu.add(createFolder);
        menu.add(exit);
        menuBar.add(menu);
        upload.addActionListener(this::actionPerformed);
        createFolder.addActionListener(this::actionPerformed);
        exit.addActionListener(this::actionPerformed);
        setJMenuBar(menuBar);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
                    if(!curDirectory.equals(file)){
                        itemPanel.removeAll();
                        setStage(itemPanel,curDirectory.getParentFile());
                    }
                }
            }
        });
        setStage(itemPanel,file);
        add(BorderLayout.CENTER,new JScrollPane(itemPanel));
        setVisible(true);
    }

    private void setStage(JPanel itemPanel, File file) {
        curDirectory=file;
        File[] child=file.listFiles();
        int l=0;
        if(child!=null){
            l=child.length;
        }
        if(l==0){
            JLabel emptyFile=new JLabel("Empty Folder !!");
            emptyFile.setFont(font);
            itemPanel.add(emptyFile);
        }
        JLabel[] childIcons=new JLabel[l];
        HashMap<JLabel,Integer> map=new HashMap<>();
        for(int i=0;i<l;i++){
            childIcons[i]=new JLabel(child[i].getName());
            map.put(childIcons[i],i);
            childIcons[i].setVerticalTextPosition(JLabel.BOTTOM);
            childIcons[i].setFont(font);
            childIcons[i].setOpaque(true);
            childIcons[i].setBackground(Color.CYAN);
            if(child[i].isDirectory()){
                childIcons[i].setIcon(iconFolder);
            }
            else{
                childIcons[i].setIcon(iconFile);
            }
            childIcons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel label= (JLabel) e.getSource();
                    label.setBackground(Color.lightGray);
                    int index=map.get(label);
                    File clicked=child[index];
                    if(clicked.isDirectory()){
                        itemPanel.removeAll();
                        setStage(itemPanel,clicked);
                    }
                    else{
                        try {
                            downloadFile(clicked);
                            label.setBackground(Color.CYAN);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            });
            itemPanel.add(childIcons[i]);
        }
        itemPanel.revalidate();
        itemPanel.repaint();
    }

    private void downloadFile(File file) throws FileNotFoundException {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int response = fileChooser.showOpenDialog(this);
        if (response == JFileChooser.APPROVE_OPTION) {
            File location = fileChooser.getSelectedFile();
            byte data[]=new ZDriveClient().downloadPrompt(location,file);
            if(data.length==file.length()){
                JOptionPane.showMessageDialog(null,"Download Successfull!!","Alert",JOptionPane.WARNING_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null,"Download Failed!!","Alert",JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source=e.getSource();
        if(source.equals(upload)){
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int response = fileChooser.showOpenDialog(this);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                boolean res=new ZDriveClient().uploadPrompt(curDirectory,file);
                if(res){
                    JOptionPane.showMessageDialog(null,"Upload Successfull!!","Alert",JOptionPane.WARNING_MESSAGE);
                    itemPanel.removeAll();
                    setStage(itemPanel,curDirectory);
                }
                else{
                    JOptionPane.showMessageDialog(null,"Upload Failed!!","Alert",JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        else if(source.equals(createFolder)){
            String folderName = JOptionPane.showInputDialog("Enter the folder name ");
            File res = new ZDriveClient().createFolderPrompt(curDirectory,folderName);
            if(res!=null){
                itemPanel.removeAll();
                setStage(itemPanel,res.getParentFile());
            }
        }
        else if(source.equals(exit)){
            setVisible(false);
            dispose();
        }
    }
}
