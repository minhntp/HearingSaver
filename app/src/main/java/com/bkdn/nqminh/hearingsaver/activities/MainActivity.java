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
    TextView textviewRingtonePlugged;
    TextView textviewRingtoneUnplugged;
    TextView textviewNotificationPlugged;
    TextView textviewNotificationUnplugged;
    TextView textviewFeedbackPlugged;
    TextView textviewFeedbackUnplugged;
    TextView textviewMediaPlugged;
    TextView textviewMediaUnplugged;
    TextView textviewStatus;
    TextView textviewStatus2;

    Button buttonEnable;
    Button buttonDisable;
    Button buttonSave;

    Intent mServiceIntent;
    private MyService mMyService;

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

        mMyService = new MyService();
        mServiceIntent = new Intent(this, mMyService.getClass());
        connectViews();
        setPreviousStatus();
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        boolean isRunning = false;

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                isRunning = true;
                break;
            }
        }
        Log.i("debug", "service is running?" + isRunning);
        return isRunning;
    }

    private void connectViews() {
        checkboxRingtonePlugged = findViewById(R.id.checkbox_ringtone_plugged);
        checkboxRingtoneUnpugged = findViewById(R.id.checkbox_ringtone_unplugged);
        checkboxNotificationPlugged = findViewById(R.id.checkbox_notification_plugged);
        checkboxNotificationUnplugged = findViewById(R.id.checkbox_notification_unplugged);
        checkboxFeedbackPlugged = findViewById(R.id.checkbox_feedback_plugged);
        checkboxFeedbackUnplugged = findViewById(R.id.checkbox_feedback_unplugged);
        checkboxMediaPlugged = findViewById(R.id.checkbox_media_plugged);
        checkboxMediaUnplugged = findViewById(R.id.checkbox_media_unplugged);

        seekbarRingtonePlugged = findViewById(R.id.seekbar_ringtone_plugged);
        seekbarRingtoneUnplugged = findViewById(R.id.seekbar_ringtone_unplugged);
        seekbarNotificationPlugged = findViewById(R.id.seekbar_notification_plugged);
        seekbarNotificationUnplugged = findViewById(R.id.seekbar_notification_unplugged);
        seekbarFeedbackPlugged = findViewById(R.id.seekbar_feedback_plugged);
        seekbarFeedbackUnplugged = findViewById(R.id.seekbar_feedback_unplugged);
        seekbarMediaPlugged = findViewById(R.id.seekbar_media_plugged);
        seekbarMediaUnplugged = findViewById(R.id.seekbar_media_unplugged);

        textviewRingtonePlugged = findViewById(R.id.textview_ringtone_plugged);
        textviewRingtoneUnplugged = findViewById(R.id.textview_ringtone_unplugged);
        textviewNotificationPlugged = findViewById(R.id.textview_notification_plugged);
        textviewNotificationUnplugged = findViewById(R.id.textview_notification_unplugged);
        textviewFeedbackPlugged = findViewById(R.id.textview_feedback_plugged);
        textviewFeedbackUnplugged = findViewById(R.id.textview_feedback_unplugged);
        textviewMediaPlugged = findViewById(R.id.textview_media_plugged);
        textviewMediaUnplugged = findViewById(R.id.textview_media_unplugged);
        textviewStatus = findViewById(R.id.textview_status);
        textviewStatus2 = findViewById(R.id.textview_status2);

        buttonEnable = findViewById(R.id.button_enable);
        buttonDisable = findViewById(R.id.button_disable);
        buttonSave = findViewById(R.id.button_save);
    }

    private void setPreviousStatus() {
        checkboxRingtonePlugged.setChecked(sharedPreferences.getBoolean(Constants.check1, true));
        checkboxRingtoneUnpugged.setChecked(sharedPreferences.getBoolean(Constants.check2, true));
        checkboxNotificationPlugged.setChecked(sharedPreferences.getBoolean(Constants.check3, true));
        checkboxNotificationUnplugged.setChecked(sharedPreferences.getBoolean(Constants.check4, true));
        checkboxFeedbackPlugged.setChecked(sharedPreferences.getBoolean(Constants.check5, true));
        checkboxFeedbackUnplugged.setChecked(sharedPreferences.getBoolean(Constants.check6, true));
        checkboxMediaPlugged.setChecked(sharedPreferences.getBoolean(Constants.check7, true));
        checkboxMediaUnplugged.setChecked(sharedPreferences.getBoolean(Constants.check8, true));

        seekbarRingtonePlugged.setProgress(sharedPreferences.getInt(Constants.volume1, 100));
        seekbarRingtoneUnplugged.setProgress(sharedPreferences.getInt(Constants.volume2, 100));
        seekbarNotificationPlugged.setProgress(sharedPreferences.getInt(Constants.volume3, 100));
        seekbarNotificationUnplugged.setProgress(sharedPreferences.getInt(Constants.volume4, 100));
        seekbarFeedbackPlugged.setProgress(sharedPreferences.getInt(Constants.volume5, 100));
        seekbarFeedbackUnplugged.setProgress(sharedPreferences.getInt(Constants.volume6, 100));
        seekbarMediaPlugged.setProgress(sharedPreferences.getInt(Constants.volume7, 100));
        seekbarMediaUnplugged.setProgress(sharedPreferences.getInt(Constants.volume8, 100));

        textviewRingtonePlugged.setText(String.valueOf(seekbarRingtonePlugged.getProgress()));
        textviewRingtoneUnplugged.setText(String.valueOf(seekbarRingtoneUnplugged.getProgress()));
        textviewNotificationPlugged.setText(String.valueOf(seekbarNotificationPlugged.getProgress()));
        textviewNotificationUnplugged.setText(String.valueOf(seekbarNotificationUnplugged.getProgress()));
        textviewFeedbackPlugged.setText(String.valueOf(seekbarFeedbackPlugged.getProgress()));
        textviewFeedbackUnplugged.setText(String.valueOf(seekbarFeedbackUnplugged.getProgress()));
        textviewMediaPlugged.setText(String.valueOf(seekbarMediaPlugged.getProgress()));
        textviewMediaUnplugged.setText(String.valueOf(seekbarMediaUnplugged.getProgress()));

        showStatusAndStartOrStopService();
    }

    private void addEvents() {
        seekbarRingtonePlugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textviewRingtonePlugged.setText(String.valueOf(progress));
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
                textviewRingtoneUnplugged.setText(String.valueOf(progress));
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
                textviewNotificationPlugged.setText(String.valueOf(progress));
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
                textviewNotificationUnplugged.setText(String.valueOf(progress));
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
                textviewFeedbackPlugged.setText(String.valueOf(progress));
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
                textviewFeedbackUnplugged.setText(String.valueOf(progress));
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
                textviewMediaPlugged.setText(String.valueOf(progress));
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
                textviewMediaUnplugged.setText(String.valueOf(progress));
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
        boolean isDisabled = sharedPreferences.getBoolean(Constants.settingsDisabled, false);

        if (isDisabled) {
            textviewStatus2.setText(R.string.status_service_disabled);
            textviewStatus.setTextColor(Color.RED);
            buttonDisable.setEnabled(false);
            buttonEnable.setEnabled(true);
            stopService(mServiceIntent);
        } else {
            textviewStatus2.setText(R.string.status_service_enabled);
            textviewStatus.setTextColor(Color.GREEN);
            buttonEnable.setEnabled(false);
            buttonDisable.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(mServiceIntent);
            } else {
               startService(mServiceIntent);
            }
        }

        if (isServiceRunning(MyService.class)) {
            textviewStatus.setText(R.string.status_service_running);
            textviewStatus.setTextColor(Color.GREEN);
        } else {
            textviewStatus.setText(R.string.status_service_not_running);
            textviewStatus.setTextColor(Color.RED);
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


