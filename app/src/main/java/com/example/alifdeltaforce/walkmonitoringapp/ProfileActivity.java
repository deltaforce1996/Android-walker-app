package com.example.alifdeltaforce.walkmonitoringapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alifdeltaforce.walkmonitoringapp.Api.InternetConnection;
import com.example.alifdeltaforce.walkmonitoringapp.Model.User;
import com.example.alifdeltaforce.walkmonitoringapp.ServerInterface.Server;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mActionBarToolbar;
    private ImageView img_bg;
    private TextView tv_name,tv_level,tv_age,tv_gender;
    private CircleImageView iv_profile;
    private Server server;
    private String email;
    private RelativeLayout relativeLayout;
    private Button btn_delete,btn_edit;
    private Bitmap bitmap,logo,rebitmap;
    private String save_name,save_age,uri_txt;
    private ProgressDialog progressDialog;
    private BackgroundWork backgroundWork;
    private static final String TAG = "ProfileActivity WORK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tv_name = (TextView) findViewById(R.id.name_sh);
        tv_level = (TextView) findViewById(R.id.level_sh);
        tv_age = (TextView) findViewById(R.id.age_sh);
        tv_gender = (TextView) findViewById(R.id.gender_sh);
        iv_profile = (CircleImageView) findViewById(R.id.pofile_img);
        relativeLayout = (RelativeLayout) findViewById(R.id.view_pro);
        btn_delete = (Button) findViewById(R.id.button_delete);
        btn_edit = (Button) findViewById(R.id.button_edit);
        img_bg = (ImageView) findViewById(R.id.bg_image);


        server = InternetConnection.getApiCline().create(Server.class);

        mActionBarToolbar = (Toolbar) findViewById(R.id.tools_bar_profile);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Profile");
        mActionBarToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_delete.setOnClickListener(this);
        btn_edit.setOnClickListener(this);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        SharedPreferences sp = getSharedPreferences("user_login", Context.MODE_PRIVATE);
        email = sp.getString("userEmail","");

         GetuserData();
    }

    private void GetuserData(){

        Call<User> getDetaile = server.GetUserDataDetail_fix(email);
        getDetaile.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                  try {
                         tv_name.setText(""+response.body().getUserDetail().getUserName());
                         tv_level.setText("Level "+response.body().getUserDetail().getUserLevel());
                         tv_age.setText("Age : "+response.body().getUserDetail().getUserAge()+" Years old");
                         tv_gender.setText("Gender : "+response.body().getUserDetail().getUserGender());


                         save_name = response.body().getUserDetail().getUserName();
                         save_age = response.body().getUserDetail().getUserAge();

                         uri_txt = response.body().getUserDetail().getUserImg();

                       Snackbar.make(relativeLayout,""+uri_txt,Snackbar.LENGTH_SHORT).show();

                     if (response.body().getUserDetail().getUserImg()== null||response.body().getUserDetail().getUserImg()==""){

                         img_bg.setImageResource(R.drawable.profile_test);
                         iv_profile.setImageResource(R.drawable.profile_test);
                         Log.d(TAG,"Thread Working");

                     }else {

                         backgroundWork = new BackgroundWork();
                         backgroundWork.start();

                     }
                  }catch (Exception e){
                         Toast.makeText(ProfileActivity.this, "Exception e", Toast.LENGTH_SHORT).show();
                  }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DeleteUser(){
        Call<User>user_delete = server.deleteUser(email);
            user_delete.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                   Snackbar.make(relativeLayout,""+response.body().getMassage(),Snackbar.LENGTH_SHORT).show();

                    SharedPreferences preferences = getSharedPreferences("user_login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.remove("userEmail");
                    edit.remove("userPass");
                    edit.commit();

                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Snackbar.make(relativeLayout,"onFailure",Snackbar.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onClick(View v) {
      if (v == btn_delete){
          AlertDialog();
      }else if (v == btn_edit){
          Dialog_EditPofile();
      }
    }

    private void AlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setMessage("Do you want delete your account");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteUser();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final int REQUEST_GALLERY = 1;

    private void UploadProfile(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
        Log.d(TAG,"UploadProfile : Work");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                rebitmap = Bitmap.createScaledBitmap(bitmap, 780, 680, true);
                Log.d(TAG,"Resize Image");
                add_pic.setImageBitmap(rebitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d(TAG,"Resize Image FileNotFoundException");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG,"Resize Image IOException");
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private CircleImageView add_pic;

    private void Dialog_EditPofile() {

        final AlertDialog.Builder dialog_edit = new AlertDialog.Builder(ProfileActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_edit_pofile, null);
        final EditText edit_n = (EditText) mView.findViewById(R.id.edt_p_name);
        final EditText edit_a = (EditText) mView.findViewById(R.id.edt_p_age);
        final Button button_p = (Button) mView.findViewById(R.id.button_edit_d);
        add_pic = (CircleImageView) mView.findViewById(R.id.edt_picccc);

        dialog_edit.setView(mView);
        final AlertDialog dialog = dialog_edit.create();
        dialog.show();

        if (save_age != null && save_name != null) {
            edit_n.setText(save_name);
            edit_a.setText(save_age);
            Log.d(TAG,"save_age : save_name not null ");
        }

        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadProfile();
            }
        });

        button_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();

                String name = edit_n.getText().toString();
                String s_age = edit_a.getText().toString();

                if (name.isEmpty() && s_age.isEmpty()) {
                    Snackbar.make(relativeLayout,"Some value not null",Snackbar.LENGTH_SHORT).show();
                    Log.d(TAG,"save_age : save_name isEmpty() ");
                } else {

                    try {
                        uri_txt = getStringImage(rebitmap);
                        Log.d(TAG,"getStringImage(rebitmap) is compalte");
                    } catch (Exception e) {
                        uri_txt = "";
                    }

                    int age = Integer.parseInt(s_age);

                    Call<User> edit_request = server.EditUser(name, age, uri_txt, email);
                    edit_request.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
//                        Snackbar.make(relativeLayout,""+response.body().getMassage(),Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                            startActivity(getIntent());
                            Log.d(TAG,"Edit is compelte");
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Snackbar.make(relativeLayout, "onFailure", Snackbar.LENGTH_SHORT).show();
                            Log.d(TAG,"Edit is onFailure");
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Data...");
        progressDialog.show();
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            Log.d(TAG, "" + encodedImage);
            return encodedImage;
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

    class BackgroundWork extends Thread {
        @Override
        public void run() {
            super.run();
            logo = getBitmapFromURL(uri_txt);
            iv_profile.post(new Runnable() {
                @Override
                public void run() {
                    iv_profile.setImageBitmap(logo);
                    img_bg.setImageBitmap(logo);
                    Log.d(TAG,"Thread Working");
                }
            });
        }
    }

}

