package com.example.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
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

    private AssetManager mAssetManager;

    private List<Sound> mSounds = new ArrayList<Sound>();

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();

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

            String assetsPath = SOUNDS_FOLDER + "/" + fileName;
            Sound sound = new Sound(assetsPath);

            mSounds.add(sound);

        }

    }

    public List<Sound> getSounds() {
        return mSounds;
    }
}
