package com.robertson.tools;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.robertson.benchmark.DistortationFn;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
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
				for (double angle = 0.0; angle < 360 && i < points; angle += delta) {
					double a = distanceactors.get(Math.abs(random.nextInt()) % distanceactors.size()) * 0.5 * dx;
					double b = distanceactors.get(Math.abs(random.nextInt()) % distanceactors.size()) * 0.5 * dy;
					clist.add(new Coordinate(
							cx + a * Math.sin(Math.toRadians(angle)),
							cy + b * Math.cos(Math.toRadians(angle))));
					// amount -= delta;
					// delta = amount / (double) (points - i);
					i++;
				}

				if (clist.size() == 1) {
					return geometryFactory.createPoint(clist.getCoordinate(0));
				}
				else if (clist.size() == 2) {
					return geometryFactory.createLineString(clist.toCoordinateArray());
				}
				clist.add(clist.get(0));

				return geometryFactory.createPolygon(clist.toCoordinateArray());
			}

			@Override
			public void remove() {

			}
		};
	}

	/**
	 * @param count
	 * @param size
	 * @param distortationFn
	 * @param env
	 * @return
	 */
	public Iterator<Geometry> generate(
			final int count,
			final int size,
			final DistortationFn distortationFn,
			final Envelope env ) {
		// Create the star-ellipses for intersections later on
		return new Iterator<Geometry>() {
			int currentCount = 0;
			GeometryFactory geometryFactory = new GeometryFactory();
			final Random random = new Random(
					90210);
			double angle = 0;

			@Override
			public boolean hasNext() {
				return currentCount < count;
			}

			@Override
			public Geometry next() {
				currentCount++;

				Geometry[] geos = new Geometry[size];
				double cx = env.centre().x * distortationFn.distort();
				double cy = env.centre().y * distortationFn.distort();

				double dx = env.getWidth();
				double dy = env.getHeight();
				double factor = 1.0;

				for (int i = 0; i < geos.length; i++) {

					double a = random.nextDouble() * 0.5 * factor * dx;
					double b = random.nextDouble() * 0.5 * factor * dy;
					final Coordinate coord = new Coordinate(
							cx + a * Math.sin(Math.toRadians(angle)),
							cy + b * Math.cos(Math.toRadians(angle)));

					final double delta = random.nextDouble() * 2.0;
					angle = angle + delta;
					if (angle > 360) {
						angle = 360 - angle;
						factor = factor - (factor * 0.3);
					}

					geos[i] = geometryFactory.createPoint(coord);
				}
				return new GeometryCollection(
						geos,
						geometryFactory);
			}

			@Override
			public void remove() {

			}
		};
	}
}
