package com.example.firebase4.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.firebase4.ClockView.ClockView;
import com.example.firebase4.DataBase.SQLiteDataBaseHelper;
import com.example.firebase4.R;
import com.example.firebase4.TestActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class home_Fragment extends Fragment {

    private TextView tv_showtime;
    private ImageView iv_apm;
    private TextView tv_date,tv_week;
    private TextView timeText,timeText2;
    private int h,m,h2,m2;
    private ClockView freeDraw;
    private Handler handler = new Handler();



    public static final String USER_WEEK_PLAN = "user_week_plan";
    public SQLiteDatabase db;
    public SQLiteDataBaseHelper sqLiteDataBaseHelper;
    //在此實作Fragment的邏輯

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //宣告參照
        View fragment_home = inflater.inflate(R.layout.fragment_home, container, false);
        Context context = fragment_home.getContext();
        sqLiteDataBaseHelper = new SQLiteDataBaseHelper(context);
        db = sqLiteDataBaseHelper.getReadableDatabase();
        freeDraw = new ClockView(home_Fragment.super.getContext());


        init(fragment_home);

        //取得今天星期
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int i = calendar.get(Calendar.DAY_OF_WEEK); // the day of the week in numerical format


        //從sqlite得到相對應資料
        Cursor c = db.rawQuery(" SELECT * FROM " + USER_WEEK_PLAN
                + " WHERE WEEKDAY =" + "'" + i + "'", null);
        String s2 ="";
        while (c.moveToNext()) {

            int id = c.getInt(0);
            int weekday = c.getInt(1);
            String startime = c.getString(2);
            int duration = c.getInt(3);
            Calendar start = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            try {
                start.setTime(df.parse(startime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar end = (Calendar) start.clone();
            end.add(Calendar.HOUR_OF_DAY, duration);
            String endtime = df.format(end.getTime());
            String s = String.format("進食開始時間為 %s,結束時間為%s ", startime, endtime);
            s2 += s;

            //得到開始時間的小時，分鐘
            //得到結束時間的小時，分鐘
            //drawExt2是第一個傳入值
            //drawExt是第二個傳入值
            int startHour = start.get(Calendar.HOUR_OF_DAY);
            int startMin = start.get(Calendar.MINUTE);
            int endHour = end.get(Calendar.HOUR_OF_DAY);
            int endMin = end.get(Calendar.MINUTE);

            freeDraw.drawExt2(startHour,startMin);
            freeDraw.drawExt(endHour,endMin);


        }

        tv_showtime.setText(s2);

        handler.removeCallbacks(updateTimer);//設定定時要執行的方法
        handler.postDelayed(updateTimer, 500);//設定Delay的時間
        return fragment_home;
    }

    private void init(View fragment_home) {
        tv_showtime=fragment_home.findViewById(R.id.tv_showtime);
        iv_apm=fragment_home.findViewById(R.id.iv_apm);

        timeText=fragment_home.findViewById(R.id.timeText);
        timeText2 = fragment_home.findViewById(R.id.timeText2);
        freeDraw = fragment_home.findViewById(R.id.freeDraw);
    }

    @SuppressLint("SetTextI18n")
    public void onResume(){
        super.onResume();
    }
    private Runnable updateTimer = new Runnable() {

        public void run() {
            handler.postDelayed(this, 500);

            long time = System.currentTimeMillis();
            final Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(time);
            int apm = mCalendar.get(Calendar.AM_PM);

            if (apm == 0) {
                iv_apm.setImageResource(R.drawable.sun_icon);
            } else {
                iv_apm.setImageResource(R.drawable.moon);
            }
            Calendar mCal = Calendar.getInstance();
            CharSequence s = DateFormat.format("MM月.dd", mCal.getTime());
            //tv_date.setText(s);
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            String week = null;
            switch (day) {
                case Calendar.SUNDAY:
                    week = "日";
                    break;
                case Calendar.MONDAY:
                    week = "一";
                    break;
                case Calendar.TUESDAY:
                    week = "二";
                    break;
                case Calendar.THURSDAY:
                    week = "三";
                    break;
                case Calendar.WEDNESDAY:
                    week = "四";
                    break;
                case Calendar.FRIDAY:
                    week = "五";
                    break;
                case Calendar.SATURDAY:
                    week = "六";
                    break;
            }
            //tv_week.setText("\t星期" + week);


            //获取当前时间
            int second = mCalendar.get(Calendar.SECOND);
            int minute = mCalendar.get(Calendar.MINUTE);
            int hour = mCalendar.get(Calendar.HOUR);
            long between = 0;
            int show = h * 60 * 60 + m * 60;
            int show2 = h2 * 60 * 60 + m2 * 60;
            if (hour * 60 * 60 + minute * 60 + second >= show && hour * 60 * 60 + minute * 60 + second <= show2) {
                timeText.setVisibility(View.VISIBLE);
                timeText2.setVisibility(View.VISIBLE);
                timeText.setText("目前為斷食時段");
                timeText.setTextColor(Color.RED);

                between = (h2 * 60 * 60 + m2 * 60) - (hour * 60 * 60 + minute * 60 + second);
                long nh = 60 * 60;
                long nm = 60;
                // 计算差多少小时
                long between_hour = between / nh;
                // 计算差多少分钟
                long between_min = between % nh / nm;
                // 计算差多少秒//输出结果
                long between_sec = between % nh % nm;
                String strBetween = String.format("%02d", between_hour) + ":" + String.format("%02d", between_min) + ":" + String.format("%02d", between_sec);
                timeText2.setText(strBetween);
            } else {
                timeText.setVisibility(View.INVISIBLE);
                timeText2.setVisibility(View.INVISIBLE);

            }
        }
    };
}

/*1.getActivity()；//獲取包含該fragment的活動（activity）上下文
        2.getContext()；//獲取該fragment上下文
        3.getActivity().getApplicationContext()；//通過包含該fragment的活動（activity）獲取整個應用的上下文
        4.getContext().getApplicationContext()；//通過該fragment獲取整個應用的上下文*/