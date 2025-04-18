import org.gradle.plugins.ide.eclipse.model.internal.FileReferenceFactory
import java.util.jar.JarFile

plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

val mod_version = property("mod_version").toString()
version = mod_version
var version_name = version
val mod_build_number = property("mod_build_number").toString();
if(!mod_build_number.isEmpty()) {
	version_name = mod_version + "_X" + mod_build_number + "_H261"
	version = "[${version_name}]"
}
group = "com.hbm" // http://maven.apache.org/guides/mini/guide-naming-conventions.html
//base { archivesBaseName.set("HBM-NTM") }

/*minecraft {
	runDir = "eclipse"
}*/

/*tasks.get('version') {
	println(project.version)
}*/

/*
processResources {
	// this will ensure that this task is redone when the versions change.
	inputs.property("version", project.version)
	inputs.property("mcversion", project.minecraft.version)

	// replace stuff in mcmod.info, nothing else
	from(sourceSets.main.resources.srcDirs) {
		include 'mcmod.info'

		// replace version and mcversion
		filesMatching('mcmod.info') {
			// replace version, mcversion and credits
			expand([
				version: version_name,
				credits: project.credits
			])
		}
	}

	// copy everything else, thats not the mcmod.info
	from(sourceSets.main.resources.srcDirs) {
		exclude 'mcmod.info'
	}
}*/

// A little hack to fix codechicken's crazy maven structure (at least in 1.7.10)
/*eclipse.classpath.file.whenMerged { cp ->
	// Find all codechicken source jars
	val srcent = cp.entries.findAll { entry : File -> entry.path.contains("codechicken") && entry.path.endsWith("-src.jar") }

	// Remove them from classpath
	cp.entries.removeAll(srcent)

	// Map the source entries to their dev counterparts based on basename
	val srcmap = HashMap<String, File>()
	srcent.forEach { entry ->
		val file = File(entry.path)
		srcmap.put(file.getName().replace("-src.jar", "-dev.jar"), file)
	}

	// Create file reference factory
	val fileref = FileReferenceFactory()

	// Find all codechicken development jars
	cp.entries.find { entry : File -> entry.path.contains("codechicken") && entry.path.endsWith("-dev.jar") }.forEach { entry ->
		var srcmapping = File(entry.path) // Initialize the srcmapping from the dev jar path
		srcmapping = srcmap.get(srcmapping.getName()) // Transform it using the sourcemap
		entry = fileref.fromFile(srcmapping) // Set the source path
	}
}*/

