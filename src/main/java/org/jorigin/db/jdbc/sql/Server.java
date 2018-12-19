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

import java.sql.DatabaseMetaData;
import java.util.ArrayList;


/**
 * This classes represent a database server. A server contains a collection of {@link org.jorigin.db.jdbc.sql.Database databases}.
 * @author Julien Seinturier - (c) 2009 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.1.0
 */
public interface Server {
  
  /**
   * Get the name of the server.
   * @return the name of the server
   */
  public String getName();
  
  /**
   * Set the databases hosted by the server
   * @param databases the databases hosted by the server
   */
  public void setDatabases(ArrayList<Database> databases);
  
  /**
   * Get the databases hosted by the server.
   * @return the databases hosted by the server.
   */
  public ArrayList<Database> getDatabases();
  
  /**
   * Get the metadata associated with the server
   * @return the metadata of the server.
   */
  public DatabaseMetaData getMetadata();
  
  /**
   * Add a new database to the server if the database if not in the list already.
   * @param database the database to add to the server list
   * @return true if the database is added, false otherwise
   */
  public boolean addDatabase(Database database);
 
  /**
   * Remove a database from the database list.
   * @param database the database to remove
   * @return true if the database is removed, false
   * otherwise
   */
  public boolean removeDatabase(Database database);
}
