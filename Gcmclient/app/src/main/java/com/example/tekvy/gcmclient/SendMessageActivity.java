package com.example.tekvy.gcmclient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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


public class SendMessageActivity extends ActionBarActivity {

    EditText header_editText, content_ediitText;
    Button send_button;
    String mainResponse , headerTxt, contentTxt;
    OkHttpClient client;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        header_editText= (EditText) findViewById(R.id.header);
        content_ediitText = (EditText) findViewById(R.id.content);
        send_button = (Button) findViewById(R.id.button);
        client = new OkHttpClient();
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                headerTxt = header_editText.getText().toString();
                 contentTxt = content_ediitText.getText().toString();


                  run("http://devp.herokuapp.com/sendgcm");
            }
        });

    }

    public void run(String url) {

        RequestBody formBody = new FormEncodingBuilder()
                .add("header", headerTxt)
                .add("user_email", email)
                .add("content", contentTxt)
                .build();

        Request request = new Request.Builder().url(url).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                // TODO Auto-generated method stub

                mainResponse = arg0.body().string();
                Log.d("RESPONSE STRING", mainResponse);
                if (mainResponse.contains("email")) {



                    SendMessageActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(SendMessageActivity.this,
                                    "Message successfully sent",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {

                    SendMessageActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(SendMessageActivity.this,
                                    "Unable to send message .", Toast.LENGTH_LONG)
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
