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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.components.io.filemappers.FileExtensionMapper;
import org.codehaus.plexus.components.io.filemappers.FileMapper;
import org.codehaus.plexus.util.DirectoryScanner;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Visitor;
import org.jpmml.model.PMMLUtil;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.visitors.LocatorNullifier;
import org.jpmml.model.visitors.LocatorTransformer;
import org.jpmml.model.visitors.VisitorBattery;

/**
 * <p>
 * A MOJO that loads a class model object from a PMML file, processes it, and stores the result to a SER file.
 * </p>
 *
 * @see SerializationUtil
 */
@Mojo (
	name = "ser"
)
public class SerMojo extends AbstractMojo {

	@Parameter (
		defaultValue = "${project}",
		required = true
	)
	private MavenProject project;

	/**
	 * <p>
	 * The name of the output data format.
	 * </p>
	 */
	@Parameter (
		defaultValue = "SER"
	)
	private Format format = null;

	/**
	 * <p>
	 * The default file name mapper.
	 * A model set can override it by specifying its own file name mapper.
	 * </p>
	 *
	 * <p>
	 * The default file name mapper changes the file name extension to match that of the output data format.
	 * For example, <code>mymodel.pmml</code> would be mapped to <code>mymodel.ser</code>.
	 * </p>
	 *
	 * @see FileExtensionMapper
	 */
	@Parameter
	private FileMapper fileMapper = null;

	/**
	 * <p>
	 * A list of one or more model sets.
	 * </p>
	 */
	@Parameter (
		required = true
	)
	private List<ModelSet> modelSets = null;

	/**
	 * <p>
	 * A boolean indicating if the SAX Locator information, if available, should be transferred over to the SER file.
	 * </p>
	 */
	@Parameter
	private boolean keepLocator = false;

	/**
	 * <p>
	 * A list of zero or more {@link Visitor} class names.
	 * </p>
	 */
	@Parameter
	private List<String> visitorClasses = null;


	public SerMojo(){
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Format format = getFormat();
		if(format == null){
			format = Format.SER;

			setFormat(format);
		}

		FileMapper fileMapper = getFileMapper();
		if(fileMapper == null){
			String extension = (format.name()).toLowerCase();

			FileExtensionMapper fileExtensionMapper = new FileExtensionMapper();
			fileExtensionMapper.setTargetExtension(extension);

			fileMapper = fileExtensionMapper;

			setFileMapper(fileMapper);
		}

		VisitorBattery visitorBattery = new VisitorBattery();

		boolean keepLocator = getKeepLocator();

		visitorBattery.add(keepLocator ? LocatorTransformer.class : LocatorNullifier.class);

		List<String> visitorClasses = getVisitorClasses();
		if(visitorClasses != null){

			try {
				for(String visitorClass : visitorClasses){
					Class<?> clazz = Class.forName(visitorClass);

					visitorBattery.add(clazz.asSubclass(Visitor.class));
				}
			} catch(ClassNotFoundException | ClassCastException e){
				throw new MojoFailureException("Invalid visitorClasses parameter", e);
			}
		}

		List<ModelSet> modelSets = getModelSets();

		if(modelSets == null || modelSets.isEmpty()){
			throw new MojoFailureException("Missing modelSets parameter");
		}

		for(ModelSet modelSet : modelSets){
			transform(modelSet, visitorBattery);
		}
	}

	private void transform(ModelSet modelSet, VisitorBattery visitorBattery) throws MojoExecutionException {
		Log log = getLog();

		log.info("Processing model set from " + modelSet.getDir() + " to " + modelSet.getOutputDir());

		Format format = getFormat();

		FileMapper fileMapper = modelSet.getFileMapper();
		if(fileMapper == null){
			fileMapper = getFileMapper();
		}

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
				File pmmlFile = new File(dir, name);
				File outputFile = new File(outputDir, fileMapper.getMappedFileName(name));

				File outputParentDir = outputFile.getParentFile();
				if(outputParentDir != null && !outputParentDir.exists()){
					outputParentDir.mkdirs();
				}

				transform(format, pmmlFile, outputFile, visitorBattery);
			} catch(Exception e){
				throw new MojoExecutionException("Failed to process model " + name, e);
			}
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

	public Format getFormat(){
		return this.format;
	}

	public void setFormat(Format format){
		this.format = format;
	}

	public FileMapper getFileMapper(){
		return this.fileMapper;
	}

	public void setFileMapper(FileMapper fileMapper){
		this.fileMapper = fileMapper;
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

	public List<String> getVisitorClasses(){
		return this.visitorClasses;
	}

	public void setVisitorClasses(List<String> visitorClasses){
		this.visitorClasses = visitorClasses;
	}

	static
	private void transform(Format format, File pmmlFile, File serFile, VisitorBattery visitorBattery) throws Exception {
		PMML pmml;

		try(InputStream is = new FileInputStream(pmmlFile)){
			pmml = PMMLUtil.unmarshal(is);
		}

		visitorBattery.applyTo(pmml);

		try(OutputStream os = new FileOutputStream(serFile)){
			format.write(pmml, os);
		}
	}
}