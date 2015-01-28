package com.pablisco.physics;

import org.jbox2d.common.Vec2;

/**
 * Created by pablisco on 28/01/2015.
 *
 * Utility class for simple Vec initiation
 */
public class Vecs {

	/**
	 * Creates a simple instance of  {@link Vec2}
	 * @param a
	 * @param b
	 * @return
	 */
	public static Vec2 vec(float a, float b) {
		return new Vec2(a, b);
	}

	/**
	 * Creates an array of {@link Vec2} from the provided entries.
	 * Make sure the provided entries are multiple of 2
	 * @param entries
	 * @return
	 */
	public static Vec2[] vec2Array(float... entries) {
		if (entries.length % 2 != 0) {
			throw new IllegalArgumentException("Entries should be multiple of 2");
		}
		Vec2[] results = new Vec2[entries.length / 2];
		for(int i = 0, e = 0, n = entries.length; i < n;i+=2, e++) {
			results[e] = vec(entries[i], entries[i+1]);
		}
		return results;
	}

}
