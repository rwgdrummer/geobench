package com.robertson.geobench.operations;

import java.util.Arrays;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.robertson.benchmark.BaseOperation;
import com.robertson.benchmark.BenchmarkIteration;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.prep.PreparedGeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

public class JTSInversePreparedIntersectionOperationWithIntern extends
		BaseOperation<byte[]>
{
	private static final Interner<GeoImage> geometryInterner = Interners.newWeakInterner();
	final static PreparedGeometryFactory FACTORY = new PreparedGeometryFactory();

	public JTSInversePreparedIntersectionOperationWithIntern() {

	}

	@Override
	public String getName() {
		return "JTS Inverse Prepared Intersection With Intern";
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

		GeoImage image = geometryInterner.intern(new GeoImage(
				obj1));
		image.init();
		try {
			return (FACTORY.create(new WKBReader().read(obj2)).intersects(image.getGeometry())) ? 1 : 0;
		}
		catch (ParseException e) {
			e.printStackTrace();
			return Double.NaN;
		}
	}

	private static class GeoImage
	{
		final byte[] image;
		private Geometry geometry = null;

		public GeoImage(
				byte[] image ) {
			super();
			this.image = image;
		}

		public synchronized void init() {
			if (geometry == null) {
				try {
					geometry = new WKBReader().read(image);
				}
				catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		public Geometry getGeometry() {
			return geometry;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(image);
			return result;
		}

		@Override
		public boolean equals(
				Object obj ) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			GeoImage other = (GeoImage) obj;
			if (!Arrays.equals(
					image,
					other.image)) return false;
			return true;
		}

	}
}
