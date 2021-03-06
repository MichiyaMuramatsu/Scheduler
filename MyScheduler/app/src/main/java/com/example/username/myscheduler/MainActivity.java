package com.example.username.myscheduler;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm mR;
    private ListView mLV;
    private CalendarView calendarView;
    private RealmResults<Schedule> schedules;
    ScheduleAdapter adapter;
    int cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 19) {
            View decor = this.getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {startActivity(new Intent(MainActivity.this,ScheduleEditActivity.class));}
        });
        mR = Realm.getDefaultInstance();

        mLV = (ListView) findViewById(R.id.listView);

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

            public  void onSelectedDayChange(CalendarView view, int year,int month,int dayOfMonth){
                Date date = new Date(year-1900,month,dayOfMonth);

                date.setHours(00);
                date.setMinutes(00);
                date.setSeconds(00);


                RealmQuery<Schedule> rQ = mR.where(Schedule.class);


                System.out.println("scheduleDBの中身" + mR.where(Schedule.class).findAll());

                rQ.equalTo("date",date);

                System.out.println("scheduleDBの検索結果" + rQ.findAll());
                schedules = rQ.findAll();


                adapter = new ScheduleAdapter(schedules,calendarView);
                mLV.setAdapter(adapter);
            }
        });


        mLV.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Schedule schedule = (Schedule) parent.getItemAtPosition(position);
                        startActivity(new Intent(MainActivity.this,ScheduleEditActivity.class).putExtra("schedule_id", schedule.getId()));
                    }
                });





    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mR.close();
    }
}
