package com.example.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public abstract class GeometricEntity extends Entity {
    private int _color = Color.YELLOW;
    protected float _radius; // TODO: Default value? Only rounds

    @Override
    void update() {

    }

    @Override
    void render(Canvas canvas, Paint paint) {
        paint.setColor(_color);
        canvas.drawCircle(_x + _radius, _y+_radius, _radius, paint);
    }

    @Override
    void onCollision(Entity that) {
        respawn();
    }

    @Override
    void respawn() {

    }

    public void set_color(int color) {
        this._color = color;
    }

    public void set_color(int red, int green, int blue) {
        _color = Color.rgb(red, green, blue);
    }
}
