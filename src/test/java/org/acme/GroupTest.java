package org.acme;


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

//	///////////////////////////////////////////////////////////////////////////
	
	//for some funny compiler glitch, cannot star-import factory method below..
	static El e(String name) {
		return El.el(name);
	}
	
	@Data 
	@RequiredArgsConstructor(staticName="el")
	public static class El {
		
		final String name;
	}
	
	public static class G extends Group<El,G>{
		
		G() { 
			super(El::name);
		}
	}
	
	public static class G2 extends Group<El,G2>{
		
		G2(El ... es) { 
			super(El::name);
			add(es);
		}
		
		@Override
		protected void add(El el) {
			
			if (!el.equals((El.el("bad"))))
				super.add(el);
		}
		
		@Override
		protected El remove(String s) {
			
			return s.equals("good")? null : super.remove(s);
		}
	}
}
