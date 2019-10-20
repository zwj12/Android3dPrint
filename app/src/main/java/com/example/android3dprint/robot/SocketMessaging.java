package com.example.android3dprint.robot;

import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketMessaging {
    private static Socket socket;
    private static  OutputStream outputStream;
    private static  InputStream inputStream ;

    public void connect (String strIPAddress, int intPort)throws Exception
    {
        Log.d("Michael","The socket is connecting");
        socket= new Socket(strIPAddress, intPort);
        Log.d("Michael","The socket is created");
        outputStream = socket.getOutputStream();
        Log.d("Michael","The outputStream is connected");
        inputStream = socket.getInputStream();
        Log.d("Michael","The inputStream is connected");
    }

    public void close()throws Exception
    {
        socket.shutdownOutput();
        socket.shutdownInput();
        outputStream.close();
        inputStream.close();
        socket.close();
    }

    public int getRobotStatus()throws  Exception
    {
        outputStream.write(SocketMessageType.GetRobotStatus.getCommand());
        outputStream.write(0);
        outputStream.write(0);
        outputStream.flush();

        int responseCommand=inputStream.read();
        int messageLength=inputStream.read();
        messageLength= inputStream.read()<<8 + messageLength;
        byte[] message=new byte[4];
        int robotStatus=-1;
        if(messageLength==4){
            inputStream.read(message);
            robotStatus=message[0]+message[1]<<8+message[2]<<16+message[3]<<24;
        }
        return  robotStatus;
    }
}
