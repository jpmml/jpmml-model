<?xml version="1.0" ?>
<!-- 
Copyright (c) 2016 Villu Ruusmann
-->
<xsl:stylesheet version="1.0" xmlns="http://www.dmg.org/PMML-4_4" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="extensions-content">
		<xs:simpleType name="MATH-CONTEXT">
			<xs:restriction base="xs:string">
				<xs:enumeration value="float"/>
				<xs:enumeration value="double"/>
			</xs:restriction>
		</xs:simpleType>
	</xsl:template>

	<xsl:template match="xs:element[@name='TextIndex' or @name='TextIndexNormalization']/xs:complexType/xs:attribute[@name='wordSeparatorCharacterRE']">
		<xsl:copy-of select="."/>
		<xs:attribute name="x-wordRE" type="xs:string"/>
	</xsl:template>

	<xsl:template match="xs:element[@name='NeuralNetwork' or @name='NeuralLayer']/xs:complexType/xs:attribute[@name='threshold']">
		<xsl:copy-of select="."/>
		<xs:attribute name="x-leakage" type="REAL-NUMBER"/>
	</xsl:template>

	<xsl:template match="xs:element[@name='OutputField']/xs:complexType/xs:attribute[@name='targetField']">
		<xsl:copy-of select="."/>
		<xs:attribute name="x-reportField" type="FIELD-NAME"/>
	</xsl:template>

	<xsl:template match="xs:element[@name='PMML']/xs:complexType/xs:attribute[@name='version']">
		<xsl:copy-of select="."/>
		<xs:attribute name="x-baseVersion" type="xs:string"/>
	</xsl:template>

	<xsl:template match="xs:attribute[@name='isScorable']">
		<xsl:copy-of select="."/>
		<xs:attribute name="x-mathContext" type="MATH-CONTEXT" default="double"/>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='DATATYPE']/xs:restriction">
		<xsl:variable
			name="index"
			select="count(xs:enumeration[@value='dateDaysSince[1980]']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xs:enumeration value="x-dateDaysSince[1990]"/>
			<xs:enumeration value="x-dateDaysSince[2000]"/>
			<xs:enumeration value="x-dateDaysSince[2010]"/>
			<xs:enumeration value="x-dateDaysSince[2020]"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
			<xs:enumeration value="x-dateTimeSecondsSince[1990]"/>
			<xs:enumeration value="x-dateTimeSecondsSince[2000]"/>
			<xs:enumeration value="x-dateTimeSecondsSince[2010]"/>
			<xs:enumeration value="x-dateTimeSecondsSince[2020]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='RESULT-FEATURE']/xs:restriction">
		<xs:restriction>
			<xsl:copy-of select="@*"/>
			<xsl:copy-of select="node()"/>
			<xs:enumeration value="x-report"/>
		</xs:restriction>
	</xsl:template>
</xsl:stylesheet>
