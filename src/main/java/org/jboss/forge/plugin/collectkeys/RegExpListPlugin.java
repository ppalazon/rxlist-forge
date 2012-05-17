package org.jboss.forge.plugin.collectkeys;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.jboss.forge.plugin.collectkeys.files.RegExpFiles;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellColor;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;

@Alias("collect-keys")
@Help(value = "Collect key properties list from source code with regular expressions")
public class RegExpListPlugin implements Plugin {

    @Inject
    private ShellPrompt prompt;

    @Inject
    Shell shell;

    @Inject
    RegExpFiles regExpFiles;

    @DefaultCommand(help = "Collect key properties list from source code with regular expressions")
    public void run(
	    PipeOut out,
	    @Option(name = "property-files", description = "Regular expression to match property files", help = "Regular expression to match property files") final String propertyFiles,
	    @Option(name = "source-files", description = "Regular expression to match source files to search for message keys", help = "Regular expression to match source files to search for message keys") final String sourceFiles,
	    @Option(name = "key-pattern", description = "Regular expression for matching a key in a source file", help = "Regular expression for matching a key in a source file") final String keyPattern)
	    throws ConfigurationException {

	out.println("Searching " + keyPattern + " in all " + sourceFiles + " for update " + propertyFiles);

	String regx = keyPattern.replace("''", "\"");

	// Check if we are in a directory
	DirectoryResource currentDirectory = shell.getCurrentDirectory();
	// Search all regexp on rxSearchFiles
	List<FileResource<?>> searchFiles = regExpFiles.searchFiles(currentDirectory, sourceFiles);

	// Extract key from searched regexp
	Set<String> foundedKey = new HashSet<String>();
	Pattern searchIn = Pattern.compile(regx);
	for (FileResource<?> file : searchFiles) {
	    try {
		String content = IOUtils.toString(file.getResourceInputStream());
		Matcher matcher = searchIn.matcher(content);
		while (matcher.find()) {
		    String key = matcher.group(1);
		    if (!foundedKey.contains(key)) {
			foundedKey.add(key);
		    }
		}
	    } catch (IOException e) {
		out.print(e.getLocalizedMessage());
	    }
	}

	// Get keys from properties files
	List<FileResource<?>> propertiesFiles = regExpFiles.searchFiles(currentDirectory, propertyFiles);
	for (FileResource<?> fileprop : propertiesFiles) {
	    PropertiesConfiguration propConfiguration = new PropertiesConfiguration(fileprop.getFullyQualifiedName());
	    // propConfiguration.load();
	    propConfiguration.setAutoSave(true);
	    for (String key : foundedKey) {
		if (!propConfiguration.containsKey(key)) {
		    out.println(ShellColor.GREEN, "Added " + key + " to " + fileprop.getName());
		    propConfiguration.addProperty(key, ">>" + key);
		}
	    }
	    // propConfiguration.save();
	}
	// Add not inserted keys with default value
    }

}
