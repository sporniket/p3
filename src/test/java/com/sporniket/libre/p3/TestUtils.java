/**
 * 
 */
package com.sporniket.libre.p3;

import com.sporniket.libre.io.parser.properties.MultipleLinePropertyParsedEvent;

/**
 * Utility class.
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
 * @since 1
 */
abstract class TestUtils
{
	/**
	 * Create an already setup {@link P3}.
	 * 
	 * @param directives
	 * @return
	 */
	public static P3 createP3(String[] directives)
	{
		P3 _result = new P3();
		MultipleLinePropertyParsedEvent _setupEvent = new MultipleLinePropertyParsedEvent(P3.DEFAULT_PROPERTY_NAME_FOR_DIRECTIVES,
				directives);
		_result.onMultipleLinePropertyParsed(_setupEvent);
		return _result;
	}
}
