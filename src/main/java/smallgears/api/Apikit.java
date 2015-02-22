package smallgears.api;

import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.*;

import java.util.stream.Stream;

import lombok.experimental.UtilityClass;


@UtilityClass
public class Apikit {

	/**
	 * Turns an {@link Iterable} into a sequential {@link Stream}.
	 */
	public static <T> Stream<T> streamof(Iterable<T> vals) {
		
		return streamof(vals,false);
	}
	
	/**
	 * Turns an {@link Iterable} into a {@link Stream}.
	 */
	public static <T> Stream<T> streamof(Iterable<T> vals, boolean parallel) {
		
		return stream(vals.spliterator(),parallel);
	}
	
	/**
	 * <code>null</code>-free string joins.
	 */
	public static String join(Stream<String> vals) {
		
		return vals.filter(s->s!=null).collect(joining());
	}
	
}
