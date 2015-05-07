<?xml version="1.0" ?>
<!-- 
Copyright (c) 2014 Villu Ruusmann
-->
<xsl:stylesheet version="1.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="compatibility-content">
		<xsd:element name="CenterFields">
			<xsd:complexType>
				<xsd:sequence>
					<xsd:element ref="DerivedField" maxOccurs="unbounded"/>
				</xsd:sequence>
			</xsd:complexType>
		</xsd:element>
	</xsl:template>

	<xsl:template match="xsd:element[@name='ClusteringModel']/xsd:complexType/xsd:sequence">
		<xsl:variable
			name="index"
			select="count(xsd:element[@ref='ClusteringField']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xsd:element ref="CenterFields" minOccurs="0"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xsd:element[@name='DerivedField']/xsd:complexType/xsd:sequence">
		<xsl:variable
			name="index"
			select="count(xsd:group[@ref='EXPRESSION']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xsd:element ref="Interval" minOccurs="0" maxOccurs="unbounded"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xsd:element[@name='Level']/xsd:complexType">
		<xsl:variable
			name="index"
			select="count(xsd:attribute[@ref='smoothedValue']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xsd:attribute name="quadraticSmoothedValue" type="REAL-NUMBER"/>
			<xsd:attribute name="cubicSmoothedValue" type="REAL-NUMBER"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xsd:element[@name='NormDiscrete']/xsd:complexType/xsd:attribute[@name='method']/xsd:simpleType/xsd:restriction">
		<xsl:variable
			name="index"
			select="count(xsd:enumeration[@value='indicator']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xsd:enumeration value="thermometer"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xsd:element[@name='Segmentation']/xsd:complexType/xsd:sequence">
		<xsl:variable
			name="index"
			select="count(xsd:element[@ref='Extension']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xsd:element ref="LocalTransformations" minOccurs="0"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xsd:element[@name='SequenceModel']/xsd:complexType">
		<xsl:variable
			name="index"
			select="count(xsd:attribute[@name='avgNumberOfTAsPerTAGroup']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xsd:attribute name="minimumSupport" type="REAL-NUMBER" use="required"/>
			<xsd:attribute name="minimumConfidence" type="REAL-NUMBER" use="required"/>
			<xsd:attribute name="lengthLimit" type="INT-NUMBER"/>
			<xsd:attribute name="numberOfItems" type="INT-NUMBER" use="required"/>
			<xsd:attribute name="numberOfSets" type="INT-NUMBER" use="required"/>
			<xsd:attribute name="numberOfSequences" type="INT-NUMBER" use="required"/>
			<xsd:attribute name="numberOfRules" type="INT-NUMBER" use="required"/>
			<xsd:attribute name="timeWindowWidth" type="INT-NUMBER"/>
			<xsd:attribute name="minimumTime" type="INT-NUMBER"/>
			<xsd:attribute name="maximumTime" type="INT-NUMBER"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xsd:element[@name='SupportVectorMachineModel']/xsd:complexType">
		<xsl:variable
			name="index"
			select="count(xsd:attribute[@name='svmRepresentation']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xsd:attribute name="alternateBinaryTargetCategory" type="xsd:string" use="optional"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xsd:element[@name='TimeSeries']/xsd:complexType/xsd:sequence">
		<xsl:variable
			name="index"
			select="count(xsd:element[@ref='TimeAnchor']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xsd:element ref="TimeException" minOccurs="0" maxOccurs="2"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xsd:element[@name='Trend_ExpoSmooth']/xsd:complexType/xsd:attribute[@name='trend']/xsd:simpleType/xsd:restriction">
		<xsl:variable
			name="index"
			select="count(xsd:enumeration[@value='damped_multiplicative']/preceding-sibling::*) + 1"
		/>
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="node()[position() &lt;= $index]"/>
			<xsd:enumeration value="double_exponential"/>
			<xsl:apply-templates select="node()[position() &gt; $index]"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>