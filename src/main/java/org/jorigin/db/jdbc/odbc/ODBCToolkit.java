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
package org.jorigin.db.jdbc.odbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jorigin.db.jdbc.ColumnHeader;
import org.jorigin.db.jdbc.JDBCConnection;
import org.jorigin.db.jdbc.JDBCConnectionException;
import org.jorigin.db.jdbc.mysql.MySQLColumnHeader;
import org.jorigin.db.jdbc.sql.Catalog;
import org.jorigin.db.jdbc.sql.Database;
import org.jorigin.db.jdbc.sql.SQLToolkit;
import org.jorigin.db.jdbc.sql.Schema;
import org.jorigin.db.jdbc.sql.Server;
import org.jorigin.db.jdbc.sql.Table;


public class ODBCToolkit implements SQLToolkit {

  /**
   * The JDBC connection used by this toolkit
   */
  private JDBCConnection connection = null;
  
  /**
   * Create a new ODBC toolkit for JDBC using the given JDBC connection.
   * @param connection the connection to use.
   */
  public ODBCToolkit(JDBCConnection connection){
    this.connection =  connection; 
  }
  
  
  public boolean addAdministratorPrivilege(String dbName, String dbUser,
      String dbServer) throws JDBCConnectionException {
    return false;
  }

  public boolean addAdministratorPrivilege(String dbName, String dbUser)
      throws JDBCConnectionException {
    return false;
  }

  public boolean addUserPrivilege(String dbName, String dbUser, String dbServer)
      throws JDBCConnectionException {
    return false;
  }

  public boolean addUserPrivilege(String dbName, String dbUser)
      throws JDBCConnectionException {
    return false;
  }

  public boolean addViewerPrivilege(String dbName, String dbUser,
      String dbServer) throws JDBCConnectionException {
    return false;
  }

  public boolean addViewerPrivilege(String dbName, String dbUser)
      throws JDBCConnectionException {
    return false;
  }

  public boolean createDatabase(String dbName) throws JDBCConnectionException {
    return false;
  }

  public boolean createUser(String userName, String host, String password)
      throws JDBCConnectionException {
    return false;
  }

  public boolean deleteDatabase(String dbName) throws JDBCConnectionException {
    return false;
  }

  public boolean deleteTable(String dbName, String tableName)
      throws JDBCConnectionException {
    return false;
  }

  public boolean deleteTable(String tableName) throws JDBCConnectionException {
    return false;
  }

  public boolean deleteUser(String dbUser) throws JDBCConnectionException {
    return false;
  }

  public ResultSet executeQuery(String query) throws JDBCConnectionException {
    Statement statement  = null;
    ResultSet resultSet  = null;
   
    try {
      statement = connection.getConnection().createStatement();
      resultSet = statement.executeQuery(query);

//      statement.close();
    
//      tn = System.currentTimeMillis();
      
//      System.out.println("[ODBCToolkit] [executeQuery] request processed by server in "+(tn-t0)+" ms");
    } catch (SQLException ex) {
      
      
      throw connection.processSQLException(
             ex, connection.getDbName(), connection.getDbUser(), 
             connection.getDbPassword(), connection.getDbServer(), 
             connection.getDbPort(), connection.getDbType(), query);    
    }
    
    return resultSet;   
  }

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

  public JDBCConnection getConnection() {
    return connection;
  }

  public Object[][] getTableData(Table table) throws JDBCConnectionException {
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
      query +=table.getName();
      
    // Dans le cas contraire, on fait confiance à la connection d'être
    // initialisée avec la bonne base.
    } else {
      query += table.getName();
    }
    
    query += ";";
    
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

  public ColumnHeader[] getTableHeader(Table table)
      throws JDBCConnectionException {
    ColumnHeader[] result = null;

    DatabaseMetaData metaData = null;
    
    ArrayList<ColumnHeader> headers = null;
    
    //  Si la table est inexistante, on ne peut rien retourner
    if (table == null){
//      System.out.println("[ODBCToolkit] getTableHeader(Table) no table available ");
      result = null;
    } else {
      
      // Si la base de données contenant la base est connue,
      // on l'utilise pour remonter jusqu'au serveur.
      if (table.getSchema() != null){
        
        // Si le serveur est trouvé, on utilise ses meta donnees
        if (table.getSchema().getServer() != null){
          
          if (table.getSchema().getServer().getMetadata() != null){
            
//            System.out.println("[ODBCToolkit] getTableHeader(Table) server metadata available ");
            
            metaData = table.getDatabase().getServer().getMetadata();
            headers = getTableHeader(metaData, table.getName(), table.getDatabase().getName());
          } else {
            try {
//              System.out.println("[ODBCToolkit] getTableHeader(Table) server metadata not available");
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
//            System.out.println("[ODBCToolkit] getTableHeader(Table) metadata from conn ");
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
//          System.out.println("[ODBCToolkit] getTableHeader(Table) metadata from conn 2");
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
    
//    System.out.println("[MySQLJDBCToolkit] getTableHeader(Table) headers: "+headers);
    
    // Transformation de la liste en tableau
    if (headers != null){
      result =  headers.toArray(new ColumnHeader[headers.size()]);
    } 
    
    return result;
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
/*
      ResultSet catalogs = metaData.getCatalogs();
      
      //catalogs.first();
      while (catalogs.next()){
        System.out.println("[ODBCToolkit] [getTableHeader] Catalog: "+catalogs.getString(1));
      }
      catalogs.close();
*/      
      
      rs = metaData.getColumns(null, null, tableName, null);
      //catalogs.first();
      
      while (rs.next ()) {
        
        name             = rs.getString ("COLUMN_NAME");
        ordinalPosition  = rs.getInt("ORDINAL_POSITION");
        defaultValue     = rs.getString("COLUMN_DEF");
        
        isNullable       = rs.getString("IS_NULLABLE");
        type             = -1;
        
        
        charOctetLength  = rs.getInt("CHAR_OCTET_LENGTH");
        typeName         = rs.getString("DATA_TYPE");
        
        
        numericPrecision = rs.getInt("DECIMAL_DIGITS");
/*        
        numericScale     = rs.getInt("NUMERIC_SCALE");
        columnKey        = rs.getString("COLUMN_KEY");
        extra            = rs.getString("EXTRA");
        privileges       = rs.getString("PRIVILEGES");
        
        columnSize       = 0;
        decimalDigits    = 0;
        nullable         = 0;
        remarks          = rs.getString("COLUMN_COMMENT");
        */
        autoIncrement    = null;

//        System.out.println("[ODBCToolkit] getTableHeader(DatabaseMetaData, String) column name: "+name);
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

  
  
  public ArrayList<Database> listDatabases() throws JDBCConnectionException {
    ArrayList<Database> resultat = new ArrayList<Database>();

    Server server     = null;
    ODBCDatabase database = null;
    
    DatabaseMetaData dmd = null;
    
    ResultSet tables = null;
    
    // Une source de données ODBC ne correspond qu'a une seule base.
    server = new ODBCServer(connection.getName()); 
    database = new ODBCDatabase(connection.getDbName(), server);
    resultat.add(database);

    
    try {
      
      // on récupère les métadonnées à partir de la connexion
      dmd = connection.getConnection().getMetaData();
     
      tables = dmd.getTables(null, null, "%", null);
      
      if (tables != null){
        // Itérer et extraire les valeurs des colonnes dans des variables
        while(tables.next()){
           Object valeurColonne = tables.getObject(3);
           database.addTable(new ODBCTable((String)valeurColonne, database));
        }
      }
      
    } catch (SQLException ex) {
      throw connection.processSQLException(
          ex, connection.getDbName(), connection.getDbUser(), 
          connection.getDbPassword(), connection.getDbServer(), 
          connection.getDbPort(), connection.getDbType(), "List tables using metadata");    
    }
    
    server    = null;
    
    return resultat;  
  }

  public ArrayList<Table> listTables(Database database)
      throws JDBCConnectionException {
    ArrayList<Table> tables = null;
    
    tables = listTables(database.getName());
    
    if (tables != null){
      for(int i = 0; i < tables.size(); i++){
       // tables.get(i).setSchema(database);
      }
    }
    
    return tables;
  }

  public ArrayList<Table> listTables(String dbName)
      throws JDBCConnectionException {
    ArrayList<Table> resultat = new ArrayList<Table>();
    
    Server server        = null;
    
    ODBCDatabase database    = null;
    
    DatabaseMetaData dmd = null;
    
    ResultSet tables     = null;
    
    try {
      
      connection.setDbName(dbName);
     
      server = new ODBCServer(connection.getName()); 
      database = new ODBCDatabase(connection.getDbName(), server);
      
      // on récupère les métadonnées à partir de la connexion
      dmd = connection.getConnection().getMetaData();
     
      tables = dmd.getTables(null, null, "%", null);
      
      if (tables != null){
        // Itérer et extraire les valeurs des colonnes dans des variables
        while(tables.next()){
           Object valeurColonne = tables.getObject(3);
           database.addTable(new ODBCTable((String)valeurColonne, database));
        }
      }
      
    } catch (SQLException ex) {
      throw connection.processSQLException(
          ex, connection.getDbName(), connection.getDbUser(), 
          connection.getDbPassword(), connection.getDbServer(), 
          connection.getDbPort(), connection.getDbType(), "List tables using metadata");    
    }

    return resultat;  
  }

  public Server mapServer() throws JDBCConnectionException {
    Server server                 = null;
    ArrayList<Database> databases = null;
    
    try {

      server = new ODBCServer(connection.getName(), connection.getConnection().getMetaData());
    } catch (SQLException ex) {
      server = new ODBCServer(connection.getName());
    }
    
    databases = listDatabases();
    
    server.setDatabases(databases);
    
    return server; 
  }

  public boolean reloadPrivileges() throws JDBCConnectionException {
    return false;
  }

  public boolean removeBasePrivilege(String dbName, String dbServer)
      throws JDBCConnectionException {
    return false;
  }

  public boolean removeBasePrivilege(String dbName)
      throws JDBCConnectionException {
    return false;
  }

  public boolean removeHostPrivilege(String dbServer)
      throws JDBCConnectionException {
    return false;
  }

  public boolean removePrivilege(String dbName, String dbUser, String dbServer)
      throws JDBCConnectionException {
    return false;
  }

  public boolean removePrivilege(String dbName, String dbUser)
      throws JDBCConnectionException {
    return false;
  }

  public boolean removePrivilege(String dbUser) throws JDBCConnectionException {
    return false;
  }


  @Override
  public Catalog createCatalog(String catalogName, Database database)
      throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public Database createDatabase(String dbName, Server server)
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
