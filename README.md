JPMML-Model [![Build Status](https://travis-ci.org/jpmml/jpmml-model.png?branch=master)](https://travis-ci.org/jpmml/jpmml-model)
===========

Java Class Model API for Predictive Model Markup Language (PMML).

# Features #

* Full support for PMML 3.0, 3.1, 3.2, 4.0, 4.1 and 4.2 schemas:
  * Class hierarchy.
  * Schema version annotations.
* Fluent API:
  * Value constructors.
* SAX Locator information
* [Visitor pattern] (http://en.wikipedia.org/wiki/Visitor_pattern):
  * Validation agents.
  * Optimization and transformation agents.

# Installation #

JPMML-Model library JAR files (together with accompanying Java source and Javadocs JAR files) are released via [Maven Central Repository] (http://repo1.maven.org/maven2/org/jpmml/). Please join the [JPMML mailing list] (https://groups.google.com/forum/#!forum/jpmml) for release announcements.

The current version is **1.1.5** (1 July, 2014).

```xml
<!-- Class model classes -->
<dependency>
	<groupId>org.jpmml</groupId>
	<artifactId>pmml-model</artifactId>
	<version>1.1.5</version>
</dependency>
<!-- Class model annotations -->
<dependency>
	<groupId>org.jpmml</groupId>
	<artifactId>pmml-schema</artifactId>
	<version>1.1.5</version>
</dependency>
```

# Usage #

The class model consists of two types of classes. There is a small number of manually crafted classes that are used for structuring the class hierarchy. They are permanently stored in the Java sources directory `/pmml-model/src/main/java`. Additionally, there is a much greater number of automatically generated classes that represent actual PMML elements. They can be found in the generated Java sources directory `/pmml-model/target/generated-sources/xjc` after a successful build operation.

All class model classes descend from class `org.dmg.pmml.PMMLObject`. Additional class hierarchy levels, if any, represent common behaviour and/or features. For example, all model classes descend from class `org.dmg.pmml.Model`.

There is not much documentation accompanying class model classes. The application developer should consult with the [PMML specification] (http://www.dmg.org/v4-1/GeneralStructure.html) about individual PMML elements and attributes.

### Unmarshalling ###

Load any PMML schema version 3.X or 4.X document into live `org.dmg.pmml.PMML` instance:

```java
InputStream is = ...

InputSource source = new InputSource(is);

// Use SAX filtering to transform PMML schema version 3.X and 4.X documents to PMML schema version 4.2 document
SAXSource transformedSource = ImportFilter.apply(source);

PMML pmml = JAXBUtil.unmarshalPMML(transformedSource);
```

### Applying visitors ###

Deleting SAX Locator information from the class model:
```java
PMML pmml = ...

pmml.apply(new SourceLocationNullifier());
```

### Marshalling ###

Store live `org.dmg.pmml.PMML` instance into PMML schema version 4.2 document:

```java
PMML pmml = ...

OutputStream os = ...

StreamResult result = new StreamResult(os);

JAXBUtil.marshalPMML(pmml, result);
```

# Example applications #

Module `pmml-model-example` exemplifies the use of JPMML-Model library.

This module can be built using [Apache Maven] (http://maven.apache.org/):
```
mvn clean install
```

The resulting uber-JAR file `target/example-1.1-SNAPSHOT.jar` contains the following command-line applications:
* `org.jpmml.model.CopyExample` [(source)] (https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/CopyExample.java). Transforms a PMML schema version 3.X or 4.X document to a PMML schema version 4.2 document.
* `org.jpmml.model.SegmentationOutputExample` [(source)] (https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/SegmentationOutputExample.java). Extends the Output element of a segmentation model with OutputField elements that expose the predicted values of individual segments.
* `org.jpmml.model.GolfingTreeModelExample` [(source)] (https://github.com/jpmml/jpmml-model/blob/master/pmml-model-example/src/main/java/org/jpmml/model/GolfingTreeModelExample.java). Produces a TreeModel for the "golfing" exercise.

For example, transforming `input.pmml` to `output.pmml`:
```
java -cp target/example-1.1-SNAPSHOT.jar org.jpmml.model.CopyExample --input input.pmml --output output.pmml
```

# License #

JPMML-Model is licensed under the [BSD 3-Clause License] (http://opensource.org/licenses/BSD-3-Clause).

# Additional information #

Please contact [info@openscoring.io] (mailto:info@openscoring.io)