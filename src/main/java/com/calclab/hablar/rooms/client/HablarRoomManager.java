package com.calclab.hablar.rooms.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.emite.xep.muc.client.events.RoomInvitationEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationHandler;
import com.calclab.hablar.chat.client.ui.ChatMessage;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.avatars.AvatarDisplay;
import com.calclab.hablar.core.client.avatars.AvatarPresenter;
import com.calclab.hablar.core.client.avatars.ZoomableAvatarPresenter;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.core.client.page.PagePresenter.Visibility;
import com.calclab.hablar.icons.client.AvatarProviderRegistry;
import com.calclab.hablar.rooms.client.room.RoomDisplay;
import com.calclab.hablar.rooms.client.room.RoomPresenter;
import com.calclab.hablar.rooms.client.room.RoomWidget;
import com.google.inject.Inject;

public class HablarRoomManager {
	
	private AvatarProviderRegistry registry;
	
	private HashMap<XmppURI,AvatarPresenter> avatars = new HashMap<XmppURI,AvatarPresenter>();

	public static interface RoomPageFactory {
		RoomDisplay create(boolean sendButtonVisible);
	}

	public static interface RoomPresenterFactory {
		RoomPresenter create(HablarEventBus eventBus, Room room, RoomDisplay display);
	}

	private final Hablar hablar;
	private final RoomPageFactory factory;
	private final RoomPresenterFactory presenterFactory;
	private final HashMap<XmppURI, RoomPresenter> roomPages;
	private final ArrayList<RoomInvitation> acceptedInvitations;
	private final XmppRoster roster;

	@Inject
	public HablarRoomManager(final RoomManager rooms, final Hablar hablar, final HablarRoomsConfig config, final RoomPageFactory factory,
			final RoomPresenterFactory presenterFactory, final AvatarProviderRegistry registry, final XmppRoster roster) {
		this.hablar = hablar;
		this.factory = factory;
		this.presenterFactory = presenterFactory;
		this.registry = registry;
		this.roster = roster;
		acceptedInvitations = new ArrayList<RoomInvitation>();
		roomPages = new HashMap<XmppURI, RoomPresenter>();

		rooms.addChatChangedHandler(new ChatChangedHandler() {
			@Override
			public void onChatChanged(final ChatChangedEvent event) {
				if (event.isCreated()) {
					createRoom((Room) event.getChat());
				} else if (event.isOpened()) {
					openRoom(event.getChat());
				}
			}
		});

		rooms.addRoomInvitationReceivedHandler(new RoomInvitationHandler() {
			@Override
			public void onRoomInvitation(final RoomInvitationEvent event) {
				final RoomInvitation invitation = event.getRoomInvitation();
				acceptedInvitations.add(invitation);
				rooms.acceptRoomInvitation(invitation);
			}
		});
	}

	public HablarRoomManager(final XmppSession session, final XmppRoster roster, final RoomManager rooms, final Hablar hablar, final HablarRoomsConfig config,
			final AvatarProviderRegistry registry) {
		this(rooms, hablar, config, new RoomPageFactory() {
			@Override
			public RoomDisplay create(final boolean sendButtonVisible) {
				return new RoomWidget(sendButtonVisible);
			}
		}, new RoomPresenterFactory() {
			@Override
			public RoomPresenter create(final HablarEventBus eventBus, final Room room, final RoomDisplay display) {
				return new RoomPresenter(session, roster, eventBus, room, display, registry, rooms);
			}
		}, registry, roster);
	}

	protected void createRoom(final Room room) {
		final RoomDisplay display = factory.create(true);
		final RoomPresenter presenter = presenterFactory.create(hablar.getEventBus(), room, display);
		roomPages.put(room.getURI(), presenter);
		hablar.addPage(presenter);
		
		room.addOccupantChangedHandler(new OccupantChangedHandler() {
			@Override
			public void onOccupantChanged(final OccupantChangedEvent event) {
				if (event.getChangeType().equals(ChangeTypes.added)) {
					
					XmppURI jid = event.getOccupant().getJID();
					
					String nameString = event.getOccupant().getNick();
					
					if(jid != null) {
						RosterItem item = roster.getItemByJID(jid);
						
						if(item != null) {
							nameString = item.getName() + " (" + nameString + ")";
						}
					}
					
					AvatarDisplay avatarDisplay = display.addAvatar(nameString);
					AvatarPresenter avatarPres = new ZoomableAvatarPresenter(avatarDisplay, registry.getFromMeta());
					avatarPres.setJid(event.getOccupant().getJID());
					avatars.put(event.getOccupant().getJID(), avatarPres);
						
				} else if (event.getChangeType().equals(ChangeTypes.removed)) {
					AvatarPresenter pres = avatars.get(event.getOccupant().getJID());
					if(pres != null) {
						display.removeAvatar(pres.getDisplay());
						avatars.remove(event.getOccupant().getJID());
					}
				} // We're only interested in add and remove events, AFAIK.
			}
		});
	}

	protected RoomInvitation getInvitation(final XmppURI uri) {
		for (final RoomInvitation invitation : acceptedInvitations) {
			if (invitation.getRoomURI().getNode().equals(uri.getNode())) {
				return invitation;
			}
		}
		return null;
	}

	protected void openRoom(final Chat room) {
		final RoomPresenter roomPage = roomPages.get(room.getURI());
		assert roomPage != null : "Error in room pages - HablarRoomManager";
		final RoomInvitation invitation = getInvitation(room.getURI());
		if (invitation != null) {
			acceptedInvitations.remove(invitation);
			String message = "You have been invited to this group chat";
			message += invitation.getInvitor() != null ? " by " + invitation.getInvitor().getNode() : "";
			message += invitation.getReason() != null ? ": " + invitation.getReason() : "";
			roomPage.addMessage(new ChatMessage(message));
			roomPage.requestVisibility(Visibility.notFocused);
		} else {
			roomPage.requestVisibility(Visibility.focused);
		}
	}
}