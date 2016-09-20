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
 * @version 5
 * @since 2
 */
public abstract class WrappedObjectMapperProcessor
{
	private static class SetterTarget
	{
		private final Object myObjectToChange;

		private final String mySetterName;

		public SetterTarget(Object objectToChange, String setterName)
		{
			myObjectToChange = objectToChange;
			mySetterName = setterName;
		}

		public Object getObjectToChange()
		{
			return myObjectToChange;
		}

		public String getSetterName()
		{
			return mySetterName;
		}

	}

	private static final String PREFIX__GETTER = "get";

	private static final String PREFIX__SETTER = "set";

	private static final String REGEXP__CHAR_DOT = "\\.";

	public void process(String name, String value) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		SetterTarget _target = findSetterTarget(name);
		findAndInvokeCompatibleSetter(_target, value);
	}

	public void process(String name, String value[]) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		SetterTarget _target = findSetterTarget(name);
		Object _objectToChange = _target.getObjectToChange();
		Method _setter = _objectToChange.getClass().getMethod(_target.getSetterName(), value.getClass());
		_setter.invoke(_objectToChange, (Object) value);
	}

	protected abstract Object getObject();

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

	/**
	 * Find the first compatible setter (conversion from String is supported) and invoke it.
	 * 
	 * @param target
	 *            the object to change and setter name.
	 * @param value
	 *            the value.
	 * @throws IllegalAccessException
	 *             when there is a problem.
	 * @throws InvocationTargetException
	 *             when there is a problem.
	 * @throws NoSuchMethodError
	 *             when there is no compatible setter.
	 */
	private void findAndInvokeCompatibleSetter(SetterTarget target, String value) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodError
	{
		Object _objectToChange = target.getObjectToChange();
		Method[] _declaredMethods = _objectToChange.getClass().getDeclaredMethods();
		String _setterName = target.getSetterName();
		for (Method _candidate : _declaredMethods)
		{
			if (_candidate.getName().equals(_setterName))
			{
				Class<?>[] _parameterTypes = _candidate.getParameterTypes();
				if (_parameterTypes.length != 1)
				{
					continue;
				}
				Class<?> _class = _parameterTypes[0];
				if (_class == String.class)
				{
					// no conversion
					_candidate.invoke(_objectToChange, value);
					return;
				}
				else if (_class == Integer.TYPE)
				{
					_candidate.invoke(_objectToChange, Integer.parseInt(value));
					return;
				}
				else if (_class == Long.TYPE)
				{
					_candidate.invoke(_objectToChange, Long.parseLong(value));
					return;

				}
				else if (_class == Float.TYPE)
				{
					_candidate.invoke(_objectToChange, Float.parseFloat(value));
					return;

				}
				else if (_class == Double.TYPE)
				{
					_candidate.invoke(_objectToChange, Double.parseDouble(value));
					return;

				}
				else if (_class == Boolean.TYPE)
				{
					_candidate.invoke(_objectToChange, Boolean.parseBoolean(value));
					return;
				}
			}
		}
		throw new NoSuchMethodError(_setterName);
	}

	/**
	 * Find the object to change and the setter method name.
	 * 
	 * @param path
	 *            the path to the object and setter.
	 * @return a {@link SetterTarget} storing the object to change and the setter name.
	 * @throws NoSuchMethodException
	 *             when a getter does not exists.
	 * @throws IllegalAccessException
	 *             when there is a problem.
	 * @throws InvocationTargetException
	 *             when there is a problem.
	 */
	private SetterTarget findSetterTarget(String path) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException
	{
		String[] _accessStack = path.split(REGEXP__CHAR_DOT);
		if (0 == _accessStack.length)
		{
			throw new IllegalArgumentException(path);
		}
		int _setterIndex = _accessStack.length - 1;
		Object _toChange = getObject();
		for (int _index = 0; _index < _setterIndex; _index++)
		{
			String _getterName = computeGetterName(_accessStack[_index]);
			Method _getter = _toChange.getClass().getMethod(_getterName, (Class<?>[]) null);
			_toChange = _getter.invoke(_toChange, (Object[]) null);
		}
		String _setterName = computeSetterName(_accessStack[_setterIndex]);
		SetterTarget _target = new SetterTarget(_toChange, _setterName);
		return _target;
	}
}
