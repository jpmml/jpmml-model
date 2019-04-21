<?xml version="1.0" ?>
<!--
Copyright (c) 2016 Villu Ruusmann
-->
<xsl:stylesheet version="1.0" xmlns="http://www.dmg.org/PMML-4_3" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="common.xsl"/>

	<xsl:template name="extension">
		<xs:element ref="Extension" minOccurs="0" maxOccurs="unbounded"/> 
	</xsl:template>

	<xsl:template name="extension-comment">
		<xsl:comment> &lt;xs:element ref=&quot;Extension&quot; minOccurs=&quot;0&quot; maxOccurs=&quot;unbounded&quot;/&gt; </xsl:comment>
	</xsl:template>

	<!--
	Simplify Model type definitions by keeping the leading Extension list and commenting out the trailing Extension list
	-->
	<xsl:template match="xs:element[@ref='Extension'][position() &gt; 1 and position() = last()]">
		<xsl:call-template name="extension-comment"/>
	</xsl:template>

	<!--
	Simplify EmbeddedModel group definition from XSD sequence to XSD choice by commenting out the Extension list
	-->
	<xsl:template match="xs:group[@name='EmbeddedModel']/xs:sequence/xs:element[@ref='Extension']">
		<xsl:call-template name="extension-comment"/>
	</xsl:template>

	<!--
	Simplify Array type definition
	-->
	<xsl:template match="xs:element[@name='Array']">
	</xsl:template>

	<xsl:template match="xs:complexType[@name='ArrayType']">
		<xs:element name="Array">
			<xs:complexType>
				<xs:simpleContent>
					<xs:extension base="xs:anySimpleType">
						<xsl:copy-of select="*"/>
					</xs:extension>
				</xs:simpleContent>
			</xs:complexType>
		</xs:element>
	</xsl:template>

	<!--
	Simplify CONTINUOUS-DISTRIBUTION-TYPE group definition from XSD sequence to XSD choice by relocating the Extension list
	-->
	<xsl:template match="xs:group[@name='CONTINUOUS-DISTRIBUTION-TYPES']">
		<xsl:copy>
			<xsl:apply-templates select="@*|xs:sequence/xs:choice"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:element[@name='Alternate' or @name='Baseline']/xs:complexType">
		<xsl:copy>
			<xs:sequence>
				<xsl:call-template name="extension"/>
				<xsl:apply-templates select="@*|node()"/>
			</xs:sequence>
		</xsl:copy>
	</xsl:template>

	<!--
	Simplify REAL-SparseArray type definition
	-->
	<xsl:template match="xs:element[@name='REAL-SparseArray']/xs:complexType/xs:attribute[@name='defaultValue']/@type">
		<xsl:attribute name="type">xs:double</xsl:attribute>
	</xsl:template>

	<!--
	Add missing XML type information
	-->
	<xsl:template match="xs:element[@name='FieldValue' or @name='FieldValueCount']/xs:complexType/xs:attribute[@name='value']">
		<xs:attribute type="xs:string">
			<xsl:copy-of select="@*"/>
		</xs:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='OutputField']/xs:complexType/xs:attribute[@name='isMultiValued']">
		<xs:attribute type="xs:string">
			<xsl:copy-of select="@*"/>
		</xs:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='TimeAnchor' or @name='TimeCycle']/xs:complexType/xs:attribute[@name='displayName']">
		<xs:attribute type="xs:string">
			<xsl:copy-of select="@*"/>
		</xs:attribute>
	</xsl:template>

	<!--
	Add dummy timeseries algorithm types
	-->
	<xsl:template match="xs:element[@name='ARIMA' or @name='SeasonalTrendDecomposition' or @name='SpectralAnalysis']">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
			<xs:complexType>
			</xs:complexType>
		</xsl:copy>
	</xsl:template>

	<!--
	Replace xs:string with enum
	-->
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

	<xsl:template match="xs:element[@name='Discretize' or @name='MapValues']/xs:complexType/xs:attribute[@name='defaultValue']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='Node']/xs:complexType/xs:attribute[@name='defaultChild' or @name='id']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='Discretize' or @name='MapValues']/xs:complexType/xs:attribute[@name='mapMissingTo']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='MiningField']/xs:complexType/xs:attribute[@name='missingValueReplacement' or @name='x-invalidValueReplacement']/@type">
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

	<xsl:template match="xs:element[@name='BaselineStratum' or @name='CategoricalPredictor' or @name='Category' or @name='NormDiscrete' or @name='OutputField' or @name='PairCounts' or @name='ParentValue' or @name='PPCell' or @name='ResultField' or @name='ScoreDistribution' or @name='SimplePredicate' or @name='TargetValue' or @name='TargetValueCount' or @name='TargetValueStat' or @name='Value' or @name='ValueProbability']/xs:complexType/xs:attribute[@name='value']/@type">
		<xsl:attribute name="type">xs:anySimpleType</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='FieldValue' or @name='FieldValueCount']/xs:complexType/xs:attribute[@name='value']">
		<xs:attribute type="xs:anySimpleType">
			<xsl:copy-of select="@*"/>
		</xs:attribute>
	</xsl:template>

	<!--
	Replace FIELD-NAME with xs:string where appropriate
	-->
	<xsl:template match="xs:element[@name='Characteristic']/xs:complexType/xs:attribute[@name='name']/@type">
		<xsl:attribute name="type">xs:string</xsl:attribute>
	</xsl:template>

	<!--
	Replace xs:string with FIELD-NAME where appropriate
	-->
	<xsl:template match="xs:element[@name='BayesInput' or @name='BayesOutput']/xs:complexType/xs:attribute[@name='fieldName']/@type">
		<xsl:attribute name="type">FIELD-NAME</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='InstanceField' or @name='VerificationField']/xs:complexType/xs:attribute[@name='field']/@type">
		<xsl:attribute name="type">FIELD-NAME</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='NearestNeighborModel']/xs:complexType/xs:attribute[@name='instanceIdVariable']/@type">
		<xsl:attribute name="type">FIELD-NAME</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='ParameterField']/xs:complexType/xs:attribute[@name='name']/@type">
		<xsl:attribute name="type">FIELD-NAME</xsl:attribute>
	</xsl:template>

	<xsl:template match="xs:element[@name='PredictiveModelQuality']/xs:complexType/xs:attribute[@name='targetField']/@type">
		<xsl:attribute name="type">FIELD-NAME</xsl:attribute>
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