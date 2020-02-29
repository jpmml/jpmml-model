JPMML-Model [![Build Status](https://travis-ci.org/jpmml/jpmml-model.png?branch=master)](https://travis-ci.org/jpmml/jpmml-model)
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
  * Google Web Toolkit (GWT).
* Supported JAXB runtimes:
  * [GlassFish Metro](https://metro.java.net)
  * [EclipseLink MOXy](https://www.eclipse.org/eclipselink)
* Supported SerDe runtimes:
  * [FasterXML Jackson](https://github.com/FasterXML/jackson)

# Prerequisites #

* Java 1.8 or newer.

# Installation #

JPMML-Model library JAR files (together with accompanying Java source and Javadocs JAR files) are released via [Maven Central Repository](https://repo1.maven.org/maven2/org/jpmml/).

The current version is **1.5.0** (29 February, 2020).

```xml
<dependency>
	<groupId>org.jpmml</groupId>
	<artifactId>pmml-model</artifactId>
	<version>1.5.0</version>
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

# Example applications #

Module `pmml-model-example` exemplifies the use of JPMML-Model library.

This module can be built using [Apache Maven](https://maven.apache.org/):
```
mvn clean install
```

The resulting uber-JAR file `target/pmml-model-example-executable-1.5-SNAPSHOT.jar` contains the following command-line applications:
* `org.jpmml.model.example.CopyExample` [(source)](https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/example/CopyExample.java). Copies and transforms a PMML schema version 3.X or 4.X document to a PMML schema version 4.4 document.
* `org.jpmml.model.example.ObfuscationExample` [(source)](https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/example/ObfuscationExample.java). Obfuscates a PMML document by replacing field names with their MD5 hashes.
* `org.jpmml.model.example.TranslationExample` [(source)](https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/example/TranslationExample.java). Translates a PMML document to a JSON or YAML document.
* `org.jpmml.model.example.ValidationExample` [(source)](https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/example/ValidationExample.java). Validates a PMML document against the built-in XML Schema Definition (XSD) resource.

Copying `input.pmml` to `output.pmml`; the size of the class model is estimated using the Java agent technology:
```
java -javaagent:../pmml-agent/target/pmml-agent-1.5-SNAPSHOT.jar -cp target/pmml-model-example-executable-1.5-SNAPSHOT.jar org.jpmml.model.example.CopyExample --summary true --input input.pmml --output output.pmml
```

Checking the validity of `model.pmml`:
```
java -cp target/pmml-model-example-executable-1.5-SNAPSHOT.jar org.jpmml.model.example.ValidationExample --input model.pmml
```

Getting help:
```
java -cp target/pmml-model-example-executable-1.5-SNAPSHOT.jar <application class name> --help
```

It is possible to activate a specific Java XML Binding (JAXB) runtime by setting the value of the `javax.xml.bind.context.factory` system property. Use `com.sun.xml.bind.v2.ContextFactory` for activating a GlassFish Metro runtime, and `org.eclipse.persistence.jaxb.JAXBContextFactory` for activating an EclipseLink MOXy runtime.

For example:
```
java -Djavax.xml.bind.context.factory=org.eclipse.persistence.jaxb.JAXBContextFactory -cp target/pmml-model-example-executable-1.5-SNAPSHOT.jar ...
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

Interested in using JPMML software in your application? Please contact [info@openscoring.io](mailto:info@openscoring.io)
