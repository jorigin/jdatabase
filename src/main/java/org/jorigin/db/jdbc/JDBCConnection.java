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
package org.jorigin.db.jdbc;



import java.awt.event.AWTEventListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.event.EventListenerList;

/**
 * <p>Title: Arpenteur</p>
 *
 * <p>Description: an ARchitectural PhotogrammEtric Network Tool for EdUcation
 * and Research </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: MAP umr CNR3 694</p>
 *
 * @author malika nedir
 * @version 3.4.x
 */
public class JDBCConnection {


  /**
   * An unknown database server type flag
   */
  public static final int UNKNOWN    = 0;
  
  /**
   * The mysql database flag
   */
  public static final int MYSQL      = 1;

  /**
   * The PostGreSQL database flag
   */
  public static final int POSTGRESQL = 2;
  
  /**
   * The ODBC database flag
   */
  public static final int ODBC       = 3;
  
  /**
   * The ODBC source from an acess  file (.mdb)
   */
  public static final int ODBC_ACCESS = 4;
  
  /**
   * The default unknown port
   */
  public static final int UNKNOWN_STANDARD_PORT = -1;
  
  /**
   * The standard MySQL port
   */
  public static final int MYSQL_STANDARD_PORT = 3306;
  
  /**
   * The standard PostgreSQL port
   */
  public static final int POSTGRESQL_STANDARD_PORT = 5432;
  
  /**
   * The standard ODBC port.
   */
  public static final int ODBC_STANDARD_PORT       = -1;
  
  /**
   * The standard ODBC_ACCESS port.
   */
  public static final int ODBC_ACCESS_STANDARD_PORT       = -1;
  
  public static final String[] TYPES = {"Unknown", "MySQL", "PostGreSQL", "ODBC", "ODBC_ACCESS"};
  
  
  public static final int[] PORTS = {-1, 3306, 5432, 5555, -1};
  
  
  /**
   * Specify if an active connection to the database is open.
   */
  private boolean connect = false;
  
  
  private Connection connection = null;
  
  /**
   * The name of the database
   */
  private String dbName     = null;
  
  /**
   * The user to log on the database
   */
  private String dbUser     = null;
  
  /**
   * The password of the user to log on the database.
   */
  private String dbPassword = null;
  
  /**
   * The URL of the database server
   */
  private String dbServer   = null;
  
  /**
   * The port on which the database server is listening.
   */
  private int    dbPort     = 3306;
  
  /**
   * The database server type.
   */
  private int dbType        = MYSQL;
  
  Statement stmt            = null;
  
  /**
   * Listenners list
   */
  protected EventListenerList idListenerList = null;
  
  
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC CONSTRUCTEUR                                                             CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  
  /**
   * Create a default JDBC connection without any connexion information avauilable.
   */
  public JDBCConnection(){
    this(null, null, null, null, -1, -1);
  }
  
  /**
   * Create a new database connection. All database information are given in parameter.
   * @param dbName the name of the database
   * @param dbUser the user to log on the database server
   * @param dbPassword the user password
   * @param dbServer the database server URL
   * @param dbPort the port on which the database server is listening
   * @param dbType the databasetype (MYSQL, POSTGRESQL)
   */
  public JDBCConnection(String dbName, String dbUser, String dbPassword, String dbServer, int dbPort, int dbType) {
    this.dbName     = dbName;
    this.dbUser     = dbUser;
    this.dbPassword = dbPassword;
    this.dbServer   = dbServer;
    this.dbPort     = dbPort;
    this.dbType     = dbType;
    
    this.connect = false;
    
    idListenerList = new EventListenerList();
  }

  /**
   * Create a new database connection from the database profile given in parameter.
   * @param profile the database profile.
   */
  public JDBCConnection(DatabaseProfile profile){
	this(profile.getDbName(), profile.getDbUser(), 
		 profile.getDbUserPassword(), profile.getDbServer(),
		 profile.getDbServerPort(), profile.getDbType());
  }
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC FIN CONSTRUCTEUR                                                         CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC

//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
//EE EVENEMENT                                                                EE
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
  
  
  /**
   * Add a Listener to this object
   * @param l the Listener added to the object
   */
  public void addJDBCDatabaseAccessListener(AWTEventListener l) {
    idListenerList.add(AWTEventListener.class, l);
  }

  /**
   * Remove an Listener from this object
   * @param l the Listener to remove
   */
  public void removeJDBCDatabaseAccessListener(AWTEventListener l) {
    idListenerList.remove(AWTEventListener.class, l);
  }

  
 /**
  * Fire a new mesurable database event.
  * @param e the event.
  */
 protected void fireEvent(JDBConnectionEvent e) {
   Object[] listeners = idListenerList.getListenerList();
   for (int i = listeners.length - 2; i >= 0; i -= 2) {
     if (listeners[i] == AWTEventListener.class) {
       ( (AWTEventListener) listeners[i + 1]).eventDispatched(e);
     }
   }
 }
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
//FIN EVENEMENT                                                               EE
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
  
//SGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSG
//BD GESTION BD                                                               SG
//SGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSG
  public void connect() 
         throws JDBCConnectionException{
	 

  String databaseURL = null; 
	 
  //  Deconnexion si une connexion existe deja
  if (connect){
    this.disconnect();
  }  
   
  fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_START));
	   
   // Initialisation du protocoloe dans l'url
   switch(dbType){
     case JDBCConnection.MYSQL:
	   databaseURL = "jdbc:mysql://";
     break;
     
     case JDBCConnection.POSTGRESQL:  
    	 databaseURL = "jdbc:postgresql://";
     break;
     
     case JDBCConnection.ODBC:
       databaseURL = "jdbc:odbc://";
     break;
     
     case JDBCConnection.ODBC_ACCESS:
       databaseURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
     break;
     
     default:
       fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
       throw new JDBCConnectionException("The given database type is incorrect", JDBCConnectionException.UNKNOWN_DATABASE_TYPE);
     }
	 
   
     // Integration du DNS (ou de l'ip) du serveur a� l'url
     if (dbServer != null){
       databaseURL += dbServer;   
     } else {
       
       if (dbType != ODBC_ACCESS){
         fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
         throw new JDBCConnectionException("No database server URL specified", JDBCConnectionException.BAD_SERVER);
       }
     }
	   
     // Prise en compte du port
     if (dbPort > 0){
       databaseURL += ":" + dbPort + "/";   
     } else {
       if (dbType != ODBC_ACCESS){
         fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
         throw new JDBCConnectionException("Bad server port: "+dbPort, JDBCConnectionException.BAD_SERVER_PORT);
       }
     }

     // Prise en compte du nom de base de donnees
     if (dbName != null){
       databaseURL += dbName + "";
     } else {
       fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
       throw new JDBCConnectionException("Bad database name: "+dbName, JDBCConnectionException.BAD_DATABASE_NAME);
     }

     // Connexion a� la base en utilisant le driver JDBC approprie
     switch(dbType){
     case JDBCConnection.MYSQL:
       try {
         Class.forName("org.gjt.mm.mysql.Driver").newInstance();
         //DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
       } catch (Exception ex) {
         connect = false;
         fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
         throw new JDBCConnectionException("Cannot instanciate driver: org.gjt.mm.mysql.Driver", 
             JDBCConnectionException.NO_SUITABLE_DRIVER, ex);
       }

       try {
         connection = DriverManager.getConnection(databaseURL, dbUser, dbPassword);
         stmt = connection.createStatement();
         connect = true;
       } catch (SQLException eSQL) {
         connect = false;
         throw processSQLException(eSQL, dbName, dbUser, dbPassword, dbServer, dbPort, dbType, null);


       } catch (Exception ex) {
         connect = false;
         fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
         throw new JDBCConnectionException("Unknown exception: "+ex.getMessage(), 0, ex);
       }
       break;

       // Connection � une base de donnees POSTGRE
     case JDBCConnection.POSTGRESQL:
       try {
         Class.forName("org.postgresql.Driver");
       } catch (Exception ex) {
         connect = false;
         fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
         throw new JDBCConnectionException("Cannot instanciate driver: org.gjt.mm.mysql.Driver", 
             JDBCConnectionException.NO_SUITABLE_DRIVER, ex);
       }

       try {
         connection = DriverManager.getConnection(databaseURL, dbUser, dbPassword);
         stmt = connection.createStatement();
         connect = true;
       } catch (SQLException eSQL) {
         connect = false;

         fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
         throw processSQLException(eSQL, dbName, dbUser, dbPassword, dbServer, dbPort, dbType, null);
       }

       break;

       // Connection � une base de donnees ODBC
     case JDBCConnection.ODBC:
       try {
         Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
       } catch (Exception ex) {
         connect = false;
         fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
         throw new JDBCConnectionException("Cannot instanciate driver: sun.jdbc.odbc.JdbcOdbcDriver", 
             JDBCConnectionException.NO_SUITABLE_DRIVER, ex);
       }

       try {
         connection = DriverManager.getConnection(databaseURL, dbUser, dbPassword);
         stmt = connection.createStatement();
         connect = true;
       } catch (SQLException eSQL) {
         connect = false;

         fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
         throw processSQLException(eSQL, dbName, dbUser, dbPassword, dbServer, dbPort, dbType, null);
       }

       break;

       // Connection � une base de donnees ODBC
     case JDBCConnection.ODBC_ACCESS:
       try {
         Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
       } catch (Exception ex) {
         connect = false;
         fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
         throw new JDBCConnectionException("Cannot instanciate driver: sun.jdbc.odbc.JdbcOdbcDriver", 
             JDBCConnectionException.NO_SUITABLE_DRIVER, ex);
       }

       try {
         connection = DriverManager.getConnection(databaseURL, dbUser, dbPassword);
         stmt = connection.createStatement();
         connect = true;
       } catch (SQLException eSQL) {
         connect = false;

         fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
         throw processSQLException(eSQL, dbName, dbUser, dbPassword, dbServer, dbPort, dbType, null);
       }

       break;

     default:
       connect = false;
     fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
     throw new JDBCConnectionException("Unknown database type", 
         JDBCConnectionException.UNKNOWN_DATABASE_TYPE, null);
     }


     fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_SUCCESS));
  }
 
  /**
   * Close connection with the database.
   * @throws JDBCConnectionException fired if a database disconnection error occurs.
   */
  public void disconnect()
         throws JDBCConnectionException{
	  
    try {
         if (stmt != null){
	  stmt.close();
         }
         if (connection != null){
	  connection.close();
         }
          connect = false;
	} catch (SQLException eSQL) {
          connect = false;
	  throw new JDBCConnectionException("Disconnection exception ", 0, eSQL);     
	}
  }
  
  /**
   * Send a query to the database server. After being processed, the result set has to be closed.
   * @param query the query to send.
   * @return the query result.
   * @throws JDBCConnectionException throwed if an error occurs during the query processing.
   */
  public ResultSet executeQuery(String query)
         throws JDBCConnectionException {
  
    ResultSet rset = null;  
	  
    try {
      rset = stmt.executeQuery(query);
    } catch (SQLException eSQL) {
      rset = null;
	  
      throw processSQLException(eSQL, dbName, dbUser, dbPassword, dbServer, dbPort, dbType, query);
      
    }
	
    return rset;
  }
  
  
  /**
   * Sent a no return SQL query to the server (INSERT, DELETE, UPDATE) 
   * @param query the query to send
   * @return the row count involved in the statemet or 0 if classic SQL query
   * @throws JDBCConnectionException if an error occurs.
   */
  public int executeUpdate(String query)
             throws JDBCConnectionException{
    int ret = 0;
    
    try {
      ret = this.stmt.executeUpdate(query);
    } catch (SQLException eSQL) {
      ret = 0;
          
      throw processSQLException(eSQL, dbName, dbUser, dbPassword, dbServer, dbPort, dbType, query);
    }
    
    return ret;
  }
  
  /**
   * Create a new prepared statement. This method create a compiled and optimized query
   * to use with parameters. 
   * @param query the query to compile.
   * @return the prepared statement
   * @throws JDBCConnectionException if an error occurs
   * @see #java.sql.Connection.prepareStatement(String)
   */
  public PreparedStatement prepareStatement(String query)
         throws JDBCConnectionException{
    
    PreparedStatement statement = null;
    
    try {
      statement = this.connection.prepareStatement(query);
    } catch (SQLException eSQL) {
        throw processSQLException(eSQL, dbName, dbUser, dbPassword,
                                  dbServer, dbPort, dbType, query);  

    }
    
    return statement;
    
  }
  
  
  /**
   * Delegates commit to the connection. This method Makes all changes made since the previous
   * commit/rollback permanent and releases any database locks
   * currently held by this <code>Connection</code> object. 
   * This method should be
   * used only when auto-commit mode has been disabled.
   *
   * @exception JDBCConnectionException if a database access error occurs or this
   *            <code>Connection</code> object is in auto-commit mode
   * @see #setAutoCommit 
   * @see java.sql.Connection
   */
  public void commit() 
       throws JDBCConnectionException{
    try {
      this.connection.commit();
    } catch (SQLException ex) {
      throw processSQLException(ex, dbName, dbUser, dbPassword,
          dbServer, dbPort, dbType, null);  
    }
  }

  /**
   * Delegates rollback to the connection. This method undoes all changes made in the current transaction
   * and releases any database locks currently held
   * by this <code>Connection</code> object. This method should be 
   * used only when auto-commit mode has been disabled.
   *
   * @exception JDBCConnectionException if a database access error occurs or this
   *            <code>Connection</code> object is in auto-commit mode
   * @see #setAutoCommit 
   * @see java.sql.Connection
   */
  public void rollback() 
       throws JDBCConnectionException{
    try {
      this.connection.rollback();
    } catch (SQLException eSQL) {
      throw processSQLException(eSQL, dbName, dbUser, dbPassword,
          dbServer, dbPort, dbType, null);  
    }
  }

  
//SGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSG
//BD FIN GESTION BD                                                           SG
//SGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSGBDSG
 

//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA ACCESSEURS                                                               AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
 /**
  * Return the state of connection for the database access. True is returned if a
  * connection is alive, false otherwise.
  * @return true if a connection exists and work, false otherwise.
  */
 public boolean isConnected(){
   return this.connect;
 }
 
 /**
  * Set the database name. If a connection is active, the connection is restarted.
  * @param dbName the new database name.
  */
 public void setDbName(String dbName){
   this.dbName = dbName;
   
   if (isConnected()){
     try {
       this.disconnect();
	   this.connect();
     } catch (JDBCConnectionException e) {
	  System.out.println(e.getMessage());
	  e.printStackTrace();
     }
   }
 }
 
 /**
  * Get the database name.
  * @return the database name.
  */
 public String getDbName(){
   return this.dbName;
 }
 
 /**
  * Set the user to log on the database. If a connection is active, 
  * the connection is restarted.
  * @param the user to log on the database.
  */
 public void setDbUser(String dbUser){
   this.dbUser = dbUser;
   
   if (isConnected()){
     try {
	   this.disconnect();
	   this.connect();
	 } catch (JDBCConnectionException e) {
	   System.out.println(e.getMessage());
	   e.printStackTrace();
	 }
   }
   
 }
 
 /**
  * Get the user to log on the database. 
  * @return the user to log on the database.
  */
 public String getDbUser(){
   return this.dbUser;
 }
 
 
 
 /**
  * Set the password of the user to log on the database.
  * If a connection is active, the connection is restarted.
  *  @param the password of the user to log on the database.
  */
 public void setDbPassword(String dbPassword){
   this.dbPassword =  dbPassword;
   
   if (isConnected()){
     try {
	   this.disconnect();
	   this.connect();
     } catch (JDBCConnectionException e) {
	   System.out.println(e.getMessage());
	   e.printStackTrace();
     }
   }
 }
 
 /**
  * Get the password of the user to log on the database.
  * @return the password of the user to log on the database.
  */
 public String getDbPassword(){
   return this.dbPassword;
 }
 
 /**
  * Set the URL of the database server. 
  * If a connection is active, the connection is restarted.
  * @param String the URL of the database server. 
  */
 public void setDbServer(String dbServer){
   this.dbServer = dbServer;
   
   if (isConnected()){
     try {
	   this.disconnect();
	   this.connect();
     } catch (JDBCConnectionException e) {
	   System.out.println(e.getMessage());
	   e.printStackTrace();
     }
   }   
 }
 
 /**
  * Get the URL of the database server. 
  * @return the URL of the database server. 
  */
 public String getDbServer(){
   return this.dbServer;
 }
 
 /**
  * Set the port on which the database server is listening.
  * If a connection is active, the connection is restarted.
  * @param The port on which the database server is listening.
  */
 public void setDbPort(int dbPort){
   this.dbPort = dbPort;
   
   if (isConnected()){
     try {
       this.disconnect();
	   this.connect();
	 } catch (JDBCConnectionException e) {
	   System.out.println(e.getMessage());
	   e.printStackTrace();
	 }
   }   
 }
 
 
 /**
  * Get the port on which the database server is listening.
  * @return the port on which the database server is listening.
  */
 public int getDbPort(){
   return this.dbPort;
 }
 
 
 /**
  * Set the database server type. 
  * If a connection is active, the connection is restarted.
  * @param the database server type. 
  */
 public void setDbType(int dbType){
   this.dbType = dbType;
   
   if (isConnected()){
     try {
       this.disconnect();
	   this.connect();
	 } catch (JDBCConnectionException e) {
	   System.out.println(e.getMessage());
	   e.printStackTrace();
	 }
   }   
 }
 
 /**
  * Get the database server type. 
  * @return the database server type. 
  */
 public int getDbType(){
   return this.dbType;
 }
 
 /**
  * Set the connection information from a database profile
  * @param profile the database profile.
  */
 public void setDatabaseProfile(DatabaseProfile profile){
   this.dbName     = profile.getDbName();
   this.dbUser     = profile.getDbUser(); 
   this.dbPassword = profile.getDbUserPassword();
   this.dbServer   = profile.getDbServer();
   this.dbPort     = profile.getDbServerPort();
   this.dbType     = profile.getDbType(); 
   
   if (isConnected()){
     try {
       this.disconnect();
       this.connect();
     } catch (JDBCConnectionException e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
     }
   }   
   
 }
 
 /**
  * Return the connection used by the JDBCConnection.
  * @return the database connection.
  */
 public Connection getConnection(){
   return this.connection;
 }
 
 /**
  * Get the name of the connection
  * @return the name of the connection
  */
 public String getName(){
   
   String str    = getDbUser()+"@";
   
   if (getDbServer() ==  null){
     str += "localhost";
   } else {
     str += getDbServer();
   }
   
   if (getDbPort() > 0){
     str += ":"+getDbPort();
   }
   
   str += "/"+getDbName();
   
   return str; 
 }
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA FIN ACCESSEURS                                                           AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

 /**
  * Process an SQL exception and create a generic JDBCDatabase exception.
  * Thismethod delegates to specific driver exception process.
  * @param ex the source SQL Exception
  * @param dbName the name of the database
  * @param dbUser the user to log on the database server
  * @param dbPassword the user password
  * @param dbServer the database server URL
  * @param dbPort the port on which the database server is listening
  * @param dbType the databasetype (MYSQL, POSTGRESQL)
  * @param query the sql query
  */
 public JDBCConnectionException processSQLException(SQLException ex, String dbName, 
                                                    String dbUser, String dbPassword,
                                                    String dbServer, int dbPort,
                                                    int dbType, String query){
	 
  
   
   
   JDBCConnectionException exception = null;
   
   Throwable source = null;
   
   
   // Excepions PostGre --------------------------------------------------
   
   // SQL Warnings
   if (ex.getSQLState().startsWith("01")){
     exception = new JDBCConnectionException("SQL Warning ", 
                                       JDBCConnectionException.WARNING_UNDEFINED, ex);
   
   // No Data (this is also SQL warnings)
   } else if (ex.getSQLState().startsWith("02")){
         exception = new JDBCConnectionException("Request produce no result (normal with a INSERT, UPDATE, DELETE request)", 
                                           JDBCConnectionException.WARNING_NO_RESULT, ex); 
   }
   
   // Statement not yet complete
   else if(ex.getSQLState().startsWith("03")){
         exception = new JDBCConnectionException(ex.getMessage(), 
                                           JDBCConnectionException.NATIVE_SERVER_EXCEPTION, ex); 
   }
   
   // Connection exception
   else if (ex.getSQLState().startsWith("08")){
     source = ex.getCause();
     
     // Serveur inateignable
     if (source instanceof java.net.UnknownHostException){
       exception = new JDBCConnectionException("Unknown server host: "+dbServer, 
                                             JDBCConnectionException.BAD_SERVER, ex);  
     } 
     
     else if (source instanceof java.net.ConnectException){
       exception = new JDBCConnectionException("Cannot conect to host "+dbServer+" on port "+dbPort, 
                                             JDBCConnectionException.BAD_SERVER_PORT, ex);               
     }
     
   }
   
   // Bad user
   else if (ex.getSQLState().startsWith("28")){
     exception = new JDBCConnectionException("Access denied for user "+dbUser+" and given password to database "+dbName, 
                                           JDBCConnectionException.DATABASE_ACCESS_DENIED, ex);      
   }
   
   // Database (catalog) does not exists
   else if (ex.getSQLState().startsWith("3D")){
     exception = new JDBCConnectionException("Database "+dbName+" does not exists", 
                                           JDBCConnectionException.BAD_DATABASE_NAME, ex);      
   }
   
   // Syntax error in query
   else if (ex.getSQLState().startsWith("42")){
     exception = new JDBCConnectionException("Syntax error in query "+query, 
                                           JDBCConnectionException.BAD_SQL_QUERY, ex);      
   }
   
   else {
/*
     exception = new JDBCConnectionException("Unhandled PostGRESQL: "+ex.getMessage()+" State: "+ex.getSQLState(), 
                                           JDBCConnectionException.NATIVE_SERVER_EXCEPTION, ex);        
*/ 
   }
   // ----------------------------------------------------------
   
   
   // Exception mySQL
   if (exception == null){
   exception = processMySQLException(ex, dbName,  dbUser, dbPassword, dbServer,dbPort,
                                     dbType, query);
   }
   // ----------------------------------------------------------
   
 
    return exception;
  }
 
 

 /**
  * Process a MySQL exception and create a generic JDBCDatabase exception.
  * The error code table is available at http://dev.mysql.com/doc/refman/5.0/en/error-messages-server.html
  * @param ex the source MySQL Exception
  * @param dbName the name of the database
  * @param dbUser the user to log on the database server
  * @param dbPassword the user password
  * @param dbServer the database server URL
  * @param dbPort the port on which the database server is listening
  * @param dbType the databasetype (MYSQL, POSTGRESQL)
  * @param query the sql query
  */
 protected JDBCConnectionException processMySQLException(SQLException ex, String dbName, 
                                                         String dbUser, String dbPassword,
                                                         String dbServer, int dbPort,
                                                         int dbType, String query){
   
   JDBCConnectionException exception = null;
   
   // Traduction de l'exception SQL en exception JDBC
   // Liste complete sur: http://dev.mysql.com/doc/refman/5.0/en/error-messages-server.html
   switch(ex.getErrorCode()){
      
     //  Fin de fichier inattendue
     case 1039:
       connect = false;
       fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
       exception = new JDBCConnectionException(ex.getMessage()+" "+dbName, 
                                              JDBCConnectionException.UNEXPECTED_EOF, ex);
    
     // Trop de connexions sur le serveur
     case 1040:
       connect = false;
       fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
       exception = new JDBCConnectionException("Too many connections on "+dbName, 
                                                JDBCConnectionException.TOO_MANY_CONNECTIONS, ex);
      
     // Hote serveur inconnu
     case 1042:
       connect = false;
       fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
       exception = new JDBCConnectionException("Unknown server host: "+dbServer, JDBCConnectionException.BAD_SERVER, ex);

          
     // Access a� la base de donnée refuse
     case 1044:
       connect = false;
       fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
       exception = new JDBCConnectionException("Access denied for user "+dbUser+" and given password to database "+dbName, 
                                                JDBCConnectionException.DATABASE_ACCESS_DENIED, ex);
     
     // Access refuse pour l'utilisateur / mot de passe
     case 1045:
       connect = false;
       fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
       exception = new JDBCConnectionException("Access denied for "+this.dbUser+" bad user name or password", 
                                                JDBCConnectionException.DATABASE_ACCESS_DENIED, ex);
         
     // Pas de base de donnees selectionnee
     case 1046:
       connect = false;
       fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
       exception = new JDBCConnectionException("Database "+dbName+" does not exists", 
                                                JDBCConnectionException.BAD_DATABASE_NAME, ex);
          
     // Base de donnees inconnue
     case 1049:
       connect = false;
       fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
       exception = new JDBCConnectionException("Unknown database: "+dbName, 
                                                JDBCConnectionException.BAD_DATABASE_NAME, ex);
       
     default:
       connect = false;
       fireEvent(new JDBConnectionEvent(this, JDBConnectionEvent.DATABASE_CONNECTION_FAIL));
       exception = new JDBCConnectionException("Unknown MySQL Exception (Code : "+ex.getErrorCode()+") "+dbUser+"@"+dbServer+"/"+dbName+" query: "+query, 
                                              JDBCConnectionException.NATIVE_SERVER_EXCEPTION, ex);
   }

   return exception;
   
 }
}
