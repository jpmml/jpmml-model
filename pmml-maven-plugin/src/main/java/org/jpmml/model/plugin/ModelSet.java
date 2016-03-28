/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.plugin;

import java.io.File;

import org.codehaus.plexus.components.io.filemappers.FileMapper;

public class ModelSet {

	private File dir = null;

	private File outputDir = null;

	private String[] includes = null;

	private String[] excludes = null;

	private FileMapper fileMapper = null;


	public File getDir(){
		return this.dir;
	}

	public void setDir(File dir){
		this.dir = dir;
	}

	public File getOutputDir(){
		return this.outputDir;
	}

	public void setOutputDir(File outputDir){
		this.outputDir = outputDir;
	}

	public String[] getIncludes(){
		return this.includes;
	}

	public void setIncludes(String[] includes){
		this.includes = includes;
	}

	public String[] getExcludes(){
		return this.excludes;
	}

	public void setExcludes(String[] excludes){
		this.excludes = excludes;
	}

	public FileMapper getFileMapper(){
		return this.fileMapper;
	}

	public void setFileMapper(FileMapper fileMapper){
		this.fileMapper = fileMapper;
	}
}