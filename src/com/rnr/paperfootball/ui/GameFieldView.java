package com.rnr.paperfootball.ui;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.rnr.paperfootball.base.BaseMap;
import com.rnr.paperfootball.base.BaseMapController;
import com.rnr.paperfootball.core.GameCallback;
import com.rnr.paperfootball.core.TouchHandler;

public class GameFieldView extends SurfaceView implements SurfaceHolder.Callback, GameCallback,
		OnTouchListener {
	private BaseMapController mPainter;
	private boolean mSurfaceCreated;
	private Vector<TouchHandler> mTouchHandler;

	public void addHandler(TouchHandler handler) {
		this.mTouchHandler.add(handler);
	}

	public void removeHandler(TouchHandler handler) {
		this.mTouchHandler.remove(handler);
	}

	private void paintField(SurfaceHolder surfaceHolder) {
		if (!this.mSurfaceCreated) {
			return;
		}
        Canvas C = surfaceHolder.lockCanvas();

        try {
    		Picture picture = new Picture();

    		Canvas canvas = picture.beginRecording(500, 500);
        	this.mPainter.draw(canvas);
    		picture.draw(C);
        } finally{
        	surfaceHolder.unlockCanvasAndPost(C);
        }
	}

	protected void initView() {
		this.setOnTouchListener(this);
		this.mSurfaceCreated = false;
		this.mTouchHandler = new Vector<TouchHandler>();
		this.getHolder().addCallback(this);
	}

	public GameFieldView(Context context) {
		super(context);
		this.initView();
	}

	public GameFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView();
	}

	public GameFieldView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initView();
	}

	public void setMapPainter(BaseMapController mapPainter) {
		this.mPainter = mapPainter;
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int arg1, int arg2, int arg3) {
		this.paintField(surfaceHolder);
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		this.mSurfaceCreated = true;
		this.paintField(surfaceHolder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		this.mSurfaceCreated = false;
	}

	@Override
	public void repaint(BaseMap map) {
		this.paintField(this.getHolder());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		for (TouchHandler handler : this.mTouchHandler) {
			handler.setPoint(this.mPainter, event.getX(), event.getY());
		}
		return false;
	}
}