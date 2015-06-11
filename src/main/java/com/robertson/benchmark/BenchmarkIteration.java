package com.robertson.benchmark;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class BenchmarkIteration
{
	final private TreeSet<OpResult> results = new TreeSet<OpResult>(
			new Comparator<OpResult>() {

				@Override
				public int compare(
						OpResult arg0,
						OpResult arg1 ) {
					return (arg0.duration - arg1.duration) > 0 ? 1 : -1;
				}
			});
	final private int iteration;

	public BenchmarkIteration(
			int iteration ) {
		super();
		this.iteration = iteration;
	}

	public void add(
			OpResult result ) {
		results.add(result);
	}

	public int getIteration() {
		return iteration;
	}

	public OpResult getBest() {
		return this.results.first();
	}

	public void dumpResult() {
		System.out.printf(
				"----iteration = %d-----\n",
				iteration);
		final Iterator<OpResult> it = results.iterator();
		while (it.hasNext()) {
			System.out.println(it.next().toString());
		}
		System.out.printf("\n");
	}
}
