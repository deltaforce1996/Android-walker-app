package com.example.alifdeltaforce.walkmonitoringapp;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import at.markushi.ui.CircleButton;
import me.relex.circleindicator.CircleIndicator;


public class MainActivity extends AppCompatActivity {
    MyPageAdapter adapter;
    ViewPager pager;
    CircleIndicator indicator;
    private CircleButton btnNenxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MyPageAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);

        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        btnNenxt = (CircleButton) findViewById(R.id.circleButtonNext);
        btnNenxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, ""+pager.getCurrentItem(), Toast.LENGTH_SHORT).show();
                if (pager.getCurrentItem() == 0) {
                    pager.setCurrentItem(getItem(1),true);
                }else if (pager.getCurrentItem() == 1)
                        pager.setCurrentItem(getItem(-1),true);
                }
        });

    }

    private int getItem(int i) {
        return pager.getCurrentItem() + i;
    }

}
