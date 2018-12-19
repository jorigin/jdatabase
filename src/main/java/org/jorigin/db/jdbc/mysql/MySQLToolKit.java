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
package org.jorigin.db.jdbc.mysql;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;

import org.jorigin.Common;
import org.jorigin.db.jdbc.ColumnHeader;
import org.jorigin.db.jdbc.JDBCConnection;
import org.jorigin.db.jdbc.JDBCConnectionException;
import org.jorigin.db.jdbc.sql.Catalog;
import org.jorigin.db.jdbc.sql.Database;
import org.jorigin.db.jdbc.sql.SQLToolkit;
import org.jorigin.db.jdbc.sql.Schema;
import org.jorigin.db.jdbc.sql.Server;
import org.jorigin.db.jdbc.sql.Table;


/**
 * This class contains MySQL specific wrapper used with JDBC 
 * @author Julien Seinturier
 */
public class MySQLToolKit implements SQLToolkit{

  /**
   * The JDBC connection used by this toolkit
   */
  private JDBCConnection connection = null;
  
  
  /**
   * Create a new MySQL toolkit for JDBC using the given JDBC connection.
   * @param connection the connection to use.
   */
  public MySQLToolKit(JDBCConnection connection){
    this.connection =  connection; 
  }
  
  /**
   * Get the connection attached to the toolkit
   * @return the connection attached to the toolkit.
   */
  public JDBCConnection getConnection(){
    return connection;
  }
  
  @Override
  public Database createDatabase(String dbName, Server server) 
  throws JDBCConnectionException{
//    return executeUpdate("CREATE DATABASE "+dbName+" DEFAULT CHARACTER SET "+charset+" COLLATE "+collate+";"); 
//    return executeUpdate("CREATE DATABASE db_name DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;");
    
    if (executeUpdate("CREATE DATABASE "+dbName+" DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;")){
      return new MySQLDatabase(dbName, server);
    } else {
      return null;
    }
    
    
  }
  
  /**
   * Delete a database from the server. All table and data in the database are deleted.
   * @param dbName the name of the database to delete
   * @return true if the database is deleted, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean deleteDatabase(String dbName) 
  throws JDBCConnectionException{
    
    // Effacement des privileges attaches a la base.
    this.removeBasePrivilege(dbName);
    
    return executeUpdate("DROP DATABASE "+dbName);
  }
  
  
  /**
   * Map the entire server. This method create a tree representing the server and
   * the databases hosted. Each database is also mapped and the table contained in
   * each databases are listed.
   * @return the server map
   * @throws JDBCConnectionException if an error occurs.
   */
  public Server mapServer()
  throws JDBCConnectionException{
    Server server                 = null;
    ArrayList<Database> databases = null;
    
    
    try {
      server = new MySQLServer(connection.getName(), connection.getConnection().getMetaData());
    
    
      try {
        ResultSet rset = connection.getConnection().getMetaData().getCatalogs();

        Common.logger.log(Level.INFO, "Catalogs: ");
        while (rset.next()) {
          Common.logger.log(Level.INFO, "  "+rset.getString("TABLE_CAT"));
        }
        rset.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      try {
        ResultSet rset = connection.getConnection().getMetaData().getSchemas();

        Common.logger.log(Level.INFO, "Schemas: ");
        while (rset.next()) {
          Common.logger.log(Level.INFO, "  "+rset.getString("TABLE_SCHEM")+" ("+rset.getString("TABLE_CATALOG")+")");
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    
      try {
        ResultSet rset = connection.getConnection().getMetaData().getTables(null, null, null, null);

        Common.logger.log(Level.INFO, "Tables: ");
        while (rset.next()) {
          Common.logger.log(Level.INFO, "  "+rset.getString("TABLE_NAME")+" schema: "+rset.getString("TABLE_SCHEM")+" catalog: "+rset.getString("TABLE_CAT"));
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
    } catch (SQLException ex) {
      server = new MySQLServer(connection.getName());
    }
    
    databases = listDatabases();
    
    server.setDatabases(databases);
    
    return server; 
  }
  
  /**
   * List all database accessible by a the user used to log on the database server.
   * @return the list of database accessible by the user.
   * @throws JDBCConnectionException if an error occurs.
   */
  public ArrayList<Database> listDatabases() 
  throws JDBCConnectionException{
    ArrayList<Database> resultat = new ArrayList<Database>();
    
    Statement statement  = null;
    ResultSet resultSet  = null;
    String req           = null;
    
    Server server     = null;
    MySQLDatabase database = null;
    
    // Listage des bases de données
    try {
      statement = connection.getConnection().createStatement();
      resultSet = statement.executeQuery("SHOW DATABASES");
      
      server = new MySQLServer(connection.getName());
      
      while (resultSet.next()) {
        // On ignore le repertoire lost+found present dans les systeme linux
        if(!resultSet.getString(1).equals("lost+found")){
          database = new MySQLDatabase(resultSet.getString(1), server);
          resultat.add(database);
        }
      }
      
      resultSet.close();
      statement.close();
      
    } catch (SQLException ex)  {
      throw connection.processSQLException(
             ex, connection.getDbName(), connection.getDbUser(), 
             connection.getDbPassword(), connection.getDbServer(), 
             connection.getDbPort(), connection.getDbType(), "SHOW DATABASES;");    
    }
    
    // Création de la liste des tables par base de données
    for(int i = 0; i < resultat.size(); i++){
      database = (MySQLDatabase)resultat.get(i);
      
      try {
        statement = connection.getConnection().createStatement();
        if (database.getName() != null){
          req       = "SHOW TABLES FROM `"+database.getName()+"`;";
          resultSet = statement.executeQuery(req);
        } else {
          req       = "SHOW TABLES;";
          resultSet = statement.executeQuery(req);
        }
        
        while (resultSet.next()) {
          // On ignore le repertoire lost+found present dans les systeme linux
          if(!resultSet.getString(1).equals("lost+found")){
            database.addTable(new MySQLTable(resultSet.getString(1), database));
          }
        }
          
        resultSet.close();
        statement.close();
        
      } catch (SQLException ex)  {
        throw connection.processSQLException(
               ex, connection.getDbName(), connection.getDbUser(), 
               connection.getDbPassword(), connection.getDbServer(), 
               connection.getDbPort(), connection.getDbType(), req);    
      }
      
    }
    
    
    resultSet = null;
    statement = null;
    server    = null;
    
    return resultat;  
  }
  
  
  public ArrayList<Table> listTables(Database database)
  throws JDBCConnectionException{
    ArrayList<Table> tables = null;
    
    tables = listTables(database.getName());
    
    if (tables != null){
      for(int i = 0; i < tables.size(); i++){
        //((MySQLTable)tables.get(i)).setSchema((MySQLDatabase)database);
      }
    }
    
    return tables;
  }
  
  /**
   * List all tables of the database given in parameter accessible by the user logged on the database server.
   * If the database name given is <code>null</code>, all tables are shown.
   * @param the name of the database containing tables to show.
   * @return the list of table accessible by the user
   * @throws JDBCConnectionException if an error occurs
   */
  public ArrayList<Table> listTables(String dbName)
  throws JDBCConnectionException{
    ArrayList<Table> resultat = new ArrayList<Table>();
    
    Statement statement  = null;
    ResultSet resultSet  = null;
    
    MySQLDatabase database    = null;
    
    try {
      statement = connection.getConnection().createStatement();
      if (dbName != null){
        resultSet = statement.executeQuery("SHOW TABLES FROM "+dbName);
        database = new MySQLDatabase(dbName, null);
      } else {
        resultSet = statement.executeQuery("SHOW TABLES");
      }
      
      while (resultSet.next()) {
        // On ignore le repertoire lost+found present dans les systeme linux
        if(!resultSet.getString(1).equals("lost+found")){
          resultat.add(new MySQLTable(resultSet.getString(1), database));
        }
      }
        
      resultSet.close();
      statement.close();
      
    } catch (SQLException ex)  {
      throw connection.processSQLException(
             ex, connection.getDbName(), connection.getDbUser(), 
             connection.getDbPassword(), connection.getDbServer(), 
             connection.getDbPort(), connection.getDbType(), "SHOW TABLES");    
    }
    
    resultSet = null;
    statement = null;
    
    return resultat;  
  }
  
  /**
   * Return the content of the given table as an array of object. The column headers can be obtained
   * by the <code>Object[] getTableHeader(Table database)</code> method.
   * @param table the table containingthe data
   * @return an array of data
   * @throws JDBCConnectionException if an error occurs
   * @see #getTableHeader(Table);
   */
  public Object[][] getTableData(Table table)
  throws JDBCConnectionException{
    Object[][] result             = null;
    String query                  = "SELECT * FROM ";
    Object[] line                 = null;
    int columnCount               = 0;
    ResultSet resultSet           = null;
    ArrayList<Object[]> arrayList = null;
    
    // Si la table est nulle, pas de reqête
    if (table == null){
      return null;
    }
    
    // Si la base de données est connut, on lui faut précéder le nom de la table
    if (table.getSchema() != null){
      query += table.getSchema().getName()+"."+table.getName();
      
    // Dans le cas contraire, on fait confiance à la connection d'être
    // initialisée avec la bonne base.
    } else {
      query += table.getName();
    }
    
    // Requete sur la table pour avoir toutes les données
    // Chaque ligne est stockée dans un tableau. Chaque tableau
    // est stocké dans une liste de lignes
    try {
      resultSet   = connection.executeQuery(query);
      columnCount = resultSet.getMetaData().getColumnCount();
      line        = new Object[columnCount];
      arrayList   = new ArrayList<Object[]>();
      
      while(resultSet.next()){
        for(int i = 1; i <= columnCount; i++){
          line[i - 1] = resultSet.getObject(i);
        }
        arrayList.add(line);
        line = new Object[columnCount];
      }
      
      resultSet.close();
      resultSet = null;
      
    } catch (SQLException ex) {
      try {
        resultSet.close();
        resultSet = null;
       
      } catch (SQLException ex1) {
        resultSet = null;
      }
      
      throw connection.processSQLException(ex, 
          connection.getDbName(), 
          connection.getDbUser(), 
          connection.getDbPassword(), 
          connection.getDbServer(), 
          connection.getDbPort(), 
          connection.getDbType(), 
          "metadata.getColumns()");
    }
    
    // Si le resultat n'est pas nul, le tableau des resultats est cree
    if ((arrayList != null) && (arrayList.size() > 0)){
      result = arrayList.toArray(new Object[arrayList.size()][columnCount]);
    } else {
      result = null;
    }
    
    arrayList.clear();
    arrayList = null;
    query     = null;
    line      = null;
    
    return result;
  }
  
  /**
   * Get the headers (column names) of a table. The data corresponding can be extracted with method
   * <code>Object[][] getTableData(Table table)</code>
   * @param table the table containing desired header
   * @return the headers array
   * @throws JDBCConnectionException if an error occurs
   * @see #getTableData(Table)
   */
  public ColumnHeader[] getTableHeader(Table table)
  throws JDBCConnectionException{
    ColumnHeader[] result = null;

    DatabaseMetaData metaData = null;
    
    ArrayList<ColumnHeader> headers = null;
    
    //  Si la table est inexistante, on ne peut rien retourner
    if (table == null){
      System.out.println("[MySQLJDBCToolkit] getTableHeader(Table) no table available ");
      result = null;
    } else {
      
      // Si la base de données contenant la base est connue,
      // on l'utilise pour remonter jusqu'au serveur.
      if (table.getDatabase() != null){
        
        // Si le serveur est trouvé, on utilise ses meta donnees
        if (table.getDatabase().getServer() != null){
          
          if (table.getDatabase().getServer().getMetadata() != null){
            
            System.out.println("[MySQLJDBCToolkit] getTableHeader(Table) server metadata available ");
            
            metaData = table.getDatabase().getServer().getMetadata();
            headers = getTableHeader(metaData, table.getName(), table.getDatabase().getName());
          } else {
            try {
              System.out.println("[MySQLJDBCToolkit] getTableHeader(Table) server metadata not available");
              metaData = connection.getConnection().getMetaData();
              headers = getTableHeader(metaData, table.getName(), table.getDatabase().getName());
            } catch (SQLException ex) {
              throw connection.processSQLException(ex, 
                  connection.getDbName(), 
                  connection.getDbUser(), 
                  connection.getDbPassword(), 
                  connection.getDbServer(), 
                  connection.getDbPort(), 
                  connection.getDbType(), 
                  "metadata.getColumns()");
            }
          }
          
        // Dans le cas contraire, on utilise la connection pour acceder aux metadonnees  
        } else {
          try {
            System.out.println("[MySQLJDBCToolkit] getTableHeader(Table) metadata from conn ");
            metaData = connection.getConnection().getMetaData();
            headers = getTableHeader(metaData, table.getName(), table.getDatabase().getName());
          } catch (SQLException ex) {
            throw connection.processSQLException(ex, 
                connection.getDbName(), 
                connection.getDbUser(), 
                connection.getDbPassword(), 
                connection.getDbServer(), 
                connection.getDbPort(), 
                connection.getDbType(), 
                "metadata.getColumns()");
          }
        }
        
      // Si la base de donnée n'existe pas, on utilise la connection
      // pour rechercher des meta donnees
      } else {
        try {
          System.out.println("[MySQLJDBCToolkit] getTableHeader(Table) metadata from conn 2");
          metaData = connection.getConnection().getMetaData();
          headers = getTableHeader(metaData, table.getName(), null);
        } catch (SQLException ex) {
          throw connection.processSQLException(ex, 
              connection.getDbName(), 
              connection.getDbUser(), 
              connection.getDbPassword(), 
              connection.getDbServer(), 
              connection.getDbPort(), 
              connection.getDbType(), 
              "metadata.getColumns()");
        }
      }
    }
    
    System.out.println("[MySQLJDBCToolkit] getTableHeader(Table) headers: "+headers);
    
    // Transformation de la liste en tableau
    if (headers != null){
      result =  headers.toArray(new ColumnHeader[headers.size()]);
    } 
    
    return result;
  }
  
  
  /**
   * Delete (drop) the targeted table in the database with name given in parameter.
   * @param dbName the name of the database containing the table to delete.
   * @param tableName the name of the table to delete.
   * @return true if the table is deleted, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean deleteTable(String dbName, String tableName)
  throws JDBCConnectionException{
    return executeUpdate("DROP TABLE " + dbName + "." + tableName);    
  }
  
  /**
   * Delete (drop) the targeted table in the current database.
   * @param tableName the name of the table to delete.
   * @return true if the table is deleted, false otherwise
   * @throws JDBCConnectionException JDBCConnectionException if an error occurs.
   */
  public boolean deleteTable(String tableName)
  throws JDBCConnectionException{
    return executeUpdate("DROP TABLE " + connection.getDbName() + "." + tableName);        
  }
  
  
  
  public boolean createUser(String userName, 
                              String host, 
                              String password
                              )
  throws JDBCConnectionException{
    
    
    
    String query =  "INSERT INTO mysql.user ( "
                  + "`Host`"                   +" , "
                  + "`User`"                   +" , "
                  + "`Password`"               +" , "
                  + "`Select_priv`"            +" , "
                  + "`Insert_priv`"            +" , "
                  + "`Update_priv`"            +" , "
                  + "`Delete_priv`"            +" , "
                  + "`Create_priv`"            +" , "
                  + "`Drop_priv`"              +" , " 
                  + "`Reload_priv`"            +" , "
                  + "`Shutdown_priv`"          +" , "
                  + "`Process_priv`"           +" , "
                  + "`File_priv`"              +" , "
                  + "`Grant_priv`"             +" , "
                  + "`References_priv`"        +" , "
                  + "`Index_priv`"             +" , "
                  + "`Alter_priv`"             +" , "
                  + "`Show_db_priv`"           +" , "
                  + "`Super_priv`"             +" , "
                  + "`Create_tmp_table_priv`"  +" , "
                  + "`Lock_tables_priv`"       +" , "
                  + "`Execute_priv`"           +" , "
                  + "`Repl_slave_priv`"        +" , "
                  + "`Repl_client_priv`"       +" , "
                  + "`Create_view_priv`"       +" , "
                  + "`Show_view_priv`"         +" , "
                  + "`Create_routine_priv`"     +" , "
                  + "`Alter_routine_priv`"     +" , "
                  + "`Create_user_priv`"       +" , "
                  + "`ssl_type`"               +" , "
                  + "`ssl_cipher`"             +" , "
                  + "`x509_issuer`"            +" , "
                  + "`x509_subject`"           +" , "
                  + "`max_questions`"          +" , "
                  + "`max_updates`"            +" , "
                  + "`max_connections`"        +" , "
                  + "`max_user_connections`"   
                  
                  + ") VALUES ("
                  
                  + "'"+host+"'"               +", " // Host
                  + "'"+userName+"'"           +", " // User
                  + "PASSWORD('"+password+"')" +", " // Password
                  + "'Y'"                      +", " // Select
                  + "'N'"                      +", " // Insert
                  + "'N'"                      +", " // Update
                  + "'N'"                      +", " // Delete
                  + "'N'"                      +", " // Create
                  + "'N'"                      +", " // Drop
                  + "'N'"                      +", " // Reload
                  + "'N'"                      +", " // Shutdown
                  + "'N'"                      +", " // Process
                  + "'N'"                      +", " // File
                  + "'N'"                      +", " // Grant
                  + "'N'"                      +", " // References
                  + "'N'"                      +", " // Index
                  + "'N'"                      +", " // Alter
                  + "'N'"                      +", " // Show DB
                  + "'N'"                      +", " // Super
                  + "'N'"                      +", " // Create tmp table
                  + "'N'"                      +", " // Lock table
                  + "'N'"                      +", " // Execute
                  + "'N'"                      +", " // Repl slave
                  + "'N'"                      +", " // Repl client
                  + "'N'"                      +", " // Create view
                  + "'N'"                      +", " // Show view
                  + "'N'"                      +", " // Create routine
                  + "'N'"                      +", " // Alter routine
                  + "'N'"                      +", " // Create user priv
                  + "''"                       +", " // ssl type
                  + "''"                       +", " // ssl cipher
                  + "''"                       +", " // x509 issuer
                  + "''"                       +", " // x509 subject
                  + "'0'"                      +", " // max questions
                  + "'0'"                      +", " // max updates
                  + "'0'"                      +", " // max connections
                  + "'0'"                            // max user connections
                  + ");";
     
    return executeUpdate(query);
  }
  
  
  /**
   * Delete the user given in parameter from the database server.
   * @param dbUser the name of the user to delete
   * @return true if the user is deleted, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean deleteUser(String dbUser)
  throws JDBCConnectionException{
    
    String query = "DELETE FROM mysql.user WHERE `User` = '"+dbUser+"';";
    
    // Effacement des privileges de l'utilisateur
    this.removePrivilege(dbUser);
    
    return executeUpdate(query);
  }
  
  
  /**
   * Set the user with name given in parameter to be administrator of the 
   * chosen database. The user must bu a valid user name (for example created 
   * with the method {@link createUser()}) and the database must exist.  
   * @param dbName the name of the database
   * @param dbUser the name of the user
   * @param dbServer the server where the user should be logged (% for all server)
   * @return true if the administrator is created, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean addAdministratorPrivilege(String dbName, String dbUser, String dbServer)
  throws JDBCConnectionException{
    
    if (dbServer == null){
      dbServer = "%";
    }
    
    String query =  "INSERT INTO mysql.db ("
                   +"`Host`"                  +" , "
                   +"`Db`"                    +" , "
                   +"`User`"                  +" , "
                   +"`Select_priv`"           +" , "
                   +"`Insert_priv`"           +" , "
                   +"`Update_priv`"           +" , "
                   +"`Delete_priv`"           +" , "
                   +"`Create_priv`"           +" , "
                   +"`Drop_priv`"             +" , "
                   +"`Grant_priv`"            +" , "
                   +"`References_priv`"       +" , "
                   +"`Index_priv`"            +" , "
                   +"`Alter_priv`"            +" , "
                   +"`Create_tmp_table_priv`" +" , "
                   +"`Lock_tables_priv`"      +" , "
                   +"`Create_view_priv`"      +" , "
                   +"`Show_view_priv`"        +" , "
                   +"`Create_routine_priv`"   +" , "
                   +"`Alter_routine_priv`"    +" , "
                   +"`Execute_priv`"
                   
                   +") VALUES ("
                       
                   +"'"+dbServer+"'"    +" , "
                   +"'"+dbName+"'"    +" , "
                   +"'"+dbUser+"'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"
                   +");";
    
    return executeUpdate(query);
  }
  
  
  /**
   * Set the user with name given in parameter to be administrator of the 
   * chosen database. The user must bu a valid user name (for example created 
   * with the method {@link createUser()}) and the database must exist.
   * @param dbName the name of the database
   * @param dbUser the name of the user
   * @return true if the administrator is created, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   * @see addAdministrator(String dbName, String dbUser, String host)
   */
  public boolean addAdministratorPrivilege(String dbName, String dbUser)
  throws JDBCConnectionException{
    return addAdministratorPrivilege(dbName, dbUser, null);
  }
  
   
  /**
   * Set the user with name given in parameter to be standard user of the 
   * chosen database. The user must bu a valid user name (for example created 
   * with the method {@link createUser()}) and the database must exist.  
   * @param dbName the name of the database
   * @param dbUser the name of the user
   * @param dbServer the server where the user should be logged (% for all server)
   * @return true if the administrator is created, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean addUserPrivilege(String dbName, String dbUser, String dbServer)
  throws JDBCConnectionException{
    
    if (dbServer == null){
      dbServer = "%";
    }
    
    String query =  "INSERT INTO mysql.db ("
                   +"`Host`"                  +" , "
                   +"`Db`"                    +" , "
                   +"`User`"                  +" , "
                   +"`Select_priv`"           +" , "
                   +"`Insert_priv`"           +" , "
                   +"`Update_priv`"           +" , "
                   +"`Delete_priv`"           +" , "
                   +"`Create_priv`"           +" , "
                   +"`Drop_priv`"             +" , "
                   +"`Grant_priv`"            +" , "
                   +"`References_priv`"       +" , "
                   +"`Index_priv`"            +" , "
                   +"`Alter_priv`"            +" , "
                   +"`Create_tmp_table_priv`" +" , "
                   +"`Lock_tables_priv`"      +" , "
                   +"`Create_view_priv`"      +" , "
                   +"`Show_view_priv`"        +" , "
                   +"`Create_routine_priv`"   +" , "
                   +"`Alter_routine_priv`"    +" , "
                   +"`Execute_priv`"
                   
                   +") VALUES ("
                       
                   +"'"+dbServer+"'"    +" , "
                   +"'"+dbName+"'"    +" , "
                   +"'"+dbUser+"'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"    +" , "
                   +"'Y'"
                   +");";
    
    return executeUpdate(query);
  }
 
  
  /**
   * Set the user with name given in parameter to be standard user of the 
   * chosen database. The user must bu a valid user name (for example created 
   * with the method {@link createUser()}) and the database must exist.  
   * @param dbName the name of the database
   * @param dbUser the name of the user
   * @return true if the administrator is created, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean addUserPrivilege(String dbName, String dbUser)
  throws JDBCConnectionException{
    return addUserPrivilege(dbName, dbUser, null);
  }
  
  
  /**
   * Set the user with name given in parameter to be a viewer of the 
   * chosen database. A viewer can only view data. No modification is authorized.
   * The user must bu a valid user name (for example created 
   * with the method {@link createUser()}) and the database must exist.  
   * @param dbName the name of the database
   * @param dbUser the name of the user
   * @param dbServer the server where the user should be logged (% for all server)
   * @return true if the administrator is created, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean addViewerPrivilege(String dbName, String dbUser, String dbServer)
  throws JDBCConnectionException{
    
    if (dbServer == null){
      dbServer = "%";
    }
    
    String query =  "INSERT INTO mysql.db ("
                   +"`Host`"                  +" , "
                   +"`Db`"                    +" , "
                   +"`User`"                  +" , "
                   +"`Select_priv`"           +" , "
                   +"`Insert_priv`"           +" , "
                   +"`Update_priv`"           +" , "
                   +"`Delete_priv`"           +" , "
                   +"`Create_priv`"           +" , "
                   +"`Drop_priv`"             +" , "
                   +"`Grant_priv`"            +" , "
                   +"`References_priv`"       +" , "
                   +"`Index_priv`"            +" , "
                   +"`Alter_priv`"            +" , "
                   +"`Create_tmp_table_priv`" +" , "
                   +"`Lock_tables_priv`"      +" , "
                   +"`Create_view_priv`"      +" , "
                   +"`Show_view_priv`"        +" , "
                   +"`Create_routine_priv`"   +" , "
                   +"`Alter_routine_priv`"    +" , "
                   +"`Execute_priv`"
                   
                   +") VALUES ("
                       
                   +"'"+dbServer+"'"    +" , "
                   +"'"+dbName+"'"    +" , "
                   +"'"+dbUser+"'"    +" , "
                   +"'Y'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'Y'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'Y'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"    +" , "
                   +"'N'"
                   +");";
    
    return executeUpdate(query);
  }
 
  
  /**
   * Set the user with name given in parameter to be a viewer of the 
   * chosen database. A viewer can only view data. No modification is authorized.
   * The user must bu a valid user name (for example created 
   * with the method {@link createUser()}) and the database must exist.  
   * @param dbName the name of the database
   * @param dbUser the name of the user
   * @return true if the administrator is created, false otherwise
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean addViewerPrivilege(String dbName, String dbUser)
  throws JDBCConnectionException{
    return addViewerPrivilege(dbName, dbUser, null);
  }
  
  
  /**
   * Remove priviliege for the given user logged on the given server 
   * for the specified base. If <code>dbName</code> is null, the privilege on all
   * databases are removed. If <code>dbServer</code> is null, privilege are removed 
   * for all hosts. If all paramater are set to <code>null</code>, this method simply 
   * return <code>false</code> without doing anything.
   * @param dbName the name of the database
   * @param dbUser the name of the user
   * @param dbServer the server where the user should be logged (% for all server)
   * @return true if the privilege are removed.
   * @throws JDBCConnectionException if an error occurs.
   */
  public boolean removePrivilege(String dbName, String dbUser, String dbServer)
  throws JDBCConnectionException{
    
    String query = null;
    
    // Un nom de serveur est spécifié
    if (dbServer != null){
      query = "`Host` = '"+dbServer+"'"; 
    }
    
    // Un nom de base de donnée est spécifié
    if (dbName != null){
      if (query != null){
        query += " AND `Db` = '"+dbName+"'";
      } else {
        query = "`Db` = '"+dbName+"'";
      }
    }
    
    
    // Un nom d'utilisateur est spécifié.
    if (dbUser != null){
      if (query != null){
        query = "AND `User` = '"+dbUser+"'";
      } else {
        query = "`User` = '"+dbUser+"'";
      }
    }
    
    
    // Si la requete n'est pas nulle (au moins un parametre est non nul)
    // alors elle est executée.
    if (query != null){
      query = "DELETE FROM mysql.db WHERE "+query;
      return executeUpdate(query);
    }
    
    // La requete n'a pas été exécutée car tous les paramètres sont null.
    return false;
  
  }
  
  
  /**
   * Remove priviliege for the given user
   * on the specified base. This method
   * call <code>removePrivilege(dbName, dbUser, null);</code>
   * @param dbName the name of the database
   * @param dbUser the name of the user
   * @return true if the privilege are removed.
   * @throws JDBCConnectionException if an error occurs.
   * @see removeAdministrator(String dbName, String dbUser, String dbServer)
   */
  public boolean removePrivilege(String dbName, String dbUser)
  throws JDBCConnectionException{
   
    return removePrivilege(dbName, dbUser, null);
  }
 
  /**
   * Remove all privilege for the given user. After this method, the user 
   * should not be able to make any action on the databases. This method
   * call <code>removePrivilege(null, dbUser, null);</code>
   * @param dbUser the name of the user
   * @return true if the privilege are removed.
   * @throws JDBCConnectionException  if an error occurs.
   * @see removeAdministrator(String dbName, String dbUser, String dbServer)
   */
  public boolean removePrivilege(String dbUser)
  throws JDBCConnectionException{
    
    return removePrivilege(null, dbUser, null);
  }
  
  
  /**
   * Remove all privileges on the database <code>dbName</code> for the host
   * <code>dbServer</code>. This method
   * call <code>removePrivilege(dbName, null, dbServer);</code>
   * @param dbName the name of the database
   * @param dbServer the server where the user should be logged (% for all server)
   * @return true if the privilege are removed.
   * @throws JDBCConnectionException if an error occurs.
   * @see removeAdministrator(String dbName, String dbUser, String dbServer)
   */
  public boolean removeBasePrivilege(String dbName, String dbServer)
  throws JDBCConnectionException{
    return removePrivilege(dbName, null, dbServer);
  }
  
  
  /**
   * Remove all privileges on the database <code>dbName</code> for all hosts. 
   * This method
   * call <code>removePrivilege(dbName, null, null);</code>
   * @param dbName the name of the database
   * @return true if the privilege are removed.
   * @throws JDBCConnectionException if an error occurs.
   * @see removeAdministrator(String dbName, String dbUser, String dbServer)
   */
  public boolean removeBasePrivilege(String dbName)
  throws JDBCConnectionException{
    return removePrivilege(dbName, null, null);
  }
  
  
  /**
   * Remove all privilege attached to the host <code>dbServer</code>. This method
   * call <code>removePrivilege(null, null, dbServer)</code>
   * @param dbServer the server where the user should be logged (% for all server)
   * @return true if the privilege are removed.
   * @throws JDBCConnectionException if an error occurs.
   * @see removeAdministrator(String dbName, String dbUser, String dbServer)
   */
  public boolean removeHostPrivilege(String dbServer)
  throws JDBCConnectionException{
    return removePrivilege(null, null, dbServer);
  }
  
  /**
   * Execute a query on the database. A new statement is created and a bollean is
   * returned.
   * @param query the query to process
   * @return the result set containing the query result
   * @throws JDBCConnectionException if an error occurs during the query processing.
   */
  public ResultSet executeQuery(String query) throws JDBCConnectionException {
  
    
  
    Statement statement  = null;
    ResultSet resultSet  = null;
   
    long t0 = System.currentTimeMillis();
    long tn = 0;
    
    try {
      statement = connection.getConnection().createStatement();
      resultSet = statement.executeQuery(query);

//      statement.close();
    
      tn = System.currentTimeMillis();
      
      System.out.println("[MySQLToolkit] [executeQuery] request processed by server in "+(tn-t0)+" ms");
    } catch (SQLException ex) {
      
      
      throw connection.processSQLException(
             ex, connection.getDbName(), connection.getDbUser(), 
             connection.getDbPassword(), connection.getDbServer(), 
             connection.getDbPort(), connection.getDbType(), query);    
    }
    
    return resultSet;    
  }
  
  
  
  
  
  
  /**
   * Execute an update on the database. A new statement is created and a bollean is
   * returned.
   * @param query the query to process
   * @return true if the query is processed, false otherwise
   * @throws JDBCConnectionException if an error occurs during the query processing.
   */
  public boolean executeUpdate(String query) throws JDBCConnectionException {
    
    boolean resultat    = false;
    Statement statement  = null;
   
    
    try {
      statement = connection.getConnection().createStatement();
      statement.executeUpdate(query);
        
      resultat = true;
        
      statement.close();
    
    } catch (SQLException ex) {
      resultat = false;
      
      throw connection.processSQLException(
             ex, connection.getDbName(), connection.getDbUser(), 
             connection.getDbPassword(), connection.getDbServer(), 
             connection.getDbPort(), connection.getDbType(), query);    
    }
        
    statement = null;
    
    return resultat;    
  }
  
  
  /**
   * Private function that extract column headers from metadata table
   * @param metaData the metadata to use
   * @param tableName the table containing the columns
   * @return the list of column headers.
   */
  private ArrayList<ColumnHeader> getTableHeader(DatabaseMetaData metaData, String tableName, String databaseName)
  throws JDBCConnectionException{
    
    
    String name         = null;
    int type            = -1;
    String typeName     = null;
    int columnSize      = -1;
    int decimalDigits   = 0;
    int nullable        = 0;
    String remarks      = null;
    String defaultValue = null;
    int charOctetLength = 0;
    int ordinalPosition = 0;
    String isNullable = "";
    String autoIncrement = "";

    int numericPrecision = 0;
    int numericScale     = 0;
    
    String columnKey     = null;
    String extra         = null;
    String privileges    = null;
    
    ArrayList<ColumnHeader> headers = new ArrayList<ColumnHeader>();
    
    if ((metaData == null) || (tableName == null)){
      return null;
    }
    
    try {
      ResultSet rs = null;
      
      if (databaseName == null){
        databaseName = connection.getDbName();
      }

      rs = connection.executeQuery("SELECT * FROM information_schema.COLUMNS WHERE `TABLE_SCHEMA` = '"+databaseName+"'"
          + "AND `TABLE_NAME` = '"+tableName+"';");
      
      System.out.println("[MySQLJDBCToolkit] getTableHeader(DatabaseMetaData, String) extracting columns for "+tableName+"@"+databaseName);
      while (rs.next ()) {
        
        name             = rs.getString ("COLUMN_NAME");
        ordinalPosition  = rs.getInt("ORDINAL_POSITION");
        defaultValue     = rs.getString("COLUMN_DEFAULT");
        isNullable       = rs.getString("IS_NULLABLE");
        type             = -1;
        charOctetLength  = rs.getInt("CHARACTER_OCTET_LENGTH");
        typeName         = rs.getString("DATA_TYPE");
        numericPrecision = rs.getInt("NUMERIC_PRECISION");
        numericScale     = rs.getInt("NUMERIC_SCALE");
        columnKey        = rs.getString("COLUMN_KEY");
        extra            = rs.getString("EXTRA");
        privileges       = rs.getString("PRIVILEGES");
        
        columnSize       = 0;
        decimalDigits    = 0;
        nullable         = 0;
        remarks          = rs.getString("COLUMN_COMMENT");
    
        autoIncrement    = null;

        System.out.println("[MySQLJDBCToolkit] getTableHeader(DatabaseMetaData, String) column name: "+name);
        headers.add(new MySQLColumnHeader(name, type, typeName, columnSize, 
                                     decimalDigits, nullable, remarks, defaultValue, 
                                     charOctetLength, ordinalPosition, isNullable, autoIncrement, numericPrecision,
                                     numericScale, columnKey, extra, privileges));
        
        name          = null;
        typeName      = null;
        remarks       = null;
        defaultValue  = null;
        isNullable    = null;
        autoIncrement = null;
      }
    } catch (SQLException ex) {
      throw connection.processSQLException(ex, 
                                           connection.getDbName(), 
                                           connection.getDbUser(), 
                                           connection.getDbPassword(), 
                                           connection.getDbServer(), 
                                           connection.getDbPort(), 
                                           connection.getDbType(), 
                                           "metadata.getColumns()");
    }
    
    return headers;
  }
  
  
  /**
   * Reload the privilege on the server.
   * @return true if the privilege are successfully reloaded, false otherwise.
   */
  public boolean reloadPrivileges() throws JDBCConnectionException{
    
    connection.executeQuery("FLUSH PRIVILEGES");
    
    return true;
  }

  @Override
  public Catalog createCatalog(String catalogName, Database database)
      throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Schema createSchema(String schemaName, Database database)
      throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean deleteCatalog(Catalog catalog) throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean deleteDatabase(Database database)
      throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean deleteSchema(Schema schema) throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean deleteTable(Table table) throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ArrayList<Catalog> listCatalogs(Database database)
      throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ArrayList<Database> listDatabases(Server server)
      throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ArrayList<Schema> listSchemas(Database database)
      throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ArrayList<Table> listTables(Schema schema)
      throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return null;
  }
  
}
