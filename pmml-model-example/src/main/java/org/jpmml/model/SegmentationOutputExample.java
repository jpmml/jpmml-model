/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.List;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.MiningModel;
import org.dmg.pmml.Model;
import org.dmg.pmml.Output;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMML;
import org.dmg.pmml.ResultFeatureType;
import org.dmg.pmml.Segment;
import org.dmg.pmml.Segmentation;

public class SegmentationOutputExample extends TransformationExample {

	static
	public void main(String... args) throws Exception {
		execute(SegmentationOutputExample.class, args);
	}

	@Override
	public PMML transform(PMML pmml){
		List<Model> models = pmml.getModels();

		for(Model model : models){

			if(model instanceof MiningModel){
				MiningModel miningModel = (MiningModel)model;

				transform(miningModel);
			}
		}

		return pmml;
	}

	private void transform(MiningModel model){
		Segmentation segmentation = model.getSegmentation();
		if(segmentation == null){
			return;
		}

		Output output = model.getOutput();

		// If the Output element is missing, then create one
		if(output == null){
			output = new Output();

			model.setOutput(output);
		}

		List<Segment> segments = segmentation.getSegments();
		for(int i = 0; i < segments.size(); i++){
			Segment segment = segments.get(i);

			String id = segment.getId();

			// If the Segment id attribute is missing, then use an implicit 1-based index
			if(id == null){
				id = String.valueOf(i + 1);
			}

			OutputField outputField = new OutputField()
				.setName(FieldName.create("segment_" + id))
				.setResultFeature(ResultFeatureType.PREDICTED_VALUE)
				.setSegmentId(id);

			output.addOutputFields(outputField);
		}
	}
}