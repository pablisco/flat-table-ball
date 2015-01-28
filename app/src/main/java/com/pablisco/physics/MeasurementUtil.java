package com.pablisco.physics;

import org.jbox2d.common.Vec2;

/**
 * Created by pablisco on 24/01/2015.
 *
 * This class provides with a set of utility methods that are used for converting measurements
 * from screen to world pixels. It provides methods to convert simple units and vectors
 */
public class MeasurementUtil {

	/**
	 * Pixel per meter
	 */
	public static final float PPM = 128f;

	/**
	 * Converts a measurement of pixels into meters
	 * @param value to be converted
	 * @return the number of pixels divided by {@link #PPM}
	 */
	public static float pixelToMeters(float value) {
		return value / PPM;
	}

	/**
	 * Converts a measurement of meters into pixels
	 * @param value of meters to be converted
	 * @return the number of meters times {@link #PPM}
	 */
	public static float metersToPixel(float value) {
		return value * PPM;
	}

	/**
	 * Same as {@link #pixelToMeters(float)} but with {@link Vec2} as parameters
	 */
	public static Vec2 pixelToMeters(Vec2 vector) {
		return new Vec2(vector.x / PPM, vector.y / PPM);
	}

	/**
	 * Same as {@link #metersToPixel(float)} but with {@link Vec2} as parameters
	 */
	public static Vec2 metersToPixels(Vec2 vector) {
		return new Vec2(vector.x * PPM, vector.y * PPM);
	}

}
