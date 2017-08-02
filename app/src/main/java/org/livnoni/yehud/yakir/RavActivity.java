package org.livnoni.yehud.yakir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;

public class RavActivity extends AppCompatActivity {

    String docUrl = "https://docs.google.com/document/d/1FafW9e8EJFI4mFIPRBhCt7dyxC7HFEtSgUqi7r5Sa-A/pub";
    TextView ravMsgTV;
    String updateMsg ="";
    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rav);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ravMsgTV = (TextView) findViewById(R.id.ravMsgTV);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("מתחבר לשרת...");
        mProgressDialog.setMessage("טוען את דבר הרב");

        new grabData().execute();




    }


    public class grabData extends AsyncTask<Void,Void,Void> {
        String[][] trtd;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(docUrl).get();
                Elements spans = doc.select("div[id=contents]").select("span");
                for(Element span : spans){
                    updateMsg = updateMsg + span.text() +"\n";
                    Log.d("grabDocData","span["+span.id()+"]="+span.text());

                }
                Log.d("grabDocData",updateMsg);


                Log.d("grabDocData","FINISH------------------------------------------");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            showUpdateMsg();

        }

        @Override
        protected void onPreExecute(){
            mProgressDialog.show();
        }

    }

    public void showUpdateMsg()
    {
        if(updateMsg.length()>1)
        {
            ravMsgTV.append(updateMsg);
            ravMsgTV.setMovementMethod(new ScrollingMovementMethod());
        }
        else
        {
            ravMsgTV.setTextColor(Color.RED);
            ravMsgTV.append("אין הודעות חדשות");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return super.onOptionsItemSelected(menuItem);

    }
}
