package com.robertson.geobench.operations;

import com.robertson.benchmark.BaseOperation;
import com.robertson.benchmark.BenchmarkIteration;
import com.vividsolutions.jts.geom.prep.PreparedGeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

public class JTSInversePreparedIntersectionOperationWithoutIntern extends
		BaseOperation<byte[]>
{

	final static PreparedGeometryFactory FACTORY = new PreparedGeometryFactory();

	public JTSInversePreparedIntersectionOperationWithoutIntern() {

	}

	@Override
	public String getName() {
		return "JTS Inverse Prepared Intersection Without Intern";
	}

	@Override
	public String getStatName() {
		return "count";
	}

	@Override
	public double op(
			BenchmarkIteration iteration,
			byte[] obj1,
			byte[] obj2 ) {

		try {
			return (FACTORY.create(new WKBReader().read(obj2)).intersects(new WKBReader().read(obj1))) ? 1 : 0;
		}
		catch (ParseException e) {
			e.printStackTrace();
			return Double.NaN;
		}
	}
}
