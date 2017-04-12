package com.ipl.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.ipl.R;
import com.ipl.model.UserDetailsModel;

import java.util.ArrayList;


public class UserDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initView();
    }

    /**
     * This method is for the initialization of component
     */
    private void initView() {
        final EditText edtName = (EditText) findViewById(R.id.activity_user_details_edt_user_name);
        final EditText edtEmail = (EditText) findViewById(R.id.activity_user_details_edt_user_email);
        final EditText edtNickName = (EditText) findViewById(R.id.activity_user_details_edt_user_nick_name);


    }
}
