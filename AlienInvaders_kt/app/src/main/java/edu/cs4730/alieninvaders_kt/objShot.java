package edu.cs4730.alieninvaders_kt;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 *
 */

public class objShot extends obj {

  private Bitmap shotBmp;

  objShot(int tx, int ty, int w, int wid, int hei, Bitmap pic) {
    super(tx, ty, w, wid, hei);
    shotBmp = pic;
  }

  @Override
  void draw(Canvas c) {
    c.drawBitmap(shotBmp, x, y, null);
  }

  void move(int ty, int shotmove, float top) {
    if (y - shotmove > top) {
      super.move(0, ty);
    } else {
      //declare it dead.
      dead();
    }
  }

}
