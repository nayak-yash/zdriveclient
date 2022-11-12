package controller;

import models.User;
import models.File;
import views.LoginScreen;

import java.io.*;
import java.net.Socket;

public class ZDriveClient {
    private int REQUEST_CODE ;
    private Socket socket;
    public ZDriveClient(){
        try {
            socket=new Socket("localhost",6969);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Long loginPrompt(String username,String password){
        REQUEST_CODE=1;
        Long userId=null;
        try {
            DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeInt(REQUEST_CODE);
            dataOutputStream.writeUTF(username);
            dataOutputStream.flush();
            dataOutputStream.writeUTF(password);
            DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
            userId=dataInputStream.readLong();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userId;
    }
    public boolean registerPrompt(User user){
        boolean result=false;
        REQUEST_CODE=2;
        try {
            DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeInt(REQUEST_CODE);
            dataOutputStream.writeUTF(user.getName());
            dataOutputStream.flush();
            dataOutputStream.writeUTF(user.getUserName());
            dataOutputStream.flush();
            dataOutputStream.writeUTF(user.getEmail());
            dataOutputStream.flush();
            dataOutputStream.writeUTF(user.getPassword());
            dataOutputStream.flush();
            DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
            result =dataInputStream.readBoolean();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean uploadPrompt(long userId,File file){
        boolean result=false;
        REQUEST_CODE=3;
        try {
            DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeInt(REQUEST_CODE);
            dataOutputStream.writeLong(userId);
            dataOutputStream.flush();
            dataOutputStream.writeUTF(file.getName());
            dataOutputStream.flush();
            dataOutputStream.writeUTF(file.getType());
            dataOutputStream.flush();
            dataOutputStream.writeLong(file.getCreatedAt());
            dataOutputStream.writeInt(file.getData().length);
            dataOutputStream.flush();
            dataOutputStream.write(file.getData());
            dataOutputStream.flush();
            DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
            result =dataInputStream.readBoolean();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) {
        LoginScreen loginScreen= new LoginScreen();
    }
}
