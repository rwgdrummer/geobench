package com.robertson.benchmark;

import java.util.Collection;

/**
 * 
 * Request an operation between an object and a set of data.
 * 
 * The object instance is contained by the operation since each type of object
 * may require different approaches to perform a similar operation. This allows
 * comparing different approaches to perform the same type of operation with the
 * same.
 * 
 * @param <T>
 */
public abstract class BaseOperation<T>
{
	public static final int SAMPLE_SIZE = 5;

	public BaseOperation() {
		super();
	}

	public OpResult run(
			BenchmarkIteration iteration,
			Collection<T> drivingObjects,
			Collection<T> compareSet ) {

		long st = System.nanoTime();

		double result = 0;
		for (int i = 0; i < SAMPLE_SIZE; i++) {
			result = 0;
			for (T drivingObject : drivingObjects) {
				for (T objToCompare : compareSet) {
					result += op(
							iteration,
							drivingObject,
							objToCompare);
				}
			}
		}

		return new OpResult(
				getStatName(),
				(System.nanoTime() - st) / SAMPLE_SIZE,
				compareSet.size(),
				getName(),
				result);
	}

	public abstract String getName();

	public abstract String getStatName();

	public abstract double op(
			BenchmarkIteration iteration,
			T obj1,
			T obj2 );

}
