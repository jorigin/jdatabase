package org.jorigin.db.jdbc.odbc;

import org.jorigin.db.jdbc.sql.Catalog;
import org.jorigin.db.jdbc.sql.Database;
import org.jorigin.db.jdbc.sql.Schema;
import org.jorigin.db.jdbc.sql.Server;
import org.jorigin.db.jdbc.sql.Table;

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
