package com.example.michaelsmac.assignment3_mromao_se3314b;

import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by MichaelsMac on 2017-03-23.
 */

public class RTPModel {

    DatagramSocket rtpSocket;
    byte [] receivePackets = new byte[100000];
    DatagramPacket myPackets = new DatagramPacket(receivePackets, receivePackets.length);


    public RTPModel(int port, InetAddress ip ) {
        try {

            this.rtpSocket = new DatagramSocket(port, ip); //Creates and binds the socket to the port and IP specified
            this.rtpSocket.setSoTimeout(1000);
        }

        catch(SocketException e) //catches any socket exception
        {
        
        }
    }

    public void close()
    {
        try {
            this.rtpSocket.close();
        }

        catch (Exception e) {

        }

    }

    public byte[] receiveBytes()
    {
        try {
            this.rtpSocket.receive(myPackets);
        }

        catch(java.io.IOException e) {

        }

        String returnMessage = new String(myPackets.getData(), 0 , myPackets.getLength());
        return returnMessage.getBytes();
    }

}
