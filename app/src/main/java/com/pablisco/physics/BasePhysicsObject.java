package com.pablisco.physics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 * Created by pablisco on 23/01/2015.
 *
 * Abstract implementation of the {@link PhysicsObject} type. It provide common functionality
 * such as position, paint and visibility.
 *
 * @param <S> Used for the builder pattern (method chaining)
 *
 */
public abstract class BasePhysicsObject<S extends BasePhysicsObject<S>> implements PhysicsObject {

	private Paint paint = new Paint();
	private boolean visible = true;

	private Vec2 position = new Vec2(0, 0);
	private float rotation = 0f;

	protected Body body;

	private float density = 0.8f;
	private float friction = 0.7f;
	private float restitution = 0.8f;

	private BodyType bodyType = BodyType.DYNAMIC;

	private Vec2 relativePosition;

	{
		paint.setColor(Color.WHITE);
	}

	/* setters with a builder pattern */

	public S setColor(int color) {
		paint.setColor(color);
		return self();
	}

	public S setVisible(boolean visible) {
		this.visible = visible;
		return self();
	}

	public S setDensity(float density) {
		this.density = density;
		return self();
	}

	public S setFriction(float friction) {
		this.friction = friction;
		return self();
	}

	public S setRestitution(float restitution) {
		this.restitution = restitution;
		return self();
	}

	public S setPosition(Vec2 position) {
		this.position = position;
		return self();
	}

	public S setType(BodyType type) {
		this.bodyType = type;
		return self();
	}

	public S setPosition(float x, float y) {
		return setPosition(new Vec2(x, y));
	}

	/**
	 * Used to provide a relative position. This will be calculated when the object's body is created.
	 * Any parameters outside of the range [0.0, 1.0] will have unpredicted situations, although it
	 * can be used to hide objects or to bring them into the scene.
	 * @param x horizontal position form 0.0 to 1.0
	 * @param y vertical position from 0.0 to 1.0
	 * @return self
	 */
	public S setRelativePosition(float x, float y) {
		relativePosition = new Vec2(x, y);
		return self();
	}

	@Override
	public boolean requiresRelative() {
		// instead of a flag we can check if the object requires size updates from the physics engine
		return relativePosition != null;
	}

	@Override
	public void setupRelative(int width, int height) {
		if (body != null) {
			// the body must be created after the position is calculated
			throw new IllegalStateException("body was initiated before position calculated");
		}
		if (relativePosition != null) {
			// apply position and invalidate the relative position so that requiresRelative()
			// returns false after
			float x = width * relativePosition.x;
			float y = height * relativePosition.y;
			setPosition(x, y);
			relativePosition = null;
		}
	}

	/**
	 * This is used by implementations of this type. This class will take care of position and rotation
	 * @param canvas a place to plait
	 */
	protected void onDraw(Canvas canvas) {
		// no op
	}

	/**
	 * This implementation will gather the position of the body and
	 * @param canvas A place for use to paint
	 */
	@Override
	public final void draw(Canvas canvas) {
		if (visible) {
			position = MeasurementUtil.metersToPixels(body.getPosition());
			rotation = body.getAngle();
			Log.i(this.getClass().getSimpleName(), "draw(canvas)" + position);
			canvas.save();
			// move the canvas to the object location
			canvas.translate(position.x, position.y);
			// rotate the canvas
			canvas.rotate(rotation);
			// delegate to children
			onDraw(canvas);
			// back to normal
			canvas.restore();
		}
	}

	/**
	 * @return and instance of the current body for external use
	 */
	public Body getBody() {
		return body;
	}

	@Override
	public void onAttach(PhysicsEngine engine) {
		// no op
	}

	@Override
	public void onCreate(World world) {
		body = world.createBody(createBodyDefinition());
		onBodyCreation(body);
	}

	@Override
	public void onDestroy(World world) {
		if (body != null) {
			world.destroyBody(body);
		}
	}

	public BodyDef createBodyDefinition() {
		BodyDef result =  new BodyDef();
		result.type = bodyType;
		result.active = true;
		result.position = MeasurementUtil.pixelToMeters(position);
		return result;
	}

	/**
	 * This method is called when the object is created so we can manipulate it.
	 * If overwritten the caller has to call this method to ensure the right fixture is used unless
	 * it creates one of it's own
	 * @param body of the object
	 */
	public void onBodyCreation(Body body) {
		this.body = body;
		// Create fixture definition
		FixtureDef fixtureDef = defineFixture();
		// Attach the ficture
		body.createFixture(fixtureDef);
	}

	/**
	 * This method creates a simple fixture given the provided density, friction and restitution.
	 * @return a Fixture definition
	 */
	protected FixtureDef defineFixture() {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		return fixtureDef;
	}

	/**
	 * This method is used to enable "extendable" builder patterns
	 * @return self
	 */
	@SuppressWarnings("unchecked")
	protected S self() {
		return (S)this;
	}

	/**
	 * This method can be used by implementations of this type in order to change the type of paint
	 * to be used. This is not used locally here but serves as a simple way to provide with a paint
	 * that we can modify if needed.
	 * @return the paint
	 */
	protected Paint getPaint() {
		return paint;
	}

}
