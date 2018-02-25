package com.MemoryLadder.NumbersChallenges;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.util.SparseIntArray;

import com.mastersofmemory.memoryladder.R;

import java.util.Locale;

class SoundManager {

    private final Context mContext;
    private TextToSpeech speaker;

    /* Legacy fields only */
    private boolean legacy = false;
    private SoundPool mSoundPool;
    private SparseIntArray mSoundPoolMap;
    private AudioManager mAudioManager;

    SoundManager(Context mContext, float speechRate) {
        this.mContext = mContext;
        this.speaker = new TextToSpeech(mContext, status -> {
            if (status == TextToSpeech.SUCCESS && speaker != null) {
                int result = speaker.setLanguage(Locale.UK);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    setupLegacySpeaker();
                }
                else {
                    speaker.setPitch(.8f);
                    speaker.setSpeechRate(speechRate);
                }
            }
            else {
                setupLegacySpeaker();
            }
        });
    }

    void playSound(int index) {
        if (legacy) {
            playLegacySound(index);
        }
        else {
            speaker.speak(Integer.toString(index), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void stop() {
        if (speaker != null) {
            speaker.stop();
            speaker.shutdown();
        }
    }

    private void setupLegacySpeaker() {
        initLegacySounds();
        legacy = true;
    }

    private void initLegacySounds() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new SparseIntArray();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        addLegacySound(0, R.raw.sound0);
        addLegacySound(1, R.raw.sound1);
        addLegacySound(2, R.raw.sound2);
        addLegacySound(3, R.raw.sound3);
        addLegacySound(4, R.raw.sound4);
        addLegacySound(5, R.raw.sound5);
        addLegacySound(6, R.raw.sound6);
        addLegacySound(7, R.raw.sound7);
        addLegacySound(8, R.raw.sound8);
        addLegacySound(9, R.raw.sound9);
    }

    private void addLegacySound(int Index, int SoundID) {
        mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 2));
    }

    private void playLegacySound(int index) {
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1.1f);
    }
}