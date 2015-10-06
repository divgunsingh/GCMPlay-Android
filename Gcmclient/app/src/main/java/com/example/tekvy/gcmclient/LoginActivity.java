package com.example.tekvy.gcmclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    OkHttpClient client;
    String mainResponse;
    EditText email_editText,password_editText;
    String emailstr,passwordstr;
    Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        client = new OkHttpClient();

        login_button = (Button) findViewById(R.id.login);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailstr = email_editText.getText().toString();
                passwordstr = password_editText.getText().toString();

            }
        });


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void sendLoginInfoToBackend(String url) {

        RequestBody formBody = new FormEncodingBuilder()
                .add("user_email", emailstr)
                .add("user_password", passwordstr)
                .build();

        Request request = new Request.Builder().url(url).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                // TODO Auto-generated method stub

                mainResponse = arg0.body().string();
                Log.d("RESPONSE STRING", mainResponse);
                if (mainResponse.contains("true")) {
                    LoginActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Intent i = new Intent(LoginActivity.this,MemberListActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } else {

                    LoginActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(LoginActivity.this,
                                    "Unable To Login", Toast.LENGTH_LONG)
                                    .show();

                        }
                    });

                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                // TODO Auto-generated method stub

            }
        });

    }



}
