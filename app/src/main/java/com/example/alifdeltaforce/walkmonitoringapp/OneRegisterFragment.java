package com.example.alifdeltaforce.walkmonitoringapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.alifdeltaforce.walkmonitoringapp.Api.InternetConnection;
import com.example.alifdeltaforce.walkmonitoringapp.Model.User;
import com.example.alifdeltaforce.walkmonitoringapp.ServerInterface.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneRegisterFragment extends Fragment {


    public OneRegisterFragment() {
        // Required empty public constructor
    }

    private Button btn_login;
    private EditText edt_email,edt_pass;
    private Server list_server;
    String status_userEmail,status_userPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_register, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sp = getActivity().getSharedPreferences("user_login", Context.MODE_PRIVATE);
        String status_userEmail = sp.getString("userEmail","");
        String status_userPass = sp.getString("userPass","");

        edt_email = (EditText) view.findViewById(R.id.edit_login_txt_email);
        edt_pass = (EditText) view.findViewById(R.id.edit_login_txt_pass);

        list_server = InternetConnection.getApiCline().create(Server.class);

        btn_login = (Button) view.findViewById(R.id.button_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin();

            }
        });

        if (status_userEmail != ""&&status_userPass != ""){
            Intent intent = new Intent(getActivity(), MenuActivity.class);
            startActivity(intent);
        }

    }


    private void CheckLogin(){

        String userEmail = edt_email.getText().toString();
        String userPass = edt_pass.getText().toString();

        if (!userEmail.isEmpty()&&!userPass.isEmpty()) {

            Call<User> call_user_login = list_server.check_login(userEmail, userPass);
            call_user_login.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    try {
                        Snackbar.make(getView(), "" + response.body().getMassage()+"  userID : "+response.body().getUserDetail().getUserID(), Snackbar.LENGTH_SHORT).show();

                        SharedPreferences preferences = getActivity().getSharedPreferences("user_login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("userEmail",response.body().getUserDetail().getUserEmail());
                        edit.putString("userPass",response.body().getUserDetail().getUserPass());
                        edit.commit();

                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        startActivity(intent);

                    }catch (Exception e) {
                        Snackbar.make(getView(), "" + response.body().getMassage(), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Snackbar.make(getView(), "onFailure", Snackbar.LENGTH_SHORT).show();
                }
            });

        }else {
            Snackbar.make(getView(), "Some value empty", Snackbar.LENGTH_SHORT).show();
        }
    }

//    ทำงานเมื่อมีการเช็คกรณี User ยังไม่ออกจากระบบ
    private void Autologin(){

        Call<User> Autologin = list_server.GetUserAutoLogin();
        Autologin.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    Snackbar.make(getView(), "" + response.body().getMassage()+"  userID : "+response.body().getUserDetail().getUserID(), Snackbar.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Snackbar.make(getView(), "" + response.body().getMassage(), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Snackbar.make(getView(), "onFailure", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

}
