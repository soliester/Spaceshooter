package com.example.spaceshooter;

public class SpecialStars extends BitmapEntity {
    private final static int FRIEND_HEIGHT = 80;
    private final static int FRIEND_SPAWN_OFFSET = Game.STAGE_WIDTH;

    SpecialStars() {
        super();
        int resID = R.drawable.star_detail;
        loadBitmap(resID, FRIEND_HEIGHT);
        respawn();
    }


    @Override
    void respawn(){
        _x = Game.STAGE_WIDTH + _game._rng.nextInt(FRIEND_SPAWN_OFFSET);
        _y = _game._rng.nextInt(Game.STAGE_HEIGHT- FRIEND_HEIGHT);
    }


    @Override
    void update(){
        _velX = -_game._playerSpeed; // TODO: Magic value? Should be varied
        _x += _velX;
        if(right()<0){
            _x = Game.STAGE_WIDTH + _game._rng.nextInt(FRIEND_SPAWN_OFFSET);
        }
    }

    @Override
    void onCollision(final Entity that){
        respawn();
        }
}
