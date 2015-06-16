package com.robertson.benchmark;

import java.io.PrintStream;
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
	final private int complexityIteration;
	final private int sizeIteration;

	public BenchmarkIteration(
			int complexityIteration,
			int sizeIteration ) {
		super();
		this.complexityIteration = complexityIteration;
		this.sizeIteration = sizeIteration;
	}

	public void add(
			OpResult result ) {
		results.add(result);
	}

	public int getComplexityIteration() {
		return complexityIteration;
	}

	public int getSizeIteration() {
		return sizeIteration;
	}

	public OpResult getBest() {
		return this.results.first();
	}

	public void dumpCSV(PrintStream os) {
		final Iterator<OpResult> it = results.iterator();
		while (it.hasNext()) {
			os.println(complexityIteration +", " + sizeIteration + ", " + it.next().getCSV());
		}
	}

	public void dumpResult() {
		final Iterator<OpResult> it = results.iterator();
		while (it.hasNext()) {
			System.out.println("ci=" + complexityIteration + ", si=" + sizeIteration + ", " + it.next().toString());
		}
		System.out.println();
	}
}
