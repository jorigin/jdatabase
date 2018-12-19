package org.jdatabase.jdbc.sql;

import java.util.ArrayList;

import org.jdatabase.JDatabase;


/**
 * This class represents a SQL DDL schema. Formaly, a schema is a part of a 
 * {@link org.jdatabase.jdbc.sql.Catalog catalog} and a container of 
 * {@link org.jdatabase.jdbc.sql.Table tables}.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 */
public interface Schema {
  
  /**
   * Get the name of the schema.
   * @return the name of the schema
   */
  public String getName();
  
  /**
   * Get the {@link org.jdatabase.jdbc.sql.Table tables} contained by the schema.
   * @return the tables contained by the schema.
   */
  public ArrayList<Table> getTables();
  
  /**
   * Get the {@link org.jdatabase.jdbc.sql.Catalog catalog} containing this schema.
   * @return the catalog containing this schema.
   */
  public Catalog getCatalog();
  
  /**
   * Get the {@link org.jdatabase.jdbc.sql.Database database} containing the schema. 
   * This method should return the result of <code>getCatalog().getDatabase()</code> or <code>null</code> 
   * if no information is available.
   * @return the database containing the schema.
   */
  public Database getDatabase();
  
  /**
   * Get the {@link org.jdatabase.jdbc.sql.Server server} containing the schema.
   * This method should return the result of <code>getDatabase().getServer()</code> or <code>null</code> 
   * if no information is available.
   * @return the server containing the schema.
   */
  public Server getServer();
  
  
  /**
   * Add a {@link org.jdatabase.jdbc.sql.Table table} to the schema. The table is added only if it is not already present
   * in the schema.
   * @param table the table to add
   * @return true if the table is added, false otherwise.
   */
  public boolean addTable(Table table);
  
  
  /**
   * Remove the {@link org.jdatabase.jdbc.sql.Table table} given in parameter from the schema.
   * @param table the table to remove.
   * @return true if the table is removed, false otherwise.
   */
  public boolean removeTable(Table table);
}
