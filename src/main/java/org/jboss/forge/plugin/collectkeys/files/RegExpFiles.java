package org.jboss.forge.plugin.collectkeys.files;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Singleton;

import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.ResourceFlag;

@Singleton
public class RegExpFiles {

    public List<FileResource<?>> searchFiles(DirectoryResource dir, String regexp) {
	Pattern patternFile = Pattern.compile(regexp);
	List<FileResource<?>> founded = new ArrayList<FileResource<?>>();
	List<Resource<?>> listResources = dir.listResources();

	for (Resource<?> resource : listResources) {
	    if ((resource.isFlagSet(ResourceFlag.File) || resource.isFlagSet(ResourceFlag.ProjectSourceFile))
		    && !resource.isFlagSet(ResourceFlag.Node)) {
		Matcher matcher = patternFile.matcher(resource.getName());
		if (matcher.matches()) {
		    founded.add((FileResource<?>) resource);
		}
	    }
	    if (resource.isFlagSet(ResourceFlag.Node)) {
		founded.addAll(searchFiles((DirectoryResource) resource, regexp));
	    }
	}

	return founded;
    }
}
