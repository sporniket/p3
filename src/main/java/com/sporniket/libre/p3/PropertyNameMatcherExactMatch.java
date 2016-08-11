/**
 * 
 */
package com.sporniket.libre.p3;

/**
 * {@link PropertyNameMatcher} that looks for an exact match against a specific name.
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
 * @version 1
 * @since 1
 */
class PropertyNameMatcherExactMatch implements PropertyNameMatcher
{
	private final String myName;

	public PropertyNameMatcherExactMatch(String name)
	{
		myName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sporniket.libre.p3.PropertyNameMatcher#isMatching(java.lang.String)
	 */
	@Override
	public boolean isMatching(String propertyName)
	{
		return getName().equals(propertyName);
	}

	private String getName()
	{
		return myName;
	}

}
