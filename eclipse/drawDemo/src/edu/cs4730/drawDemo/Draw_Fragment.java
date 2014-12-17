package edu.cs4730.drawDemo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Draw_Fragment extends Fragment implements Runnable {

	ImageView theboardfield;
	Bitmap theboard;
	Canvas theboardc;
	Button btnClear,btnAClear,btnNColor;
	final int boardsize = 480;
	boolean isAnimation = false;
	protected Handler handler;
	//for drawing
	Rect myRec;
	Paint myColor;
	ColorList myColorList = new ColorList();
	Bitmap alien;
	//for the thread
	Thread myThread;
	
	
	public Draw_Fragment() {
		// Required empty public constructor
		
        //required if the fragment is adding menu items, otherwise it calls the menu methods.
        setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View myView = inflater.inflate(R.layout.draw_fragment, container, false);
		
		

		//Animated clear button, will start a thread to clear the image to white
		btnAClear = (Button)  myView.findViewById(R.id.button1);
		btnAClear.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View v) {
				startThread();  //done to simply a few things.
			}
		});
		
		//Simple clear button, reset the image to white.
		btnClear = (Button) myView.findViewById(R.id.button2);
		btnClear.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View v) {
				theboardc.drawColor(Color.WHITE);  //background color for the board.
				drawBmp();
			}
		});		
		
		//changes to the next color in the list
		btnNColor = (Button)  myView.findViewById(R.id.button3);
		btnNColor.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View v) {
				myColorList.next();
				myColor.setColor(myColorList.getNum());
			}
		});		
		
		
		
		//get the imageview and create a bitmap to put in the imageview.
		//also create the canvas to draw on.
		theboardfield = (ImageView)  myView.findViewById(R.id.boardfield);
		theboard = Bitmap.createBitmap(boardsize, boardsize, Bitmap.Config.ARGB_8888);
		theboardc = new Canvas(theboard);
		theboardc.drawColor(Color.WHITE);  //background color for the board.
		theboardfield.setImageBitmap(theboard);
		theboardfield.setOnTouchListener(new myTouchListener());

		//For drawing
		myRec = new Rect(0,0,10,10);
		myColor = new Paint();  //default black
		myColor.setColor(myColorList.getNum());
		myColor.setStyle(Paint.Style.FILL);
			
		//load a picture and draw it onto the screen.
		alien = BitmapFactory.decodeResource(getResources(), R.drawable.alien);
		//draw it on the screen.
		theboardc.drawBitmap(alien,null,new Rect (0,0,300,300),myColor);
		//message handler for the animation.
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) { //redraw image
					if (theboard != null && theboardfield != null) {
						drawBmp();
					}
				} 
			}
		};
		
		return myView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_frag, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.loadimage:
			bmpload();
			return true;
		case R.id.saveimage:
			bmpsave();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * simple method to set the board in the imageveiw and then cause it to redraw.
	 */
	void drawBmp() {
		theboardfield.setImageBitmap(theboard);
		theboardfield.invalidate();
	}
	/*
	 * Starts up a new thread to run some animation.
	 */
	public void startThread() {
		isAnimation = true;
		//new Thread(this).start();
		myThread = new Thread(this);
		myThread.start();
	}

	@Override
	public void run() {
		boolean done = false;
		int block = boardsize /10;
		int x = 0, i =0;
		//first draw a red down the board
		//myColor.setColor(Color.RED);//use current color instead of red.
		while (!done) {
			if (!isAnimation) {return;}  //To stop the thread, if the system is paused or killed.
			//clear part of the board
			/*
			for (i=0; i<=block; i++) {
			  theboardc.drawLine(0, x+i, boardsize, x+i, myColor);
			}
			x += block;
			*/
			 theboardc.drawLine(0,x,boardsize,x,myColor);
			 x++;
			 
			//send message to redraw
			handler.sendEmptyMessage(0);
			//now wait a little bit.
			try {
				Thread.sleep(10);  //change to 100 for a commented out block, instead of just a line.
			} catch (InterruptedException e) {
				; //don't care
			}
			//determine if we are done or move the x?
			if (x >= boardsize) done = true;
		}
		//now draw white back up the board
		x = boardsize - block;
		done = false;
		myColor.setColor(Color.WHITE);  
		while (!done) {
			if (!isAnimation) {return;}  //To stop the thread, if the system is paused or killed.
			//clear part of the board
			for (i=0; i<=block; i++) {
			  theboardc.drawLine(0, x+i, boardsize, x+i, myColor);
			}
			x -= block;
			//send message to redraw
			handler.sendEmptyMessage(0);
			//now wait a little bit.
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				; //don't care
			}
			//determine if we are done or move the x?
			if (x <0) done = true;
		}
		//now draw white back up the board
		
		myColor.setColor(myColorList.getNum());
		isAnimation = false;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	public void onPause() {
		finish();
		super.onPause();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	public void finish() {
		//first make sure the thread is stopped, to prevent a force close.
		if (myThread != null) {
			isAnimation = false;
			try {
				if (myThread.isAlive()) {
				  myThread.join();
				}
			} catch (InterruptedException e) {
				; //don't care.
			}
			myThread = null;
		}
		theboard = null;
		theboardc = null;
		myColor = null;
		handler = null;
//		super.finish();
	}
	
	/*
	 * TouchListener will draw a square on the image where "touched".
	 * If doing an animated clear, it will return without doing anything.
	 */
    class myTouchListener implements View.OnTouchListener {
    	@Override  	
    	public boolean onTouch(View v, MotionEvent event) {
    		
    		//Don't draw if there is animation going on.
    		if (isAnimation) return true;  //but say we handled (ignored) the event.
    		
    	    //We just need the x and y position, to draw on the canvas
    		//so, retrieve the new x and y touch positions
    		int x = (int) event.getX();
    		int y = (int) event.getY();
    		//now draw on our canvas
    		myRec.set(x,y,x+10,y+10);
    		theboardc.drawRect(myRec, myColor);
    		drawBmp();
    		return true;
    	}
    };
    
    
    /*
     * If that is a stored file called, DrawDemo.png, then it will load it into the imageview.
     */
    void bmpload() {
    	
    	DataInputStream in;
    	//file is located in the private data section of the app, instead of on the sdcard.
    	String filename = "DrawDemo.png";
        Bitmap bmp = null;
        
		try {
			in = new DataInputStream( getActivity().openFileInput(filename) ) ;
			bmp = BitmapFactory.decodeStream(in);
			//theboard = bmp;  can't draw on screen anymore...!
			theboardc.drawBitmap(bmp, 0, 0, null);
			drawBmp();
		} catch (FileNotFoundException e) {
			Log.i("bmpload", "file not found");
		} 

    }
    
    
    /*
     * Save the image drawn in a file called DrawDemo.png
     */
    void bmpsave() {
    	String filename = "DrawDemo.png";
		DataOutputStream dos;
		//store the image in the local data directory.
		try {
			dos = new DataOutputStream(getActivity().openFileOutput(filename, Context.MODE_PRIVATE));
    		
    	    // FileOutputStream out = new FileOutputStream();
    	     if (theboard.compress(Bitmap.CompressFormat.PNG, 90, dos) )
    	    	 Log.i("bmpsave", "It worked");
    	     else 
    	    	 Log.w("bmpsave", "Bmp save failed!");
    	     dos.flush();
    	     dos.close();
    	} catch (Exception e) {
    	     e.printStackTrace();
    	}

    }
	
}
