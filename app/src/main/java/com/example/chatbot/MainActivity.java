package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.ConversationActions;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button sendmsg;
    EditText msg;
    ListView msglist;
    ArrayAdapter<String> arrayAdapter;
    private ArrayList<Message> arrayList = new ArrayList<Message>(100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendmsg = findViewById(R.id.sendMessageButton);
        msg = findViewById(R.id.usermessage);
        sendmsg.setOnClickListener(this);
        msglist = findViewById(R.id.messageHistoryList);

    }

    @Override
    public void onClick(View v) {
        String strmsg = msg.getText().toString().trim();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.5:3000/api/v1/bots/bibhu/converse/userId";
        JSONObject jsonBody = new JSONObject();
        Message m=new Message();
        m.setMessage(strmsg);
        m.setSelf(true);
        arrayList.add(m);
        customadapter c=new customadapter(getApplicationContext(),arrayList);
        msglist.setAdapter(c);
        try {
            jsonBody.put("type", "text");
            jsonBody.put("text", strmsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url,jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        Log.i("response",response.toString());
                        try {
                            JSONArray jsonArray=response.getJSONArray("responses");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                if(c.getString("type").equals("text")) {
                                    String text = c.getString("text");
                                    Log.i("responserrr", text);
                                    Message m=new Message();
                                    m.setMessage(text);
                                    m.setSelf(false);
                                    arrayList.add(m);
                                    customadapter d=new customadapter(getApplicationContext(),arrayList);
                                    msglist.setAdapter(d);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error",error.toString());

                    }

                });
        requestQueue.add(jsonObjectRequest);
    }
}
