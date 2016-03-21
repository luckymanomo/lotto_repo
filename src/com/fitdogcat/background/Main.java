package com.fitdogcat.background;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {
	final static Logger logger = LogManager.getLogger(Main.class);
	public static void main(String[] args){
		logger.debug("Here is some DEBUG");                

		logger.info("Here is some INFO");               

		logger.warn("Here is some WARN");                

		logger.error("Here is some ERROR");               

		logger.fatal("Here is some FATAL");      
	}
}
