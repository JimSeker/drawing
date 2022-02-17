package edu.cs4730.alieninvaders_kt;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * simple abstract class to hold the basics for the "objs"
 */

public abstract class obj {
    int x, y;
    boolean alive;
    Rect rec;

    obj() {
        x = 0;
        y = 0;
        alive = false;
    }

    obj(int tx, int ty, int w, int wid, int hei) {
        x = tx;
        y = ty;
        rec = new Rect(x, y, x + wid, y + hei);
        alive = true;
    }

    abstract void draw(Canvas c);

    void move(int tx, int ty) {
        x += tx;
        y += ty;
        rec.offsetTo(x, y);
    }

    public void set(int tx, int ty) {
        x = tx;
        y = ty;
        rec.offsetTo(x, y);
    }

    void dead() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    boolean collision(Rect them) {
        return rec.intersects(them.left, them.top, them.right, them.bottom);
    }

    int score() {
        return 0;
    }
}
