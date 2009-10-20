/*
 * Copyright (C) 2009 JavaRosa
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.javarosa.entity.activity;

import java.util.Hashtable;
import java.util.Vector;

import org.javarosa.core.Context;
import org.javarosa.core.api.Constants;
import org.javarosa.core.api.IActivity;
import org.javarosa.core.api.ICommand;
import org.javarosa.core.api.IShell;
import org.javarosa.core.api.IView;
import org.javarosa.core.services.storage.utilities.IRecordStoreEnumeration;
import org.javarosa.core.services.storage.utilities.RMSUtility;
import org.javarosa.core.services.storage.utilities.RecordStorageException;
import org.javarosa.entity.model.IEntity;
import org.javarosa.entity.model.view.EntitySelectDetailPopup;
import org.javarosa.entity.model.view.EntitySelectView;
import org.javarosa.entity.util.EntitySelectContext;

public class EntitySelectActivity implements IActivity {
	public static final String ENTITY_ID_KEY = "entity-id";
	public static final String ACTION_NEW_ENTITY = "new-entity";
	
	private IShell parent;
	private EntitySelectContext context;
	private IView activeView;
	private EntitySelectView selView;
	
	private RMSUtility entityRMS;
	private IEntity entityPrototype;
	private String newEntityIDKey;
	
	boolean immediatelySelectNewlyCreated = true;
	
	Vector entities;	
	
	public EntitySelectActivity (IShell parent, String title) {
		this.parent = parent;
		selView = new EntitySelectView(this, title);
	}

	public void start(Context context) {
		this.context = new EntitySelectContext(context);

		entityRMS = this.context.getRMSUtility();
		entityPrototype = this.context.getEntityPrototype();
		newEntityIDKey = this.context.getNewEntityIDKey();

		selView.entityType = entityPrototype.entityType();
		selView.setStyleKey(this.context.getStyleKey());
		
		Integer newMode = this.context.getNewMode();
		if(newMode != null) {
			selView.newMode = newMode.intValue();
		}
		
		loadEntities();
		if(this.context.isBailOnEmpty() && entities.isEmpty()) {
			parent.returnFromActivity(this, Constants.ACTIVITY_CANCEL, new Hashtable());
		} else {
			selView.init();
			showList();
		}
	}

	private void loadEntities () {
		entities = new Vector();
		
		IRecordStoreEnumeration recenum = entityRMS.enumerateMetaData();
		while (recenum.hasNextElement()) {
			try {
				loadEntity(recenum.nextRecordId());
			} catch (RecordStorageException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadEntity (int recordID) {
		IEntity entity = entityPrototype.factory(recordID);
		entity.readEntity(entity.fetchRMS(entityRMS));
		if(this.context.getEntityFilter().isPermitted(entity)) {
			entities.addElement(entity);		
		}
	}
	
	public void setView (IView view) {
		activeView = view;
		parent.setDisplay(this, view);
	}
	
	public void contextChanged (Context context) {
		this.context.mergeInContext(context);
	}

	public void halt() {
		//do nothing
	}

	public void resume(Context context) {
		int newEntityID = (context.getElement(newEntityIDKey) != null ? ((Integer)context.getElement(newEntityIDKey)).intValue() : -1);
		
		if (newEntityID != -1) {
			//returning from 'create new entity' activity
			if (immediatelySelectNewlyCreated) {
				entityChosen(newEntityID);
			} else {
				loadEntity(newEntityID);
				selView.refresh(newEntityID);
				showList();
			}
		} else {
			//resumed from halt, etc., OR cancelled out of new entity activity (so no new entity created)
			this.contextChanged(context);
			setView(activeView);
		}
	}

	public void destroy() {
		//do nothing
	}
	
	public Context getActivityContext() {
		return context;
	}
	
	public void setShell (IShell shell) {
		this.parent = shell;
	}
	
	public Vector search (String key) {
		Vector matches = new Vector();
		
		if (key == null || key.equals("")) {
			for (int i = 0; i < entities.size(); i++)
				matches.addElement(new Integer(i));
		} else {
			for (int i = 0; i < entities.size(); i++) {
				IEntity entity = (IEntity)entities.elementAt(i);
				
				if (entity.matchID(key) || entity.matchName(key)) {
					matches.addElement(new Integer(i));
				}
			}
		}
		
		return matches;
	}
	
	public void showList () {
		selView.show();
	}
	
	public void itemSelected (int i) {
		IEntity entity = (IEntity)entities.elementAt(i);
		EntitySelectDetailPopup psdp = new EntitySelectDetailPopup(this, entity, entityPrototype, entityRMS);
		psdp.show();
	}
	
	public void entityChosen (int entityID) {
		Hashtable returnArgs = new Hashtable();
		returnArgs.put(ENTITY_ID_KEY, new Integer(entityID));
		parent.returnFromActivity(this, Constants.ACTIVITY_COMPLETE, returnArgs);
	}
	
	public void newEntity () {
		Hashtable returnArgs = new Hashtable();
		returnArgs.put("action", ACTION_NEW_ENTITY);
		parent.returnFromActivity(this, Constants.ACTIVITY_NEEDS_RESOLUTION, returnArgs);
	}
	
	public void exit () {
		parent.returnFromActivity(this, Constants.ACTIVITY_CANCEL, null);
	}
	
	public String[] getDataFields (int i) {
		return ((IEntity)entities.elementAt(i)).getShortFields();
	}
	
	public String[] getTitleData () {
		return entityPrototype.getHeaders(false);
	}
	
	public String getDataName (int i) {
		return ((IEntity)entities.elementAt(i)).getName();
	}
	
	public String getDataID (int i) {
		return ((IEntity)entities.elementAt(i)).getID();
	}	

	public int getRecordID (int i) {
		return ((IEntity)entities.elementAt(i)).getRecordID();
	}

	public void annotateCommand(ICommand command) {
		// TODO Auto-generated method stub
	}	
	
	public String getEntityType() {
		return entityPrototype.entityType();
	}

}
