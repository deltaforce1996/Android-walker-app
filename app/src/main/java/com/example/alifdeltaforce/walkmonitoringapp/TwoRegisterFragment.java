package com.example.alifdeltaforce.walkmonitoringapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.alifdeltaforce.walkmonitoringapp.Api.InternetConnection;
import com.example.alifdeltaforce.walkmonitoringapp.Model.User;
import com.example.alifdeltaforce.walkmonitoringapp.ServerInterface.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoRegisterFragment extends Fragment {

    private RadioGroup radioGroup;
    private EditText edt_email,edt_username,edt_pass,edt_con,edt_age;
    private Server list_server;
    private String userGender;
    private Button btn_register;

    public TwoRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two_register, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_email = (EditText) view.findViewById(R.id.edit_register_txt_email);
        edt_username = (EditText) view.findViewById(R.id.edit_register_txt_username);
        edt_pass= (EditText) view.findViewById(R.id.edit_register_txt_password);
        edt_con = (EditText) view.findViewById(R.id.edit_register_txt_confrim);
        edt_age = (EditText) view.findViewById(R.id.edit_register_txt_Age);
        btn_register = (Button) view.findViewById(R.id.button_register);

        radioGroup = (RadioGroup) view.findViewById(R.id.group_radio_button);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_male:
                        userGender = "MALE";
                        Toast.makeText(getContext(), "MALE" +userGender, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_female:
                        userGender = "FEMALE";
                        Toast.makeText(getContext(), "FEMALE" +userGender, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_server = InternetConnection.getApiCline().create(Server.class);
                RegisterUser();
            }
        });

    }

    private int userAge;

    private void RegisterUser(){

        String userEmail = edt_email.getText().toString();
        String userName = edt_username.getText().toString();
        String userPass = edt_pass.getText().toString();
        String userCon = edt_con.getText().toString();
        String user_Age = edt_age.getText().toString();

        if (!userEmail.isEmpty()&&!userName.isEmpty()&&!userPass.isEmpty()&&!userCon.isEmpty()&&!user_Age.isEmpty()){


            if (userPass.length() <= 6) {

                userAge = Integer.parseInt(user_Age);

                Call<User> calls = list_server.create_new_User(userEmail, userName, userPass, userAge, userGender);
                calls.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Snackbar.make(getView(), "" + response.body().getMassage(), Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Snackbar.make(getView(), ""+t.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });

            }else {
                Snackbar.make(getView(),"Pass Word is 6 Text !", Snackbar.LENGTH_SHORT).show();
            }

        }else {
            Snackbar.make(getView(),"Some value empty", Snackbar.LENGTH_SHORT).show();
        }

    }

}
