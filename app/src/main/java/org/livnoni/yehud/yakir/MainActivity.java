package org.livnoni.yehud.yakir;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Calendar;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Button goToTfilaCtivity;
    private ProgressDialog mProgressDialog;
    TextView welcomeView ,closestMinyansTV, updateMsgTV;


    String sheetUrl = "https://docs.google.com/spreadsheets/d/1AercbZdDUV5AhMFT7YTCHLsHGzxmY1HCynpt9qw-zyM/pubhtml#";
    String docUrl = "https://docs.google.com/document/d/1SjlZiydYFpwctaTzcuVGx3C1tVT3DqFX-AOQh4vhHiw/pub";
    DataObject dataObject;
    String updateMsg ="";
    public static Vector<staticClass.Minyan> minyansVector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        goToTfilaCtivity = (Button) findViewById(R.id.TfilaBtn);
        goToTfilaCtivity.setBackgroundColor(Color.RED);

        welcomeView = (TextView) findViewById(R.id.welcomeView);
        closestMinyansTV = (TextView) findViewById(R.id.closestMinyansTV);
        updateMsgTV = (TextView) findViewById(R.id.updateMsgTV);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("מתחבר לשרת...");
        mProgressDialog.setMessage("טוען זמני תפילות");

        minyansVector = new Vector<staticClass.Minyan>();
        dataObject = new DataObject();
        new grabData().execute();

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


                doc = Jsoup.connect(docUrl).get();
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
            dataObject.printData();
            dataObject.parseData();
            goToTfilaCtivity.setBackgroundColor(Color.GREEN);
            showWelcomeText();
            showUpdateMsg();
            try {
                printMinyansVectorData();
                showClosestMinyans();
            } catch (ParseException e) {
                e.printStackTrace();
            }
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



    public void showWelcomeText()
    {
        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int mintueOfDay = c.get(Calendar.MINUTE);

        Log.d("Calendar=",hourOfDay+"");

        if(hourOfDay >= 4 && hourOfDay < 12){
            welcomeView.setText("בוקר טוב");
        }else if(hourOfDay >= 12 && hourOfDay < 17){
            welcomeView.setText("צהריים טובים");
        }else if(hourOfDay >= 17 && hourOfDay < 21){
            welcomeView.setText("ערב טוב");
        }else if( (hourOfDay >= 21 && hourOfDay < 24) || (hourOfDay >= 0 && hourOfDay <= 4) ){
            welcomeView.setText("לילה טוב");
        }
    }

    public void showClosestMinyans() throws ParseException {
        Calendar c = Calendar.getInstance();
        String currentDat = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
        for(int i=0; i<minyansVector.size(); i++)
        {
            String [] closestDate = minyansVector.get(i).getNearestMinyans(currentDat);
            if(closestDate != null)
            {
                closestMinyansTV.append( closestDate[0]+": "+closestDate[1]+" ב-"+closestDate[2]+"\n");
                Log.d("ClosestDate=",closestDate[0]+","+closestDate[1]+","+closestDate[2]);
            }
        }
        if(closestMinyansTV.getText() == "")
        {
            currentDat = "00:01";
            for(int i=0; i<minyansVector.size(); i++)
            {
                String [] closestDate = minyansVector.get(i).getNearestMinyans(currentDat);
                if(closestDate != null)
                {
                    closestMinyansTV.append( closestDate[0]+": "+closestDate[1]+" ב-"+closestDate[2]+"\n");
                    Log.d("ClosestDate=",closestDate[0]+","+closestDate[1]+","+closestDate[2]);
                }
            }
        }
        if(closestMinyansTV.getText() == "")
        {
            closestMinyansTV.setTextColor(Color.RED);
            closestMinyansTV.append("אין מניינים בזמן הקרוב");
        }
    }


    public void printMinyansVectorData() throws ParseException {
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


    public void showUpdateMsg()
    {
        if(updateMsg.length()>1)
        {
            updateMsgTV.append(updateMsg);
            updateMsgTV.setMovementMethod(new ScrollingMovementMethod());
        }
        else
        {
            updateMsgTV.setTextColor(Color.RED);
            updateMsgTV.append("אין הודעות חדשות");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(Menu.NONE,0,Menu.NONE,"אודות");
        menu.add(Menu.NONE,1,Menu.NONE,"שלח פידבק");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case 0:
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setIcon(R.drawable.ic_info_black_24dp);
                alertDialog.setTitle("אודות");
                alertDialog.setMessage("אפליקציית יקיר"+"\n"+"מציגה מידע עדכני אודות זמני תפילות"+"\n"+"נכונות המידע בכפוף לעדכוני הגבאים"+"\n\n\n"+getString(R.string.copyright));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;
            case 1:
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "yehuda.livnoni@gmail.com" });
                Email.putExtra(Intent.EXTRA_SUBJECT, "פידבק לאפליקציית יקיר");
                Email.putExtra(Intent.EXTRA_TEXT, "היי, רציתי להגיד ש..." + "");
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
