package org.livnoni.yehud.yakir;

import android.util.Log;

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
        TfilaTime kabalatShabat;

        public Tfila(TfilaTime shaharit, TfilaTime minha, TfilaTime arvit)
        {
            this.shaharit = shaharit;
            this.minha = minha;
            this.arvit = arvit;
        }
        public Tfila(TfilaTime kabalatShabat, TfilaTime shaharit, TfilaTime minha, TfilaTime arvit)
        {
            this.kabalatShabat = kabalatShabat;
            this.shaharit = shaharit;
            this.minha = minha;
            this.arvit = arvit;
        }
        public String toString()
        {
            if(kabalatShabat != null )
            {
                return "[minhaShbat"+kabalatShabat.toString()+"],[shaharit="+shaharit.toString()+"],["+"minha="+minha.toString()+"],["+"arvit="+arvit.toString()+"]";

            }
            else
            {
                return "[shaharit="+shaharit.toString()+"],["+"minha="+minha.toString()+"],["+"arvit="+arvit.toString()+"]";
            }
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

    public static class Shuirs
    {
        Vector<Shiur> shiursVector;

        public Shuirs(String [][] trtd)
        {
            shiursVector = new Vector<Shiur>(trtd[0].length);

            for (int i =0; i<trtd.length; i++)
            {
                if(trtd[i].length == 6) //with details
                {
                    shiursVector.add(new Shiur(trtd[i][0],trtd[i][1],trtd[i][2],trtd[i][3],trtd[i][4],trtd[i][5]));
                }
                if(trtd[i].length == 5) ///withou details
                {
                    shiursVector.add(new Shiur(trtd[i][0],trtd[i][1],trtd[i][2],trtd[i][3],trtd[i][4]));
                }
            }
        }

        public String toString()
        {
            String s ="num of shiurim="+shiursVector.size()+" ";
            for (int i=0; i<shiursVector.size(); i++)
            {
                s = s +"["+ shiursVector.get(i).toString()+"] , ";
            }
            return s;
        }
        public String toString(String dayName)
        {
            String ans ="";
            for(int i=0; i<shiursVector.size(); i++)
            {
                if(shiursVector.get(i).dayName.equals(dayName))
                {
                    ans = ans + shiursVector.get(i).toString()+"\n";
                }
            }
            if(ans == "")
            {
                return null;
            }
            else
            {
                return ans;
            }
        }
        public int getNumOfShiurs(String dayName)
        {
            int count =0;
            for(int i=0; i<shiursVector.size(); i++)
            {
                if(shiursVector.get(i).dayName.equals(dayName))
                {
                    count++;
                }
            }
            return count;
        }
        public  Vector<Shiur> getVectorOfShuirs(String dayName)
        {
            Vector<Shiur> tempVector = new Vector<Shiur>();
            for(int i=0; i<shiursVector.size(); i++)
            {
                if(shiursVector.get(i).dayName.equals(dayName))
                {
                    tempVector.add(shiursVector.get(i));
                }
            }
            if(tempVector.size()>0)
            {
                return tempVector;
            }
            else
            {
                return null;
            }
        }
    }

    public static class Shiur
    {
        String dayName, time, name , spokenBy, location ,details;

        public Shiur(String dayName, String time,String name ,String spokenBy,String location ,String details)
        {
            this.dayName = dayName;
            this.time = time;
            this.name = name;
            this.spokenBy = spokenBy;
            this.location = location;
            this.details = details;
        }
        public Shiur(String dayName, String time,String name ,String spokenBy,String location)
        {
            this.dayName = dayName;
            this.time = time;
            this.name = name;
            this.spokenBy = spokenBy;
            this.location = location;
            this.details = null;
        }
        public String toString()
        {
            if(details == null || details =="" || details ==" ")
            {
                return "[ dayName:"+dayName+",time="+time+",name="+name+",spokenBy="+spokenBy+","+location+"]";
            }
            else
            {
                return "[ dayName:"+dayName+",time="+time+",name="+name+",spokenBy="+spokenBy+","+location+",details="+details+" ]";

            }
        }
        public String getName()
        {
            if(name != null)
            {
                return name;
            }
            return null;
        }
        public String getDayName()
        {
            if(dayName != null)
            {
                return dayName;
            }
            return null;
        }
        public String getTime()
        {
            if(time != null)
            {
                return time;
            }
            return null;
        }
        public String getSpokenBy()
        {
            if(spokenBy != null)
            {
                return spokenBy;
            }
            return null;
        }
        public String getLocation()
        {
            if(location != null)
            {
                return location;
            }
            return null;
        }
        public String getDetails()
        {
            if(details != null || details == "" || details == " ")
            {
                return details;
            }
            return null;
        }
    }

}
