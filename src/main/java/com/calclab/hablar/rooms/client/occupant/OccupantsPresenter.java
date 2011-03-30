package com.calclab.hablar.rooms.client.occupant;

import static com.calclab.hablar.rooms.client.HablarRooms.i18n;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.hablar.core.client.mvp.Presenter;
import com.calclab.hablar.core.client.ui.icon.Icons;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.HasText;

/**
 * Shows the list of the occupants of the rooms
 */
public class OccupantsPresenter implements Presenter<OccupantsDisplay> {

    private final OccupantsDisplay display;
    private final XmppRoster roster;
    private int occupantsCount;

    public OccupantsPresenter(final Room room, final OccupantsDisplay display, final XmppRoster roster) {
	this.display = display;
	this.roster = roster;
	occupantsCount = 0;
	updateOccupants(room);

	room.addOccupantChangedHandler(new OccupantChangedHandler() {
	    @Override
	    public void onOccupantChanged(final OccupantChangedEvent event) {
		if (event.isAdded()) {
		    occupantsCount++;
		    updateOccupants(room);
		} else if (event.isRemoved()) {
		    occupantsCount--;
		    updateOccupants(room);
		}
	    }
	});

	display.getOverAction().addMouseOverHandler(new MouseOverHandler() {
	    @Override
	    public void onMouseOver(final MouseOverEvent event) {
		display.setPanelVisible(true);
	    }
	});

	display.getOutAction().addMouseOutHandler(new MouseOutHandler() {
	    @Override
	    public void onMouseOut(final MouseOutEvent event) {
		display.setPanelVisible(false);
	    }
	});
    }

    private void addOccupantsToPanel(final Room room) {
	for (final Occupant occupant : room.getOccupants()) {
	    final OccupantDisplay ocDisplay = display.addOccupant();

	    String label = occupant.getNick();
	    final XmppURI userUri = occupant.getUserUri();
	    
	    if(userUri != null) {
	    	final String name = roster.getJidName(userUri);
	    	
	    	if((name != null) && (name != "")) {
	    		label += " (" + name + ")";
	    	}
	    }
	    
	    ocDisplay.getName().setText(label);
	    ocDisplay.setIcon(Icons.get(Icons.BUDDY_ON));
	}
    }

    @Override
    public OccupantsDisplay getDisplay() {
	return display;
    }

    private void updateOccupants(final Room room) {
	final HasText label = display.getLabel();
	label.setText(i18n().occupants(occupantsCount));
	display.clearPanel();
	addOccupantsToPanel(room);
    }

}
