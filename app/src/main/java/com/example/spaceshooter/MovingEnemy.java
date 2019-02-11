package com.example.spaceshooter;

public class Missile extends Enemy {
    private float _amplitude = 10;
    private float _frequency = 32;
    private int counter = 0;

    Missile(){
        _amplitude = 5;
        _frequency = 10;
    }

    @Override
    void update(){
        _y += _amplitude * Math.sin(counter/_frequency);
        counter++;
        super.update();
    }
}
