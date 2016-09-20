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
 * @version 5
 * @since 1
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
