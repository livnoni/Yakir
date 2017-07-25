package com.example.yehud.yakir;

import android.util.Log;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by yehud on 7/26/2017.
 */

public class Tfila
{
    Vector<Minyan> minyansVec;
    String data;

    public Tfila(String data)
    {
        this.data = data;
        parser();
    }
    public void parser()
    {
        String[] array = data.split("\n");
        Log.i("array=", Arrays.toString(array));
        for (int i=1; i<array.length; i++)
        {

        }
    }



    public class Minyan
    {
        String name;
        Vector<Vector<String>> WeekdaysVec;
        Vector<Vector<String>> SaturdayVec;



    }

}
