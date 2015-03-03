package com.darkmagician6.eventapi;

import java.lang.reflect.Method;

public class MethodData {

    public final Object source;
    public final Method target;
    public final byte priority;

    MethodData(Object source, Method target, byte priority) {
        this.source = source;
        this.target = target;
        this.priority = priority;
    }
    
	/**
	 * Gets the source Object of the data.
	 * 
	 * @return Source Object of the targeted Method.
	 */
	public Object getSource()
	{
		return source;
	}

	/**
	 * Gets the targeted Method.
	 * 
	 * @return The Method that is listening to certain Event calls.
	 */
	public Method getTarget()
	{
		return target;
	}

	/**
	 * Gets the priority value of the targeted Method.
	 * 
	 * @return The priority value of the targeted Method.
	 */
	public byte getPriority()
	{
		return priority;
	}

	
}