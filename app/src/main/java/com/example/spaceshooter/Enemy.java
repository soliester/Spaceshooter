package com.example.spaceshooter;

import static java.lang.Math.PI;

public class Enemy extends BitmapEntity {
    private final static int ENEMY_HEIGHT = 50;
    private final static int ENEMY_SPAWN_OFFSET = Game.STAGE_WIDTH;
    private int _enemyMinSpeed = 2;   // TODO: Check name - is it just vel?
    private int _enemyMaxSpeed = 20;
    private int _currentMaxSpeed = _enemyMinSpeed *2;   // TODO: check naming convention
    private int _speed = _enemyMinSpeed; // TODO: Check name - is it just vel?
    private boolean _sighted = true;

    Enemy() {
        super();
        int resID = R.drawable.rocket_1;
        switch (_game._rng.nextInt(4)){ // TODO: Adjust speed here
            case 0:
                resID = R.drawable.rocket_1;
                _enemyMinSpeed *= 2;   // TODO: Check name - is it just vel?
                _enemyMaxSpeed /= 2;
                break;
            case 1:
                resID = R.drawable.rocket_2;
                _sighted=false;
                break;
            case 2:
                resID = R.drawable.rocket_3;
                break;
            case 3:
                resID = R.drawable.rocket_4;
                break;
        }

        _currentMaxSpeed = _enemyMinSpeed *2;

        loadBitmap(resID, ENEMY_HEIGHT);
        respawn();
    }

    @Override
    void respawn(){
        _currentMaxSpeed = _enemyMinSpeed *2;
        setRandomPosition();
    }


    @Override
    void update(){ // TODO: Sligth y-offset
        _velX = - (_game._playerSpeed + _speed); // TODO: Magic value? Should be varied
        _x += _velX;

        if(!_sighted){
            _velY= (float)(Math.sin(Math.toRadians(_velX*5)));    // TODO? Value?
            _y -= _velY;
            _y = Utils.clamp(_y, 0, Game.STAGE_HEIGHT - _height);
        }

        if(right()<0){
            accelerate();
            setRandomPosition();
        }
    }

    private void setRandomPosition(){
        _x = Game.STAGE_WIDTH + _game._rng.nextInt(ENEMY_SPAWN_OFFSET);
        _y = _game._rng.nextInt(Game.STAGE_HEIGHT- ENEMY_HEIGHT);
    }

    private void accelerate(){
        _speed = _game._rng.nextInt(_currentMaxSpeed - _enemyMinSpeed) + _enemyMinSpeed; // TODO: Kanske kommentera
        if(_speed > _currentMaxSpeed /2 && _currentMaxSpeed < _enemyMaxSpeed){ // TODO: Se över?
         _currentMaxSpeed++;
        }
    }

    @Override
    void onCollision(final Entity that){
        setRandomPosition();
    }
}
