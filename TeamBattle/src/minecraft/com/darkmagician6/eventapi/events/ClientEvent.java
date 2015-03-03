package com.darkmagician6.eventapi.events;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.FlexibleArray;
import com.darkmagician6.eventapi.MethodData;


public class ClientEvent implements Event
{
	private boolean cancelled;

	public boolean isCancelled()
	{
		return cancelled;
	}

	public void setCancelled()
	{
		cancelled = true;
	}

	public void call()
	{
		cancelled = false;
		final FlexibleArray<MethodData> dataList = EventManager.get(this.getClass());
		if (dataList != null && !isCancelled())
		{
			for (final MethodData data : dataList)
			{
				try
				{
					data.target.invoke(data.source, this);
				} catch (final IllegalAccessException e)
				{
					System.out.println("Can't invoke '" + data.target.getName() + "' because it's not accessible.");
				} catch (final IllegalArgumentException e)
				{
					System.out.println("Can't invoke '" + data.target.getName() + "' because the parameter/s don't match.");
				} catch (final InvocationTargetException e)
				{
				}
			}
		}
	}
}
