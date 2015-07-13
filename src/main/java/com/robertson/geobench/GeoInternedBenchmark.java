package com.robertson.geobench;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.robertson.benchmark.BaseOperation;
import com.robertson.benchmark.Benchmark;
import com.robertson.geobench.operations.JTSIntersectionOperationWithIntern;
import com.robertson.geobench.operations.JTSIntersectionOperationWithoutIntern;
import com.robertson.geobench.operations.JTSInversePreparedIntersectionOperationWithIntern;
import com.robertson.geobench.operations.JTSInversePreparedIntersectionOperationWithoutIntern;
import com.robertson.geobench.operations.JTSPreparedIntersectionOperationWithIntern;
import com.robertson.geobench.operations.JTSPreparedIntersectionOperationWithoutIntern;
import com.robertson.tools.GeometryGenerator;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBWriter;

public class GeoInternedBenchmark extends
		Benchmark<byte[]>
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

	final static double mb = 1024 * 1024;
	Collection<byte[]> compareSet = null;
	int lastSize = 0;

	public static void dumpMem() {

		// Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();

		// Print used memory
		System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);

		// Print free memory
		System.out.println("Free Memory:" + runtime.freeMemory() / mb);

		// Print total available memory
		System.out.println("Total Memory:" + runtime.totalMemory() / mb);

		// Print Maximum available memory
		System.out.println("Max Memory:" + runtime.maxMemory() / mb);

	}

	public static void main(
			String args[] )
			throws FileNotFoundException {
		GeoInternedBenchmark benchmark = new GeoInternedBenchmark();
		if (args.length > 0) {
			if (args[0].equals("with"))
				benchmark.runWith();
			else
				benchmark.runWithout();
		}
		else {
			benchmark.run();
		}

		benchmark.dumpResult();
		dumpMem();
	}

	public void run()
			throws FileNotFoundException {
		this.setOutput(new PrintStream(
				new FileOutputStream(
						"geo_interned_bench_out.csv")));
		runRepeatedBenchmarks(
				Arrays.asList(
						new JTSPreparedIntersectionOperationWithoutIntern(),
						new JTSInversePreparedIntersectionOperationWithoutIntern(),
						new JTSInversePreparedIntersectionOperationWithIntern(),
						new JTSPreparedIntersectionOperationWithIntern(),
						new JTSIntersectionOperationWithoutIntern(),
						new JTSIntersectionOperationWithIntern()),
				100, // complexity
				10, // how many
				false);
	}

	public void runWithout() {

		runRepeatedBenchmarks(
				Collections.singletonList((BaseOperation<byte[]>) new JTSPreparedIntersectionOperationWithoutIntern()),
				100,
				1,
				false);
	}

	public void runWith() {

		runRepeatedBenchmarks(
				Collections.singletonList((BaseOperation<byte[]>) new JTSPreparedIntersectionOperationWithIntern()),
				100,
				1,
				false);
	}

	@Override
	public Collection<byte[]> constructDrivingSet(
			int complexityIteration,
			int sizeIteration ) {
		Collection<byte[]> geos = Collections2.transform(
				new ImmutableList.Builder<Geometry>().addAll(
						gen.generate(
								1,
								Arrays.asList(
										1.1,
										0.9,
										0.75),
								new FixedDistortationFn(
										1.0),
								2 * complexityIteration,
								drivingSetEnv)).build(),
				new Function<Geometry, byte[]>() {

					@Override
					public byte[] apply(
							Geometry queryGeometry ) {
						return new WKBWriter().write(queryGeometry);
					}

				});

		return geos;

	}

	@Override
	public Collection<byte[]> constructCompareSet(
			int complexityIteration,
			int sizeIteration ) {
		if (lastSize != sizeIteration) compareSet = Collections2.transform(
				new ImmutableList.Builder<Geometry>().addAll(
						gen.generate(
								sizeIteration * 10,
								Arrays.asList(
										0.8,
										0.6,
										0.7),
								new RandomDistortationFn(
										7777),
								100,
								compareSetEnv)).build(),
				new Function<Geometry, byte[]>() {

					@Override
					public byte[] apply(
							Geometry queryGeometry ) {
						return new WKBWriter().write(queryGeometry);
					}

				});
		lastSize = sizeIteration;
		return compareSet;
	}

}
