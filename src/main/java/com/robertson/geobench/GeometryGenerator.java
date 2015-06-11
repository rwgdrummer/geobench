package com.robertson.geobench;

import java.util.Iterator;
import java.util.List;

import com.robertson.benchmark.DistortationFn;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

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
			final double delta,
			final Envelope env ) {
		// Create the star-ellipses for intersections later on
		return new Iterator<Geometry>() {
			int currentCount = 0;
			GeometryFactory geometryFactory = new GeometryFactory();

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

				// We will use a coordinate list to build the linearring
				CoordinateList clist = new CoordinateList();
				double angle = 0.0;
				for (int i = 0; angle < 360; angle += delta, i++) {
					double a = distanceactors.get(i % distanceactors.size()) * 0.5 * dx;
					double b = distanceactors.get(i % distanceactors.size()) * 0.5 * dy;
					clist.add(new Coordinate(
							cx + a * Math.sin(angle),
							cy + b * Math.cos(angle)));

				}

				clist.add(clist.get(0));
				LinearRing lr = geometryFactory.createLinearRing(clist.toCoordinateArray());
				return geometryFactory.createPolygon(
						lr,
						null);
			}

			@Override
			public void remove() {

			}
		};
	}
}
