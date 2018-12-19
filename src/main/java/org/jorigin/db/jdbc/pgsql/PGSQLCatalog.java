package org.jorigin.db.jdbc.pgsql;

import java.util.ArrayList;

import org.jorigin.db.jdbc.sql.Catalog;
import org.jorigin.db.jdbc.sql.Database;
import org.jorigin.db.jdbc.sql.Schema;
import org.jorigin.db.jdbc.sql.Server;

public class PGSQLCatalog implements Catalog{

  private String name               = null;
  
  private ArrayList<Schema> schemas = null;
  
  private Database database         = null;

  /**
   * Construct a new catalog with the given <code>name</code> in the given 
   * {@link org.jorigin.db.jdbc.sql.Database database}
   * @param name the name of the catalog.
   * @param database the database containing the catalog.
   */
  public PGSQLCatalog(String name, Database database){
    this.name     = name;
    this.database = database;
    schemas       = new ArrayList<Schema>();
  }
  
  @Override
  public String getName() {
    return name;
  }

  @Override
  public ArrayList<Schema> getSchemas() {
    return schemas;
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
  public boolean removeSchema(Schema schema) {
    return schemas.remove(schema);
  }

  @Override
  public Database getDatabase() {
    return database;
  }
  
  @Override
  public Server getServer() {
    if (database != null){
      return database.getServer();
    } else {
      return null;
    }
  }

  @Override
  public boolean equals(Object o){
    if (o instanceof Catalog){
      if (getName().equals(((Catalog)o).getName())){
        if ((getDatabase() != null)&&(((Catalog)o).getDatabase() != null)){
          if (getDatabase().getName() != null){
            return getDatabase().getName().equals(((Catalog)o).getDatabase().getName());
          }
        } else if ((getDatabase() == null)&&(((Catalog)o).getDatabase() == null)){
          return true;
        }
      } 
    } 
    
    return false;
  }
}
