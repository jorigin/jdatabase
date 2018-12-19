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

import org.jdatabase.jdbc.sql.Database;

/**
 * 
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 *
 */
public class ColumnHeader {
  
  /**
   * This  string represent a primary key
   */
  public static final String KEY_PRIMARY = "PRI";
  
  /**
   * This string represent a unique key
   */
  public static final String KEY_UNIQUE  = "UNI";
  
  /**
   * This string represent a multiple key
   */
  public static final String KEY_MUL     = "MUL";
  
  /**
   * The column name
   */
  private String name       = null;
  
  /**
   * The column type
   */
  private int type          = -1;
  
  /**
   * The column type name
   */
  private String typeName   = null;
  
  /**
   * The  column size.  
   */
  private int columnSize    = -1;
  
  /**
   * The number of fractional digits. Null is returned for data types where  
   * DECIMAL_DIGITS is not applicable.
   */
  private int decimalDigits = 0;
  
  /**
   * <B>NULLABLE</B> specifies if NULL allowed. Value can be:
   *      <UL>
   *      <LI> columnNoNulls - might not allow <code>NULL</code> values
   *      <LI> columnNullable - definitely allows <code>NULL</code> values
   *      <LI> columnNullableUnknown - nullability unknown
   *      </UL>
   */
  private int nullable      = 0;
  
  /**
   * Comment describing column (may be <code>null</code>)
   */
  private String remarks    = null;

  /**
   * Default value for the column, which should be interpreted as a string when the value is 
   * enclosed in single quotes (may be <code>null</code>)
   */
  private String defaultValue = null;
  
  
  /**
   * For char types the maximum number of bytes in the column
   */
  private int charOctetLength = 0;
  
  /**
   * index of column in table 
   *      (starting at 1)
   */
  private int ordinalPosition = 0;
  
  /**
   * <B>IS_NULLABLE</B> String  => ISO rules are used to determine the nullability for a column.
   *       <UL>
   *       <LI> YES           --- if the parameter can include NULLs
   *       <LI> NO            --- if the parameter cannot include NULLs
   *       <LI> empty string  --- if the nullability for the 
   * parameter is unknown
   *       </UL>
   */
  private String isNullable = "";
  
  /**
   * <B>IS_AUTOINCREMENT</B> Indicates whether this column is auto incremented
   *       <UL>
   *       <LI> YES           --- if the column is auto incremented
   *       <LI> NO            --- if the column is not auto incremented
   *       <LI> empty string  --- if it cannot be determined whether the column is auto incremented
   * parameter is unknown
   *       </UL>
   */
  private String autoIncrement = "";
  
  /**
   * The key associated to the column
   */
  private String columnKey     = null;
  
  /**
   * Create a new column header with all metadata given.
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
  public ColumnHeader(String name, int type, String typeName, int columnSize, 
                      int decimalDigits, int nullable, String remarks, String defaultValue,
                      int charOctetLength, int ordinalPosition, String isNullable,
                      String autoIncrement, String columnKey){
    
    this.name            = name;
    
    this.type            = type;
    
    this.typeName        = typeName;
    
    this.columnSize      = columnSize;
    
    this.decimalDigits   = decimalDigits;
     
    this.nullable        = nullable;
    
    this.remarks         = remarks;
    
    this.defaultValue    = defaultValue;
    
    this.charOctetLength = charOctetLength;
    
    this.ordinalPosition = ordinalPosition;
    
    this.isNullable      = isNullable;
    
    this.autoIncrement   = autoIncrement;
    
    this.columnKey       = columnKey;
  }
  
  /**
   * Create a new column header with a name and a type given in parameter
   * @param name
   * @param type
   */
  public ColumnHeader(String name, int type){
    this.name = name;
    this.type = type;
  }
  
  @Override
  public String toString(){
    return name+" ("+typeName+")";
  }
  
  /**
   * 
   * @return
   */
  public String getName(){
    return name;
  }
  
  /**
   * 
   * @return
   */
  public int getType(){
    return type;
  }
  
  /**
   * 
   * @return
   */
  public String getTypeName(){
    return typeName;
  }
  
  /**
   * 
   * @return
   */
  public int getColumnSize(){
    return columnSize;
  }
  
  /**
   * 
   * @return
   */
  public int getDecimalDigits(){
    return decimalDigits;
  }
   
  /**
   * 
   * @return
   */
  public int getNullable(){
    return nullable;
  }
  
  /**
   * 
   * @return
   */
  public String getRemarks(){
    return remarks;
  }
  
  /**
   * 
   * @return
   */
  public String getDefaultValue(){
    return defaultValue;
  }
  
  /**
   * 
   * @return
   */
  public int getCharOctetLength(){
    return charOctetLength;
  }
  
  /**
   * 
   * @return
   */
  public int getOrdinalPosition(){
    return ordinalPosition;
  }
  
  /**
   * 
   * @return
   */
  public String isNullable(){
    return isNullable;
  }
  
  /**
   * 
   * @return
   */
  public String getAutoIncrement(){
    return autoIncrement;
  }
  
  /**
   * 
   * @return
   */
  public String getColumnKey(){
    return columnKey;
  }
}
