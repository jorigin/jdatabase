package org.jdatabase.jdbc.pgsql;

import org.jdatabase.JDatabase;
import org.jdatabase.jdbc.ColumnHeader;

/**
 * 
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 *
 */
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
