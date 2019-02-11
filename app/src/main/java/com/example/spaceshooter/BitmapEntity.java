package com.example.spaceshooter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import static com.example.spaceshooter.Utils.scaleToTargetHeight;

abstract public class BitmapEntity extends Entity {
    protected Bitmap _bitmap = null;
    BitmapEntity(){ }

    protected void loadBitmap(int resId, int height){
        destroy();
        Bitmap temp = BitmapFactory.decodeResource(
                _game.getContext().getResources(),
                resId
        );

        _bitmap = scaleToTargetHeight(temp, height);
        temp.recycle();
        _width = _bitmap.getWidth(); // TODO: Assert not null (1.8 8:30), crash properly otherwise
        _height = _bitmap.getHeight(); // TODO: See above
    }

    @Override
    void render(Canvas canvas, Paint paint) {
        canvas.drawBitmap(_bitmap, _x, _y, paint);
    }

    @Override
    void destroy() {
        if (_bitmap != null) {  // TODO: Assertion instead of if
            _bitmap.recycle();
            _bitmap = null;
        }
    }
}
