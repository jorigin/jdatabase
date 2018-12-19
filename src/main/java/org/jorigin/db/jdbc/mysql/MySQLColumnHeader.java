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
package org.jorigin.db.jdbc.mysql;

import org.jorigin.db.jdbc.ColumnHeader;





/**
 * A column header for a MySQL database.
 * @author Julien Seinturier
 *
 */
public class MySQLColumnHeader extends ColumnHeader {

  
  private int numericPrecision = 0;
  private int numericScale     = 0;
  
  private String extra         = null;
  private String privileges    = null;
  
  /**
   * Create a new MySQK column header
   * @param name
   * @param type
   * @param typeName
   * @param columnSize
   * @param decimalDigits
   * @param nullable
   * @param remarks
   * @param defaultValue
   * @param charOctetLength
   * @param ordinalPosition
   * @param isNullable
   * @param autoIncrement
   */
  public MySQLColumnHeader(String name, int type, String typeName, int columnSize, 
      int decimalDigits, int nullable, String remarks, String defaultValue,
      int charOctetLength, int ordinalPosition, String isNullable,
      String autoIncrement, int numericPrecision,
      int numericScale, String columnKey, String extra, String privileges){
    super(name, type, typeName, columnSize, 
        decimalDigits, nullable, remarks, defaultValue,
        charOctetLength, ordinalPosition, isNullable,
        autoIncrement, columnKey);
    
    this.numericPrecision = numericPrecision;
    this.numericScale     = numericScale;
    this.extra            = extra;
    this.privileges       = privileges;
  }
  
  
  /**
   * Get the numeric precision of the column
   * @return  the numeric precision of the column
   */
  public int getNumericPrecision(){
    return numericPrecision;
  }
  
  /**
   * Get the numeric scale of the column
   * @return the numeric scale of the column
   */
  public int getNumericScale(){
    return numericScale;
  }
  
  
  /**
   * Get the extra of the column
   * @return the extra of the column
   */
  public String getExtra(){
    return extra;
  }
  
  /**
   * Get the privileges of the column
   * @return the privileges of the column
   */
  public String getPrivileges(){
    return privileges;  
  }
  
  
}
