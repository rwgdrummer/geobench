package com.robertson.geobench;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.robertson.benchmark.Benchmark;
import com.robertson.geobench.operations.JTSIntersectionOperation;
import com.robertson.geobench.operations.JTSInversePreparedIntersectionOperation;
import com.robertson.geobench.operations.JTSPreparedIntersectionOperation;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Currently set up to compare intersection operations between prepared and
 * non-prepared geometries. The complexity of the driving geometry (left side)
 * is increased in each iteration.
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
	int lastSize = 0;

	public static void main(
			String args[] )throws IOException  {
		GeoBenchmark benchmark = new GeoBenchmark();
		benchmark.run();
		benchmark.dumpResult();
	}

	public void run() throws IOException {
	
		this.setOutput(new PrintStream(
				new FileOutputStream("out.csv")));
		runRepeatedBenchmarks(
				Arrays.asList(
						new JTSIntersectionOperation(),
						new JTSInversePreparedIntersectionOperation(),
						new JTSPreparedIntersectionOperation()),
				100, // complexity
				10, // how many
				false);

	}

	@Override
	public Collection<Geometry> constructCompareSet(
			int complexityIteration,
			int sizeIteration ) {
		if (lastSize != sizeIteration) compareSet = new ImmutableList.Builder<Geometry>().addAll(
				gen.generate(
						sizeIteration * 500,
						Arrays.asList(
								0.8,
								0.6,
								0.7),
						new RandomDistortationFn(
								7777),
						100,
						compareSetEnv)).build();
		lastSize = sizeIteration;
		return compareSet;
	}

	@Override
	public Collection<Geometry> constructDrivingSet(
			int complexityIteration,
			int sizeIteration ) {
		return new ImmutableList.Builder<Geometry>().addAll(
				gen.generate(
						1,
						Arrays.asList(
								1.1,
								0.9,
								0.75),
						new FixedDistortationFn(
								1.0),
						10* complexityIteration,
						drivingSetEnv)).build();
	}
}
