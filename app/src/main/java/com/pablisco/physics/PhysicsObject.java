package com.pablisco.physics;

import android.graphics.Canvas;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

/**
 * Created by pablisco on 26/01/15.
 *
 * Description of the minimum actions an object requires to be used with the {@link PhysicsEngine}
 *
 */
public interface PhysicsObject {

	/**
	 *  Used to determine if the size of this object is dependent of the size of the world
	 * @return True if the object requires updates when {@link PhysicsEngine} is provided with a size
	 */
	boolean requiresRelative();

	/**
	 * Used to provide a size to the object. Calling this should make
	 * {@link #requiresRelative()} always return false;
	 * @param width width of the world
	 * @param height height of the world
	 */
	void setupRelative(int width, int height);

	/**
	 * This will draw the content into the provided canvas.
	 * <p/>
	 * This method is iterated over in {@link PhysicsEngine#drawWorld(android.graphics.Canvas)} for
	 * each of the objects present in the world.
	 * TODO: Support this same method but with OpenGL
	 * @param canvas A place for use to paint
	 */
	void draw(Canvas canvas);

	/**
	 * This method will be called when the object is added to the {@link PhysicsEngine} but not yet
	 * ready. It'll wait for next step to be created. This is used to provide a reference to the
	 * engine if needed.
	 * @param engine a reference to the engine.
	 */
	void onAttach(PhysicsEngine engine);

	/**
	 * Used when the object is created. In here the object is responsible to create it's
	 * body in the world. This will involve calling the method {@link World#createBody(BodyDef)}.
	 * More complex objects may require to call it multiple times.
	 * @param world Used to create the body of the object
	 */
	void onCreate(World world);

	/**
	 * This method is called when the object is been scheduled to be removed. It's the responsibility
	 * of the object to call {@link World#destroyBody(Body)}. Complex object must remember to
	 * remove all the bodies from teh world.
	 *
	 * @param world Used to destroy the object
	 */
	void onDestroy(World world);
}
