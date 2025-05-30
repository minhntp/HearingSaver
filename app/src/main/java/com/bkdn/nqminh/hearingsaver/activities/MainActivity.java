package com.bkdn.nqminh.hearingsaver.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bkdn.nqminh.hearingsaver.R;
import com.bkdn.nqminh.hearingsaver.services.MyService;
import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class MainActivity extends FragmentActivity {
    private CheckBox checkboxRingtonePlugged;
    private CheckBox checkboxRingtoneUnplugged;
    private CheckBox checkboxNotificationPlugged;
    private CheckBox checkboxNotificationUnplugged;
    private CheckBox checkboxFeedbackPlugged;
    private CheckBox checkboxFeedbackUnplugged;
    private CheckBox checkboxCallPlugged;
    private CheckBox checkboxCallUnplugged;
    private CheckBox checkboxAlarmPlugged;
    private CheckBox checkboxAlarmUnplugged;
    private CheckBox checkboxMediaPlugged;
    private CheckBox checkboxMediaUnplugged;

    private SeekBar seekbarRingtonePlugged;
    private SeekBar seekbarRingtoneUnplugged;
    private SeekBar seekbarNotificationPlugged;
    private SeekBar seekbarNotificationUnplugged;
    private SeekBar seekbarFeedbackPlugged;
    private SeekBar seekbarFeedbackUnplugged;
    private SeekBar seekbarCallPlugged;
    private SeekBar seekbarCallUnplugged;
    private SeekBar seekbarAlarmPlugged;
    private SeekBar seekbarAlarmUnplugged;
    private SeekBar seekbarMediaPlugged;
    private SeekBar seekbarMediaUnplugged;

    private TextView volumeRingtonePlugged;
    private TextView volumeRingtoneUnplugged;
    private TextView volumeNotificationPlugged;
    private TextView volumeNotificationUnplugged;
    private TextView volumeFeedbackPlugged;
    private TextView volumeFeedbackUnplugged;
    private TextView volumeCallPlugged;
    private TextView volumeCallUnplugged;
    private TextView volumeAlarmPlugged;
    private TextView volumeAlarmUnplugged;
    private TextView volumeMediaPlugged;
    private TextView volumeMediaUnplugged;

    private TextView textViewSettingStatus;
    private TextView textViewServiceStatus;

    private Button buttonEnable;
    private Button buttonDisable;
    private Button buttonSave;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private final ActivityResultLauncher<String> requestNotificationPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            String grantedLog = isGranted ? "is granted" : "is denied";
            Toast.makeText(this, "Notification permission " + grantedLog, Toast.LENGTH_SHORT).show();
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        checkPermissions();
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showStatus();
    }

    private void checkPermissions() {
        // Notification Permission
        int notificationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS);
        if (notificationPermission == PackageManager.PERMISSION_DENIED) {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    private void initialize() {
        connectViews();
        addEvents();

        sharedPreferences = Operator.getInstance(getApplicationContext()).getSharedPreferences();
        editor = Operator.getInstance(getApplicationContext()).getEditor();

        setSavedSettings();

        boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            Intent mainServiceIntent = new Intent(this, MyService.class);
            startForegroundService(mainServiceIntent);
            Log.d(Constants.DEBUG_TAG, "started service from main activity");
        }
    }

    private void connectViews() {
        checkboxRingtonePlugged = findViewById(R.id.ringtone_plugged_checkbox);
        checkboxRingtoneUnplugged = findViewById(R.id.ringtone_unplugged_checkbox);
        checkboxNotificationPlugged = findViewById(R.id.notification_plugged_checkbox);
        checkboxNotificationUnplugged = findViewById(R.id.notification_unplugged_checkbox);
        checkboxFeedbackPlugged = findViewById(R.id.feedback_plugged_checkbox);
        checkboxFeedbackUnplugged = findViewById(R.id.feedback_unplugged_checkbox);
        checkboxCallPlugged = findViewById(R.id.call_plugged_checkbox);
        checkboxCallUnplugged = findViewById(R.id.call_unplugged_checkbox);
        checkboxAlarmPlugged = findViewById(R.id.alarm_plugged_checkbox);
        checkboxAlarmUnplugged = findViewById(R.id.alarm_unplugged_checkbox);
        checkboxMediaPlugged = findViewById(R.id.media_plugged_checkbox);
        checkboxMediaUnplugged = findViewById(R.id.media_unplugged_checkbox);

        seekbarRingtonePlugged = findViewById(R.id.ringtone_plugged_seekbar);
        seekbarRingtoneUnplugged = findViewById(R.id.ringtone_unplugged_seekbar);
        seekbarNotificationPlugged = findViewById(R.id.notification_plugged_seekbar);
        seekbarNotificationUnplugged = findViewById(R.id.notification_unplugged_seekbar);
        seekbarFeedbackPlugged = findViewById(R.id.feedback_plugged_seekbar);
        seekbarFeedbackUnplugged = findViewById(R.id.feedback_unplugged_seekbar);
        seekbarCallPlugged = findViewById(R.id.call_plugged_seekbar);
        seekbarCallUnplugged = findViewById(R.id.call_unplugged_seekbar);
        seekbarAlarmPlugged = findViewById(R.id.alarm_plugged_seekbar);
        seekbarAlarmUnplugged = findViewById(R.id.alarm_unplugged_seekbar);
        seekbarMediaPlugged = findViewById(R.id.media_plugged_seekbar);
        seekbarMediaUnplugged = findViewById(R.id.media_unplugged_seekbar);

        volumeRingtonePlugged = findViewById(R.id.ringtone_plugged_volume_textview);
        volumeRingtoneUnplugged = findViewById(R.id.ringtone_unplugged_volume_textview);
        volumeNotificationPlugged = findViewById(R.id.notification_plugged_volume_textview);
        volumeNotificationUnplugged = findViewById(R.id.notification_unplugged_volume_textview);
        volumeFeedbackPlugged = findViewById(R.id.feedback_plugged_volume_textview);
        volumeFeedbackUnplugged = findViewById(R.id.feedback_unplugged_volume_textview);
        volumeCallPlugged = findViewById(R.id.call_plugged_volume_textview);
        volumeCallUnplugged = findViewById(R.id.call_unplugged_volume_textview);
        volumeAlarmPlugged = findViewById(R.id.alarm_plugged_volume_textview);
        volumeAlarmUnplugged = findViewById(R.id.alarm_unplugged_volume_textview);
        volumeMediaPlugged = findViewById(R.id.media_plugged_volume_textview);
        volumeMediaUnplugged = findViewById(R.id.media_unplugged_volume_textview);

        textViewSettingStatus = findViewById(R.id.setting_status_textview);
        textViewServiceStatus = findViewById(R.id.service_status_textview);

        buttonEnable = findViewById(R.id.enable_button);
        buttonDisable = findViewById(R.id.disable_button);
        buttonSave = findViewById(R.id.save_button);
    }

    private void setSavedSettings() {
        checkboxRingtonePlugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_RING_PLUGGED, true));
        checkboxRingtoneUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_RING_UNPLUGGED, true));
        checkboxNotificationPlugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_NOTIFICATION_PLUGGED, true));
        checkboxNotificationUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_NOTIFICATION_UNPLUGGED, true));
        checkboxFeedbackPlugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_FEEDBACK_PLUGGED, true));
        checkboxFeedbackUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_FEEDBACK_UNPLUGGED, true));
        checkboxCallPlugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_CALL_PLUGGED, true));
        checkboxCallUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_CALL_UNPLUGGED, true));
        checkboxAlarmPlugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_ALARM_PLUGGED, true));
        checkboxAlarmUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_ALARM_UNPLUGGED, true));
        checkboxMediaPlugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_MEDIA_PLUGGED, true));
        checkboxMediaUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CHECKBOX_MEDIA_UNPLUGGED, true));

        seekbarRingtonePlugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_RING_PLUGGED, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarRingtoneUnplugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_RING_UNPLUGGED, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarNotificationPlugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_NOTIFICATION_PLUGGED, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarNotificationUnplugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_NOTIFICATION_UNPLUGGED, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarFeedbackPlugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_FEEDBACK_PLUGGED, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarFeedbackUnplugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_FEEDBACK_UNPLUGGED, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarCallPlugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_CALL_PLUGGED, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarCallUnplugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_CALL_UNPLUGGED, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarAlarmPlugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_ALARM_PLUGGED, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarAlarmUnplugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_ALARM_UNPLUGGED, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarMediaPlugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_MEDIA_PLUGGED, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarMediaUnplugged.setProgress(sharedPreferences.getInt(Constants.SEEKBAR_MEDIA_UNPLUGGED, Constants.DEFAULT_VOLUME_SPEAKER_MEDIA));
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
        seekbarCallPlugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeCallPlugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekbarCallUnplugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeCallUnplugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekbarAlarmPlugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeAlarmPlugged.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekbarAlarmUnplugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeAlarmUnplugged.setText(String.valueOf(progress));
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
            handleServiceStateChange(true);
            Toast.makeText(this, Constants.TOAST_ENABLE, Toast.LENGTH_SHORT).show();
        });

        buttonDisable.setOnClickListener(view -> {
            handleServiceStateChange(false);
            Toast.makeText(this, Constants.TOAST_DISABLE, Toast.LENGTH_SHORT).show();
        });

        buttonSave.setOnClickListener(view -> {
            saveSettings();
            Toast.makeText(this, Constants.SETTINGS_SAVED, Toast.LENGTH_SHORT).show();
        });
    }

    private void handleServiceStateChange(boolean isEnabled) {
        saveSettings();
        saveSettings(isEnabled);
        startOrStopService(isEnabled);
        showStatus();
    }

    private void showStatus() {
        // Check again to see if sharedPreference is set
        boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            textViewSettingStatus.setText(R.string.status_service_enabled);
            textViewSettingStatus.setTextColor(getColor(R.color.blue));
            buttonEnable.setEnabled(false);
            buttonDisable.setEnabled(true);
        } else {
            textViewSettingStatus.setText(R.string.status_service_disabled);
            textViewSettingStatus.setTextColor(Color.RED);
            buttonDisable.setEnabled(false);
            buttonEnable.setEnabled(true);
        }

        if (Operator.getInstance(getApplicationContext()).isServiceRunning()) {
            textViewServiceStatus.setText(R.string.status_service_running);
            textViewServiceStatus.setTextColor(getColor(R.color.blue));
        } else {
            textViewServiceStatus.setText(R.string.status_service_not_running);
            textViewServiceStatus.setTextColor(Color.RED);
        }
    }

    private void saveSettings() {
        editor.putInt(Constants.SEEKBAR_RING_PLUGGED, seekbarRingtonePlugged.getProgress());
        editor.putInt(Constants.SEEKBAR_RING_UNPLUGGED, seekbarRingtoneUnplugged.getProgress());
        editor.putInt(Constants.SEEKBAR_NOTIFICATION_PLUGGED, seekbarNotificationPlugged.getProgress());
        editor.putInt(Constants.SEEKBAR_NOTIFICATION_UNPLUGGED, seekbarNotificationUnplugged.getProgress());
        editor.putInt(Constants.SEEKBAR_FEEDBACK_PLUGGED, seekbarFeedbackPlugged.getProgress());
        editor.putInt(Constants.SEEKBAR_FEEDBACK_UNPLUGGED, seekbarFeedbackUnplugged.getProgress());
        editor.putInt(Constants.SEEKBAR_CALL_PLUGGED, seekbarCallPlugged.getProgress());
        editor.putInt(Constants.SEEKBAR_CALL_UNPLUGGED, seekbarCallUnplugged.getProgress());
        editor.putInt(Constants.SEEKBAR_ALARM_PLUGGED, seekbarAlarmPlugged.getProgress());
        editor.putInt(Constants.SEEKBAR_ALARM_UNPLUGGED, seekbarAlarmUnplugged.getProgress());
        editor.putInt(Constants.SEEKBAR_MEDIA_PLUGGED, seekbarMediaPlugged.getProgress());
        editor.putInt(Constants.SEEKBAR_MEDIA_UNPLUGGED, seekbarMediaUnplugged.getProgress());

        editor.putBoolean(Constants.CHECKBOX_RING_PLUGGED, checkboxRingtonePlugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_RING_UNPLUGGED, checkboxRingtoneUnplugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_NOTIFICATION_PLUGGED, checkboxNotificationPlugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_NOTIFICATION_UNPLUGGED, checkboxNotificationUnplugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_FEEDBACK_PLUGGED, checkboxFeedbackPlugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_FEEDBACK_UNPLUGGED, checkboxFeedbackUnplugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_CALL_PLUGGED, checkboxCallPlugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_CALL_UNPLUGGED, checkboxCallUnplugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_ALARM_PLUGGED, checkboxAlarmPlugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_ALARM_UNPLUGGED, checkboxAlarmUnplugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_MEDIA_PLUGGED, checkboxMediaPlugged.isChecked());
        editor.putBoolean(Constants.CHECKBOX_MEDIA_UNPLUGGED, checkboxMediaUnplugged.isChecked());

        editor.commit();
    }

    private void saveSettings(boolean isServiceEnabled) {
        editor.putBoolean(Constants.SHARED_PREFERENCE_IS_SERVICE_ENABLED, isServiceEnabled);
        editor.putBoolean(Constants.SHARED_PREFERENCE_IS_FIRST_RUN, isServiceEnabled);
        editor.putBoolean(Constants.SHARED_PREFERENCE_ADJUSTED_ON_FIRST_RUN, !isServiceEnabled);
        editor.commit();
    }

    private void startOrStopService(boolean isEnabled) {
        Intent mainServiceIntent = new Intent(this, MyService.class);
        if (isEnabled) {
            startForegroundService(mainServiceIntent);
            Log.d(Constants.DEBUG_TAG, "start service from button");
        } else {
            stopService(mainServiceIntent);
            Log.d(Constants.DEBUG_TAG, "stop service from button");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(Constants.DEBUG_TAG, "Main activity on destroy");
    }
}


