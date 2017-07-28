package com.example.yehud.yakir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TfilaActivity extends AppCompatActivity
{
    public int numOfMinayans;
    public int currentMinyanIndex;

    TextView minyanTitleTV,week_sharitTimesTV,week_MinhaTimesTV,week_ArvitTimesTV,saturday_sharitTimesTV,saturday_MinhaTimesTV,saturday_ArvitTimesTV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfila);

        numOfMinayans = MainActivity.minyansVector.size();
        currentMinyanIndex =0;


        initializelUI();


    }
    public void initializelUI()
    {
        minyanTitleTV = (TextView) findViewById(R.id.minyanTitleTV);

        week_sharitTimesTV = (TextView) findViewById(R.id.week_sharitTimesTV);
        week_MinhaTimesTV = (TextView) findViewById(R.id.week_MinhaTimesTV);
        week_ArvitTimesTV = (TextView) findViewById(R.id.week_ArvitTimesTV);
        saturday_sharitTimesTV = (TextView) findViewById(R.id.saturday_sharitTimesTV);
        saturday_MinhaTimesTV = (TextView) findViewById(R.id.saturday_MinhaTimesTV);
        saturday_ArvitTimesTV = (TextView) findViewById(R.id.saturday_ArvitTimesTV);

        minyanTitleTV.setText(MainActivity.minyansVector.get(currentMinyanIndex).name);

        week_sharitTimesTV.setText(Arrays.toString(MainActivity.minyansVector.get(currentMinyanIndex).weekday.shaharit.getTimes()));
        week_MinhaTimesTV.setText(Arrays.toString(MainActivity.minyansVector.get(currentMinyanIndex).weekday.minha.getTimes()));
        week_ArvitTimesTV.setText(Arrays.toString(MainActivity.minyansVector.get(currentMinyanIndex).weekday.arvit.getTimes()));


        saturday_sharitTimesTV.setText(Arrays.toString(MainActivity.minyansVector.get(currentMinyanIndex).saturday.shaharit.getTimes()));
        saturday_MinhaTimesTV.setText(Arrays.toString(MainActivity.minyansVector.get(currentMinyanIndex).saturday.minha.getTimes()));
        saturday_ArvitTimesTV.setText(Arrays.toString(MainActivity.minyansVector.get(currentMinyanIndex).saturday.arvit.getTimes()));




    }
}
