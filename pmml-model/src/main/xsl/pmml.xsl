<?xml version="1.0" ?>
<!--
Copyright (c) 2009 University of Tartu
-->
<xsl:stylesheet version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="2"/>

	<xsl:strip-space elements="*"/>

	<xsl:include href="pmml-compatibility.xsl"/>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template name="extension">
		<xsd:element ref="Extension" minOccurs="0" maxOccurs="unbounded"/> 
	</xsl:template>

	<xsl:template name="extension-comment">
		<xsl:comment> &lt;xs:element ref=&quot;Extension&quot; minOccurs=&quot;0&quot; maxOccurs=&quot;unbounded&quot;/&gt; </xsl:comment>
	</xsl:template>

	<xsl:template match="xsd:schema">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
			<xsl:call-template name="compatibility-content"/>
		</xsl:copy>
	</xsl:template>

	<!-- 
	Model types have one Extension list in the beginning and another Extension list in the end, which is too complex for the XJC to handle.
	-->
	<xsl:template match="xsd:element[@ref='Extension'][position() &gt; 1 and position() = last()]">
		<xsl:call-template name="extension-comment"/>
	</xsl:template>

	<xsl:template match="xsd:group[@name='EmbeddedModel']/xsd:sequence/xsd:element[@ref='Extension']">
		<xsl:call-template name="extension-comment"/>
	</xsl:template>

	<!--
	Simplify Array type definition
	-->
	<xsl:template match="xsd:element[@name='Array']">
	</xsl:template>

	<xsl:template match="xsd:complexType[@name='ArrayType']">
		<xsd:element name="Array">
			<xsd:complexType>
				<xsd:simpleContent>
					<xsd:extension base="xsd:string">
						<xsl:copy-of select="*"/>
					</xsd:extension>
				</xsd:simpleContent>
			</xsd:complexType>
		</xsd:element>
	</xsl:template>

	<!--
	Restrict xsd:string to FIELD-NAME where appropriate
	-->

	<xsl:template match="xsd:element[@name='ParameterField']/xsd:complexType/xsd:attribute[@name='name']/@type">
		<xsl:attribute name="type">FIELD-NAME</xsl:attribute>
	</xsl:template>

	<xsl:template match="xsd:element[@name='BayesInput' or @name='BayesOutput']/xsd:complexType/xsd:attribute[@name='fieldName']/@type">
		<xsl:attribute name="type">FIELD-NAME</xsl:attribute>
	</xsl:template>

	<xsl:template match="xsd:element[@name='InstanceField' or @name='VerificationField']/xsd:complexType/xsd:attribute[@name='field']/@type">
		<xsl:attribute name="type">FIELD-NAME</xsl:attribute>
	</xsl:template>

	<xsl:template match="xsd:element[@name='NearestNeighborModel']/xsd:complexType/xsd:attribute[@name='instanceIdVariable']/@type">
		<xsl:attribute name="type">FIELD-NAME</xsl:attribute>
	</xsl:template>

	<!--
	Simplify CONTINUOUS-DISTRIBUTION-TYPE group definition from XSD sequence to XSD choice by relocating the Extension element.
	-->
	<xsl:template match="xsd:group[@name='CONTINUOUS-DISTRIBUTION-TYPES']">
		<xsl:copy>
			<xsl:apply-templates select="@*|xsd:sequence/xsd:choice"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xsd:element[@name='Alternate' or @name='Baseline']/xsd:complexType">
		<xsl:copy>
			<xsd:sequence>
				<xsl:call-template name="extension"/>
				<xsl:apply-templates select="@*|node()"/>
			</xsd:sequence>
		</xsl:copy>
	</xsl:template>

	<!--
	Declare dummy timeseries algorithm types
	-->
	<xsl:template match="xsd:element[@name='ARIMA' or @name='SeasonalTrendDecomposition' or @name='SpectralAnalysis']">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
			<xsd:complexType>
			</xsd:complexType>
		</xsl:copy>
	</xsl:template>

	<!--
	Replace xsd:integer and xsd:nonNegativeInteger with INT-NUMBER
	-->
	<xsl:template match="xsd:attribute[@type='xs:integer' or @type='xs:nonNegativeInteger']/@type">
		<xsl:attribute name="type">INT-NUMBER</xsl:attribute>
	</xsl:template>

	<!-- 
	Replace xsd:float and xsd:double with NUMBER
	-->
	<xsl:template match="xsd:attribute[@type='xs:float' or @type='xs:double']/@type">
		<xsl:attribute name="type">NUMBER</xsl:attribute>
	</xsl:template>
</xsl:stylesheet>