package com.pablisco.physics;

import android.graphics.Canvas;

import com.pablisco.threads.FrameThread;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.setThreadPriority;

/**
 * Created by pablisco on 22/01/2015.
 *
 * Control center for the physics that happen with the jbox2d library.
 * <p/>
 * It starts a new thread when calling {@link #start()}. It is safe to add objects before or
 * after the engine has been started.
 *
 */
public class PhysicsEngine {

	/**
	 * For logging
	 */
	private static final String TAG = PhysicsEngine.class.getSimpleName();

	/**
	 * World used to create and destroy objects and do physics. It has a default gravity of 9.8
	 * (Earth's)
	 */
	private final World world = new World(new Vec2(0, 9.8f));

	private int velocityInteractions = 6;
	private int positionInteractions = 6;

	/**
	 * Frame rate used with the internal frame thread. This is provided in the constructor.
	 * Maybe in the future this will be able to change, for now we have it as a final field
	 */
	private final int frameRate;

	private Queue<PhysicsObject> objectsToAdd = new ConcurrentLinkedQueue<>();

	private Queue<PhysicsObject> objectsToRemove = new ConcurrentLinkedQueue<>();

	private List<PhysicsObject> victims = new CopyOnWriteArrayList<>();

	/**
	 * @see {@link #setWorldSize(int, int)}
	 * @see {@link #add(PhysicsObject)}
	 */
	private Queue<PhysicsObject> relativeObjects = new ConcurrentLinkedQueue<>();

	/**
	 * Width available
	 */
	private int width;

	/**
	 * Height available
	 */
	private int height;

	/**
	 * Internal thread used to step the world on each frame.
	 * TODO: Find a way to avoid frames from being lost (at the moment they are skipped)
	 */
	private FrameThread stepThread = new FrameThread() {

		@Override
		protected void onStart(FrameThread self) {
			Thread.currentThread().setName("Physics Thread");
			setThreadPriority(THREAD_PRIORITY_BACKGROUND);
			// our world will need some rest
			world.setAllowSleep(true);
		}

		@Override
		protected void frame(float frameTime) {
			createPendingObjects();
			deleteStaleObjects();
			world.step(frameTime, velocityInteractions, positionInteractions);
		}

		/**
		 * Here we iterate over all the objects registered for deletion and ask them to remove
		 * themselves.
		 */
		private void deleteStaleObjects() {
			while (!objectsToAdd.isEmpty()) {
				final PhysicsObject victim = objectsToAdd.poll();
				victim.onDestroy(world);
				victims.remove(victim);
			}
		}

		/**
		 * Here we iterate over all the objects registered for creation and ask them to create
		 * themselves.
		 */
		private void createPendingObjects() {
			while (!objectsToAdd.isEmpty()) {
				final PhysicsObject victim = objectsToAdd.poll();
				victim.onCreate(world);
				victims.add(victim);
			}
		}

	};

	/**
	 * Main constructor
	 * @param frameRate
	 */
	public PhysicsEngine(int frameRate) {
		this.frameRate = frameRate;
	}

	/**
	 * Start your engines! This will initiate the internal frame thread
	 */
	public void start() {
		stepThread.start(frameRate);
	}

	/**
	 * Ok, time to stop. It will stop the internal frame thread
	 */
	public void stop() {
		stepThread.stop();
	}

	/**
	 * Anybody with canvas can request to paint the world into themselves.
	 * This will iterate over each of the attached objects and paint them.
	 * @param canvas a place to paint
	 */
	public void drawWorld(Canvas canvas) {
		for (PhysicsObject victim : victims) {
			victim.draw(canvas);
		}
	}

	/**
	 * This MUST be used if any of the objects requires relative positioning (and sizing in the
	 * future)
	 *
	 * @param width available width
	 * @param height available height
	 * @return self
	 */
	public PhysicsEngine setWorldSize(int width, int height) {
		this.width = width;
		this.height = height;
		while(!relativeObjects.isEmpty()) {
			final PhysicsObject object = relativeObjects.poll();
			object.setupRelative(width, height);
			add(object);
		}
		return this;
	}

	/**
	 * Schedules objects to be added to the {@link #world}
	 * <p/>
	 * If the
	 * @param victim object to be added
	 * @return self
	 */
	public PhysicsEngine add(PhysicsObject victim) {
		victim.onAttach(this);
		// we may already have a size. This should make the object not require relative
		// updates in the following step
		if (width + height > 0) {
			victim.setupRelative(width, height);
		}
		if (victim.requiresRelative()) {
			relativeObjects.add(victim);
		} else {
			objectsToAdd.add(victim);
		}
		return this;
	}

	/**
	 * Schedules objects to be removed from the {@link #world}
	 *
	 * @param object the object to be removed
	 * @return self
	 */
	public PhysicsEngine remove(PhysicsObject object) {
		objectsToRemove.add(object);
		return this;
	}

}
