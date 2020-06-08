package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
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
    private ArrayList<String> buttonlist = new ArrayList<String>(100);

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
        msg.setText("");
        requestbot(strmsg);
    }
    public void addbutton(ArrayList btnlist)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final LinearLayout verticalLayout= (LinearLayout)findViewById(R.id.buttonlayout);
        verticalLayout.removeAllViews();
        int numberOfRows =0;
        int TotalButtons=btnlist.size();
        int counter=0;
        if(btnlist.size()%2==0){
            numberOfRows=TotalButtons/2;
        }
        else {
            numberOfRows=(TotalButtons/2)+1;
        }
        int numberOfButtonsPerRow =2;
        int buttonIdNumber = 0;
        for(int i=0;i<numberOfRows;i++){
            LinearLayout newLine = new LinearLayout(this);
            newLine.setLayoutParams(params);
            newLine.setOrientation(LinearLayout.HORIZONTAL);
            for(int j=0;j<numberOfButtonsPerRow;j++) {
                if (counter < TotalButtons) {
                    final Button button = new Button(this);
                    // You can set button parameters here:
                    params.weight=1;
                    button.setTag(btnlist.get(buttonIdNumber).toString());
                    button.setLayoutParams(params);
                    button.setText(btnlist.get(buttonIdNumber).toString());//button text
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            requestbot(button.getTag().toString());
                        }
                    });
                    newLine.addView(button);
                    buttonIdNumber++;
                    counter++;
                }

            }
            verticalLayout.addView(newLine);
        }
    }
    public void requestbot(String msg)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.8:3000/api/v1/bots/satyam/converse/userId";
        JSONObject jsonBody = new JSONObject();
        Message m=new Message();
        m.setMessage(msg);
        m.setSelf(true);
        arrayList.add(m);
        customadapter c=new customadapter(getApplicationContext(),arrayList);
        msglist.setAdapter(c);
        try {
            jsonBody.put("type", "text");
            jsonBody.put("text", msg);
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
                                if(c.getString("type").equals("custom")){
                                    buttonlist.clear();
                                    JSONArray qr=c.getJSONArray("quick_replies");
                                    for (int nr=0;nr<qr.length();nr++){
                                        JSONObject obj=qr.getJSONObject(nr);
                                        String btntext=obj.getString("title");
                                        buttonlist.add(btntext);
                                    }
                                    Log.i("btnlist",buttonlist.toString());
                                    addbutton(buttonlist);
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
