package net.thirdy.blackmarket.core;

import static org.junit.Assert.*;


import org.junit.Test;

public class SearchPayloadTest {

	@Test
	public void test() throws Exception {
		String sNewLine = "modmax=" + "\n" +
					"mods=(pseudo) (total) +# to maximum Life" + "\n" +
					"modexclude=";
		SearchPayload sp = new SearchPayload(sNewLine);
		String r = sp.getPayloadFormatted();
		
		assertEquals("modmax=&mods=%28pseudo%29+%28total%29+%2B%23+to+maximum+Life&modexclude=", r);
	}

	@Test
	public void test2() throws Exception {
		String sNewLineAndReturnCar = "modmax=" + "\r\n" +
				"mods=(pseudo) (total) +# to maximum Life" + "\r\n" +
				"modexclude=";

		String r = new SearchPayload(sNewLineAndReturnCar).getPayloadFormatted();
		
		assertEquals("modmax=&mods=%28pseudo%29+%28total%29+%2B%23+to+maximum+Life&modexclude=", r);
	}

	@Test
	public void test3() throws Exception {
		String sWithComment =
				"# comment1" + "\r\n" +
				"// comment2" + "\r\n" +
				"modmax=" + "\r\n" +
				"mods=(pseudo) (total) +# to maximum Life" + "\r\n" +
				"modexclude=";

		String r = new SearchPayload(sWithComment).getPayloadFormatted();
		
		assertEquals("modmax=&mods=%28pseudo%29+%28total%29+%2B%23+to+maximum+Life&modexclude=", r);
	}
}
