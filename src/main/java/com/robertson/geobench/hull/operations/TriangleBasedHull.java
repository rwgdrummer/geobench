package com.robertson.geobench.hull.operations;

import java.io.File;
import java.io.IOException;

import org.opensphere.geometry.algorithm.ConcaveHull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robertson.benchmark.BaseOperation;
import com.robertson.benchmark.BenchmarkIteration;
import com.robertson.hull.ShapefileTool;
import com.vividsolutions.jts.geom.Geometry;

public class TriangleBasedHull extends
		BaseOperation<Geometry>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TriangleBasedHull.class);

	public TriangleBasedHull() {}

	@Override
	public String getName() {
		return "TBH";
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
		final ConcaveHull hull = new ConcaveHull(
				obj1,
				1.5 - (0.1 * iteration.getComplexityIteration()));
		final Geometry geo = hull.getConcaveHull();
		assert (geo.isSimple());

		if (LOGGER.isTraceEnabled()) {
			try {
				ShapefileTool.writeShape(
						"tbh_hull_" + iteration.toString(),
						new File(
								"testdata"),
						new Geometry[] {
							geo
						});
			}
			catch (IOException e) {
				LOGGER.error("Cannot write out hull shape file", e);
			}
		}
		return geo.getArea();
	}
}
