package com.calclab.hablar.rooms.client.occupant;

import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.hablar.core.client.mvp.Presenter;
import com.calclab.hablar.icons.client.AvatarProviderRegistry;
import com.calclab.hablar.icons.client.IconsBundle;
import com.calclab.hablar.rooms.client.RoomMessages;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasText;

/**
 * Shows the list of the occupants of the rooms
 */
public class OccupantsPresenter implements Presenter<OccupantsDisplay> {

	private final XmppRoster roster;
	private final OccupantsDisplay display;
	private int occupantsCount;

	public OccupantsPresenter(final XmppRoster roster, final Room room, final OccupantsDisplay display, final AvatarProviderRegistry registry) {
		this.roster = roster;
		this.display = display;
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
		
		if (registry != null) {
			display.asWidget().setVisible(false);
		}
/*
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
*/
		display.getAction().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				display.setPanelVisible(!display.isPanelVisible());
			}
			
		});
	}

	private void addOccupantsToPanel(final Room room) {
		for (final Occupant occupant : room.getOccupants()) {
			final OccupantDisplay ocDisplay = display.addOccupant();
			
			String nameString = occupant.getNick();
			
			if(occupant.getJID() != null) {
				RosterItem item = roster.getItemByJID(occupant.getJID());
				
				if(item != null) {
					nameString = item.getName() + " (" + occupant.getNick() + ")";
				}
			}
			
			ocDisplay.getName().setText(nameString);
			ocDisplay.setIcon(IconsBundle.bundle.buddyIcon());
		}
	}

	@Override
	public OccupantsDisplay getDisplay() {
		return display;
	}

	private void updateOccupants(final Room room) {
		final HasText label = display.getLabel();
		label.setText(RoomMessages.msg.occupants(occupantsCount));
		display.clearPanel();
		addOccupantsToPanel(room);
	}
}
