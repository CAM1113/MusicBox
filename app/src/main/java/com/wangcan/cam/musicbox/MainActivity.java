package com.wangcan.cam.musicbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textView;
    Button playButton;
    Button pauseButton;
    Button stopButton;
    ActivityRes receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textiew) ;
        playButton = findViewById(R.id.playBtn);
        stopButton = findViewById(R.id.stopBtn);
        pauseButton = findViewById(R.id.pauseBtn);
        playButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);

        //注册广播接受器
        receiver = new ActivityRes();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MUSIC_CHANGE_ACTION);
        registerReceiver(receiver,filter);

        Intent intent = new Intent(MainActivity.this,MusicService.class);
        startService(intent);
    }

    public static final String MUSIC_CHANGE_ACTION = "MUSIC_CHANGE_ACTION_CAM";
    public static final String MUSIC_NAME = "music_name_cam_";

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setAction(MusicService.ACTION_SERVICE_RECEIVER);
        switch (view.getId())
        {
            case R.id.pauseBtn:
                intent.putExtra(MusicService.CODE,MusicService.PAUSE_STATUS);
                break;
            case R.id.stopBtn:
                intent.putExtra(MusicService.CODE,MusicService.STOP_STATUS);
                break;
            case R.id.playBtn:
                intent.putExtra(MusicService.CODE,MusicService.PLAY_STATUS);
                break;
        }
        sendBroadcast(intent);
    }

    public class ActivityRes extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Toast.makeText(context, "new music", Toast.LENGTH_SHORT).show();
            String s = intent.getStringExtra(MUSIC_NAME);
            textView.setText(s);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
}
