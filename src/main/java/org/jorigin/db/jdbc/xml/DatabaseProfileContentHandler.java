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
package org.jorigin.db.jdbc.xml;

import java.util.ArrayList;
import java.util.Properties;

import org.jorigin.db.jdbc.DatabaseProfile;
import org.jorigin.db.jdbc.JDBCConnection;
import org.jorigin.lang.PathUtil;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A Database profile XML content Handler
 * @author Julien Seinturier
 *
 */
public class DatabaseProfileContentHandler implements ContentHandler {


  public static final String NAME_ATT        = "name";
  
  public static final String TYPE_ATT        = "type";
  
  public static final String PORT_ATT        = "port";
  
  public static final String USER_ATT        = "user";
  
  public static final String HOST_ATT        = "host";
  
  public static final String PASSWORD_ATT    = "password";
  
  public static final String PROFILE_LIST_EL = "profiles";
  
  public static final String PROFILE_EL      = "profile";
  
  public static final String SERVER_EL       = "server";
  
  public static final String DATABASE_EL     = "database";
  
  public static final String MYSQL_TYPE      = "MYSQL";
  
  public static final String PGSQL_TYPE      = "PGSQL";
  
  public static final String ODBC_TYPE       = "ODBC";
  
  public static final String ODBC_ACCESS_TYPE= "ODBC_ACCESS";

  
  DatabaseProfile profile                    = null;
  
  ArrayList<DatabaseProfile> profiles        = null;
  
  String name                                = null;
  String dbServer                            = null;
  String dbName                              = null;
  String dbUser                              = null;
  String dbUserPassword                      = null;
  int dbServerPort                           = -1;
  int dbType                                 = -1;
  
  String typeName                            = null;
  
  Properties properties                      = null;
  
  public void characters(char[] ch, int start, int length) throws SAXException {

  }


  public void endDocument() throws SAXException {
  }


  public void endElement(String uri, String localName, String qName) throws SAXException {
    
    if (localName.equals(PROFILE_LIST_EL)){
    
    } else if (localName.equals(PROFILE_EL)){
      
      profiles.add(new DatabaseProfile(name, dbServer, dbName, dbUser, dbUserPassword, dbServerPort, dbType));
      

    } else if (localName.equals(SERVER_EL)){
    
    } else if (localName.equals(DATABASE_EL)){
      
    }
    
  }


  public void endPrefixMapping(String prefix) throws SAXException {
    
  }


  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    
  }

  public void processingInstruction(String target, String data) throws SAXException {
    
  }


  public void setDocumentLocator(Locator locator) {
    
  }


  public void skippedEntity(String name) throws SAXException {
    
  }


  public void startDocument() throws SAXException {
    
  }


  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    if (localName.equals(PROFILE_LIST_EL)){
      profiles = new ArrayList<DatabaseProfile>();
    
    } else if (localName.equals(PROFILE_EL)){
      
      properties = attributesToProperties(atts);
      
      name = (String) properties.get(NAME_ATT);
      
    } else if (localName.equals(SERVER_EL)){
      
      properties = attributesToProperties(atts);
      
      try {
        typeName = ((String)properties.get(TYPE_ATT)).toUpperCase();
        
        if (typeName.equals(MYSQL_TYPE)){
          dbType = JDBCConnection.MYSQL;
        } else if (typeName.equals(PGSQL_TYPE)){
          dbType = JDBCConnection.POSTGRESQL;
        } else if (typeName.equals(ODBC_TYPE)){
          dbType = JDBCConnection.ODBC;
        } else if (typeName.equals(ODBC_ACCESS_TYPE)){
          dbType = JDBCConnection.ODBC_ACCESS;
        } else {
          dbType = -1;
        }
        
      } catch (NumberFormatException ex) {
        dbType = -1;
      }
      
      try {
        dbServerPort = Integer.parseInt((String)properties.get(PORT_ATT));
      } catch (NumberFormatException ex) {
        dbServerPort = -1;
      }
      
      dbServer = (String) properties.get(HOST_ATT);
      
      properties = null;
      
      
    } else if (localName.equals(DATABASE_EL)){
      
      properties      = attributesToProperties(atts);
      
      if (dbType == JDBCConnection.ODBC_ACCESS){
        dbName = PathUtil.URIToPath((String) properties.get(NAME_ATT));
      } else {
        dbName          = (String) properties.get(NAME_ATT);
      }
      
      dbUser          = (String) properties.get(USER_ATT);
      dbUserPassword  = (String) properties.get(PASSWORD_ATT);
      
    }
    
  }

  public void startPrefixMapping(String prefix, String uri) throws SAXException {
    
  }
  
  /**
   * Convert XML attributes set to Java Properties set
   * @param attributes Attributes the set of XML attributes to convert
   * @return Properties the set of properties
   */
  public Properties attributesToProperties(Attributes attributes){
    // Récupération du nom et de la valeur de l'attribut
    String attributeName  = null;
    String attributeValue = null;

    // ensemble de propriétés correspondantes aux attributs lu dans le fichier XML
    Properties properties = new Properties();

    // Initialisation des propriétés
    for(int i = 0; i<attributes.getLength(); i++){

      // Récupération du nom et de la valeur de l'attribut
      attributeName  = attributes.getLocalName(i);
      attributeValue = attributes.getValue(i).substring(attributes.getValue(i).indexOf("=") + 1, attributes.getValue(i).length());

      // Création d'une propriété ayant même nom et valeur que l'attribut
      properties.setProperty(attributeName, attributeValue);
    }

    return properties;
  }
  
  /**
   * Get the profiles readed from XML file.
   * @return the profile list as an array list.
   */
  public ArrayList<DatabaseProfile> getProfiles(){
    return profiles;
  }
}
