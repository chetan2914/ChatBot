package com.example.chatbot;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static androidx.core.content.ContextCompat.startActivity;

public class updatedata extends AsyncTask<String, String, String> {

    private Activity activity;
    public updatedata(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection conn = null;

        try {
            URL url;
            url = new URL(params[0]);
            conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String result="";
                String line="";
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                /*JSONObject json = new JSONObject(result);
                response=json;*/
                bufferedReader.close();
                inputStream.close();

                if(result.equals("success")==true)
                {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                }
                else
                {
                    System.out.print("not entered");
                }

            } else {
                InputStream err = conn.getErrorStream();
            }
            return "done";

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /*@Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }*/
}
