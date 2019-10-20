package com.example.android3dprint.robot;

public enum SocketMessageType {
    GetRobotStatus (0),

    GetOperatingMode (5),
    GetRunMode (6),
    GetSignalDo (7),
    GetSignalAo (8),
    GetSignalGo (9),
    GetSignalDi (10),
    GetSignalAi (11),
    GetSignalGi (12),

    Set3DModelName (32),
    SetEngravingPoint (33),
    SetEngraving3DModelNames (34),
    SetEngravingPointEnd (35),

    SetSignalDo (39),
    SetSignalAo (40),
    SetSignalGo (41)
    ;
    private final int command;

    public int getCommand() {
        return command;
    }

    SocketMessageType(int command) {
        this.command = command;
    }
}
