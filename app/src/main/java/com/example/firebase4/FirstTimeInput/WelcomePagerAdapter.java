package com.example.firebase4.FirstTimeInput;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.firebase4.R;

public class WelcomePagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public WelcomePagerAdapter(Context context){
        this.context = context;
    }


    //valueArrays
    public int[] slideImage={
            R.drawable.hey_you,
            R.drawable.healthy_girl_icon,
            R.drawable.diet_icon,
            R.drawable.fitness_icon
    };

    public String[] sliderTitle={
            "Hey Hey you",
            "什麼是斷食",
            "斷食的迷思",
            "斷食並不會影響健康"
    };

    public String[] sliderContext={
            "Yea ~you",
            "洋樹起戰了論求，支化易故拉質？是一小立提方上法一房上標們身過活多到度直證，不飛沒，山河光工文亞各著。使方前臺現算天在？總著是年沒面要住量再大？記安黑興發我的到氣處接單他快企。少對一燈。",
            "年的相眾龍色在濟業學遠。說多非……命造城多，開型的大陽衣寫藝，學高之，十依服入你破則情香地種、管濟港有義士命流如期今市叫工",
            "結正頭成時辦我事聲老解而書從小出斯外……然業手如同外福告球孩；拉行有到無靈中，間財我我、行示做省，觀手了語易廣、演人一裡們前愛代長成！金那解影轉在防行細背！巴名自都？點是果住。一工打"
    };


    @Override
    public int getCount() {
        return sliderTitle.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==  object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        //相當於 tv_show=(TextView)findViewById(R.id.tv_show)
        //init the layoutInflater variable
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView img_show = (ImageView) view.findViewById(R.id.img_show);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_description=(TextView) view.findViewById(R.id.tv_description);

        img_show.setImageResource(slideImage[position]);
        tv_title.setText(sliderTitle[position]);
        tv_description.setText(sliderContext[position]);


        container.addView(view);

        return  view;

    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
