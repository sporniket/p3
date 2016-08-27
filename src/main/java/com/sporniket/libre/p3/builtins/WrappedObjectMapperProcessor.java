/**
 * 
 */
package com.sporniket.libre.p3.builtins;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base class that translate the property name as a path to a nested field of an internal object using the javabeans convention,
 * that field will be filled with the property value.
 * 
 * <p>
 * For a property name <code>a.b</code>, the introspection is used to obtain <code>getObject().getA().setB(value)</code>.
 * 
 * <p>
 * Subclasses are expected to implement the #getObject() by returning the internal object to fill with property value.
 * 
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
 * @version 1
 * @since 2
 */
public abstract class WrappedObjectMapperProcessor
{
	private static final String PREFIX__GETTER = "get";

	private static final String PREFIX__SETTER = "set";

	private static final String REGEXP__CHAR_DOT = "\\.";

	protected abstract Object getObject();

	public void process(String name, String value) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		processGeneric(name, value);
	}

	public void process(String name, String value[]) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		processGeneric(name, value);
	}

	/**
	 * The generic processing, that get any field until the last one that is set with the given value.
	 * 
	 * @param name
	 *            the property name, a dot separated list of fields.
	 * @param value
	 * @throws NoSuchMethodException
	 *             when a getter or setter does not exists.
	 * @throws SecurityException
	 *             when there is a problem.
	 * @throws IllegalAccessException
	 *             when there is a problem.
	 * @throws IllegalArgumentException
	 *             when there is a problem.
	 * @throws InvocationTargetException
	 *             when there is a problem.
	 */
	private void processGeneric(String name, Object value) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		String[] _accessStack = name.split(REGEXP__CHAR_DOT);
		if (0 == _accessStack.length)
		{
			throw new IllegalArgumentException(name);
		}
		Object _toChange = getObject();
		int _setterIndex = _accessStack.length - 1;
		for (int _index = 0; _index < _setterIndex; _index++)
		{
			String _getterName = computeGetterName(_accessStack[_index]);
			Method _getter = _toChange.getClass().getMethod(_getterName, (Class<?>[]) null);
			_toChange = _getter.invoke(_toChange, (Object[]) null);
		}
		String _setterName = computeSetterName(_accessStack[_setterIndex]);
		Method _setter = _toChange.getClass().getMethod(_setterName, value.getClass());
		_setter.invoke(_toChange, value);
	}

	/**
	 * Compute the getter name.
	 * 
	 * @param name
	 *            the name of the property to read.
	 * @return the getter name.
	 */
	private String computeGetterName(String name)
	{
		StringBuilder _result = new StringBuilder().append(PREFIX__GETTER);
		_result.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1));
		return _result.toString();
	}

	/**
	 * Compute the setter name.
	 * 
	 * @param name
	 *            the name of the property to read.
	 * @return the setter name.
	 */
	private String computeSetterName(String name)
	{
		StringBuilder _result = new StringBuilder().append(PREFIX__SETTER);
		_result.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1));
		return _result.toString();
	}
}
