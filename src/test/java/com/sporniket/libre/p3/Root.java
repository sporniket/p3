package com.sporniket.libre.p3;

public class Root
{
	private Node myChild;

	private String value;

	public Node getChild()
	{
		return myChild;
	}

	public String getValue()
	{
		return value;
	}

	public void setChild(Node child)
	{
		myChild = child;
	}

	@SuppressWarnings("unused")
	public void setValue(String value)
	{
		this.value = value;
	}

	public Root withChild(Node child)
	{
		setChild(child);
		return this;
	}

}