package com.robertson.tools;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

public class ShapeFileUtils
{
	/**
	 * 
	 * @param file
	 * @param geometries
	 * @param ids
	 * @return area
	 * @throws IOException
	 */
	public static double readShapefile(
			File file,
			List<Geometry> geometries )
			throws IOException {

		/*
		 * Attmpt to find a GeoTools DataStore that can handle the shapefile
		 */
		Map<String, Serializable> connectParameters = new HashMap<String, Serializable>();

		connectParameters.put(
				"url",
				file.toURI().toURL());
		connectParameters.put(
				"create spatial index",
				false);

		DataStore dataStore = DataStoreFinder.getDataStore(connectParameters);
		if (dataStore == null) {
			throw new java.io.FileNotFoundException(
					"No DataStore found to handle" + file.getPath());
		}

		/*
		 * We are now connected to the shapefile. Get the type name of the
		 * features within it
		 */
		String[] typeNames = dataStore.getTypeNames();
		String typeName = typeNames[0];

		/*
		 * Iterate through the features, collecting some spatial data (line or
		 * boundary length) on each one
		 */
		FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
		FeatureCollection<SimpleFeatureType, SimpleFeature> collection;
		FeatureIterator<SimpleFeature> iterator;

		featureSource = dataStore.getFeatureSource(typeName);
		collection = featureSource.getFeatures();
		iterator = collection.features();

		double totalArea = 0.0;
		try {
			while (iterator.hasNext()) {
				SimpleFeature feature = iterator.next();
				/*
				 * The spatial portion of the feature is represented by a
				 * Geometry object
				 */
				Geometry geometry = (Geometry) feature.getDefaultGeometry();
				totalArea += geometry.getArea();
				geometries.add(geometry);
			}
		}
		finally {
			/*
			 * You MUST explicitly close the feature iterator otherwise terrible
			 * things will happen !!!
			 */
			if (iterator != null) {
				iterator.close();
			}
		}

		return totalArea;

	}

}
