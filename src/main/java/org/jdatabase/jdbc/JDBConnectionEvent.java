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
package org.jdatabase.jdbc;


import java.awt.AWTEvent;

import org.jdatabase.JDatabase;
import org.jdatabase.jdbc.sql.Database;

/**
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 *
 */
public class JDBConnectionEvent extends AWTEvent {

  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public static final int DATABASE_CONNECTION_START     = AWTEvent.RESERVED_ID_MAX + 1;
  public static final int DATABASE_CONNECTION_SUCCESS   = AWTEvent.RESERVED_ID_MAX + 2;
  public static final int DATABASE_CONNECTION_FAIL      = AWTEvent.RESERVED_ID_MAX + 3;
  
  public static final int DATABASE_REQUESTING_START     = AWTEvent.RESERVED_ID_MAX + 4;
  public static final int DATABASE_REQUEST_SUCCESS      = AWTEvent.RESERVED_ID_MAX + 5;
  public static final int DATABASE_REQUEST_FAIL         = AWTEvent.RESERVED_ID_MAX + 6;
  
  
  public static final int DATABASE_RESULT_PROCESS_START   = AWTEvent.RESERVED_ID_MAX + 7;
  public static final int DATABASE_RESULT_PROCESS_SUCCESS = AWTEvent.RESERVED_ID_MAX + 8;
  public static final int DATABASE_RESULT_PROCESS_FAIL    = AWTEvent.RESERVED_ID_MAX + 9;
  
  private int size = -1;
  
 
  public JDBConnectionEvent(Object source){
    this(source, -1);
  }
  
  
  public JDBConnectionEvent(Object source, int id){
    this(source, id, -1);
  }
  
  public JDBConnectionEvent(Object source, int id, int size){
    super(source, id);
    this.size = size;
  }
  
  public void setSize(int size){
    this.size = size;
  }
  
  public int getSize(){
    return this.size;
  }
}
