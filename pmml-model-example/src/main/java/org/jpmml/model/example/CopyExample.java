/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.example;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.beust.jcommander.Parameter;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Visitor;
import org.jpmml.model.visitors.MemoryMeasurer;

public class CopyExample extends TransformationExample {

	@Parameter (
		names = {"--visitor-classes"},
		description = "List of Visitor class names"
	)
	private List<String> visitorClasses = new ArrayList<>();

	@Parameter (
		names = {"--summary"},
		description = "Print class model object summary. Requires JPMML agent",
		arity = 1
	)
	private boolean summary = false;


	static
	public void main(String... args) throws Exception {
		execute(CopyExample.class, args);
	}

	@Override
	public PMML transform(PMML pmml) throws Exception {

		if(this.summary){
			printSummary(pmml);
		}

		List<String> visitorClasses = this.visitorClasses;
		for(String visitorClass : visitorClasses){
			Class<?> clazz = Class.forName(visitorClass);

			long begin = System.currentTimeMillis();

			Visitor visitor = (Visitor)clazz.newInstance();
			visitor.applyTo(pmml);

			long end = System.currentTimeMillis();

			System.out.println("Applied " + clazz.getName() + " in " + (end - begin) + " ms.");

			if(this.summary){
				printSummary(pmml);
			}
		}

		return pmml;
	}

	private void printSummary(PMML pmml){
		MemoryMeasurer measurer = new MemoryMeasurer();
		measurer.applyTo(pmml);

		NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
		numberFormat.setGroupingUsed(true);

		long size = measurer.getSize();
		System.out.println("Bytesize of the object graph: " + numberFormat.format(size));

		Set<Object> objects = measurer.getObjects();
		System.out.println("Number of distinct Java objects in the object graph: " + numberFormat.format(objects.size()));
	}
}