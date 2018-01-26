package com.MemoryLadder.NumbersChallenges;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

class SoundManager {

    private TextToSpeech speaker;

    SoundManager(Context mContext, final double secondsPerDigit) {
        this.speaker = new TextToSpeech(mContext, status -> {
            if(status != TextToSpeech.ERROR) {
                speaker.setLanguage(Locale.UK);
                speaker.setPitch(.8f);
                if (secondsPerDigit < 1) {
                    speaker.setSpeechRate(2.5f);
                }
            }
        });
    }

    void playSound(int index) {
        speaker.speak(Integer.toString(index), TextToSpeech.QUEUE_FLUSH, null);
        System.out.println("Play: " + index);
    }

    public void stop() {
        speaker.stop();
        speaker.shutdown();
    }
}