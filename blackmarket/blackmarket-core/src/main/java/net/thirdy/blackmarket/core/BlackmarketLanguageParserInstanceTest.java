package net.thirdy.blackmarket.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlackmarketLanguageParserInstanceTest {

	@Test
	public void testParse() throws Exception {
		BlackmarketLanguageParserInstance bm = new BlackmarketLanguageParserInstance();
		String s = bm.processToken("ring");
		System.out.println(s);
	}
	
	@Test
	public void testParse2() throws Exception {
		BlackmarketLanguageParserInstance bm = new BlackmarketLanguageParserInstance();
		String s = bm.processToken("30life");
		System.out.println(s);
	}
	
	@Test
	public void testParse3() throws Exception {
		BlackmarketLanguageParserInstance bm = new BlackmarketLanguageParserInstance();
		String s = bm.parse("ring 60life");
		System.out.println(s);
	}
	
	@Test
	public void testParse4() throws Exception {
		BlackmarketLanguageParserInstance bm = new BlackmarketLanguageParserInstance();
		String s = bm.parse("ring 60life 30res");
		System.out.println(s);
	}

}
