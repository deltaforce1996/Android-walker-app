package com.example.alifdeltaforce.walkmonitoringapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alifdeltaforce.walkmonitoringapp.Api.InternetConnection;
import com.example.alifdeltaforce.walkmonitoringapp.Model.User;
import com.example.alifdeltaforce.walkmonitoringapp.ServerInterface.Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView btn_profile, btn_connect, btn_repoet, btn_alarm, btn_track;
    private Toolbar tb;
    private Server mserver;
    private String email;
    private TextView sh_email;
    private CircleImageView sh_picc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        btn_profile = (CardView) findViewById(R.id.card_me);
        btn_connect = (CardView) findViewById(R.id.card_connect);
        btn_alarm = (CardView) findViewById(R.id.card_alarm);
        btn_repoet = (CardView) findViewById(R.id.card_report);
        btn_track = (CardView) findViewById(R.id.card_track);

        sh_email = (TextView) findViewById(R.id.show_txt_email);
        sh_picc = (CircleImageView) findViewById(R.id.cir_pic);

        mserver = InternetConnection.getApiCline().create(Server.class);


        btn_profile.setOnClickListener(this);
        btn_repoet.setOnClickListener(this);
        btn_alarm.setOnClickListener(this);
        btn_track.setOnClickListener(this);
        btn_connect.setOnClickListener(this);

        tb = (Toolbar) findViewById(R.id.tools_bar);

        setSupportActionBar(tb);
        SharedPreferences sp = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        email = sp.getString("userEmail","");

        sh_email.setText(email);

        GetImage();

    }

    @Override
    public void onClick(View v) {
        if (v==btn_profile){
//            Toast.makeText(this, "PROFILE", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,ProfileActivity.class);
            finish();
            startActivity(intent);

        }
        if (v==btn_connect){
//            Toast.makeText(this, "CONNECT", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,ConnenctActivity.class);
            startActivity(intent);
        }
        if (v==btn_repoet){
//            Toast.makeText(this, "REPORT", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MenuActivity.this,ReportActivity.class);
            startActivity(intent);
        }
        if (v==btn_track){
//            Toast.makeText(this, "MONITOR", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MenuActivity.this,TrackActivity.class);
            startActivity(intent);
        }
        if (v==btn_alarm){
//            Toast.makeText(this, "ALARM", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,AlertActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btn_menu_out:
                UserSignOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void UserSignOut(){

        SharedPreferences preferences = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove("userEmail");
        edit.remove("userPass");
//        edit.remove("PIC");
        edit.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void GetImage(){
        Call<User> call = mserver.GetUserDataDetail_fix(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(MenuActivity.this, ""+response.body().getMassage(), Toast.LENGTH_SHORT).show();
                if (response.body().getUserDetail().getUserImg()==""){

                    sh_picc.setImageResource(R.drawable.profile_test);
//                    Log.d(TAG,"Thread Working");

                }else {

                    uri_txt = response.body().getUserDetail().getUserImg();
                    BackgroundWorkOne  backgroundWorkone = new BackgroundWorkOne();
                    backgroundWorkone.start();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private String uri_txt;

    private Bitmap logo;

    class BackgroundWorkOne extends Thread {
        @Override
        public void run() {
            super.run();
            logo = getBitmapFromURL(uri_txt);
            sh_picc.post(new Runnable() {
                @Override
                public void run() {

                    sh_picc.setImageBitmap(logo);

                }
            });
        }
    }

}
