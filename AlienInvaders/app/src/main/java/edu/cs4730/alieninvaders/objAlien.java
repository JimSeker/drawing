package edu.cs4730.alieninvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 *
 */

class objAlien extends obj {
  private Bitmap alien1Bmp, alien2Bmp;
  private int whichpic;
  private int tick, tick2, imgtick;
  private int dir;
  private Random myRandom;

  objAlien(int tx, int ty, int w, int wid, int hei, Bitmap pic1, Bitmap pic2) {
    super(tx, ty, w, wid, hei);
    alien1Bmp = pic1;
    alien2Bmp = pic2;
    imgtick=0;
    tick = 0;
    tick2 = 0;
    dir = 0;
    whichpic = 0;
    myRandom = new Random();
  }
  public void imgcnt() {
    imgtick++;
    if (imgtick >3) {
      imgtick =0;
      if (whichpic==0)
        whichpic=1;
      else
        whichpic=0;

    }
  }
  @Override
  void draw(Canvas c) {
    imgcnt();
    if (whichpic == 0)
      c.drawBitmap(alien1Bmp, x, y, null);
    else
      c.drawBitmap(alien2Bmp, x, y, null);
  }


  @Override
  int score() {
    return 100;
  }

  private boolean tickU(int tickMax) {
    tick++;
    if (tick > tickMax) {
      tick = 0;
      return true;
    }
    return false;
  }

  private boolean tick2U(int tickMax) {
    tick2++;
    if (tick2 > tickMax) {
      tick2 = 0;
      return true;
    }
    return false;
  }

  void move (int alienmove, float left, float right) {

    if (tickU(3)) { //yes move the alien   this keeps the alien from moving to fast.  likely to slow with scaleing.  fix? don't know.
      //new direction
      if (tick2U(30)) {  //choose a new direction
        dir = myRandom.nextInt(3) - 1;  //-1, 0, or +1  for direction.
      }
      if (x <= left || x >= right - alien1Bmp.getWidth()) { //don't move alien off edge of the board
        dir *= -1;
      }

      super.move(dir, +alienmove);   //alienmove is the 1* scale.
    }
  }

}
