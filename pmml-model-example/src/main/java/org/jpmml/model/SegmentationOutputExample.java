/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.*;

import org.dmg.pmml.*;

public class SegmentationOutputExample extends TransformationExample {

	static
	public void main(String... args) throws Exception {
		execute(SegmentationOutputExample.class, args);
	}

	@Override
	public void transform(PMML pmml){
		List<Model> models = pmml.getModels();

		for(Model model : models){

			if(model instanceof MiningModel){
				MiningModel miningModel = (MiningModel)model;

				transform(miningModel);
			}
		}
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

			model.withOutput(output);
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
				.withName(new FieldName("segment_" + id))
				.withFeature(ResultFeatureType.PREDICTED_VALUE)
				.withSegmentId(id);

			output.withOutputFields(outputField);
		}
	}
}