package com.example.andrii.sunrisesunset;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Splash_fragment extends Fragment {
    private static final int LAYOUT = R.layout.splash_screen;
    private View view;
    private Main_fragment main_fragment;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        main_fragment = new Main_fragment();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_main, main_fragment).commit();
            }
        }, 3*1000);
    return view;
    }




}
