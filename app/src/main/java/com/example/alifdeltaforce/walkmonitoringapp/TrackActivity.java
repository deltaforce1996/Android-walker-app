package com.example.alifdeltaforce.walkmonitoringapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.alifdeltaforce.walkmonitoringapp.Api.InternetConnection;
import com.example.alifdeltaforce.walkmonitoringapp.Model.Complate;
import com.example.alifdeltaforce.walkmonitoringapp.Model.Level;
import com.example.alifdeltaforce.walkmonitoringapp.Model.Templatm;
import com.example.alifdeltaforce.walkmonitoringapp.ServerInterface.Server;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackActivity extends AppCompatActivity implements SensorEventListener ,RatingDialogListener {

    private final String TAG = "TrackActivity";
    private TriggerEventListener triggerEventListener;

    private Server mserver;

    int progress = 0,counterTime = 0;
    private RingProgressBar ringProgressBar_step,ringProgressBar_time;
    private TextView tv_amountStep,tv_amounttime,distanCe;

    private SensorManager mSensorManager;
    private Sensor mSensor,dSensor;

    private boolean activityRunning;

    private int DistanceAmount;
    private int UserLevel;

    private int Tem_Step,Tem_Distant,Tem_Time;

    private String email;

    private  GraphView graph;
    private  LineGraphSeries<DataPoint> series;

    ///Fix Layout
    private String[] descriptionData = {"Level 1", "Level 2", "Level 3", "Level 4"};
    private StateProgressBar stateProgressBar;

    @Override
    protected void onResume() {
        super.onResume();

        activityRunning = true;

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        dSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);


        if (mSensor != null){

            tv_amountStep.setText(""+progress);
            mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
            mSensorManager.requestTriggerSensor(triggerEventListener,dSensor);

        }else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
        if(progress >5){
            UpdateStepForGen();
        }

    }

    long totalSeconds = 100000;
    long intervalSeconds = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        mserver = InternetConnection.getApiCline().create(Server.class);

        SharedPreferences sp = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        email = sp.getString("userEmail","");

        stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);

         graph = (GraphView) findViewById(R.id.chart1);
         distanCe = (TextView) findViewById(R.id.tv_distance);

        series = new LineGraphSeries<DataPoint>();
        series.setThickness(5);
        series.setColor(Color.RED);

        GetDataUser();

        CountDownTimer timer = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("seconds elapsed: " , String.valueOf((totalSeconds * 1000 - millisUntilFinished) / 1000));
                timerhandler.sendEmptyMessage(0);
            }

            public void onFinish() {
                Log.d( "done!", "Time's up!");
            }

        };

        timer.start();

        triggerEventListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {

                Log.d(TAG,"TriggerEvent : "+event.values[0]);
            }
        };

        tv_amountStep = (TextView)findViewById(R.id.tv_step);
        tv_amounttime = (TextView) findViewById(R.id.tv_time);

        ringProgressBar_step = (RingProgressBar) findViewById(R.id.progress_bar_2);
        ringProgressBar_time = (RingProgressBar) findViewById(R.id.progress_bar_time);

        ringProgressBar_step.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                Toast.makeText(TrackActivity.this, "Level UP", Toast.LENGTH_SHORT).show();
                showNotification();
                UpdateLevel(UserLevel+1);



            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_track);
        toolbar.setTitle("Tracking");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackActivity.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    int sum=9,i=1;

    Handler timerhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                if (counterTime<Tem_Time){
                    counterTime++;
                   ringProgressBar_time.setProgress(counterTime);
                   tv_amounttime.setText(""+counterTime+" seconds");

                   try{
                       series.appendData(new DataPoint(counterTime,progress),true,25);
                       graph.addSeries(series);
                   }catch (Exception e){
                       Log.d(TAG,"appendData Error");
                   }


                }
            }
        }
    };


    Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                if (progress<Tem_Step){
                    progress++;
                    tv_amountStep.setText(""+progress);
                    ringProgressBar_step.setProgress(progress);

                    if (progress>sum){
                        sum=9*i;
                        i++;
                        DistanceAmount++;
                        distanCe.setText(""+DistanceAmount);
                    }
                }
            }
        }
    };

    private void GetDialoReating(int num){

        progress = 0;
        counterTime = 0;

        new AppRatingDialog.Builder()
                .setPositiveButtonText("OK")
                .setNoteDescriptions(Arrays.asList("Quite ok", "Good", "Very Good", "Excellent !!!"))
                .setDefaultRating(num)
                .setTitle("Rate this application")
                .setDescription("Please select some stars and give your feedback")
                .setStarColor(R.color.colorAccent)
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorAccent)
                .setCommentInputEnabled(false)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(this)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mSensorManager.unregisterListener(this);
    }

    private void showNotification() {

        Context context = TrackActivity.this;
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Notification notification =
                new NotificationCompat.Builder(context) // this is context
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(bitmap)
                        .setContentTitle(" Notification")
                        .setContentText("Level UP")
                        .setAutoCancel(false)
                        .setColor(color)
                        .setVibrate(new long[] { 500, 1000, 500 })
                        .setLights(color, 3000, 3000)
                        .build();


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning){

            myhandler.sendEmptyMessage(0);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG,"onSensorChanged : "+accuracy);
    }

    private void GetDataUser(){

        Toast.makeText(this, ""+email, Toast.LENGTH_SHORT).show();
        Call<Level> call = mserver.GETDATAUSERFORFSET(email);
        call.enqueue(new Callback<Level>() {
            @Override
            public void onResponse(Call<Level> call, Response<Level> response) {
                if (Integer.valueOf(response.body().getSuccess())==1){

                    UserLevel = Integer.valueOf(response.body().getUserDetail().get(0).getLevel());

                    switch (Integer.valueOf(response.body().getUserDetail().get(0).getLevel())){

                        case 1:
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                            break;
                        case 2:
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                            break;
                        case 3:
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                            break;
                        case 4:
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                            break;
                        case 5:
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                            break;
                    }

                    try {
                        GetTemPlate(Integer.valueOf(response.body().getUserDetail().get(0).getLevel()));
                    }catch (Exception e){
                        Log.d(TAG,"Level Last Max Error");
                    }

                }

            }

            @Override
            public void onFailure(Call<Level> call, Throwable t) {
                Toast.makeText(TrackActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetTemPlate(final int level){

        Call<Templatm> call = mserver.getTemPlatm();
        call.enqueue(new Callback<Templatm>() {
            @Override
            public void onResponse(Call<Templatm> call, Response<Templatm> response) {

                try {
                    if (response.body().getSuccess()==1){
                        ringProgressBar_step.setMax(Integer.valueOf(response.body().getUserDetail().get(level).getStepNumber()));
                        ringProgressBar_time.setMax(Integer.valueOf(response.body().getUserDetail().get(level).getCounterTime())/10);

                        Tem_Step = Integer.valueOf(response.body().getUserDetail().get(level).getStepNumber());
                        Tem_Time = Integer.valueOf(response.body().getUserDetail().get(level).getCounterTime())/10;
                        Tem_Distant = Integer.valueOf(response.body().getUserDetail().get(level).getDistance());
                    }
                }catch (Exception e){
                    Toast.makeText(TrackActivity.this, "Most Level", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Templatm> call, Throwable t) {
                Toast.makeText(TrackActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateLevel(int i){

        Call<Complate> call = mserver.UpdateLevel(i,email,progress,counterTime,DistanceAmount);
        call.enqueue(new Callback<Complate>() {
            @Override
            public void onResponse(Call<Complate> call, Response<Complate> response) {
                Log.d("Track",response.body().getMassage());
                GetDialoReating(UserLevel+1);
            }

            @Override
            public void onFailure(Call<Complate> call, Throwable t) {

            }
        });

    }

    private void UpdateStepForGen(){

        Call<Complate> call = mserver.RealTimeUpdate(UserLevel,email,progress,counterTime,DistanceAmount);
        call.enqueue(new Callback<Complate>() {
            @Override
            public void onResponse(Call<Complate> call, Response<Complate> response) {
                Log.d(TAG,""+response.body().getMassage());
            }

            @Override
            public void onFailure(Call<Complate> call, Throwable t) {
                Log.d(TAG,""+t.getMessage());
            }
        });
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onNegativeButtonClicked() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onNeutralButtonClicked() {
        finish();
        startActivity(getIntent());
    }
}

