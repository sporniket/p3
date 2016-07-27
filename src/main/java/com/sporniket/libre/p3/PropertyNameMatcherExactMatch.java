/**
 * 
 */
package com.sporniket.libre.p3;

/**
 * {@link PropertyNameMatcher} that looks for an exact match against a specific name.
 * 
 * @author dsporn
 *
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
