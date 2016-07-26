/**
 * 
 */
package com.sporniket.libre.p3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	 * A <em>processor</em> is an object's method accepting the property name and the property value (a <code>String</code> or a
	 * <code>String[]</code>) .
	 * 
	 * The object owning the method is the processor holder.
	 * 
	 * @author dsporn
	 *
	 */
	private static class ProcessorSpec
	{
		public final Method myProcessor;

		public final Object myProcessorHolder;

		public ProcessorSpec(Object processorHolder, Method processor)
		{
			myProcessorHolder = processorHolder;
			myProcessor = processor;
		}

		public Method getProcessor()
		{
			return myProcessor;
		}

		public Object getProcessorHolder()
		{
			return myProcessorHolder;
		}

		/**
		 * Call the processor on a single line property.
		 * 
		 * @param name
		 *            value name.
		 * @param value
		 *            value.
		 */
		public void process(String name, String value)
		{
			try
			{
				getProcessor().invoke(getProcessorHolder(), name, value);
			}
			catch (IllegalAccessException _exception)
			{
				throw new RuntimeException(_exception);
			}
			catch (IllegalArgumentException _exception)
			{
				throw new RuntimeException(_exception);
			}
			catch (InvocationTargetException _exception)
			{
				throw new RuntimeException(_exception);
			}
		}

		/**
		 * Call the processor on a multiple line property.
		 * 
		 * @param name
		 *            value name.
		 * @param value
		 *            value.
		 */
		public void process(String name, String[] value)
		{
			try
			{
				getProcessor().invoke(getProcessorHolder(), name, value);
			}
			catch (IllegalAccessException _exception)
			{
				throw new RuntimeException(_exception);
			}
			catch (IllegalArgumentException _exception)
			{
				throw new RuntimeException(_exception);
			}
			catch (InvocationTargetException _exception)
			{
				throw new RuntimeException(_exception);
			}
		}
	}

	/**
	 * A processing rule is a list of {@link ProcessorSpec} restricted by a {@link PropertyNameMatcher}.
	 * 
	 * @author dsporn
	 *
	 */
	private static class RuleSpec
	{
		private final PropertyNameMatcher myMatcher;

		private final List<ProcessorSpec> myProcessors;

		public RuleSpec(PropertyNameMatcher matcher, List<ProcessorSpec> processors)
		{
			myMatcher = matcher;
			myProcessors = new ArrayList<P3.ProcessorSpec>(processors);
		}

		public PropertyNameMatcher getMatcher()
		{
			return myMatcher;
		}

		public List<ProcessorSpec> getProcessors()
		{
			return myProcessors;
		}

	}

	/**
	 * Rule specifications for processing multiple line properties.
	 */
	private final List<RuleSpec> myProcessorRuleSpecsForMultipleLineProperty = new ArrayList<P3.RuleSpec>();

	/**
	 * Rule specifications for processing single line properties.
	 */
	private final List<RuleSpec> myProcessorRuleSpecsForSingleLineProperty = new ArrayList<P3.RuleSpec>();

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
		for (RuleSpec _rule : getProcessorRuleSpecsForMultipleLineProperty())
		{
			if (_rule.getMatcher().isMatching(event.getName()))
			{
				for (ProcessorSpec _processor : _rule.getProcessors())
				{
					_processor.process(event.getName(), event.getValue());
				}
				break;
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
		for (RuleSpec _rule : getProcessorRuleSpecsForSingleLineProperty())
		{
			if (_rule.getMatcher().isMatching(event.getName()))
			{
				for (ProcessorSpec _processor : _rule.getProcessors())
				{
					_processor.process(event.getName(), event.getValue());
				}
				break;
			}
		}
	}

	private List<RuleSpec> getProcessorRuleSpecsForMultipleLineProperty()
	{
		return myProcessorRuleSpecsForMultipleLineProperty;
	}

	private List<RuleSpec> getProcessorRuleSpecsForSingleLineProperty()
	{
		return myProcessorRuleSpecsForSingleLineProperty;
	}

}
