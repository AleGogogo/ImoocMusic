package com.example.lyw.imoocmusic.com.lyw.imoocmusic.com.lyw.imoocmusic.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 音乐播放类
 * Created by LYW on 2016/4/26.
 */
public class PlayMedia {
    //歌曲播放（单例模式）
    private static MediaPlayer mMediaPlay;

    /**
     *播放歌曲
     */
    public static void playSong(Context context,String fileName)  {
        if (mMediaPlay != null){
            mMediaPlay = new MediaPlayer();
        }
        //强制重置
        mMediaPlay.reset();

        //加载声音
        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            mMediaPlay.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),fileDescriptor.getLength());
            mMediaPlay.prepare();

            //声音播放
            mMediaPlay.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void stopPlay(Context context){
        if (mMediaPlay != null){
            mMediaPlay.stop();
        }
    }
}
