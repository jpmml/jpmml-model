<?xml version="1.0" ?>
<!-- 
Copyright (c) 2016 Villu Ruusmann
-->
<xsl:stylesheet version="1.0" xmlns="http://www.dmg.org/PMML-4_3" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

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
		<xs:attribute name="x-missingPredictionTreatment" default="continue">
			<xs:simpleType>
				<xs:restriction base="xs:string">
					<xs:enumeration value="returnMissing"/>
					<xs:enumeration value="skipSegment"/>
					<xs:enumeration value="continue"/>
				</xs:restriction>
			</xs:simpleType>
		</xs:attribute>
		<xs:attribute name="x-missingThreshold" type="PROB-NUMBER" default="1"/>
	</xsl:template>

	<xsl:template match="xs:element[@name='MiningField']/xs:complexType/xs:attribute[@name='invalidValueTreatment']">
		<xs:attribute name="x-invalidValueReplacement" type="xs:string"/>
		<xsl:copy-of select="."/>
	</xsl:template>

	<xsl:template match="xs:element[@name='OutputField']/xs:complexType/xs:attribute[@name='targetField']">
		<xsl:copy-of select="."/>
		<xs:attribute name="x-reportField" type="FIELD-NAME"/>
	</xsl:template>

	<xsl:template match="xs:element[@name='OutputField']/xs:complexType/xs:sequence/xs:sequence/xs:group[@ref='EXPRESSION']">
		<xsl:copy-of select="."/>
		<xs:element ref="Value" minOccurs="0" maxOccurs="unbounded"/>
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

	<xsl:template match="xs:simpleType[@name='MISSING-VALUE-TREATMENT-METHOD']/xs:restriction">
		<xs:restriction>
			<xsl:copy-of select="@*"/>
			<xsl:copy-of select="node()"/>
			<xs:enumeration value="x-returnInvalid"/>
		</xs:restriction>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='RESULT-FEATURE']/xs:restriction">
		<xs:restriction>
			<xsl:copy-of select="@*"/>
			<xsl:copy-of select="node()"/>
			<xs:enumeration value="x-report"/>
		</xs:restriction>
	</xsl:template>
</xsl:stylesheet>
