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
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Calendar;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Button goToTfilaCtivity, RavBtn;
    private ProgressDialog mProgressDialog;
    TextView welcomeView ,closestMinyansTV, updateMsgTV,HebrewDateTV,shabatTimesTV;


    public static String sheetUrl = "https://docs.google.com/spreadsheets/d/1AercbZdDUV5AhMFT7YTCHLsHGzxmY1HCynpt9qw-zyM/pubhtml#";
    public static String docUrl = "https://docs.google.com/document/d/1SjlZiydYFpwctaTzcuVGx3C1tVT3DqFX-AOQh4vhHiw/pub";
    public static String dateUrl = "http://www.hebcal.com/etc/hdate-he.xml";
    public static String shabatTimesUrl = "http://www.hebcal.com/shabbat/?cfg=json&geonameid=8199422&m=40";
    DataObject dataObject;
    String updateMsg ="";
    public static Vector<StaticClass.Minyan> minyansVector;
    public static String hebrewDate ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forceEnglishView();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        RavBtn =(Button) findViewById(R.id.RavBtn);
        goToTfilaCtivity = (Button) findViewById(R.id.TfilaBtn);
        changeButtonBackground(goToTfilaCtivity,true);
        changeButtonBackground(RavBtn,true);

        welcomeView = (TextView) findViewById(R.id.welcomeView);
        closestMinyansTV = (TextView) findViewById(R.id.closestMinyansTV);
        updateMsgTV = (TextView) findViewById(R.id.updateMsgTV);
        HebrewDateTV = (TextView) findViewById(R.id.HebrewDateView);
        shabatTimesTV = (TextView) findViewById(R.id.shabatTimesTV);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("מתחבר לשרת...");
        mProgressDialog.setMessage("טוען זמני תפילות");

        minyansVector = new Vector<StaticClass.Minyan>();
        dataObject = new DataObject();
        new grabData().execute();

        //Animation:
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);


        goToTfilaCtivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                startActivity(new Intent(MainActivity.this, TfilaActivity.class));
            }
        });

        RavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                startActivity(new Intent(MainActivity.this, RavActivity.class));
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


                doc = Jsoup.parse(new URL(dateUrl).openStream(), "UTF-8", "", Parser.xmlParser());
                hebrewDate = doc.select("title").last().text();
                Log.d("grabDateUrl",hebrewDate);
                Log.d("grabDateUrl","FINISH------------------------------------------");




                String json = Jsoup.connect(shabatTimesUrl).ignoreContentType(true).execute().body();

                JSONObject jsonObj = new JSONObject(json);

                Log.d("grabShabat",json.toString());

                String shabatName = jsonObj.getJSONArray("items").getJSONObject(1).getString("hebrew");
                Log.d("grabShabat",shabatName);
                String enterShabat = jsonObj.getJSONArray("items").getJSONObject(0).getString("title").substring(17);
                Log.d("grabShabat",enterShabat);
                String exitShabat = jsonObj.getJSONArray("items").getJSONObject(2).getString("title").substring(19);
                Log.d("grabShabat",exitShabat);

                StaticClass.ShabatInfo shabatInfo = new StaticClass.ShabatInfo(shabatName , enterShabat, exitShabat);

                Log.d("grabShabat","FINISH------------------------------------------");


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            dataObject.printData();
            dataObject.parseData();
            changeButtonBackground(goToTfilaCtivity,false);
            changeButtonBackground(RavBtn,false);
            showWelcomeText();
            showUpdateMsg();
            initialDate();
            showShabatTimes();
            try {
                printMinyansVectorData();
                showClosestMinyans();
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("ERROR!", "some error happened...");
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
                StaticClass.TfilaTime tempShaharit = new StaticClass.TfilaTime(minyanArrayList.get(i)[4]);
                StaticClass.TfilaTime tempMinha = new StaticClass.TfilaTime(minyanArrayList.get(i)[5]);
                StaticClass.TfilaTime tempArvit = new StaticClass.TfilaTime(minyanArrayList.get(i)[6]);
                StaticClass.Tfila tempWeekday = new StaticClass.Tfila(tempShaharit, tempMinha, tempArvit);

                StaticClass.TfilaTime tempKabalatShabat = new StaticClass.TfilaTime(minyanArrayList.get(i)[9]);
                StaticClass.TfilaTime tempShaharit2 = new StaticClass.TfilaTime(minyanArrayList.get(i)[10]);
                StaticClass.TfilaTime tempMinha2 = new StaticClass.TfilaTime(minyanArrayList.get(i)[11]);
                StaticClass.TfilaTime tempArvit2 = new StaticClass.TfilaTime(minyanArrayList.get(i)[12]);
                StaticClass.Tfila tempsaturday = new StaticClass.Tfila(tempKabalatShabat,tempShaharit2, tempMinha2, tempArvit2);
                StaticClass.Minyan tempMinyan = new StaticClass.Minyan(tempMinyanName , tempWeekday , tempsaturday);

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

    public void changeButtonBackground(Button button, boolean error)
    {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(error)
        {
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                button.setBackgroundDrawable( getResources().getDrawable(R.drawable.buttonshaperrror) );
            } else {
                button.setBackground( getResources().getDrawable(R.drawable.buttonshaperrror));
            }
        }
        else
        {
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                button.setBackgroundDrawable( getResources().getDrawable(R.drawable.buttonshape) );
            } else {
                button.setBackground( getResources().getDrawable(R.drawable.buttonshape));
            }
        }
    }
    public void forceEnglishView()
    {
        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    public void initialDate()
    {
        try {
            HebrewDateTV.setText(hebrewDate);
        }
        catch (Exception e)
        {
            HebrewDateTV.setText("");
        }

    }
    public void showShabatTimes()
    {
        String enter = changeTimeFormat(StaticClass.ShabatInfo.getShabatEnter());
        String exit = changeTimeFormat(StaticClass.ShabatInfo.getShabatExit());
        String shabatName = StaticClass.ShabatInfo.getShabatName();
        if(enter != null && exit != null && shabatName != null && 1 >2)
        {
            shabatTimesTV.setText("פרשת השבוע: "+shabatName+"\n"+
                    "כניסת השבת: "+enter+"\n"+
                    "יציאת השבת:"+exit);
        }
        else //for case we cant get valid data...
        {
            ((LinearLayout) findViewById(R.id.mainLLinearLayout)).removeView(((LinearLayout) findViewById(R.id.shabatTimesLayout)));
        }

    }
    public String changeTimeFormat(String s)
    {
        try {
            String hour = s.split(":")[0];
            String min = s.split(":")[1];
            hour = (Integer.parseInt(hour)+12)+"";
            min = min.split("p")[0];
            return hour+":"+min;
        }
        catch (Exception e)
        {

        }
        return "";

    }
}
