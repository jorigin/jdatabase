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
package org.jdatabase.jdbc;

import static org.jdatabase.JDatabase.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.jdatabase.JDatabase;
import org.jdatabase.jdbc.sql.Database;

/**
 * An <a href="https://www.iso.org/standard/63555.html">SQL</a> query processor based on underlying {@link JDBCConnection JDBC database connection}.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 */
public class SQLJDBCProcessor {

  /**
   * The database connection used to interpret the queries.
   */
  private JDBCConnection database;
  
  /**
   * Create a new SQL processor using a {@link JDBCConnection JDBC database connection}.
   * @param database the database connection to use.
   */
  public SQLJDBCProcessor(JDBCConnection database){
    this.database = database;
  }

  /**
   * Process a set of SQL queries.
   * @param queries an array of SQL queries.
   * @return <code>true</code> if all queries are executed, <code>false</code> otherwise.
   */
  public boolean process(String[] queries){
    boolean result = true;
    
	if (queries == null){
	  return false;
    }
	  
    for(int i = 0; i < queries.length; i++){
	  try {

        if (queries[i].toUpperCase().startsWith("SELECT")){
          database.executeQuery(queries[i]);  
        } else {
          database.executeUpdate(queries[i]);
        }
        
        logger.log(Level.INFO, ""+queries[i]+" [OK]");
		result = result & true;
	  } catch (JDBCConnectionException e) {
	    if (e.isWarning()){
	      logger.log(Level.WARNING, ""+queries[i]+"", e);
	      result = true;
	    } else {
	      logger.log(Level.SEVERE, ""+queries[i]+"", e);
		  result = false;
	    }
	  }
	}
	      
	return result;
      
  }
  
  /**
   * Process a set of SQL queries. Queries MUST end with a semicolon ";".
   * @param sqlQueries a {@link String string} composed by semicolon separated SQL queries.
   * @return <code>true</code> if all the queries has been successfully executed and <code>false</code> otherwise.
   */
  public boolean process(String sqlQueries){
	String[] queries = null;   
	  
	boolean result = true;
	
    queries = sqlQueries.split(";");
    
    if (queries == null){
      return false;
    }
      
    for(int i = 0; i < queries.length; i++){
     
      try {
		database.executeQuery(queries[i]);
        logger.log(Level.INFO, ""+queries[i]+" [OK]");
		result = result & true;
      } catch (JDBCConnectionException e) {
        if (e.isWarning()){ 
          logger.log(Level.WARNING, ""+queries[i]);
          result = true;
        } else {
          logger.log(Level.SEVERE, ""+queries[i]);
          result = false;
        }
      }
      
    }
    
    return result;

  }
  
  /**
   * Process a set of SQL queries stored in a file. The file has to store one request per line.
   * @param file the file containing queries to process
   * @return <code>true</code> if all the queries has been successfully executed and <code>false</code> otherwise.
   */
  public boolean process(File file){
    boolean result = true;
    
    ArrayList<String> queries = null;
    
    StringBuffer query = null;
    
    
    if (!file.exists()){
      logger.log(Level.SEVERE, "File "+file.getPath()+ "does not exists");
      return false;
    }
    
    try {
        FileReader reader = new FileReader(file);
        int lu;
        
        queries = new ArrayList<String>();
        query = new StringBuffer();
        
        do {
          lu = reader.read(); 

          if (lu != -1) {   

        	if (((char) lu) != '\n'){  

              query.append((char) lu);
            
              if (((char)lu) == ';'){
                queries.add(query.toString());
                query = new StringBuffer();
              }
        	}
          }
        } while (lu != -1); 

        reader.close();
        
        this.process(queries.toArray(new String[queries.size()]));
           
    } catch (FileNotFoundException e) {
    	logger.log(Level.SEVERE, "Cannot access"+file.getPath()+": "+e.getMessage(), e);
    } catch (IOException e) {
    	logger.log(Level.SEVERE, "Cannot read"+file.getPath()+": "+e.getMessage(), e);
    }
    
    query = null;
    
    return result;
  }
}
