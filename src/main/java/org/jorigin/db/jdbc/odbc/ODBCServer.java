package org.jorigin.db.jdbc.odbc;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;

import org.jorigin.db.jdbc.sql.Database;
import org.jorigin.db.jdbc.sql.Server;

public class ODBCServer implements Server{
  String name                   = null;
  ArrayList<Database> databases = null;
  DatabaseMetaData metaData     = null;
  
  /**
   * Create a new  server containing a list of databases and metadatas
   * @param name the name of the server
   * @param databases the list of databases hoster by the server
   * @param metadata the metadata of the server.
   */
  public ODBCServer(String name, ArrayList<Database> databases, DatabaseMetaData metaData){
    this.name      = name;
    this.databases = databases;
    this.metaData  = metaData; 
  }
  
  /**
   * Create a new server containing a list of databases
   * @param name the name of the server
   * @param databases the list of databases hoster by the server
   */
  public ODBCServer(String name, ArrayList<Database> databases){
    this(name, databases, null);
  }
  
  /**
   * Create a new server with cigen name and metadata
   * @param name the name of the server
   * @param metaData the metadata associated to the server.
   */
  public ODBCServer(String name, DatabaseMetaData metaData){
    this(name, new ArrayList<Database>(), metaData);
  }
  
  /**
   * Create a new server containing no databases
   * @param name the name of the server.
   */
  public ODBCServer(String name){
    this(name, new ArrayList<Database>(), null);
  }
  
  /**
   * Get the name of the server.
   * @return the name of the server
   */
  public String getName(){
    return this.name;
  }
  
  /**
   * Set the databases hosted by the server
   * @param databases the databases hosted by the server
   */
  public void setDatabases(ArrayList<Database> databases){
    this.databases = databases;
  }
  
  /**
   * Get the databases hosted by the server.
   * @return the databases hosted by the server.
   */
  public ArrayList<Database> getDatabases(){
    return this.databases;
  }
  
  /**
   * Get the metadata associated with the server
   * @return the metadata of the server.
   */
  public DatabaseMetaData getMetadata(){
    return this.metaData;
  }
  
  /**
   * Add a new database to the server if the database if not in the list already.
   * @param database the database to add to the server list
   * @return true if the database is added, false otherwise
   */
  public boolean addDatabase(Database database){
    if ((!databases.contains(database)) && (database != null)){
      return databases.add(database);
    } else {
      return false;
    }
  }
 
  /**
   * Remove a database from the database list.
   * @param database the database to remove
   * @return true if the database is removed, false
   * otherwise
   */
  public boolean removeDatabase(Database database){
    return databases.remove(database);
  }
}
