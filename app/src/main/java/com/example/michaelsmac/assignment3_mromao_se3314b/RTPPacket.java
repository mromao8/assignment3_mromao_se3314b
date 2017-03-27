package com.example.michaelsmac.assignment3_mromao_se3314b;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * Created by MichaelsMac on 2017-03-23.
 */

public class RTPPacket {

    public int payloadType = 0;
    public int Marker = 0;
    public int CC = 0;
    public int Extension = 0;
    public byte[] headerArray;
    public int Padding = 0;
    public byte[] payloadArray;
    public int payloadSize;
    public int SSRC = 0;
    public int timeStamp;
    public int sequenceNum;
    public int Version = 2;

    public RTPPacket(byte[] rtpPacket, int packetSize) {
        this.headerArray = new byte[12];

        for (int i = 0; i < 12; i++) {
            this.headerArray[i] = rtpPacket[i];
        }

        this.payloadSize = packetSize - 12;
        this.payloadArray = new byte[payloadSize];

        for (int i = 12; i < packetSize; i++) {
            this.payloadArray[i - 12] = rtpPacket[i];
        }

        this.payloadType = this.headerArray[1] & 127;
        this.sequenceNum = ConvertToUnsigned(this.headerArray[3]) + (0x100 * ConvertToUnsigned(this.headerArray[2]));

        }

    public int ConvertToUnsigned(int myInt)
    {
        if(myInt >= 0)
        {
            return myInt;
        }

        else
        {
            return (0x100 + myInt);
        }


    }

    public int getPacketLength()
    {
        return this.payloadSize + 12;
    }

    public int getCurrentPackSize(byte [] myPacketArray) {

        for(int i = 0; i < 12; i++)
        {
            myPacketArray[i] = this.headerArray[i];
        }

        for(int j = 0; j < this.payloadSize ; j++)
        {
            myPacketArray[j + 12] = this.payloadArray[j];
        }

        return (this.payloadSize + 12);

    }

    public int getPayloadLength()
    {
        return this.payloadSize;
    }

    public int getSequenceNumber()
    {
        return this.sequenceNum;
    }

    public int getPayloadType()
    {
        return this.payloadType;
    }

    public int getTimeStamp()
    {
        return this.timeStamp;
    }

    public int getCurrentPayloadSize(byte[] myPayloadArray)
    {
        for(int i = 0; i < this.payloadSize ; i++)
        {
            myPayloadArray[i] = this.payloadArray[i];
        }

        return this.payloadSize;
    }

    public Bitmap ConvertByteToBitmap()
    {
        Bitmap myBitmap;
        byte[] byteArray = new byte[this.getPayloadLength()];
        this.getCurrentPayloadSize(byteArray);
        myBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        return myBitmap;
    }






}
