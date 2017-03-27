package com.example.michaelsmac.assignment3_mromao_se3314b;

import android.os.AsyncTask;
import android.os.Handler;
import java.net.InetAddress;
import java.util.Random;

import android.util.Log;

/**
 * Created by MichaelsMac on 2017-03-23.
 */



public class Controller {
    private RTPModel _rtpModel = null;
    private RTSPModel _rtspModel = null;
    private RTPPacket _rtpPacket = null;
    private MainActivity _myView = null;
    private String response = null;
    private String serverIP = null;
    private int serverPort = 0;
    private String session = null;
    private String sessionLine = null;
    private int rtpPort = 0;
    private int rtspSeqNum = 0;
    private String currentState = null;
    private String statusLine = null;
    private String VideoName = null;
    private int count = 0;
    private boolean isReady = true;
    private boolean isValid = true;
    private Handler myHandler;

    public Controller(MainActivity view, Handler h)
    {
        _myView = view;
        myHandler = h;

    }

    public void connectBtnClicked()
    {
        try
        {
            this.serverPort= _myView.getServerPort();
            this.serverIP=_myView.getServerIP();


            if(this.serverIP==null || this.serverIP=="")
            {
                isReady = false;
            }

            else if(this.serverPort == 0) {
                isReady = false;
            }

            else {

                isReady = true; //if the connect button is clicked more than once
            }

            if(isReady)
            {
                AsyncTask<Void,Void,Boolean> connectTask = new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void... params) {
                        try {
                            _rtspModel = new RTSPModel(InetAddress.getByName(serverIP), serverPort);
                        }

                        catch(java.net.UnknownHostException e)
                        {

                        }

                        return _rtspModel.getConnection();
                    }

                    @Override
                    protected void onPostExecute(Boolean isConnected)
                    {
                        if(isConnected)
                        {
                            currentState = "";
                            _myView.enableSetupButton();
                            _myView.enableVideoSetupButton();
                            _myView.disableConnectButton();
                            _myView.disablePlayButton();
                            _myView.disableVideoPlayButton();
                            _myView.disablePauseButton();
                            _myView.disableVideoPauseButton();
                            _myView.disableTeardownButton();
                            _myView.disableVideoTeardownButton();
                            Random num = new Random();
                            int myNum = num.nextInt(9000) + 4000;
                            session = Integer.toString(myNum);
                        }

                    }
                };
                connectTask.execute();


            }

        }

        catch(Exception ex)
        {
            Log.v("tag", "This is the exception thrown:", ex);
        }
    }

    public void teardownBtnClicked()
    {
        IncreaseSeqNum();
        AsyncTask<Void, Void, Void> teardownTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                _rtspModel.sendMessage(ConstructMessage("TEARDOWN"));
                if(getServerResponse()!= 200)
                {
                    isValid = false;
                }
                return null;
            }

            protected void onPostExecute()
            {
                if(isValid)
                {
                _myView.stopDisplayTimer();
                currentState="";
                _myView.disableTeardownButton();
                _myView.disableVideoTeardownButton();
                _myView.disablePauseButton();
                _myView.disableVideoPauseButton();
                _myView.disablePlayButton();
                _myView.disableVideoPlayButton();
                _myView.enableVideoName();
                _myView.enableSetupButton();
                _myView.enableVideoSetupButton();
                _rtpModel.close();
                rtspSeqNum = 0;
                }
            }
        }; teardownTask.execute();

    }

    public void setupBtnClicked()
    {
        this.VideoName = _myView.getVideoFileName();
        Random num = new Random();
        this.rtpPort = num.nextInt(4000) + 1000;
        AsyncTask<Void,Void,Void> setupTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if(currentState == "") {
                    rtspSeqNum = 1;
                    _rtspModel.sendMessage(ConstructMessage("SETUP"));
                    if (getServerResponse() != 200) {
                        Log.v("error", "invalid Server response");
                        Log.v("tag","returned with code:"+ Integer.toString(getServerResponse()));
                        isValid = false;
                    }
                    else
                    {
                        Log.v("tag", "we're in here..");
                        _rtpModel = new RTPModel(rtpPort, _rtspModel.getClientIP());
                    }
                }
                return null;
            }

            protected void onPostExecute()
            {

                if(isValid)
                {
                    currentState = "SETUP";
                    _myView.enablePlayButton();
                    _myView.enableVideoPlayButton();
                    _myView.disableSetupButton();
                    _myView.disableVideoSetupButton();
                    _myView.disablePauseButton();
                    _myView.disableVideoPauseButton();
                    _myView.disableVideoName();
                }

            }

        }; setupTask.execute();



    }

    public void pauseBtnClicked()
    {
        IncreaseSeqNum();
        AsyncTask<Void, Void, Void> pauseTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                _rtspModel.sendMessage(ConstructMessage("PAUSE"));
                if (getServerResponse() != 200) {
                    isValid = false;
                }

                return null;
            }

            protected void onPostExecute() {
                if (isValid) {
                    currentState = "PAUSE";
                    _myView.enablePlayButton();
                    _myView.enableVideoPlayButton();
                    _myView.disablePauseButton();
                    _myView.disableVideoPauseButton();
                    _myView.stopDisplayTimer();
                }
            }
        }; pauseTask.execute();

    }

    public void playBtnClicked()
    {
        IncreaseSeqNum();
        AsyncTask<Void, Void, Void> playTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                _rtspModel.sendMessage(ConstructMessage("PLAY"));

                if(getServerResponse()!=200)
                {
                    isValid = false;
                }

                return null;
            }

            protected void onPostExecute()
            {
                if(isValid)
                {
                    _myView.startDisplayTimer();
                    currentState="PLAY";
                    _myView.disablePlayButton();
                    _myView.disableVideoPlayButton();
                    _myView.enableTeardownButton();
                    _myView.enableVideoTeardownButton();
                    _myView.enablePauseButton();
                    _myView.enableVideoPauseButton();
                }
            }

        }; playTask.execute();

    }

    public String ConstructMessage(String CtrlType)
    {
        if(CtrlType == "SETUP")
        {
            String lineOne = "SETUP rtsp://" + this.serverIP + ":" + Integer.toString(this.serverPort) + "/" + this.VideoName + " RTSP/1.0\r\n";
            String lineTwo = "CSeq: 1\r\n";
            String lineThree = "Transport: RTP/UDP; client_port= " + Integer.toString(this.rtpPort) + "\r\n\n";
            String myString = lineOne + lineTwo + lineThree;

            return myString;
        }

        else
        {
            String lineOne = CtrlType + " rtsp://" + this.serverIP + ":" + Integer.toString(this.serverPort) + "/" + this.VideoName + " RTSP/1.0\r\n";
            String lineTwo = "CSeq: " + Integer.toString(this.rtspSeqNum) + "\r\n";
            String lineThree = "Session: " + this.session + "\r\n\n";
            String myString = lineOne + lineTwo + lineThree;

            return myString;

        }
    }

    public int getServerResponse()
    {
        AsyncTask<Void, Integer, String> serverTask = new AsyncTask<Void, Integer, String>() {
            @Override
            protected String doInBackground(Void... params) {
                statusLine = _rtspModel.receiveMessage();

                return statusLine;
            }

            @Override
            protected void onPostExecute(String mySubstring)
            {
                mySubstring = statusLine.substring(statusLine.indexOf(" ") + 1, 3);
                count = Integer.parseInt(mySubstring);
                setServerResponse(count);
            }



        }; serverTask.execute();

        return getPostServerResponse();

    }

    public void setServerResponse(int myCount)
    {
        count = myCount;
    }

    public int getPostServerResponse()
    {
        return count;
    }



    public void IncreaseSeqNum()
    {
        this.rtspSeqNum++;
    }

    public void timerFunction()
    {


        AsyncTask<Void, Void, Boolean> timerTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {

                byte[] byteArray = new byte[2048]; //create new byte array
                byteArray = _rtpModel.receiveBytes(); //set the bytes return from rtp connection to newly created array

                if(byteArray != null) //if the byteArray is not empty
                {
                    _rtpPacket = new RTPPacket(byteArray, byteArray.length);
                    return true;
                }

                else
                    return false;
            }

            protected void onPostExecute(boolean isValid)
            {
                if(!isValid) {
                    _myView.stopDisplayTimer();
                    _myView.enableSetupButton();
                    _myView.enableVideoSetupButton();
                    _myView.enableVideoName();
                    _myView.disablePlayButton();
                    _myView.disableVideoPlayButton();
                    _myView.disablePauseButton();
                    _myView.disableVideoPauseButton();
                    _myView.disableTeardownButton();
                    _myView.disableVideoTeardownButton();
                    rtspSeqNum = 0;
                    _rtpModel.close();
                }

                else
                {
                    myHandler.post(new Runnable() {

                        public void run() {
                        {
                            _myView.displayView(_rtpPacket.ConvertByteToBitmap());
                        }
                    };
                });
            }
            }
        }; timerTask.execute();

    }

}
