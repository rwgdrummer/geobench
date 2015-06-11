package com.robertson.benchmark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public abstract class Benchmark<T>
{
	private List<BenchmarkIteration> resultSet = new ArrayList<BenchmarkIteration>();

	private void runBenchmark(
			BenchmarkIteration iteration,
			Collection<BaseOperation<T>> ops,
			Collection<T> drivingObjectSet,
			Collection<T> compareSet ) {

		for (BaseOperation<T> op : ops) {
			final OpResult result = (op.run(
					drivingObjectSet,
					compareSet));
			iteration.add(result);
		}
	}

	public void dumpResult() {
		for (BenchmarkIteration result : resultSet) {
			result.dumpResult();
		}
	}

	public void runRepeatedBenchmarks(
			Collection<BaseOperation<T>> ops,
			int start,
			int iterations,
			boolean breakAtChange ) {
		BenchmarkIteration last = null;
		final int stop = start + iterations;
		for (int i = start; i < stop; i++) {
			final BenchmarkIteration iterationResults = new BenchmarkIteration(
					i);

			runBenchmark(
					iterationResults,
					ops,
					constructDrivingSet(i),
					constructCompareSet(i));

			resultSet.add(iterationResults);

			if (last != null) {
				if (breakAtChange && !last.getBest().algorithm.equals(iterationResults.getBest().algorithm)) {
					return;
				}
			}
			System.out.print('.');
			System.out.flush();
			last = iterationResults;

		}
		System.out.println();
	}

	public abstract Collection<T> constructDrivingSet(
			int iteration );

	public abstract Collection<T> constructCompareSet(
			int iteration );

	public static class RandomDistortationFn implements
			DistortationFn
	{
		final Random r;

		public RandomDistortationFn(
				long seed ) {
			r = new Random(
					seed);
		}

		@Override
		public double distort() {
			return r.nextDouble();
		}
	}

	public static class FixedDistortationFn implements
			DistortationFn
	{
		final double v;

		public FixedDistortationFn(
				double v ) {
			this.v = v;
		}

		@Override
		public double distort() {
			return v;
		}
	}
}
