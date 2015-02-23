package smallgears.api.properties;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import smallgears.api.group.Group;

/**
 * A mutable group of uniquely named {@link Property}s.
 * 
 */
@EqualsAndHashCode(callSuper=true)
public class Properties extends Group<Property,Properties> {
	
	/**
	 * Creates a group of initial properties.
	 */
	public static Properties props(@NonNull Property ... properties) {
		return new Properties().add(properties);
	}

	
	/**
	 * @see Property#prop(String, Object)
	 */
	public static Property prop(String name, Object value) {
		return Property.prop(name, value);
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	
	private Properties() {
		super(Property::name);
	}
	
	
	/**
	 * Add given elements to this group.
	 */
	public Properties add(@NonNull String ... props) {
		
		return add(asList(props));

	}
	
	/**
	 * Add elements to this group.
	 */
	public Properties add(Collection<String> names)  {
		
		return add(coll(names));
	}
	
	/**
	 * Removes given elements from this group, if they exist.
	 */
	public Properties remove(@NonNull Collection<String> names) {

		return remove(coll(names));
	}
	
	/**
	 * An element in this group, or another if the first does not exist.
	 */
	public Property getOr(@NonNull String name, String fallback) {
		
		Optional<Property> e = get(name);
		
		return e.isPresent()? e.get() : prop(name,fallback);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////
		
	private Collection<Property> coll(Collection<String> ts) {
		
		return ts.stream().map(Property::prop).collect(toList());
	
	}
	
}
