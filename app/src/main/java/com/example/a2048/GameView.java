package com.example.a2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameView extends GridLayout {
    private Card[][] cardMaps = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();
    public SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//音效
    public HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
    private Context context;
    public GameView(Context context) {
        super(context);
        this.context = context;
        initGameView();//初始化
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initGameView();
    }

    public void initGameView() {
        soundMap.put(1, soundPool.load(context, R.raw.newcard, 1));
        soundMap.put(2, soundPool.load(context, R.raw.merge, 1));
        setColumnCount(4);//每行四个
        setBackgroundColor(0xffbbada0);
        addCards(getCardWitch(), getCardWitch());//添加每个数字布局
        startGame();//开始游戏
        setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY, offsetX, offsetY;


            //通过手指移动的方向进行操作,-5是为了处理误差
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                            } else if (offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                            } else if (offsetY > 5) {
                                swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }


    //添加卡片
    private void addCards(int cardWidth, int cardHeight) {
        Card c;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                c.setNumber(0);
                c.setRadius(20);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(cardWidth-30, cardHeight-30);//铺满整个布局
                lp.setMargins(10,10,10,10);
                addView(c,lp);
                cardMaps[x][y] = c;
            }
        }
    }
    //每次操作后随机位置添加数字,如果这个位置数字不为零则不添加
    private void addRandomNum() {
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardMaps[x][y].getNumber() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }
        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        cardMaps[p.x][p.y].setNumber(Math.random() > 0.1 ? 2 : 4);
        cardMaps[p.x][p.y].startScaleInAnim(context);
        this.soundPool.play(this.soundMap.get(1), 1, 1, 0, 0, 1);
    }
    //开始游戏
    public void startGame() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardMaps[x][y].setNumber(0);
            }
        }
        //开始时显示两个数字
        addRandomNum();
        addRandomNum();

    }

    private void swipeLeft() {
        boolean merge = false;//判断是否发生操作
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (cardMaps[x1][y].getNumber() > 0) {//相邻数字大于零
                        if (cardMaps[x][y].getNumber() <= 0) {//最边上的数字为零则这个地方数字变为该数字
                            cardMaps[x][y].setNumber(cardMaps[x1][y].getNumber());
                            cardMaps[x1][y].setNumber(0);
                            x--;
                            merge = true;
                        } else if (cardMaps[x][y].equals(cardMaps[x1][y])) {//两个数字相同则合并
                            cardMaps[x][y].setNumber(cardMaps[x][y].getNumber() * 2);
                            cardMaps[x][y].startScaleInAnim(context);
                            cardMaps[x1][y].setNumber(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNumber());
                            merge = true;
                            this.soundPool.play(this.soundMap.get(2), 1, 1, 0, 0, 1);
                        }
                        break;
                    }
                }
            }

        }
        if (merge == true) {
            addRandomNum();//添加一个数字
            checkCompete();//判断游戏是否结束
        }
    }

    private void swipeRight() {
        boolean merge = false;

        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardMaps[x1][y].getNumber() > 0) {
                        if (cardMaps[x][y].getNumber() <= 0) {
                            cardMaps[x][y].setNumber(cardMaps[x1][y].getNumber());
                            cardMaps[x1][y].setNumber(0);
                            merge = true;
                            x++;
                        } else if (cardMaps[x][y].equals(cardMaps[x1][y])) {
                            cardMaps[x][y].setNumber(cardMaps[x][y].getNumber() * 2);
                            cardMaps[x][y].startScaleInAnim(context);
                            cardMaps[x1][y].setNumber(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNumber());
                            merge = true;
                            this.soundPool.play(this.soundMap.get(2), 1, 1, 0, 0, 1);
                        }
                        break;
                    }
                }
            }

        }
        if (merge == true) {
            addRandomNum();
            checkCompete();
        }
    }

    private void swipeUp() {
        boolean merge = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (cardMaps[x][y1].getNumber() > 0) {
                        if (cardMaps[x][y].getNumber() <= 0) {
                            cardMaps[x][y].setNumber(cardMaps[x][y1].getNumber());
                            cardMaps[x][y1].setNumber(0);
                            y--;
                            merge = true;
                        } else if (cardMaps[x][y].equals(cardMaps[x][y1])) {
                            cardMaps[x][y].setNumber(cardMaps[x][y].getNumber() * 2);
                            cardMaps[x][y].startScaleInAnim(context);
                            cardMaps[x][y1].setNumber(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNumber());
                            merge = true;
                            this.soundPool.play(this.soundMap.get(2), 1, 1, 0, 0, 1);
                        }
                        break;
                    }
                }
            }
        }
        if (merge == true) {
            addRandomNum();
            checkCompete();
        }
    }

    private void swipeDown() {
        boolean merge = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardMaps[x][y1].getNumber() > 0) {
                        if (cardMaps[x][y].getNumber() <= 0) {
                            cardMaps[x][y].setNumber(cardMaps[x][y1].getNumber());
                            cardMaps[x][y1].setNumber(0);
                            y++;
                            merge = true;
                        } else if (cardMaps[x][y].equals(cardMaps[x][y1])) {
                            cardMaps[x][y].setNumber(cardMaps[x][y].getNumber() * 2);
                            cardMaps[x][y].startScaleInAnim(context);
                            cardMaps[x][y1].setNumber(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNumber());
                            merge = true;
                            this.soundPool.play(this.soundMap.get(2), 1, 1, 0, 0, 1);
                        }
                        break;
                    }
                }
            }
        }
        if (merge == true) {
            addRandomNum();
            checkCompete();
        }
    }


    private int getCardWitch() {
        //声明屏幕对象
        DisplayMetrics displayMetrics;
        displayMetrics = getResources().getDisplayMetrics();
        int cardWitch;
        //提取屏幕宽
        cardWitch = displayMetrics.widthPixels;
        return (cardWitch - 10) / 4;
    }

    private void checkCompete() {
        boolean complete = true;
        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                //如果每个数字前后左右都没有相同的数字了则游戏结束
                if (cardMaps[x][y].getNumber() == 0 ||
                        (x > 0 && cardMaps[x][y].equals(cardMaps[x - 1][y])) ||
                        (x < 3 && cardMaps[x][y].equals(cardMaps[x + 1][y])) ||
                        (y > 0 && cardMaps[x][y].equals(cardMaps[x][y - 1])) ||
                        (y < 3 && cardMaps[x][y].equals(cardMaps[x][y + 1]))) {
                    complete = false;
                    break ALL;
                }
            }

        }
        if (complete == true) {
            new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束").setPositiveButton("重来", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.getMainActivity().writeScore();//存储最高分
                    MainActivity.getMainActivity().clearScore();
                    startGame();//开始游戏
                }
            }).show();

        }

    }
}
