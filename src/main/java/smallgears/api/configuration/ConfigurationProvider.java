package smallgears.api.configuration;

import static java.lang.String.*;
import static java.nio.file.Files.*;
import static smallgears.api.Apikit.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Gives access to a configuration file using a systematic location identification strategy.
 * <p>
 * Clients indicate:
 * 
 * <ul>
 * <li> the name of the <em>location</code> property, i.e. a system property that contains the file's folder.
 * <li> the name of the configuration file.
 * </ul>
 * 
 * <ul>
 * <li> if the location property does not exist, the provider moves on to consider default locations.
 * <li> it first looks for the configuration file in the current folder.
 * <li> if the file doesn't exist in the current folder, the provider looks in the home of the current user.
 * <li> if it doesn't exist there either, the provider searches the classpath.
 * </ul>
 * 
 * The strategy fails if:
 * 
 * <ul>
 * <li> the property indicated by clients exists but does not identify a readable folder, or if 
 * <li> the configuration cannot be ultimately located.
 * </ul>
 * 
 * Clients can decide to simply find the location of the configuration file, whether this already exists or not.
 * In this case, the provider falls short of searching the classpath.
 * 
 */
@RequiredArgsConstructor
@Slf4j
public class ConfigurationProvider {
	
	/**
	 * The name of the location property.
	 */
	@NonNull
	private final String locationProperty;
	
	/**
	 * The name of the configuration file.
	 */
	@NonNull
	private final String filename;
	
	
   /**
    * The path to the configuration.
    * 
    * @throws {@link IllegalStateException} if the property indicated by clients exists but does not identify a readable folder. 
    * 
    */
	public Path locate() throws IllegalStateException {

		Path dir = locate_directory();
		
		if (dir==null) {
			
			dir = Paths.get(System.getProperty("user.dir"));
			
			if (!validFileAt(dir.resolve(filename)))
				dir = Paths.get(System.getProperty("user.home"));
		}
		
		return dir.resolve(filename);

	}
	
	
	   /**
	    * The configuration.
	    * 
	    * @param mandatory if <code>true</code>, an inaccessible configuration will raise an exception.
	    * 
	    * 
	    */
		public Optional<InputStream> provide(boolean mandatory) {

			Path location = locate();
			
			InputStream stream = null;
			
			try {
			
				if (exists(location)) {
					
					log.info("loading configuration @ {}",location);
						
					stream = new FileInputStream(location.toFile());
						
				}
				else {
					
					stream = ConfigurationProvider.class.getResourceAsStream("/"+filename);

					if (stream != null)
						log.info("taking configuration for {} from classpath",filename);
					else
						if (mandatory)
							throw new AssertionError(format("no configuration for %s found on file system or classpath",filename));
				}
				
				return Optional.ofNullable(stream);
			}
			catch(Exception e) {
				throw unchecked(format("cannot read the configuration for %s",filename),e);
			}

		}
	
	
	private Path locate_directory() {
		
		String location = System.getProperty(locationProperty);
		
		if (location!=null) 
			return validDirectoryAt(location);
		
		location = System.getenv(locationProperty);
		
		if (location!=null) 
			return validDirectoryAt(location);
		
		return null;
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Path validDirectoryAt(@NonNull String location) {
		
		Path path = Paths.get(location);
	
		if (isDirectory(path) && isReadable(path))
			return path;
		
		throw new IllegalStateException(String.format("invalid configuration @ {}: not a readable directory",path));
		
				
	}
	
	private boolean validFileAt(Path path) {
			
			return Files.isReadable(path) && !Files.isDirectory(path);
		
	}
}
