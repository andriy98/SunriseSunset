package com.example.andrii.sunrisesunset;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import at.markushi.ui.CircleButton;

public class Editloc_fragment extends Fragment {
    private static final int LAYOUT = R.layout.editloc_fr;
    private View view;
    private EditText search;
    private Button but_search;
    private CircleButton get_info;
    private TextView cur_lat, cur_long,textView,city;
    private Double lat;
    private Double longt;
    private Show_fragment show_fragment;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Edit Location");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        search = view.findViewById(R.id.search);
        but_search = view.findViewById(R.id.but_search);
        cur_lat = view.findViewById(R.id.cur_lat);
        cur_long = view.findViewById(R.id.cur_long);
        show_fragment = new Show_fragment();
        get_info = view.findViewById(R.id.get_info);
        textView = view.findViewById(R.id.textView2);
        city = view.findViewById(R.id.city);
        get_info.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        but_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ConnectivityManager connectivityManager =
                            (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
                    if (networkinfo == null) {
                        Toast.makeText(getContext(), "Check your internet connection !",Toast.LENGTH_LONG).show();
                    }else {
                        if(search.length()>0) {
                            getLongLat();
                        }
                        else {
                            Toast.makeText(getContext(), "Please, enter city name !",Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        get_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Long", String.valueOf(longt));
                bundle.putString("Lat", String.valueOf(lat));
                show_fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_main, show_fragment).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

    return view;
    }
    public void getLongLat() throws JSONException {
        if(Geocoder.isPresent()){
            try {
                String location = String.valueOf(search.getText());
                Geocoder gc = new Geocoder(getContext());
                List<Address> addresses= gc.getFromLocationName(location, 5); // get the found Address Objects
                if(addresses.size()>0) {
                    Address address = addresses.get(0);
                    lat = address.getLatitude();
                    longt = address.getLongitude();
                    cur_long.setText("Longitude: "+longt);
                    cur_lat.setText("Latitude: "+lat);
                    city.setText("City:  "+search.getText());
                    get_info.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getContext(),"There is no information about this city",Toast.LENGTH_LONG).show();
                    cur_lat.setText("");
                    cur_long.setText("");
                    city.setText("");
                    get_info.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }
            } catch (IOException e) {
                // handle the exception
            }
        }
    }



}
