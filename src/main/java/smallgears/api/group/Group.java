package smallgears.api.group;

import static java.lang.String.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static smallgears.api.Apikit.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import smallgears.api.traits.Streamable;

/**
 * A mutable group of uniquely named elements, for extension.
 * <p>
 * Subclasses bind <code>SELF</code> and provide a naming function. 
 * Optionally, they override add/remove hooks.
 * 
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class Group<E,SELF extends Group<E,SELF>> implements Streamable<E> {

	private final Map<String,E> elements = synchronizedMap(new HashMap<>());

	@NonNull
	private final Function<E,String> name;

	@Override
	public Iterator<E> iterator() {
		return elements.values().iterator();
	}
	
	/**
	 * Add elements to this group.
	 */
	public SELF add(@SuppressWarnings("unchecked") @NonNull E ... elements)  {
		
		return add(asList(elements));
	}
	
	/**
	 * Add elements to this group.
	 */
	public SELF add(@NonNull SELF other)  {
		
		return add(other.elements());
	}
	
	/**
	 * Add elements to this group.
	 */
	public SELF add(@NonNull Iterable<E> elements)  {
		
		elements.forEach(this::add);
		
		return self();
	}
	
	
	protected void add(@NonNull E e)  {
		
		this.elements.put(name.apply(e),e);
		
	}
	
	/**
	 * Remove elements from this group.
	 */
	public SELF remove(@SuppressWarnings("unchecked") @NonNull E ... elements)  {
		
		return remove(asList(elements));
	
	}
	
	/**
	 * Remove elements from this group.
	 */
	public SELF remove(@NonNull SELF other)  {
		
		return remove(other.elements());
	}

	/**
	 * Remove elements from this group.
	 */
	public SELF remove(@NonNull Iterable<E> elements)  {
		
		elements.forEach(e->remove(name.apply(e)));
	
		return self();
	}
	
	/**
	 * Remove elements from this group.
	 */
	public SELF remove(@NonNull String ... names)  {
		
		Stream.of(names).forEach(this::remove);
		
		return self();
	}
	
	/**
	 * Remove elements from this group.
	 */
	protected void remove(@NonNull String name)  {
		
		this.elements.remove(name);
		
	}

	/**
	 * <code>true<code> if this has group has given elements.
	 */
	public boolean has(@NonNull String ... names)  {
		
		return has(asList(names));
	
	}
	
	/**
	 * <code>true<code> if this has group has given elements.
	 */
	public boolean has(@SuppressWarnings("unchecked") @NonNull E ... elements)  {
		
		return has(asList(elements));
	
	}

	/**
	 * <code>true<code> if this has group has given elements.
	 */
	public boolean has(@NonNull SELF other)  {
		
		return has(other.names());
	}

	/**
	 * <code>true<code> if this has group has given elements.
	 */
	public boolean has(@NonNull Iterable<String> names)  {
		
		return streamof(names).allMatch(this.elements::containsKey);
	
	}
	
	/**
	 * <code>true<code> if this has group has given elements.
	 */
	public boolean has(@NonNull Collection<E> elements)  {
		
		return streamof(elements).allMatch(this.elements::containsValue);
	
	}
	
	/**
	 * An element in this group.
	 */
	public Optional<E> get(@NonNull String name) {
		
		return Optional.ofNullable(elements.get(name));
	}
	
	/**
	 * An element in this group, or another if the first does not exist.
	 */
	public E getOr(@NonNull String name, E fallback) {
		
		Optional<E> e = get(name);
		
		return e.isPresent()? e.get() : fallback;
	}
	
	/**
	 * The number of elements in this group.
	 */
	public int size() {
		return elements.size();
	}
	
	/**
	 * <code>true</code> if there are no elements in this group.
	 * */
	public boolean empty() {
		return size()==0;
	}
	
	/**
	 * The elements in this group, in a detached collection.
	 */
	public Set<E> elements() {
		return new HashSet<>(elements.values());
	}
	
	/**
	 * The name of elements in this group, in a detached collection.
	 */
	public Set<String> names() {
		return new HashSet<>(elements.keySet());
	}
	
	@Override
	public String toString() {
		
		Function<Entry<String,E>,String> tostring = $->format("%s=%s",$.getKey(),$.getValue());
		
		return format("[%s]", elements.entrySet().stream().map(tostring).collect(joining(",")));
	}

	
	
	////////////////////////////////////////////////////////////////////////////////////
	
	private SELF self() {
		
		@SuppressWarnings("all")
		SELF self = (SELF) this;
		
		return self;
	}
}
