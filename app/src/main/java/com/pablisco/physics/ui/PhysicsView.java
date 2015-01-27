package com.pablisco.physics.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.TextureView;

import com.pablisco.physics.PhysicsEngine;
import com.pablisco.threads.FrameThread;

import static android.os.Process.THREAD_PRIORITY_DISPLAY;
import static android.os.Process.setThreadPriority;

/**
 * Created by pablisco on 24/01/2015.
 *
 * Canvas base client for {@link PhysicsEngine}. {@link TextureView} was chosen as a super class
 * because it allows us to paint to the canvas without blocking the UI thread. Which is ideal for
 * frequent intervals of painting. Ideally this could be implemented using OpenGL but for a small
 * amount of objects this should be enough to handle it.
 *
 */
public class PhysicsView extends TextureView {

	private PhysicsEngine engine = new PhysicsEngine();

	public PhysicsView(Context context) {
		super(context);
	}

	public PhysicsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PhysicsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * Internal frame used to request painting
	 */
	private FrameThread renderThread = new FrameThread() {

		@Override
		protected void onStart(FrameThread self) {
			Thread.currentThread().setName("Render Thread");
			setThreadPriority(THREAD_PRIORITY_DISPLAY);
		}

		@Override
		protected void frame(float frameTime) {
			if (isAvailable()) {
				// if the view is ready we draw the world in this thread and then post to the UI thread
				final Canvas canvas = lockCanvas(null);
				// clear canvas
				canvas.drawColor(Color.BLACK);
				// draw a nice picture of our world
				engine.drawWorld(canvas);
				// unlock and post the picture to the main thread
				unlockCanvasAndPost(canvas);
			}
		}
	};

	/**
	 * Used to access the view's {@link PhysicsEngine} by an external entity (i.e. Activity or Fragment)
	 * @return The internal engine
	 */
	public PhysicsEngine getPhysicsEngine() {
		return engine;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		renderThread.start();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		renderThread.stop();
	}

}
