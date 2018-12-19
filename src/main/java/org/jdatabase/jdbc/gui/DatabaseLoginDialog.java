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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.jdatabase.JDatabase;
import org.jdatabase.jdbc.DatabaseProfile;
import org.jorigin.lang.LangResourceBundle;


/**
 * 
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jdatabase">https://github.com/jorigin/jdatabase</a>
 * @version {@value JDatabase#version} build {@value JDatabase#BUILD}
 * @since 1.0.0
 *
 */
public class DatabaseLoginDialog extends JDialog {
  


  private static final long serialVersionUID = 1L;


  private LangResourceBundle lres    = (LangResourceBundle) LangResourceBundle.getBundle(new Locale(System.getProperty("user.language"), 
      System.getProperty("user.country")));

  
  /**
   * The connect action command identifier.
   */
  public static final String CONNECT_COMMAND = DatabaseLoginPanel.CONNECT_COMMAND;
  
  DatabaseLoginPanel loginPanel = null;
  
  JComboBox databaseProfiles    = null;
  JPanel profilesPanel          = null;
  
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC CONSTRUCTEUR                                                             CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  
  public DatabaseLoginDialog(JFrame owner){
    super(owner);
    super.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    this.setSize(new Dimension(600, 500));
    this.setPreferredSize(new Dimension(600, 500));

    initGUI();
  }
  
  public DatabaseLoginDialog(JFrame owner, Collection<DatabaseProfile> profiles){
    super(owner);
    super.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    this.setSize(new Dimension(600, 500));
    this.setPreferredSize(new Dimension(600, 500));

    if (profiles != null){
      databaseProfiles = new JComboBox(profiles.toArray(new DatabaseProfile[profiles.size()]));
    }
    
    initGUI();
  }
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC FIN CONSTRUCTEUR                                                         CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
 
//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
//II INITIALIZATION                                                           II
//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
  public void initGUI(){
    
    loginPanel = new DatabaseLoginPanel();
    
    this.getContentPane().setLayout(new BorderLayout());
    if (databaseProfiles != null){
      
      databaseProfiles.setRenderer(new DefaultListCellRenderer(){

        private static final long serialVersionUID = 1L;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus){
          
          DatabaseProfile profile = null;
          JLabel label            = null;
          
          Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
          
          if (component instanceof JLabel){
            if (value instanceof DatabaseProfile){
              profile = (DatabaseProfile)value;
              label   = (JLabel) component;
              
              if (profile.getName() != null){
                label.setText(profile.getName());
              } else {
                label.setText(profile.toString());
              }
              
            }
          }
          
          return component;
          
        }
      });
      
      profilesPanel = new JPanel();
      profilesPanel.setBorder(BorderFactory.createTitledBorder("Profiles"));
      profilesPanel.setLayout(new BorderLayout());
      profilesPanel.add(databaseProfiles, BorderLayout.CENTER);
      this.getContentPane().add(profilesPanel, BorderLayout.NORTH);
      
      databaseProfiles.addItemListener(new ItemListener(){

        public void itemStateChanged(ItemEvent e) {
          processDatabaseProfilesComboBoxEvent(e);
        }});
      
      loginPanel.setDatabaseProfile((DatabaseProfile)databaseProfiles.getItemAt(0));
    }
  
    
    
    this.getContentPane().add(loginPanel, BorderLayout.CENTER);
    
    this.setTitle(lres.getString("L_Select_input"));
    this.setSize(new Dimension(270, 290));
    this.setPreferredSize(new Dimension(270, 290));
    this.pack();
  }
  
  public void init(){
    Point location = getOwner().getLocation();
    Dimension ownerSize = getOwner().getSize();
    
    this.setLocation( ((int)location.getX() + ownerSize.width/2) - this.getWidth()/ 2,(((int)location.getY() + ownerSize.height) / 2) - this.getHeight()/2);
  }
//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
//II FIN INITIALIZATION                                                       II
//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
//EE EVENEMENT                                                                EE
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
  /**
   * This method is a redirection of the addActionListener method of the actionneable components of the panel.
   * @param l the acttion listener to attach.
   */
  public void addActionListener(ActionListener l){
    loginPanel.addActionListener(l);  
  }
  
  /**
   * Remove the action listener given in parameter from this component.
   * @param l the action listener to remove.
   */
  public void removeActionListener(ActionListener l){
    loginPanel.removeActionListener(l);  
  }
  
  protected void processDatabaseProfilesComboBoxEvent(ItemEvent e){
    if (e.getStateChange() == ItemEvent.SELECTED){
      loginPanel.setDatabaseProfile((DatabaseProfile)databaseProfiles.getSelectedItem());
    }
  }
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
//EE FIN INITIALIZATION                                                       EE
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

  
  
  public DatabaseLoginPanel getLoginPanel(){
    return this.loginPanel;
  }
  
  /**
   * Get the current database profile
   * @return the current database profile.
   */
  public DatabaseProfile getDatabaseProfile(){
    return loginPanel.getDatabaseProfile();
  }
}
