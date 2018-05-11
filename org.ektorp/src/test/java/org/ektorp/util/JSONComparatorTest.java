package org.ektorp.util;

import static org.junit.Assert.*;

import org.apache.commons.io.*;
import org.junit.*;

import java.nio.charset.StandardCharsets;

public class JSONComparatorTest {

	String jsonA;
	String jsonB;
	String jsonC;
	
	@Before
	public void setup() throws Exception {
		// doc a and b are equal but have different field order, doc c is not equal
		jsonA = IOUtils.toString(this.getClass().getResourceAsStream("doc_a.json"), StandardCharsets.UTF_8);
		jsonB = IOUtils.toString(this.getClass().getResourceAsStream("doc_b.json"), StandardCharsets.UTF_8);
		jsonC = IOUtils.toString(this.getClass().getResourceAsStream("doc_c.json"), StandardCharsets.UTF_8);
	}
	
	@Test
	public void testAreEqual() {
		assertTrue(JSONComparator.areEqual(jsonA, jsonB));
	}
	
	@Test
	public void testAreNotEqual() {
		assertFalse(JSONComparator.areEqual(jsonA, jsonC));
	}
	
	@Test
	public void testAreEqual2() {
		assertTrue(JSONComparator.areEqual(jsonA, jsonA));
	}

}
