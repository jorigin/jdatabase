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

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.event.EventListenerList;

import org.jorigin.Common;
import org.jorigin.db.jdbc.DatabaseProfile;
import org.jorigin.lang.PathUtil;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;



/**
 * An XML database profile reader.
 * @author Julien Seinturier
 */
public class DatabaseProfileReader {
//Liste des écouteurs informés des evenements du panneau
  protected EventListenerList idListenerList = new EventListenerList();
  
  /**
   * Read a database profile list list from the XML file given in parameter.
   * @param path the path of the XML file.
   */
  public ArrayList<DatabaseProfile> read(String path){
    DatabaseProfileContentHandler contentHandler = null;
    ErrorHandler errorHandler = new DefaultHandler();
    XMLReader parser = null;
    XMLFilter filter = new XMLFilterImpl();
    
    String fileName = null;
    
    if (path == null){
      return null;
    } else {
      fileName = PathUtil.URIToPath(path);
    }
    
    try {
      parser = XMLReaderFactory.createXMLReader();
      filter = new XMLFilterImpl(parser);
      contentHandler = new DatabaseProfileContentHandler();
      
      //  Enregistrement du gestionnaire de contenu auprès du parseur
      filter.setContentHandler(contentHandler);

      // Enregistrement du gestionnaire d'erreur auprès du parseur
      filter.setErrorHandler(errorHandler);
    } catch (SAXException e) {
      Common.logger.log(Level.SEVERE, "Cannot instanciate XML reader", e);
      return null;
    }
    
    // Parcour le document
    try {
      filter.parse(fileName);
    } catch (SAXException ex) {
      Common.logger.log(Level.SEVERE, "Sax exception while reading "+fileName, ex);
    } catch (IOException ex) {
      Common.logger.log(Level.SEVERE, "I/O exception while reading "+fileName, ex);
    }
    
    return contentHandler.getProfiles();
  }

}
