package org.jdatabase.jdbc.pgsql;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

import org.jdatabase.JDatabase;
import org.jdatabase.jdbc.ColumnHeader;
import org.jdatabase.jdbc.JDBCConnection;
import org.jdatabase.jdbc.JDBCConnectionException;
import org.jdatabase.jdbc.mysql.MySQLColumnHeader;
import org.jdatabase.jdbc.sql.Catalog;
import org.jdatabase.jdbc.sql.Database;
import org.jdatabase.jdbc.sql.SQLToolkit;
import org.jdatabase.jdbc.sql.Schema;
import org.jdatabase.jdbc.sql.Server;
import org.jdatabase.jdbc.sql.Table;
import org.jorigin.Common;

/**
 * 
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 *
 */
public class PGSQLToolKit implements SQLToolkit{

  /**
   * The JDBC connection used by this toolkit
   */
  private JDBCConnection connection = null;
  
  /**
   * Create a new PostgreSQL toolkit.
   * @param connection the connection to the database.
   */
  public PGSQLToolKit(JDBCConnection connection){
    this.connection = connection;
  }

  @Override
  public Server mapServer() throws JDBCConnectionException {
    Server server                 = null;
    ArrayList<Database> databases = null;
    
    try {
      server = new PGSQLServer(connection.getName(), connection.getConnection().getMetaData());
    } catch (SQLException ex) {
      Common.logger.log(Level.SEVERE, "Cannot access server metadata.", ex);
      server = new PGSQLServer(connection.getName());
    }
    
    databases = listDatabases(server);
    
    server.setDatabases(databases);
    
    return server; 
  }
  
  @Override
  public Database createDatabase(String dbName, Server server) throws JDBCConnectionException {
    
    if (executeUpdate("CREATE DATABASE "+dbName+";")){
      return new PGSQLDatabase(dbName, server);  
    } else {
      return null;
    }
  }

  @Override
  public boolean deleteDatabase(Database database) throws JDBCConnectionException {
    return executeUpdate("DROP DATABASE "+database.getName()+";"); 
  }

  @Override
  public boolean deleteTable(Table table)
      throws JDBCConnectionException {
    
    String tabName = null;
    
    if (table == null){
      return false;
    }
    
    if (table.getSchema() != null){
      tabName = table.getSchema().getName()+"."+table.getName();
    } else {
      tabName = table.getName();
    }
    
    return executeUpdate("DROP TABLE "+tabName+";"); 
  }

  @Override
  public ResultSet executeQuery(String query) throws JDBCConnectionException {
    Statement statement  = null;
    ResultSet resultSet  = null;
   
    long t0 = System.currentTimeMillis();
    long tn = 0;
    
    try {
      statement = connection.getConnection().createStatement();
      resultSet = statement.executeQuery(query);
    
      tn = System.currentTimeMillis();
      
      Common.logger.log(Level.FINE, "Request "+query+" processed by server in "+(tn-t0)+" ms");
    } catch (SQLException ex) {
      
      
      throw connection.processSQLException(
             ex, connection.getDbName(), connection.getDbUser(), 
             connection.getDbPassword(), connection.getDbServer(), 
             connection.getDbPort(), connection.getDbType(), query);    
    }
    
    return resultSet;    
  }

  @Override
  public boolean executeUpdate(String query) throws JDBCConnectionException {
    boolean resultat    = false;
    Statement statement  = null;
   
    long t0 = System.currentTimeMillis();
    long tn = 0;
    
    try {
      statement = connection.getConnection().createStatement();
      statement.executeUpdate(query);
        
      tn = System.currentTimeMillis();
      
      resultat = true;
        
      statement.close();
    
      Common.logger.log(Level.FINE, "Update "+query+" processed by server in "+(tn-t0)+" ms");
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

  @Override
  public JDBCConnection getConnection() {
    return connection;
  }

  @Override
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

  @Override
  public ColumnHeader[] getTableHeader(Table table)
      throws JDBCConnectionException {
    ColumnHeader[] result = null;

    DatabaseMetaData metaData = null;
    
    ArrayList<ColumnHeader> headers = null;
    
    //  Si la table est inexistante, on ne peut rien retourner
    if (table == null){
      Common.logger.log(Level.WARNING, "getTableHeader(Table) no table available");
      result = null;
    } else {
      
      // Si la base de données contenant la base est connue,
      // on l'utilise pour remonter jusqu'au serveur.
      if (table.getSchema() != null){
        
        // Si le serveur est trouvé, on utilise ses meta donnees
        if (table.getSchema().getCatalog() != null){
          
          if (table.getDatabase().getServer().getMetadata() != null){
            
            Common.logger.log(Level.INFO, "getTableHeader(Table) server metadata available");
            
            metaData = table.getDatabase().getServer().getMetadata();
            headers = getTableHeader(metaData, table.getName(), table.getDatabase().getName());
          } else {
            try {
              Common.logger.log(Level.WARNING, "getTableHeader(Table) server metadata not available");
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
            Common.logger.log(Level.INFO, "getTableHeader(Table) metadata from connection");
            metaData = connection.getConnection().getMetaData();
            headers = getTableHeader(metaData, table.getName(), table.getSchema().getName());
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
          Common.logger.log(Level.INFO, "getTableHeader(Table) metadata from connection");
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

    
    // Transformation de la liste en tableau
    if (headers != null){
      result =  headers.toArray(new ColumnHeader[headers.size()]);
    } 
    
    return result;
  }

  @Override
  public ArrayList<Database> listDatabases(Server server) throws JDBCConnectionException {
    
    ArrayList<Database> databases = new ArrayList<Database>();
    
    Statement statement       = null;
    ResultSet resultSet       = null;

    Iterator<Database> iter   = null;
    Database database         = null;
    
    ArrayList<Schema> schemas = null;
    
    if (server == null){
      return null;
    }
    
    // Listage des bases de données
    try {
      statement = connection.getConnection().createStatement();
      resultSet = statement.executeQuery("SELECT datname FROM pg_database;");
      
      Common.logger.log(Level.FINE, "Databases from server '"+server.getName()+"': ");
      while (resultSet.next()) {
        database = new PGSQLDatabase(resultSet.getString(1), server);
        databases.add(database);
      }
      
      resultSet.close();
      statement.close();
      
    } catch (SQLException ex)  {
      throw connection.processSQLException(
             ex, connection.getDbName(), connection.getDbUser(), 
             connection.getDbPassword(), connection.getDbServer(), 
             connection.getDbPort(), connection.getDbType(), "SHOW DATABASES;");    
    } finally{
      resultSet = null;
      statement = null;
    }

    // Listage des schemas pour chaque base de donnee.
    iter = databases.iterator();
    while(iter.hasNext()){
      database = iter.next();
      
      try {
        schemas = listSchemas(database);
        if (schemas != null){
          for(Schema schema : schemas){
            database.addSchema(schema);
          }
        }
      } catch (Exception e) {
        Common.logger.log(Level.SEVERE, "Cannot list schema for database '"+database.getName()+"', ignoring", e);
      }
      
      schemas  = null;
      database = null;
    }

    return databases;  
  }

  @Override
  public ArrayList<Table> listTables(Schema schema)
  throws JDBCConnectionException {
  
    ArrayList<Table> tables = new ArrayList<Table>();
    
    Statement statement     = null;
    ResultSet resultSet     = null;
    
    
    try {
      statement = connection.getConnection().createStatement();
      if (schema.getName() != null){
        resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = '"+schema.getName()+"';");
      } else {
        resultSet = statement.executeQuery("SHOW TABLES");
      }
      
      Common.logger.log(Level.FINE, "Tables from "+schema.getName()+": ");
      while (resultSet.next()) {
        // On ignore le repertoire lost+found present dans les systeme linux
        tables.add(new PGSQLTable(resultSet.getString(1), schema));
        Common.logger.log(Level.FINE, "  "+resultSet.getString(1));
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
    
    return tables; 
    
  }

  @Override
  public boolean reloadPrivileges() throws JDBCConnectionException {
    // TODO Auto-generated method stub
    return false;
  }

  
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
    
    ResultSet rs = null;
    
    if ((metaData == null) || (tableName == null)){
      return null;
    }
    
    try {
      rs = metaData.getColumns(databaseName, null, tableName, "*");
      
      
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

        Common.logger.log(Level.INFO, "getTableHeader(DatabaseMetaData, String) column name: "+name);
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
      
    } catch (SQLException e) {
      Common.logger.log(Level.SEVERE, "Unable to read table "+tableName+" metadata.", e);
    }
    
    return headers;
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
  public boolean deleteSchema(Schema schema) throws JDBCConnectionException {
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
  public ArrayList<Schema> listSchemas(Database database)
      throws JDBCConnectionException {
    ArrayList<Schema> resultat = new ArrayList<Schema>();
    
    Statement statement   = null;
    ResultSet resultSet   = null;
    
    Schema schema         = null;
    
    Iterator<Schema> iter   = null;
    
    ArrayList<Table> tables   = null;
    Iterator<Table> tableIter = null;
    Table table               = null;
    
    if (database == null){
      return null;
    }
    
    // Listage des schemas
    try {
      statement = connection.getConnection().createStatement();
      
      resultSet = statement.executeQuery("SELECT nspname FROM pg_namespace;");
      
      Common.logger.log(Level.FINE, "Schemas from database '"+database.getName()+"': ");
      while (resultSet.next()) {
        schema = new PGSQLSchema(resultSet.getString(1), database);
        resultat.add(schema);
      }
      
      resultSet.close();
      statement.close();

      iter = resultat.iterator();
      while(iter.hasNext()){
        schema = iter.next();
        
        tables = listTables(schema);

        if (tables != null){
          tableIter = tables.iterator();
          while(tableIter.hasNext()){
            table = tableIter.next();
            schema.addTable(table);
            table = null;
          }
          tableIter = null;
        }
        schema = null;
        tables = null;
      }
      
    } catch (SQLException ex)  {
      throw connection.processSQLException(
             ex, connection.getDbName(), connection.getDbUser(), 
             connection.getDbPassword(), connection.getDbServer(), 
             connection.getDbPort(), connection.getDbType(), "SELECT * FROM pg_namespace;");    
    }

    resultSet = null;
    statement = null;
    
    return resultat;  
  }

  
  public static void main(String[] args){
    
    Common.logger.setLevel(Level.FINEST);
    Common.logger.getHandlers()[0].setLevel(Level.FINEST);
    
    JDBCConnection conn = new JDBCConnection("inf2", "seinturier", "zsxqsd20", "localhost", 5432, JDBCConnection.POSTGRESQL);
    
    PGSQLToolKit tk = new PGSQLToolKit(conn);
    
    try {
      conn.connect();
      tk.mapServer();
    } catch (JDBCConnectionException e) {
      Common.logger.log(Level.SEVERE, "PGSQL toolkit error...", e);
    }
  }
}
