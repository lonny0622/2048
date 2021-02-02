package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.PublicKey;

public class MainActivity extends AppCompatActivity {
    private TextView tvScore;//显示当前得分
    private TextView tvHighestScore;//显示最高分
    private Button btnRestart;
    private GameView Gv;
    public static MainActivity mainActivity = null;
    private int score = 0;
    private SharedPreferences mSp;//储存最高分
    public MainActivity(){
            mainActivity = this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSp=super.getSharedPreferences("Highest_Score",MODE_PRIVATE);//初始化SharedPreferences对象
        tvScore = findViewById(R.id.tv_score);
        btnRestart = findViewById(R.id.btn_restart);
        Gv = findViewById(R.id.game_view);
        showScore();
        tvHighestScore = findViewById(R.id.tv_highest_score);
        tvHighestScore.setText(readScore());//显示最高得分
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gv.startGame();
                writeScore();
                clearScore();
                showScore();
            }
        });

    }
    public static MainActivity getMainActivity(){
        return mainActivity;
    }
    public void clearScore(){
        score=0;
        showScore();
    }//分数清零
    public  void showScore(){
        tvScore.setText(score+"");
    }//设置分数
    public void addScore(int s){
        score += s;
        showScore();//添加分数
    }
    //存数据
    public void writeScore() {
        SharedPreferences.Editor editor=mSp.edit();
        int high_score =Integer.parseInt(readScore());
        int new_score=Integer.parseInt(tvScore.getText().toString());
        if(new_score>high_score) {
            editor.putString("Highest_Score", tvScore.getText().toString());
            editor.commit();
            tvHighestScore.setText(new_score+"");
        }

    }
    //读数据
    public String readScore() {
        if(mSp.getString("Highest_Score", "")==""){
            return "0";
        }else {
            return  mSp.getString("Highest_Score", "");
        }

    }



}