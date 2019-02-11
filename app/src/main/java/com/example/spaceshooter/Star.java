package com.example.spaceshooter;

public class Star extends GeometricEntity {
    static final String TAG = "Star";


    Star(){
        respawn();
    }

    @Override
    void respawn(){
        _x = _game._rng.nextInt(Game.STAGE_WIDTH);
        _y = _game._rng.nextInt(Game.STAGE_HEIGHT);
        _radius = _game._rng.nextInt(6)+2; // TODO: Hard coded magic value

        int red = 255; // TODO: Odd values
        int green = _game._rng.nextInt(250-100)+100; //
        int blue = 0;
        set_color(red, green, blue);

        _width= _radius*2;
        _height= _radius*2;
        _velX = -0.5f; // TODO: Hard coded magic value
    }

    @Override
    void update(){
        _x += _velX;
        _y += _velY;
        _velX= (int)-(_game._playerSpeed * (0.1*_radius)); // TODO: Check values,
        _x = Utils.wrap(_x, 0, Game.STAGE_WIDTH + _width);
    }

    @Override
    void onCollision(Entity that) {} //Do nothing - should maybe not be abstract?
}
