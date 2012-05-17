package org.jboss.forge.plugin.collectkeys.files;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.forge.plugin.collectkeys.files.RegExpFiles;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class RegExpFilesTest extends AbstractShellTest {

    @Inject
    RegExpFiles regExpFiles;

    @Deployment
    public static JavaArchive getDeployment() {
	return AbstractShellTest.getDeployment().addPackages(true, RegExpFiles.class.getPackage());
    }

    @Test
    public void searchFilesOneDirectoryTest() throws Exception {
	DirectoryResource currentDirectory = getShell().getCurrentDirectory();

	File comp1 = new File(currentDirectory.getFullyQualifiedName(), "comp.java");
	if (!comp1.exists())
	    comp1.createNewFile();
	File comp2 = new File(currentDirectory.getFullyQualifiedName(), "comp2.java");
	if (!comp2.exists())
	    comp2.createNewFile();

	List<FileResource<?>> searchFiles = regExpFiles.searchFiles(currentDirectory, "comp.java");
	assertEquals(1, searchFiles.size());

	searchFiles = regExpFiles.searchFiles(currentDirectory, "comp.?.java");
	assertEquals(2, searchFiles.size());

	searchFiles = regExpFiles.searchFiles(currentDirectory, ".*.java");
	assertTrue(searchFiles.size() >= 2);

	comp1.delete();
	comp2.delete();
    }

    @Test
    public void searchFilesTwoDirectoryTest() throws IOException {
	DirectoryResource currentDirectory = getShell().getCurrentDirectory();

	File comp1 = new File(currentDirectory.getFullyQualifiedName(), "Component.java");
	if (!comp1.exists())
	    comp1.createNewFile();
	File comp2 = new File(currentDirectory.getFullyQualifiedName(), "Component2.java");
	if (!comp2.exists())
	    comp2.createNewFile();

	File level1 = new File(currentDirectory.getFullyQualifiedName(), "level1");
	if (!level1.exists())
	    level1.mkdir();

	File comp3 = new File(level1, "Component3.java");
	if (!comp3.exists())
	    comp3.createNewFile();
	File comp4 = new File(level1, "Component4.java");
	if (!comp4.exists())
	    comp4.createNewFile();

	List<FileResource<?>> searchFiles = regExpFiles.searchFiles(currentDirectory, "Component.java");
	assertEquals(1, searchFiles.size());

	searchFiles = regExpFiles.searchFiles(currentDirectory, "Component.?.java");
	assertEquals(4, searchFiles.size());

	searchFiles = regExpFiles.searchFiles(currentDirectory, "Component[0-9]+.java");
	assertEquals(3, searchFiles.size());

	searchFiles = regExpFiles.searchFiles(currentDirectory, ".*.java");
	assertTrue(searchFiles.size() >= 4);

	comp1.delete();
	comp2.delete();
	comp3.delete();
	comp4.delete();
	level1.delete();
    }

    @Test
    public void searchFilesThreeDirectoryTest() throws IOException {
	DirectoryResource currentDirectory = getShell().getCurrentDirectory();

	File comp1 = new File(currentDirectory.getFullyQualifiedName(), "Component.java");
	if (!comp1.exists())
	    comp1.createNewFile();
	File comp2 = new File(currentDirectory.getFullyQualifiedName(), "Component2.java");
	if (!comp2.exists())
	    comp2.createNewFile();

	File level1 = new File(currentDirectory.getFullyQualifiedName(), "level1");
	if (!level1.exists())
	    level1.mkdir();

	File comp3 = new File(level1, "Component3.java");
	if (!comp3.exists())
	    comp3.createNewFile();
	File comp4 = new File(level1, "Component4.java");
	if (!comp4.exists())
	    comp4.createNewFile();

	File level2 = new File(level1, "level2");
	if (!level2.exists())
	    level2.mkdir();

	File comp5 = new File(level2, "Component5.java");
	if (!comp5.exists())
	    comp5.createNewFile();
	File comp6 = new File(level2, "Component6.java");
	if (!comp6.exists())
	    comp6.createNewFile();

	List<FileResource<?>> searchFiles = regExpFiles.searchFiles(currentDirectory, "Component.java");
	assertEquals(1, searchFiles.size());

	searchFiles = regExpFiles.searchFiles(currentDirectory, "Component.?.java");
	assertEquals(6, searchFiles.size());

	searchFiles = regExpFiles.searchFiles(currentDirectory, "Component[0-9]+.java");
	assertEquals(5, searchFiles.size());

	searchFiles = regExpFiles.searchFiles(currentDirectory, ".*.java");
	assertTrue(searchFiles.size() >= 6);

	comp1.delete();
	comp2.delete();
	comp3.delete();
	comp4.delete();
	comp5.delete();
	comp6.delete();
	level2.delete();
	level1.delete();
    }
}
