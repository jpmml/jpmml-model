<?xml version="1.0" ?>
<!-- 
Copyright (c) 2022 Villu Ruusmann
-->
<xsl:stylesheet version="1.0" xmlns="http://www.dmg.org/PMML-4_4" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!--
	Allow empty collections
	-->
	<xsl:template match="xs:element[@name='DataDictionary']/xs:complexType/xs:sequence/xs:element[@ref='DataField']">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
			<xsl:attribute name="minOccurs">0</xsl:attribute>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:element[@name='MiningSchema']/xs:complexType/xs:sequence/xs:element[@ref='MiningField']">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
			<xsl:attribute name="minOccurs">0</xsl:attribute>
		</xsl:copy>
	</xsl:template>

	<!--
	Replace required with optional
	-->
	<xsl:template match="xs:element[@name='BayesOutput']/xs:complexType/xs:attribute[@name='fieldName']/@use">
	</xsl:template>

	<xsl:template match="xs:element[@name='ScoreDistribution']/xs:complexType/xs:attribute[@name='recordCount']/@use">
	</xsl:template>

	<!--
	Replace xs:string with enum
	-->
	<xsl:template match="xs:element[@name='OutputField']/xs:complexType/xs:attribute[@name='isMultiValued']">
		<xs:attribute name="isMultiValued" default="0">
			<xs:simpleType>
				<xs:restriction base="xs:string">
					<xs:enumeration value="0"/>
					<xs:enumeration value="1"/>
				</xs:restriction>
			</xs:simpleType>
		</xs:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='SetPredicate']/xs:complexType/xs:attribute[@name='operator']">
		<xs:attribute name="operator" fixed="supersetOf">
			<xs:simpleType>
				<xs:restriction base="xs:string">
					<xs:enumeration value="supersetOf"/>
				</xs:restriction>
			</xs:simpleType>
		</xs:attribute>
	</xsl:template>

	<!--
	Replace FIELD-NAME with xs:string
	-->
	<xsl:template match="xs:element[@name='PredictorTerm']/xs:complexType/xs:attribute[@name='name']/@type">
		<xsl:attribute name="type">xs:string</xsl:attribute>
	</xsl:template>

	<!--
	Replace xs:string with xs:anySimpleType where appropriate
	-->
	<xsl:template match="xs:element[@name='Constant' or @name='MatCell']/xs:complexType/xs:simpleContent/xs:extension/@base">
		<xsl:attribute name="base">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='SupportVectorMachine']/xs:complexType/xs:attribute[@name='alternateTargetCategory']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='SupportVectorMachineModel']/xs:complexType/xs:attribute[@name='alternateBinaryTargetCategory']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='DiscretizeBin']/xs:complexType/xs:attribute[@name='binValue']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='MultivariateStat']/xs:complexType/xs:attribute[@name='category']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='Node']/xs:complexType/xs:attribute[@name='defaultChild' or @name='id']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='RuleSet']/xs:complexType/xs:attribute[@name='defaultScore']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='Apply' or @name='Discretize' or @name='MapValues']/xs:complexType/xs:attribute[@name='defaultValue']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='Apply' or @name='Discretize' or @name='FieldRef' or @name='MapValues']/xs:complexType/xs:attribute[@name='mapMissingTo']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='MiningField']/xs:complexType/xs:attribute[@name='missingValueReplacement' or @name='invalidValueReplacement']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='Node' or @name='SimpleRule']/xs:complexType/xs:attribute[@name='score']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='MultivariateStats' or @name='PCell' or @name='PCovCell' or @name='PPCell' or @name='RegressionTable' or @name='SupportVectorMachine']/xs:complexType/xs:attribute[@name='targetCategory']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='GeneralRegressionModel']/xs:complexType/xs:attribute[@name='targetReferenceCategory']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='BaselineStratum' or @name='CategoricalPredictor' or @name='Category' or @name='Decision' or @name='FieldValue' or @name='FieldValueCount' or @name='NormDiscrete' or @name='OutputField' or @name='PairCounts' or @name='ParentValue' or @name='PPCell' or @name='ResultField' or @name='ScoreDistribution' or @name='SimplePredicate' or @name='TargetValue' or @name='TargetValueCount' or @name='TargetValueStat' or @name='Value' or @name='ValueProbability']/xs:complexType/xs:attribute[@name='value']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<!--
	Unify count attributes to xs:nonNegativeInteger
	-->
	<xsl:template match="xs:attribute[starts-with(@name, 'numberOf')]/@type">
		<xsl:attribute name="type">xs:nonNegativeInteger</xsl:attribute>
	</xsl:template>

	<!--
	Replace PROB-NUMBER with NUMBER where appropriate
	-->
	<xsl:template match="xs:element[@name='ScoreDistribution']/xs:complexType/xs:attribute[@name='confidence']/@type">
		<xsl:attribute name="type">NUMBER</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='MiningField' or @name='MultivariateStat']/xs:complexType/xs:attribute[@name='importance']/@type">
		<xsl:attribute name="type">NUMBER</xsl:attribute>
	</xsl:template>

	<!--
	Replace xs:integer with INT-NUMBER
	-->
	<xsl:template match="xs:attribute[@type='xs:integer']/@type">
		<xsl:attribute name="type">INT-NUMBER</xsl:attribute>
	</xsl:template>

	<!--
	Replace xs:float and xs:double with REAL-NUMBER
	-->
	<xsl:template match="xs:attribute[@type='xs:float' or @type='xs:double']/@type">
		<xsl:attribute name="type">REAL-NUMBER</xsl:attribute>
	</xsl:template>
</xsl:stylesheet>