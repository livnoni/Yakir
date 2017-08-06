package org.livnoni.yehud.yakir;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by yehud on 7/27/2017.
 */

public class StaticClass
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
        /*
        Check the neareset minayns and return str [] that present it.
        String [] = 3 elements-> 1) name 2) kind 3)hour
        * */
        public String [] getNearestMinyans(String currentDate) throws ParseException {
            String [] closestMinayan = new String[3];
            Long difference = new Long(Long.MAX_VALUE);

            for(int i=0; i<weekday.shaharit.getTimes().length; i++)
            {
                Long tempDifference = diffrentDate(currentDate, weekday.shaharit.getTimes()[i]);
                if(tempDifference >=0 && tempDifference<difference)
                {
                    difference = tempDifference;
                    closestMinayan[0] = name;
                    closestMinayan[1] = "שחרית";
                    closestMinayan[2] = weekday.shaharit.getTimes()[i];
                }
            }
            for(int i=0; i<weekday.minha.getTimes().length; i++)
            {
                Long tempDifference = diffrentDate(currentDate, weekday.minha.getTimes()[i]);
                if(tempDifference >=0 && tempDifference<difference)
                {
                    difference = tempDifference;
                    closestMinayan[0] = name;
                    closestMinayan[1] = "מנחה";
                    closestMinayan[2] = weekday.minha.getTimes()[i];
                }
            }for(int i=0; i<weekday.arvit.getTimes().length; i++)
            {
                Long tempDifference = diffrentDate(currentDate, weekday.arvit.getTimes()[i]);
                if(tempDifference >=0 && tempDifference<difference)
                {
                    difference = tempDifference;
                    closestMinayan[0] = name;
                    closestMinayan[1] = "ערבית";
                    closestMinayan[2] = weekday.arvit.getTimes()[i];
                }
            }
            if(closestMinayan[0]==null)
            {
                return null;
            }

            return closestMinayan;

        }
        public long diffrentDate(String currentDate, String newTime) throws ParseException {
//            String time1 = "16:00";
//            String time2 = "19:00";

            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date date1 = format.parse(newTime);
            Date date2 = format.parse(currentDate);
            long difference = date1.getTime() - date2.getTime();
            return  difference;
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
        public String [] getTimes()
        {
            String [] time = new String[timesVector.size()-1];
            for (int i=0; i<time.length; i++)
            {
                time[i] = timesVector.get(i+1);
            }
            return time;
        }
    }
    public static class HebrewTime
    {
        String currentDate;
        HebrewTime(String s)
        {
            this.currentDate = s;
        }
        public String toString()
        {
            return currentDate;
        }
    }
    public static class ShabatInfo
    {
        static String currentName;
        static String enterShabatTime;
        static String exitShabatTime;

        ShabatInfo(String name, String enter, String exit)
        {
            this.currentName = name;
            this.enterShabatTime = enter;
            this.exitShabatTime = exit;
        }
        public String toString()
        {
            return "["+currentName+","+enterShabatTime+","+exitShabatTime+"]";
        }
        public static String getShabatName()
        {
            return currentName;
        }
        public static String getShabatEnter()
        {
            return enterShabatTime;
        }
        public static String getShabatExit()
        {
            return exitShabatTime;
        }
    }
}
