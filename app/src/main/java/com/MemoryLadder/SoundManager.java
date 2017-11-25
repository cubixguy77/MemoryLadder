package com.MemoryLadder;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

class SoundManager {

    private TextToSpeech speaker;

    SoundManager(Context mContext) {
        this.speaker = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speaker.setLanguage(Locale.UK);
                    speaker.setPitch(.8f);
                }
            }
        });
    }

    void playSound(int index) {
        speaker.speak(Integer.toString(index), TextToSpeech.QUEUE_FLUSH, null);
        System.out.println("Play: " + index);
    }
}