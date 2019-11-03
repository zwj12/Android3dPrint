package com.example.android3dprint.robot;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public enum SocketMessageType {
    CloseConnection(0, 0, 128, -1),
    GetOperatingMode(1, 0, 129, 4),
    GetRunMode(2, 0, 130, 4),
    GetRobotStatus(7, 0, 135, 4),

    GetSignalDo(8, -1, 136, 1),
    GetSignalGo(9, -1, 137, 4),
    GetSignalAo(10, -1, 138, 4),
    GetSignalDi(11, -1, 139, 1),
    GetSignalGi(12, -1, 140, 4),
    GetSignalAi(13, -1, 141, 4),

    SetSignalDo(16, -1, 144, 0),
    SetSignalGo(17, -1, 145, 0),
    SetSignalAo(18, -1, 146, 0),

    GetNumData(24, -1, 152, 4),
    SetNumData(25, -1, 153, 0),

    Error(-1, 0, 255, 0);

    private static final String TAG = "SocketMessageType";

    private final int requestCommand;
    private final int responseCommand;

    public int getRequestCommand() {
        return requestCommand;
    }

    public int getResponseCommand() {
        return responseCommand;
    }

    //    Not include the header's length, which means the whole socket data length
    //    is requestDataLength+3 or responseDataLength+3
    //    -1 means the data length is alterable
    private int requestDataLength;
    private int responseDataLength;

    public int getRequestDataLength() {
        return requestDataLength;
    }

    public void setRequestDataLength(int requestDataLength) {
        this.requestDataLength = requestDataLength;
    }

    public int getResponseDataLength() {
        return responseDataLength;
    }

    public void setResponseDataLength(int responseDataLength) {
        this.responseDataLength = responseDataLength;
    }

    private String signalName;

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    private double signalValue;

    public double getSignalValue() {
        return signalValue;
    }

    public void setSignalValue(double signalValue) {
        this.signalValue = signalValue;
    }

    private String symbolName;

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    private Object symbolValue;

    public Object getSymbolValue() {
        return symbolValue;
    }

    public void setSymbolValue(Object symbolValue) {
        this.symbolValue = symbolValue;
    }

    private boolean responseError;

    public boolean isResponseError() {
        return responseError;
    }

    public Object responseValue;

    SocketMessageType(int requestCommand, int requestDataLength, int responseCommand, int responseDataLength) {
        this.requestCommand = requestCommand;
        this.requestDataLength = requestDataLength;
        this.responseCommand = responseCommand;
        this.responseDataLength = responseDataLength;
    }

    public byte[] getRequestRawBytes() throws IOException {
        ByteArrayOutputStream requestBAOS = new ByteArrayOutputStream(1024);
        this.packRequestRawBytes(requestBAOS);
//        Log.d(TAG,String.format("%d,%d,%d",this.requestCommand,this.requestDataLength,requestBAOS.toByteArray().length));
        return requestBAOS.toByteArray();
    }

    public void packRequestRawBytes(ByteArrayOutputStream requestBAOS) throws IOException {
        DataOutputStream requestDOS = new DataOutputStream(requestBAOS);
        this.packRequestRawBytes(requestDOS);
    }

    //Don't support Socket stream, only support ByteArrayInputStream or ByteArrayOutputStream
    public void packRequestRawBytes(DataOutputStream requestDOS) throws IOException {
        SocketMessageType socketMessageType = SocketMessageType.valueOf(this.name());
        switch (socketMessageType) {
            case CloseConnection:
            case GetOperatingMode:
            case GetRunMode:
            case GetRobotStatus:
                this.packSocketHeader(requestDOS);
                break;
            case GetSignalDo:
            case GetSignalGo:
            case GetSignalAo:
            case GetSignalDi:
            case GetSignalGi:
            case GetSignalAi:
                this.requestDataLength = signalName.length();
                this.packSocketHeader(requestDOS);
                requestDOS.writeBytes(signalName);
                break;
            case SetSignalDo:
                this.requestDataLength = signalName.length() + 1;
                this.packSocketHeader(requestDOS);
                requestDOS.writeByte((int) Math.round(signalValue));
                requestDOS.writeBytes(signalName);
                break;
            case SetSignalGo:
                this.requestDataLength = signalName.length() + 4;
                this.packSocketHeader(requestDOS);
                requestDOS.writeInt((int) Math.round(Math.abs(signalValue)));
                requestDOS.writeBytes(signalName);
                break;
            case SetSignalAo:
                this.requestDataLength = signalName.length() + 4;
                this.packSocketHeader(requestDOS);
                requestDOS.writeFloat((float) signalValue);
                requestDOS.writeBytes(signalName);
//                Log.d(TAG,String.format("%d,%d",this.requestCommand,this.requestDataLength));
                break;
            case GetNumData:
                this.requestDataLength = symbolName.length();
                this.packSocketHeader(requestDOS);
                requestDOS.writeBytes(symbolName);
                break;
            case SetNumData:
                this.requestDataLength = symbolName.length() + 4;
                this.packSocketHeader(requestDOS);
                requestDOS.writeFloat(Float.parseFloat( symbolValue.toString()));
                requestDOS.writeBytes(symbolName);
                break;
        }
    }

    private void packSocketHeader(DataOutputStream requestDOS) throws IOException {
        requestDOS.writeByte(this.requestCommand);
        requestDOS.writeShort(this.requestDataLength);
    }

    //The length of rawBytes maybe greater than the valid byte data
    //which means that the responseDataLength+3 may be less than the length of the rawBytes
    public int unpackResponseRawBytes(byte[] rawBytes) throws IOException {
        ByteArrayInputStream requestBAIS = new ByteArrayInputStream(rawBytes);
        return this.unpackResponseRawBytes(requestBAIS);
    }

    public int unpackResponseRawBytes(ByteArrayInputStream requestBAIS) throws IOException {
        DataInputStream requestDIS = new DataInputStream(requestBAIS);
        return this.unpackResponseRawBytes(requestDIS);
    }

    //Don't support Socket stream, only support ByteArrayInputStream or ByteArrayOutputStream
    public int unpackResponseRawBytes(DataInputStream requestDIS) throws IOException {
        int responseCommand = requestDIS.readByte() & 0xFF;
        int responseDataLength = requestDIS.readShort();
        if (responseCommand == SocketMessageType.Error.getResponseCommand()) {
            requestDIS.skipBytes(responseDataLength);
            return -1;
        }
        SocketMessageType socketMessageType = SocketMessageType.valueOf(this.name());
        this.responseError=true;
        switch (socketMessageType) {
            case CloseConnection:
            case SetSignalDo:
            case SetSignalGo:
            case SetSignalAo:
            case SetNumData:
                if (responseDataLength != this.getResponseDataLength()) {
                    requestDIS.skipBytes(responseDataLength);
                    this.responseError=false;
                    return -1;
                }
                break;
            case GetOperatingMode:
            case GetRunMode:
            case GetRobotStatus:
                if (responseDataLength != this.getResponseDataLength()) {
                    requestDIS.skipBytes(responseDataLength);
                    this.responseError=false;
                    return -1;
                } else {
                    responseValue = requestDIS.readInt();
                }
                break;
            case GetSignalDo:
            case GetSignalDi:
                if (responseDataLength != this.getResponseDataLength()) {
                    requestDIS.skipBytes(responseDataLength);
                    this.responseError=false;
                    return -1;
                } else {
                    signalValue = requestDIS.readByte();
                    responseValue = signalValue;
                }
                break;
            case GetSignalGo:
            case GetSignalGi:
                if (responseDataLength != this.getResponseDataLength()) {
                    requestDIS.skipBytes(responseDataLength);
                    this.responseError=false;
                    return -1;
                } else {
                    signalValue = requestDIS.readInt();
                    responseValue = signalValue;
                }
                break;
            case GetSignalAo:
            case GetSignalAi:
                if (responseDataLength != this.getResponseDataLength()) {
                    requestDIS.skipBytes(responseDataLength);
                    this.responseError=false;
                    return -1;
                } else {
                    signalValue = requestDIS.readFloat();
                    responseValue = signalValue;
                }
                break;
            case GetNumData:
                if (responseDataLength != this.getResponseDataLength()) {
                    requestDIS.skipBytes(responseDataLength);
                    this.responseError=false;
                    return -1;
                } else {
                    symbolValue = requestDIS.readFloat();
                    responseValue = symbolValue;
                }
                break;
        }
        return 0;
    }

}
