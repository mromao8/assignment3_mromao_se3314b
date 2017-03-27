package com.example.michaelsmac.assignment3_mromao_se3314b;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.os.StrictMode;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ImageView myDisplay;
    private ImageButton videoPauseBtn, videoPlayBtn, videoTeardownBtn, videoSetupBtn;
    private Button playBtn, pauseBtn, teardownBtn, setupBtn, connectBtn, exitBtn;
    private EditText serverIP, portNumber, clientResponse, serverResponse;
    private Spinner videoSelection;
    private String videoFileName = "Video 3";
    private Timer videoDisplay;
    private Handler handler;
    private boolean isButtons;
    final Controller _controller = new Controller(this, handler);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        myDisplay = (ImageView) findViewById(R.id.myDisplay);
        pauseBtn = (Button) findViewById(R.id.pauseBtn); //implemented
        playBtn = (Button) findViewById(R.id.playBtn); //implemented
        teardownBtn = (Button) findViewById(R.id.teardownBtn); //implemented
        setupBtn = (Button) findViewById(R.id.setupBtn); //implemented
        videoPauseBtn = (ImageButton) findViewById(R.id.videoPauseBtn); //implemented
        videoPlayBtn = (ImageButton) findViewById(R.id.videoPlayBtn); //implemented
        videoTeardownBtn = (ImageButton) findViewById(R.id.videoTeardownBtn); //implemented
        videoSetupBtn = (ImageButton) findViewById(R.id.videoSetupBtn); //implemented
        connectBtn = (Button) findViewById(R.id.connectBtn); //implemented
        exitBtn = (Button) findViewById(R.id.exitBtn); //implemented
        serverIP = (EditText) findViewById(R.id.serverIP);
        portNumber = (EditText) findViewById(R.id.portNumber);
        videoSelection = (Spinner) findViewById(R.id.videoSelection);


        //Disable functionality of client responses and server responses, as well as unneeded buttons
        disableAllVideoButtons();
        disableSetupButton();
        disablePlayButton();
        disablePauseButton();
        disableTeardownButton();


        //Create a new list of videos to be displayed in dropdown
        final ArrayList<String> spinnerList = new ArrayList<String>();
        spinnerList.add("Video 1");
        spinnerList.add("Video 2");
        spinnerList.add("Video 3");

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerList); //add List to array adapter
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //set the dropdown as a spinner dropdown
        videoSelection.setAdapter(adp1); //connect adapter to spinner

        playBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                _controller.playBtnClicked();
            }
        });

        pauseBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                _controller.pauseBtnClicked();
            }
        });

        teardownBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                _controller.teardownBtnClicked();
            }
        });

        setupBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                _controller.setupBtnClicked();
            }
        });

        videoPauseBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                disableAllVideoButtons();
                _controller.pauseBtnClicked();
            }
        });

        videoPlayBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                disableAllVideoButtons();
                _controller.playBtnClicked();
            }
        });

        videoTeardownBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                disableAllVideoButtons();
                _controller.teardownBtnClicked();
            }
        });

        videoSetupBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                disableAllVideoButtons();
                _controller.setupBtnClicked();
            }
        });

        connectBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                _controller.connectBtnClicked();
            }
        });

        exitBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        videoSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (videoSelection.getSelectedItem() == "Video 1") {

                } else if (videoSelection.getSelectedItem() == "Video 2") {

                } else {

                }
            }
        });

        myDisplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!isButtons)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            enableAllVideoButtons();
                        }
                    }).run();

                    isButtons = true;

                }

                else
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            disableAllVideoButtons();
                        }
                    }).run();

                    isButtons = false;
                }

            }


        });

    }

    public void enableVideoName() {
        videoSelection.setEnabled(true);
    }

    public void disableVideoName() {
        videoSelection.setEnabled(false);
    }

    public String getVideoFileName() {
        return this.videoFileName;
    }

    public void setVideoFileName(String filename) {
        this.videoFileName = filename;
    }

    public void startDisplayTimer() {
        this.videoDisplay = new Timer();
        videoDisplay.schedule(new DisplayTask(), 0, 100);
    }

    class DisplayTask extends TimerTask {

        @Override
        public void run() {
            _controller.timerFunction();
        }
    }

    public void stopDisplayTimer() {
        this.videoDisplay.cancel();
    }

    public void displayView(Bitmap myImage) {
        myDisplay.setImageBitmap(myImage);
    }

    public int getServerPort() {
        Log.v("this tag", portNumber.getText().toString());
        return Integer.parseInt(portNumber.getText().toString());
    }

    public String getServerIP() {
        Log.v("this tag", serverIP.getText().toString());
        return serverIP.getText().toString();
    }

    public void enableSetupButton() {
        setupBtn.setEnabled(true);
    }

    public void enablePauseButton() {
        pauseBtn.setEnabled(true);
    }

    public void enablePlayButton() {
        playBtn.setEnabled(true);
    }

    public void enableTeardownButton() {
        teardownBtn.setEnabled(true);
    }

    public void disableSetupButton() {
        setupBtn.setEnabled(false);
    }

    public void disablePauseButton() {
        pauseBtn.setEnabled(false);
    }

    public void disablePlayButton() {
        playBtn.setEnabled(false);
    }

    public void disableTeardownButton() {
        teardownBtn.setEnabled(false);
    }

    public void enableVideoPlayButton() {
        videoPlayBtn.setEnabled(true);
        videoPlayBtn.setVisibility(View.VISIBLE);
    }

    public void enableVideoPauseButton() {
        videoPauseBtn.setEnabled(true);
        videoPauseBtn.setVisibility(View.VISIBLE);
    }

    public void enableVideoTeardownButton() {
        videoTeardownBtn.setEnabled(true);
        videoTeardownBtn.setVisibility(View.VISIBLE);
    }

    public void enableVideoSetupButton() {
        videoSetupBtn.setEnabled(true);
        videoSetupBtn.setVisibility(View.VISIBLE);
    }

    public void disableVideoSetupButton() {
        videoSetupBtn.setEnabled(false);
        videoSetupBtn.setVisibility(View.INVISIBLE);
    }

    public void disableVideoPlayButton() {
        videoPlayBtn.setEnabled(false);
        videoPlayBtn.setVisibility(View.INVISIBLE);
    }

    public void disableVideoPauseButton() {
        videoPauseBtn.setEnabled(false);
        videoPauseBtn.setVisibility(View.INVISIBLE);
    }

    public void disableVideoTeardownButton() {
        videoTeardownBtn.setEnabled(false);
        videoTeardownBtn.setVisibility(View.INVISIBLE);
    }

    public void disableConnectButton() {
        connectBtn.setEnabled(false);
    }

    public void enableAllVideoButtons() {
        enableVideoPlayButton();
        enableVideoPauseButton();
        enableVideoSetupButton();
        enableVideoTeardownButton();
    }

    public void disableAllVideoButtons() {
        disableVideoPlayButton();
        disableVideoPauseButton();
        disableVideoSetupButton();
        disableVideoTeardownButton();
    }

}
