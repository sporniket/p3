/**
 * 
 */
package com.sporniket.libre.p3;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

import com.sporniket.libre.io.parser.properties.MultipleLinePropertyParsedEvent;
import com.sporniket.libre.io.parser.properties.PropertiesParsingListener;
import com.sporniket.libre.io.parser.properties.SingleLinePropertyParsedEvent;
import com.sporniket.scripting.sslpoi.core.InitialisationMode;
import com.sporniket.scripting.sslpoi.core.SslpoiException;
import com.sporniket.scripting.sslpoi.mass.PartialExpression;
import com.sporniket.scripting.sslpoi.mass.PartialExpressionLiteralString;
import com.sporniket.scripting.sslpoi.mass.PartialExpressionLogical;
import com.sporniket.scripting.sslpoi.mass.PartialIdentifier;
import com.sporniket.scripting.sslpoi.mass.Statement;
import com.sporniket.scripting.sslpoi.mass.StatementAlternative;
import com.sporniket.scripting.sslpoi.mass.StatementCall;
import com.sporniket.scripting.sslpoi.mass.StatementDefineAs;
import com.sporniket.scripting.sslpoi.mass.StatementFromNode;
import com.sporniket.scripting.sslpoi.mass.StatementIf;
import com.sporniket.scripting.sslpoi.mass.StatementOn;
import com.sporniket.scripting.sslpoi.vess.AnalyzerLexical;
import com.sporniket.scripting.sslpoi.vess.AnalyzerSyntaxic;
import com.sporniket.scripting.sslpoi.vess.VessNode;

/**
 * P3 (Programmable Property Processor) is a {@link PropertiesParsingListener} that will dispatch received
 * {@link SingleLinePropertyParsedEvent} and {@link MultipleLinePropertyParsedEvent} to other {@link PropertiesParsingListener}
 * according to the property name of the event.
 * 
 * <p>
 * To setup this dispatching, this listener wait for a specific property (by default,
 * <code>{@link #DEFAULT_PROPERTY_NAME_FOR_DIRECTIVES}={@value #DEFAULT_PROPERTY_NAME_FOR_DIRECTIVES}</code>) containing a list of
 * directives written using the <a href="http://github.com/sporniket/sslpoi">Sporny Scripting Language</a>.
 * 
 * <p>
 * A typical script will looks like :
 * 
 * <pre>
 * define foo as new com.foo.Foo
 * define bar as new com.foo.Bar
 * 
 * on singleLinePropertyParsed with a String named name, a String named value
 *     if name is like "foo\\..*"
 *         call processFoo from foo using name as name, value as value
 *     else if name is like "foo2\\..*"
 *         call processFoo2 from foo using name as name, value as value
 *     else
 *         call process from foo using name as name, value as value
 *     endif
 * endon
 * 
 * on multipleLinePropertyParsed with a String named name, a String[] named value
 *     if name is "bar"
 *         call processBar from bar using name as name, value as value
 *         call process from foo using name as name, value as value
 *     endif
 * endon
 * </pre>
 * 
 * <p>
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
 * <p>
 * The processor selection is done by comparing the property name to a specific value (e.g. <code>is "specific.value"</code>) or to
 * a pattern (a regexp, e.g. <code>is like "my\\.regexp"</code>). More than one processor may be called for matched property name.
 * 
 * <p>
 * P3 is a <strong>read-only</strong> {@link Map}, objects created in the directives are accessible using the identifier as a key,
 * e.g <code>_p3.get("foo")</code>.
 * 
 * @author dsporn
 *
 */
public class P3 implements PropertiesParsingListener, Map<String, Object>
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

	private static final String EVENT__ON_MULTIPLE_LINE_PROPERTY_PARSED = "multipleLinePropertyParsed";

	private static final String EVENT__ON_SINGLE_LINE_PROPERTY_PARSED = "singleLinePropertyParsed";

	private static final String METHOD_NAME__DIRECTIVES_PROCESSOR = "executeProgram";

	/**
	 * Map for storing objects declared in the directives.
	 */
	private final Map<String, Object> myContext = new HashMap<String, Object>();

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
			Method _processorSingleLineProperty = this.getClass().getMethod(METHOD_NAME__DIRECTIVES_PROCESSOR, String.class,
					String.class);
			ProcessorSpec _processorSpecSingleLineProperty = new ProcessorSpec(this, _processorSingleLineProperty);
			RuleSpec _ruleSpecSingleLineProperty = new RuleSpec(_matcher, _processorSpecSingleLineProperty);
			getProcessorRuleSpecsForSingleLineProperty().add(_ruleSpecSingleLineProperty);

			Method _processorMultipleLineProperty = this.getClass().getMethod(METHOD_NAME__DIRECTIVES_PROCESSOR, String.class,
					String[].class);
			ProcessorSpec _processorSpecMultipleLineProperty = new ProcessorSpec(this, _processorMultipleLineProperty);
			RuleSpec _ruleSpecMultipleLineProperty = new RuleSpec(_matcher, _processorSpecMultipleLineProperty);
			getProcessorRuleSpecsForMultipleLineProperty().add(_ruleSpecMultipleLineProperty);
		}
		catch (NoSuchMethodException | SecurityException _exception)
		{
			throw new RuntimeException(_exception);
		}
	}

	@Override
	public void clear()
	{
		// Silently ignore
	}

	@Override
	public boolean containsKey(Object key)
	{
		return getContext().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return getContext().containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet()
	{
		return new HashSet<Map.Entry<String, Object>>(getContext().entrySet());
	}

	@Override
	public Object get(Object key)
	{
		return getContext().get(key);
	}

	@Override
	public boolean isEmpty()
	{
		return getContext().isEmpty();
	}

	@Override
	public Set<String> keySet()
	{
		return getContext().keySet();
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

	@Override
	public Object put(String key, Object value)
	{
		// Silently ignore
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m)
	{
		// Silently ignore
	}

	@Override
	public Object remove(Object key)
	{
		// Silently ignore
		return null;
	}

	@Override
	public int size()
	{
		return getContext().size();
	}

	@Override
	public Collection<Object> values()
	{
		return getContext().values();
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
	private void executeProgram(String name, String source) throws Exception
	{
		List<Statement> _directives = executeProgram__compile(source);
		if (!_directives.isEmpty())
		{
			executeProgram__parseDirectives(_directives);
		}
		throw new Exception("not implemented yet !");
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
	private void executeProgram(String name, String[] source) throws Exception
	{
		String _singleStringSource = executeProgram__makeSingleStringSource(source);
		executeProgram(name, _singleStringSource);
	}

	private List<Statement> executeProgram__compile(String source) throws Exception, SslpoiException
	{
		AnalyzerSyntaxic _parser = executeProgram__createParser();
		VessNode _parsed = executeProgram__parseSource(source, _parser);
		List<Statement> _directives = new ArrayList<Statement>(StatementFromNode.convertNodeList(_parsed));
		return _directives;
	}

	private AnalyzerSyntaxic executeProgram__createParser()
	{
		final ComplexSymbolFactory _symbolFactory = new ComplexSymbolFactory();
		final AnalyzerLexical _lexer = new AnalyzerLexical(null);
		_lexer.setSymbolFactory(_symbolFactory);
		AnalyzerSyntaxic _parser = new AnalyzerSyntaxic(_lexer, _symbolFactory);
		return _parser;
	}

	private String executeProgram__makeSingleStringSource(String[] source)
	{
		StringBuilder _result = new StringBuilder();
		for (String _line : source)
		{
			if (0 != _result.length())
			{
				_result.append("\n");
			}
			_result.append(_line);
		}
		return _result.toString();
	}

	private void executeProgram__parseDirectives(List<Statement> directives) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, Exception
	{
		// Scan the statement list and apply valid statement. Unvalid or unknown statement will be ignored.
		for (Statement _directive : directives)
		{
			if (_directive instanceof StatementDefineAs)
			{
				executeProgram__parseDirectives__process((StatementDefineAs) _directive);
			}
			else if (_directive instanceof StatementOn)
			{
				executeProgram__parseDirectives__process((StatementOn) _directive);
			}
		}
	}

	private void executeProgram__parseDirectives__process(StatementDefineAs directive) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException
	{
		PartialIdentifier _identifier = directive.getIdentifier();
		if (InitialisationMode.NEW == directive.getInitialisationMode() && !_identifier.isArray())
		{
			Class<?> _class = Class.forName(_identifier.getClassName());
			getContext().put(_identifier.getIdentifier(), _class.newInstance());
		}
	}

	private void executeProgram__parseDirectives__process(StatementOn directive) throws Exception
	{
		List<RuleSpec> _target = null;
		Class<?> _valueType = null;
		switch (directive.getEventName())
		{
			case EVENT__ON_SINGLE_LINE_PROPERTY_PARSED:
				_target = getProcessorRuleSpecsForSingleLineProperty();
				_valueType = String.class;
				break;
			case EVENT__ON_MULTIPLE_LINE_PROPERTY_PARSED:
				_target = getProcessorRuleSpecsForMultipleLineProperty();
				_valueType = String[].class;
				break;
		}
		if (null != _target)
		{
			for (Statement _statement : directive.getStatements())
			{
				if (_statement instanceof StatementIf)
				{
					executeProgram__parseDirectives__processRuleset((StatementIf) _statement, _target, _valueType);
				}
			}
		}
	}

	private void executeProgram__parseDirectives__processRuleset(StatementIf directive, List<RuleSpec> target, Class<?> valueType)
			throws Exception
	{
		for (StatementAlternative _alternative : directive.getAlternatives())
		{
			switch (_alternative.getTest().getOperator())
			{
				case IS:
					executeProgram__parseDirectives__processRuleset__matchExact(_alternative, valueType, target);
					break;
				case IS_LIKE:
					executeProgram__parseDirectives__processRuleset__matchPattern(_alternative, valueType, target);
					break;
			}
		}
	}

	private void executeProgram__parseDirectives__processRuleset__addProcessor(StatementCall directive, Class<?> valueType,
			List<ProcessorSpec> target) throws NoSuchMethodException
	{
		List<String> _methodAccessor = directive.getMethodAccessor();
		if (_methodAccessor.size() == 2)
		{
			String _holderName = _methodAccessor.get(0);
			if (getContext().containsKey(_holderName))
			{
				Object _holder = getContext().get(_holderName);
				Method _processor = _holder.getClass().getMethod(_methodAccessor.get(1), String.class, valueType);
				target.add(new ProcessorSpec(_holder, _processor));
			}
		}
	}

	private List<ProcessorSpec> executeProgram__parseDirectives__processRuleset__listProcessors(
			List<Statement> directives, Class<?> valueType) throws NoSuchMethodException
	{
		List<ProcessorSpec> _processors = new ArrayList<P3.ProcessorSpec>(directives.size());
		for (Statement _directive : directives)
		{
			if (_directive instanceof StatementCall)
			{
				executeProgram__parseDirectives__processRuleset__addProcessor((StatementCall) _directive, valueType, _processors);
			}
		}
		return _processors;
	}

	private void executeProgram__parseDirectives__processRuleset__matchExact(StatementAlternative rule, Class<?> valueType,
			List<RuleSpec> target) throws NoSuchMethodException
	{
		PartialExpressionLogical _test = rule.getTest();
		PartialExpression _rightExpression = _test.getRightExpression();
		if (_rightExpression instanceof PartialExpressionLiteralString)
		{
			PartialExpressionLiteralString _nameToMatch = (PartialExpressionLiteralString) _rightExpression;
			PropertyNameMatcher _matcher = new PropertyNameMatcherExactMatch(_nameToMatch.getValue());
			List<ProcessorSpec> _processors = executeProgram__parseDirectives__processRuleset__listProcessors(rule.getStatements(), valueType);
			target.add(new RuleSpec(_matcher, _processors));
		}
	}

	private void executeProgram__parseDirectives__processRuleset__matchPattern(StatementAlternative rule, Class<?> valueType,
			List<RuleSpec> target) throws NoSuchMethodException
	{
		PartialExpressionLogical _test = rule.getTest();
		PartialExpression _rightExpression = _test.getRightExpression();
		if (_rightExpression instanceof PartialExpressionLiteralString)
		{
			PartialExpressionLiteralString _nameToMatch = (PartialExpressionLiteralString) _rightExpression;
			PropertyNameMatcher _matcher = new PropertyNameMatcherLike(_nameToMatch.getValue());
			List<ProcessorSpec> _processors = executeProgram__parseDirectives__processRuleset__listProcessors(rule.getStatements(), valueType);
			target.add(new RuleSpec(_matcher, _processors));
		}
	}

	private VessNode executeProgram__parseSource(String source, AnalyzerSyntaxic parser) throws Exception
	{
		Reader statementReader = new StringReader(source);
		((AnalyzerLexical) parser.getScanner()).yyreset(statementReader);
		final ComplexSymbol _symbol = (ComplexSymbol) parser.debug_parse();
		Object _value = _symbol.value;
		if (_value instanceof VessNode)
		{
			VessNode _node = (VessNode) _value;
			return _node;
		}
		throw new IllegalStateException("No node found for '" + source + "'");
	}

	private Map<String, Object> getContext()
	{
		return myContext;
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
