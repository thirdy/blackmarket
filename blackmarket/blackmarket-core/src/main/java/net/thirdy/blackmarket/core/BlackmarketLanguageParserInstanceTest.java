package net.thirdy.blackmarket.core;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import net.thirdy.blackmarket.core.util.BlackmarketConfig;

public class BlackmarketLanguageParserInstanceTest {
	
	@BeforeClass
	public static void setup() {
		BlackmarketConfig.setupConfigFiles();
	}

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
	
	@Test
	public void testParse5() throws Exception {
		BlackmarketLanguageParserInstance bm = new BlackmarketLanguageParserInstance();
		String s = bm.parse("boot 60eleres unid");
		System.out.println(s);
	}
	
	@Test
	public void testParse_links_and_sockets() throws Exception {
		BlackmarketLanguageParserInstance bm = new BlackmarketLanguageParserInstance();
		String s = bm.parse("chest 6s5l");
		System.out.println(s);
		
		bm = new BlackmarketLanguageParserInstance();
		s = bm.parse("glove 4s");
		System.out.println(s);
	}

}
