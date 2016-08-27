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
 * Unit testing for an exact matching of a property name with an exact value.
 * 
 * <p>
 * &copy; Copyright 2016 David Sporn
 * </p>
 * <hr>
 * 
 * <p>
 * This file is part of <i>P3, the Programmable Properties Processor</i>.
 * 
 * <p>
 * <i>P3, the Programmable Properties Processor</i> is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * <p>
 * <i>P3, the Programmable Properties Processor</i> is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * <p>
 * You should have received a copy of the GNU General Public License along with <i>P3, the Programmable Properties Processor</i>.
 * If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 * 
 * <hr>
 * 
 * @author David SPORN 
 * @version 2
 * @since 1
 */
public class TestPropertyNameMatcherExactMatch
{
	private static final String[] DIRECTIVES =
	{
			"define foo as new com.sporniket.libre.p3.PropertiesCatcher",
			"on singleLinePropertyParsed with a String named name, a String named value",
			"    if name is \"catched\"",
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
		_processor.onSingleLinePropertyParsed(new SingleLinePropertyParsedEvent("catched", "foo"));

		Map<String, String> _properties = ((PropertiesCatcher) _processor.get("foo")).getProperties();
		assertThat(_properties.keySet(), not(hasItem("not.catched")));
		assertThat(_properties.keySet(), hasItem("catched"));
		assertThat(_properties.get("catched"),is("foo"));

	}
}
