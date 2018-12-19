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
package org.jdatabase.jdbc.sql;

import org.jdatabase.JDatabase;

/**
 * This class represent a table of data stored in a {@link org.jdatabase.jdbc.sql.Schema schema}
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 */
public interface Table {

  /**
   * Get the name of the table
   * @return the name of the table
   */
  public String getName();
  
  /**
   * Get the {@link org.jdatabase.jdbc.sql.Schema schema} containing the table
   * @return the schema containing the table
   */
  public Schema getSchema();
  
  /**
   * Get the {@link org.jdatabase.jdbc.sql.Catalog catalog} containing this table.
   * This method should return the result of <code>getSchema().getCatalog().getDatabase()</code> or <code>null</code> 
   * @return the catalog containing this table.
   */
  public Catalog getCatalog();
  
  /**
   * Get the {@link org.jdatabase.jdbc.sql.Database database} containing the table. 
   * This method should return the result of <code>getSchema().getCatalog().getDatabase()</code> or <code>null</code> 
   * if no information is available.
   * @return the database containing the table.
   */
  public Database getDatabase();
  
  /**
   * Get the {@link org.jdatabase.jdbc.sql.Server server} containing the table.
   * This method should return the result of <code>getSchema().getCatalog().getDatabase().getServer()</code> or <code>null</code> 
   * if no information is available.
   * @return the server containing the table.
   */
  public Server getServer();
  
}
