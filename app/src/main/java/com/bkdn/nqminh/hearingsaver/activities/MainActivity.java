package com.bkdn.nqminh.hearingsaver.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.R;
import com.bkdn.nqminh.hearingsaver.services.MyService;
import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class MainActivity extends Activity {
    //    Context context;
    private CheckBox checkboxRingtonePlugged;
    private CheckBox checkboxRingtoneUnpugged;
    private CheckBox checkboxNotificationPlugged;
    private CheckBox checkboxNotificationUnplugged;
    private CheckBox checkboxFeedbackPlugged;
    private CheckBox checkboxFeedbackUnplugged;
    private CheckBox checkboxMediaPlugged;
    private CheckBox checkboxMediaUnplugged;
    private SeekBar seekbarRingtonePlugged;
    private SeekBar seekbarRingtoneUnplugged;
    private SeekBar seekbarNotificationPlugged;
    private SeekBar seekbarNotificationUnplugged;
    private SeekBar seekbarFeedbackPlugged;
    private SeekBar seekbarFeedbackUnplugged;
    private SeekBar seekbarMediaPlugged;
    private SeekBar seekbarMediaUnplugged;
    private TextView volumeRingtonePlugged;
    private TextView volumeRingtoneUnplugged;
    private TextView volumeNotificationPlugged;
    private TextView volumeNotificationUnplugged;
    private TextView volumeFeedbackPlugged;
    private TextView volumeFeedbackUnplugged;
    private TextView volumeMediaPlugged;
    private TextView volumeMediaUnplugged;
    private TextView textViewSettingStatus;
    private TextView textViewServiceStatus;

    private Button buttonEnable;
    private Button buttonDisable;
    private Button buttonSave;

    private Intent mainServiceIntent;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initialize();
        addEvents();
    }

    private void initialize() {

        sharedPreferences = Operator.getInstance(getApplicationContext()).getSharedPreferences();
        editor = Operator.getInstance(getApplicationContext()).getEditor();

        connectViews();

        mainServiceIntent = new Intent(this, MyService.class);

//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(this, 0, mainServiceIntent, 0);
//
//        Notification notification = new Notification.Builder(this, Constants.CHANNEL_ID)
//                .setContentTitle(Constants.NOTIFICATION_TITLE)
//                .setContentText(Constants.NOTIFICATION_MESSAGE)
//                .setSmallIcon(R.drawable.ic_notification)
//                .setContentIntent(pendingIntent)
//                .setTicker(Constants.NOTIFICATION_TICKER)
//                .build();

        boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.IS_SERVICE_ENABLED, false);
        if (isServiceEnabled) {
            startForegroundService(mainServiceIntent);
        }

        setPreviousStatus();
    }

    private void connectViews() {
        checkboxRingtonePlugged = findViewById(R.id.ringtone_plugged_checkbox);
        checkboxRingtoneUnpugged = findViewById(R.id.ringtone_unplugged_checkbox);
        checkboxNotificationPlugged = findViewById(R.id.notification_plugged_checkbox);
        checkboxNotificationUnplugged = findViewById(R.id.notification_unplugged_checkbox);
        checkboxFeedbackPlugged = findViewById(R.id.feedback_plugged_checkbox);
        checkboxFeedbackUnplugged = findViewById(R.id.feedback_unplugged_checkbox);
        checkboxMediaPlugged = findViewById(R.id.media_plugged_checkbox);
        checkboxMediaUnplugged = findViewById(R.id.media_unplugged_checkbox);

        seekbarRingtonePlugged = findViewById(R.id.ringtone_plugged_seekbar);
        seekbarRingtoneUnplugged = findViewById(R.id.ringtone_unplugged_seekbar);
        seekbarNotificationPlugged = findViewById(R.id.notification_plugged_seekbar);
        seekbarNotificationUnplugged = findViewById(R.id.notification_unplugged_seekbar);
        seekbarFeedbackPlugged = findViewById(R.id.feedback_plugged_seekbar);
        seekbarFeedbackUnplugged = findViewById(R.id.feedback_unplugged_seekbar);
        seekbarMediaPlugged = findViewById(R.id.media_plugged_seekbar);
        seekbarMediaUnplugged = findViewById(R.id.media_unplugged_seekbar);

        volumeRingtonePlugged = findViewById(R.id.ringtone_plugged_volume_textview);
        volumeRingtoneUnplugged = findViewById(R.id.ringtone_unplugged_volume_textview);
        volumeNotificationPlugged = findViewById(R.id.notification_plugged_volume_textview);
        volumeNotificationUnplugged = findViewById(R.id.notification_unplugged_volume_textview);
        volumeFeedbackPlugged = findViewById(R.id.feedback_plugged_volume_textview);
        volumeFeedbackUnplugged = findViewById(R.id.feedback_unplugged_volume_textview);
        volumeMediaPlugged = findViewById(R.id.media_plugged_volume_textview);
        volumeMediaUnplugged = findViewById(R.id.media_unplugged_volume_textview);
        textViewSettingStatus = findViewById(R.id.setting_status_textview);
        textViewServiceStatus = findViewById(R.id.service_status_textview);

        buttonEnable = findViewById(R.id.enable_button);
        buttonDisable = findViewById(R.id.disable_button);
        buttonSave = findViewById(R.id.save_button);
    }

//    private void setDefaultOnFirstRun() {
//        if (!sharedPreferences.getBoolean(Constants.firstRun, false)) {
//            editor.putBoolean(Constants.settingsDisabled, true);
//            editor.putBoolean(Constants.firstRun, true);
//            editor.commit();
//        }
//    }

    private void setPreviousStatus() {
        checkboxRingtonePlugged.setChecked(sharedPreferences.getBoolean(Constants.CHECK_1, true));
        checkboxRingtoneUnpugged.setChecked(sharedPreferences.getBoolean(Constants.CHECK_2, true));
        checkboxNotificationPlugged.setChecked(sharedPreferences.getBoolean(Constants.CHECK_3, true));
        checkboxNotificationUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CHECK_4, true));
        checkboxFeedbackPlugged.setChecked(sharedPreferences.getBoolean(Constants.CHECK_5, true));
        checkboxFeedbackUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CHECK_6, true));
        checkboxMediaPlugged.setChecked(sharedPreferences.getBoolean(Constants.CHECK_7, true));
        checkboxMediaUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CHECK_8, true));

        seekbarRingtonePlugged.setProgress(sharedPreferences.getInt(Constants.VOLUME_1, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarRingtoneUnplugged.setProgress(sharedPreferences.getInt(Constants.VOLUME_2, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarNotificationPlugged.setProgress(sharedPreferences.getInt(Constants.VOLUME_3, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarNotificationUnplugged.setProgress(sharedPreferences.getInt(Constants.VOLUME_4, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarFeedbackPlugged.setProgress(sharedPreferences.getInt(Constants.VOLUME_5, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarFeedbackUnplugged.setProgress(sharedPreferences.getInt(Constants.VOLUME_6, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarMediaPlugged.setProgress(sharedPreferences.getInt(Constants.VOLUME_7, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarMediaUnplugged.setProgress(sharedPreferences.getInt(Constants.VOLUME_8, Constants.DEFAULT_VOLUME_SPEAKER));

        volumeRingtonePlugged.setText(String.valueOf(seekbarRingtonePlugged.getProgress()));
        volumeRingtoneUnplugged.setText(String.valueOf(seekbarRingtoneUnplugged.getProgress()));
        volumeNotificationPlugged.setText(String.valueOf(seekbarNotificationPlugged.getProgress()));
        volumeNotificationUnplugged.setText(String.valueOf(seekbarNotificationUnplugged.getProgress()));
        volumeFeedbackPlugged.setText(String.valueOf(seekbarFeedbackPlugged.getProgress()));
        volumeFeedbackUnplugged.setText(String.valueOf(seekbarFeedbackUnplugged.getProgress()));
        volumeMediaPlugged.setText(String.valueOf(seekbarMediaPlugged.getProgress()));
        volumeMediaUnplugged.setText(String.valueOf(seekbarMediaUnplugged.getProgress()));

        handleServiceStateChange();
    }

    private void addEvents() {
        seekbarRingtonePlugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeRingtonePlugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarRingtoneUnplugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeRingtoneUnplugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarNotificationPlugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeNotificationPlugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarNotificationUnplugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeNotificationUnplugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarFeedbackPlugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeFeedbackPlugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarFeedbackUnplugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeFeedbackUnplugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarMediaPlugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeMediaPlugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarMediaUnplugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeMediaUnplugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonEnable.setOnClickListener(view -> {
            saveSettings(true);
            handleServiceStateChange();
            Toast.makeText(this, Constants.TOAST_ENABLE, Toast.LENGTH_SHORT).show();
        });

        buttonDisable.setOnClickListener(view -> {
            saveSettings(false);
            handleServiceStateChange();
            Toast.makeText(this, Constants.TOAST_DISABLE, Toast.LENGTH_SHORT).show();
        });

        buttonSave.setOnClickListener(view -> {
            saveSettings();
            Toast.makeText(this, Constants.SETTINGS_SAVED, Toast.LENGTH_SHORT).show();
        });
    }

    private void handleServiceStateChange() {

        // Check again to see if sharedPreference is set
        boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            textViewSettingStatus.setText(R.string.status_service_enabled);
            textViewSettingStatus.setTextColor(getColor(R.color.blue));
            buttonEnable.setEnabled(false);
            buttonDisable.setEnabled(true);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.FIRST_RUN_0, true);
            editor.putBoolean(Constants.FIRST_RUN_1, true);
            editor.apply();

            startForegroundService(mainServiceIntent);
        } else {
            textViewSettingStatus.setText(R.string.status_service_disabled);
            textViewSettingStatus.setTextColor(Color.RED);
            buttonDisable.setEnabled(false);
            buttonEnable.setEnabled(true);
            stopService(mainServiceIntent);
        }

        if (Operator.getInstance(getApplicationContext()).isServiceRunning()) {
            textViewServiceStatus.setText(R.string.status_service_running);
            textViewServiceStatus.setTextColor(getColor(R.color.blue));
        } else {
            textViewServiceStatus.setText(R.string.status_service_not_running);
            textViewServiceStatus.setTextColor(Color.RED);
        }
    }

    private  void saveSettings() {
        editor.putInt(Constants.VOLUME_1, seekbarRingtonePlugged.getProgress());
        editor.putInt(Constants.VOLUME_2, seekbarRingtoneUnplugged.getProgress());
        editor.putInt(Constants.VOLUME_3, seekbarNotificationPlugged.getProgress());
        editor.putInt(Constants.VOLUME_4, seekbarNotificationUnplugged.getProgress());
        editor.putInt(Constants.VOLUME_5, seekbarFeedbackPlugged.getProgress());
        editor.putInt(Constants.VOLUME_6, seekbarFeedbackUnplugged.getProgress());
        editor.putInt(Constants.VOLUME_7, seekbarMediaPlugged.getProgress());
        editor.putInt(Constants.VOLUME_8, seekbarMediaUnplugged.getProgress());
        editor.putBoolean(Constants.CHECK_1, checkboxRingtonePlugged.isChecked());
        editor.putBoolean(Constants.CHECK_2, checkboxRingtoneUnpugged.isChecked());
        editor.putBoolean(Constants.CHECK_3, checkboxNotificationPlugged.isChecked());
        editor.putBoolean(Constants.CHECK_4, checkboxNotificationUnplugged.isChecked());
        editor.putBoolean(Constants.CHECK_5, checkboxFeedbackPlugged.isChecked());
        editor.putBoolean(Constants.CHECK_6, checkboxFeedbackUnplugged.isChecked());
        editor.putBoolean(Constants.CHECK_7, checkboxMediaPlugged.isChecked());
        editor.putBoolean(Constants.CHECK_8, checkboxMediaUnplugged.isChecked());
        editor.commit();
    }

    private void saveSettings(boolean isServiceEnabled) {
        editor.putBoolean(Constants.IS_SERVICE_ENABLED, isServiceEnabled);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, Constants.TOAST_ACTIVITY_DESTROYED, Toast.LENGTH_SHORT).show();
        stopService(mainServiceIntent);
        super.onDestroy();
    }

}


