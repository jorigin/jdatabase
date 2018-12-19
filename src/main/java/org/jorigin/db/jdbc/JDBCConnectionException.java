/*
  This file is part of JOrigin Common Library.

    JOrigin Common is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JOrigin Common is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JOrigin Common.  If not, see <http://www.gnu.org/licenses/>.
    
*/
package org.jorigin.db.jdbc;

/**
 * 
 */


/**
 * @author jus
 *
 */
public class JDBCConnectionException extends Exception{


  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final int NATIVE_SERVER_EXCEPTION = 0;
  	
  /**
   * The flag of no suitable driver exception.
   */  
  public static final int NO_SUITABLE_DRIVER     = 1;
  
  public static final int BAD_SERVER             = 2;
  
  public static final int BAD_SERVER_PORT        = 3;
  
  public static final int DATABASE_ACCESS_DENIED = 4;
  
  public static final int TOO_MANY_CONNECTIONS   = 5;
  
  public static final int NO_DATABASE_SELECTED   = 6;
  
  public static final int BAD_DATABASE_NAME      = 7;
  
  public static final int UNKNOWN_DATABASE       = 8;
  
  public static final int UNKNOWN_DATABASE_TYPE  = 9;
  
  public static final int UNEXPECTED_EOF         = 10;
  
  public static final int BAD_SQL_QUERY          = 11;
  
  
  public static final int WARNING_UNDEFINED      = 100;
  
  public static final int WARNING_NO_RESULT      = 101;
  
  
  private int id = 0;
  

  /**
   * Create a new JDBCDatabaseAccess excpetion
   * @param message the message of the exception
   * @param id the numeric identifier of the exception
   */
  public JDBCConnectionException(String message, int id){
    this(message, id, null);
  }
  
  /**
   * Create a new JDBCDatabaseAccess excpetion
   * @param message the message of the exception
   * @param id the numeric identifier of the exception
   * @param t the source of the exception.
   */
  public JDBCConnectionException(String message, int id, Throwable t){
    super(message, t);
    this.id = id;
  }
  
  
  /**
   * Return the identifier of the exception
   * @return the identifier of the exception
   */
  public int getId(){
    return this.id;
  }
  
  /**
   * Return if the exception is raised by a warning. A warning
   * is not an error and in many cases it can be ignored.
   * @return true if the exception is a warning, false otherwise.
   */
  public boolean isWarning(){
	if ((this.id >= 100) && (this.id <= 199)){
      return true;
	} else {
	  return false;
	}
  }
}
