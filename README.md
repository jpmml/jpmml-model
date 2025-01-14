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

JPMML-Model library JAR files (together with accompanying Java source and Javadocs JAR files) are released via [Maven Central Repository](https://repo1.maven.org/maven2/org/jpmml/).

The current version is **1.6.11** (10 January, 2025).

```xml
<dependency>
	<groupId>org.jpmml</groupId>
	<artifactId>pmml-model</artifactId>
	<version>1.6.11</version>
</dependency>
```

# Usage #

The class model consists of two types of classes. There is a small number of manually crafted classes that are used for structuring the class hierarchy. They are permanently stored in the Java sources directory `/pmml-model/src/main/java`. Additionally, there is a much greater number of automatically generated classes that represent actual PMML elements. They can be found in the generated Java sources directory `/pmml-model/target/generated-sources/xjc` after a successful build operation.

All class model classes descend from class `org.dmg.pmml.PMMLObject`. Additional class hierarchy levels, if any, represent common behaviour and/or features. For example, all model classes descend from class `org.dmg.pmml.Model`.

The class model should be self-explanatory. The application developer is advised to consult with the latest [PMML specification](http://dmg.org/pmml/v4-3/GeneralStructure.html) about the specifics of individual PMML elements and attributes.

### Unmarshalling ###

Loading any PMML schema version 3.X or 4.X document into live `org.dmg.pmml.PMML` instance:

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
  LocatorNullifier nullifier = new LocatorNullifier();
  nullifier.applyTo(pmml);
}
```

### Marshalling ###

Storing live `org.dmg.pmml.PMML` instance into PMML schema version 4.4 document:

```java
public void store(PMML pmml, OutputStream os) throws JAXBException {
  org.jpmml.model.PMMLUtil.marshal(pmml, os);
}
```

# Documentation #

Current:

* [Troubleshooting PMML documents](https://openscoring.io/blog/2018/06/15/troubleshooting_pmml/)
* [Configuring JAXB dependency for Java SE versions 8, 9, 10 and 11](https://openscoring.io/blog/2019/02/28/jpmml_model_api_configuring_jaxb_dependency/)

Slightly outdated:

* [Converting PMML documents between different schema versions](https://openscoring.io/blog/2014/06/20/jpmml_model_api_import_export/)
* [Transforming and measuring the memory consumption of PMML class model objects using the Java agent technology](https://openscoring.io/blog/2015/02/06/jpmml_model_api_transform_measure/)
* [Extending PMML documents with custom XML content](https://openscoring.io/blog/2015/05/15/jpmml_model_api_vendor_extensions/)

# Support #

Limited public support is available via the [JPMML mailing list](https://groups.google.com/forum/#!forum/jpmml).

# License #

JPMML-Model is licensed under the [BSD 3-Clause License](https://opensource.org/licenses/BSD-3-Clause).

# Additional information #

JPMML-Model is developed and maintained by Openscoring Ltd, Estonia.

Interested in using [Java PMML API](https://github.com/jpmml) software in your company? Please contact [info@openscoring.io](mailto:info@openscoring.io)
