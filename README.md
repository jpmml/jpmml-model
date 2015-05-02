JPMML-Model [![Build Status](https://travis-ci.org/jpmml/jpmml-model.png?branch=master)](https://travis-ci.org/jpmml/jpmml-model)
===========

Java Class Model API for Predictive Model Markup Language (PMML).

# Features #

* Full support for PMML 3.0, 3.1, 3.2, 4.0, 4.1 and 4.2 schemas:
  * Schema version annotations.
* Enhanced API:
  * Class hierarchy.
  * Value constructors.
  * Method chaining.
* [Visitor pattern] (http://en.wikipedia.org/wiki/Visitor_pattern):
  * Validation agents.
  * Optimization and transformation agents.
* Memory efficient:
  * Optional SAX Locator information
* GWT compatible

# Prerequisites #

* Java 1.7 or newer

# Installation #

JPMML-Model library JAR files (together with accompanying Java source and Javadocs JAR files) are released via [Maven Central Repository] (http://repo1.maven.org/maven2/org/jpmml/). Please join the [JPMML mailing list] (https://groups.google.com/forum/#!forum/jpmml) for release announcements.

The current version is **1.2.0** (7 April, 2015).

```xml
<dependency>
	<groupId>org.jpmml</groupId>
	<artifactId>pmml-model</artifactId>
	<version>1.2.0</version>
</dependency>
```

# Usage #

The class model consists of two types of classes. There is a small number of manually crafted classes that are used for structuring the class hierarchy. They are permanently stored in the Java sources directory `/pmml-model/src/main/java`. Additionally, there is a much greater number of automatically generated classes that represent actual PMML elements. They can be found in the generated Java sources directory `/pmml-model/target/generated-sources/xjc` after a successful build operation.

All class model classes descend from class `org.dmg.pmml.PMMLObject`. Additional class hierarchy levels, if any, represent common behaviour and/or features. For example, all model classes descend from class `org.dmg.pmml.Model`.

The class model should be self-explanatory. The application developer is advised to consult with the latest [PMML specification] (http://www.dmg.org/v4-2-1/GeneralStructure.html) about the specifics of individual PMML elements and attributes.

### Unmarshalling ###

Load any PMML schema version 3.X or 4.X document into live `org.dmg.pmml.PMML` instance:

```java
public PMML load(InputStream is) throws Exception {
  InputSource source = new InputSource(is);

  // Use SAX filtering to transform PMML schema version 3.X and 4.X documents to PMML schema version 4.2 document
  SAXSource transformedSource = ImportFilter.apply(source);

  return JAXBUtil.unmarshalPMML(transformedSource);
}
```

**Important**: It is the responsibility of the application developer to ensure that the XML document does not contain malicious content (eg. XEE and XXE attacks).

### Applying visitors ###

Delete SAX Locator information from the class model:

```java
public void optimize(PMML pmml){
  LocatorNullifier nullifier = new LocatorNullifier();
  nullifier.applyTo(pmml);
}
```

### Marshalling ###

Store live `org.dmg.pmml.PMML` instance into PMML schema version 4.2 document:

```java
public void store(PMML pmml, OutputStream os) throws Exception {
  StreamResult result = new StreamResult(os);

  JAXBUtil.marshalPMML(pmml, result);
}
```

# Example applications #

Module `pmml-model-example` exemplifies the use of JPMML-Model library.

This module can be built using [Apache Maven] (http://maven.apache.org/):
```
mvn clean install
```

The resulting uber-JAR file `target/example-1.2-SNAPSHOT.jar` contains the following command-line applications:
* `org.jpmml.model.CopyExample` [(source)] (https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/CopyExample.java). Transforms a PMML schema version 3.X or 4.X document to a PMML schema version 4.2 document.
* `org.jpmml.model.ValidationExample` [(source)] (https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/ValidationExample.java). Validates a PMML schema version 3.X or 4.X document against the built-in XML Schema Definition (XSD) resource.
* `org.jpmml.model.SegmentationOutputExample` [(source)] (https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/SegmentationOutputExample.java). Extends the Output element of a segmentation model with OutputField elements that expose the predicted values of individual segments.
* `org.jpmml.model.GolfingTreeModelExample` [(source)] (https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/GolfingTreeModelExample.java). Produces a TreeModel for the "golfing" exercise.

For example, checking the validity of `model.pmml`:
```
java -cp target/example-1.2-SNAPSHOT.jar org.jpmml.model.ValidationExample --input model.pmml
```

# License #

JPMML-Model is licensed under the [BSD 3-Clause License] (http://opensource.org/licenses/BSD-3-Clause).

# Additional information #

Please contact [info@openscoring.io] (mailto:info@openscoring.io)