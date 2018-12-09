package com.bkdn.nqminh.hearingsaver;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static SharedPreferences settings;
    static SharedPreferences.Editor editor;
    Context context;
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
    Button buttonEnable;
    Button buttonDisable;
    Intent mServiceIntent;
    private MyService mMyService;

    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initialize();
        addEvents();
    }

    private void initialize() {
        settings = this.getSharedPreferences("data", MODE_PRIVATE);
        editor = settings.edit();

        context = this;

        mMyService = new MyService(getContext());
        mServiceIntent = new Intent(getContext(), mMyService.getClass());
//        mServiceIntent = new Intent(getContext(), MyService.class);

        connectViews();
        setPreviousStatus();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    private void connectViews() {
        checkboxRingtonePlugged = (CheckBox) findViewById(R.id.checkbox_ringtone_plugged);
        checkboxRingtoneUnpugged = (CheckBox) findViewById(R.id.checkbox_ringtone_unplugged);
        checkboxNotificationPlugged = (CheckBox) findViewById(R.id.checkbox_notification_plugged);
        checkboxNotificationUnplugged = (CheckBox) findViewById(R.id.checkbox_notification_unplugged);
        checkboxFeedbackPlugged = (CheckBox) findViewById(R.id.checkbox_feedback_plugged);
        checkboxFeedbackUnplugged = (CheckBox) findViewById(R.id.checkbox_feedback_unplugged);
        checkboxMediaPlugged = (CheckBox) findViewById(R.id.checkbox_media_plugged);
        checkboxMediaUnplugged = (CheckBox) findViewById(R.id.checkbox_media_unplugged);

        seekbarRingtonePlugged = (SeekBar) findViewById(R.id.seekbar_ringtone_plugged);
        seekbarRingtoneUnplugged = (SeekBar) findViewById(R.id.seekbar_ringtone_unplugged);
        seekbarNotificationPlugged = (SeekBar) findViewById(R.id.seekbar_notification_plugged);
        seekbarNotificationUnplugged = (SeekBar) findViewById(R.id.seekbar_notification_unplugged);
        seekbarFeedbackPlugged = (SeekBar) findViewById(R.id.seekbar_feedback_plugged);
        seekbarFeedbackUnplugged = (SeekBar) findViewById(R.id.seekbar_feedback_unplugged);
        seekbarMediaPlugged = (SeekBar) findViewById(R.id.seekbar_media_plugged);
        seekbarMediaUnplugged = (SeekBar) findViewById(R.id.seekbar_media_unplugged);

        textviewRingtonePlugged = (TextView) findViewById(R.id.textview_ringtone_plugged);
        textviewRingtoneUnplugged = (TextView) findViewById(R.id.textview_ringtone_unplugged);
        textviewNotificationPlugged = (TextView) findViewById(R.id.textview_notification_plugged);
        textviewNotificationUnplugged = (TextView) findViewById(R.id.textview_notification_unplugged);
        textviewFeedbackPlugged = (TextView) findViewById(R.id.textview_feedback_plugged);
        textviewFeedbackUnplugged = (TextView) findViewById(R.id.textview_feedback_unplugged);
        textviewMediaPlugged = (TextView) findViewById(R.id.textview_media_plugged);
        textviewMediaUnplugged = (TextView) findViewById(R.id.textview_media_unplugged);
        textviewStatus = (TextView) findViewById(R.id.textview_status);

        buttonEnable = (Button) findViewById(R.id.button_enable);
        buttonDisable = (Button) findViewById(R.id.button_disable);
    }

    private void setPreviousStatus() {
        checkboxRingtonePlugged.setChecked(settings.getBoolean("check1", true));
        checkboxRingtoneUnpugged.setChecked(settings.getBoolean("check2", true));
        checkboxNotificationPlugged.setChecked(settings.getBoolean("check3", true));
        checkboxNotificationUnplugged.setChecked(settings.getBoolean("check4", true));
        checkboxFeedbackPlugged.setChecked(settings.getBoolean("check5", true));
        checkboxFeedbackUnplugged.setChecked(settings.getBoolean("check6", true));
        checkboxMediaPlugged.setChecked(settings.getBoolean("check7", true));
        checkboxMediaUnplugged.setChecked(settings.getBoolean("check8", true));

        seekbarRingtonePlugged.setProgress(settings.getInt("volume1", 100));
        seekbarRingtoneUnplugged.setProgress(settings.getInt("volume2", 100));
        seekbarNotificationPlugged.setProgress(settings.getInt("volume3", 100));
        seekbarNotificationUnplugged.setProgress(settings.getInt("volume4", 100));
        seekbarFeedbackPlugged.setProgress(settings.getInt("volume5", 100));
        seekbarFeedbackUnplugged.setProgress(settings.getInt("volume6", 100));
        seekbarMediaPlugged.setProgress(settings.getInt("volume7", 100));
        seekbarMediaUnplugged.setProgress(settings.getInt("volume8", 100));

        textviewRingtonePlugged.setText("" + seekbarRingtonePlugged.getProgress());
        textviewRingtoneUnplugged.setText("" + seekbarRingtoneUnplugged.getProgress());
        textviewNotificationPlugged.setText("" + seekbarNotificationPlugged.getProgress());
        textviewNotificationUnplugged.setText("" + seekbarNotificationUnplugged.getProgress());
        textviewFeedbackPlugged.setText("" + seekbarFeedbackPlugged.getProgress());
        textviewFeedbackUnplugged.setText("" + seekbarFeedbackUnplugged.getProgress());
        textviewMediaPlugged.setText("" + seekbarMediaPlugged.getProgress());
        textviewMediaUnplugged.setText("" + seekbarMediaUnplugged.getProgress());

        if (isMyServiceRunning(mMyService.getClass())) {
            textviewStatus.setText("Service is running!");
        } else {
            textviewStatus.setText("Service is not running!");
        }
    }

    private void addEvents() {
        seekbarRingtonePlugged.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textviewRingtonePlugged.setText("" + progress);
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
                textviewRingtoneUnplugged.setText("" + progress);
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
                textviewNotificationPlugged.setText("" + progress);
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
                textviewNotificationUnplugged.setText("" + progress);
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
                textviewFeedbackPlugged.setText("" + progress);
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
                textviewFeedbackUnplugged.setText("" + progress);
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
                textviewMediaPlugged.setText("" + progress);
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
                textviewMediaUnplugged.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.button_enable:
//                if (!isMyServiceRunning(mMyService.getClass())) {
//                    startService(mServiceIntent);
//                }
//                startForegroundService(mServiceIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(mServiceIntent);
                } else {
                    context.startService(mServiceIntent);
                }
                editor.putBoolean("disable", false);
                editor.commit();
                saveStatus();
                if (isMyServiceRunning(mMyService.getClass())) {
                    textviewStatus.setText("Service is running!");
                } else {
                    textviewStatus.setText("Service is not running!");
                }
                break;
            case R.id.button_disable:
                stopService(mServiceIntent);
                saveStatus();
                editor.putBoolean("disable", true);
                editor.commit();
                if (isMyServiceRunning(mMyService.getClass())) {
                    textviewStatus.setText("Service is running!");
                } else {
                    textviewStatus.setText("Service is not running!");
                }
                break;
            case R.id.button_save:
                saveStatus();
                Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
                if (isMyServiceRunning(mMyService.getClass())) {
                    textviewStatus.setText("Service is running!");
                } else {
                    textviewStatus.setText("Service is not running!");
                }
                break;
        }
    }

    private void saveStatus() {
        editor.putInt("volume1", seekbarRingtonePlugged.getProgress());
        editor.putInt("volume2", seekbarRingtoneUnplugged.getProgress());
        editor.putInt("volume3", seekbarNotificationPlugged.getProgress());
        editor.putInt("volume4", seekbarNotificationUnplugged.getProgress());
        editor.putInt("volume5", seekbarFeedbackPlugged.getProgress());
        editor.putInt("volume6", seekbarFeedbackUnplugged.getProgress());
        editor.putInt("volume7", seekbarMediaPlugged.getProgress());
        editor.putInt("volume8", seekbarMediaUnplugged.getProgress());
        editor.putBoolean("check1", checkboxRingtonePlugged.isChecked());
        editor.putBoolean("check2", checkboxRingtoneUnpugged.isChecked());
        editor.putBoolean("check3", checkboxNotificationPlugged.isChecked());
        editor.putBoolean("check4", checkboxNotificationUnplugged.isChecked());
        editor.putBoolean("check5", checkboxFeedbackPlugged.isChecked());
        editor.putBoolean("check6", checkboxFeedbackUnplugged.isChecked());
        editor.putBoolean("check7", checkboxMediaPlugged.isChecked());
        editor.putBoolean("check8", checkboxMediaUnplugged.isChecked());
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }

}


