<?xml version="1.0" ?>
<!-- 
Copyright (c) 2016 Villu Ruusmann
-->
<xsl:stylesheet version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="extensions-content">
	</xsl:template>

	<xsl:template match="xs:element[@name='PMML']/xs:complexType/xs:attribute[@name='version']">
		<xsl:copy-of select="."/>
		<xs:attribute name="x-baseVersion" type="xs:string"/>
	</xsl:template>
</xsl:stylesheet>
