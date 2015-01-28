package com.pablisco.physics.objects;

import android.graphics.Canvas;

/**
 * Created by pablisco on 28/01/2015.
 */
public abstract class AbstractBox<S extends AbstractBox<S>> extends ShapeObject<S> {

	protected float top, left, right, bottom;

	protected float width, height;

	private boolean requiresRelative = false;

	public AbstractBox(float width, float height) {
		this.width = width;
		this.height = height;
		top = height * -0.5f;
		bottom = height * 0.5f;
		left = width * -0.5f;
		right = width * 0.5f;
	}

	/**
	 * Used to determine if the size provided is the final one or the relative one
	 * @return
	 */
	public S relative() {
		requiresRelative = true;
		return self();
	}

	@Override
	public boolean requiresRelative() {
		return super.requiresRelative() || requiresRelative;
	}

	@Override
	public void setupRelative(int width, int height) {
		super.setupRelative(width, height);
		if (requiresRelative) {
			this.width = width * this.width;
			this.height = height * this.height;
			requiresRelative = false;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(left, top, right, bottom, getPaint());
	}

}
