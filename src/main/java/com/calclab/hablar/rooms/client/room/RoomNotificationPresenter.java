package com.calclab.hablar.rooms.client.room;

import java.util.ArrayList;
import java.util.HashMap;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.emite.xep.muc.client.events.RoomInvitationSentEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationSentHandler;
import com.calclab.emite.xep.muc.client.subject.RoomSubject;
import com.calclab.emite.xep.muc.client.subject.RoomSubjectChangedEvent;
import com.calclab.emite.xep.muc.client.subject.RoomSubjectChangedHandler;
import com.calclab.hablar.chat.client.ui.ChatMessage;
import com.calclab.hablar.rooms.client.RoomMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

public class RoomNotificationPresenter {

	private final RoomPresenter roomPresenter;

	private final String me;
	
	private class OccupantStatus {
		Occupant occupant;
		String changeType;
		Timer flushTimer;
	}
	
	// This is used to suppress adjacent left/joined notifications
	private final HashMap<XmppURI, OccupantStatus> occupantTracker;

	public RoomNotificationPresenter(final XmppSession session, final XmppRoster roster, final RoomPresenter roomPresenter, final Room room) {
		this.roomPresenter = roomPresenter;
		me = session.getCurrentUserURI().getNode();

		occupantTracker = new HashMap<XmppURI, OccupantStatus>();
		
		room.addOccupantChangedHandler(new OccupantChangedHandler() {

			@Override
			public void onOccupantChanged(final OccupantChangedEvent event) {
				changeOccupantStatus(event.getOccupant(), event.getChangeType());
			}
		});
		
		room.addBeforeSendMessageHandler(new MessageHandler() {
			
			@Override
			public void onMessage(MessageEvent event) {
				// We only care about messages from an actual occupant (not the room)
				if(occupantTracker.size() > 0) {
					ArrayList<XmppURI> occupantSet = new ArrayList<XmppURI>(occupantTracker.keySet());
					
					for(XmppURI xmppURI : occupantSet) {
						flushNotification(xmppURI);
					}
				}
			}
		});
		
		room.addBeforeReceiveMessageHandler(new MessageHandler() {
			
			@Override
			public void onMessage(MessageEvent event) {
				// We only care about messages from an actual occupant (not the room)
				if(!event.getMessage().getFrom().getResource().isEmpty()) {
					if(occupantTracker.size() > 0) {
						ArrayList<XmppURI> occupantSet = new ArrayList<XmppURI>(occupantTracker.keySet());
						
						for(XmppURI xmppURI : occupantSet) {
							flushNotification(xmppURI);
						}
					}
				}
			}
		});

		/*
		 * room.onOccupantModified(new Listener<Occupant>() {
		 * 
		 * @Override public void onEvent(final Occupant occupant) {
		 * show(i18n().occupantModified(occupant.getNick())); } });
		 */

		RoomSubject.addRoomSubjectChangedHandler(room, new RoomSubjectChangedHandler() {

			@Override
			public void onSubjectChanged(final RoomSubjectChangedEvent event) {
				final Occupant occupant = room.getOccupantByOccupantUri(event.getOccupantUri());

				String message;
				if (occupant != null) {
					message = RoomMessages.msg.roomSubjectChanged(occupant.getNick(), event.getSubject());
				} else { // The subject has been changed by a room rule.
					message = RoomMessages.msg.roomSubjectChangedAnonymous(event.getSubject());
				}
				show(message);
			}
		});

		room.addRoomInvitationSentHandler(new RoomInvitationSentHandler() {

			@Override
			public void onRoomInvitationSent(final RoomInvitationSentEvent event) {
				final XmppURI userJid = event.getUserJid();
				final RosterItem item = roster.getItemByJID(userJid);
				final String name = ((item != null) && (item.getName() != null)) ? item.getName() : userJid.getNode();

				final String body = isEmpty(event.getReasonText()) ? RoomMessages.msg.invitationSent(name) : RoomMessages.msg.invitationSentWithReason(name,
						event.getReasonText());
				show(body);
			}

			private boolean isEmpty(final String reason) {
				return (reason == null) || reason.trim().equals("");
			}
		});

	}
	
	private void changeOccupantStatus(final Occupant occupant, final String change) {
		GWT.log("Change Occupant Status: " + occupant + " : " + change);
		
		if(!ChangeTypes.added.equals(change) && !ChangeTypes.removed.equals(change)) {
			// We only care about adds and removes
			return;
		}

		OccupantStatus status = occupantTracker.get(occupant.getUserUri().getJID());
		
		if(status != null) {
			if(status.flushTimer != null) {
				status.flushTimer.cancel();
			}
			
			if(!status.changeType.equals(change)) {
				occupantTracker.remove(occupant.getUserUri().getJID());
				return;
			}
		} else {
			status = new OccupantStatus();
			occupantTracker.put(occupant.getUserUri().getJID(), status);
		}

		final OccupantStatus fStatus = status;
		
		status.changeType = change;
		status.occupant = occupant;
		status.flushTimer = new Timer() {
			@Override
			public void run() {
				fStatus.flushTimer = null;
				flushNotification(occupant.getUserUri().getJID());
			}
		};
		
		// TODO Make this not hardcoded
		status.flushTimer.schedule(120000);
	}
	
	private void flushNotification(final XmppURI occupantUserUri) {
		OccupantStatus status = occupantTracker.remove(occupantUserUri);

		if(status == null) {
			// No stored notification to flush
			GWT.log("Flush Occupant Status no status: " + occupantUserUri);
			return;
		}
		
		GWT.log("Flush Occupant Status: " + status.occupant);
		
		if(status.flushTimer != null) {
			status.flushTimer.cancel();
		}
		
		final String node = occupantUserUri != null ? occupantUserUri.getNode() : null;

		if (!me.equals(node)) {
			if (ChangeTypes.added.equals(status.changeType)) {
				final String body = RoomMessages.msg.occupantHasJoined(status.occupant.getNick());
				show(body);
			} else if (ChangeTypes.removed.equals(status.changeType)) {
				final String body = RoomMessages.msg.occupantHasLeft(status.occupant.getNick());
				show(body);
			}
		}
	}

	private void show(final String body) {
		roomPresenter.addMessage(new ChatMessage(body));
		roomPresenter.fireUserNotification(body);
	}
}
