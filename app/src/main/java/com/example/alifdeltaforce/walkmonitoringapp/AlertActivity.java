package com.example.alifdeltaforce.walkmonitoringapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alifdeltaforce.walkmonitoringapp.Receiver.AlarmReceiver;

import java.util.Calendar;

public class AlertActivity extends AppCompatActivity {

    private Toolbar mToolsbar;
    private EditText edtTime2;
    private boolean WakeActive;
    private int mHour, mMinute;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            window.setStatusBarColor(getColor(R.color.sky_blue_pressed));
        }else {
            window.setStatusBarColor(getResources().getColor(R.color.sky_blue_pressed));
        }

        Intent i = getIntent();
        WakeActive = i.getBooleanExtra("NUMBERS",false);
        if (WakeActive){
            ShowDialogAlert();
        }

        edtTime2 = (EditText) findViewById(R.id.edt_txt_time2);


        edtTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AlertActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        edtTime2.setText(""+hourOfDay+" : "+minute);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);

//                        WekeUp(cal);
                        startAlarm(cal,true,false);
                    }
                },mHour, mMinute, true);



                timePickerDialog.show();
            }
        });

        mToolsbar = (Toolbar) findViewById(R.id.toolbar_t);

        setSupportActionBar(mToolsbar);
        getSupportActionBar().setTitle("Set Time Notify");
        mToolsbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ShowDialogAlert();

        mToolsbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertActivity.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

//    Handler myhandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//
//        }
//    };




    private void ShowDialogAlert(){

        WakeActive = false;

        final AlertDialog.Builder dialog_edit = new AlertDialog.Builder(AlertActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.notify_dialog, null);

        dialog_edit.setView(mView);
        final AlertDialog dialog = dialog_edit.create();
        dialog.show();

        Button btn_close = (Button) mView.findViewById(R.id.btn_summit);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertActivity.this,MenuActivity.class);
                finish();
                startActivity(intent);
            }
        });


    }

    private void startAlarm(Calendar cal, boolean isNotification, boolean isRepeat) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent;
        PendingIntent pendingIntent = null;

        Toast.makeText(this, ""+cal.getTimeInMillis(), Toast.LENGTH_SHORT).show();

        if(isNotification){
            intent = new Intent(AlertActivity.this,AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        }

        if (!isRepeat){
//            alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+30000,pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);
        }else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000,3000,pendingIntent);
        }
    }

//    class CheckTime extends Thread{
//
//        @Override
//        public void run() {
//            super.run();
//
//            try {
//                Thread.sleep(1000);
//
//            }catch (InterruptedException e){
//
//                e.printStackTrace();
//            }
//        }
//    }
}
