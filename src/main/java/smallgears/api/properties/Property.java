package smallgears.api.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A named property with a mutable, arbitrarily typed value.
 */
@Data
@ToString
@RequiredArgsConstructor(staticName="prop")
@AllArgsConstructor(staticName="prop")
public class Property {

	@NonNull
	private final String name;
	
	private Object value;
	
	/**
	 * Returns the value of this property under a given type.
	 * 
	 * @throws IllegalStateException if the value does not have the given type.
	 */
	public <S> S as(Class<S> type) {
		
		if (is(type))
			return type.cast(value());
		
		throw new IllegalStateException("property value "+value()+" of type "+value().getClass()+" cannot be typed as "+type.getCanonicalName());
		
	}
	
	/**
	 * Returns the value of this property if it has a given type, otherwise a fallback value of that type.
	 * 
	 */
	@SuppressWarnings("all")
	public <S> S asOr(S fallback) {
		
		return is(fallback.getClass()) ? (S) value() : fallback;
				
	}
	
	/**
	 * Returns <code>true</code> if the value of this property has a given type.
	 */
	public boolean is(Class<?> type) {
		return type.isInstance(value());
	}
	
	
	

}
