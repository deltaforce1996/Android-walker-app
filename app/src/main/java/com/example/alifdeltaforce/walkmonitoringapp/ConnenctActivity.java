package com.example.alifdeltaforce.walkmonitoringapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ConnenctActivity extends AppCompatActivity {

    private Toolbar mToolsbar;
    private ImageView btn_scanner;
    private IntentIntegrator qrscan;
    private EditText txt_show_qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connenct);

        btn_scanner = (ImageView) findViewById(R.id.button_scanner);
        mToolsbar = (Toolbar) findViewById(R.id.tools_bar_connect);
        txt_show_qrCode = (EditText) findViewById(R.id.edt_txt_getQrcode);

        setSupportActionBar(mToolsbar);
        getSupportActionBar().setTitle("Connect");
        mToolsbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        qrscan = new IntentIntegrator(this);

        mToolsbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnenctActivity.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrscan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                qrscan.setPrompt("Scan");
                qrscan.setCameraId(0);
                qrscan.setBeepEnabled(true);
                qrscan.setBarcodeImageEnabled(false);
                qrscan.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult != null){
            if (intentResult.getContents() == null){
                super.onActivityResult(requestCode, resultCode, data);
//                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }else {
                txt_show_qrCode.setText(""+intentResult.getContents());
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
