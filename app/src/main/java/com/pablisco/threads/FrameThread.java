package com.pablisco.threads;

import android.util.Log;

import static com.pablisco.flattableball.BuildConfig.DEBUG;
import static java.lang.System.currentTimeMillis;

/**
 * Created by pablisco on 25/01/15.
 *
 * Useful Thread (actually a Runnable but a thread at heart) used to simplify a step by step.
 * This can be used to do actions that require to be executed in a timely manner given a rate.
 * <p>
 * The default rate is 60 but it can be changes upon starting it using {@link #start(int)}
 * </p>
 *
 */
public abstract class FrameThread implements Runnable {

	/**
	 * Used for logging
	 */
	private static final String TAG = FrameThread.class.getSimpleName();

	/**
	 * Internal reference to the thread that runs this runnable
	 */
	private Thread thread = new Thread();

	/**
	 * How many milliseconds each frame has to wait for
	 */
	private long frameWait;

	/**
	 * The time in seconds (float) that each frame will take
	 */
	private float frameTime;

	/**
	 * Default rate used in {@link #start()}
	 */
	private static final int DEFAULT_FRAME_RATE = 60;

	/**
	 * This method has to be implemented by subclasses and is called 1/n of a second.
	 * With n being the provided frame rate.
	 * @param frameTime This provides the subclass the time it has to be run in seconds (1.0/n).
	 */
	protected abstract void frame(float frameTime);

	/**
	 * This optional method can be used to do actions in the thread when it gets started
	 * (i.e. Setting the name of the thread).
	 * @param self a reference to oneself
	 */
	protected void onStart(FrameThread self) {
		// no op
	}

	/**
	 * The main part of the thread. It'll run the {@link #frame(float)} method and then work out
	 * how much it need to sleep before next execution.
	 */
	@Override
	public final void run() {
		onStart(this);
		// we will run until the thread is stopped by being nullified
		while (thread != null) {
			long start = currentTimeMillis();
			frame(frameTime);
			long elapsed = currentTimeMillis() - start;
			// let's see if we overshoot on time
			if (elapsed > frameWait) {
				if (DEBUG) {
					Log.d(TAG, Thread.currentThread().getName() + " took too long: " + elapsed + "ms");
					int frameSkip = 0;
					// work out how many frames we may have missed and how much we run into this one
					while (elapsed > frameWait) {
						elapsed -= frameWait;
						frameSkip++;
					}
					Log.d(TAG, "Skipping " + frameSkip + " frames");
				} else {
					// faster method for production (no debugging)
					elapsed %= frameWait;
				}
			}
			try {
				// Sleep for how much longer we have left in the frame
				Thread.sleep(frameWait - elapsed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Main start point. It will create a new thread and
	 * @param frameRate used to calculate the time for each frame
	 */
	public void start(int frameRate) {
		// calculate the frame times
		frameWait = (1000) / frameRate;
		frameTime = frameWait / (1000.0f);
		if (DEBUG) {
			Log.d(TAG, "Starting frame thread");
			if (thread != null) {
				Log.w(TAG, "We have already a thread running");
			}
		}
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Alias of {@link #start(int)} with {@link #DEFAULT_FRAME_RATE} as parameter
	 */
	public void start() {
		start(DEFAULT_FRAME_RATE);
	}

	/**
	 * Used to stop the thread
	 */
	public void stop() {
		if (thread != null) {
			thread = null;
		}
	}

}
