package com.example.xiaoxiaobai.flagman;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MyCalendar cal ;
    final private String database_name = "FlagMan.db";
    final static List<DayFinish> list = new ArrayList<>();
    public static SQLiteDatabase db;

    public int electricity;   //剩余电量，由电池实际的电量获取



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //cal = (MyCalendar)findViewById(R.id.cal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView Electricity_Big=(TextView)findViewById(R.id.textView2);
        setElectricity();
        String str="   " + electricity + "<small><small>%</small></small>";
        Electricity_Big.setTextSize(120);
        Electricity_Big.setTextColor(Color.rgb(255, 255, 255));
        Electricity_Big.setText(Html.fromHtml(str));

        TextView State_Grade=(TextView)findViewById(R.id.textView3);
        String str1="电池状态";
        State_Grade.setTextSize(15);
        State_Grade.setTextColor(Color.rgb(255, 255, 255));
        State_Grade.setText(Html.fromHtml(str1));

        TextView Charging_Time=(TextView)findViewById(R.id.textView4);
        String str2="预计充满";
        Charging_Time.setTextSize(15);
        Charging_Time.setTextColor(Color.rgb(255, 255, 255));
        Charging_Time.setText(Html.fromHtml(str2));

        TextView Temperature=(TextView)findViewById(R.id.textView5);

        String str3="电池温度";
        Temperature.setTextSize(15);
        Temperature.setTextColor(Color.rgb(255, 255, 255));
        Temperature.setText(Html.fromHtml(str3));




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        AppOpsManager appOps = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), this.getPackageName());
        if (mode != AppOpsManager.MODE_ALLOWED) startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

        SQliteOpeartion sq = new SQliteOpeartion(this, database_name, null, 1);
        db = sq.getWritableDatabase();

        addTempRecord();

        list.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, Check_Flag.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_set) {
            Intent intent = new Intent(MainActivity.this, Check_App_Info.class);
            startActivity(intent);
        } else if (id == R.id.nav_todo1) {
            //Intent intent = new Intent(MainActivity.this, BatteryState.class);
            Intent intent = new Intent(MainActivity.this, Battery_State.class);
            startActivity(intent);
            Toast. makeText (MainActivity.this, "请选择你的出行日期和里程！", Toast. LENGTH_SHORT ).show();
        } else if (id == R.id.nav_todo2) {
            Toast. makeText (MainActivity.this, "会有新功能的！", Toast. LENGTH_SHORT ).show();
        } else if (id == R.id.nav_todo3) {
            Toast. makeText (MainActivity.this, "会有新功能的！", Toast. LENGTH_SHORT ).show();
        } else if (id == R.id.nav_share) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("关于我们：");
            //builder.setIcon(R.drawable. tools );
            builder.setMessage("这群开发者很懒，\n什么都不想留下，\n但写安卓的人叫\nHaibo Xiu :)");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "感谢支持！", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast. makeText (MainActivity.this, "依然爱你！", Toast. LENGTH_SHORT ).show();
                }
            });
            builder.create().show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addTempRecord(){
        list.add(new MainActivity.DayFinish(1,2,2));
        list.add(new MainActivity.DayFinish(2,1,2));
        list.add(new MainActivity.DayFinish(3,0,2));
        list.add(new MainActivity.DayFinish(4,2,2));
        list.add(new MainActivity.DayFinish(5,2,2));
        list.add(new MainActivity.DayFinish(6,2,2));
        list.add(new MainActivity.DayFinish(7,2,2));
        list.add(new MainActivity.DayFinish(8,0,2));
        list.add(new MainActivity.DayFinish(9,1,2));
        list.add(new MainActivity.DayFinish(10,2,2));
        list.add(new MainActivity.DayFinish(11,2,5));
        list.add(new MainActivity.DayFinish(12,2,2));
        list.add(new MainActivity.DayFinish(13,2,2));
        list.add(new MainActivity.DayFinish(14,3,2));
        list.add(new MainActivity.DayFinish(15,2,2));
        list.add(new MainActivity.DayFinish(16,1,2));
        list.add(new MainActivity.DayFinish(17,0,2));
        list.add(new MainActivity.DayFinish(18,2,2));
        list.add(new MainActivity.DayFinish(19,2,2));
        list.add(new MainActivity.DayFinish(20,0,2));
        list.add(new MainActivity.DayFinish(21,2,2));
        list.add(new MainActivity.DayFinish(22,1,2));
        list.add(new MainActivity.DayFinish(23,2,0));
        list.add(new MainActivity.DayFinish(24,0,2));
        list.add(new MainActivity.DayFinish(25,2,2));
        list.add(new MainActivity.DayFinish(26,2,2));
        list.add(new MainActivity.DayFinish(27,2,2));
        list.add(new MainActivity.DayFinish(28,2,2));
        list.add(new MainActivity.DayFinish(29,2,2));
        list.add(new MainActivity.DayFinish(30,2,2));
        list.add(new MainActivity.DayFinish(31,2,2));

        for(DayFinish temp : list){
            TaskInfo task = new TaskInfo();
            task.year = 2018;
            task.month = 12;
            task.day = temp.day;
            task.task_num = temp.all;
            task.finished_num = temp.finish;
            SQliteOpeartion.InsertIntoTask(MainActivity.db, task);
        }

        for(int i = 1; i<=9; i++){
            TaskInfo task = new TaskInfo();
            task.year = 2019;
            task.month = 1;
            task.day = i;
            task.task_num = list.get(i).all;
            task.finished_num = list.get(i).finish;
            SQliteOpeartion.InsertIntoTask(MainActivity.db, task);
        }
    }

    static class DayFinish{
        int day;
        int all;
        int finish;
        public DayFinish(int day, int finish, int all) {
            this.day = day;
            this.all = all;
            this.finish = finish;
        }
    }




    //从电池的服务器端获取数据
    public void setElectricity(){
        //GET
        electricity = 65;
    }



}

