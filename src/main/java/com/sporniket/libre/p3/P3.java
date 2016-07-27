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
 * P3 (Programmable Property Processor) is a {@link PropertiesParsingListener} that will dispatch received
 * {@link SingleLinePropertyParsedEvent} and {@link MultipleLinePropertyParsedEvent} to other {@link PropertiesParsingListener}
 * according to the property name of the event.
 * 
 * To setup this dispatching, this listener wait for a specific property (by default,
 * <code>{@link #DEFAULT_PROPERTY_NAME_FOR_DIRECTIVES}={@value #DEFAULT_PROPERTY_NAME_FOR_DIRECTIVES}</code>) containing a list of
 * directives written using the <a href="http://github.com/sporniket/sslpoi">Sporny Scripting Language</a>.
 * 
 * A typical script will looks like :
 * 
 * <pre>
 * define foo as new com.foo.Foo
 * define bar as new com.foo.Bar
 * 
 * on onSingleLinePropertyParsed with a String named name, a String named value
 *     if name is like "foo\\..*"
 *         call processFoo from foo using name as name, value as value
 *     else if name is like "foo2\\..*"
 *         call processFoo2 from foo using name as name, value as value
 *     else
 *         call process from foo using name as name, value as value
 *     endif
 * endon
 * 
 * on onMultipleLinePropertyParsed with a String named name, a String[] named value
 *     if name is "bar"
 *         call processBar from bar using name as name, value as value
 *         call process from foo using name as name, value as value
 *     endif
 * endon
 * </pre>
 * 
 * A valid processor is a method that accepts two parameters : a String that is the property name, and a String or a String array
 * that is the property value. Thus, in the example, the class <code>com.foo.Foo</code> and <code>com.foo.Bar</code> look like :
 * 
 * <pre>
 * package com.foo;
 * 
 * //...
 * public class Foo
 * {
 * 	// ...
 * 	public void process(String name, String value) {...}
 * 
 * 	public void process(String name, String[] value) {...}
 * 
 * 	public void processFoo(String name, String value) {...}
 * 
 * 	public void processFoo2(String name, String value) {...}
 * 	// ...
 * }
 * 
 * // ...
 * public class Bar
 * {
 * 	// ...
 * 	public void processBar(String name, String value) {...}
 * 	// ...
 * }
 * </pre>
 * 
 * The processor selection is done by comparing the property name to a specific value (e.g. <code>is "specific.value"</code>) or to
 * a pattern (a regexp, e.g. <code>is like "my\\.regexp"</code>). More than one processor may be called for matched property name.
 * 
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

		@SuppressWarnings("unused")
		public RuleSpec(PropertyNameMatcher matcher, List<ProcessorSpec> processors)
		{
			myMatcher = matcher;
			myProcessors = new ArrayList<P3.ProcessorSpec>(processors);
		}

		public RuleSpec(PropertyNameMatcher matcher, ProcessorSpec singleProcessor)
		{
			myMatcher = matcher;
			myProcessors = new ArrayList<P3.ProcessorSpec>(1);
			myProcessors.add(singleProcessor);
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

	public static final String DEFAULT_PROPERTY_NAME_FOR_DIRECTIVES = "__DIRECTIVES__";

	private static final String METHOD_NAME__DIRECTIVES_PROCESSOR = "executeProgram";

	/**
	 * Rule specifications for processing multiple line properties.
	 */
	private final List<RuleSpec> myProcessorRuleSpecsForMultipleLineProperty = new ArrayList<P3.RuleSpec>();

	/**
	 * Rule specifications for processing single line properties.
	 */
	private final List<RuleSpec> myProcessorRuleSpecsForSingleLineProperty = new ArrayList<P3.RuleSpec>();

	/**
	 * Create a P3 looking for directives from the property {@link #DEFAULT_PROPERTY_NAME_FOR_DIRECTIVES}.
	 */
	public P3()
	{
		this(DEFAULT_PROPERTY_NAME_FOR_DIRECTIVES);
	}

	/**
	 * Create a P3 looking for directives from the specified property.
	 * 
	 * @param directivesName
	 */
	public P3(String directivesName)
	{
		PropertyNameMatcherExactMatch _matcher = new PropertyNameMatcherExactMatch(directivesName);

		try
		{
			Method _processorSingleLineProperty = this.getClass().getMethod(METHOD_NAME__DIRECTIVES_PROCESSOR, String.class, String.class);
			ProcessorSpec _processorSpecSingleLineProperty = new ProcessorSpec(this, _processorSingleLineProperty);
			RuleSpec _ruleSpecSingleLineProperty = new RuleSpec(_matcher, _processorSpecSingleLineProperty);
			getProcessorRuleSpecsForSingleLineProperty().add(_ruleSpecSingleLineProperty);

			Method _processorMultipleLineProperty = this.getClass().getMethod(METHOD_NAME__DIRECTIVES_PROCESSOR, String.class, String[].class);
			ProcessorSpec _processorSpecMultipleLineProperty = new ProcessorSpec(this, _processorMultipleLineProperty);
			RuleSpec _ruleSpecMultipleLineProperty = new RuleSpec(_matcher, _processorSpecMultipleLineProperty);
			getProcessorRuleSpecsForMultipleLineProperty().add(_ruleSpecMultipleLineProperty);
		}
		catch (NoSuchMethodException | SecurityException _exception)
		{
			throw new RuntimeException(_exception);
		}
	}

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

	/**
	 * The processor for extracting directives.
	 * 
	 * @param name
	 *            property name, unused.
	 * @param source
	 *            the directives.
	 * @throws Exception
	 *             when there is a problem.
	 */
	@SuppressWarnings("unused")
	private void executeProgram(String name, String source) throws Exception
	{
		String[] _sourceAsStringArray = new String[]
		{
			source
		};
		executeProgram(name, _sourceAsStringArray);
	}

	/**
	 * The processor for extracting directives.
	 * 
	 * @param name
	 *            property name, unused.
	 * @param source
	 *            the directives.
	 * @throws Exception
	 *             when there is a problem.
	 */
	private void executeProgram(String name, String[] source) throws Exception
	{
		throw new Exception("not implemented yet !");
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
