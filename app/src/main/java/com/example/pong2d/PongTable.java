package com.example.pong2d;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class PongTable extends SurfaceView implements SurfaceHolder.Callback {



    private Player mPlayer;
    private Player mOpponent;
    private Ball mBall;
    private Paint mNetPaint;
    private Paint mTableBoundsPaint;
    private int mTableWidth;
    private int mTableHeight;
    SurfaceHolder mholder;
    private Context mContext;

    public static float PHY_RACQUET_SPEED=15.0F;
    public static float PHY_BALL_SPEED=15.0F;

    private float mAiMoveProbability;

    private boolean move;
    private float mlastTouchY;

    public void initPongTable(Context ctx, AttributeSet attr){
        mContext = ctx;
        mholder = getHolder();
        mholder.addCallback(this);

        //Game thread initializer

        TypedArray arr = ctx.obtainStyledAttributes(attr, R.styleable.PongTable);
        int racketHeight= arr.getInteger(R.styleable.PongTable_racketHeight, 340);
        int racketWidth= arr.getInteger(R.styleable.PongTable_racketWidth, 100);
        int ballRadius= arr.getInteger(R.styleable.PongTable_ballRaius, 25);

        //set player
        Paint playerPaint = new Paint();
        playerPaint.setAntiAlias(true);
        playerPaint.setColor(ContextCompat.getColor(mContext, R.color.player));
        mPlayer = new Player(racketWidth,racketHeight, playerPaint);

        //set opponent
        Paint opponentPaint = new Paint();
        opponentPaint.setAntiAlias(true);
        opponentPaint.setColor(ContextCompat.getColor(mContext, R.color.opponent));
        mOpponent = new Player(racketWidth,racketHeight, opponentPaint);

        //set ball
        Paint ballPaint = new Paint();
        ballPaint.setAntiAlias(true);
        ballPaint.setColor(ContextCompat.getColor(mContext, R.color.ball));
        mBall = new Ball(ballRadius, ballPaint);

        //Draw lines
        mNetPaint = new Paint();
        mNetPaint.setAntiAlias(true);
        mNetPaint.setColor(Color.WHITE);
        mNetPaint.setAlpha(80);
        mNetPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNetPaint.setStrokeWidth(10f);
        mNetPaint.setPathEffect(new DashPathEffect(new float[]{5,5},0));

        //set Bounds
        mTableBoundsPaint= new Paint();
        mTableBoundsPaint.setAntiAlias(true);
        mTableBoundsPaint.setColor(Color.BLACK);
        mTableBoundsPaint.setStyle(Paint.Style.STROKE);
        mTableBoundsPaint.setStrokeWidth(15f);

        //set Ai
        mAiMoveProbability = 0.8f;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(ContextCompat.getColor(mContext,R.color.table));
        canvas.drawRect(0,0,mTableWidth,mTableHeight,mTableBoundsPaint);
        int middle = mTableWidth/2;
        canvas.drawLine(middle,1,middle,mTableHeight-1,mNetPaint);

        mPlayer.draw(canvas);
        mOpponent.draw(canvas);
        mBall.draw(canvas);
    }

    public PongTable(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPongTable(context,attrs);
    }

    public PongTable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPongTable(context,attrs);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        mTableWidth=width;
        mTableHeight=height;

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void doAi(){
        if(mOpponent.bounds.top>mBall.cy){
            movePlayer(mOpponent,mOpponent.bounds.left, mOpponent.bounds.top-PHY_RACQUET_SPEED);
        } else if (mOpponent.bounds.top+mOpponent.getRacquetHeight()<mBall.cy) {
            movePlayer(mOpponent, mOpponent.bounds.left, mOpponent.bounds.top+PHY_RACQUET_SPEED);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private boolean isTouchOnRacket(MotionEvent event, Player mPlayer){
        return mPlayer.bounds.contains(event.getX(),event.getY());
    }

    public void movePlayerRacquet(float dy, Player player){

        synchronized (mholder){
            movePlayer(player,player.bounds.left,player.bounds.top+dy);
        }
    }
    public synchronized void movePlayer(Player player, float left, float top){
        if(left<2){
            left=2;
        }else if(left+ player.getRacquetWidth()>=mTableWidth-1){
            top=mTableHeight-player.getRacquetWidth()-2;
        }
        if(top<0){
            top=0;
        }else if(top+player.getRacquetHeight()-1>=mTableHeight){
            top=mTableHeight-player.getRacquetHeight()-1;
        }
    }


}
