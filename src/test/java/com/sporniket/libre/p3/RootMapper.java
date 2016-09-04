package com.sporniket.libre.p3;

import java.net.MalformedURLException;
import java.net.URL;

import com.sporniket.libre.p3.builtins.WrappedObjectMapperProcessor;

/**
 * Test class.
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
 * @version 4
 * @since 2
 * @see TestWrappedObjectMapperProcessor
 */
public class RootMapper extends WrappedObjectMapperProcessor
{
	private final Root myRoot = new Root().withChild(new Node());

	public Root getRoot()
	{
		return myRoot;
	}

	public void processSpecial(String name, String value) throws MalformedURLException
	{
		getRoot().setSpecialProcess(new URL(value));
	}

	public void processSpecial(String name, String[] value) throws MalformedURLException
	{
		URL _value = null ;
		if (value.length > 0)
		{
			_value = new URL(value[0]);
		}
		getRoot().setSpecialProcess(_value);
	}
	
	@Override
	protected Object getObject()
	{
		return getRoot();
	}

}