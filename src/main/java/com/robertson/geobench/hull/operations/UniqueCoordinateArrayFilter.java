package com.robertson.geobench.hull.operations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;

public class UniqueCoordinateArrayFilter implements
		CoordinateFilter
{
	private TreeSet<Coordinate> treeSet = new TreeSet<Coordinate>();
	private ArrayList<Coordinate> list = new ArrayList<Coordinate>();

	public UniqueCoordinateArrayFilter() {}

	/**
	 * Returns the gathered <code>Coordinate</code>s.
	 * 
	 * @return the <code>Coordinate</code>s collected by this
	 *         <code>CoordinateArrayFilter</code>
	 */
	public Coordinate[] getCoordinates() {
		Coordinate[] coordinates = new Coordinate[list.size()];
		return (Coordinate[]) list.toArray(coordinates);
	}

	public Collection<Coordinate> getCoordinatesCollection() {
		return list;
	}

	public void filter(
			Coordinate coord ) {
		if (!treeSet.contains(coord)) {
			list.add(coord);
			treeSet.add(coord);
		}
	}
}