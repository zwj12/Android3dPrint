package com.example.android3dprint.robot;

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
}
