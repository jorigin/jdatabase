package org.jdatabase.jdbc.pgsql;

import java.util.ArrayList;

import org.jdatabase.JDatabase;
import org.jdatabase.jdbc.sql.Catalog;
import org.jdatabase.jdbc.sql.Database;
import org.jdatabase.jdbc.sql.Schema;
import org.jdatabase.jdbc.sql.Server;

/**
 * 
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 *
 */
public class PGSQLDatabase implements Database{

  private String name         = null;
  
  ArrayList<Catalog> catalogs = null;
  
  ArrayList<Schema> schemas   = null;
  
  private Server server       = null;
  
  /**
   * Create a new PostgreSQL {@link org.jdatabase.jdbc.sql.Database database} hosted by the given
   * {@link org.jdatabase.jdbc.sql.Server server}.
   * @param name the name of the database.
   * @param server the server that hosts the database.
   */
  public PGSQLDatabase(String name, Server server){
    this.name   = name;
    this.server = server;
    catalogs    = new ArrayList<Catalog>();
    schemas     = new ArrayList<Schema>();
  }
  
  @Override
  public ArrayList<Catalog> getCatalogs() {
    return catalogs;
  }

  @Override
  public boolean addCatalog(Catalog catalog) {
    if (!catalogs.contains(catalog)){
       return catalogs.add(catalog); 
    } else {
      return false;
    }
  }
  
  @Override
  public boolean removeCatalog(Catalog catalog) {
    return catalogs.remove(catalog);
  }
  
  @Override
  public String getName() {
    return name;
  }

  @Override
  public Server getServer() {
    return server;
  }
  
  @Override
  public boolean equals(Object o){
    if (o instanceof Database){
      if (getName().equals(((Catalog)o).getName())){
        if ((getServer() != null)&&(((Database)o).getServer() != null)){
          if (getServer().getName() != null){
            return getServer().getName().equals(((Database)o).getServer().getName());
          }
        } else if ((getServer() == null)&&(((Database)o).getServer() == null)){
          return true;
        }
      } 
    } 
    
    return false;
  }

  @Override
  public boolean addSchema(Schema schema) {
    if (!schemas.contains(schema)){
      return schemas.add(schema);  
    } else {
      return false;
    }
  }

  @Override
  public ArrayList<Schema> getSchemas() {
    return schemas;
  }

  @Override
  public boolean removeSchema(Schema schema) {
    return schemas.remove(schema);
  }
}
