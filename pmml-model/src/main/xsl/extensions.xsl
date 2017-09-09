<?xml version="1.0" ?>
<!-- 
Copyright (c) 2016 Villu Ruusmann
-->
<xsl:stylesheet version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="extensions-content">
		<xs:simpleType name="MATH-CONTEXT">
			<xs:restriction base="xs:string">
				<xs:enumeration value="float"/>
				<xs:enumeration value="double"/>
			</xs:restriction>
		</xs:simpleType>
	</xsl:template>

	<xsl:template name="inline-multipleModelMethod">
		<xsl:copy>
			<xsl:apply-templates select="@*[name() != 'type']"/>
			<xs:simpleType>
				<xs:restriction base="xs:string">
					<xsl:copy-of select="//xs:simpleType[@name='MULTIPLE-MODEL-METHOD']/xs:restriction/xs:enumeration[@value='majorityVote' or @value='weightedMajorityVote' or @value='average' or @value='weightedAverage' or @value='median']"/>
					<xs:enumeration value="x-weightedMedian"/>
					<xsl:copy-of select="//xs:simpleType[@name='MULTIPLE-MODEL-METHOD']/xs:restriction/xs:enumeration[@value='max' or @value='sum']"/>
					<xs:enumeration value="x-weightedSum"/>
					<xsl:copy-of select="//xs:simpleType[@name='MULTIPLE-MODEL-METHOD']/xs:restriction/xs:enumeration[@value='selectFirst' or @value='selectAll' or @value='modelChain']"/>
				</xs:restriction>
			</xs:simpleType>
		</xsl:copy>
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

	<xsl:template match="xs:simpleType[@name='RESULT-FEATURE']/xs:restriction">
		<xs:restriction>
			<xsl:copy-of select="@*"/>
			<xsl:copy-of select="node()"/>
			<xs:enumeration value="x-report"/>
		</xs:restriction>
	</xsl:template>
</xsl:stylesheet>
