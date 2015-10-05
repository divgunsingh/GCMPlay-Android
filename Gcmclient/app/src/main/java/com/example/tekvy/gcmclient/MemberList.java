package com.example.tekvy.gcmclient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MemberList extends ActionBarActivity {

    OkHttpClient client;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<MemberListModel> modelList;
    String mainResponse;
    MemberListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        client = new OkHttpClient();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Toast.makeText(MemberList.this, "Loading",Toast.LENGTH_LONG ).show();

        modelList=new ArrayList<MemberListModel>();
        listAdapter = new MemberListAdapter(modelList,getApplicationContext());
        mAdapter=listAdapter;
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        try {
            runPostRequest();

        }
        catch (Exception e){
            e.printStackTrace();
        }


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click

                // Toast.makeText(getApplicationContext(),  position,Toast.LENGTH_LONG).show();

                       String ss= Integer.toString(position);

                         String email = modelList.get(position).getEmail();
                        Log.d(ss,email);

                        Intent i = new Intent(MemberList.this, SendMessageActivity.class);
                        i.putExtra("email",email);
                        startActivity(i);

                    }
                })
        );






    }

    public void runPostRequest(){

        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noCache().build())
                .url("http://devp.herokuapp.com/getusers")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                mainResponse=response.body().string();
                response.body().close();
                Log.d("Response", mainResponse);
                if(mainResponse.contains("users")){

                    MemberList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            List<MemberListModel> latestList= parseData(mainResponse);


                        }
                    });


                }

            }
        });
    }

    public List<MemberListModel> parseData(String s){

        try {
            JSONObject jsonResponse = new JSONObject(s);
            JSONArray ar = jsonResponse.getJSONArray("users");
           // List<MemberListModel> modelList = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                MemberListModel model = new MemberListModel();

                model.setName(obj.getString("name"));
                model.setEmail(obj.getString("email"));
                model.setId(obj.getInt("id"));
                model.setGcm_regid(obj.getString("gcm_regid"));

                Log.d("Obj- JSOn", obj.toString());
                modelList.add(model);
                //listAdapter.add(model,i);
                mAdapter.notifyDataSetChanged();

            }
            return modelList;
        }catch (Exception e){

            e.printStackTrace();
            return null;
        }

    }


}
