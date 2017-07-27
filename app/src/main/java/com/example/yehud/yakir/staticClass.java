package com.example.yehud.yakir;

import java.util.Vector;

/**
 * Created by yehud on 7/27/2017.
 */

public class staticClass
{
    public static class Minyan
    {
        String name;
        Tfila weekday,saturday;

        public Minyan(String name, Tfila weekday , Tfila saturday)
        {
            this.name = name;
            this.weekday = weekday;
            this.saturday = saturday;
        }
        public String toString()
        {
            return "["+name+" -> weekday="+weekday.toString()+" , saturday="+saturday.toString()+"]";
        }

    }
    public static class Tfila
    {
        TfilaTime shaharit,minha,arvit;

        public Tfila(TfilaTime shaharit, TfilaTime minha, TfilaTime arvit)
        {
            this.shaharit = shaharit;
            this.minha = minha;
            this.arvit = arvit;
        }
        public String toString()
        {
            return "[shaharit="+shaharit.toString()+"],["+"minha="+minha.toString()+"],["+"arvit="+arvit.toString()+"]";
        }

    }
    public static class TfilaTime
    {
        Vector<String> timesVector;

        public TfilaTime(String [] data)
        {
            timesVector = new Vector<String>();
            for(int i = data.length-1; i>=0; i--)
            {
                if(data[i]!=null && data[i]!="" && data[i]!=" ")
                {
                    timesVector.add(data[i]);
                }
            }
        }
        public String toString()
        {
            return timesVector.toString();
        }
    }
}
