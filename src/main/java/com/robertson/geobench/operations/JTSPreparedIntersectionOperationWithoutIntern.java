package com.robertson.geobench.operations;

import com.robertson.benchmark.BaseOperation;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

public class JTSPreparedIntersectionOperationWithoutIntern extends
		BaseOperation<byte[]>
{

	public JTSPreparedIntersectionOperationWithoutIntern() {

	}

	@Override
	public String getName() {
		return "JTS Prepared Intersection With Intern";
	}

	@Override
	public String getStatName() {
		return "count";
	}

	@Override
	public double op(
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
