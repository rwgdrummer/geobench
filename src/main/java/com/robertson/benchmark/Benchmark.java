package com.robertson.benchmark;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public abstract class Benchmark<T>
{
	private List<BenchmarkIteration> resultSet = new ArrayList<BenchmarkIteration>();
	private PrintStream output = System.out;

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

	public void dumpCSV(
			PrintStream os ) {
		for (BenchmarkIteration result : resultSet) {
			result.dumpCSV(os);
		}
	}

	public void runRepeatedBenchmarks(
			Collection<BaseOperation<T>> ops,
			int complexityIterations,
			int sizeIterations,
			boolean breakAtChange ) {
		BenchmarkIteration last = null;

		for (int si = 1; si <= sizeIterations; si++) {
			for (int ci = 1; ci <= complexityIterations; ci++) {
				final BenchmarkIteration iterationResults = new BenchmarkIteration(
						ci,
						si);

				runBenchmark(
						iterationResults,
						ops,
						constructDrivingSet(
								ci,
								si),
						constructCompareSet(
								ci,
								si));

				resultSet.add(iterationResults);

				if (last != null) {
					if (breakAtChange && !last.getBest().algorithm.equals(iterationResults.getBest().algorithm)) {
						return;
					}
				}
			//	System.out.print('.');
			//	System.out.flush();
				last = iterationResults;

			}
			//System.out.println();
			dumpCSV(output);
			resultSet.clear();
		}
	}

	
	public PrintStream getOutput() {
		return output;
	}

	public void setOutput(
			PrintStream output ) {
		this.output = output;
	}

	public abstract Collection<T> constructDrivingSet(
			int complexityIteration,
			int sizeIteration );

	public abstract Collection<T> constructCompareSet(
			int complexityIteration,
			int sizeIteration );

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
