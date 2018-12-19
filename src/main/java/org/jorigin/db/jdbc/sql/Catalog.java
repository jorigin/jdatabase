package org.jorigin.db.jdbc.sql;

import java.util.ArrayList;


/**
 * This class represents a SQL DDL catalog. Formaly, a catalog is a part of a 
 * {@link org.jorigin.db.jdbc.sql.Server database server} and a container of 
 * {@link org.jorigin.db.jdbc.sql.Schema database schemas}.
 * @author Julien Seinturier - (c) 2009 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.1.0
 */
public interface Catalog {

  /**
   * Get the name of the catalog.
   * @return the name of the catalog
   */
  public String getName();
  
  /**
   * Get the {@link org.jorigin.db.jdbc.sql.Schema schemas} contained by the catalog.
   * @return the schemas contained by the catalog.
   */
  public ArrayList<Schema> getSchemas();
  
  /**
   * Get the {@link org.jorigin.db.jdbc.sql.Database database} hosting the catalog.
   * @return the database containing the catalog.
   */
  public Database getDatabase();
  
  /**
   * Get the {@link org.jorigin.db.jdbc.sql.Server server} containing the catalog.
   * This method should return the result of <code>getDatabase().getServer()</code> or <code>null</code> 
   * if no information is available.
   * @return the server containing the catalog.
   */
  public Server getServer();
  
  /**
   * Add a {@link org.jorigin.db.jdbc.sql.Schema schema} to the catalog. The schema is added only if it is not already present
   * in the catalog.
   * @param schema the schema to add
   * @return true if the schema is added, false otherwise.
   */
  public boolean addSchema(Schema schema);
  
  
  /**
   * Remove the {@link org.jorigin.db.jdbc.sql.Schema schema} given in parameter from the catalog.
   * @param schema the schema to remove.
   * @return true if the schema is removed, false otherwise.
   */
  public boolean removeSchema(Schema schema);
}
