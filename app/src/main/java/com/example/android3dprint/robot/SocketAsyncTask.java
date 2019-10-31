package com.example.android3dprint.robot;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.example.android3dprint.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketAsyncTask extends AsyncTask<byte[], Integer, byte[]> {
    private static final String TAG = "SocketAsyncTask";
    private static final int connectTimeOut = 10000;
    private static final int soTimeOut = 0;

    private static String HOST = "10.0.2.2";
    private static int PORT = 3003;
    private static Socket socket = new Socket();
    private static OutputStream outputStream;
    private static InputStream inputStream;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    private MainActivity activity;

    public SocketAsyncTask(String HOST, int PORT, MainActivity activity) {
        SocketAsyncTask.HOST = HOST;
        SocketAsyncTask.PORT = PORT;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute in UI thread");
    }

    @Override
    protected byte[] doInBackground(byte[]... sendBytes) {
        ByteArrayInputStream requestBAIS = new ByteArrayInputStream(sendBytes[0]);
        DataInputStream requestDIS = new DataInputStream(requestBAIS);
        ByteArrayOutputStream requestBAOS = new ByteArrayOutputStream(1024);
        DataOutputStream requestDOS = new DataOutputStream(requestBAOS);
        byte[] receiveBytes = new byte[1024];

        try {
            connectToRobot();

            requestDIS.skipBytes(1);
            int requestDataLength = requestDIS.readShort();
            dataOutputStream.write(sendBytes[0], 0, requestDataLength + 3);
            dataOutputStream.flush();

            requestDOS.writeByte(dataInputStream.readByte());
            int dataLength = dataInputStream.readShort();
            requestDOS.writeShort(dataLength);
            dataInputStream.read(receiveBytes, 0, dataLength);
            requestDOS.write(receiveBytes, 0, dataLength);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        Log.d(TAG, String.format("doInBackground in SocketAsyncTask thread"));
        return requestBAOS.toByteArray();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.d(TAG, String.format("onProgressUpdate in UI thread, %d", values[0]));
    }

    @Override
    protected void onPostExecute(byte[] receiveBytes) {
        super.onPostExecute(receiveBytes);
        SocketMessageType socketMessageType = SocketMessageType.GetOperatingMode;
        try {
            int intOperatingMode = socketMessageType.GetOperatingMode(receiveBytes);
            Log.d(TAG, String.format("onPostExecute in UI thread, %d", intOperatingMode));
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void connectToRobot() throws IOException {
        if (!socket.isConnected() || socket.isClosed()) {
            SocketAddress endpoint = new InetSocketAddress(HOST, PORT);
            socket.connect(endpoint, connectTimeOut);
            socket.setSoTimeout(soTimeOut);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(outputStream));
            Log.d(TAG, "The robot is connected");
        }
    }

}
