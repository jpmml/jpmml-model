<?xml version="1.0" ?>
<!--
Copyright (c) 2009 University of Tartu
-->
<xsl:stylesheet version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="2" xmlns:xalan="http://xml.apache.org/xslt"/>

	<xsl:strip-space elements="*"/>

	<xsl:include href="pmml-compatibility.xsl"/>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template name="extension">
		<xs:element ref="Extension" minOccurs="0" maxOccurs="unbounded"/> 
	</xsl:template>

	<xsl:template name="extension-comment">
		<xsl:comment> &lt;xs:element ref=&quot;Extension&quot; minOccurs=&quot;0&quot; maxOccurs=&quot;unbounded&quot;/&gt; </xsl:comment>
	</xsl:template>

	<xsl:template name="inline-enum">
		<xsl:param
			name="name"
		/>
		<xsl:copy>
			<xsl:apply-templates select="@*[name() != 'type']"/>
			<xs:simpleType>
				<xsl:copy-of select="//xs:simpleType[@name=$name]/node()"/>
			</xs:simpleType>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:schema">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
			<xsl:call-template name="compatibility-content"/>
		</xsl:copy>
	</xsl:template>

	<!--
	Ensure that the Extension list is on the first position
	-->
	<xsl:template match="xs:sequence">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="count(xs:element[@ref='Extension']) = 1">
					<xsl:apply-templates select="node()[@ref='Extension']"/>
					<xsl:apply-templates select="@*|node()[not(@ref='Extension')]"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="@*|node()"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:copy>
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
					<xs:extension base="xs:string">
						<xsl:copy-of select="*"/>
					</xs:extension>
				</xs:simpleContent>
			</xs:complexType>
		</xs:element>
	</xsl:template>

	<!--
	Restrict xs:string to FIELD-NAME where appropriate
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
	Declare dummy timeseries algorithm types
	-->
	<xsl:template match="xs:element[@name='ARIMA' or @name='SeasonalTrendDecomposition' or @name='SpectralAnalysis']">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
			<xs:complexType>
			</xs:complexType>
		</xsl:copy>
	</xsl:template>

	<!--
	Inline Delimiter enum types
	-->
	<xsl:template match="xs:element[@name='Delimiter']/xs:complexType/xs:attribute[@name='delimiter']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">DELIMITER</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='DELIMITER']">
	</xsl:template>

	<!--
	Inline GeneralRegressionModel enum types
	-->
	<xsl:template match="xs:element[@name='GeneralRegressionModel']/xs:complexType/xs:attribute[@name='cumulativeLink']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">CUMULATIVE-LINK-FUNCTION</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:element[@name='GeneralRegressionModel']/xs:complexType/xs:attribute[@name='linkFunction']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">LINK-FUNCTION</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='CUMULATIVE-LINK-FUNCTION' or @name='LINK-FUNCTION']">
	</xsl:template>

	<!--
	Inline MiningField enum types
	-->
	<xsl:template match="xs:element[@name='MiningField']/xs:complexType/xs:attribute[@name='usageType']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">FIELD-USAGE-TYPE</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='FIELD-USAGE-TYPE']">
	</xsl:template>

	<!--
	Inline NearestNeighborModel enum types
	-->
	<xsl:template match="xs:element[@name='NearestNeighborModel']/xs:complexType/xs:attribute[@name='categoricalScoringMethod']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">CAT-SCORING-METHOD</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:element[@name='NearestNeighborModel']/xs:complexType/xs:attribute[@name='continuousScoringMethod']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">CONT-SCORING-METHOD</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='CAT-SCORING-METHOD' or @name='CONT-SCORING-METHOD']">
	</xsl:template>

	<!--
	Inline OutputField enum types
	-->
	<xsl:template match="xs:element[@name='OutputField']/xs:complexType/xs:attribute[@name='ruleFeature']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">RULE-FEATURE</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='RULE-FEATURE']">
	</xsl:template>

	<!--
	Inline Segmentation enum types
	-->
	<xsl:template match="xs:element[@name='Segmentation']/xs:complexType/xs:attribute[@name='multipleModelMethod']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">MULTIPLE-MODEL-METHOD</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='MULTIPLE-MODEL-METHOD']">
	</xsl:template>

	<!--
	Inline SupportVectorMachineModel enum types
	-->
	<xsl:template match="xs:element[@name='SupportVectorMachineModel']/xs:complexType/xs:attribute[@name='classificationMethod']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">SVM-CLASSIFICATION-METHOD</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:element[@name='SupportVectorMachineModel']/xs:complexType/xs:attribute[@name='svmRepresentation']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">SVM-REPRESENTATION</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='SVM-CLASSIFICATION-METHOD' or @name='SVM-REPRESENTATION']">
	</xsl:template>

	<!--
	Inline TestDistributions enum types
	-->
	<xsl:template match="xs:element[@name='TestDistributions']/xs:complexType/xs:attribute[@name='testStatistic']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">BASELINE-TEST-STATISTIC</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='BASELINE-TEST-STATISTIC']">
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
	Replace xs:integer, xs:positiveInteger and xs:nonNegativeInteger with INT-NUMBER
	-->
	<xsl:template match="xs:attribute[@type='xs:integer' or @type='xs:positiveInteger' or @type='xs:nonNegativeInteger']/@type">
		<xsl:attribute name="type">INT-NUMBER</xsl:attribute>
	</xsl:template>

	<!-- 
	Replace xs:float and xs:double with NUMBER
	-->
	<xsl:template match="xs:attribute[@type='xs:float' or @type='xs:double']/@type">
		<xsl:attribute name="type">NUMBER</xsl:attribute>
	</xsl:template>

	<!--
	Replace xs:integer with xs:int
	-->
	<xsl:template match="xs:simpleType[@name='INT-NUMBER']/xs:restriction/@base">
		<xsl:attribute name="base">xs:int</xsl:attribute>
	</xsl:template>
</xsl:stylesheet>
