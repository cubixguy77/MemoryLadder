package com.Tester;

import java.util.HashMap;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
//import com.MemoryLadderFull.R;

public class SoundManager {
 
 private  SoundPool mSoundPool; 
 private  HashMap<Integer, Integer> mSoundPoolMap; 
 private  AudioManager  mAudioManager;
 private  Context mContext;
 
 
 public SoundManager(Context mContext) {
   this.mContext = mContext;
   initSounds();
 }
 
 public void initSounds() {
   mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0); 
   mSoundPoolMap = new HashMap<Integer, Integer>(); 
   mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);       
   
   addSound(0, R.raw.sound0);
   addSound(1, R.raw.sound1);
   addSound(2, R.raw.sound2);
   addSound(3, R.raw.sound3);
   addSound(4, R.raw.sound4);
   addSound(5, R.raw.sound5);
   addSound(6, R.raw.sound6);
   addSound(7, R.raw.sound7);
   addSound(8, R.raw.sound8);
   addSound(9, R.raw.sound9);
 } 
 
 public void addSound(int Index, int SoundID) {
   mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 2));
 }
 
 public void playSound(int index) {
   int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
   mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f); 
 }
 
}