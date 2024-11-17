/*
 * Copyrght (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.ClusteringModelQuality;
import org.dmg.pmml.Extension;
import org.dmg.pmml.Header;
import org.dmg.pmml.ModelExplanation;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.dmg.pmml.clustering.ClusteringModel;
import org.dmg.pmml.clustering.PMMLAttributes;
import org.jpmml.model.ReflectionUtil;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class VersionDowngraderTest {

	@Test
	public void downgrade(){
		Extension extension = new Extension();

		ClusteringModelQuality clusteringModelQuality = new ClusteringModelQuality()
			.addExtensions(extension);

		ModelExplanation modelExplanation = new ModelExplanation()
			.addClusteringModelQualities(clusteringModelQuality);

		ClusteringModel clusteringModel = new ClusteringModel()
			.setScorable(false)
			.setModelExplanation(modelExplanation);

		Header header = new Header()
			.setModelVersion("1.0");

		PMML pmml = new PMML()
			.setHeader(header)
			.addModels(clusteringModel);

		VersionInspector inspector;

		try {
			inspector = new VersionDowngrader(Version.XPMML);

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}

		inspector = new VersionDowngrader(Version.PMML_4_4);
		inspector.applyTo(pmml);

		assertTrue(clusteringModelQuality.hasExtensions());

		inspector = new VersionDowngrader(Version.PMML_4_3);
		inspector.applyTo(pmml);

		assertFalse(clusteringModelQuality.hasExtensions());

		assertNotNull(header.getModelVersion());

		inspector = new VersionDowngrader(Version.PMML_4_1);
		inspector.applyTo(pmml);

		assertNull(header.getModelVersion());

		assertFalse(clusteringModel.isScorable());

		inspector = new VersionDowngrader(Version.PMML_4_0);
		inspector.applyTo(pmml);

		assertTrue(clusteringModel.isScorable());

		assertNull(ReflectionUtil.getFieldValue(PMMLAttributes.CLUSTERINGMODEL_SCORABLE, clusteringModel));

		assertNotNull(clusteringModel.getModelExplanation());

		inspector = new VersionDowngrader(Version.PMML_3_2);
		inspector.applyTo(pmml);

		assertNull(clusteringModel.getModelExplanation());
	}
}