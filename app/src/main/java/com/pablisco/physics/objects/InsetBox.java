package com.pablisco.physics.objects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.pablisco.physics.Vecs;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

/**
 * Created by pablisco on 28/01/2015.
 */
public class InsetBox extends AbstractBox<InsetBox> {

	public InsetBox(float width, float height) {
		super(width, height);
		getPaint().setStyle(Paint.Style.STROKE);
	}

	@Override
	protected Shape createShape() {
		ChainShape shape = new ChainShape();
		Vec2[] vertices = Vecs.vec2Array(
			left, top,
			right, top,
			right, bottom,
			left, bottom,
			left, top
		);
		shape.createChain(vertices, vertices.length);
		return shape;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

	}
}
