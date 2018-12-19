package org.jorigin.db.jdbc.pgsql;

import java.util.ArrayList;

import org.jorigin.db.jdbc.sql.Catalog;
import org.jorigin.db.jdbc.sql.Database;
import org.jorigin.db.jdbc.sql.Schema;
import org.jorigin.db.jdbc.sql.Server;
import org.jorigin.db.jdbc.sql.Table;

public class PGSQLSchema implements Schema{

  private String name             = null;
  
  private Catalog catalog         = null;
  
  private ArrayList<Table> tables = null;
  
  /**
   * Create a new schema with the given <code>name</code> within 
   * the given {@link org.jorigin.db.jdbc.sql.Catalog catalog} 
   * and the given {@link org.jorigin.db.jdbc.sql.Database database}.
   * @param name the name of the schema.
   * @param catalog the catalog containing the schema.
   * @param database the database containing the schema.
   */
  public PGSQLSchema(String name, Catalog catalog, Database database){
    this.name    = name;
    this.catalog = catalog;
    tables       = new ArrayList<Table>();
  }
  
  /**
   * Create a new schema with the given <code>name</code> within the given {@link org.jorigin.db.jdbc.sql.Database database}.
   * The {@link org.jorigin.db.jdbc.sql.Catalog catalog} attached to this database is set to <code>null</code>.
   * @param name the name of the schema.
   * @param database the database containing the schema.
   */
  public PGSQLSchema(String name, Database database){
    this.name    = name;
    this.catalog = null;
    tables       = new ArrayList<Table>();
  }
  
  @Override
  public boolean addTable(Table table) {
    if (!tables.contains(table)){
      return tables.add(table);
    } else {
      return false;
    }
  }

  @Override
  public boolean removeTable(Table table) {
    return tables.remove(table);
  }
  
  @Override
  public Catalog getCatalog() {
    return catalog;
  }

  @Override
  public Database getDatabase() {
    if (catalog != null){
      return catalog.getDatabase();
    } else {
      return null;
    }
  }

  @Override
  public Server getServer() {
    if (catalog != null){
      return catalog.getServer();
    } else {
      return null;
    }
  }
  
  @Override
  public String getName() {
    return name;
  }

  @Override
  public ArrayList<Table> getTables() {
    return tables;
  }
}
