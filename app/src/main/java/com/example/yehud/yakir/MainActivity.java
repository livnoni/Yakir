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
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    Tfila tfila;
    private ProgressDialog mProgressDialog;


    String docUrl = "https://docs.google.com/document/d/1SjlZiydYFpwctaTzcuVGx3C1tVT3DqFX-AOQh4vhHiw/pub";
    String sheetUrl = "https://docs.google.com/spreadsheets/d/1AercbZdDUV5AhMFT7YTCHLsHGzxmY1HCynpt9qw-zyM/pubhtml#";

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



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new doit().execute();
            }
        });

    }


    public class doit extends AsyncTask<Void,Void,Void>{
        String words;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(sheetUrl).get();
                Elements table = doc.select("table[class=waffle]");

                Iterator<Element> iterator = table.select("tr").iterator();
                int iter =1;
                while(iterator.hasNext()){
                    iter++;
                    String line = iterator.next().text();
                    words = words +line +"\n";
                    Log.i("JSOUP",line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textView.setText(words);
            tfila = new Tfila((words));
            mProgressDialog.dismiss();


        }

        @Override
        protected void onPreExecute(){
            mProgressDialog.show();
        }





    }
}
