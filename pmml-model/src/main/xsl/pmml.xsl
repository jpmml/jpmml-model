<?xml version="1.0" ?>
<!--
Copyright (c) 2009 University of Tartu
-->
<xsl:stylesheet version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="common.xsl"/>
	<xsl:include href="compatibility.xsl"/>
	<xsl:include href="extensions.xsl"/>

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
			<xsl:call-template name="extensions-content"/>
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

	<xsl:template match="xs:element[@name='Delimiter']/xs:complexType/xs:attribute[@name='gap']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">GAP</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='DELIMITER' or @name='GAP']">
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
	Inline NeuralNetwork and NeuralLayer shared enum types
	-->
	<xsl:template match="xs:element[@name='NeuralNetwork' or @name='NeuralLayer']/xs:complexType/xs:attribute[@name='activationFunction']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">ACTIVATION-FUNCTION</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:element[@name='NeuralNetwork' or @name='NeuralLayer']/xs:complexType/xs:attribute[@name='normalizationMethod']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">NN-NORMALIZATION-METHOD</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='ACTIVATION-FUNCTION' or @name='NN-NORMALIZATION-METHOD']">
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
	Inline RegressionModel and Regression shared enum types
	-->
	<xsl:template match="xs:element[@name='RegressionModel' or @name='Regression']/xs:complexType/xs:attribute[@name='normalizationMethod']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">REGRESSIONNORMALIZATIONMETHOD</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='REGRESSIONNORMALIZATIONMETHOD']">
	</xsl:template>

	<!--
	Inline Segmentation enum types
	-->
	<xsl:template match="xs:element[@name='Segmentation']/xs:complexType/xs:attribute[@name='multipleModelMethod']">
		<xsl:call-template name="inline-multipleModelMethod"/>
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
	Inline TimeAnchor enum types
	-->
	<xsl:template match="xs:element[@name='TimeAnchor']/xs:complexType/xs:attribute[@name='type']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">TIME-ANCHOR</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='TIME-ANCHOR']">
	</xsl:template>

	<!--
	Inline TimeCycle enum types
	-->
	<xsl:template match="xs:element[@name='TimeCycle']/xs:complexType/xs:attribute[@name='type']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">VALID-TIME-SPEC</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='VALID-TIME-SPEC']">
	</xsl:template>

	<!--
	Inline TimeException enum types
	-->
	<xsl:template match="xs:element[@name='TimeException']/xs:complexType/xs:attribute[@name='type']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">TIME-EXCEPTION-TYPE</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='TIME-EXCEPTION-TYPE']">
	</xsl:template>

	<!--
	Inline TimeSeries enum types
	-->
	<xsl:template match="xs:element[@name='TimeSeries']/xs:complexType/xs:attribute[@name='interpolationMethod']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">INTERPOLATION-METHOD</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:element[@name='TimeSeries']/xs:complexType/xs:attribute[@name='usage']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">TIMESERIES-USAGE</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='INTERPOLATION-METHOD' or @name='TIMESERIES-USAGE']">
	</xsl:template>

	<!--
	Inline TimeSeriesModel enum types
	-->
	<xsl:template match="xs:element[@name='TimeSeriesModel']/xs:complexType/xs:attribute[@name='bestFit']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">TIMESERIES-ALGORITHM</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='TIMESERIES-ALGORITHM']">
	</xsl:template>

	<!--
	Inline TreeModel and DecisionTree shared enum types
	-->
	<xsl:template match="xs:element[@name='TreeModel' or @name='DecisionTree']/xs:complexType/xs:attribute[@name='missingValueStrategy']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">MISSING-VALUE-STRATEGY</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:element[@name='TreeModel' or @name='DecisionTree']/xs:complexType/xs:attribute[@name='noTrueChildStrategy']">
		<xsl:call-template name="inline-enum">
			<xsl:with-param name="name">NO-TRUE-CHILD-STRATEGY</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="xs:simpleType[@name='MISSING-VALUE-STRATEGY' or @name='NO-TRUE-CHILD-STRATEGY']">
	</xsl:template>
</xsl:stylesheet>
