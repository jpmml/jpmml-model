<?xml version="1.0" ?>
<!--
Copyright (c) 2009 University of Tartu
Copyright (c) 2014 Villu Ruusmann
-->
<xsl:stylesheet version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="common.xsl"/>
	<xsl:include href="compatibility.xsl"/>
	<xsl:include href="extensions.xsl"/>

	<xsl:template match="xs:schema">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
			<xsl:call-template name="compatibility-content"/>
			<xsl:call-template name="extensions-content"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
