package org.jdatabase.jdbc.odbc;

import org.jdatabase.jdbc.sql.Catalog;
import org.jdatabase.jdbc.sql.Database;
import org.jdatabase.jdbc.sql.Schema;
import org.jdatabase.jdbc.sql.Server;
import org.jdatabase.jdbc.sql.Table;

public class ODBCTable implements Table{

  /**
   * The name of the table
   */
  private String name       = null;
  
  /**
   * The schema storing the table
   */
  private Schema schema = null;
  
  /**
   * Create a new table stored in a schema
   * @param name the name of the table
   * @param schema the schema storing the table.
   */
  public ODBCTable(String name, Schema schema){
    this.name     = name;
    this.schema = schema;
  }
  
  @Override
  public String getName(){
    return this.name;
  }

  @Override
  public Schema getSchema() {
    return schema;
  }

  @Override
  public Catalog getCatalog() {
    if (schema != null){
      return schema.getCatalog();
    } else {
      return null;
    }
  }

  @Override
  public Database getDatabase() {
    if (schema != null){
      return schema.getDatabase();
    } else {
      return null;
    }
  }

  @Override
  public Server getServer() {
    if (schema != null){
      return schema.getServer();
    } else {
      return null;
    }
  }
}
