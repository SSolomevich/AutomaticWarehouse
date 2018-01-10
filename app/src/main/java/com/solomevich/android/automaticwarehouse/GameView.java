package com.solomevich.android.automaticwarehouse;

/**
 * Created by 15 on 26.10.2017.
 */

import java.util.ArrayList;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;




public class GameView extends SurfaceView
        implements Runnable
{


    int c ;
    /**Объект класса GameLoopThread*/
    private GameThread mThread;
    int countRobot;
    int endX;
    int endY;

    //    robot хранит роботов
    private List<Robot> robot = new ArrayList<Robot>();
    Bitmap robots;
    //    box хранит ящики
    private List<Box> box = new LinkedList<>();
    //    boxInWarehouseList хранит значение true, если ящик вывезли со склада
    private LinkedList<Boolean> boxInWarehouseList = new LinkedList<>();
    //    boxInWorkList хранит значение true, если робот занял ящик для отвоза
    private LinkedList<Boolean> boxInWorkList = new LinkedList<>();
    //    numberRobot хранит значение номера робота, который будет отвозить данный ящик
    private LinkedList<Integer> numberRobot = new LinkedList<>();
//    закрашивает область в белый, на которой стоял ящик
    private List<Box> boxWhite = new ArrayList<Box>();
    Bitmap boxes;
    Bitmap white;
    LinkedHashSet<String> dialog;
    LinkedList<String> dialogFinal;
//    переменная запуска отображения роботов
    Boolean startPlay;
    Paint p;
//    отрисовка склада
    float[] points;
//    отрисовка кнопки simulate
    float[] pointsSimulate;


// При нажатии на Simulate запускается работа роботов
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        Resources res = getResources();
        if (
        event.getX()>=res.getInteger(R.integer.buttom_simulate_0_y)&&event.getX()<=res.getInteger(R.integer.buttom_simulate_max_y)&&
        event.getY()>=res.getInteger(R.integer.buttom_simulate_0_x)&&event.getY()<=res.getInteger(R.integer.buttom_simulate_max_x)
        &&action==MotionEvent.ACTION_DOWN||action==MotionEvent.ACTION_MOVE
                ) {
            startPlay=true;
        }
        return true;
    }
    /**Переменная запускающая поток рисования*/
    private boolean running = false;

    //-------------Start of GameThread--------------------------------------------------\\

    public class GameThread extends Thread
    {
        /**Объект класса*/
        private GameView view;

        /**Конструктор класса*/
        public GameThread(GameView view)
        {
            this.view = view;
        }

        /**Задание состояния потока*/
        public void setRunning(boolean run)
        {
            running = run;
        }

        /** Действия, выполняемые в потоке */
        public void run()
        {
            while (running)
            {
                Canvas canvas = null;
                try
                {
                    // подготовка Canvas-а
                    canvas = view.getHolder().lockCanvas();

                    synchronized (view.getHolder())

                    {
                        // собственно рисование
                        onDraw(canvas);
                    }
                }
                catch (Exception e) { }
                finally
                {
                    if (canvas != null)
                    {
                        view.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    //-------------End of GameThread--------------------------------------------------\\



    public GameView(Context context, String s)
    {
        super(context);
        mThread = new GameThread(this);
        p = new Paint();
        startPlay=false;
        Resources res = getResources();
        /*Рисуем все наши объекты и все все все*/
//        Отрисовка склада
        points = new float[]{res.getInteger(R.integer.field_0_y),res.getInteger(R.integer.field_0_x),
                res.getInteger(R.integer.field_max_y),res.getInteger(R.integer.field_0_x),


                res.getInteger(R.integer.field_0_y),res.getInteger(R.integer.field_max_x),
                res.getInteger(R.integer.field_max_y),res.getInteger(R.integer.field_max_x),

                res.getInteger(R.integer.field_0_y),res.getInteger(R.integer.field_0_x),
                res.getInteger(R.integer.field_0_y),res.getInteger(R.integer.field_end1_x),

                res.getInteger(R.integer.field_0_y),res.getInteger(R.integer.field_end2_x),
                res.getInteger(R.integer.field_0_y),res.getInteger(R.integer.field_max_x),


                res.getInteger(R.integer.field_max_y),res.getInteger(R.integer.field_0_x),
                res.getInteger(R.integer.field_max_y),res.getInteger(R.integer.field_start1_y),

                res.getInteger(R.integer.field_max_y),res.getInteger(R.integer.field_start2_y),
                res.getInteger(R.integer.field_max_y),res.getInteger(R.integer.field_max_x),

        };

//        Отрисовка кнопки Simulate
        pointsSimulate = new float[]{res.getInteger(R.integer.buttom_simulate_0_y),res.getInteger(R.integer.buttom_simulate_0_x),
                res.getInteger(R.integer.buttom_simulate_max_y),res.getInteger(R.integer.buttom_simulate_0_x),

                res.getInteger(R.integer.buttom_simulate_max_y),res.getInteger(R.integer.buttom_simulate_0_x),
                res.getInteger(R.integer.buttom_simulate_max_y),res.getInteger(R.integer.buttom_simulate_max_x),

                res.getInteger(R.integer.buttom_simulate_max_y),res.getInteger(R.integer.buttom_simulate_max_x),
                res.getInteger(R.integer.buttom_simulate_0_y),res.getInteger(R.integer.buttom_simulate_max_x),

                res.getInteger(R.integer.buttom_simulate_0_y),res.getInteger(R.integer.buttom_simulate_0_x),
                res.getInteger(R.integer.buttom_simulate_0_y),res.getInteger(R.integer.buttom_simulate_max_x),
        };

//     Место для вывоза ящиков
        endX=res.getInteger(R.integer.field_endx);
        endY=res.getInteger(R.integer.field_endy);


        getHolder().addCallback(new SurfaceHolder.Callback()
        {
            /*** Уничтожение области рисования */
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                boolean retry = true;
                mThread.setRunning(false);
                while (retry)
                {
                    try
                    {
                        // ожидание завершение потока
                        mThread.join();
                        retry = false;
                    }
                    catch (InterruptedException e) { }
                }
            }

            /** Создание области рисования */

            public void surfaceCreated(SurfaceHolder holder)
            {


                mThread.setRunning(true);
                mThread.start();


            }

            /** Изменение области рисования */
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {
            }
        });


        int[] boxes2 = res.getIntArray(R.array.boxes);

// считывание всех координат ящиков из ресурсов
        boxes= BitmapFactory.decodeResource(getResources(), R.drawable.boxmini);


        for (int i=0;i<boxes2.length/2;i++) {
            // создаем ящики, их количество в 2 раза меньше количества координат
            box.add(new Box(this, boxes,context,i));
            // все ящики сразу на складе, вывезенных нет
            boxInWarehouseList.add(false);
            // все ящики сразу не в работе, не заняты роботами
            boxInWorkList.add(false);
            numberRobot.add(0);
        }

//        количество роботов
        countRobot=Integer.parseInt(s);


if (countRobot>0) {
//если количество роботов больше, чем ящиков, то отображается только столько роботов, сколько ящиков,
//    каждый робот толкает один ящик
    if (countRobot > box.size()) {
        for (int i = 0; i < numberRobot.size(); i++) {
            numberRobot.set(i, i + 1);
        }
//        иначе назначаем роботам ящики, которые они должны отвезти, причем делим поровну.
//        если не делится поровну, то тому, кто раньше начал возить достается больше ящиков
    } else {
        if (box.size() % countRobot == 0) {
            for (int i = 0; i < countRobot; i++) {
                numberRobot.set(numberRobot.size() / countRobot * i, i + 1);
            }
        } else {
            if (countRobot == 2) {
                for (int i = 0; i < countRobot; i++) {
                    numberRobot.set((numberRobot.size() + 1) / countRobot * i, i + 1);
                }
            } else if (countRobot == 3) {
                if (box.size() % countRobot > 1) {
                    for (int i = 0; i < countRobot; i++) {
                        numberRobot.set((numberRobot.size() + 1) / countRobot * i, i + 1);
                    }
                } else {
                    numberRobot.set(0, 1);
                    for (int i = 1; i < countRobot; i++) {
                        numberRobot.set(numberRobot.size() / countRobot * i + 1, i + 1);
                    }
                }
            } else if (countRobot == 4) {
                if (box.size() % countRobot < 2) {
                    numberRobot.set(0, 1);
                    for (int i = 1; i < countRobot; i++) {
                        numberRobot.set(numberRobot.size() / countRobot * i + 1, i + 1);
                    }
                } else if (box.size() % countRobot == 2) {

                    for (int i = 1; i < countRobot; i++) {
                        numberRobot.set(0, 1);
                        numberRobot.set(box.size() / countRobot + 1, 2);
                        numberRobot.set((box.size() / countRobot + 1) * 2, 3);
                        numberRobot.set((box.size() / countRobot) + (box.size() / countRobot + 1) * 2, 4);
                    }
                } else if (box.size() % countRobot == 3) {

                    for (int i = 0; i < countRobot; i++) {
                        numberRobot.set((numberRobot.size() + 1) / countRobot * i, i + 1);
                    }
                }
            } else if (countRobot == 5) {
                if (box.size() % countRobot == 1) {
                    numberRobot.set(0, 1);
                    for (int i = 1; i < countRobot; i++) {
                        numberRobot.set(numberRobot.size() / countRobot * i + 1, i + 1);
                    }
                } else if (box.size() % countRobot == 2) {
                    for (int i = 0; i < countRobot; i++) {
                        numberRobot.set(0, 1);
                        numberRobot.set(box.size() / countRobot + 1, 2);
                        numberRobot.set((box.size() / countRobot + 1) * 2, 3);
                        numberRobot.set((box.size() / countRobot) + (box.size() / countRobot + 1) * 2, 4);
                        numberRobot.set((box.size() / countRobot) + (box.size() / countRobot + 1) * 2 + box.size() / countRobot, 5);
                    }
                } else if (box.size() % countRobot == 3) {
                    for (int i = 0; i < countRobot; i++) {
                        numberRobot.set(0, 1);
                        numberRobot.set(box.size() / countRobot + 1, 2);
                        numberRobot.set((box.size() / countRobot + 1) * 2, 3);
                        numberRobot.set((box.size() / countRobot) + 1 + (box.size() / countRobot + 1) * 2, 4);
                        numberRobot.set((box.size() / countRobot) + (box.size() / countRobot + 1) * 2 + box.size() / countRobot + 1, 5);
                    }
                } else if (box.size() % countRobot == 4) {
                    for (int i = 0; i < countRobot; i++) {
                        numberRobot.set((numberRobot.size() + 1) / countRobot * i, i + 1);
                    }

                }
            }

        }
    }

}

        white= BitmapFactory.decodeResource(getResources(), R.drawable.white);

        boxWhite.add(new Box(this, white,context,0));




        robots = BitmapFactory.decodeResource(getResources(), R.drawable.robotdemo);
        for (int i=0;i<boxes2.length-1;i=i+2) {
            robot.add(new Robot(this, robots,i));
        }

        dialog=new LinkedHashSet<>();

    }






    /**Функция рисующая все спрайты и фон*/
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        p.setStrokeWidth(10);
        Resources res = getResources();

        for (int i=0;i<box.size();i++){
            box.get(i).onDraw(canvas,boxes, box.get(i).x, box.get(i).y, null);
        }
        p.setColor(Color.BLUE);
        canvas.drawLines(pointsSimulate,p);
        p.setTextSize(85);
        canvas.drawText(res.getString(R.string.Simulate), res.getInteger(R.integer.simulate_x), res.getInteger(R.integer.simulate_y), p);
        p.setColor(Color.BLACK);
// рисуем линии по точкам из массива points1
        canvas.drawLines(points,p);


        p.setTextSize(46);
        canvas.drawText("Start", res.getInteger(R.integer.field_start_start), res.getInteger(R.integer.field_start_end), p);
        canvas.drawText("End", res.getInteger(R.integer.field_end_start), res.getInteger(R.integer.field_end_end), p);



        dialogFinal = new LinkedList<>();


if (startPlay) {

//вывод диалога роботов

    for (String set : dialog) {

        dialogFinal.add(set);

    }

    //выводить не больше 8 сообщений
    if (dialogFinal.size() > 7) {
        for (int a = 0; a < 8; a++) {
            canvas.drawText(dialogFinal.get(dialogFinal.size() - 8 + a), 50, 100 * (a + 1), p);
        }
    } else {
        for (int a = 0; a < dialogFinal.size(); a++) {
            canvas.drawText(dialogFinal.get(a), 50, 100 * (a + 1), p);
        }
    }


    //действия роботов. перебираем роботов и ящики.
        for (int numberRob=1;numberRob<countRobot+1;numberRob++) {
            for (int a = 0; a < boxInWarehouseList.size(); a++) {
//                если ящик на складе и определен робот, который отвозит, то робот везет
                if (boxInWarehouseList.get(a) == false && numberRobot.get(a) == numberRob) {
                    boxInWarehouseList.set(a, driveUp(canvas, a * 2, robot.get(numberRob-1), boxInWarehouseList.get(a)));
                    dialog.add("Я "+numberRob +", отвезу "+(a+1) +" ящик");

                }
// если ящик отвезли к выходу, то робот переключается на следующий ящик
                if (boxInWarehouseList.get(a) == true) {
                    if(numberRobot.get(a)!=100){
                        numberRobot.set(a, 100);
                        dialog.add("Я "+numberRob +", отвез "+(a+1) +" ящик");}

                    int nextNumberBox=a;
                    if (a<boxInWarehouseList.size()-1&&boxInWarehouseList.get(nextNumberBox + 1) == false && numberRobot.get(nextNumberBox + 1) == 0) {
                        numberRobot.set(nextNumberBox + 1, numberRob);

                    }

                }

            }

        }
}

    }




    //метод для отрисовки движения роботов и ящиков
    public Boolean driveUp(Canvas canvas, Integer j, Robot robot, Boolean boxInWarehousefalse){
        Resources res = getResources();
        int[] boxes2 = res.getIntArray(R.array.boxes);

            int xBox = 0;
            int yBox = 0;

            LinkedList<Integer> coordinatesBoxes = findBox();

// координаты начальные ящика, который отвозит робот
            xBox = coordinatesBoxes.get(j);
            yBox = coordinatesBoxes.get(j + 1) + 60;


//если отвез ящик и стоит у выхода, то начнет двигаться вниз, пока не доедет ниже ящика
        if (box.get(j / 2).y != endY &&robot.x != xBox&&
                box.get(j / 2).x!=endX
                &&robot.y < yBox
                ) {
            robot.onDrawBottom(canvas);

        }

//        остальные движения

            if (robot.x != xBox && robot.y != endY && box.get(j / 2).x != endX && box.get(j / 2).y != endY
                    && robot.x>box.get(j / 2).x&&robot.y == 830
                    ) {
                robot.onDrawLeft(canvas);
            }


            if (robot.x != xBox && robot.y != endY && box.get(j / 2).x != endX && box.get(j / 2).y != endY
                    && robot.x<box.get(j / 2).x&&robot.y == 830||robot.y == yBox
                    ) {
                robot.onDrawRight(canvas);
            }




            if (robot.x == xBox && robot.y != yBox && robot.y - endY > yBox - endY

                    ) {
                robot.onDrawTop(canvas);
            }




            if (robot.y - endY <= yBox - endY && robot.y != endY && box.get(j / 2).y != endY
                    &&box.get(j / 2).x==robot.x
                    ) {

                boxWhite.get(0).onDraw(canvas, white, boxes2[j], boxes2[j + 1], null);
                robot.onDrawTop(canvas);
                box.get(j / 2).onDrawTop(canvas);
            }

            if (box.get(j / 2).y == endY && robot.x != endX && robot.x != box.get(j / 2).x + 60 &&
                    robot.y != endY&& box.get(j / 2).x != endX) {

                boxWhite.get(0).onDraw(canvas, white, boxes2[j], boxes2[j + 1], null);
                robot.onDrawRight(canvas);
                box.get(j / 2).onDrawPlace(canvas);
            }
            if (box.get(j / 2).y == endY && robot.x == box.get(j / 2).x + 60 && robot.y != endY) {

                boxWhite.get(0).onDraw(canvas, white, boxes2[j], boxes2[j + 1], null);
                robot.onDrawTop(canvas);
                box.get(j / 2).onDrawPlace(canvas);
            }

            if (box.get(j / 2).y == endY && robot.y == endY && robot.x - endX > box.get(j / 2).x - endX + 60) {

                boxWhite.get(0).onDraw(canvas, white, boxes2[j], boxes2[j + 1], null);
                robot.onDrawLeft(canvas);
                box.get(j / 2).onDrawPlace(canvas);
            }


            if (box.get(j / 2).y == endY && robot.y == endY && robot.x - endX <= box.get(j / 2).x - endX + 60 &&
                    robot.x != endX && box.get(j / 2).x != endX

                    ) {
                boxWhite.get(0).onDraw(canvas, white, boxes2[j], boxes2[j + 1], null);
                boxWhite.get(j % 2).onDraw(canvas, white, box.get(j / 2).x, box.get(j / 2).y, null);
                robot.onDrawLeft(canvas);
                box.get(j / 2).onDrawLeft(canvas);
            }




        if (box.get(j / 2).y == endY &&
                box.get(j / 2).x==endX
                &&robot.y == endY
                ) {
            boxWhite.get(0).onDraw(canvas, white, boxes2[j], boxes2[j + 1], null);
            boxWhite.get(j % 2).onDraw(canvas, white, box.get(j / 2).x, box.get(j / 2).y, null);

            robot.onDrawPlace(canvas);
            box.get(j / 2).onDrawPlace(canvas);
//            когда выполняется это условие, переключается на след ящик
            boxInWarehousefalse=true;

        }

return boxInWarehousefalse;

    }

// метод возращает линкедлист со всеми координатами
    public LinkedList<Integer> findBox(){
        Resources res = getResources();


        int[] boxes2 = res.getIntArray(R.array.boxes);


        LinkedList<Integer> list=new LinkedList<>();
        for (int i=0;i<boxes2.length;i++){
            list.add(boxes2[i]);
        }
        return list;
    }


    public void run() {

    }


}
