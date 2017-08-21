package com.umer.andriod.mediaandcamera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.util.Random;



public class MainActivity extends AppCompatActivity  implements  View.OnClickListener {

    Button  btn_Record , btn_Play , btn_Stop , btn_StopPlayingRecording ;
    String AudioSavePathInDevice  = null;
    MediaRecorder mediaRecorder;
    Context mContext;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static  final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        btn_Record = (Button)findViewById(R.id.btn_record);
        btn_Stop = (Button)findViewById(R.id.btn_stop);
        btn_Play = (Button)findViewById(R.id.btn_play);
        btn_StopPlayingRecording = (Button)findViewById(R.id.btn_stopplayingrecording);

        btn_Record.setOnClickListener(this);
        btn_Stop.setOnClickListener(this);
        btn_Play.setOnClickListener(this);
        btn_StopPlayingRecording.setOnClickListener(this);

        //Not Enabled Button
        btn_Stop.setEnabled(false);
        btn_Play.setEnabled(false);
        btn_StopPlayingRecording.setEnabled(false);

        //Random Class Instance
        random = new Random();


    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_record:

                if(checkPermission())
                {
                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    btn_Record.setEnabled(false);
                    btn_Stop.setEnabled(true);

                    Toast.makeText(MainActivity.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
                break;



            case R.id.btn_stop:

                mediaRecorder.stop();
                btn_Stop.setEnabled(false);
                btn_Play.setEnabled(true);
                btn_Record.setEnabled(true);
                btn_StopPlayingRecording.setEnabled(false);

                Toast.makeText(MainActivity.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
                break;

            case R.id.btn_play:
                btn_Stop.setEnabled(false);
                btn_Record.setEnabled(false);
                btn_StopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(MainActivity.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();

                break;
            case R.id.btn_stopplayingrecording:
                btn_Stop.setEnabled(false);
                btn_Record.setEnabled(true);
                btn_StopPlayingRecording.setEnabled(false);
                btn_Play.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            break;


        }
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }


    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    }
                    else  {
                          Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

}
