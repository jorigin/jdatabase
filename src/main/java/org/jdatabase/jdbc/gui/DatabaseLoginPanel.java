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
package org.jdatabase.jdbc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdatabase.JDatabase;
import org.jdatabase.jdbc.DatabaseProfile;
import org.jdatabase.jdbc.JDBCConnection;


/**
 * This class is a standard panel used to open connection to a database.<br/>
 * This panel is a form composed by following fields:
 * <ul>
 * <li> The user field</li>
 * <li> The password field</li>
 * <li> The server field</li>
 * <li> The database field</li>
 * </ul>
 * The user field specify the user that connect to the database server. The password field authenticate the user.
 * The server field enable to reach the database server location. The database name is the identifier of the database
 * in the server.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 *
 */
public class DatabaseLoginPanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * The connect action command identifier.
   */
  public static final String CONNECT_COMMAND = "connectCommand";
  
  /**
   * The horizontal orientation flag
   */
  public static final int HORIZONTAL = 1;
  
  /**
   * The vertical orientation flag (default)
   */
  public static final int VERTICAL = 2;
  
  /**
   * The user field label
   */
  protected JLabel userLabel         = null;
  
  /**
   * The user field
   */
  protected JTextField userTextField = null;
  
  /**
   * The user field panel
   */
  protected JPanel userPanel = null;
  
  /**
   * The password field label
   */
  protected JLabel passwordLabel     = null;
  
  /**
   * The password field
   */
  protected JPasswordField passwordField = null;
  
  /**
   * The password field panel
   */
  protected JPanel passwordPanel = null;
  
  /**
   * The server field label
   */
  protected JLabel serverLabel   = null;
  
  /**
   * The server text field
   */
  protected JTextField serverTextField = null;
  
  /**
   * The server field panel
   */
  protected JPanel serverPanel = null;
  
  /**
   * The database field label 
   */
  protected JLabel databaseLabel = null;
  
  /**
   * The database text field.
   */
  protected JTextField databaseTextField = null;
  
  /**
   * The database field panel.
   */
  protected JPanel databasePanel = null;
  
  
  
  /**
   * The server port field label 
   */
  protected JLabel portLabel = null;
  
  /**
   * The port text field.
   */
  protected JTextField portTextField = null;
  
  
  
  /**
   * The port field panel.
   */
  protected JPanel portPanel = null;
  
  
  /**
   * The server type label label 
   */
  protected JLabel typeLabel = null;
  
  /**
   * The type combo box.
   */
  protected JComboBox typeComboBox = null;
  
  /**
   * The type field panel.
   */
  protected JPanel typePanel = null;
  
 
  
  /**
   * The button that launch database connection.
   */
  protected JButton connectButton = null;
  
  /**
   * The default and original width of the field components.
   */
  private int fieldWidth  = 180;
  
  /**
   * The default and original height of the field components.
   */
  private int fieldHeight = 20;
  
  /**
   * The default and original width of the label components.
   */
  private int labelWidth  = 80;
  
  /**
   * The default and original height of the label components.
   */
  private int labelHeight = 20;
  
  /**
   * The default and original width of the connecte button.
   */
  private int connectButtonWidth = 120;
  
  /**
   * The default and original height of the connecte button.
   */
  private int connectButtonHeight = 20;
  
  /**
   * The horizontal gap between components.
   */
  private int hgap = 10;
  
  /**
   * The vertical gat between comonents.
   */
  private int vgap = 10;
 
  /**
   * The layout orientation of the component.
   */
  private int orientation = VERTICAL;
  
  
  int dbType = 0;
  
  
  DatabaseProfile dbProfile = null;
  
  /**
   * The state of the connection to the database descripbed by the current profile.
   */
  private boolean connected = false;
  
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC CONSTRUCTEUR                                                             CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  /**
   * Construct a nex default database connection panel.
   */
  public DatabaseLoginPanel(){
    this(VERTICAL);
  }
  
  /**
   * Construct a new database connection panel with the layout orientation given in parameter.
   * @param orientation the orientation (VERTICAL or HORIZONTAL)
   */
  public DatabaseLoginPanel(int orientation){
    this(orientation, null);
  }
  
  /**
   * Construct a new login panel attached to a given database profile.
   * @param dbProfile the database profile to use and modify.
   */
  public DatabaseLoginPanel(DatabaseProfile dbProfile){
    this(VERTICAL, dbProfile);
  }
  
  /**
   * Construct a new login panel attached to a given database profile and with a given 
   * orientation.
   * @param orientation the orientation of the database login panel
   * @param dbProfile the database profile to use and modify.
   */
  public DatabaseLoginPanel(int orientation, DatabaseProfile dbProfile){
    super();
    if (dbProfile == null){
     this.dbProfile = new DatabaseProfile("server", "name", "user", "password", -1, -1); 
    } else {
      this.dbProfile = dbProfile;
    }
    this.orientation = orientation;
    initGUI();
  }
  
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC FIN CONSTRUCTEUR                                                         CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC

//INITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITIN
//IN INITIALIZATION                                                           IT
//INITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITIN

  /**
   * Init the Graphical User Interface components.
   */
  protected void initGUI(){
    
    Dimension dim = null;
    Dimension labelDim = new Dimension(this.labelWidth, this.labelHeight);
    GridBagConstraints c = null;
    
    Insets insets   = new Insets(3,0,0,0);
    
    userLabel         = new JLabel("User: ");
    userLabel.setSize(labelDim);
    userLabel.setPreferredSize(labelDim);
    userLabel.setMaximumSize(labelDim);
    
    userTextField     = new JTextField();
    userTextField.addInputMethodListener(new InputMethodListener (){

      public void caretPositionChanged(InputMethodEvent event) {
  
      }

      public void inputMethodTextChanged(InputMethodEvent event) {
        setDbUser(userTextField.getText());
      }});
    
    
    
    passwordLabel     = new JLabel("Password: ");
    passwordLabel.setSize(labelDim);
    passwordLabel.setPreferredSize(labelDim);
    passwordLabel.setMaximumSize(labelDim);
    
    passwordField     = new JPasswordField();
    passwordField.addInputMethodListener(new InputMethodListener (){

      public void caretPositionChanged(InputMethodEvent event) {

      }

      public void inputMethodTextChanged(InputMethodEvent event) {
        setDbPassword(new String(passwordField.getPassword()));
      }});

    
    
    serverLabel       = new JLabel("Server: ");
    serverLabel.setSize(labelDim);
    serverLabel.setPreferredSize(labelDim);
    serverLabel.setMaximumSize(labelDim);
    serverTextField   = new JTextField();
    serverTextField.addInputMethodListener(new InputMethodListener (){

      public void caretPositionChanged(InputMethodEvent event) {

      }

      public void inputMethodTextChanged(InputMethodEvent event) {
        setDbServer(serverTextField.getText());
      }});
    
    
    databaseLabel     = new JLabel("Database: ");
    databaseLabel.setSize(labelDim);
    databaseLabel.setPreferredSize(labelDim);
    databaseLabel.setMaximumSize(labelDim);
    databaseTextField = new JTextField();
    databaseTextField.addInputMethodListener(new InputMethodListener (){

      public void caretPositionChanged(InputMethodEvent event) {

      }

      public void inputMethodTextChanged(InputMethodEvent event) {
        setDbName(databaseTextField.getText());
      }});
    
    
    typeLabel         = new JLabel("Type : ");
    typeLabel.setSize(labelDim);
    typeLabel.setPreferredSize(labelDim);
    typeLabel.setMaximumSize(labelDim);
    

    typeComboBox = new JComboBox(JDBCConnection.TYPES);
    typeComboBox.setSelectedIndex(dbType);
    typeComboBox.addItemListener(new ItemListener(){
      public void itemStateChanged(ItemEvent e) {
        
        
        switch (e.getStateChange()){
        case ItemEvent.SELECTED:
          //System.out.println("DB Type value changed: "+typeComboBox.getSelectedIndex());
          dbType = typeComboBox.getSelectedIndex();
          setDbType(dbType);
          break;
        }
        
      }});
    
    
    portLabel         = new JLabel("Port: ");
    portLabel.setSize(labelDim);
    portLabel.setPreferredSize(labelDim);
    portLabel.setMaximumSize(labelDim);
    portTextField   = new JTextField();
    portTextField.addInputMethodListener(new InputMethodListener (){

      public void caretPositionChanged(InputMethodEvent event) {

      }

      public void inputMethodTextChanged(InputMethodEvent event) {
        //System.out.println("DB Port value changed: "+portTextField.getText());
        setDbPort(Integer.parseInt(portTextField.getText()));
      }});

    connectButton     = new JButton();
    connectButton = new JButton();
    connectButton.setPreferredSize(new Dimension(20,20));
    connectButton.setEnabled(true);
    connectButton.setToolTipText("Connect to the database");
    connectButton.setActionCommand(CONNECT_COMMAND);
    connectButton.setBorderPainted(false);
    connectButton.setFocusPainted(true);
    connectButton.setContentAreaFilled(true);
    connectButton.setIcon(new ImageIcon(getClass().getResource("resource/icon/db_connected.png")));
    connectButton.setText("Connect");
    connectButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        processAction(e);
      }
    });  
    
    // Ce panneau ayant une taille fixe, il n'est pas necessaire d'utiliser un layout
    this.setLayout(null);
  
    if (orientation == VERTICAL){
      // Chaque composant est placé manuellement.
      userPanel = new JPanel();
      userPanel.setLayout(new BorderLayout());
      userPanel.add(userLabel, BorderLayout.WEST);
      userPanel.add(userTextField, BorderLayout.CENTER);
      
      passwordPanel = new JPanel();
      passwordPanel.setLayout(new BorderLayout());
      passwordPanel.add(passwordLabel, BorderLayout.WEST);
      passwordPanel.add(passwordField, BorderLayout.CENTER);
      
      serverPanel = new JPanel();
      serverPanel.setLayout(new BorderLayout());
      serverPanel.add(serverLabel, BorderLayout.WEST);
      serverPanel.add(serverTextField, BorderLayout.CENTER);
      
      databasePanel = new JPanel();
      databasePanel.setLayout(new BorderLayout());
      databasePanel.add(databaseLabel, BorderLayout.WEST);
      databasePanel.add(databaseTextField, BorderLayout.CENTER);
      
      typePanel = new JPanel();
      typePanel.setLayout(new BorderLayout());
      typePanel.add(typeLabel, BorderLayout.WEST);
      typePanel.add(typeComboBox, BorderLayout.CENTER);
      
      portPanel = new JPanel();
      portPanel.setLayout(new BorderLayout());
      portPanel.add(portLabel, BorderLayout.WEST);
      portPanel.add(portTextField, BorderLayout.CENTER);
      
      setLayout(new GridBagLayout());
      
      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.fill      = GridBagConstraints.HORIZONTAL;
      c.insets    = insets;
      c.weightx   = 1.0;
      c.weighty   = 1.0;
      add(userPanel, c);
      
      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.fill      = GridBagConstraints.HORIZONTAL;
      c.insets    = insets;
      c.weightx   = 1.0;
      c.weighty   = 1.0;
      add(passwordPanel, c);

      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.fill      = GridBagConstraints.HORIZONTAL;
      c.insets    = insets;
      c.weightx   = 1.0;
      c.weighty   = 1.0;
      add(serverPanel, c);
      
      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.fill      = GridBagConstraints.HORIZONTAL;
      c.insets    = insets;
      c.weightx   = 1.0;
      c.weighty   = 1.0;
      add(databasePanel, c);
      
      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.fill      = GridBagConstraints.HORIZONTAL;
      c.insets    = insets;
      c.weightx   = 1.0;
      c.weighty   = 1.0;
      add(typePanel, c);
      
      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.fill      = GridBagConstraints.HORIZONTAL;
      c.insets    = insets;
      c.weightx   = 1.0;
      c.weighty   = 1.0;
      add(portPanel, c);
      

      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.fill      = GridBagConstraints.HORIZONTAL;
      c.insets    = insets;
      c.weightx   = 1.0;
      c.weighty   = 1.0;
      add(connectButton, c);
      
    } else {
      // Chaque composant est placé manuellement.
      userLabel.setBounds(this.hgap, this.vgap, this.labelWidth, this.labelHeight);
      userTextField.setBounds(this.hgap+this.labelWidth, this.vgap, this.fieldWidth, this.fieldHeight);
        
      passwordLabel.setBounds(this.hgap, 2*this.vgap + this.fieldHeight, this.labelWidth, this.labelHeight);
      passwordField.setBounds(this.hgap+this.labelWidth, 2*this.vgap + this.fieldHeight, this.fieldWidth, this.fieldHeight);
    
      serverLabel.setBounds(this.hgap, 3*this.vgap + 2*this.fieldHeight, this.labelWidth, this.labelHeight);
      serverTextField.setBounds(this.hgap+this.labelWidth, 3*this.vgap + 2*this.fieldHeight, this.fieldWidth, this.fieldHeight);
    
      databaseLabel.setBounds(this.hgap, 4*this.vgap + 3*this.fieldHeight, this.labelWidth, this.labelHeight);
      databaseTextField.setBounds(this.hgap+this.labelWidth, 4*this.vgap + 3*this.fieldHeight, this.fieldWidth, this.fieldHeight);
      
      connectButton.setBounds(this.hgap, 5*this.vgap + 4*this.fieldHeight, this.connectButtonWidth, this.connectButtonHeight);
    
      dim = new Dimension(3*this.hgap+this.labelWidth+this.fieldWidth, 
          5*this.vgap+5*this.fieldHeight);
      this.setSize(dim);
      this.setPreferredSize(dim);
      this.add(userLabel);
      this.add(userTextField);
      this.add(passwordLabel);
      this.add(passwordField);
      this.add(serverLabel);
      this.add(serverTextField);
      this.add(databaseLabel);
      this.add(databaseTextField);
      this.add(connectButton);
    }
    
    this.setBorder(BorderFactory.createTitledBorder("Connection"));
    
    
    
    dim = null;
  }
//INITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITIN
//IN FIN INITIALIZATION                                                       IT
//INITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITINITIN

  /**
   * This method is a redirection of the addActionListener method of the actionneable components of the panel.
   * @param l the acttion listener to attach.
   */
  public void addActionListener(ActionListener l){
    connectButton.addActionListener(l);  
  }
  
  /**
   * Remove the action listener given in parameter from this component.
   * @param l the action listener to remove.
   */
  public void removeActionListener(ActionListener l){
    connectButton.removeActionListener(l);  
  }
  
  /**
   * Process an action event. By default, this method does nothing.
   * @param e the action event.
   */
  protected void processAction(ActionEvent e){
    
  }
  
  /**
   * Get the user field value
   * @return a string representing the user field value
   */
  public String getDbUser(){
    return this.userTextField.getText();
  }
  
  /**
   * set the user field values
   * @param user the string representing the user field value.
   */
  public void setDbUser(String user){
    this.userTextField.setText(user);
    this.dbProfile.setDbUser(user);
  }
  

  /**
   * Get the password field value
   * @return a string representing the password field value
   */
  public String getDbPassword(){
    return this.dbProfile.getDbUserPassword();
  }
  
  /**
   * set the password field values
   * @param password the string representing the password field value.
   */
  public void setDbPassword(String password){
    this.passwordField.setText(password);
    this.dbProfile.setDbUserPassword(password);
  }

  
  /**
   * Get the server field value
   * @return a string representing the server field value
   */
  public String getDbServer(){
    return this.dbProfile.getDbServer();
  }
  
  /**
   * set the servver field values
   * @param server the string representing the server field value.
   */
  public void setDbServer(String server){
    this.serverTextField.setText(server);
    this.dbProfile.setDbServer(server);  
  }
  
  
  /**
   * Get the database field value
   * @return a string representing the database field value
   */
  public String getDbName(){
    return this.dbProfile.getDbName();
  }
  
  /**
   * set the database field values
   * @param database the string representing the database field value.
   */
  public void setDbName(String database){
    this.databaseTextField.setText(database);
    this.dbProfile.setDbName(database);
    
  }
  
  /**
   * Set the database server port
   * @param dbPort the port
   */
  public void setDbPort(int dbPort){
    this.portTextField.setText(""+dbPort);
    this.dbProfile.setDbServerPort(dbPort);
    
  }
  
  
  /**
   * Get the database server port
   * @return dbPort the port
   */
  public int getDbPort(){
    return this.dbProfile.getDbServerPort();
  }
  
  
  /**
   * Set the database server type
   * @param dbPort the type
   */
  public void setDbType(int dbType){
    this.dbProfile.setDbType(dbType);
    this.typeComboBox.setSelectedIndex(dbType);
    setDbPort(JDBCConnection.PORTS[dbType]);
  }
  
  
  /**
   * Get the database server type
   * @return dbPort the type
   */
  public int getDbType(){
    return this.dbProfile.getDbType();
  }

  /**
   * Set the database profile used by the login panel. The database
   * profile is modified when the field are modified.
   * @param dbProfile the database profile to use.
   */
  public void setDatabaseProfile(DatabaseProfile dbProfile){
    this.dbProfile = dbProfile;
    
    this.userTextField.setText(dbProfile.getDbUser());
    this.passwordField.setText(dbProfile.getDbUserPassword());
    this.databaseTextField.setText(dbProfile.getDbName());
    this.serverTextField.setText(dbProfile.getDbServer());
    this.portTextField.setText(""+dbProfile.getDbServerPort());
    this.typeComboBox.setSelectedIndex(dbProfile.getDbType());
  }
  
  /**
   * Get the database profile used with this login panel.
   * @return the database profile.
   */
  public DatabaseProfile getDatabaseProfile(){
    
   
    dbProfile.setDbUser(this.userTextField.getText()); 
    dbProfile.setDbUserPassword(new String(this.passwordField.getPassword()));
    dbProfile.setDbName(this.databaseTextField.getText());
    dbProfile.setDbServer(this.serverTextField.getText());
    
    try {
      dbProfile.setDbServerPort(Integer.parseInt(this.portTextField.getText()));
    } catch (NumberFormatException ex) {
     dbProfile.setDbServerPort(-1);
    }
    
    dbProfile.setDbType(this.typeComboBox.getSelectedIndex());
    return this.dbProfile;
  }
  
  /**
   * Set the state of the connection to the database described by the
   * currrent profile in the login panel.
   * @param connected the connection state.
   */
  public void setConnected(boolean connected){
    this.connected = connected;
    
    if (connected){
      connectButton.setIcon(new ImageIcon(getClass().getResource("resource/icon/db_not_connected.png")));
      connectButton.setText("Disconnect");
    } else {
      connectButton.setIcon(new ImageIcon(getClass().getResource("resource/icon/db_connected.png")));
      connectButton.setText("Connect");
    }
    
  }
  
  /**
   * Return if the connection actived in the login panel is connected
   * @return true if a connection exists, false otherwise
   */
  public boolean isConnected(){
    return this.connected;
  }
  
}

