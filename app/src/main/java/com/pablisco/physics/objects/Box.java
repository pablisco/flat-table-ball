package com.pablisco.physics.objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

import static com.pablisco.physics.MeasurementUtil.pixelToMeters;

/**
 * Created by pablisco on 28/01/2015.
 */
public class Box extends AbstractBox<Box> {

	public Box(float width, float height) {
		super(width, height);
	}

	@Override
	protected Shape createShape() {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(pixelToMeters(width), pixelToMeters(height));
		return shape;
	}

}
