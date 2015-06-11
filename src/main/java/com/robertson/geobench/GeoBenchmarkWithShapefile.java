package com.robertson.geobench;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.robertson.benchmark.Benchmark;
import com.robertson.benchmark.Benchmark.RandomDistortationFn;
import com.robertson.geobench.operations.JTSIntersectionOperation;
import com.robertson.geobench.operations.JTSPreparedIntersectionOperation;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class GeoBenchmarkWithShapefile extends
		Benchmark<Geometry>
{

	final Envelope compareSetEnv;
	final List<Geometry> driverSet;
	final GeometryGenerator gen = new GeometryGenerator();

	private Envelope calcEnvelope() {
		double maxx = -360, maxy = -360, minx = 360, miny = 360;
		for (Geometry geo : driverSet) {
			final Envelope env = geo.getEnvelopeInternal();
			maxx = Math.max(
					maxx,
					env.getMaxX() + 2);
			minx = Math.min(
					minx,
					env.getMinX() - 2);
			maxy = Math.max(
					maxy,
					env.getMaxY() + 2);
			miny = Math.min(
					miny,
					env.getMinY() - 2);
		}
		return new Envelope(
				Math.max(
						minx,
						-180),
				Math.min(
						maxx,
						180),
				Math.max(
						miny,
						-90),
				Math.min(
						maxy,
						90));
	}

	public GeoBenchmarkWithShapefile(
			List<Geometry> driverSet ) {
		super();
		this.driverSet = driverSet;
		compareSetEnv = calcEnvelope();
	}

	public static void main(
			String args[] )
			throws IOException {

		List<Geometry> driverSet = new ArrayList<Geometry>();

		ShapeFileUtils.readShapefile(
				new File(
						args[0]),
				driverSet);
		GeoBenchmarkWithShapefile benchmark = new GeoBenchmarkWithShapefile(
				driverSet);
		benchmark.run();
		benchmark.dumpResult();
	}

	public void run() {

		runRepeatedBenchmarks(
				Arrays.asList(
						new JTSIntersectionOperation(),
						new JTSPreparedIntersectionOperation()),
				1,
				10,
				false);

	}

	@Override
	public Collection<Geometry> constructCompareSet(
			int iteration ) {
		return new ImmutableList.Builder<Geometry>().addAll(
				gen.generate(
						iteration,
						Arrays.asList(
								0.3,
								0.2,
								0.1),
						new RandomDistortationFn(
								7777),
						0.1,
						compareSetEnv)).build();
	}

	@Override
	public Collection<Geometry> constructDrivingSet(
			int iteration ) {
		return this.driverSet;
	}
}
