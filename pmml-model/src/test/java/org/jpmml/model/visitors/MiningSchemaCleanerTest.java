/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.stream.StreamSource;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.PMML;
import org.dmg.pmml.RegressionModel;
import org.dmg.pmml.Segment;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.FieldNameUtil;
import org.jpmml.model.JAXBUtil;
import org.jpmml.model.PMMLUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiningSchemaCleanerTest {

	@Test
	public void clean() throws Exception {
		PMML pmml;

		try(InputStream is = PMMLUtil.getResourceAsStream(FieldResolverTest.class)){
			pmml = JAXBUtil.unmarshalPMML(new StreamSource(is));
		}

		MiningSchemaCleaner cleaner = new MiningSchemaCleaner();
		cleaner.applyTo(pmml);

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(RegressionModel regressionModel){
				MiningSchema miningSchema = regressionModel.getMiningSchema();

				Segment segment = (Segment)VisitorUtil.getParent(this);

				String id = segment.getId();

				if("first".equals(id)){
					checkMiningSchema(FieldNameUtil.create("x1"), miningSchema);
				} else

				if("second".equals(id)){
					checkMiningSchema(FieldNameUtil.create("x2"), miningSchema);
				} else

				if("third".equals(id)){
					checkMiningSchema(FieldNameUtil.create("x3"), miningSchema);
				} else

				if("sum".equals(id)){
					checkMiningSchema(FieldNameUtil.create("y", "first_output", "second_output", "third_output"), miningSchema);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(regressionModel);
			}
		};

		visitor.applyTo(pmml);
	}

	static
	private void checkMiningSchema(Set<FieldName> names, MiningSchema miningSchema){
		Set<FieldName> fieldNames = new LinkedHashSet<>();

		List<MiningField> miningFields = miningSchema.getMiningFields();
		for(MiningField miningField : miningFields){
			FieldName name = miningField.getName();

			fieldNames.add(name);
		}

		assertEquals(names, fieldNames);
	}
}