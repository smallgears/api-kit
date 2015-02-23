package org.acme;


import static org.acme.GroupTest.E.*;
import static org.junit.Assert.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.junit.Test;

import smallgears.api.group.Group;

public class GroupTest {

	
	
	@Test
	public void cruds() {
		
		G g = new G();
		
		assertTrue(g.empty());
		
		g.add(e("1"),e("2"));

		assertEquals(2,g.size());
		
		assertTrue(g.has(e("1"),e("2")));
		assertTrue(g.has("1","2"));
		assertFalse(g.has(e("1"),e("2"),e("3")));
		
		assertTrue(g.get("1").isPresent());
		assertFalse(g.get("3").isPresent());
		
		assertEquals(e("3"),g.getOr("3",e("3")));
		
		g.remove(e("1"),e("3"));
		g.remove("2");
		
		assertTrue(g.empty());
	}
	
	@Test
	public void hooks() {

		G2 g = new G2(e("good"),e("bad"));

		assertTrue(g.has("good"));
		assertFalse(g.has("bad"));
		
		g.remove("good");
		assertTrue(g.has("good"));

	}
	
	///////////////////////////////////////////////////////////////////////////
	
	@Data
	@RequiredArgsConstructor(staticName="e") 
	static class E {
		final String name;
	}
	
	static class G extends Group<E,G>{
		
		G() { 
			super(E::name);
		}
	}
	
	static class G2 extends Group<E,G>{
		
		G2(E ... es) { 
			super(E::name);
			add(es);
		}
		
		@Override
		protected void add(E e) {
			
			if (!e.equals((e("bad"))))
				super.add(e);
		}
		
		@Override
		protected void remove(String s) {
			
			if (!s.equals("good"))
				super.remove(s);
		}
	}
}
