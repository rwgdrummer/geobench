package com.robertson.geobench.operations;

import com.robertson.benchmark.BaseOperation;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.prep.PreparedGeometryFactory;

public class JTSPreparedIntersectionOperation extends
		BaseOperation<Geometry>
{

	final static PreparedGeometryFactory FACTORY = new PreparedGeometryFactory();

	public JTSPreparedIntersectionOperation() {

	}

	@Override
	public String getName() {
		return "JTS Prepared Intersection";
	}

	@Override
	public String getStatName() {
		return "count";
	}

	@Override
	public double op(
			Geometry obj1,
			Geometry obj2 ) {

		return (FACTORY.create(
				obj1).intersects(
				obj2) ? 1 : 0);
	}
}
