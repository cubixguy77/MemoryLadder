package com.memoryladder.taketest.numbers.spoken.ui.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.util.SparseIntArray;

import com.mastersofmemory.memoryladder.R;

import java.util.Locale;

public class SoundManager {

    private TextToSpeech speaker;

    /* Legacy fields only */
    private boolean legacy = false;
    private SoundPool mSoundPool;
    private SparseIntArray mSoundPoolMap;
    private AudioManager mAudioManager;

    public SoundManager(Context context, float speechRate, Locale locale) {
        Locale localeToUse = locale != null ? locale : Locale.UK;
        this.speaker = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS && speaker != null) {
                int result = speaker.setLanguage(localeToUse);
                if (locale == Locale.UK || result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    setupLegacySpeaker(context);
                }
                else {
                    speaker.setPitch(.8f);
                    speaker.setSpeechRate(speechRate);
                }
            }
            else {
                setupLegacySpeaker(context);
            }
        });
    }

    public void playSound(int index) {
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

    public boolean isSpeakerActive() {
        return speaker != null;
    }

    private void setupLegacySpeaker(Context context) {
        initLegacySounds(context);
        legacy = true;
    }

    private void initLegacySounds(Context context) {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new SparseIntArray();
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        addLegacySound(0, R.raw.sound0, context);
        addLegacySound(9, R.raw.sound9, context);
        addLegacySound(2, R.raw.sound2, context);
        addLegacySound(3, R.raw.sound3, context);
        addLegacySound(4, R.raw.sound4, context);
        addLegacySound(5, R.raw.sound5, context);
        addLegacySound(6, R.raw.sound6, context);
        addLegacySound(7, R.raw.sound7, context);
        addLegacySound(8, R.raw.sound8, context);
        addLegacySound(1, R.raw.sound1, context);
    }

    private void addLegacySound(int Index, int SoundID, Context context) {
        mSoundPoolMap.put(Index, mSoundPool.load(context, SoundID, 2));
    }

    private void playLegacySound(int index) {
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1.1f);
    }
}