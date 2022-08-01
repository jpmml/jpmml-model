/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;
import org.jpmml.model.ReflectionUtil;
import org.xml.sax.Locator;

@XmlAccessorType (
	value = XmlAccessType.FIELD
)
@XmlTransient
@JsonAutoDetect (
	fieldVisibility = JsonAutoDetect.Visibility.ANY,
	getterVisibility = JsonAutoDetect.Visibility.NONE,
	isGetterVisibility = JsonAutoDetect.Visibility.NONE,
	setterVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonInclude (
	value = JsonInclude.Include.NON_EMPTY
)
abstract
public class PMMLObject implements HasLocator, Serializable, Visitable {

	@XmlTransient
	@org.glassfish.jaxb.core.annotation.XmlLocation
	@org.eclipse.persistence.oxm.annotations.XmlLocation
	@JsonIgnore
	private Locator locator;


	@Override
	public boolean hasLocator(){
		return (this.locator != null);
	}

	@Override
	public Locator getLocator(){
		return this.locator;
	}

	@Override
	public void setLocator(Locator locator){
		this.locator = locator;
	}

	static
	public int[] getSchemaVersion(){
		return getSchemaVersion(PMML.class);
	}

	static
	public int[] getSchemaVersion(Class<? extends PMMLObject> clazz){
		long serialVersionUID;

		try {
			java.lang.reflect.Field serialVersionUIDField = ReflectionUtil.getSerialVersionUIDField(clazz);

			if(!serialVersionUIDField.isAccessible()){
				serialVersionUIDField.setAccessible(true);
			}

			serialVersionUID = (long)serialVersionUIDField.getLong(null);
		} catch(ReflectiveOperationException roe){
			throw new RuntimeException(roe);
		}

		int major = (int)((serialVersionUID >> 24) & 0xFF);
		int minor = (int)((serialVersionUID >> 16) & 0xFF);
		int patch = (int)((serialVersionUID >> 8) & 0xFF);

		int implementation = (int)(serialVersionUID & 0xFF);

		return new int[]{
			major, minor, patch,
			implementation
		};
	}

	static
	protected VisitorAction traverse(Visitor visitor, Visitable first){

		if(first != null){
			return first.accept(visitor);
		}

		return VisitorAction.CONTINUE;
	}

	static
	protected VisitorAction traverse(Visitor visitor, Visitable first, Visitable second){

		if(first != null){
			VisitorAction status = first.accept(visitor);

			if(status != VisitorAction.CONTINUE){
				return status;
			}
		} // End if

		if(second != null){
			return second.accept(visitor);
		}

		return VisitorAction.CONTINUE;
	}

	static
	protected VisitorAction traverse(Visitor visitor, Visitable... objects){

		for(int i = 0, max = objects.length; i < max; i++){
			Visitable visitable = objects[i];

			if(visitable != null){
				VisitorAction status = visitable.accept(visitor);

				if(status != VisitorAction.CONTINUE){
					return status;
				}
			}
		}

		return VisitorAction.CONTINUE;
	}

	static
	protected VisitorAction traverse(Visitor visitor, List<? extends Visitable> objects){

		for(int i = 0, max = objects.size(); i < max; i++){
			Visitable visitable = objects.get(i);

			if(visitable != null){
				VisitorAction status = visitable.accept(visitor);

				if(status != VisitorAction.CONTINUE){
					return status;
				}
			}
		}

		return VisitorAction.CONTINUE;
	}

	static
	protected VisitorAction traverseMixed(Visitor visitor, List<?> objects){

		for(int i = 0, max = objects.size(); i < max; i++){
			Object object = objects.get(i);

			if(object instanceof Visitable){
				Visitable visitable = (Visitable)object;

				VisitorAction status = visitable.accept(visitor);

				if(status != VisitorAction.CONTINUE){
					return status;
				}
			}
		}

		return VisitorAction.CONTINUE;
	}
}