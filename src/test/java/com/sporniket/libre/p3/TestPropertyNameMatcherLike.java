/**
 * 
 */
package com.sporniket.libre.p3;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import com.sporniket.libre.io.parser.properties.SingleLinePropertyParsedEvent;

/**
 * @author dsporn
 *
 */
public class TestPropertyNameMatcherLike
{
	private static final String[] DIRECTIVES =
	{
			"define foo as new com.sporniket.libre.p3.PropertiesCatcher",
			"on singleLinePropertyParsed with a String named name, a String named value",
			"    if name is like \"catched\\\\..*\"",
			"        call store from foo using name as name, value as value",
			"    endif",
			"",
			"endon"
	};

	@Test
	public void testExactMatch()
	{
		P3 _processor = TestUtils.createP3(DIRECTIVES);
		_processor.onSingleLinePropertyParsed(new SingleLinePropertyParsedEvent("not.catched", "whatever"));
		_processor.onSingleLinePropertyParsed(new SingleLinePropertyParsedEvent("catched.foo", "foo"));
		_processor.onSingleLinePropertyParsed(new SingleLinePropertyParsedEvent("catched.bar", "bar"));

		Map<String, String> _properties = ((PropertiesCatcher) _processor.get("foo")).getProperties();
		assertThat(_properties.keySet(), not(hasItem("not.catched")));
		assertThat(_properties.keySet(), hasItem("catched.foo"));
		assertThat(_properties.get("catched.foo"),is("foo"));
		assertThat(_properties.keySet(), hasItem("catched.bar"));
		assertThat(_properties.get("catched.bar"),is("bar"));

	}
}
