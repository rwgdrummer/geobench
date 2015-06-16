package com.robertson.geobench;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.robertson.benchmark.DistortationFn;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class GeometryGenerator
{
	/**
	 * @param count
	 * @param distanceactors
	 * @param distortationFn
	 * @param delta
	 * @param env
	 * @return
	 */
	public Iterator<Geometry> generate(
			final int count,
			final List<Double> distanceactors,
			final DistortationFn distortationFn,
			final int points,
			final Envelope env ) {
		// Create the star-ellipses for intersections later on
		return new Iterator<Geometry>() {
			int currentCount = 0;
			GeometryFactory geometryFactory = new GeometryFactory();
			final Random random = new Random(
					90210);

			@Override
			public boolean hasNext() {
				return currentCount < count;
			}

			@Override
			public Geometry next() {
				// Thanks to Chris Bennight for the foundations of this code.
				currentCount++;
				double cx = env.centre().x * distortationFn.distort();
				double cy = env.centre().y * distortationFn.distort();

				double dx = env.getWidth();
				double dy = env.getHeight();

				// We will use a coordinate list to build the linear ring
				CoordinateList clist = new CoordinateList();
				int i = 0;
				double amount = 360.0;
				double delta = amount / (double) points;
				for (double angle = 0.0; angle < 360 && i < points; angle += (delta - (delta * (random.nextDouble())))) {
					double a = distanceactors.get(Math.abs(random.nextInt()) % distanceactors.size()) * 0.5 * dx;
					double b = distanceactors.get(Math.abs(random.nextInt()) % distanceactors.size()) * 0.5 * dy;
					clist.add(new Coordinate(
							cx + a * Math.sin(Math.toRadians(angle)),
							cy + b * Math.cos(Math.toRadians(angle))));
					amount -= delta;
					delta = amount / (double) (points - i);
					i++;
				}

				clist.add(clist.get(0));
				return geometryFactory.createPolygon(clist.toCoordinateArray());
			}

			@Override
			public void remove() {

			}
		};
	}
}
