package com.example.michaelsmac.assignment3_mromao_se3314b;

import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by MichaelsMac on 2017-03-23.
 */

public class RTSPModel {

    private InetAddress clientIP = null;
    private InetAddress serverIP = null;
    private int rtspPort;
    private Socket rtspSocket;
    private boolean isConnected;


    public RTSPModel(InetAddress ip, int port)
    {

            this.rtspSocket = new Socket();
            this.serverIP = ip;
            this.rtspPort = port;
            SocketAddress mySocket = new InetSocketAddress(this.serverIP, this.rtspPort);


        try {

            this.rtspSocket.connect(mySocket,10000); //attempt to connect to the socket
            if(this.rtspSocket.isConnected())
            {
                isConnected = true;
            }

            else {

                isConnected = false;
            }
        }

        catch(java.io.IOException e) { //catch if there's an exception

        }

        this.clientIP = this.rtspSocket.getLocalAddress();


    }

    public InetAddress getClientIP()
    {
        return this.clientIP;
    }

    public void setClientIP(InetAddress ip)
    {
        this.clientIP=ip;
    }

    public boolean getConnection()
    {
        if(isConnected)
        {
            return true;
        }

        else
            return false;
    }

    public String receiveMessage()
    {
        byte [] bufferArray = new byte[2048];
        int count = 0;
        String message = null;

        try {
            InputStream myInputStream = this.rtspSocket.getInputStream();
            InputStreamReader myInputStreamReader = new InputStreamReader(myInputStream);
            BufferedReader myBufferedReader = new BufferedReader(myInputStreamReader);
            message = myBufferedReader.readLine();
        }

        catch (java.io.IOException e)
        {

        }

        return message;
    }

    public void sendMessage(String msg)
    {
       try {
           OutputStream myOutputStream = this.rtspSocket.getOutputStream();
           OutputStreamWriter myOutputStreamWriter = new OutputStreamWriter(myOutputStream);
           BufferedWriter myBufferedWriter = new BufferedWriter(myOutputStreamWriter);
           myBufferedWriter.write(msg);
           myBufferedWriter.flush();
       }

       catch (java.io.IOException e)
       {

       }
    }

    public void close()
    {
        try {
            this.rtspSocket.close();
        }

        catch (java.io.IOException e)
        {

        }
    }

}
