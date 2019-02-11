package com.example.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bullet extends GeometricEntity {
    static final String TAG = "Bullet";
    private Entity _shooter;
    private boolean shooting = false;

    public Bullet(Entity shooter){
        set_color(Color.WHITE);
        _shooter= shooter;  // TODO: Kolla om fult?
        respawn();
    }

    @Override
    void update() {
        if(_game._isBoosting && _x == _shooter.right()+1){
            shooting =true;
            _velX = 60f;
                Game._jukebox.play(Jukebox.SHOOT); // TODO: Bad placement
        }

        if(shooting){
            _x += _velX;
        } else {
            _x = _shooter.right()+1;
            _y = _shooter.centerY()-10;
        }


        if (left() > Game.STAGE_WIDTH) {
            respawn();
        }

    }

    @Override
    void render(Canvas canvas, Paint paint) {
        if(shooting){
            super.render(canvas, paint);
        }
    }

    @Override
    void respawn() {
        _radius =4; // TODO: Hard coded magic value
        _width= _radius*2;
        _height= _radius*2;

        _y = _shooter.centerY()-10;
        _x = _shooter.right()+1;
        _velX = _game._playerSpeed;
        shooting = false;
    }
}
