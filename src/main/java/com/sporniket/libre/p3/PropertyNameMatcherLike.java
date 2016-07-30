/**
 * 
 */
package com.sporniket.libre.p3;

import java.util.regex.Pattern;

/**
 * {@link PropertyNameMatcher} that looks for a match with a regular expression.
 * 
 * @author dsporn
 *
 */
public class PropertyNameMatcherLike implements PropertyNameMatcher
{
	/**
	 * Regular expression to match.
	 */
	private final Pattern myPattern;

	public PropertyNameMatcherLike(String pattern)
	{
		myPattern = Pattern.compile(pattern);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sporniket.libre.p3.PropertyNameMatcher#isMatching(java.lang.String)
	 */
	@Override
	public boolean isMatching(String propertyName)
	{
		return getPattern().matcher(propertyName).matches();
	}

	private Pattern getPattern()
	{
		return myPattern;
	}

}
