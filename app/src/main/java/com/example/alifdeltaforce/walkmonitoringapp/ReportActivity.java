package com.example.alifdeltaforce.walkmonitoringapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.alifdeltaforce.walkmonitoringapp.Api.InternetConnection;
import com.example.alifdeltaforce.walkmonitoringapp.Model.Report;
import com.example.alifdeltaforce.walkmonitoringapp.ServerInterface.Server;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private Server server;
    private LineGraphSeries<DataPoint> series2;
    private String email;
    private GraphView graph;
    private Toolbar mToolsbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        SharedPreferences sp = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        email = sp.getString("userEmail","");

        server = InternetConnection.getApiCline().create(Server.class);


        graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Graph View Step/Week");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Re);
        toolbar.setTitle("Report");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // second series
        series2 = new LineGraphSeries<>();

        series2.setDrawBackground(true);
        series2.setDrawDataPoints(true);
        series2.setColor(Color.argb(255, 255, 60, 60));
        series2.setBackgroundColor(Color.argb(100, 204, 119, 119));

        GetReport(email);

    }


    private void GetReport(String txt_email){

        final Date[] date = new Date[6];

        Call<Report> call = server.GetPePort(txt_email);
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


                try {
                    for (int i =0;i<response.body().getReport().size();i++){

                        date[i] = format.parse(response.body().getReport().get(i).getDate());

                        series2.appendData(new DataPoint(i,Integer.valueOf(response.body().getReport().get(i).getStepSum())),true,100);
                    }
                }catch (Exception e){
                    Toast.makeText(ReportActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                graph.getViewport().setScalable(true);
//                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
                graph.getGridLabelRenderer().setNumHorizontalLabels(7);

//                graph.getViewport().setMinX(date[0].getDate());
//                graph.getViewport().setXAxisBoundsManual(true);
//
//                graph.getGridLabelRenderer().setHumanRounding(false);
                graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
                graph.addSeries(series2);


            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {

                Toast.makeText(ReportActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
