package org.jdatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The JDatabase package common class. 
 * This class enable to set general parameters and to get general information about this JDatabase implementation.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value #BUILD}
 * @since 1.0.0
 */
public class JDatabase {

	  /**
	   * The build version.
	   */
	  public static final long BUILD     = 20181219L;
	  
	  /**
	   * The version number
	   */
	  public static final String version = "1.0.0";
	  
	  /**
	   * The {@link java.util.logging.Logger logger} used for reporting.
	   */
	  public static Logger logger = null;
	  
	  static {
	    init();
	  }
  
	  /**
	   * Initialize the JDatabase common package.
	   */
	  public static final void init(){

	    logger = Logger.getLogger("org.jorigin.jdatabase");
	    
	    String property = System.getProperty("java.util.logging.level");
	    Level level = Level.INFO;
	    if (property != null){
	      if (property.equalsIgnoreCase("OFF")){
	        level = Level.OFF;
	      } else if (property.equalsIgnoreCase("SEVERE")){
	        level = Level.SEVERE;
	      } else if (property.equalsIgnoreCase("WARNING")){
	        level = Level.WARNING;
	      } else if (property.equalsIgnoreCase("INFO")){
	        level = Level.INFO;
	      } else if (property.equalsIgnoreCase("CONFIG")){
	        level = Level.CONFIG;
	      } else if (property.equalsIgnoreCase("FINE")){
	        level = Level.FINE;
	      } else if (property.equalsIgnoreCase("FINER")){
	        level = Level.FINER;
	      } else if (property.equalsIgnoreCase("FINEST")){
	        level = Level.FINEST;
	      } else if (property.equalsIgnoreCase("ALL")){
	        level = Level.ALL;
	      }
	    }

	    logger.setLevel(level);
	  }  
	  
	  /**
	   * Set the {@link Logger logger} to use for reporting.
	   * @param logger the {@link Logger logger} to use for reporting.
	   */
	  public static void setLogger(Logger logger){
		  JDatabase.logger = logger;
	  }
}
