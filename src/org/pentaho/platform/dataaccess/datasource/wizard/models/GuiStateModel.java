/*
 * Copyright 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created Aug 15, 2010 
 * @author wseyler
 */


package org.pentaho.platform.dataaccess.datasource.wizard.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.metadata.model.LogicalModel;
import org.pentaho.platform.dataaccess.datasource.beans.Connection;
import org.pentaho.ui.xul.XulEventSourceAdapter;
import org.pentaho.ui.xul.stereotype.Bindable;

/**
 * @author wseyler
 *
 */
//TODO: move this to the Relational Datasource
public class GuiStateModel extends XulEventSourceAdapter {

  private boolean relationalValidated;
  private boolean relationalPreviewValidated;
  private boolean relationalApplyValidated;
  private List<Connection> connections = new ArrayList<Connection>();
  private String previewLimit = "10"; //$NON-NLS-1$
  private List<LogicalModel> logicalModels;
  private String localeCode;

  private boolean dataStagingComplete;
  private FileInfo selectedCsvFile;

  private boolean dirty = true;
  private RelationalModelValidationListenerCollection relationalModelValidationListeners;

  @Bindable
  public List<Connection> getConnections() {
    return connections;
  }

  public void addConnection(Connection connection) {
    List<Connection> previousValue = getPreviousValue();
    connections.add(connection);
    this.firePropertyChange("connections", previousValue, connections); //$NON-NLS-1$
  }

  public void updateConnection(Connection connection) {
    List<Connection> previousValue = getPreviousValue();
    Connection conn = getConnectionByName(connection.getName());
    conn.setDriverClass(connection.getDriverClass());
    conn.setPassword(connection.getPassword());
    conn.setUrl(connection.getUrl());
    conn.setUsername(connection.getUsername());
    this.firePropertyChange("connections", previousValue, connections); //$NON-NLS-1$
  }

  @Bindable
  private List<Connection> getPreviousValue() {
    List<Connection> previousValue = new ArrayList<Connection>();
    for (Connection conn : connections) {
      previousValue.add(conn);
    }
    return previousValue;
  }

  public void deleteConnection(Connection connection) {
    List<Connection> previousValue = getPreviousValue();
    connections.remove(connections.indexOf(connection));
    this.firePropertyChange("connections", previousValue, connections); //$NON-NLS-1$
  }

  public void deleteConnection(String name) {
    for (Connection connection : connections) {
      if (connection.getName().equals(name)) {
        deleteConnection(connection);
        break;
      }
    }
  }

  @Bindable
  public void setConnections(List<Connection> value) {
    List<Connection> previousValue = getPreviousValue();
    this.connections = value;
    this.firePropertyChange("connections", previousValue, value); //$NON-NLS-1$
  }

  @Bindable
  public String getPreviewLimit() {
    return previewLimit;
  }

  @Bindable
  public void setPreviewLimit(String value) {
    String previousVal = this.previewLimit;
    this.previewLimit = value;
    this.firePropertyChange("previewLimit", previousVal, value); //$NON-NLS-1$
  }

  public Connection getConnectionByName(String name) {
    for (Connection connection : connections) {
      if (connection.getName().equals(name)) {
        return connection;
      }
    }
    return null;
  }

  public Integer getConnectionIndex(Connection conn) {
	  Connection connection = getConnectionByName(conn.getName());
    return connections.indexOf(connection);
  }

  @Bindable
  public boolean isRelationalValidated() {
    return relationalValidated;
  }

  @Bindable
  private void setRelationalValidated(boolean value) {
    if(value != this.relationalValidated) {
      this.relationalValidated = value;
      this.firePropertyChange("relationalValidated", !value, value);
    }
  }

  public void validateRelational() {
    setRelationalPreviewValidated(true);
    setRelationalApplyValidated(true);
    setRelationalValidated(true);
    fireRelationalModelValid();
  }

  public void invalidateRelational() {
    setRelationalPreviewValidated(false);
    setRelationalApplyValidated(false);
    setRelationalValidated(false);
    fireRelationalModelInValid();
  }

  /*
   * Clears out the model
   */
  @Bindable
  public void clearModel() {
    setPreviewLimit("10");
    setSelectedCsvFile(null);
  }

  public void addRelationalModelValidationListener(IRelationalModelValidationListener listener) {
    if (relationalModelValidationListeners == null) {
      relationalModelValidationListeners = new RelationalModelValidationListenerCollection();
    }
    relationalModelValidationListeners.add(listener);
  }

  public void removeRelationalListener(IRelationalModelValidationListener listener) {
    if (relationalModelValidationListeners != null) {
      relationalModelValidationListeners.remove(listener);
    }
  }

  /**
   * Fire all current {@link IRelationalModelValidationListener}.
   */
  void fireRelationalModelValid() {

    if (relationalModelValidationListeners != null) {
      relationalModelValidationListeners.fireRelationalModelValid();
    }
  }
  
  /**
   * Fire all current {@link IRelationalModelValidationListener}.
   */
  void fireRelationalModelInValid() {

    if (relationalModelValidationListeners != null) {
      relationalModelValidationListeners.fireRelationalModelInValid();
    }
  }
  public void setRelationalPreviewValidated(boolean value) {
    if (value != this.relationalPreviewValidated) {
      this.relationalPreviewValidated = value;
      this.firePropertyChange("relationalPreviewValidated", !value, this.relationalPreviewValidated);
    }
  }
  public boolean isRelationalPreviewValidated() {
    return this.relationalPreviewValidated;
  }
  
  public boolean isRelationalApplyValidated() {
    return relationalApplyValidated;
  }

  public void setRelationalApplyValidated(boolean value) {
    if (value != this.relationalApplyValidated) {
      this.relationalApplyValidated = value;
      this.firePropertyChange("relationalApplyValidated", !value, this.relationalApplyValidated);
    }    
  }

  public List<LogicalModel> getLogicalModels() {
    return logicalModels;
  }

  public void setLogicalModels(List<LogicalModel> logicalModels) {
    this.logicalModels = logicalModels;
  }

  public String getLocaleCode() {
    return localeCode;
  }

  public void setLocaleCode(String localeCode) {
    this.localeCode = localeCode;
  }

  public void setDataStagingComplete(boolean status) {
    dataStagingComplete = status;
  }
  
  public boolean isDataStagingComplete() {
    return dataStagingComplete;
  }

  public FileInfo getSelectedCsvFile() {
    return selectedCsvFile;
  }

  public void setSelectedCsvFile(FileInfo selectedCsvFile) {
    this.selectedCsvFile = selectedCsvFile;
  }

  @Bindable
  public boolean isDirty() {
    return dirty;
  }

  @Bindable
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
    firePropertyChange("dirty", null, dirty);
  }
}
