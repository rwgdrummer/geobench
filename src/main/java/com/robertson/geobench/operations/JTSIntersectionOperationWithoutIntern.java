package com.robertson.geobench.operations;

import com.robertson.benchmark.BaseOperation;
import com.robertson.benchmark.BenchmarkIteration;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

public class JTSIntersectionOperationWithoutIntern extends
		BaseOperation<byte[]>
{

	public JTSIntersectionOperationWithoutIntern() {

	}

	@Override
	public String getName() {
		return "JTS Intersection Without Intern";
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
			return (new WKBReader().read(obj1).intersects(new WKBReader().read(obj2))) ? 1 : 0;
		}
		catch (ParseException e) {
			e.printStackTrace();
			return Double.NaN;
		}
	}
}
