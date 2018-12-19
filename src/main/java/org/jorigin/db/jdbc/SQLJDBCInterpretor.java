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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.jorigin.Common;


public class SQLJDBCInterpretor {

  /**
   * The database connection used to interpret the queries.
   */
  private JDBCConnection database;
	
  
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC CONSTRUCTEUR                                                             CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  /**
   * Create a new SQL interpretor using a JDBC database connection.
   * @param the database connection to use.
   */
  public SQLJDBCInterpretor(JDBCConnection database){
    this.database = database;
  }
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC FIN CONSTRUCTEUR                                                         CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC

  /**
   * Process a set of SQL queries
   * @param an array of SQL queries
   * @return true if all queries are executed, false otherwise.
   */
  public boolean process(String[] queries){
    boolean result = true;
    
	if (queries == null){
	  return false;
    }
	  
    for(int i = 0; i < queries.length; i++){
	  try {

        // Selection de la methode a employer pour lancer la requete
        if (queries[i].toUpperCase().startsWith("SELECT")){
          database.executeQuery(queries[i]);  
        } else {
          database.executeUpdate(queries[i]);
        }
        
        Common.logger.log(Level.INFO, ""+queries[i]+" [OK]");
		result = result & true;
	  } catch (JDBCConnectionException e) {
	    if (e.isWarning()){
	      Common.logger.log(Level.WARNING, ""+queries[i]+"", e);
	      result = true;
	    } else {
	      Common.logger.log(Level.SEVERE, ""+queries[i]+"", e);
		  result = false;
	    }
	  }
	}
	      
	return result;
      
  }
  
  /**
   * Process a set of SQL queries. Queries MUST end with a semicolon ";".
   * @param the String composed by semicolon separated SQL queries.
   * @return a boolean specifying if all the queries has been successfully executed.
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
        Common.logger.log(Level.INFO, ""+queries[i]+" [OK]");
		result = result & true;
      } catch (JDBCConnectionException e) {
        if (e.isWarning()){ 
          Common.logger.log(Level.WARNING, ""+queries[i]);
          result = true;
        } else {
          Common.logger.log(Level.SEVERE, ""+queries[i]);
          result = false;
        }
      }
      
    }
    
    return result;

  }
  
  /**
   * Process a set of SQL queries stored in a file.
   * @param file the file containing queries to process
   * @return a boolean specifying if all the queries has been successfully executed.
   */
  public boolean process(File file){
    boolean result = true;
    
    ArrayList<String> queries = null;
    
    StringBuffer query = null;
    
    
    if (!file.exists()){
      System.out.println("[ERROR] File "+file.getPath()+ "does not exists");
      return false;
    }
    
    try {
        FileReader reader = new FileReader(file);
        int lu;
        
        queries = new ArrayList<String>();
        query = new StringBuffer();
        
        do {
          // Lecture d'un caract�re sous forme d'entier
          lu = reader.read(); 
          
          // Si nous lisons la fin de fichier comme premier caract�re
          // (fichier vide) alors ne rien faire.
          if (lu != -1) {   
        	  
        	// On ignore les retour � la ligne
        	if (((char) lu) != '\n'){  
        	  
        	  // Construction de la requ�te caract�re par caract�re
              query.append((char) lu);
            
              // Si la fin de ligne est rencontr�e, on stocke la requete
              // et on en cr�e une nouvelle
              if (((char)lu) == ';'){
                queries.add(query.toString());
                query = new StringBuffer();
              }
        	}
          }
        } while (lu != -1); // Arr�t � la fin du fichier
        
        // Fermeture du fichier.
        reader.close();
        
        // Execution des requetes
        this.process(queries.toArray(new String[5]));
           
    } catch (FileNotFoundException e) {
        System.out.println("[ERROR] Cannot access"+file.getPath());
    } catch (IOException e) {
    	System.out.println("[ERROR] Cannot read"+file.getPath());
    }
    
    query = null;
    
    return result;
  }
}
