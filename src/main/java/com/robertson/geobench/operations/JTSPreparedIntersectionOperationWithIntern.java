package com.robertson.geobench.operations;

import java.util.Arrays;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.robertson.benchmark.BaseOperation;
import com.vividsolutions.jts.geom.prep.PreparedGeometry;
import com.vividsolutions.jts.geom.prep.PreparedGeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

public class JTSPreparedIntersectionOperationWithIntern extends
		BaseOperation<byte[]>
{
	private static final Interner<GeoImage> geometryInterner = Interners.newWeakInterner();
	final static PreparedGeometryFactory FACTORY = new PreparedGeometryFactory();

	public JTSPreparedIntersectionOperationWithIntern() {

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

		GeoImage image = geometryInterner.intern(new GeoImage(
				obj1));
		image.init();
		try {
			return (image.getPreparedGeometry().intersects(new WKBReader().read(obj2))) ? 1 : 0;
		}
		catch (ParseException e) {
			e.printStackTrace();
			return Double.NaN;
		}
	}

	private static class GeoImage
	{
		final byte[] image;
		private PreparedGeometry preparedGeometry = null;

		public GeoImage(
				byte[] image ) {
			super();
			this.image = image;
		}

		public synchronized void init() {
			if (preparedGeometry == null) {
				try {
					preparedGeometry = FACTORY.create(new WKBReader().read(image));
				}
				catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		public PreparedGeometry getPreparedGeometry() {
			return preparedGeometry;
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
