package edu.cs4730.alieninvaders_kt;


import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * This is the class for the Ship.
 */

public class objShip extends obj {

  private Bitmap shipBmp;

  objShip(int tx, int ty, int w, int wid, int hei, Bitmap pic) {
    super(tx, ty, w, wid, hei);
    shipBmp =  pic;

  }

  @Override
  void draw(Canvas c) {
    c.drawBitmap(shipBmp, x, y, null);
  }


}
