package com.example.android3dprint.robot;

import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class SocketSendService implements Runnable {
    private static final String TAG = "SocketSendService";
    private SocketHandler socketHandler;

    public SocketSendService(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @Override
    public void run() {
        try {
            if (this.socketHandler.isRawData) {
                Message msg = Message.obtain();
                msg.what = socketHandler.socketMessageType.getRequestCommand();
                switch (socketHandler.socketMessageType) {
                    case GetRobotStatus:
                        msg.arg1 = this.GetRobotStatus();
                        break;
                    case CloseConnection:
                        this.CloseConnection();
                        break;
                    case GetOperatingMode:
                        msg.arg1 = this.GetOperatingMode();
                        break;
                    case GetSignalDo:
                        msg.arg1 = this.GetSignalDo(this.socketHandler.signalName);
                        msg.obj = this.socketHandler.signalName;
                        break;
                    case GetSignalGo:
                        msg.arg1 = this.GetSignalGo(this.socketHandler.signalName);
                        msg.obj = this.socketHandler.signalName;
                        break;
                    case GetSignalAo:
                        msg.obj = this.GetSignalAo(this.socketHandler.signalName);
                        break;
                    case SetSignalDo:
                        this.SetSignalDo(this.socketHandler.signalName);
                        break;
                    case SetSignalGo:
                        this.SetSignalGo(this.socketHandler.signalName);
                        break;
                    case SetSignalAo:
                        this.SetSignalAo(this.socketHandler.signalName);
                        break;
                    case GetSignalDi:
                        msg.arg1 = this.GetSignalDi(this.socketHandler.signalName);
                        msg.obj = this.socketHandler.signalName;
                        break;
                    case GetSignalGi:
                        msg.arg1 = this.GetSignalGi(this.socketHandler.signalName);
                        msg.obj = this.socketHandler.signalName;
                        break;
                    case GetSignalAi:
                        msg.obj = this.GetSignalAi(this.socketHandler.signalName);
                        break;
                }
                socketHandler.sendMessage(msg);
            } else {
                this.socketHandler.printWriter.println(this.socketHandler.sendMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

    }

    private synchronized  int GetRobotStatus() throws Exception {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetRobotStatus.getRequestCommand());
        socketHandler.dataOutputStream.writeShort(0);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        short dataLength = socketHandler.dataInputStream.readShort();
        int robotStatus = socketHandler.dataInputStream.readInt();
        if ((responseCommand == SocketMessageType.GetRobotStatus.getRequestCommand() + 128) && dataLength == 4) {
            return robotStatus;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d,%d", responseCommand, dataLength, robotStatus));
        }
    }

    private synchronized void CloseConnection() throws Exception {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.CloseConnection.getRequestCommand());
        socketHandler.dataOutputStream.writeShort(0);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        short dataLength = socketHandler.dataInputStream.readShort();
        if ((responseCommand == SocketMessageType.CloseConnection.getRequestCommand() + 128) && dataLength == 0) {
            return;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private synchronized  int GetOperatingMode() throws Exception {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetOperatingMode.getRequestCommand());
        socketHandler.dataOutputStream.writeShort(0);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        short dataLength = socketHandler.dataInputStream.readShort();
         if ((responseCommand == SocketMessageType.GetOperatingMode.getRequestCommand() + 128) && dataLength == 4) {
            int operatingMode = socketHandler.dataInputStream.readInt();
            return operatingMode;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private synchronized  int GetRunMode () throws Exception
    {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetRunMode.getRequestCommand());
        socketHandler.dataOutputStream.writeShort(0);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        short dataLength = socketHandler.dataInputStream.readShort();
        if ((responseCommand == SocketMessageType.GetRunMode.getRequestCommand() + 128) && dataLength == 4) {
            int operatingMode = socketHandler.dataInputStream.readInt();
            return operatingMode;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private synchronized  int GetSignalDo (String signalName) throws Exception
    {
        Log.d(TAG,"GetSignalDo :" + signalName);
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetSignalDo.getRequestCommand());
        short dataLength = (short)signalName.length();
        socketHandler.dataOutputStream.writeShort( dataLength);
        socketHandler.dataOutputStream.writeBytes(signalName);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        dataLength = socketHandler.dataInputStream.readShort();
        if ((responseCommand == SocketMessageType.GetSignalDo.getRequestCommand() + 128) && dataLength == 1) {
            int signalDoValue = socketHandler.dataInputStream.readByte();
            return signalDoValue;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private synchronized  int GetSignalGo (String signalName) throws Exception
    {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetSignalGo.getRequestCommand());
        short dataLength = (short)signalName.length();
        socketHandler.dataOutputStream.writeShort( dataLength);
        socketHandler.dataOutputStream.writeBytes(signalName);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        dataLength = socketHandler.dataInputStream.readShort();
        Log.d(TAG, String.format("GetSignalGo:%s, responseCommand:%d, dataLength:%d", signalName,responseCommand,dataLength));
        if ((responseCommand == SocketMessageType.GetSignalGo.getRequestCommand() + 128) && dataLength == 4) {
            int signalValue =socketHandler.dataInputStream.readInt();
            return signalValue;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private synchronized  float GetSignalAo (String signalName) throws Exception
    {
        Log.d(TAG,"GetSignalAo :" + signalName);
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetSignalAo.getRequestCommand());
        short dataLength = (short)signalName.length();
        socketHandler.dataOutputStream.writeShort( dataLength);
        socketHandler.dataOutputStream.writeBytes(signalName);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        dataLength = socketHandler.dataInputStream.readShort();
        if ((responseCommand == SocketMessageType.GetSignalAo.getRequestCommand() + 128) && dataLength == 4) {
            float signalValue =socketHandler.dataInputStream.readFloat();
            return signalValue;
        } else {
            socketHandler.dataInputStream.skipBytes(socketHandler.dataInputStream.available());
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private synchronized void SetSignalDo (String signalName) throws Exception
    {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.SetSignalDo.getRequestCommand());
        short dataLength = (short)(signalName.length()+1);
        socketHandler.dataOutputStream.writeShort( dataLength);
        socketHandler.dataOutputStream.writeByte((int)Math.round(socketHandler.signalValue));
        socketHandler.dataOutputStream.writeBytes(signalName);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        dataLength = socketHandler.dataInputStream.readShort();
        if ((responseCommand == SocketMessageType.SetSignalDo.getRequestCommand() + 128) && dataLength == 0) {
            return ;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private synchronized void SetSignalGo (String signalName) throws Exception
    {
        Log.d(TAG,"SetSignalGo:" +signalName);
        socketHandler.dataOutputStream.writeByte(SocketMessageType.SetSignalGo.getRequestCommand());
        short dataLength = (short)(signalName.length()+4);
        socketHandler.dataOutputStream.writeShort( dataLength);
        socketHandler.dataOutputStream.writeInt((int)Math.round(Math.abs(socketHandler.signalValue)));
        socketHandler.dataOutputStream.writeBytes(signalName);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        dataLength = socketHandler.dataInputStream.readShort();
        if ((responseCommand == SocketMessageType.SetSignalGo.getRequestCommand() + 128) && dataLength == 0) {
            return ;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private synchronized void SetSignalAo (String signalName) throws Exception
    {
        Log.d(TAG,"SetSignalAo:" +signalName);
        socketHandler.dataOutputStream.writeByte(SocketMessageType.SetSignalAo.getRequestCommand());
        short dataLength = (short)(signalName.length()+4);
        socketHandler.dataOutputStream.writeShort( dataLength);
        float signalValue=(float) socketHandler.signalValue;
        socketHandler.dataOutputStream.writeFloat(signalValue);
        socketHandler.dataOutputStream.writeBytes(signalName);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        dataLength = socketHandler.dataInputStream.readShort();
        if ((responseCommand == SocketMessageType.SetSignalAo.getRequestCommand() + 128) && dataLength == 0) {
            return ;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }


    private synchronized  int GetSignalDi (String signalName) throws Exception
    {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetSignalDi.getRequestCommand());
        short dataLength = (short)signalName.length();
        socketHandler.dataOutputStream.writeShort( dataLength);
        socketHandler.dataOutputStream.writeBytes(signalName);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        dataLength = socketHandler.dataInputStream.readShort();
        if ((responseCommand == SocketMessageType.GetSignalDi.getRequestCommand() + 128) && dataLength == 1) {
            int signalDoValue = socketHandler.dataInputStream.readByte();
            return signalDoValue;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private synchronized  int GetSignalGi (String signalName) throws Exception
    {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetSignalGi.getRequestCommand());
        short dataLength = (short)signalName.length();
        socketHandler.dataOutputStream.writeShort( dataLength);
        socketHandler.dataOutputStream.writeBytes(signalName);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        dataLength = socketHandler.dataInputStream.readShort();
        if ((responseCommand == SocketMessageType.GetSignalGi.getRequestCommand() + 128) && dataLength == 4) {
            int signalValue =socketHandler.dataInputStream.readInt();
            return signalValue;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private synchronized  float GetSignalAi (String signalName) throws Exception
    {
        Log.d(TAG,"GetSignalAi :" + signalName);
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetSignalAi.getRequestCommand());
        short dataLength = (short)signalName.length();
        socketHandler.dataOutputStream.writeShort( dataLength);
        socketHandler.dataOutputStream.writeBytes(signalName);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        dataLength = socketHandler.dataInputStream.readShort();
        if ((responseCommand == SocketMessageType.GetSignalAi.getRequestCommand() + 128) && dataLength == 4) {
            float signalValue =socketHandler.dataInputStream.readFloat();
            return signalValue;
        } else {
            socketHandler.dataInputStream.skipBytes(socketHandler.dataInputStream.available());
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }
}
