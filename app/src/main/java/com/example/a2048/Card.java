package com.example.a2048;

import android.content.Context;
import android.print.PrintAttributes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class Card extends CardView {
    private int number = 0;
    private TextView label;
    public Card(@NonNull Context context) {
        super(context);
        label = new TextView(getContext());
        label.setTextSize(32);
        label.setGravity(Gravity.CENTER);
        label.setBackgroundColor(0xffffffff);
        LayoutParams lp = new LayoutParams(-1,-1);//铺满整个布局
        addView(label,lp);
        setNumber(0);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        if(number<=0){
            label.setText("");
            label.setBackgroundColor(0xffFFFFFF);//根据数字大小显示颜色
        }else {
            label.setText(number + "");
            switch (number){
                case 2:
                    label.setBackgroundColor(0xffFFEFD5);
                    break;
                case 4:
                    label.setBackgroundColor(0xffFFE4E1);
                    break;
                case 8:
                    label.setBackgroundColor(0xffFFDEAD);
                    break;
                case 16:
                    label.setBackgroundColor(0xffFFC1C1);
                    break;
                case 32:
                    label.setBackgroundColor(0xffFFB90F);
                    break;
                case 64:
                    label.setBackgroundColor(0xffFFA54F);
                    break;
                case 128:
                    label.setBackgroundColor(0xffFF8C00);
                    break;
                case 256:
                    label.setBackgroundColor(0xffFF7F50);
                    break;
                case 512:
                    label.setBackgroundColor(0xffFF6EB4);
                    break;
                case 1024:
                    label.setBackgroundColor(0xffFF4500);
                    break;
                case 2048:
                    label.setBackgroundColor(0xffFF3030);
                    break;
                default:
                    label.setBackgroundColor(0xffFF0000);
            }
        }
    }
    public boolean equals(Card o){
        return getNumber() == o.getNumber();
    }
    /**
     * 缩放变大动画
     *

     */
    public  void startScaleInAnim(Context context) {
        Animation animation_in = AnimationUtils.loadAnimation(context, R.anim.anim_scale_in);
        if (label!= null) {
            label.startAnimation(animation_in);

        }
    }
}
