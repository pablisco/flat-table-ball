package com.pablisco.physics.objects;

import com.pablisco.physics.BasePhysicsObject;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.FixtureDef;

/**
 * Created by pablisco on 27/01/2015.
 *
 * This type is the base point for all the simple shapes available.
 *
 */
public abstract class ShapeObject<S extends ShapeObject<S>> extends BasePhysicsObject<S> {

	/**
	 * Suntypes must provide a shape to draw
	 * @return the shape to be used in the physical emulation
	 */
	protected abstract Shape createShape();

	/**
	 * We override this method because we need to attach the shope to our ficture
	 * @return a definition of a fixture
	 */
	@Override
	protected FixtureDef defineFixture() {
		FixtureDef definition = super.defineFixture();
		definition.shape = createShape();
		return definition;
	}
}
