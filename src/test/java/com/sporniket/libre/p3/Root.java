package com.sporniket.libre.p3;

import java.net.URL;

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
public class Root
{
	private boolean myBooleanValue ;

	private Node myChild;
	
	private float myFloatValue ;
	
	private int myIntValue ;
	
	private URL mySpecialProcess ;
	
	private String myValue;

	public Node getChild()
	{
		return myChild;
	}

	public float getFloatValue()
	{
		return myFloatValue;
	}

	public int getIntValue()
	{
		return myIntValue;
	}

	public URL getSpecialProcess()
	{
		return mySpecialProcess;
	}

	public String getValue()
	{
		return myValue;
	}

	public boolean isBooleanValue()
	{
		return myBooleanValue;
	}

	public void setBooleanValue(boolean booleanValue)
	{
		this.myBooleanValue = booleanValue;
	}

	public void setChild(Node child)
	{
		myChild = child;
	}

	public void setFloatValue(float floatValue)
	{
		this.myFloatValue = floatValue;
	}

	public void setIntValue(int intValue)
	{
		this.myIntValue = intValue;
	}

	public void setSpecialProcess(URL specialProcess)
	{
		mySpecialProcess = specialProcess;
	}

	@SuppressWarnings("unused")
	public void setValue(String value)
	{
		this.myValue = value;
	}

	public Root withChild(Node child)
	{
		setChild(child);
		return this;
	}

}