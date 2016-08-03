/**
 * 
 */
package com.sporniket.libre.p3;

import java.util.HashMap;
import java.util.Map;

/**
 * Processor holder for {@link P3} that stores the values in a map, multiple line properties are converted into a single
 * {@link String} with each line separated by a '\n'.
 * 
 * @author dsporn
 *
 */
class PropertiesCatcher
{
	private final Map<String, String> myProperties = new HashMap<String, String>();

	public Map<String, String> getProperties()
	{
		return myProperties;
	}

	public void store(String name, String value)
	{
		getProperties().put(name, value);
	}

	public void store(String name, String[] value)
	{
		store(name, String.join("\n", value));
	}
}
