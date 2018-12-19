package org.jorigin.db.jdbc.sql;

import java.util.ArrayList;


/**
 * This class represents a SQL DDL schema. Formaly, a schema is a part of a 
 * {@link org.jorigin.db.jdbc.sql.Catalog catalog} and a container of 
 * {@link org.jorigin.db.jdbc.sql.Table tables}.
 * @author Julien Seinturier - (c) 2009 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.1.0
 */
public interface Schema {
  
  /**
   * Get the name of the schema.
   * @return the name of the schema
   */
  public String getName();
  
  /**
   * Get the {@link org.jorigin.db.jdbc.sql.Table tables} contained by the schema.
   * @return the tables contained by the schema.
   */
  public ArrayList<Table> getTables();
  
  /**
   * Get the {@link org.jorigin.db.jdbc.sql.Catalog catalog} containing this schema.
   * @return the catalog containing this schema.
   */
  public Catalog getCatalog();
  
  /**
   * Get the {@link org.jorigin.db.jdbc.sql.Database database} containing the schema. 
   * This method should return the result of <code>getCatalog().getDatabase()</code> or <code>null</code> 
   * if no information is available.
   * @return the database containing the schema.
   */
  public Database getDatabase();
  
  /**
   * Get the {@link org.jorigin.db.jdbc.sql.Server server} containing the schema.
   * This method should return the result of <code>getDatabase().getServer()</code> or <code>null</code> 
   * if no information is available.
   * @return the server containing the schema.
   */
  public Server getServer();
  
  
  /**
   * Add a {@link org.jorigin.db.jdbc.sql.Table table} to the schema. The table is added only if it is not already present
   * in the schema.
   * @param table the table to add
   * @return true if the table is added, false otherwise.
   */
  public boolean addTable(Table table);
  
  
  /**
   * Remove the {@link org.jorigin.db.jdbc.sql.Table table} given in parameter from the schema.
   * @param table the table to remove.
   * @return true if the table is removed, false otherwise.
   */
  public boolean removeTable(Table table);
}
