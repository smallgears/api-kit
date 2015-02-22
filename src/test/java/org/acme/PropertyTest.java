package org.acme;


import static org.junit.Assert.*;
import static smallgears.api.properties.Properties.*;

import org.junit.Test;

import smallgears.api.properties.Properties;
import smallgears.api.properties.Property;

public class PropertyTest {

	//test with DSL compromises test modularity and convenience.
	
	@Test
	public void property() {

		Property p = prop("n","v");
		
		assertEquals(p,prop("n","v"));
		
		assertTrue(p.is(String.class));
		assertEquals("v",p.as(Object.class));
		assertEquals("v",p.value());
		assertEquals("n",p.name());

		p.value("new");
		assertEquals("new",p.value());
	}
	
	@Test
	public void properties() {

		Property p1 = prop("n1","v1");
		Property p2 = prop("n2","v2");
		
		Properties ps = props(p1,p2);
		
		assertEquals(ps,props(p1,p2));
		
		assertTrue(ps.has(p1,p2));
		assertTrue(ps.has(p1.name(),p2.name()));
		assertTrue(ps.has(props(p1,p2)));
		
		Property p3 = prop("n3","v3");

		assertFalse(ps.has(p3));
		
		try {
			ps.prop("bad");
			fail();
		}
		catch(IllegalStateException matternot) {}
		
		assertEquals(prop("bad","default"),ps.propOr("bad", "default"));
		
		Property p4 = prop("n4","v4");
		
		ps.add(p3,p4);
		
		assertEquals(ps,props(p1,p2,p3,p4));
		
		ps.remove(p1,p3);
		
		assertEquals(ps,props(p4,p2));
		
		ps.remove(props(p2,p4));
		
		assertTrue(ps.empty());


	}
	
	
}
