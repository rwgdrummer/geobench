package com.robertson.geobench.operations;

import com.robertson.benchmark.BaseOperation;
import com.robertson.benchmark.BenchmarkIteration;
import com.vividsolutions.jts.geom.Geometry;

public class JTSIntersectionOperation extends
		BaseOperation<Geometry>
{

	public JTSIntersectionOperation() {

	}

	@Override
	public String getName() {
		return "JTS Intersection";
	}

	@Override
	public String getStatName() {
		return "count";
	}

	@Override
	public double op(
			BenchmarkIteration iteration,
			Geometry obj1,
			Geometry obj2 ) {
		return (obj1.intersects(obj2) ? 1 : 0);
	}
}
