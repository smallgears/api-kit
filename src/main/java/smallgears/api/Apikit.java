package smallgears.api;

import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.*;

import java.util.stream.Stream;

import lombok.experimental.UtilityClass;


@UtilityClass
public class Apikit {

	
	
	//////////////////////////////////////////////////////////////////////////////// streams
	
	/**
	 * Turns an {@link Iterable} into a sequential {@link Stream}.
	 */
	public <T> Stream<T> streamof(Iterable<T> vals) {
		
		return streamof(vals,false);
	}
	
	/**
	 * Turns an {@link Iterable} into a {@link Stream}.
	 */
	public <T> Stream<T> streamof(Iterable<T> vals, boolean parallel) {
		
		return stream(vals.spliterator(),parallel);
	}
	
	/**
	 * A <code>null</code>-free string join.
	 */
	public String join(Stream<String> vals) {
		
		return vals.filter(s->s!=null).collect(joining());
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////// faults

	/**
	 * An unchecked wrapper for an exception.
	 * @return the wrapper, or the original exception if already unchcked.
	 */
	public RuntimeException unchecked(Throwable t) {
	
		return 
				t instanceof RuntimeException? 
						RuntimeException.class.cast(t) :
						new RuntimeException(t);
	}


	/**
	 * An unchecked wrapper for an exception, even those that are already unchecked.
	 */
	public RuntimeException unchecked(String msg, Throwable t) {
		
		msg = msg + " (see cause) ";
		return 
				t instanceof IllegalArgumentException? new IllegalArgumentException(msg,t) :
				t instanceof IllegalStateException ? new IllegalStateException(msg,t) :
				new RuntimeException(msg,t);
	}
	
	/**
	 * Throws the exception returned by {@link #unchecked(String, Throwable)}. 
	 */
	public void rethrow(String msg,Throwable t) throws RuntimeException {
		
		throw unchecked(msg,t);

	}
	
	/**
	 * Throws the exception returned by {@link #unchecked(Throwable)}. 
	 */
	public void rethrow(Throwable t) throws RuntimeException {
		
		throw unchecked(t);

	}
	
}
