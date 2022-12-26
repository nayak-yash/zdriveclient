package controller;

import models.User;

import java.io.*;
import java.net.Socket;

public class ZDriveClient {
    private int REQUEST_CODE ;
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    public ZDriveClient(){
        try {
            socket=new Socket("localhost",6969);
            os=socket.getOutputStream();
            is=socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public File loginPrompt(String username,String password){
        REQUEST_CODE=1;
        File f = null;
        try {
            DataOutputStream dataOutputStream=new DataOutputStream(os);
            dataOutputStream.writeInt(REQUEST_CODE);
            dataOutputStream.writeUTF(username);
            dataOutputStream.flush();
            dataOutputStream.writeUTF(password);
            DataInputStream dataInputStream=new DataInputStream(is);
            Boolean res=dataInputStream.readBoolean();
            if (!res) {
                return null;
            }
            ObjectInputStream ois=new ObjectInputStream(is);
            f= (File) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }
    public boolean registerPrompt(User user){
        boolean result=false;
        REQUEST_CODE=2;
        try {
            DataOutputStream dataOutputStream=new DataOutputStream(os);
            dataOutputStream.writeInt(REQUEST_CODE);
            ObjectOutputStream out=new ObjectOutputStream(os);
            out.writeObject(user);
            out.flush();
            DataInputStream dataInputStream=new DataInputStream(is);
            result =dataInputStream.readBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean uploadPrompt(File curDirectory, File file){
        boolean result=false;
        int len=(int)file.length();
        byte data[]=new byte[len];
        REQUEST_CODE=3;
        try {
            DataOutputStream dataOutputStream=new DataOutputStream(os);
            DataInputStream dataInputStream=new DataInputStream(is);
            dataOutputStream.writeInt(REQUEST_CODE);
            dataOutputStream.writeUTF(file.getName());
            dataOutputStream.writeInt(len);
            FileInputStream fis=new FileInputStream(file);
            fis.read(data,0,len);
            dataOutputStream.write(data);
            dataOutputStream.flush();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(curDirectory);
            oos.flush();
            result=dataInputStream.readBoolean();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public byte[] downloadPrompt(File dir,File file){
        int len=(int)file.length();
        byte data[]=new byte[len];
        REQUEST_CODE=4;
        try {
            DataOutputStream dataOutputStream=new DataOutputStream(os);
            DataInputStream dataInputStream=new DataInputStream(is);
            dataOutputStream.writeInt(REQUEST_CODE);
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(file);
            oos.flush();
            dataInputStream.read(data,0,len);
            File newFile=new File(dir.getAbsolutePath()+"\\"+file.getName());
            FileOutputStream fos=new FileOutputStream(newFile);
            fos.write(data,0,len);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public File createFolderPrompt(File dir, String name){
        File renewed = null;
        REQUEST_CODE=5;
        try {
            DataOutputStream dataOutputStream=new DataOutputStream(os);
            dataOutputStream.writeInt(REQUEST_CODE);
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(dir);
            oos.flush();
            dataOutputStream.writeUTF(name);
            ObjectInputStream ois=new ObjectInputStream(is);
            renewed=(File)ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return renewed;
    }
}
