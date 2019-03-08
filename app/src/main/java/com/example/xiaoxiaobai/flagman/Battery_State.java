package com.example.xiaoxiaobai.flagman;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Battery_State extends AppCompatActivity{

    private MyCalendar cal ;
    final private String database_name = "FlagMan.db";
    final static List<MainActivity.DayFinish> list = new ArrayList<>();
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_battery_state);
        cal = (MyCalendar) findViewById(R.id.cal);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        AppOpsManager appOps = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), this.getPackageName());
        if (mode != AppOpsManager.MODE_ALLOWED)
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));*/

        SQliteOpeartion sq = new SQliteOpeartion(this, database_name, null, 1);
        db = sq.getWritableDatabase();


        list.clear();


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        cal.setRenwu(String.valueOf(year) + "年" + String.valueOf(month) + "月", list);

        cal.setOnClickListener(new MyCalendar.onClickListener() {
            @Override
            public void onLeftRowClick() {
                //Toast.makeText(Battery_State.this, "点击减箭头", Toast.LENGTH_SHORT).show();
                cal.monthChange(-1);

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            Battery_State.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cal.setRenwu(list);
                                }
                            });
                        } catch (Exception e) {
                        }
                    }
                }.start();
            }

            @Override
            public void onRightRowClick() {

                cal.monthChange(1);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            Battery_State.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cal.setRenwu(list);
                                }
                            });
                        } catch (Exception e) {
                        }
                    }
                }.start();
            }

            @Override
            public void onTitleClick(String monthStr, Date month) {
                Toast.makeText(Battery_State.this, "点击了标题：" + monthStr, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWeekClick(int weekIndex, String weekStr) {
                Toast.makeText(Battery_State.this, "点击了星期：" + weekStr, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDayClick(int day, String dayStr, MainActivity.DayFinish finish) {

                Toast.makeText(Battery_State.this, "点击了日期：" + dayStr, Toast.LENGTH_SHORT).show();
                Log.w("", "点击了日期:" + dayStr);
            }
            public void onFirstDayClick(int day, String dayStr, MainActivity.DayFinish finish) {

                Toast.makeText(Battery_State.this, "点击了出行日期：" + dayStr, Toast.LENGTH_SHORT).show();

            }
            public void onSecondDayClick(int day, String dayStr, MainActivity.DayFinish finish) {

                Toast.makeText(Battery_State.this, "点击了返程日期：" + dayStr, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
