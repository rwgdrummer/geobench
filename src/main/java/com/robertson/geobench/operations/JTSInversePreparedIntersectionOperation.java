package com.robertson.geobench.operations;

import com.robertson.benchmark.BaseOperation;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.prep.PreparedGeometryFactory;

public class JTSInversePreparedIntersectionOperation extends
		BaseOperation<Geometry>
{

	final static PreparedGeometryFactory FACTORY = new PreparedGeometryFactory();

	public JTSInversePreparedIntersectionOperation() {

	}

	@Override
	public String getName() {
		return "Inverse JTS Prepared Intersection";
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
				obj2).intersects(
				obj1) ? 1 : 0);
	}
}
