package com.example.myaudio;

import static android.Manifest.permission.RECORD_AUDIO;
//import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    BarVisualizer v;
    int t;
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    boolean recordingActive = false;

    private boolean permissionToRecordAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = (Button) findViewById(R.id.button);
        buttonStop = (Button) findViewById(R.id.button2);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3);
        buttonStopPlayingRecording = (Button) findViewById(R.id.button4);
        v= findViewById(R.id.bar);
        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        requestPermission();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioSavePathInDevice = getExternalCacheDir().getAbsolutePath();
                AudioSavePathInDevice += "/audiorecordtest.3gp";
                MediaRecorderReady();
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                recordingActive = false;

            }
        });
        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null){

                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });
        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    int audiosessionId = mediaPlayer.getAudioSessionId();
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                    if(audiosessionId != -1){
                        v.setAudioSessionId(audiosessionId);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();



            }
        });
    }
    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        t=mediaRecorder.getMaxAmplitude();
        System.out.println("AMPLITUDE "+t);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case RequestPermissionCode:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

}





