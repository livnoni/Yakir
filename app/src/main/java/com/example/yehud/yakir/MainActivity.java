package com.example.yehud.yakir;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    private ProgressDialog mProgressDialog;


    String sheetUrl = "https://docs.google.com/spreadsheets/d/1AercbZdDUV5AhMFT7YTCHLsHGzxmY1HCynpt9qw-zyM/pubhtml#";
    DataObject dataObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("מתחבר לשרת...");
        mProgressDialog.setMessage("טוען זמני תפילות");


        dataObject = new DataObject();
        new grabData().execute();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new grabData().execute();
            }
        });

    }


    public class grabData extends AsyncTask<Void,Void,Void>{
        String[][] trtd;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(sheetUrl).get();
                Elements tables = doc.select("table[class=waffle]");
                for (Element table : tables) {
                    Elements trs = table.select("tr");
                    trtd = new String[trs.size()][];
                    for (int i = 0; i < trs.size(); i++) {
                        Elements tds = trs.get(i).select("td");
                        trtd[i] = new String[tds.size()];
                        for (int j = 0; j < tds.size(); j++) {
                            trtd[i][tds.size()-j-1] = tds.get(j).text();
//                            trtd[i][j] = tds.get(j).text();
                        }
                    }
                    // trtd now contains the desired array for this table
                    for(int i=0; i<trtd.length; i++)
                    {
                        Log.d("trtd["+i+"]", Arrays.toString(trtd[i]));
                    }
                    dataObject.addMinyan(trtd);

                }

                Log.d("grabData","FINISH------------------------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            dataObject.printData();

        }

        @Override
        protected void onPreExecute(){
            mProgressDialog.show();
        }

    }

    public class DataObject
    {
        ArrayList<String[][]> minyanArrayList;

        public DataObject()
        {
            minyanArrayList = new ArrayList<String[][]>();
        }
        public void addMinyan(String [][] minyan)
        {
            minyanArrayList.add(minyan);
        }
        public void printData()
        {
            for(int i=0; i<minyanArrayList.size(); i++){
                for(int j=0; j<minyanArrayList.get(i).length; j++){
                    Log.d("Minyan_"+i,Arrays.toString(minyanArrayList.get(i)[j]));
                }
            }
        }

//        public class Minyan
//        {
//            String name;
//            Times weekday;
//            Times saturday;
//
//            public Minyan(Times weekday, Times saturday)
//            {
//                this.weekday = weekday;
//                this.saturday = saturday;
//            }
//        }
//        public class Times
//        {
//            String [] morningPray;
//            String [] middlePray;
//            String [] eveningPray;
//
//            public Times(String [] morningPray ,String [] middlePray, String [] eveningPray)
//            {
//                this.morningPray = morningPray;
//                this.middlePray = middlePray;
//                this.eveningPray = eveningPray;
//            }
//
//        }

    }
}
