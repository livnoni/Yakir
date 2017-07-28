package com.example.yehud.yakir;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Button goToTfilaCtivity;
    TextView textView;
    private ProgressDialog mProgressDialog;


    String sheetUrl = "https://docs.google.com/spreadsheets/d/1AercbZdDUV5AhMFT7YTCHLsHGzxmY1HCynpt9qw-zyM/pubhtml#";
    DataObject dataObject;
    public static Vector<staticClass.Minyan> minyansVector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("מתחבר לשרת...");
        mProgressDialog.setMessage("טוען זמני תפילות");

        minyansVector = new Vector<staticClass.Minyan>();
        dataObject = new DataObject();
        new grabData().execute();

        goToTfilaCtivity = (Button) findViewById(R.id.TfilaBtn);
        goToTfilaCtivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TfilaActivity.class));
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
                        }
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
            dataObject.parseData();
            printMinyansVectorData();

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
            Log.d("Minyan_","----------------------------------------------------------");

        }
        public void  parseData()
        {
            for(int i=0; i<minyanArrayList.size(); i++)
            {
                String tempMinyanName = minyanArrayList.get(i)[1][0];
                staticClass.TfilaTime tempShaharit = new staticClass.TfilaTime(minyanArrayList.get(i)[4]);
                staticClass.TfilaTime tempMinha = new staticClass.TfilaTime(minyanArrayList.get(i)[5]);
                staticClass.TfilaTime tempArvit = new staticClass.TfilaTime(minyanArrayList.get(i)[6]);
                staticClass.Tfila tempWeekday = new staticClass.Tfila(tempShaharit, tempMinha, tempArvit);

                staticClass.TfilaTime tempShaharit2 = new staticClass.TfilaTime(minyanArrayList.get(i)[9]);
                staticClass.TfilaTime tempMinha2 = new staticClass.TfilaTime(minyanArrayList.get(i)[10]);
                staticClass.TfilaTime tempArvit2 = new staticClass.TfilaTime(minyanArrayList.get(i)[11]);
                staticClass.Tfila tempsaturday = new staticClass.Tfila(tempShaharit2, tempMinha2, tempArvit2);
                staticClass.Minyan tempMinyan = new staticClass.Minyan(tempMinyanName , tempWeekday , tempsaturday);

                minyansVector.add(tempMinyan);
            }
        }
    }

//    public class Minyan
//    {
//        String name;
//        Tfila weekday,saturday;
//
//        public Minyan(String name, Tfila weekday , Tfila saturday)
//        {
//            this.name = name;
//            this.weekday = weekday;
//            this.saturday = saturday;
//        }
//        public String toString()
//        {
//            return "["+name+" -> weekday="+weekday.toString()+" , saturday="+saturday.toString()+"]";
//        }
//
//    }
//    public class Tfila
//    {
//        TfilaTime shaharit,minha,arvit;
//
//        public Tfila(TfilaTime shaharit, TfilaTime minha, TfilaTime arvit)
//        {
//            this.shaharit = shaharit;
//            this.minha = minha;
//            this.arvit = arvit;
//        }
//        public String toString()
//        {
//            return "[shaharit="+shaharit.toString()+"],["+"minha="+minha.toString()+"],["+"arvit="+arvit.toString()+"]";
//        }
//
//    }
//    public class TfilaTime
//    {
//        Vector<String> timesVector;
//
//        public TfilaTime(String [] data)
//        {
//            timesVector = new Vector<String>();
//            for(int i = data.length-1; i>=0; i--)
//            {
//                if(data[i]!=null && data[i]!="" && data[i]!=" ")
//                {
//                    timesVector.add(data[i]);
//                }
//            }
//        }
//        public String toString()
//        {
//            return timesVector.toString();
//        }
//    }
    public void printMinyansVectorData()
    {
        if(minyansVector==null)
        {
            Log.d("minyansVector=","NULL");
        }
        else
        {
            for(int i=0; i<minyansVector.size(); i++)
            {
                Log.d("PRINT:",minyansVector.get(i).toString());
            }

        }

    }
}
