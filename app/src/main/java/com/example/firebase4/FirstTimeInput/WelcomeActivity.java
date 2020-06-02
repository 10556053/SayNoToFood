package com.example.firebase4.FirstTimeInput;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.firebase4.HomePage;
import com.example.firebase4.MainActivity;
import com.example.firebase4.R;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager vp_welcome;
    private LinearLayout lv_dots;
    private Button bt_prev,bt_next;
    private WelcomePagerAdapter welcomePagerAdapter;

    public TextView [] mdots;

    private int current_page;
    private int cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SharedPreferences sharedPreferences = getSharedPreferences("isFirstTime",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("isFirst","no");
        editor.apply();

        vp_welcome =(ViewPager)findViewById(R.id.vp_welcome);
        lv_dots = (LinearLayout)findViewById(R.id.lv_dots);
        bt_next=(Button)findViewById(R.id.bt_next);
        bt_prev=(Button)findViewById(R.id.bt_prev);



        welcomePagerAdapter = new WelcomePagerAdapter(this);
        vp_welcome.setAdapter(welcomePagerAdapter);

        addDotIndicator(0);

        vp_welcome.addOnPageChangeListener(viewChangeListener);

        bt_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int current_page = vp_welcome.getCurrentItem()+1;

                if (current_page<mdots.length){
                    vp_welcome.setCurrentItem(current_page);
                }

                if(current_page==mdots.length){
                    current_page=mdots.length+1;
                }

                if(current_page > (mdots.length)){

                    Intent i = new Intent(WelcomeActivity.this, TutorialVideo.class);
                    startActivity(i);
                }

            }
        });
        bt_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current_page = vp_welcome.getCurrentItem()-1;
                vp_welcome.setCurrentItem(current_page);
                cnt-=1;
            }
        });

    }

    public void addDotIndicator(int position){
        mdots = new TextView[4];
        lv_dots.removeAllViews();

        //產生三個點點
        for(int i = 0;i<mdots.length;i++){
            mdots[i]= new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            //將點點加到linearLayout
            lv_dots.addView(mdots[i]);

        }
        if (mdots.length>0){
            mdots[position].setTextColor(getResources().getColor(R.color.coloeWhite));
        }
    }

    ViewPager.OnPageChangeListener viewChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotIndicator(position);
            current_page = position;



            if (current_page==0){
                bt_next.setEnabled(true);
                bt_prev.setEnabled(false);
                bt_prev.setVisibility(View.INVISIBLE);
                bt_prev.setText("");
                bt_next.setText("next");

            }else if(current_page ==mdots.length-1){
                bt_next.setEnabled(true);
                bt_prev.setEnabled(true);
                bt_prev.setVisibility(View.VISIBLE);
                bt_prev.setText("back");
                bt_next.setText("finish");

            }else{
                bt_next.setEnabled(true);
                bt_prev.setEnabled(true);
                bt_prev.setVisibility(View.VISIBLE);
                bt_prev.setText("back");
                bt_next.setText("next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(WelcomeActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent i = new Intent(Intent.ACTION_MAIN);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addCategory(Intent.CATEGORY_HOME);
                                    startActivity(i);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
        }
        return true;
    }
}
