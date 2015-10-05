package com.example.tekvy.gcmclient;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class RegisterActivity extends ActionBarActivity {

    Button submit_button, member_button;
    EditText name_editText,email_editText,password_editText;

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "580848980186";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Demo";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    String regid="";
    String namestr,emailstr,passwordstr,mainResponse;

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

          submit_button = (Button) findViewById(R.id.sumbit);
          name_editText = (EditText) findViewById(R.id.name);
          email_editText = (EditText) findViewById(R.id.email);
          password_editText = (EditText) findViewById(R.id.password);
          member_button  = (Button) findViewById(R.id.members);

          client = new OkHttpClient();


        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            //regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namestr= name_editText.getText().toString();
                emailstr = email_editText.getText().toString();
                passwordstr = password_editText.getText().toString();

                if(!regid.isEmpty()){

                    sendRegistrationIdToBackend("http://devp.herokuapp.com/register");

                }else{

                    registerInBackground();
                    Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_LONG).show();

                }

            }
        });

        member_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, MemberListActivity.class);
                startActivity(i);
            }
        });


    }

    //send data to 3rd party server

    public void sendRegistrationIdToBackend(String url) {

        RequestBody formBody = new FormEncodingBuilder()
                .add("user_name", namestr)
                .add("user_email", emailstr)
                .add("user_password", passwordstr)
                .add("gcm_regid", regid).build();

        Request request = new Request.Builder().url(url).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                // TODO Auto-generated method stub

                mainResponse = arg0.body().string();
                Log.d("RESPONSE STRING", mainResponse);
                if (mainResponse.contains(namestr)) {
                    RegisterActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(RegisterActivity.this,
                                    "Registration Successful",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {

                    RegisterActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(RegisterActivity.this,
                                    "Unable To Register", Toast.LENGTH_LONG)
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

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
              //sendRegistrationIdToBackend("http://devp.herokuapp.com");

                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // name_editText.setText(regid);

                         Toast.makeText(getApplicationContext(),regid,Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        }.execute(null, null, null);
    }



    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
