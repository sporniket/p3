/**
 * 
 */
package com.sporniket.libre.p3;

import com.sporniket.libre.io.parser.properties.MultipleLinePropertyParsedEvent;

/**
 * Utility class.
 * @author dsporn
 *
 */
abstract class TestUtils
{
	/**
	 * Create an already setup {@link P3}.
	 * @param directives
	 * @return
	 */
	public static P3 createP3(String[] directives)
	{
		P3 _result = new P3() ;
		MultipleLinePropertyParsedEvent _setupEvent = new MultipleLinePropertyParsedEvent(P3.DEFAULT_PROPERTY_NAME_FOR_DIRECTIVES, directives);
		_result.onMultipleLinePropertyParsed(_setupEvent);
		return _result ;
	}
}
