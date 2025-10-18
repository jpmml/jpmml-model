JPMML-Model [![Build Status](https://github.com/jpmml/jpmml-model/workflows/maven/badge.svg)](https://github.com/jpmml/jpmml-model/actions?query=workflow%3A%22maven%22)
===========

Java Class Model API for Predictive Model Markup Language (PMML).

# Features #

* Full support for PMML 3.0, 3.1, 3.2, 4.0, 4.1, 4.2, 4.3 and 4.4 schemas:
  * Schema version annotations.
  * Extension elements, attributes, enum values.
* Enhanced API:
  * Class hierarchy.
  * Marker interfaces for common traits.
  * Value constructors.
  * Method chaining-friendly setter methods.
  * Optional SAX Locator information.
* [Visitor pattern](https://en.wikipedia.org/wiki/Visitor_pattern):
  * Validation agents.
  * Optimization and transformation agents.
* Supported platforms:
  * Java SE and EE.
  * Android.
* Supported JAXB runtimes:
  * [GlassFish Metro](https://metro.java.net)
  * [EclipseLink MOXy](https://www.eclipse.org/eclipselink)
* Supported SerDe runtimes:
  * [Kryo](https://github.com/EsotericSoftware/kryo)
  * [FasterXML Jackson](https://github.com/FasterXML/jackson)
* Supported cross-compiler runtimes:
  * [TeaVM](https://teavm.org)

# Prerequisites #

* Java 11 or newer.

# Installation #

JPMML-Model library JAR files (together with accompanying Java source and Javadocs JAR files) are released via the Maven Central Repository.

The current version is **1.7.4** (3 July, 2025).

```xml
<dependency>
	<groupId>org.jpmml</groupId>
	<artifactId>pmml-model</artifactId>
	<version>1.7.4</version>
</dependency>
```

# Usage #

The class model consists of two types of classes. There is a small number of manually crafted classes that are used for structuring the class hierarchy. They are permanently stored in the Java sources directory `/pmml-model/src/main/java`. Additionally, there is a much larger number of automatically generated classes that represent actual PMML elements. They can be found in the generated Java sources directory `/pmml-model/target/generated-sources/xjc` after a successful build operation.

All class model classes descend from the `org.dmg.pmml.PMMLObject` base class. Additional class hierarchy levels, if any, represent common state and/or behaviour. For example, all model classes descend from the `org.dmg.pmml.Model` base class.

The class model should be self-explanatory. The application developer is advised to consult with the latest [PMML specification](https://dmg.org/pmml/v4-4-1/GeneralStructure.html) about the specifics of individual PMML elements and attributes.

### Unmarshalling ###

Loading a PMML schema version 3.X or 4.X document into a live `org.dmg.pmml.PMML` object:

```java
public PMML load(InputStream is) throws SAXException, JAXBException {
	return org.jpmml.model.PMMLUtil.unmarshal(is);
}
```

**Important**: It is the responsibility of the application developer to ensure that the XML document does not contain malicious content (eg. XEE and XXE attacks).

### Applying visitors ###

Deleting SAX Locator information from the class model:

```java
public void optimize(PMML pmml){
	var visitor = new org.jpmml.model.visitors.LocatorNullifier();

	visitor.applyTo(pmml);
}
```

### Marshalling ###

Storing a live `org.dmg.pmml.PMML` object into a PMML schema version 4.4 document:

```java
public void store(PMML pmml, OutputStream os) throws JAXBException {
	org.jpmml.model.PMMLUtil.marshal(pmml, os);
}
```

# License #

JPMML-Model is licensed under the [BSD 3-Clause License](https://opensource.org/licenses/BSD-3-Clause).

# Additional information #

JPMML-Model is developed and maintained by Openscoring Ltd, Estonia.

Interested in using [Java PMML API](https://github.com/jpmml) software in your company? Please contact [info@openscoring.io](mailto:info@openscoring.io)
