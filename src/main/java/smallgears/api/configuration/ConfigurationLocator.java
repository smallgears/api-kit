package smallgears.api.configuration;

import static java.nio.file.Files.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Identifies the designated path to a configuration file (whether the file exists or not).
 * <p>
 * Clients indicate:
 * 
 * <ul>
 * <li> the name of the <em>location</code> property, i.e. a system property that contains the file's folder.
 * <li> the name of the configuration file.
 * </ul>
 * 
 * If the location property does not exist, the locator moves on to consider default locations.
 * It first looks for the configuration file in the current folder.
 * If the file doesn't exist in the current folder, the locator settles for the home of the current user, whether
 * the file already exists there or not.
 * 
 * The strategy fails if the property indicated by clients exists but does not identify a readable folder.
 * 
 */
@RequiredArgsConstructor
public class ConfigurationLocator {
	
	/**
	 * The name of the location property.
	 */
	@NonNull
	private final String config_property;
	
	/**
	 * The name of the configuration file.
	 */
	@NonNull
	private final String config_filename;
	
	
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
			
			if (!validFileAt(dir.resolve(config_filename)))
				dir = Paths.get(System.getProperty("user.home"));
		}
		
		return dir.resolve(config_filename);

	}
	
	
	private Path locate_directory() {
		
		String location = System.getProperty(config_property);
		
		if (location!=null) 
			return validDirectoryAt(location);
		
		location = System.getenv(config_property);
		
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
