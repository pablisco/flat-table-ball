package com.pablisco.physics.objects;

import android.graphics.Canvas;

import com.pablisco.physics.PhysicsEngine;
import com.pablisco.physics.PhysicsObject;

import org.jbox2d.dynamics.World;

import java.util.List;

/**
 * Created by pablisco on 28/01/2015.
 *
 * Base work for compounding objects
 *
 */
public abstract class CompoundObject implements PhysicsObject {

	private List<PhysicsObject> children = createChildren();

	protected abstract List<PhysicsObject> createChildren();

	@Override
	public boolean requiresRelative() {
		boolean result = false;
		for (PhysicsObject child : children) {
			if(child.requiresRelative()) {
				result = true;;
				break;
			}
		}
		return result;
	}

	@Override
	public void setupRelative(int width, int height) {
		for (PhysicsObject child : children) {
			child.setupRelative(width, height);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		for (PhysicsObject child : children) {
			child.draw(canvas);
		}
	}

	@Override
	public void onAttach(PhysicsEngine engine) {
		for (PhysicsObject child : children) {
			child.onAttach(engine);
		}
	}

	@Override
	public void onCreate(World world) {
		for (PhysicsObject child : children) {
			child.onCreate(world);
		}
	}

	@Override
	public void onDestroy(World world) {
		for (PhysicsObject child : children) {
			child.onDestroy(world);
		}
	}

}
