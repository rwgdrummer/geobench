package com.robertson.geobench.hull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.robertson.benchmark.Benchmark;
import com.robertson.geobench.hull.operations.GeometryHullComplex;
import com.robertson.geobench.hull.operations.GeometryHullSimple;
import com.robertson.geobench.hull.operations.TriangleBasedHull;
import com.robertson.tools.GeometryGenerator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ConcaveHullBench extends
		Benchmark<Geometry>
{
	final Envelope drivingSetEnv = new Envelope(
			45,
			55,
			35,
			45);


	final Map<Integer, Collection<Geometry>> dataSet = new HashMap<Integer, Collection<Geometry>>();

	final GeometryGenerator gen = new GeometryGenerator();

	public static void main(
			String args[] )
			throws IOException {
		ConcaveHullBench benchmark = new ConcaveHullBench();
		benchmark.run();
		benchmark.dumpResult();
	}

	public void run()
			throws IOException {

		this.includeValue = true;
		this.setOutput(new PrintStream(
				new FileOutputStream(
						"geo_hull_bench_out.csv")));
		runRepeatedBenchmarks(
				Arrays.asList(
						new TriangleBasedHull(),
						new GeometryHullComplex(),
						new GeometryHullSimple()),
				10, // complexity
				10, // how many iterations
				false);

	}

	@Override
	public Collection<Geometry> constructDrivingSet(
			int complexityIteration,
			int sizeIteration ) {
		Collection<Geometry> geoSet = dataSet.get(sizeIteration);
		if (geoSet == null) {
			geoSet = new ImmutableList.Builder<Geometry>().addAll(
					gen.generate(
							1,
							sizeIteration * 2000,
							new RandomDistortationFn(
									7777),
							drivingSetEnv)).build();
			
		//	try {
		//		GeometryCollection geoCollection = (GeometryCollection) geoSet.iterator().next();
		//		int c = geoCollection.getNumGeometries();
		//		Geometry colls[] = new Geometry[c];
		//		for (int i = 0; i < c; i++) {
		//			colls[i] = geoCollection.getGeometryN(i);
		//		}
		//		ShapefileTool.writeShape(
		//				"ghc_points_" + sizeIteration,
		//				new File(
		//						"testdata"),
		//				colls);
		//	}
		//	catch (IOException e) {
		//		e.printStackTrace();
		//	}
			dataSet.put(
					sizeIteration,
					geoSet);
		}
		return geoSet;
	}

	@Override
	public Collection<Geometry> constructCompareSet(
			int complexityIteration,
			int sizeIteration ) {
		// dummy
		return Collections.singletonList((Geometry) new GeometryFactory().createPoint(new Coordinate(
				0,
				0)));
	}

}
