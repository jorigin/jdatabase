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


import java.sql.ResultSet;
import java.util.ArrayList;

import org.jdatabase.JDatabase;
import org.jdatabase.jdbc.ColumnHeader;
import org.jdatabase.jdbc.JDBCConnection;
import org.jdatabase.jdbc.JDBCConnectionException;

/**
 * This classes describe the method to be implemented by all toolkit that can be used as  SQL toolkit for a
 * JDBC connection.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 */
public interface SQLToolkit {
  
  /**
   * Get the {@link org.jdatabase.jdbc.JDBCConnection connection} attached to the toolkit
   * @return the connection attached to the toolkit.
   * @see org.jdatabase.jdbc.JDBCConnection
   */
  public JDBCConnection getConnection();
  
  /**
   * Map the entire {@link org.jdatabase.jdbc.sql.Server server}. This method create a tree representing the server and
   * the databases hosted. Each {@link org.jdatabase.jdbc.sql.Database database} is recursively mapped.
   * @return the mapped server
   * @throws JDBCConnectionException if an error occurs.
   */
  public Server mapServer()
  throws JDBCConnectionException;
  
  /**
   * List all the {@link org.jdatabase.jdbc.sql.Database databases} accessible by a the user logged on the server given in parameter.
   * @param server the server hosting the databases to list.
   * @return the list of database accessible by the user.
   * @throws JDBCConnectionException if an error occurs.
   */
  public ArrayList<Database> listDatabases(Server server) 
  throws JDBCConnectionException;
  
  /**
   * Create a new {@link org.jdatabase.jdbc.sql.Database database} with name given by <code>dbName</code> on the given 
   * {@link org.jdatabase.jdbc.sql.Server server}.
   * @param dbName the name of the database.
   * @param server the server that will host the database.
   * @return the new database if the creation is a success, <code>null</code> if no database has been created.
   * @throws JDBCConnectionException if error occurs.
   */
  public Database createDatabase(String dbName, Server server) 
  throws JDBCConnectionException;
  
  /**
   * Delete a {@link org.jdatabase.jdbc.sql.Database database} from the server. All data within the database are deleted.
   * @param database the database to delete
   * @return true if the database is deleted, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean deleteDatabase(Database database) 
  throws JDBCConnectionException;
  
  
  /**
   * List the {@link org.jdatabase.jdbc.sql.Catalog catalogs} attached to the given database.
   * @param database the database containing the catalogs.
   * @return the list of catalogs contained by this database.
   * @throws JDBCConnectionException if errors occur.
   */
  public ArrayList<Catalog> listCatalogs(Database database)
  throws JDBCConnectionException;
  
  /**
   * Create a new {@link org.jdatabase.jdbc.sql.Catalog catalog} within the database. The catalog is created only if it does not exist.
   * The method return the created catalog or <code>null</code> if the catalog is not created.
   * @param catalogName the name of the new catalog.
   * @param database the {@link org.jdatabase.jdbc.sql.Database database} which the catalog is attached.
   * @return the new catalog if the creation is a success, <code>null</code> if no catalog has been created.
   * @throws JDBCConnectionException if errors occur.
   */
  public Catalog createCatalog(String catalogName, Database database)
  throws JDBCConnectionException;;
  
  /**
   * Delete the given {@link org.jdatabase.jdbc.sql.Catalog catalog} from the {@link org.jdatabase.jdbc.sql.Database database}.
   * @param catalog the catalog to delete.
   * @return <true> if the catalog is successfully deleted, false otherwise.
   * @throws JDBCConnectionException if errors occur.
   */
  public boolean deleteCatalog(Catalog catalog)
  throws JDBCConnectionException;
  
  /**
   * List all the {@link org.jdatabase.jdbc.sql.Schema schemas} available within the given {@link org.jdatabase.jdbc.sql.Database database}.
   * @param database the database containing the database
   * @return the list of schemas contained by this database.
   * @throws JDBCConnectionException if error occurs.
   */
  public ArrayList<Schema> listSchemas(Database database)
  throws JDBCConnectionException;
  
  /**
   * Create a {@link org.jdatabase.jdbc.sql.Schema schema} within the given {@link org.jdatabase.jdbc.sql.Database database}
   * @param schemaName the name of the schema to create.
   * @param database the database hosting the schema.
   * @return the new schema if the creation is a success, <code>null</code> if no schema has been created.
   * @throws JDBCConnectionException if errors occur.
   */
  public Schema createSchema(String schemaName, Database database)
  throws JDBCConnectionException;
  
  /**
   * Delete the given {@link org.jdatabase.jdbc.sql.Schema schema} from its {@link org.jdatabase.jdbc.sql.Catalog catalog}
   * @param schema the schema to delete.
   * @return <true> if the schema is successfully deleted, false otherwise.
   * @throws JDBCConnectionException if errors occur.
   */
  public boolean deleteSchema(Schema schema)
  throws JDBCConnectionException;
  
  /**
   * List all {@link org.jdatabase.jdbc.sql.Table tables} of the {@link org.jdatabase.jdbc.sql.Schema schema} 
   * given in parameter accessible by the user logged on the database server.
   * If the schema name given is <code>null</code>, all tables are shown.
   * @param the schema containing tables to show.
   * @return the list of table accessible by the user
   * @throws JDBCConnectionException if an error occurs
   */
  public ArrayList<Table> listTables(Schema schema)
  throws JDBCConnectionException;
  
  /**
   * Delete (drop) the {@link org.jdatabase.jdbc.sql.Table tables} given in parameter.
   * @param table the table to delete.
   * @return true if the table is deleted, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean deleteTable(Table table)
  throws JDBCConnectionException;
  
  /**
   * Return the content of the given {@link org.jdatabase.jdbc.sql.Table table} as an array of object. 
   * The column headers can be obtained by the {@link #getTableHeader(Table) getTableHeader(Table)} method.
   * @param table the table containing the data
   * @return an array of data
   * @throws JDBCConnectionException if an error occurs
   * @see #getTableHeader(Table);
   */
  public Object[][] getTableData(Table table)
  throws JDBCConnectionException;
  
  /**
   * Get the headers (column names) of a table. The data corresponding can be extracted with method
   * {@link #getTableData(Table)}
   * @param table the table containing desired header
   * @return the headers array
   * @throws JDBCConnectionException if an error occurs
   * @see #getTableData(Table)
   */
  public ColumnHeader[] getTableHeader(Table table)
  throws JDBCConnectionException;
  
  /**
   * Execute a query on the current database. A new statement is created and a boolean is
   * returned.
   * @param query the query to process
   * @return the result set containing the query result
   * @throws JDBCConnectionException if an error occurs during the query processing.
   */
  public ResultSet executeQuery(String query) throws JDBCConnectionException ;
  
  /**
   * Execute an update on the current database. A new statement is created and a boolean is
   * returned.
   * @param query the query to process
   * @return true if the query is processed, false otherwise
   * @throws JDBCConnectionException if an error occurs during the query processing.
   */
  public boolean executeUpdate(String query) throws JDBCConnectionException ;
  
  /**
   * Reload the privilege on the server.
   * @return true if the privilege are successfully reloaded, false otherwise.
   */
  public boolean reloadPrivileges() throws JDBCConnectionException ;
}
