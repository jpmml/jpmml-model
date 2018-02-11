<?xml version="1.0" ?>
<!-- 
Copyright (c) 2014 Villu Ruusmann
-->
<xsl:stylesheet version="1.0" xmlns="http://www.dmg.org/PMML-4_3" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="compatibility-content">
		<xs:element name="CenterFields">
			<xs:complexType>
				<xs:sequence>
					<xs:element ref="DerivedField" maxOccurs="unbounded"/>
				</xs:sequence>
			</xs:complexType>
		</xs:element>
	</xsl:template>

	<xsl:template match="xs:element[@name='ClusteringModel']/xs:complexType/xs:sequence">
		<xsl:variable
			name="index"
			select="count(xs:element[@ref='ClusteringField']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xs:element ref="CenterFields" minOccurs="0"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:element[@name='DerivedField']/xs:complexType/xs:sequence">
		<xsl:variable
			name="index"
			select="count(xs:group[@ref='EXPRESSION']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xs:element ref="Interval" minOccurs="0" maxOccurs="unbounded"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:element[@name='Level']/xs:complexType">
		<xsl:variable
			name="index"
			select="count(xs:attribute[@ref='smoothedValue']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xs:attribute name="quadraticSmoothedValue" type="REAL-NUMBER"/>
			<xs:attribute name="cubicSmoothedValue" type="REAL-NUMBER"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:element[@name='NormDiscrete']/xs:complexType">
		<xsl:variable
			name="index"
			select="count(xs:attribute[@name='field']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xs:attribute name="method" fixed="indicator">
				<xs:simpleType>
					<xs:restriction base="xs:string">
						<xs:enumeration value="indicator"/>
						<xs:enumeration value="thermometer"/>
					</xs:restriction>
				</xs:simpleType>
			</xs:attribute>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:element[@name='Segmentation']/xs:complexType/xs:sequence">
		<xsl:variable
			name="index"
			select="count(xs:element[@ref='Extension']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xs:element ref="LocalTransformations" minOccurs="0"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:element[@name='SequenceModel']/xs:complexType">
		<xsl:variable
			name="index"
			select="count(xs:attribute[@name='avgNumberOfTAsPerTAGroup']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xs:attribute name="minimumSupport" type="REAL-NUMBER" use="required"/>
			<xs:attribute name="minimumConfidence" type="REAL-NUMBER" use="required"/>
			<xs:attribute name="lengthLimit" type="INT-NUMBER"/>
			<xs:attribute name="numberOfItems" type="INT-NUMBER" use="required"/>
			<xs:attribute name="numberOfSets" type="INT-NUMBER" use="required"/>
			<xs:attribute name="numberOfSequences" type="INT-NUMBER" use="required"/>
			<xs:attribute name="numberOfRules" type="INT-NUMBER" use="required"/>
			<xs:attribute name="timeWindowWidth" type="INT-NUMBER"/>
			<xs:attribute name="minimumTime" type="INT-NUMBER"/>
			<xs:attribute name="maximumTime" type="INT-NUMBER"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:element[@name='SupportVectorMachineModel']/xs:complexType">
		<xsl:variable
			name="index"
			select="count(xs:attribute[@name='svmRepresentation']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xs:attribute name="alternateBinaryTargetCategory" type="xs:string" use="optional"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:element[@name='TimeSeries']/xs:complexType/xs:sequence">
		<xsl:variable
			name="index"
			select="count(xs:element[@ref='TimeAnchor']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xs:element ref="TimeException" minOccurs="0" maxOccurs="2"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xs:element[@name='Trend_ExpoSmooth']/xs:complexType/xs:attribute[@name='trend']/xs:simpleType/xs:restriction">
		<xsl:variable
			name="index"
			select="count(xs:enumeration[@value='damped_multiplicative']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xs:enumeration value="double_exponential"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
