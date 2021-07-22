package com.bkdn.nqminh.hearingsaver.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
    private CheckBox checkboxRingtoneUnplugged;
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
    }

    private void initialize() {
        connectViews();
        addEvents();

        sharedPreferences = Operator.getInstance(getApplicationContext()).getSharedPreferences();
        editor = Operator.getInstance(getApplicationContext()).getEditor();

        setSavedSettings();

        mainServiceIntent = new Intent(this, MyService.class);

        boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.SP_IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            startForegroundService(mainServiceIntent);
        }

        showStatus();
    }

    private void connectViews() {
        checkboxRingtonePlugged = findViewById(R.id.ringtone_plugged_checkbox);
        checkboxRingtoneUnplugged = findViewById(R.id.ringtone_unplugged_checkbox);
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

    private void setSavedSettings() {
        checkboxRingtonePlugged.setChecked(sharedPreferences.getBoolean(Constants.CB_RING_PLUGGED, true));
        checkboxRingtoneUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CB_RING_UNPLUGGED, true));
        checkboxNotificationPlugged.setChecked(sharedPreferences.getBoolean(Constants.CB_NOTI_PLUGGED, true));
        checkboxNotificationUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CB_NOTI_UNPLUGGED, true));
        checkboxFeedbackPlugged.setChecked(sharedPreferences.getBoolean(Constants.CB_FEEDBACK_PLUGGED, true));
        checkboxFeedbackUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CB_FEEDBACK_UNPLUGGED, true));
        checkboxMediaPlugged.setChecked(sharedPreferences.getBoolean(Constants.CB_MEDIA_PLUGGED, true));
        checkboxMediaUnplugged.setChecked(sharedPreferences.getBoolean(Constants.CB_MEDIA_UNPLUGGED, true));

        seekbarRingtonePlugged.setProgress(sharedPreferences.getInt(Constants.SKB_RING_PLUGGED, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarRingtoneUnplugged.setProgress(sharedPreferences.getInt(Constants.SKB_RING_UNPLUGGED, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarNotificationPlugged.setProgress(sharedPreferences.getInt(Constants.SKB_NOTI_PLUGGED, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarNotificationUnplugged.setProgress(sharedPreferences.getInt(Constants.SKB_NOTI_UNPLUGGED, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarFeedbackPlugged.setProgress(sharedPreferences.getInt(Constants.SKB_FEEDBACK_PLUGGED, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarFeedbackUnplugged.setProgress(sharedPreferences.getInt(Constants.SKB_FEEDBACK_UNPLUGGED, Constants.DEFAULT_VOLUME_SPEAKER));
        seekbarMediaPlugged.setProgress(sharedPreferences.getInt(Constants.SKB_MEDIA_PLUGGED, Constants.DEFAULT_VOLUME_HEADPHONE));
        seekbarMediaUnplugged.setProgress(sharedPreferences.getInt(Constants.SKB_MEDIA_UNPLUGGED, Constants.DEFAULT_VOLUME_SPEAKER));

        volumeRingtonePlugged.setText(String.valueOf(seekbarRingtonePlugged.getProgress()));
        volumeRingtoneUnplugged.setText(String.valueOf(seekbarRingtoneUnplugged.getProgress()));
        volumeNotificationPlugged.setText(String.valueOf(seekbarNotificationPlugged.getProgress()));
        volumeNotificationUnplugged.setText(String.valueOf(seekbarNotificationUnplugged.getProgress()));
        volumeFeedbackPlugged.setText(String.valueOf(seekbarFeedbackPlugged.getProgress()));
        volumeFeedbackUnplugged.setText(String.valueOf(seekbarFeedbackUnplugged.getProgress()));
        volumeMediaPlugged.setText(String.valueOf(seekbarMediaPlugged.getProgress()));
        volumeMediaUnplugged.setText(String.valueOf(seekbarMediaUnplugged.getProgress()));
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

    private void handleServiceStateChange( boolean isEnabled) {
        saveSettings();
        saveSettings(isEnabled);
        startOrStopService(isEnabled);
        showStatus();
    }

    private void showStatus() {

        // Check again to see if sharedPreference is set
        boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.SP_IS_SERVICE_ENABLED, false);

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

    private  void saveSettings() {
        editor.putInt(Constants.SKB_RING_PLUGGED, seekbarRingtonePlugged.getProgress());
        editor.putInt(Constants.SKB_RING_UNPLUGGED, seekbarRingtoneUnplugged.getProgress());
        editor.putInt(Constants.SKB_NOTI_PLUGGED, seekbarNotificationPlugged.getProgress());
        editor.putInt(Constants.SKB_NOTI_UNPLUGGED, seekbarNotificationUnplugged.getProgress());
        editor.putInt(Constants.SKB_FEEDBACK_PLUGGED, seekbarFeedbackPlugged.getProgress());
        editor.putInt(Constants.SKB_FEEDBACK_UNPLUGGED, seekbarFeedbackUnplugged.getProgress());
        editor.putInt(Constants.SKB_MEDIA_PLUGGED, seekbarMediaPlugged.getProgress());
        editor.putInt(Constants.SKB_MEDIA_UNPLUGGED, seekbarMediaUnplugged.getProgress());
        editor.putBoolean(Constants.CB_RING_PLUGGED, checkboxRingtonePlugged.isChecked());
        editor.putBoolean(Constants.CB_RING_UNPLUGGED, checkboxRingtoneUnplugged.isChecked());
        editor.putBoolean(Constants.CB_NOTI_PLUGGED, checkboxNotificationPlugged.isChecked());
        editor.putBoolean(Constants.CB_NOTI_UNPLUGGED, checkboxNotificationUnplugged.isChecked());
        editor.putBoolean(Constants.CB_FEEDBACK_PLUGGED, checkboxFeedbackPlugged.isChecked());
        editor.putBoolean(Constants.CB_FEEDBACK_UNPLUGGED, checkboxFeedbackUnplugged.isChecked());
        editor.putBoolean(Constants.CB_MEDIA_PLUGGED, checkboxMediaPlugged.isChecked());
        editor.putBoolean(Constants.CB_MEDIA_UNPLUGGED, checkboxMediaUnplugged.isChecked());
        editor.commit();
    }

    private void saveSettings(boolean isServiceEnabled) {
        editor.putBoolean(Constants.SP_IS_SERVICE_ENABLED, isServiceEnabled);
        editor.putBoolean(Constants.SP_IS_FIRST_RUN, isServiceEnabled);
        editor.commit();
    }

    private void startOrStopService(boolean isEnabled) {
        if(isEnabled){
            startForegroundService(mainServiceIntent);
        } else {
            stopService(mainServiceIntent);
        }
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, Constants.TOAST_ACTIVITY_DESTROYED, Toast.LENGTH_SHORT).show();
        stopService(mainServiceIntent);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Default behavior: finish (destroy) the activity
//        super.onBackPressed();

        // Do nothing instead
        moveTaskToBack(true);
    }
}


