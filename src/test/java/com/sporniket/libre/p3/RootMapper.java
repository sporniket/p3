package com.sporniket.libre.p3;

import com.sporniket.libre.p3.builtins.WrappedObjectMapperProcessor;

public class RootMapper extends WrappedObjectMapperProcessor
{
	private final Root myRoot = new Root().withChild(new Node());

	public Root getRoot()
	{
		return myRoot;
	}

	@Override
	protected Object getObject()
	{
		return getRoot();
	}

}