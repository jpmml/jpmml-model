/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Objects;

import org.dmg.pmml.Apply;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLAttributes;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.TargetValue;
import org.dmg.pmml.Version;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.time_series.TrendExpoSmooth;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.UnsupportedAttributeException;
import org.jpmml.model.UnsupportedElementException;
import org.jpmml.model.annotations.Added;
import org.jpmml.model.annotations.Optional;
import org.jpmml.model.annotations.Removed;
import org.jpmml.model.annotations.Required;
import org.jpmml.model.filters.ExportFilter;

/**
 * <p>
 * A Visitor that downgrades a class model object from the latest PMML schema version to some earlier one.
 * </p>
 *
 * @see ExportFilter
 */
public class VersionDowngrader extends VersionInspector {

	private Version version = null;


	public VersionDowngrader(Version version){
		this.version = Objects.requireNonNull(version);

		if(!version.isStandard()){
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void handleAdded(PMMLObject object, AnnotatedElement element, Added added){
		Version version = added.value();

		if(version.isStandard() && version.compareTo(this.version) > 0){

			if(element instanceof Class){
				// Ignored
			} else

			if(element instanceof Field){
				Field field = (Field)element;

				if(added.removable()){
					ReflectionUtil.setFieldValue(field, object, null);
				}
			} else

			{
				throw new IllegalArgumentException();
			}
		}
	}

	@Override
	public void handleRemoved(PMMLObject object, AnnotatedElement element, Removed removed){
	}

	@Override
	public void handleOptional(PMMLObject object, AnnotatedElement element, Optional optional){
	}

	@Override
	public void handleRequired(PMMLObject object, AnnotatedElement element, Required required){
	}

	@Override
	public VisitorAction visit(Apply apply){
		Object defaultValue = apply.getDefaultValue();

		if(defaultValue != null){

			if(this.version.compareTo(Version.PMML_4_1) == 0){
				Object mapMissingTo = apply.getMapMissingTo();

				if(mapMissingTo != null){
					throw new UnsupportedAttributeException(apply, PMMLAttributes.APPLY_DEFAULTVALUE, defaultValue);
				}

				apply
					.setDefaultValue(null)
					.setMapMissingTo(defaultValue);
			}
		}

		return super.visit(apply);
	}

	@Override
	public VisitorAction visit(MiningField miningField){
		MiningField.UsageType usageType = miningField.getUsageType();

		switch(usageType){
			case TARGET:
				if(this.version.compareTo(Version.PMML_4_2) < 0){
					miningField.setUsageType(MiningField.UsageType.PREDICTED);
				}
				break;
			default:
				break;
		}

		return super.visit(miningField);
	}

	@Override
	public VisitorAction visit(PMML pmml){
		pmml.setVersion(this.version.getVersion());

		return super.visit(pmml);
	}

	@Override
	public VisitorAction visit(TargetValue targetValue){
		String displayValue = targetValue.getDisplayValue();

		if(displayValue != null){

			if(this.version.compareTo(Version.PMML_3_2) <= 0){
				throw new UnsupportedAttributeException(targetValue, PMMLAttributes.TARGETVALUE_DISPLAYVALUE, displayValue);
			}
		}

		return super.visit(targetValue);
	}

	@Override
	public VisitorAction visit(TrendExpoSmooth trendExpoSmooth){

		if(this.version.compareTo(Version.PMML_4_0) == 0){
			throw new UnsupportedElementException(trendExpoSmooth);
		}

		return super.visit(trendExpoSmooth);
	}
}