package com.robertson.geobench;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.robertson.benchmark.Benchmark;
import com.robertson.benchmark.Benchmark.FixedDistortationFn;
import com.robertson.benchmark.Benchmark.RandomDistortationFn;
import com.robertson.geobench.operations.JTSIntersectionOperation;
import com.robertson.geobench.operations.JTSPreparedIntersectionOperation;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/** 
 * Currently set up to compare intersection operations between prepared and non-prepared geometries.
 * The complexity of the driving geometry (left side) is increased in each iteration.
 * 
 * @author rwgdrummer
 *
 */
public class GeoBenchmark extends
		Benchmark<Geometry>
{
	final Envelope drivingSetEnv = new Envelope(
			45,
			55,
			35,
			45);
	final Envelope compareSetEnv = new Envelope(
			42,
			57,
			32,
			47);
	final GeometryGenerator gen = new GeometryGenerator();

	Collection<Geometry> compareSet = null;

	public static void main(
			String args[] ) {
		GeoBenchmark benchmark = new GeoBenchmark();
		benchmark.run();
		benchmark.dumpResult();
	}

	public void run() {
		runRepeatedBenchmarks(
				Arrays.asList(
						new JTSIntersectionOperation(),
						new JTSPreparedIntersectionOperation()),
				1,
				5,
				false);

	}

	@Override
	public Collection<Geometry> constructCompareSet(
			int iteration ) {
		if (compareSet == null) compareSet = new ImmutableList.Builder<Geometry>().addAll(
				gen.generate(
						1000,
						Arrays.asList(
								0.3,
								0.2,
								0.1),
						new RandomDistortationFn(
								7777),
						0.1,
						compareSetEnv)).build();
		return compareSet;
	}

	@Override
	public Collection<Geometry> constructDrivingSet(
			int iteration ) {
		return new ImmutableList.Builder<Geometry>().addAll(
				gen.generate(
						2,
						Arrays.asList(
								1.1,
								0.9,
								0.75),
						new FixedDistortationFn(
								1.0),
						0.05 / (double) iteration,
						drivingSetEnv)).build();
	}
}
