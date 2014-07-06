package fr.unice.polytech.freetime.app.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.internal.widget.LinearLayoutICS;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import fr.unice.polytech.calendarmodule.FreeTimeCalendarService;
import fr.unice.polytech.entities.FtEventEntity;
import fr.unice.polytech.freetime.app.Control;
import fr.unice.polytech.freetime.app.Entities.User;
import fr.unice.polytech.freetime.app.Entities.event;
import fr.unice.polytech.freetime.app.FreeTimeApplication;
import fr.unice.polytech.freetime.app.ManageTask.ViewTaskList;
import fr.unice.polytech.freetime.app.R;
import fr.unice.polytech.freetime.app.TopView.AddElements;
import fr.unice.polytech.freetime.app.utils.Fonction;

public class CalendarViewDay extends Activity implements GestureDetector.OnGestureListener{


    private static final String DEBUG_TAG = "Gestures";

    private final String IMPORTED="#ffb2b2ff";
    private final String FREETIME="#ffaeffcf";
    private final String LONGTERM="#ffffcb9a";
    private final String ONETIME="#fff0b9ff";
    private final String ALLDAY="#ffffffb8";
    private final String RECURRING="#ffffc6ce";

    private List<FtEventEntity> eventSun;
    private List<FtEventEntity> eventMon;
    private List<FtEventEntity> eventTue;
    private List<FtEventEntity> eventWed;
    private List<FtEventEntity> eventThu;
    private List<FtEventEntity> eventFri;
    private List<FtEventEntity> eventSat;

    private Control control;
    private TextView[] daysNumber;

    private GestureDetectorCompat mDetector;
    private ArrayList<TextView> textViews;
    private GregorianCalendar currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view_day);
        control= new Control((FreeTimeApplication)getApplication());

        // set user preferences
        User user= (User) getIntent().getSerializableExtra("user");



        daysNumber = new TextView[7];


        if(user!=null) {

            SharedPreferences settings = getSharedPreferences(FreeTimeCalendarService.PREFNAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(FreeTimeCalendarService.PREF_WAKEUP_HOUR, user.getStartDayHour());
            editor.putInt(FreeTimeCalendarService.PREF_WAKEUP_MINUTE, user.getStartDayMinute());
            editor.putInt(FreeTimeCalendarService.PREF_BEDTIME_HOUR, user.getEndDayHour());
            editor.putInt(FreeTimeCalendarService.PREF_BEDTIME_MINUTE, user.getEndDayMinute());
            editor.putString(FreeTimeCalendarService.PREF_USER_NAME, user.getName());
            editor.commit();

            if (user.getCal_ID() != null) {
                control.importCalendar(user.getCal_ID());
            }

            List<event> freeTimeEvent = user.getFreeTime();
            List<event> recurrentEvent = user.getEvents();

            for (event e : recurrentEvent) {
                control.createRecurrentEvent(e);
            }

            for (event e : freeTimeEvent) {
                control.createFreeTimeSlot(e);
            }

        }

        textViews = new ArrayList<TextView>();
        setTextViewNumber();

        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        currentDay = new GregorianCalendar();
        drawWeek(false);


    }

    private void drawWeek(boolean invalidate){

        mDetector = new GestureDetectorCompat(this,this);


        //set Marker
        View view= findViewById(R.id.currentTimeLineView);

        LinearLayout.LayoutParams paramCT = (LinearLayout.LayoutParams) view.getLayoutParams();

        int minuteTime= currentDay.get(Calendar.HOUR_OF_DAY)*60 + currentDay.get(Calendar.MINUTE);

        int minuteTime_px= Fonction.dpToPx(getApplicationContext(),minuteTime);
        paramCT.setMargins(0, minuteTime_px, 0, 0);
        view.setLayoutParams(paramCT);
        setMarkerDay(currentDay.get(Calendar.DAY_OF_WEEK));

        TextView tvMonth= (TextView) findViewById(R.id.currentMonthTextView);
        TextView tvYear= (TextView) findViewById(R.id.currentYearTextView);
        tvYear.setText("" + currentDay.get(Calendar.YEAR));
        tvMonth.setText(""+getCharSequenseMonth(currentDay.get(Calendar.MONTH)));

        //get all FTevent
        getFTevent(currentDay,invalidate);

    }

    /**
     * Give a week relative to the day
     * @param day
     * @return
     */
    public GregorianCalendar[] getRelativeWeek(int day,boolean invalidate){
        GregorianCalendar[] week = new GregorianCalendar[7];
        for(int i = 0;i<week.length;i++){
            week[i] = (GregorianCalendar) currentDay.clone();
            int deep = (i-day+1);
            week[i].set(Calendar.DAY_OF_MONTH,week[i].get(Calendar.DAY_OF_MONTH)+deep);
            //Update text number of day
            int nbrDay= week[i].get(Calendar.DAY_OF_MONTH);
            daysNumber[i].setText("" + nbrDay);
        }

        return week;
    }

    public void getFTevent(GregorianCalendar today,boolean invalidate){
        //recupéré tour les autre jour ed la semaine
        GregorianCalendar[] week = getRelativeWeek(today.get(Calendar.DAY_OF_WEEK),invalidate);
        eventSun= control.getFTeventByDay(week[0]);
        eventMon= control.getFTeventByDay(week[1]);
        eventTue= control.getFTeventByDay(week[2]);
        eventWed= control.getFTeventByDay(week[3]);
        eventThu= control.getFTeventByDay(week[4]);
        eventFri= control.getFTeventByDay(week[5]);
        eventSat= control.getFTeventByDay(week[6]);


        // placer les events pour ce jour
        for(FtEventEntity e:eventSun){
            int startHour= (int) e.getStartHourAndMinute()/(3600*1000);
            int startMin= (int) (e.getStartHourAndMinute()% (3600*1000))/60000;
            int durationMIn= (int) (e.getEndTime()-e.getStartTime())/60000;

            placeEventOnView(Calendar.SUNDAY,startHour,startMin,durationMIn,e.getTitle(),getColor(e.getFtEventType()),invalidate);
        }

        for(FtEventEntity e:eventMon){
            int startHour= (int) e.getStartHourAndMinute()/(3600*1000);
            int startMin= (int) (e.getStartHourAndMinute()% (3600*1000))/60000;
            int durationMIn= (int) (e.getEndTime()-e.getStartTime())/60000;

            placeEventOnView(Calendar.MONDAY,startHour,startMin,durationMIn,e.getTitle(),getColor(e.getFtEventType()),invalidate);
        }

        for(FtEventEntity e:eventTue){
            int startHour= (int) e.getStartHourAndMinute()/(3600*1000);
            int startMin= (int) (e.getStartHourAndMinute()% (3600*1000))/60000;
            int durationMIn= (int) (e.getEndTime()-e.getStartTime())/60000;

            placeEventOnView(Calendar.TUESDAY,startHour,startMin,durationMIn,e.getTitle(),getColor(e.getFtEventType()),invalidate);
        }

        for(FtEventEntity e:eventWed){
            int startHour= (int) e.getStartHourAndMinute()/(3600*1000);
            int startMin= (int) (e.getStartHourAndMinute()% (3600*1000))/60000;
            int durationMIn= (int) (e.getEndTime()-e.getStartTime())/60000;

            placeEventOnView(Calendar.WEDNESDAY,startHour,startMin,durationMIn,e.getTitle(),getColor(e.getFtEventType()),invalidate);
        }

        for(FtEventEntity e:eventThu){
            int startHour= (int) e.getStartHourAndMinute()/(3600*1000);
            int startMin= (int) (e.getStartHourAndMinute()% (3600*1000))/60000;
            int durationMIn= (int) (e.getEndTime()-e.getStartTime())/60000;

            placeEventOnView(Calendar.THURSDAY,startHour,startMin,durationMIn,e.getTitle(),getColor(e.getFtEventType()),invalidate);
        }

        for(FtEventEntity e:eventFri){
            int startHour= (int) e.getStartHourAndMinute()/(3600*1000);
            int startMin= (int) (e.getStartHourAndMinute()% (3600*1000))/60000;
            int durationMIn= (int) (e.getEndTime()-e.getStartTime())/60000;

            placeEventOnView(Calendar.FRIDAY,startHour,startMin,durationMIn,e.getTitle(),getColor(e.getFtEventType()),invalidate);
        }

        for(FtEventEntity e:eventSat){
            int startHour= (int) e.getStartHourAndMinute()/(3600*1000);
            int startMin= (int) (e.getStartHourAndMinute()% (3600*1000))/60000;
            int durationMIn= (int) ((e.getDuration())/60000);

            placeEventOnView(Calendar.SATURDAY,startHour,startMin,durationMIn,e.getTitle(),getColor(e.getFtEventType()),invalidate);
        }
    }

    public void placeEventOnView(int jour,int startHour,int startMin,int durationMin,String title,String color,boolean invalidate){
        int day= findDayId(jour);
        int durationHeure= durationMin/60;
        int lastMinute= 60 - (durationMin - (60*durationHeure));

        //get id of firstHour
        int startHourId=findHourId(startHour);

        //value of the last hour and lastMinute
        int lastHour=0;

        if( durationHeure >0){

            if(lastMinute>0) {
                lastHour = startHour + durationHeure;

                if (startMin + lastMinute > 60) {

                    lastMinute = startMin + lastMinute - 60;
                } else {
                    lastMinute += startMin;
                }
            }else {
                lastHour= startHour+durationHeure;
            }

        }else{
            if(startMin+durationMin>60){
                lastHour=startHour+1;
            }else{
                lastHour=startHour;
            }
        }

        //get id of last Hour
        int endHourId=0;
        if(startMin+durationMin>60){
            endHourId= findHourId(lastHour);
        }

        //init tab of value of interme hour
        int[] heuresInt;
        int nbrOfHourInt=0;
        if(durationHeure>1) {
            heuresInt = new int[lastHour - startHour - 1];

            //get value of intermediate hour
            for(int i=0;i<(lastHour-startHour-1);i++){
                heuresInt[i]=startHour+i+1;
            }

            // init tab of interm hour id
            int[] heureInterId=new int[heuresInt.length];


            // set value of intermediate hour id
            if(heuresInt.length>0){
                int j=0;
                for(int i:heuresInt){
                    heureInterId[j]=findHourId(i);
                    j++;
                }
                nbrOfHourInt=heuresInt.length;
            }

            // create textview for every intermediate hour
            for(int id: heureInterId){
                RelativeLayout RL= (RelativeLayout) findViewById(day).findViewById(id).findViewById(R.id.linearLayoutHour);
                RelativeLayout.LayoutParams paramL = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                TextView tv= new TextView(this);
                paramL.setMargins(2,2,2,2);
                tv.setLayoutParams(paramL);
                tv.setBackgroundColor(Color.parseColor(color));
                //We add textViews to be able to remove them
                textViews.add(tv);
                RL.addView(tv);
                if(invalidate){
                    RL.invalidate();
                }
            }
        }


        //set the first hour textView
        int lenghtFistHour=0;

        if(durationHeure>1 || startMin+durationMin>60) {
            lenghtFistHour = 60 - startMin;
        }else{
            lenghtFistHour=durationMin;
        }

       /* if(durationHeure == 0 ){
            lenghtFistHour-= (60 - durationMin);
        }*/

        // calcul la heuteur du créneau de la premiere heure en fonction de la marge des des minutes de début
        int lenghtFirstHour_px= Fonction.dpToPx(getApplicationContext(),lenghtFistHour);
        int startMin_px=Fonction.dpToPx(getApplicationContext(),startMin);
        RelativeLayout rl= (RelativeLayout) findViewById(day).findViewById(startHourId).findViewById(R.id.linearLayoutHour);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,lenghtFirstHour_px));
        params.setMargins(2,startMin_px,2,2);

        TextView tv= new TextView(this);
        tv.setText(title);
        tv.setTextSize(9);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        tv.setLayoutParams(params);
        tv.setBackgroundColor(Color.parseColor(color));
        //We add the first textView to be able to remove it
        textViews.add(tv);
        rl.addView(tv);
        if(invalidate){
            rl.invalidate();
        }



        //set the last hour textview if différent of the firstHour
        if(startMin+durationMin>60) {
            lastMinute= durationMin - lenghtFistHour - nbrOfHourInt*60;
            int min = Fonction.dpToPx(getApplicationContext(), lastMinute);
            RelativeLayout rlLast = (RelativeLayout) findViewById(day).findViewById(endHourId).findViewById(R.id.linearLayoutHour);
            RelativeLayout.LayoutParams paramsLast = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, min));
            paramsLast.setMargins(2, 2, 2, 2);

            TextView tvlast = new TextView(this);
            tvlast.setLayoutParams(paramsLast);
            tvlast.setBackgroundColor(Color.parseColor(color));
            //We add the last textView to be able to remove it
            textViews.add(tvlast);
            rlLast.addView(tvlast);
            if(invalidate){
                rlLast.invalidate();
            }

        }

    }

    public int findHourId(int hour){
        int heure;
        switch (hour){
            case 0:heure=R.id.hour_0;break;
            case 1:heure= R.id.hour_1;break;
            case 2:heure= R.id.hour_2;break;
            case 3:heure= R.id.hour_3;break;
            case 4:heure= R.id.hour_4;break;
            case 5:heure= R.id.hour_5;break;
            case 6:heure= R.id.hour_6;break;
            case 7:heure= R.id.hour_7;break;
            case 8:heure= R.id.hour_8;break;
            case 9:heure= R.id.hour_9;break;
            case 10:heure= R.id.hour_10;break;
            case 11:heure= R.id.hour_11;break;
            case 12:heure= R.id.hour_12;break;
            case 13:heure= R.id.hour_13;break;
            case 14:heure= R.id.hour_14;break;
            case 15:heure= R.id.hour_15;break;
            case 16:heure= R.id.hour_16;break;
            case 17:heure= R.id.hour_17;break;
            case 18:heure= R.id.hour_18;break;
            case 19:heure= R.id.hour_19;break;
            case 20:heure= R.id.hour_20;break;
            case 21:heure= R.id.hour_21;break;
            case 22:heure= R.id.hour_22;break;
            case 23:heure= R.id.hour_23;break;
            default:heure= R.id.hour_1;break;
        }
        return heure;
    }

    public int  findDayId(int jour){
        int day;
        switch (jour){
            case Calendar.SUNDAY:day=R.id.calendar_z_sun;break;
            case Calendar.MONDAY:day=R.id.calendar_z_mon;break;
            case Calendar.TUESDAY:day=R.id.calendar_z_tue;break;
            case Calendar.WEDNESDAY:day=R.id.calendar_z_wed;break;
            case Calendar.THURSDAY:day=R.id.calendar_z_thu;break;
            case Calendar.FRIDAY:day=R.id.calendar_z_fri;break;
            case Calendar.SATURDAY:day=R.id.calendar_z_sat;break;
            default:day=R.id.calendar_z_sun;break;
        }

        return day;
    }

    public void setMarkerDay(int jour){

        View view;
        switch (jour){
            case Calendar.SUNDAY:
                view= (View) findViewById(R.id.dayMarkerViewSun);
                view.setVisibility(View.VISIBLE);break;
            case Calendar.MONDAY:
                view= (View) findViewById(R.id.dayMarkerViewMon);
                view.setVisibility(View.VISIBLE);break;
            case Calendar.TUESDAY:
                view= (View) findViewById(R.id.dayMarkerViewTue);
                view.setVisibility(View.VISIBLE);break;
            case Calendar.THURSDAY:
                view= (View) findViewById(R.id.dayMarkerViewThu);
                view.setVisibility(View.VISIBLE);break;
            case Calendar.FRIDAY:
                view= (View) findViewById(R.id.dayMarkerViewFri);
                view.setVisibility(View.VISIBLE);break;
            case Calendar.SATURDAY:
                view= (View) findViewById(R.id.dayMarkerViewSat);
                view.setVisibility(View.VISIBLE);break;
            case Calendar.WEDNESDAY:
                view= (View) findViewById(R.id.dayMarkerViewWed);
                view.setVisibility(View.VISIBLE);break;
        }
    }


    public String getColor(FtEventEntity.FtEventType type){
        if(type.equals(FtEventEntity.FtEventType.ALLDAY)){
            return ALLDAY;
        }
        if(type.equals(FtEventEntity.FtEventType.ONETIME)){
            return ONETIME;
        }
        if(type.equals(FtEventEntity.FtEventType.RECURRING)){
            return RECURRING;
        }
        if(type.equals(FtEventEntity.FtEventType.FREETIME)){
            return FREETIME;
        }
        if(type.equals(FtEventEntity.FtEventType.IMPORTED)){
            return IMPORTED;
        }
        if(type.equals(FtEventEntity.FtEventType.LONGTERM)){
            return LONGTERM;
        }
        return IMPORTED;
    }

    public void setTextViewNumber(){
        daysNumber[0]= (TextView) findViewById(R.id.sundayDateTextView);
        daysNumber[1]= (TextView) findViewById(R.id.mondayDateTextView);
        daysNumber[2]= (TextView) findViewById(R.id.tuesdayDateTextView);
        daysNumber[3]= (TextView) findViewById(R.id.wednesdayDateTextView);
        daysNumber[4]= (TextView) findViewById(R.id.thursdayDateTextView);
        daysNumber[5]= (TextView) findViewById(R.id.fridayDateTextView);
        daysNumber[6]= (TextView) findViewById(R.id.saturdayDateTextView);

    }

    public String getCharSequenseMonth(int monthNum){
        String month="default_value_month";
        switch (monthNum){
            case 0:month="Jan";break;
            case 1:month= "Feb";break;
            case 2:month= "Mar";break;
            case 3:month= "Apr";break;
            case 4:month= "May";break;
            case 5:month="Jun";break;
            case 6:month= "Jul";break;
            case 7:month= "Aug";break;
            case 8:month= "Sep";break;
            case 9:month="Oct";break;
            case 10:month= "Nov";break;
            case 11:month= "Dec";break;
        }
        return month;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_view_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_ListTask:
                Intent intentList= new Intent(this, ViewTaskList.class);
                startActivity(intentList);
                break;

            case R.id.action_new:
                Intent intent= new Intent(this, AddElements.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {

        try {
            float diffX = event2.getX() - event1.getX();
            if (diffX > 0) {
               // Toast.makeText(getApplicationContext(),"FlingRight",Toast.LENGTH_LONG).show();
                previousWeek();
            } else {
               // Toast.makeText(getApplicationContext(),"FlingLeft",Toast.LENGTH_LONG).show();
                nextWeek();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }


        //Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
        //Toast.makeText(getApplicationContext(),"Fling",Toast.LENGTH_LONG).show();
        return true;
    }
    private void removeAllTextViews(){
        RelativeLayout ll = (RelativeLayout)findViewById(R.id.linearLayoutHour);
        for(TextView v : textViews) {
            ((ViewManager)v.getParent()).removeView(v);
        }
        textViews.removeAll(textViews);
    }

    private void previousWeek() {
        removeAllTextViews();
        currentDay.set(Calendar.DAY_OF_MONTH,currentDay.get(Calendar.DAY_OF_MONTH)-7);
        drawWeek(true);
    }

    private void nextWeek() {
        removeAllTextViews();
        currentDay.set(Calendar.DAY_OF_MONTH,currentDay.get(Calendar.DAY_OF_MONTH)+7);
        drawWeek(true);
    }


}
