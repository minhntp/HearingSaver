package com.bkdn.nqminh.hearingsaver.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    CheckBox checkboxRingtonePlugged;
    CheckBox checkboxRingtoneUnpugged;
    CheckBox checkboxNotificationPlugged;
    CheckBox checkboxNotificationUnplugged;
    CheckBox checkboxFeedbackPlugged;
    CheckBox checkboxFeedbackUnplugged;
    CheckBox checkboxMediaPlugged;
    CheckBox checkboxMediaUnplugged;
    SeekBar seekbarRingtonePlugged;
    SeekBar seekbarRingtoneUnplugged;
    SeekBar seekbarNotificationPlugged;
    SeekBar seekbarNotificationUnplugged;
    SeekBar seekbarFeedbackPlugged;
    SeekBar seekbarFeedbackUnplugged;
    SeekBar seekbarMediaPlugged;
    SeekBar seekbarMediaUnplugged;
    TextView volumeRingtonePlugged;
    TextView volumeRingtoneUnplugged;
    TextView volumeNotificationPlugged;
    TextView volumeNotificationUnplugged;
    TextView volumeFeedbackPlugged;
    TextView volumeFeedbackUnplugged;
    TextView volumeMediaPlugged;
    TextView volumeMediaUnplugged;
    TextView textViewSettingStatus;
    TextView textViewServiceStatus;

    Button buttonEnable;
    Button buttonDisable;
    Button buttonSave;

    Intent mServiceIntent;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initialize();
        addEvents();
    }

    private void initialize() {

        sharedPreferences = Operator.getInstance().getSharedPreferences();
        editor = Operator.getInstance().getEditor();

        MyService mMyService = new MyService();
        mServiceIntent = new Intent(this, mMyService.getClass());
        connectViews();
//        setDefaultOnFirstRun();
        setPreviousStatus();
    }

    private boolean isServiceRunning() {
        boolean isRunning = false;

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MyService.class.getName().equals(service.service.getClassName())) {
                isRunning = true;
                break;
            }
        }
        Log.i("debug", "service is running?" + isRunning);
        return isRunning;
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
        checkboxRingtonePlugged.setChecked(sharedPreferences.getBoolean(Constants.check1, true));
        checkboxRingtoneUnpugged.setChecked(sharedPreferences.getBoolean(Constants.check2, true));
        checkboxNotificationPlugged.setChecked(sharedPreferences.getBoolean(Constants.check3, true));
        checkboxNotificationUnplugged.setChecked(sharedPreferences.getBoolean(Constants.check4, true));
        checkboxFeedbackPlugged.setChecked(sharedPreferences.getBoolean(Constants.check5, true));
        checkboxFeedbackUnplugged.setChecked(sharedPreferences.getBoolean(Constants.check6, true));
        checkboxMediaPlugged.setChecked(sharedPreferences.getBoolean(Constants.check7, true));
        checkboxMediaUnplugged.setChecked(sharedPreferences.getBoolean(Constants.check8, true));

        seekbarRingtonePlugged.setProgress(sharedPreferences.getInt(Constants.volume1, Constants.defaultVolumeHeadphone));
        seekbarRingtoneUnplugged.setProgress(sharedPreferences.getInt(Constants.volume2, Constants.defaultVolumeSpeaker));
        seekbarNotificationPlugged.setProgress(sharedPreferences.getInt(Constants.volume3, Constants.defaultVolumeHeadphone));
        seekbarNotificationUnplugged.setProgress(sharedPreferences.getInt(Constants.volume4, Constants.defaultVolumeSpeaker));
        seekbarFeedbackPlugged.setProgress(sharedPreferences.getInt(Constants.volume5, Constants.defaultVolumeHeadphone));
        seekbarFeedbackUnplugged.setProgress(sharedPreferences.getInt(Constants.volume6, Constants.defaultVolumeSpeaker));
        seekbarMediaPlugged.setProgress(sharedPreferences.getInt(Constants.volume7, Constants.defaultVolumeHeadphone));
        seekbarMediaUnplugged.setProgress(sharedPreferences.getInt(Constants.volume8, Constants.defaultVolumeSpeaker));

        volumeRingtonePlugged.setText(String.valueOf(seekbarRingtonePlugged.getProgress()));
        volumeRingtoneUnplugged.setText(String.valueOf(seekbarRingtoneUnplugged.getProgress()));
        volumeNotificationPlugged.setText(String.valueOf(seekbarNotificationPlugged.getProgress()));
        volumeNotificationUnplugged.setText(String.valueOf(seekbarNotificationUnplugged.getProgress()));
        volumeFeedbackPlugged.setText(String.valueOf(seekbarFeedbackPlugged.getProgress()));
        volumeFeedbackUnplugged.setText(String.valueOf(seekbarFeedbackUnplugged.getProgress()));
        volumeMediaPlugged.setText(String.valueOf(seekbarMediaPlugged.getProgress()));
        volumeMediaUnplugged.setText(String.valueOf(seekbarMediaUnplugged.getProgress()));

        showStatusAndStartOrStopService();
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

        buttonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                buttonEnable.setEnabled(false);
//                buttonDisable.setEnabled(true);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    context.startForegroundService(mServiceIntent);
//                } else {
//                    context.startService(mServiceIntent);
//                }
                saveValues();
                editor.putBoolean(Constants.settingsDisabled, false);
                editor.commit();
                showStatusAndStartOrStopService();
            }
        });

        buttonDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                stopService(mServiceIntent);
//                buttonEnable.setEnabled(true);
//                buttonDisable.setEnabled(false);
                saveValues();
                editor.putBoolean(Constants.settingsDisabled, true);
                editor.commit();
                showStatusAndStartOrStopService();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveValues();
            }
        });
    }

    private void showStatusAndStartOrStopService() {
        boolean isDisabled = sharedPreferences.getBoolean(Constants.settingsDisabled, true);

        if (isDisabled) {
            textViewSettingStatus.setText(R.string.status_service_disabled);
            textViewSettingStatus.setTextColor(Color.RED);
            buttonDisable.setEnabled(false);
            buttonEnable.setEnabled(true);
            stopService(mServiceIntent);
        } else {
            textViewSettingStatus.setText(R.string.status_service_enabled);
            textViewSettingStatus.setTextColor(getColor(R.color.blue));
            buttonEnable.setEnabled(false);
            buttonDisable.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(mServiceIntent);
            } else {
               startService(mServiceIntent);
            }
        }

        if (isServiceRunning()) {
            textViewServiceStatus.setText(R.string.status_service_running);
            textViewServiceStatus.setTextColor(getColor(R.color.blue));
        } else {
            textViewServiceStatus.setText(R.string.status_service_not_running);
            textViewServiceStatus.setTextColor(Color.RED);
        }
    }

    private void saveValues() {
        editor.putInt(Constants.volume1, seekbarRingtonePlugged.getProgress());
        editor.putInt(Constants.volume2, seekbarRingtoneUnplugged.getProgress());
        editor.putInt(Constants.volume3, seekbarNotificationPlugged.getProgress());
        editor.putInt(Constants.volume4, seekbarNotificationUnplugged.getProgress());
        editor.putInt(Constants.volume5, seekbarFeedbackPlugged.getProgress());
        editor.putInt(Constants.volume6, seekbarFeedbackUnplugged.getProgress());
        editor.putInt(Constants.volume7, seekbarMediaPlugged.getProgress());
        editor.putInt(Constants.volume8, seekbarMediaUnplugged.getProgress());
        editor.putBoolean(Constants.check1, checkboxRingtonePlugged.isChecked());
        editor.putBoolean(Constants.check2, checkboxRingtoneUnpugged.isChecked());
        editor.putBoolean(Constants.check3, checkboxNotificationPlugged.isChecked());
        editor.putBoolean(Constants.check4, checkboxNotificationUnplugged.isChecked());
        editor.putBoolean(Constants.check5, checkboxFeedbackPlugged.isChecked());
        editor.putBoolean(Constants.check6, checkboxFeedbackUnplugged.isChecked());
        editor.putBoolean(Constants.check7, checkboxMediaPlugged.isChecked());
        editor.putBoolean(Constants.check8, checkboxMediaUnplugged.isChecked());
        editor.commit();
        Toast.makeText(this, Constants.toastSettingsSaved, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }

}


