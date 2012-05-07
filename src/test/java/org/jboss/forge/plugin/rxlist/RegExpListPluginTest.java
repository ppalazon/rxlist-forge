package org.jboss.forge.plugin.rxlist;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class RegExpListPluginTest extends AbstractShellTest {

    @Deployment
    public static JavaArchive getDeployment() {
	return AbstractShellTest.getDeployment().addPackages(true, RegExpListPlugin.class.getPackage())
	        .addAsResource("messages_en.properties");
    }

    @Test
    public void regExpList() throws Exception {
	DirectoryResource tempDir = createTempFolder();

	File resources = new File(tempDir.getFullyQualifiedName(), "resources");
	FileUtils.forceMkdir(resources);

	File src = new File(tempDir.getFullyQualifiedName(), "src");
	FileUtils.forceMkdir(src);

	File web = new File(src, "webpages");
	FileUtils.forceMkdir(web);

	File java = new File(src, "java");
	FileUtils.forceMkdir(java);

	FileUtils.copyFileToDirectory(new File("src/test/resources/messages_en.properties"), resources);
	FileUtils.copyFileToDirectory(new File("src/test/resources/messages_es.properties"), resources);
	FileUtils.copyFileToDirectory(new File("src/test/resources/Componente1.java"), java);
	FileUtils.copyFileToDirectory(new File("src/test/resources/index.xhtml"), web);

	int counten = countKeys(new File(resources, "messages_en.properties"));
	int countes = countKeys(new File(resources, "messages_es.properties"));

	assertTrue(counten == 2);
	assertTrue(countes == 2);

	getShell().execute("cd " + tempDir.getFullyQualifiedName());

	System.out.println("Temporal dir " + tempDir.getFullyQualifiedName());

	getShell()
	        .execute(
	                "rxlist --rxProperties messages_.*.properties --rxSearchFiles .*.java --regexp Componente1.getMessage\\(\"(.*?)\"\\);");

	System.out.println(getOutput());

	getShell()
	        .execute(
	                "rxlist --rxProperties messages_.*.properties --rxSearchFiles .*.xhtml --regexp messages.getMessage\\('(.*?)'\\)");

	System.out.println(getOutput());

	counten = countKeys(new File(resources, "messages_en.properties"));
	countes = countKeys(new File(resources, "messages_es.properties"));

	assertTrue(counten == countes);

	FileUtils.forceDelete(new File(tempDir.getFullyQualifiedName()));

    }

    private int countKeys(File propertiesFile) throws ConfigurationException {
	PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(propertiesFile);
	Iterator keys = propertiesConfiguration.getKeys();
	int keyen = 0;
	while (keys.hasNext()) {
	    keys.next();
	    keyen++;
	}
	return keyen;
    }

}
