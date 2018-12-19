/*
  This file is part of JOrigin Common Library.

    JOrigin Common is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JOrigin Common is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JOrigin Common.  If not, see <http://www.gnu.org/licenses/>.
    
*/
package org.jorigin.db.jdbc.sql;

import java.util.ArrayList;


/**
 * This classes describe a database. The database is hosted by a {@link org.jorigin.db.jdbc.sql.Server server} and contains a 
 * collection of {@link org.jorigin.db.jdbc.sql.Catalog catalogs}.
 * @author Julien Seinturier - (c) 2009 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.1.0
 */
public interface Database {

  /**
   * Get the name of the database.
   * @return the name of the database
   */
  public String getName();
  
  /**
   * Get the {@link org.jorigin.db.jdbc.sql.Catalog catalogs} contained within the database.
   * @return the catalogs contained within the database.
   */
  public ArrayList<Catalog> getCatalogs();

  /**
   * Get the {@link org.jorigin.db.jdbc.sql.Schema schemas} contained by the database.
   * @return the schemas contained by the database.
   */
  public ArrayList<Schema> getSchemas();
  
  /**
   * Get the {@link org.jorigin.db.jdbc.sql.Server server} hosting the database.
   * @return the server hosting the satabase.
   */
  public Server getServer();
  
  
  /**
   * Add a {@link org.jorigin.db.jdbc.sql.Catalog catalog} to the database. 
   * The catalog is added only if it is not already present in the database.
   * @param catalog the catalog to add
   * @return true if the catalog is added, false otherwise.
   */
  public boolean addCatalog(Catalog catalog);
  
  
  /**
   * Remove the {@link org.jorigin.db.jdbc.sql.Catalog catalog} given in parameter from the database.
   * @param catalog the catalog to remove.
   * @return true if the catalog is removed, false otherwise.
   */
  public boolean removeCatalog(Catalog catalog);
  
  /**
   * Add a {@link org.jorigin.db.jdbc.sql.Schema schema} to the database. The schema is added only if it is not already present
   * in the database.
   * @param schema the schema to add
   * @return true if the schema is added, false otherwise.
   */
  public boolean addSchema(Schema schema);
  
  
  /**
   * Remove the {@link org.jorigin.db.jdbc.sql.Schema schema} given in parameter from the database.
   * @param schema the schema to remove.
   * @return true if the schema is removed, false otherwise.
   */
  public boolean removeSchema(Schema schema);
}
