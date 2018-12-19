package org.jdatabase.jdbc.odbc;

import java.util.ArrayList;

import org.jdatabase.JDatabase;
import org.jdatabase.jdbc.sql.Catalog;
import org.jdatabase.jdbc.sql.Database;
import org.jdatabase.jdbc.sql.Schema;
import org.jdatabase.jdbc.sql.Server;
import org.jdatabase.jdbc.sql.Table;

/**
 * 
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 *
 */
public class ODBCDatabase implements Database, Catalog, Schema{

  /**
   * The server hosting the database
   */
  private Server server           = null;
  
  /**
   * The name of the database
   */
  private String name             = null;
  
  /**
   * The tables contained in the database
   */
  private ArrayList<Table> tables = null;
  
  /**
   * Create a new database stored on a specified server and containing given tables
   * @param name the name of the database
   * @param server the name of the server
   * @param tables the tables contained in the database
   */
  public ODBCDatabase(String name, Server server, ArrayList<Table> tables){
    this.name   = name;
    this.server = server;
    this.tables = tables;
  }
  
  
  /**
   * Create a new database stored on a specified server.
   * @param name the name of the database
   * @param server the name of the server
   */
  public ODBCDatabase(String name, Server server){
    this.name   = name;
    this.server = server;
    this.tables = new ArrayList<Table>();
  }
  
  // -- Database ----------------------------------------------------
  @Override
  public String getName(){
    return this.name;
  }
  
  @Override
  public Server getServer(){
    return this.server;
  }
  
  @Override
  public ArrayList<Catalog> getCatalogs() {
    ArrayList<Catalog> result = new ArrayList<Catalog>();
    result.add(this);
    return result;
  }
  
  @Override
  public boolean addCatalog(Catalog catalog) {
    return false;
  }

  @Override
  public boolean removeCatalog(Catalog catalog) {
    return false;
  }
  // ----------------------------------------------------------------
  
  // -- Catalog -----------------------------------------------------
  @Override
  public Database getDatabase() {
    return this;
  }
  
  @Override
  public ArrayList<Schema> getSchemas() {
    ArrayList<Schema> result = new ArrayList<Schema>();
    result.add(this);
    return result;
  }

  @Override
  public boolean addSchema(Schema schema) {
    return false;
  }
  
  @Override
  public boolean removeSchema(Schema schema) {
    return false;
  }
  // ----------------------------------------------------------------
  
  // -- Schema ------------------------------------------------------
  @Override
  public Catalog getCatalog() {
    return this;
  }
  
  @Override
  public ArrayList<Table> getTables(){
    return this.tables;
  }
  
  @Override
  public boolean addTable(Table table){
    if (table != null){
      if (tables != null){
        if (!tables.contains(table)){
          return tables.add(table);
        } else {
          return false;
        }
      } else {
        tables = new ArrayList<Table>();
        tables.add(table);
        return true;
      }
    } else {
      return false;
    }
  }
  
  @Override
  public boolean removeTable(Table table){
    if (tables != null){
      return tables.remove(table);
    }else{
      return false;
    }
  }
  // ----------------------------------------------------------------
  
}
