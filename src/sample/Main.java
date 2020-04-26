package sample;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Main extends Application {

    static long speed=0;
    static int foodColor=0;
    static int width=20;
    static int height=20;
    static int foodX=0;
    static int foodY=0;
    static int cornerSize=25;
    static List<Corner> snake = new ArrayList<>();
    static Dir direction= Dir.left;
    static boolean gameOver=false;
    static Random rand =new Random();
    static boolean pause = false;
    static int score =0;


    public enum Dir
    {
        left,right,up,down
    }






    @Override
    public void start(Stage primaryStage) throws Exception{

        newFood();
        VBox root =new VBox();
        Canvas c = new Canvas(width*cornerSize,height*cornerSize);
        GraphicsContext gc = c.getGraphicsContext2D();
        root.getChildren().add(c);
        //tick(gc);


        new AnimationTimer()
        {

            long lastTick=0;

            public void handle(long now)
            {


                if(now-lastTick +speed*60000000>100000000)
                {
                    lastTick=now;
                    tick(gc);
                }

            }

        }.start();


        Scene scene =new Scene(root, width*cornerSize, height*cornerSize);


        scene.addEventFilter(KeyEvent.KEY_PRESSED,key->{//nacisniecie przycisku

            if(key.getCode()== KeyCode.W){direction=Dir.up;speed=1;}
            if(key.getCode()== KeyCode.A){direction=Dir.left;speed=1;}
            if(key.getCode()== KeyCode.S){direction=Dir.down;speed=1;}
            if(key.getCode()== KeyCode.D){direction=Dir.right;speed=1;}
            if(key.getCode()==KeyCode.P){pause=!pause;}

        });



        scene.addEventFilter(KeyEvent.KEY_RELEASED,key->{

            if(key.getCode()== KeyCode.W){direction=Dir.up;speed=0;}
            if(key.getCode()== KeyCode.A){direction=Dir.left;speed=0;}
            if(key.getCode()== KeyCode.S){direction=Dir.down;speed=0;}
            if(key.getCode()== KeyCode.D){direction=Dir.right;speed=0;}




        });





        snake.add(new Corner(width/2,height/2));//pierwsze elementy weza
        snake.add(new Corner(width/2,height/2));
        snake.add(new Corner(width/2,height/2));


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();

    }



    public static void tick(GraphicsContext gc) {

        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("",50));
            gc.fillText("GAME OVER",100,250);
            return;
        }


        if(pause)
        {
            gc.setFill(Color.RED);
            gc.setFont(new Font("",50));
            gc.fillText("Pause",200,250);
            return;
        }


        for (int i = snake.size() - 1; i >= 1; i--) {//przesuwanie weza
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }


        switch (direction) {


            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;


            case down:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                break;


            case right:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                break;


            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;


        }

        //eat food

        if ((foodX == snake.get(0).x) & (foodY == snake.get(0).y)) {
            snake.add(new Corner(-1, -1));
            score++;

            newFood();
        }


        //self destroy

        for (int i = 1; i < snake.size(); i++)
        {
            if((snake.get(0).x==snake.get(i).x)&(snake.get(0).y==snake.get(i).y))
                gameOver=true;

        }


    //fill background in black
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width*cornerSize,height*cornerSize);


    //score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("",30));
        gc.fillText("Score:"+score,10,30);



        //Food Color

        Color cc=Color.WHITE;

        switch(foodColor)
        {
            case 0: cc=Color.PURPLE;
            break;
            case 1: cc=Color.RED;
                break;
            case 2: cc=Color.CHOCOLATE;
                break;
            case 3: cc=Color.CYAN;
                break;
            case 4: cc=Color.DARKGRAY;
                break;

        }


        gc.setFill(cc);
        gc.fillOval(foodX*cornerSize,foodY*cornerSize,cornerSize,cornerSize);



        //snake

        for(Corner c:snake)//rysowanie weza
        {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x*cornerSize,c.y*cornerSize,cornerSize-3,cornerSize-3);
            gc.setFill(Color.GREEN);
            gc.fillRect(c.x*cornerSize,c.y*cornerSize,cornerSize-4,cornerSize-4);

        }



    }


//food
    public static void newFood()
    {
         foodX = rand.nextInt(width);
         foodY = rand.nextInt(height);
         foodColor=rand.nextInt(5);

    }





    public static void main(String[] args) {
        launch(args);
    }
}
