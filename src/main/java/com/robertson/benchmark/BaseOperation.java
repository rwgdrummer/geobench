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
	public BaseOperation() {
		super();
	}

	public OpResult run(
			Collection<T> drivingObjects,
			Collection<T> compareSet ) {

		long st = System.nanoTime();
		double result = 0;
		for (T drivingObject : drivingObjects) {
			for (T objToCompare : compareSet) {
				result += op(
						drivingObject,
						objToCompare);
			}
		}

		return new OpResult(
				getStatName(),
				System.nanoTime() - st,
				compareSet.size(),
				getName(),
				result);
	}

	public abstract String getName();

	public abstract String getStatName();

	public abstract double op(
			T obj1,
			T obj2 );

}
