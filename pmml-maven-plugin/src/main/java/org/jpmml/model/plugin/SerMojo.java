/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.transform.Source;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Visitor;
import org.jpmml.model.ImportFilter;
import org.jpmml.model.JAXBUtil;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.visitors.LocatorNullifier;
import org.jpmml.model.visitors.LocatorTransformer;
import org.xml.sax.InputSource;

@Mojo (
	name = "ser"
)
public class SerMojo extends AbstractMojo {

	@Parameter (
		defaultValue = "${project}",
		required = true
	)
	private MavenProject project;

	@Parameter
	private List<ModelSet> modelSets = null;

	@Parameter
	private boolean keepLocator = false;


	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		List<ModelSet> modelSets = getModelSets();

		if(modelSets == null || modelSets.isEmpty()){
			throw new MojoFailureException("No modelSets configured");
		}

		for(ModelSet modelSet : modelSets){
			transform(modelSet);
		}
	}

	private void transform(ModelSet modelSet) throws MojoExecutionException {
		Log log = getLog();

		log.info("Processing model set from " + modelSet.getDir() + " to " + modelSet.getOutputDir());

		File dir = resolveFile(modelSet.getDir());
		File outputDir = resolveFile(modelSet.getOutputDir());

		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setBasedir(dir);
		scanner.setIncludes(modelSet.getIncludes());
		scanner.setExcludes(modelSet.getExcludes());

		scanner.scan();

		String[] names = scanner.getIncludedFiles();
		for(String name : names){
			log.info("Processing model " + name);

			try {
				transform(name, dir, outputDir);
			} catch(Exception e){
				throw new MojoExecutionException("Failed to process model " + name, e);
			}
		}
	}

	private void transform(String name, File dir, File outputDir) throws Exception {
		boolean keepLocator = getKeepLocator();

		int dot = name.indexOf('.');
		if(dot <= 0){
			throw new IllegalArgumentException(name);
		}

		File pmmlFile = new File(dir, name);
		File serFile = new File(outputDir, name.substring(0, dot) + ".ser");

		File outputParent = serFile.getParentFile();
		if(!outputParent.exists()){
			outputParent.mkdirs();
		}

		PMML pmml;

		try(InputStream is = new FileInputStream(pmmlFile)){
			Source source = ImportFilter.apply(new InputSource(is));

			pmml = JAXBUtil.unmarshalPMML(source);
		}

		Visitor visitor = (keepLocator ? new LocatorTransformer() : new LocatorNullifier());
		visitor.applyTo(pmml);

		try(OutputStream os = new FileOutputStream(serFile)){
			SerializationUtil.serializePMML(pmml, os);
		}
	}

	private File resolveFile(File file){
		MavenProject project = getProject();

		if(!file.isAbsolute()){
			file = new File(project.getBasedir(), file.getPath());
		}

		return file;
	}

	public MavenProject getProject(){
		return this.project;
	}

	public void setProject(MavenProject project){
		this.project = project;
	}

	public List<ModelSet> getModelSets(){
		return this.modelSets;
	}

	public void setModelSets(List<ModelSet> modelSets){
		this.modelSets = modelSets;
	}

	public boolean getKeepLocator(){
		return this.keepLocator;
	}

	public void setKeepLocator(boolean keepLocator){
		this.keepLocator = keepLocator;
	}
}