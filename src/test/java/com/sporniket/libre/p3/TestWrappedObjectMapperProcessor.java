package com.sporniket.libre.p3;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.sporniket.libre.io.parser.properties.MultipleLinePropertyParsedEvent;
import com.sporniket.libre.io.parser.properties.SingleLinePropertyParsedEvent;
import com.sporniket.libre.p3.builtins.WrappedObjectMapperProcessor;

/**
 * Test for {@link WrappedObjectMapperProcessor}.
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
 * <i>P3, the Programmable Properties Processor</i> is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * <p>
 * <i>P3, the Programmable Properties Processor</i> is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * <p>
 * You should have received a copy of the GNU General Public License along with <i>P3, the Programmable Properties Processor</i>. If
 * not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 * 
 * <hr>
 * 
 * @author David SPORN
 * @version 2
 * @since 2
 */
public class TestWrappedObjectMapperProcessor
{
	private static final String[] DIRECTIVES =
	{
			"define foo as new com.sporniket.libre.p3.RootMapper",
			"on singleLinePropertyParsed with a String named name, a String named value",
			"    if name is like \".*\"",
			"        call process from foo using name as name, value as value",
			"    endif",
			"",
			"endon",
			"",
			"on multipleLinePropertyParsed with a String named name, a String named value",
			"    if name is like \"child\\\\..*\"",
			"        call process from foo using name as name, value as value",
			"    endif",
			"",
			"endon",
	};

	@Test
	public void testMapper()
	{
		P3 _processor = TestUtils.createP3(DIRECTIVES);
		_processor.onSingleLinePropertyParsed(new SingleLinePropertyParsedEvent("value", "foo"));
		String[] _value =
		{
				"a", "b"
		};
		_processor.onMultipleLinePropertyParsed(new MultipleLinePropertyParsedEvent("child.value", _value));

		Root _root = ((RootMapper) _processor.get("foo")).getRoot();
		assertThat(_root.getValue(), is("foo"));
		assertThat(_root.getChild().getValue()[0], is("a"));
		assertThat(_root.getChild().getValue()[1], is("b"));

	}

}
