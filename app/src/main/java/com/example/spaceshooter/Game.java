package com.example.spaceshooter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class Game extends SurfaceView implements Runnable {
    public static final String TAG = "Game";
    public static final String PREFS = "com.example.spaceshooter";
    public static final String HIGH_SCORE = "highest_score";
    static final int STAGE_WIDTH = 1280; // TODO: Move game settings to resources
    static final int STAGE_HEIGHT = 720; // TODO: Change hard code value
    static final int STAR_COUNT = 40;
    static final int ENEMY_COUNT = 4;
    static final int FRIEND_COUNT = 2;

    private Thread _gameThread = null;
    private volatile boolean _isRunning = false;
    private SurfaceHolder _holder = null;
    private Paint _paint = new Paint();
    private Canvas _canvas = null; // TODO: Check error, only null atm
    private Bullet _bullet = null;

    private ArrayList<Entity> _entities = new ArrayList<>();

    private Player _player = null;
    final Random _rng = new Random();

    volatile boolean _isBoosting = false; // Volatile not necessary? But across thread values
    float _playerSpeed = 0f;
    int _starsCatched = 0;
    int _maxStartCatched = 0;
    private boolean _gameOver = false;
    static Jukebox _jukebox = null; // TODO: Se över typ
    private SharedPreferences _prefs = null;
    private SharedPreferences.Editor _editor = null;

    public Game(Context context) {
        super(context);
        Entity._game = this;
        _holder = getHolder();
        _holder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT); // TODO: Control resolution, not necessary
        _jukebox = new Jukebox(context);

        _prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        _editor = _prefs.edit();

        for (int i = 0; i < STAR_COUNT; i++) {
            _entities.add(new Star());
        }
        for (int i = 0; i < ENEMY_COUNT; i++) {
            _entities.add(new Enemy());
        }

        for (int i = 0; i < FRIEND_COUNT; i++) {
            _entities.add(new SpecialStars());
        }
        _player = new Player();
        _bullet = new Bullet(_player);
        restart();
    }

    private void restart() {
        for (Entity e : _entities) {
            e.respawn();
        }
        _player.respawn();
        _starsCatched = 0;
        _maxStartCatched = _prefs.getInt(HIGH_SCORE, 0);
        _gameOver = false;
        Game._jukebox.play(Jukebox.START); // TODO: Does not work

        // TODO: Sound effect for restart
    }

    @Override
    public void run() {
        while (_isRunning) {
            update();
            render();
        }
    }

    private void update() {
        if (_gameOver) {
            return;
        }
        _player.update();
        _bullet.update();
        for (Entity e : _entities) {
            e.update();
        }
        checkCollisions();
        checkGameOver();
    }

    private void checkGameOver() {
        if (_player._health <= 0) {
            _jukebox.play(Jukebox.GAMEOVER); // TODO: Felplacerad?
            _gameOver = true;
        }
        if (_starsCatched > _maxStartCatched) {
            _maxStartCatched = _starsCatched;
            _editor.putInt(HIGH_SCORE, _maxStartCatched);
            _editor.apply();
        }
    }

    private void checkCollisions() {
        Entity temp = null;
        for (int i = STAR_COUNT; i < _entities.size()-FRIEND_COUNT; i++) { // TODO: Change or describe logic
            temp = _entities.get(i);
            if (_player.isColliding(temp)) {
                _player.onCollision(temp);
                temp.onCollision(_player);
                _jukebox.play(Jukebox.CRASH);
            }
            if(_bullet.isColliding(temp)){  // TODO: Refactor duplicate code
                temp.onCollision(_bullet);
                _bullet.onCollision(temp);
                _jukebox.play(Jukebox.HIT);
            }
        }

        for (int i = _entities.size() - FRIEND_COUNT; i < _entities.size(); i++) {
            temp = _entities.get(i);
            if (_player.isColliding(temp)) {
                _jukebox.play(Jukebox.SCORE); // TODO: Does not work
                _starsCatched +=1;       // TODO: In _player?
                temp.onCollision(_player);
            }
        }
    }

    private void render() {
        if (!lockCanvas()) {
            return;
        }

        _canvas.drawColor(Color.rgb(10, 5, 30)); //Clear the screen
        for (Entity e : _entities) {
            e.render(_canvas, _paint);
        }

        _player.render(_canvas, _paint);
        _bullet.render(_canvas, _paint);
        renderHUD(_canvas, _paint);
        _holder.unlockCanvasAndPost(_canvas);
    }

    private void renderHUD(final Canvas canvas, final Paint paint) {
        final float textSize = 48f;
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(textSize);
        if (!_gameOver) {
            canvas.drawText("Health: " + _player._health, 10, textSize, paint); // TODO: Text in res?
            canvas.drawText("Score: " + _starsCatched, STAGE_WIDTH-STAGE_WIDTH/3f, textSize, paint); // TODO: Text in res?
            canvas.drawText("Highscore: " + _maxStartCatched, STAGE_WIDTH-STAGE_WIDTH/3f, textSize * 2, paint); // TODO: Text in res?

        } else {
            final float centerY = STAGE_HEIGHT / 2f;
            canvas.drawText("GAME OVER!", STAGE_WIDTH / 2f, centerY, paint); // TODO: Text in res?
            canvas.drawText("(press to restart)", STAGE_WIDTH / 2f, centerY + textSize, paint); // TODO: Text in res?
        }
    }

    private boolean lockCanvas() {
        if (!_holder.getSurface().isValid()) {
            return false;
        }
        _canvas = _holder.lockCanvas();
        return (_canvas != null);
    }

    protected void onResume() {
        Log.d(TAG, "onResume");
        _isRunning = true;
        _gameThread = new Thread(this);
        _gameThread.start();
    }

    protected void onPause() {
        Log.d(TAG, "onPause");
        _isRunning = false;
        try {
            _gameThread.join();
        } catch (InterruptedException e) {
            Log.d(TAG, Log.getStackTraceString(e.getCause()));
        }
    }

    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        _gameThread = null;
        for (Entity e : _entities) {
            e.destroy();
        }
        Entity._game = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP: // Finger lifted
                _isBoosting = false;
                if (_gameOver) {
                    restart();
                }
                break;
            case MotionEvent.ACTION_DOWN: // Finger presser
                _isBoosting = true;
                break;
        }
        return true;
    }
}
