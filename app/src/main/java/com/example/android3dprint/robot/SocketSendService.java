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
                msg.what = socketHandler.socketMessageType.getCommand();
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
                }
                socketHandler.sendMessage(msg);
            } else {
                this.socketHandler.printWriter.println(this.socketHandler.sendMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

    }

    private int GetRobotStatus() throws Exception {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetRobotStatus.getCommand());
        socketHandler.dataOutputStream.writeShort(0);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        short dataLength = Short.reverseBytes(socketHandler.dataInputStream.readShort());
        int robotStatus = Integer.reverseBytes(socketHandler.dataInputStream.readInt());
        if ((responseCommand == SocketMessageType.GetRobotStatus.getCommand() + 128) && dataLength == 4) {
            return robotStatus;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d,%d", responseCommand, dataLength, robotStatus));
        }
    }

    private void CloseConnection() throws Exception {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.CloseConnection.getCommand());
        socketHandler.dataOutputStream.writeShort(0);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        short dataLength = Short.reverseBytes(socketHandler.dataInputStream.readShort());
        if ((responseCommand == SocketMessageType.CloseConnection.getCommand() + 128) && dataLength == 0) {
            return;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, dataLength));
        }
    }

    private int GetOperatingMode() throws Exception {
        socketHandler.dataOutputStream.writeByte(SocketMessageType.GetOperatingMode.getCommand());
        socketHandler.dataOutputStream.writeShort(0);
        socketHandler.dataOutputStream.flush();

        int responseCommand = socketHandler.dataInputStream.readByte() & 0xFF;
        short dataLength = Short.reverseBytes(socketHandler.dataInputStream.readShort());
        int operatingMode = Integer.reverseBytes(socketHandler.dataInputStream.readInt());
        if ((responseCommand == SocketMessageType.GetOperatingMode.getCommand() + 128) && dataLength == 4) {
            return operatingMode;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d,%d", responseCommand, dataLength, operatingMode));
        }
    }
}
