package org.jorigin.db.jdbc.pgsql;

import org.jorigin.db.jdbc.ColumnHeader;

public class PGSQLColumnHeader extends ColumnHeader {

  public PGSQLColumnHeader(String name, int type) {
    super(name, type);
  }

  public PGSQLColumnHeader(String name, int type, String typeName, int columnSize, 
      int decimalDigits, int nullable, String remarks, String defaultValue,
      int charOctetLength, int ordinalPosition, String isNullable,
      String autoIncrement, String columnKey){
    super(name, type, typeName, columnSize,  decimalDigits, nullable, remarks, defaultValue,
        charOctetLength, ordinalPosition, isNullable, autoIncrement, columnKey);
  }
}
