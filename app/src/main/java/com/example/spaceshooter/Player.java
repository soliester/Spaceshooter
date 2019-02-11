package com.example.spaceshooter;

public class Player extends BitmapEntity {
    static final String TAG = "Player";
    private final static int PLAYER_HEIGHT = 100; // TODO: Gameplay value
    private final static int STARTING_POSITION = 40;
    private final static int STARTING_HEALTH = 4; // TODO: Hard coded magic value
    private final static float ACC = 1.1f; // TODO: Configure file all underneath
    private final static float MIN_VEL = 2f;
    private final static float MAX_VEL = 20f;
    private final static float GRAVITY = 1.5f;
    private final static float LIFT = -(GRAVITY * 2f);
    private final static float DRAG = 0.97f;

    int _health = STARTING_HEALTH;
    private int hitCountdown = 0;

    Player(){
        super();
        loadBitmap(R.drawable.player_ship, PLAYER_HEIGHT);
        respawn();
    }

    @Override
    void respawn(){
        _x = STARTING_POSITION;
        _health = STARTING_HEALTH;
        _velX=MIN_VEL;
        _velY=MIN_VEL;
    }

    @Override
    void update() {
        _velX *= DRAG;
        _velY += GRAVITY;
        if (_game._isBoosting) {
            _velX *= ACC;
            _velY += LIFT;
        }
        _velX = Utils.clamp(_velX, MIN_VEL, MAX_VEL);
        _velY = Utils.clamp(_velY, -MAX_VEL, MAX_VEL);
        _y += _velY;
        _y = Utils.clamp(_y, 0, Game.STAGE_HEIGHT - _height);
        _game._playerSpeed=_velX;

        if(hitCountdown>0){
            hitCountdown--;
        }
        else if (hitCountdown==0){
            loadBitmap(R.drawable.player_ship, PLAYER_HEIGHT);      //TODO: Ska inte bytas här?
            hitCountdown--;
        }
        // TODO: Experiment
    }

    @Override
    void onCollision(Entity that) {
        //TODO: Implement recovery frames (temporary immortality after taking damage)
        if(hitCountdown<=0){    // TODO: Se över logik
            _health--;
            loadBitmap(R.drawable.player_ship_hit, PLAYER_HEIGHT);
        }
        hitCountdown=20;
    }


}
