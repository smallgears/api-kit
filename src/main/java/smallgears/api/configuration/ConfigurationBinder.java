package smallgears.api.configuration;

import static smallgears.api.Apikit.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import lombok.Cleanup;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Loads and save json configurations
 */
@RequiredArgsConstructor
public class ConfigurationBinder<C> {

	@NonNull @Getter
	private final ObjectMapper mapper;
	
	@NonNull
	private final Class<C> type;
	
	/**
	 * Creates its own object mapper.
	 */
	public ConfigurationBinder(Class<C> type) {
		this(new ObjectMapper(),type);
	}

	public C load(InputStream stream) {

		try {

			return mapper.readValue(stream,type);

		}
		catch (Exception e) {
			throw unchecked("cannot load configuration (see cause)", e);
		}
	}
	
	public void save(C cfg, File location) {

		try {
			
			@Cleanup FileOutputStream stream = new FileOutputStream(location);

			mapper.writeValue(stream, cfg);

		}
		catch (Exception e) {
			throw unchecked("cannot save configuration (see cause)", e);
		}
	}
	

}
