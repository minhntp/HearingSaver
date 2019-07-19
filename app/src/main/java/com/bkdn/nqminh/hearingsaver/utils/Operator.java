package com.bkdn.nqminh.hearingsaver.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import com.bkdn.nqminh.hearingsaver.application.HearingSaver;

public class Operator {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AudioManager audioManager;

    private static Operator instance;

    static public Operator getInstance() {
        if (instance == null) {
            instance = new Operator();
            instance.sharedPreferences = HearingSaver.getAppContext().getSharedPreferences(Constants.settingsData, Context.MODE_PRIVATE);
            instance.editor = instance.sharedPreferences.edit();
            instance.audioManager = (AudioManager) HearingSaver.getAppContext().getSystemService(Context.AUDIO_SERVICE);
        }
        return instance;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setVolumeExceptMedia(boolean isPlugged) {
        if (isPlugged) {
            if (sharedPreferences.getBoolean(Constants.check1, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_RING,
                        sharedPreferences.getInt(Constants.volume1, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.check3, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_NOTIFICATION,
                        sharedPreferences.getInt(Constants.volume3, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.check5, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_SYSTEM,
                        sharedPreferences.getInt(Constants.volume5, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        } else {
            if (sharedPreferences.getBoolean(Constants.check2, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_RING,
                        sharedPreferences.getInt(Constants.volume2, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.check4, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_NOTIFICATION,
                        sharedPreferences.getInt(Constants.volume4, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.check6, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_SYSTEM,
                        sharedPreferences.getInt(Constants.volume6, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        }
    }

    public void setMediaVolume(boolean isPlugged) {
        if (isPlugged) {
            if (sharedPreferences.getBoolean(Constants.check7, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        sharedPreferences.getInt(Constants.volume7, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        } else {
            if (sharedPreferences.getBoolean(Constants.check8, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        sharedPreferences.getInt(Constants.volume8, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        }
    }

    public void setPending(boolean isPending, boolean isPlugged) {
        editor.putBoolean(Constants.settingsPending, isPending);
        editor.putBoolean(Constants.settingsPendingIsPlugged, isPlugged);
        editor.commit();
    }
}
