package me.client.utils;

import me.client.Client;

public class Logger {

	public void log(String message, LogType logType) {
		switch(logType) {
		case NORMAL: 
			System.out.println("[" + Client.instance.Client_Name + "] " + message);
			break;
		case WARNING:
			System.out.println("[" + Client.instance.Client_Name + " - WARNING] " + message);
			break;
		case ERROR:
			System.out.println("[" + Client.instance.Client_Name + " - ERROR] " + message);
		}
	}
	
	public static enum LogType {
		NORMAL, WARNING, ERROR
	}
}
