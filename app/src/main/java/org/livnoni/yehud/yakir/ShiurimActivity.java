package org.livnoni.yehud.yakir;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

public class ShiurimActivity extends AppCompatActivity {

    String sheetUrl = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQ95GwooKs7aU4WUIuMEKkx4bYkU8ilVzy1LJ8Kn318XL7yNnz7UjNf5tDIafTCBYinUoA70wcyhA4L/pubhtml";
    private ProgressDialog mProgressDialog;
    public StaticClass.Shuirs shuirs;
    TextView dayNameTV;
    int dayNumber;
    final String [] dayName={"ראשון","שני","שלישי","רביעי","חמישי","שישי","שבת"};
    RadioButton[] radioButtons;
    LinearLayout LinearLayoutForBtns;



    //For swipe:
    float x1,x2;
    public static final int swipeSensitivity = 200;  //[0 is highest Sensitivity]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiurim);

        forceEnglishView();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dayNameTV = (TextView) findViewById(R.id.dayNameTV);

        radioButtons = new RadioButton[7];
        LinearLayoutForBtns = (LinearLayout) findViewById(R.id.LinearLayoutForBtns);

        updateDayNumber();
        Log.d("currentDayName=",dayName[dayNumber-1]+"");
        Log.d("dayNumber=",dayNumber+"");
        initializelUI();
        initializelRadioButtons();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("מתחבר לשרת...");
        mProgressDialog.setMessage("טוען שיעורים");

        mProgressDialog.show();


        new grabData().execute();


    }

    public class grabData extends AsyncTask<Void,Void,Void> {
        String[][] trtd;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(sheetUrl).get();
                Elements tables = doc.select("table[class=waffle]");

                int size = tables.select("tr").size();
                trtd = new String[size-3][];
                Log.d("table size=",size+"");
                for(int i=3; i<size; i++ )
                {
                    Element tr = tables.select("tr").get(i);
                    Log.d("table["+i+"]",tr.text());
                    trtd[i-3] = new String[tr.select("td").size()];
                    for(int j =0; j<tr.select("td").size(); j++ )
                    {
                        trtd[i-3][j]= tr.select("td").get(j).text();
                        Log.d("tr["+i+"]td["+j+"]",trtd[i-3][j]);
                    }
                }
                //todo: create class that get the trtd [][]...

                shuirs = new StaticClass.Shuirs(trtd);
                Log.d("ShiurimLog",shuirs.toString()+"");




                Log.d("ShiurimLog","FINISH------------------------------------------");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                Log.d("Exeption!", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();

        }

        @Override
        protected void onPreExecute(){
            mProgressDialog.show();
        }

    }

    public void updateDayNumber()
    {
        Calendar calendar = Calendar.getInstance();
        dayNumber = calendar.get(Calendar.DAY_OF_WEEK); ;
    }

    public void initializelUI()
    {
        dayNameTV.setText("יום "+dayName[dayNumber-1]);
    }

    public void initializelRadioButtons()
    {
        try
        {
            if(radioButtons[0] == null)               //this for first run
            {
                for(int i=0; i<radioButtons.length; i++)
                {
                    radioButtons[i] = new RadioButton(this);
                    radioButtons[i].setClickable(false);
                    if(i==dayNumber-1)
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
                    if(i==dayNumber-1)
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
        catch (Exception e)
        {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        for(int i=0; i<7; i++)
        {
            menu.add(Menu.NONE,i,Menu.NONE,dayName[i]);
        }

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        dayNumber = menuItem.getItemId()+1;
        if(menuItem.getItemId() == android.R.id.home) //back button have pressed
        {
            onBackPressed();
        }
        else                                        //menu buttons have pressed.
        {
            initializelUI();
            initializelRadioButtons();
        }
        return super.onOptionsItemSelected(menuItem);

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

                //if right to left sweep event on screen
                if (x1 - x2 > swipeSensitivity)
                {
                    if(dayNumber>1)
                    {
                        dayNumber--;
                        initializelUI();
                        initializelRadioButtons();
//                        initializelRadioButtons();
                    }
                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_SHORT).show();
                }

                // if left to right sweep event on screen
                if (x2 - x1 > swipeSensitivity)
                {
                    if(dayNumber<7)
                    {
                        dayNumber++;
                        initializelUI();
                        initializelRadioButtons();
                    }
                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_SHORT).show();
                }


                break;
            }
        }
        return false;
    }







    public void forceEnglishView()
    {
        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
}
