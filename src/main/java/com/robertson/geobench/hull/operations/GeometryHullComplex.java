package com.robertson.geobench.hull.operations;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robertson.benchmark.BaseOperation;
import com.robertson.benchmark.BenchmarkIteration;
import com.robertson.hull.GeometryHullTool;
import com.robertson.hull.ShapefileTool;
import com.robertson.tools.CoordinateEuclideanDistanceFn;
import com.vividsolutions.jts.algorithm.ConvexHull;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

public class GeometryHullComplex extends
		BaseOperation<Geometry>
{

	private static final Logger LOGGER = LoggerFactory.getLogger(GeometryHullComplex.class);

	GeometryHullTool geoHullTool = new GeometryHullTool();

	public GeometryHullComplex() {
		geoHullTool.setDistanceFnForCoordinate(new CoordinateEuclideanDistanceFn());
	}

	@Override
	public String getName() {
		return "GHC";
	}

	@Override
	public String getStatName() {
		return "area";
	}

	@Override
	public double op(
			BenchmarkIteration iteration,
			Geometry obj1,
			Geometry obj2 ) {
		geoHullTool.setConcaveThreshold(1.0 + (1.0 - iteration.getComplexityIteration() * 0.1));
		final UniqueCoordinateArrayFilter filter = new UniqueCoordinateArrayFilter();
		((GeometryCollection) obj1).apply(filter);

		final ConvexHull hull = new ConvexHull(
				filter.getCoordinates(),
				obj1.getFactory());
		final Geometry geo = geoHullTool.createHullFromGeometry(
				hull.getConvexHull(),
				filter.getCoordinatesCollection(),
				false);

		if (LOGGER.isTraceEnabled()) {
			try {
				ShapefileTool.writeShape(
						"ghc_hull_" + iteration.toString(),
						new File(
								"testdata"),
						new Geometry[] {
							geo
						});
			}
			catch (IOException e) {
				LOGGER.error(
						"Cannot write out hull shape file",
						e);
			}
		}

		assert (geo.isSimple());

		return geo.getArea();
	}
}
