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



public class DatabaseProfile{
  
  private String dbServer       = null;
  private String dbName         = null;
  private String dbUser         = null;
  private String dbUserPassword = null;
  private int dbServerPort      = 3306;
  private int dbType            = JDBCConnection.MYSQL;
  
  private String name           = null;
  private String description    = null;
  
  //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
  //RR REDEFINITION                                           RR
  //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
  @Override
  public String toString(){
    String str = "";
    
    str += dbUser+":"+dbUserPassword+"@"+dbServer+":"+dbServerPort+"/"+dbName;
    
    return str;
  }
  //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
  //RR FIN REDEFINITION                                       RR
  //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
  
  /**
   * Create a new database profile. A database profile store connection informations about the database.
   * @param dbServer the DNS name (or IP) of the database server
   * @param dbName the name of the database on the server
   * @param dbUser the user attached to the profile and used as login on the database server
   * @param dbUserPassword the password of the user
   * @param dbServerPort the port of the database server
   * @param dbType the type of the database server (can be ODBC, MySQL, ..) specified by the class JDBCDatabaseAccess
   * @see #JDBCConnection.
   */
  public DatabaseProfile(String dbServer, String dbName, String dbUser, 
                         String dbUserPassword, int dbServerPort, int dbType){
    this.dbServer       = dbServer;
    this.dbName         = dbName;
    this.dbUser         = dbUser;
    this.dbUserPassword = dbUserPassword;
    this.dbServerPort   = dbServerPort;
    this.dbType         = dbType;
  }
  
  /**
   * Create a new database profile. A database profile store connection informations about the database.
   * @param name the name of the profile.
   * @param dbServer the DNS name (or IP) of the database server
   * @param dbName the name of the database on the server
   * @param dbUser the user attached to the profile and used as login on the database server
   * @param dbUserPassword the password of the user
   * @param dbServerPort the port of the database server
   * @param dbType the type of the database server (can be ODBC, MySQL, ..) specified by the class JDBCDatabaseAccess
   * @see #JDBCConnection.
   */
  public DatabaseProfile(String name, String dbServer, String dbName, String dbUser, 
    String dbUserPassword, int dbServerPort, int dbType){
    this.dbServer       = dbServer;
    this.dbName         = dbName;
    this.dbUser         = dbUser;
    this.dbUserPassword = dbUserPassword;
    this.dbServerPort   = dbServerPort;
    this.dbType         = dbType;
    
    this.name           = name;
}
  
  /**
   * Set the server dns (or IP) of the database server.
   * @param the database server dns name (or IP)
   */
  public void setDbServer(String dbServer){
    this.dbServer = dbServer;
  }
  
  /**
   * Get the server dns (or IP) of the database server.
   * @return the database server dns name (or IP)
   */
  public String getDbServer(){
    return this.dbServer;
  }
  
  /**
   * Set the database name on the server
   * @param the database name
   */
  public void setDbName(String dbName){
    this.dbName = dbName;
  }
  
  /**
   * Get the database name on the server
   * @return the database name
   */
  public String getDbName(){
    return this.dbName;
  }
  
  
  
  /**
   * Set the user attached to the profile
   * @param the user name
   */
  public void setDbUser(String dbUser){
    this.dbUser = dbUser;
  }
 
  
  /**
   * Get the user attached to the profile
   * @return the user name
   */
  public String getDbUser(){
    return this.dbUser;
  }
  
  /**
   * Set the user password.
   * @param the user password.
   */
  public void setDbUserPassword(String dbUserPassword){
    this.dbUserPassword = dbUserPassword;
  }
  
  /**
   * Get the user password.
   * @return the user password.
   */
  public String getDbUserPassword(){
    return this.dbUserPassword;
  }
   
  /**
   * Set the database server port used.
   * @param the database server port.
   */
  public void setDbServerPort(int dbServerPort){
    this.dbServerPort = dbServerPort;
  }

  /**
   * Get the database server port used.
   * @return the database server port.
   */
  public int getDbServerPort(){
    return this.dbServerPort;
  }
  
  /**
   * Set the database server type. The possible types are given in the
   * JDBCDatabaseAccess class
   * @param the int representing the database server type.
   * @see #JDBCDatabaseAccess
   */
  public void setDbType(int dbType){
    this.dbType = dbType;
  }

  
  /**
   * Get the database server type. The possible types are given in the
   * JDBCDatabaseAccess class
   * @return the int representing the database server type
   * @see #JDBCDatabaseAccess
   */
  public int getDbType(){
    return this.dbType;
  }
  
  /**
   * Set the name of this profile.
   * @param name the name of the profile.
   */
  public void setName(String name){
    this.name = name;
  }
  
  /**
   * Get the name of the profile.
   * @return the name of the profile.
   */
  public String getName(){
    return this.name;
  }
  
  /**
   * Set the description of this profile.
   * @param description the description of the profile.
   */
  public void setDescription(String description){
    this.description = description;
  }
  
  /**
   * Get the description of the profile.
   * @return the description of the profile.
   */
  public String getDescription(){
    return this.description;
  }
  
}