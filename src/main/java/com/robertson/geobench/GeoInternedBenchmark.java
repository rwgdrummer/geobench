package com.robertson.geobench;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.robertson.benchmark.BaseOperation;
import com.robertson.benchmark.Benchmark;
import com.robertson.benchmark.Benchmark.FixedDistortationFn;
import com.robertson.benchmark.Benchmark.RandomDistortationFn;
import com.robertson.geobench.operations.JTSPreparedIntersectionOperationWithIntern;
import com.robertson.geobench.operations.JTSPreparedIntersectionOperationWithoutIntern;
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

	CompareSetSizeCalculator calculator = new CompareSetSizeCalculator() {

		@Override
		public int getSize(
				int iteration ) {
			return iteration;
		}

	};

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
			String args[] ) {
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

	public void run() {
		runRepeatedBenchmarks(
				Arrays.asList(
						new JTSPreparedIntersectionOperationWithoutIntern(),
						new JTSPreparedIntersectionOperationWithIntern()),
				1,
				10,
				true);
	}

	public void runWithout() {
		calculator = new CompareSetSizeCalculator() {

			@Override
			public int getSize(
					int iteration ) {
				return ((int) (iteration / 1000) + 1) * 1000;
			}

		};
		runRepeatedBenchmarks(
				Collections.singletonList((BaseOperation<byte[]>) new JTSPreparedIntersectionOperationWithoutIntern()),
				1,
				10,
				false);
	}

	public void runWith() {
		calculator = new CompareSetSizeCalculator() {

			@Override
			public int getSize(
					int iteration ) {
				return ((int) (iteration / 1000) + 1) * 1000;
			}

		};
		runRepeatedBenchmarks(
				Collections.singletonList((BaseOperation<byte[]>) new JTSPreparedIntersectionOperationWithIntern()),
				1,
				10,
				false);
	}

	@Override
	public Collection<byte[]> constructDrivingSet(
			int iteration ) {
		ImmutableList.Builder<byte[]> set = new ImmutableList.Builder<byte[]>();
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
								0.05,
								drivingSetEnv)).build(),
				new Function<Geometry, byte[]>() {

					@Override
					public byte[] apply(
							Geometry queryGeometry ) {
						return new WKBWriter().write(queryGeometry);
					}

				});
		for (int i = 0; i < iteration; i++) {
			set.addAll(geos);
		}

		return set.build();

	}

	@Override
	public Collection<byte[]> constructCompareSet(
			int iteration ) {
		return Collections2.transform(
				new ImmutableList.Builder<Geometry>().addAll(
						gen.generate(
								calculator.getSize(iteration),
								Arrays.asList(
										0.3,
										0.2,
										0.1),
								new RandomDistortationFn(
										7777),
								0.1,
								compareSetEnv)).build(),
				new Function<Geometry, byte[]>() {

					@Override
					public byte[] apply(
							Geometry queryGeometry ) {
						return new WKBWriter().write(queryGeometry);
					}

				});
	}

	private interface CompareSetSizeCalculator
	{
		public int getSize(
				int iteration );
	}
}
