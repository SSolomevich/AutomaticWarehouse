package com.solomevich.android.automaticwarehouse;

/**
 * Created by 15 on 26.10.2017.
 */
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;


import java.util.LinkedList;




public class Box
{
    /**Объект главного класса*/
    GameView gameView;

    //спрайт
    Bitmap bmp;

    //х и у координаты ящика
    int x;
    int y;
    public int speed;

    private Context context;
    //конструктор
    public Box(GameView gameView, Bitmap bmp, Context current, int i)
    {
        this.context = current;
         LinkedList<Integer> coord = findBox();
        this.gameView = gameView;
        this.bmp = bmp;
        this.x = coord.get(i*2);
        this.y = coord.get(i*2+1);
        this.speed = 1;
    }

    //рисуем наш спрайт
    public void onDraw(Canvas c, Bitmap bmp2, int x1, int y1, GameView g)
    {
        c.drawBitmap(bmp2, x1, y1, null);
    }


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

    public LinkedList<Integer> findBox(){
        Resources res = context.getResources();


        int[] boxes2 = res.getIntArray(R.array.boxes);



        LinkedList<Integer> list=new LinkedList<>();
        for (int i=0;i<boxes2.length;i++){
            list.add(boxes2[i]);
        }

        return list;
    }

}
