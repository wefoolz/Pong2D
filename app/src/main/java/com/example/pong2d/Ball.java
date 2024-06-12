package com.example.pong2d;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {
    public float cx;
    public float cy;
    public float velocity_x;
    public float velocity_y;

    private int radius;
    private Paint paint;

    public Ball(int radius, Paint paint) {
        this.paint = paint;
        this.radius=radius;
        this.velocity_x=PongTable.PHY_BALL_SPEED;
        this.velocity_y=PongTable.PHY_BALL_SPEED;
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(cx, cy, radius,paint);
    }

    public void moveBall(Canvas canvas){
        cx+=velocity_x;
        cy+=velocity_y;

        if(cy<radius){
            cy=radius;
        } else if (cy+radius>=canvas.getHeight()) {
            cy= canvas.getHeight()-radius-1;
        }
    }

    @Override
    public String toString() {
        return "cx=" + cx +
                " cy=" + cy +
                " velocity_cx=" + velocity_x +
                " velocity_cy=" + velocity_y +
                " radius=" + radius +
                " paint=" + paint;
    }
}
