package com.chs.mt.pxe_r500.MusicBox;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;


public class AudioUtil {

    public static void stopOtherPlayer(Context mContext){
        AudioManager mAm;
        boolean vIsActive=false;
        MyOnAudioFocusChangeListener mListener=null;
        mAm = (AudioManager) mContext.getSystemService(
                Context.AUDIO_SERVICE);
        vIsActive=mAm.isMusicActive();
        mListener = new MyOnAudioFocusChangeListener();
        if(vIsActive){
            int result = mAm.requestAudioFocus(mListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Log.d("AudioUtil", "requestAudioFocus successfully.");
            }
            else {
                Log.d("AudioUtil", "requestAudioFocus failed.");
            }
        }
    }

    public static class MyOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {
            // TODO Auto-generated method stub
            Log.d("AudioUtil", "focusChange="+focusChange);
        }
    }
}
