package com.robertson.hull;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class ShapefileTool
{

	private static SimpleFeatureType createFeatureType(
			final String typeName,
			final boolean isPoint ) {

		final SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName(typeName);
		builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference
													// system

		// add attributes in order
		builder.add(
				"the_geom",
				isPoint ? Point.class : Polygon.class);
		builder.length(
				15).add(
				"Name",
				String.class); // <- 15 chars width for name field

		// build the type

		return builder.buildFeatureType();
	}

	public static void writeShape(
			final String typeName,
			final File dir,
			final Geometry[] shapes )
			throws IOException {

		if (!dir.exists()) dir.mkdirs();

		final SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(
				createFeatureType(
						typeName,
						shapes[0] instanceof Point));

		final ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

		final Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put(
				"url",
				new File(
						dir.getAbsolutePath() + "/" + typeName + ".shp").toURI().toURL());
		params.put(
				"create spatial index",
				Boolean.TRUE);

		final ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
		newDataStore.createSchema(createFeatureType(
				typeName,
				shapes[0] instanceof Point));
		final Transaction transaction = new DefaultTransaction(
				"create");

		try (final FeatureWriter<SimpleFeatureType, SimpleFeature> writer = newDataStore.getFeatureWriterAppend(
				typeName,
				transaction)) {
			final int i = 1;
			for (final Geometry shape : shapes) {
				featureBuilder.add(shape);
				featureBuilder.add(Integer.valueOf(i));
				final SimpleFeature feature = featureBuilder.buildFeature(null);
				final SimpleFeature copy = writer.next();
				for (final AttributeDescriptor attrD : feature.getFeatureType().getAttributeDescriptors()) {
					// the null case should only happen for geometry
					if (copy.getFeatureType().getDescriptor(
							attrD.getName()) != null) {
						copy.setAttribute(
								attrD.getName(),
								feature.getAttribute(attrD.getName()));
					}
				}
				// shape files force geometry name to be 'the_geom'. So isolate
				// this change
				copy.setDefaultGeometry(feature.getDefaultGeometry());
				writer.write();
			}
		}
		catch (final Exception problem) {
			problem.printStackTrace();
			transaction.rollback();
		}
		finally {
			transaction.commit();
			transaction.close();
		}

	}

}
