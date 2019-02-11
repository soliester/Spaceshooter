package com.example.spaceshooter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;

import java.io.IOException;

public class Jukebox  {
    private SoundPool _soundPool = null;
    private static final int MAX_STREAMS = 3;
    static int CRASH;
    static int SCORE;
    static int GAMEOVER;
    static int START;
    static int HIT;
    static int SHOOT;

    Jukebox(Context context){
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        _soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(MAX_STREAMS)
                .build();

        loadSounds(context); // TODO: Experiment!!
    }

    private void loadSounds(Context context) {
        CRASH = _soundPool.load(loadDescriptor(context, "crash.wav"), 1);
        SCORE = _soundPool.load(loadDescriptor(context, "catch.wav"), 1);   // TODO: Refaktorera filnamn
        HIT = _soundPool.load(loadDescriptor(context, "hit.wav"), 1);
        GAMEOVER = _soundPool.load(loadDescriptor(context, "gameover.wav"), 1);
        START = _soundPool.load(loadDescriptor(context, "starting.wav"), 1);
        SHOOT = _soundPool.load(loadDescriptor(context, "shooting.wav"), 1);
    }

    private AssetFileDescriptor loadDescriptor(Context context, String fileName){
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd(fileName); // TODO: Other sounds
            return descriptor;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // TODO: Should probably not do it like this
    }

    void play (final int soundID) {
        final float leftVolume = 1f;
        final float rightVolume = 1f;
        final int priority = 1;
        final int loop = 0;
        final float rate = 1.0f;

        if (soundID > 0) {
            _soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
        }
    }
}
