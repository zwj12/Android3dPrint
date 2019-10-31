package com.example.android3dprint.robot;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public enum SocketMessageType {
    CloseConnection (0),
    GetOperatingMode (1),
    GetRunMode (2),
    GetRobotStatus (7),

    GetSignalDo (8),
    GetSignalAo (9),
    GetSignalGo (10),
    GetSignalDi (11),
    GetSignalAi (12),
    GetSignalGi (13),

    SetSignalDo (16),
    SetSignalAo (17),
    SetSignalGo (18),
    ;
    private final int command;

    public int getCommand() {
        return command;
    }

    SocketMessageType(int command) {
        this.command = command;
    }

    public int GetOperatingMode (byte[] receiveBytes) throws Exception {
        ByteArrayInputStream requestBAIS = new ByteArrayInputStream(receiveBytes);
        DataInputStream requestDIS = new DataInputStream(requestBAIS);
        int responseCommand = requestDIS.readByte() & 0xFF;
        int requestDataLength = requestDIS.readShort();

        if ((responseCommand == SocketMessageType.GetOperatingMode.getCommand() + 128) && requestDataLength == 4) {
            int operatingMode = requestDIS.readInt();
            return operatingMode;
        } else {
            throw new Exception(String.format("Wrong Protocol : %d,%d", responseCommand, requestDataLength));
        }
    }
}
