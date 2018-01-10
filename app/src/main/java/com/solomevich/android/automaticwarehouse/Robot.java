package com.solomevich.android.automaticwarehouse;

/**
 * Created by 15 on 27.10.2017.
 */
import java.util.LinkedList;
import java.util.Random;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Robot {

    /**Х и У коорданаты*/
    public int x;
    public int y;

    /**Скорость*/
    public int speed;

    /**Выосота и ширина спрайта*/
    public int width;
    public int height;

    public GameView gameView;
    public Bitmap bmp;


    /**Конструктор класса*/
    public Robot(GameView gameView, Bitmap bmp, int i){

        this.gameView = gameView;
        this.bmp = bmp;

        this.x = 1860+i*100;
        this.y = 830;

        this.speed = 1;

        this.width = 5;
        this.height = 5;
    }
    private Context context;

    public void onDrawLeft(Canvas c)
    {
        x -= speed;
        c.drawBitmap(bmp, x, y, null);
    }

    public void onDrawTop(Canvas c)
    {
        y -= speed;
        c.drawBitmap(bmp, x, y, null);
    }
    public void onDrawBottom(Canvas c)
    {
        y += speed;
        c.drawBitmap(bmp, x, y, null);
    }
    public void onDrawRight(Canvas c)
    {
        x += speed;
        c.drawBitmap(bmp, x, y, null);
    }
    public void onDrawPlace(Canvas c)
    {
        c.drawBitmap(bmp, x, y, null);
    }

}
