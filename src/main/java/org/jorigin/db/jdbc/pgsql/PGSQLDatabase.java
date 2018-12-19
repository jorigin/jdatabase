package org.jorigin.db.jdbc.pgsql;

import java.util.ArrayList;

import org.jorigin.db.jdbc.sql.Catalog;
import org.jorigin.db.jdbc.sql.Database;
import org.jorigin.db.jdbc.sql.Schema;
import org.jorigin.db.jdbc.sql.Server;

public class PGSQLDatabase implements Database{

  private String name         = null;
  
  ArrayList<Catalog> catalogs = null;
  
  ArrayList<Schema> schemas   = null;
  
  private Server server       = null;
  
  /**
   * Create a new PostgreSQL {@link org.jorigin.db.jdbc.sql.Database database} hosted by the given
   * {@link org.jorigin.db.jdbc.sql.Server server}.
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
