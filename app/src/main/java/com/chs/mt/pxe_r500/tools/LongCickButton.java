package com.chs.mt.pxe_r500.tools;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

@SuppressLint("ClickableViewAccessibility") public class LongCickButton extends Button{
	private boolean clickdown = false;
	private LongTouchListener mListener;
	private int mtime;
	
	public LongCickButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//clickdown = true;
			
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			clickdown = false;
		}
		return super.onTouchEvent(event);
	}
	
	public void setStart(){
		clickdown = true;
		new LongTouchTask().execute();
		System.out.println("BUG setStart clickdown="+clickdown);
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class  LongTouchTask extends AsyncTask<Void, Integer, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			while(clickdown){				
				sleep(mtime);
				publishProgress(0);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mListener.onLongTouch();
		}
		
	}
	
	public void setOnLongTouchListener(LongTouchListener listener, int time) {
		mListener = listener;
		mtime = time;
		
	}

	public interface LongTouchListener {
		void onLongTouch();
	}
	
}
