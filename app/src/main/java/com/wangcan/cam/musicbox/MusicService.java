package com.wangcan.cam.musicbox;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {

    MediaPlayer player;
    AssetManager manager;
    String [] musics = {"beautiful.mp3","promise.mp3","wish.mp3"};
    public static final int PLAY_STATUS = 0;
    public static final int PAUSE_STATUS = 1;
    public static final int STOP_STATUS = 2;
    private int currentMusicIndex = 0;
    private MusicReceiver receiver;

    private static final String TAG = "CAM";
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");
        manager = getAssets();
        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                currentMusicIndex++;

                currentMusicIndex%=musics.length;
                prepareAndPlayMusic(musics[currentMusicIndex]);
                Intent intent = new Intent();
                intent.setAction(MainActivity.MUSIC_CHANGE_ACTION);
                intent.putExtra(MainActivity.MUSIC_NAME,musics[currentMusicIndex]);
                sendBroadcast(intent);
            }
        });

        prepareAndPlayMusic(musics[currentMusicIndex]);
        receiver = new MusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SERVICE_RECEIVER);
        registerReceiver(receiver,filter);

    }

    private void prepareAndPlayMusic(String music)
    {
        try {
            AssetFileDescriptor asf = manager.openFd(music);
            player.reset();
            player.setDataSource(asf.getFileDescriptor(),asf.getStartOffset(),asf.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final String ACTION_SERVICE_RECEIVER = "ACTION_MUSIC_RECEIVER_RECEIVER_CAM";
    public static final String CODE = "RECEIVER_CODE_CAM";
    public class MusicReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int code = intent.getIntExtra(CODE,PLAY_STATUS);
            switch (code)
            {
                case PLAY_STATUS:
                    player.start();
                    break;
                case STOP_STATUS:
                    player.stop();
                    break;
                case PAUSE_STATUS:
                    player.pause();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
