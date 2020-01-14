package com.example.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.audiofx.LoudnessEnhancer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {

    //日志记录
    private static final String TAG = "BeatBox";

    //声音文件的目录
    private static final String SOUNDS_FOLDER = "sample_sounds";

    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssetManager;

    private List<Sound> mSounds = new ArrayList<Sound>();

    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;

        try {
            soundNames = mAssetManager.list(SOUNDS_FOLDER);
            Log.i(TAG,  "Found " + soundNames.length + " sounds");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not list assets", e);
            return;
        }

        for (String fileName : soundNames) {

            try {
                String assetsPath = SOUNDS_FOLDER + "/" + fileName;
                Sound sound = new Sound(assetsPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Could not load sound " + fileName, e);
            } finally {

            }

        }

    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssetManager.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    //播放音乐
    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release() {
        mSoundPool.release();
    }

    public List<Sound> getSounds() {
        return mSounds;
    }
}
