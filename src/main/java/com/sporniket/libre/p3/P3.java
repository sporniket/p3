/**
 * 
 */
package com.sporniket.libre.p3;

import java.util.ArrayList;
import java.util.List;

import com.sporniket.libre.io.parser.properties.MultipleLinePropertyParsedEvent;
import com.sporniket.libre.io.parser.properties.PropertiesParsingListener;
import com.sporniket.libre.io.parser.properties.SingleLinePropertyParsedEvent;

/**
 * @author dsporn
 *
 */
public class P3 implements PropertiesParsingListener
{
	/**
	 * A <em>processor</em> is defined by the {@link PropertyNameMatcher} that select the properties for which it is appliable, and
	 * the {@link PropertiesParsingListener} that implement the actual processing.
	 * 
	 * @author dsporn
	 *
	 */
	private static class Processor
	{
		public final PropertiesParsingListener myListener;

		public final PropertyNameMatcher myMatcher;

		public Processor(PropertyNameMatcher matcher, PropertiesParsingListener listener)
		{
			myMatcher = matcher;
			myListener = listener;
		}

		private PropertiesParsingListener getListener()
		{
			return myListener;
		}

		private PropertyNameMatcher getMatcher()
		{
			return myMatcher;
		}
	}
	
	private final List<Processor> myProcessors = new ArrayList<P3.Processor>() ;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sporniket.libre.io.parser.properties.PropertiesParsingListener#onMultipleLinePropertyParsed(com.sporniket.libre.io.parser
	 * .properties.MultipleLinePropertyParsedEvent)
	 */
	@Override
	public void onMultipleLinePropertyParsed(MultipleLinePropertyParsedEvent event)
	{
		for (Processor _p : getProcessors())
		{
			if (_p.getMatcher().isMatching(event.getName()))
			{
				_p.getListener().onMultipleLinePropertyParsed(event);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sporniket.libre.io.parser.properties.PropertiesParsingListener#onSingleLinePropertyParsed(com.sporniket.libre.io.parser
	 * .properties.SingleLinePropertyParsedEvent)
	 */
	@Override
	public void onSingleLinePropertyParsed(SingleLinePropertyParsedEvent event)
	{
		for (Processor _p : getProcessors())
		{
			if (_p.getMatcher().isMatching(event.getName()))
			{
				_p.getListener().onSingleLinePropertyParsed(event);
			}
		}
	}

	private List<Processor> getProcessors()
	{
		return myProcessors;
	}

}
