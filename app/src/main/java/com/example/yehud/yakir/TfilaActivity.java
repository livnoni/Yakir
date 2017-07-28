package com.example.yehud.yakir;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;



public class TfilaActivity extends AppCompatActivity
{
    public int numOfMinayans;
    public int currentMinyanIndex;
    // FOr get the Objject of all the data: MainActivity.minyansVector.get(currentMinyanIndex)

    TextView minyanTitleTV,week_sharitTimesTV,week_MinhaTimesTV,week_ArvitTimesTV,saturday_sharitTimesTV,saturday_MinhaTimesTV,saturday_ArvitTimesTV;
    RadioButton[] radioButtons;
    LinearLayout LinearLayoutForBtns;

    //For swipe:
    float x1,x2;
    public static final int swipeSensitivity = 200;  //[0 is highest Sensitivity]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfila);

        numOfMinayans = MainActivity.minyansVector.size();
        currentMinyanIndex =0;
        radioButtons = new RadioButton[numOfMinayans];
        LinearLayoutForBtns = (LinearLayout) findViewById(R.id.LinearLayoutForBtns);


        initializelUI();
        initializelRadioButtons();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        for(int i=0; i<numOfMinayans; i++)
        {
            menu.add(Menu.NONE,i,Menu.NONE,MainActivity.minyansVector.get(i).name);
        }

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        currentMinyanIndex = menuItem.getItemId();
        initializelUI();
        initializelRadioButtons();
        return super.onOptionsItemSelected(menuItem);
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

    public void initializelRadioButtons()
    {
        if(radioButtons[0]==null)               //this for first run
        {
            for(int i=0; i<radioButtons.length; i++)
            {
                radioButtons[i] = new RadioButton(this);
                radioButtons[i].setClickable(false);
                if(i==currentMinyanIndex)
                {
                    radioButtons[i].setChecked(true);
                }
                else
                {
                    radioButtons[i].setChecked(false);
                }
                LinearLayoutForBtns.addView(radioButtons[i]);
            }
        }
        else                                   //just update the relevant radio button to turn on
        {
            for(int i=0; i<radioButtons.length; i++)
            {
                if(i==currentMinyanIndex)
                {
                    radioButtons[i].setChecked(true);
                }
                else
                {
                    radioButtons[i].setChecked(false);
                }
            }

        }

    }

    // onTouchEvent () method gets called when User performs any touch event on screen
    // Method to handle touch event like left to right swap and right to left swap
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                Log.d("POS=","x1="+x1);
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();
                Log.d("POS=","x2="+x2);

                //if left to right sweep event on screen
                if (x2 - x1 > swipeSensitivity)
                {
                    if(currentMinyanIndex<numOfMinayans-1)
                    {
                        currentMinyanIndex++;
                        initializelUI();
                        initializelRadioButtons();
                    }
//                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_SHORT).show();
                }

                // if right to left sweep event on screen
                if (x1 - x2 > swipeSensitivity)
                {
                    if(currentMinyanIndex>0)
                    {
                        currentMinyanIndex--;
                        initializelUI();
                        initializelRadioButtons();
                    }
//                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_SHORT).show();
                }


                break;
            }
        }
        return false;
    }


}
