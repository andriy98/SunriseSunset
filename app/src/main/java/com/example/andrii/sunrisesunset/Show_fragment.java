package com.example.andrii.sunrisesunset;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;

public class Show_fragment extends Fragment {

    private static final int LAYOUT = R.layout.show_fr;
    private View view;
    private String lat, longt;
    private Bundle bundle;
    private String response;
    private TextView first_ligth, sunrise, last_ligth, sunset, day_length, cur_lat, cur_long;
    private ProgressDialog progressDialog;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Show information");
        progressDialog = ProgressDialog.show(getContext(),"Loading","Please wait...",false,false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        first_ligth = view.findViewById(R.id.first_light);
        sunrise = view.findViewById(R.id.sunrise_time);
        last_ligth = view.findViewById(R.id.last_light);
        sunset = view.findViewById(R.id.sunset_time);
        day_length = view.findViewById(R.id.day_length_text);
        cur_lat = view.findViewById(R.id.cur_lat);
        cur_long = view.findViewById(R.id.cur_long);
        bundle = this.getArguments();
        if (bundle != null) {
            lat = bundle.getString("Lat");
            longt = bundle.getString("Long");
        }
        new DownloadPageTask().execute("https://api.sunrise-sunset.org/json?lat="+ lat+"&lng="+longt+"&date=today");
        return view;
    }


    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadOneUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            response = result;
            parseResult();
            super.onPostExecute(result);
        }

        private String downloadOneUrl(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    int read = 0;
                    while ((read = inputStream.read()) != -1) {
                        bos.write(read);
                    }
                    byte[] result = bos.toByteArray();
                    bos.close();

                    data = new String(result);

                } else {
                    data = connection.getResponseMessage() + " . Error Code : " + responseCode;
                }
                connection.disconnect();
                //return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }
    }
    public void parseResult(){
        if(response.length()>1){
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonRes = (JSONObject) jsonObject.get("results");
                first_ligth.setText("First ligth at "+jsonRes.getString("civil_twilight_begin"));
                sunrise.setText(jsonRes.getString("sunrise"));
                last_ligth.setText("Last light at "+jsonRes.getString("civil_twilight_end"));
                sunset.setText(jsonRes.getString("sunset"));
                day_length.setText("Day length: " + jsonRes.getString("day_length"));
                cur_long.setText("Current longitude: "+longt);
                cur_lat.setText("Current latitude: "+lat);
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                progressDialog.dismiss();
            }
        }
    }
}