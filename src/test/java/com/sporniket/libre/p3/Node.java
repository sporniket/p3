package com.sporniket.libre.p3;

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
 * @version 2
 * @since 2
 * @see TestWrappedObjectMapperProcessor
 */
public class Node
{
	private double myDoubleValue ;
	
	private long myLongValue ;
	
	private String[] myValue;

	public double getDoubleValue()
	{
		return myDoubleValue;
	}

	public long getLongValue()
	{
		return myLongValue;
	}

	public String[] getValue()
	{
		return myValue;
	}

	public void setDoubleValue(double doubleValue)
	{
		myDoubleValue = doubleValue;
	}

	public void setLongValue(long longValue)
	{
		myLongValue = longValue;
	}

	@SuppressWarnings("unused")
	public void setValue(String[] value)
	{
		this.myValue = value;
	}
}