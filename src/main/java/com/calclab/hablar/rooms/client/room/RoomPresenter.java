package com.calclab.hablar.rooms.client.room;

import java.util.Date;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.ChatStates;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.xep.delay.client.Delay;
import com.calclab.emite.xep.delay.client.DelayHelper;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.hablar.chat.client.ui.ChatMessage;
import com.calclab.hablar.chat.client.ui.ChatPresenter;
import com.calclab.hablar.chat.client.ui.ColorHelper;
import com.calclab.hablar.chat.client.ui.chatmessageformat.ChatMessageFormatter;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.core.client.page.events.UserMessageEvent;
import com.calclab.hablar.core.client.ui.menu.Action;
import com.calclab.hablar.core.client.validators.Empty;
import com.calclab.hablar.icons.client.AvatarConfig;
import com.calclab.hablar.icons.client.AvatarProviderRegistry;
import com.calclab.hablar.icons.client.IconsBundle;
import com.calclab.hablar.rooms.client.RoomMessages;
import com.calclab.hablar.rooms.client.RoomName;
import com.calclab.hablar.rooms.client.occupant.OccupantsPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Timer;

public class RoomPresenter extends ChatPresenter implements RoomPage {
	private enum ConnectedState {
		never,
		hasConnected,
		hasReconnected
	}
	
	public static final String TYPE = "Room";
	public static final String ROOM_MESSAGE = "RoomMessage";
	private static int id = 0;
	
	/**
	 * A state flag which is used to determine if this chat has connected at some point in the past.
	 * If this flag is reconnected then a number of informational messages will be suppressed
	 */
	private ConnectedState connectedState;
	
	private boolean suppressInfoMessages;
	
	private Timer suppressMessagesTimer;
	
	private final Room room;
	
	private final AvatarConfig avatarConfig;

	public RoomPresenter(final XmppSession session, final XmppRoster roster, final HablarEventBus eventBus, final Room room, final RoomDisplay display,
			final AvatarProviderRegistry registry, final RoomManager roomManager) {
		super(TYPE, "" + ++id, eventBus, room, display, registry);
		this.room = room;
		connectedState = ConnectedState.never;
		suppressInfoMessages = false;
		
		display.setId(getId());
		
		this.avatarConfig = registry.getFromMeta();

		new RoomNotificationPresenter(session, roster, this, room);
		new OccupantsPresenter(roster, room, display.createOccupantsDisplay(room.getID()), registry);

		final String roomName = RoomName.decode(room.getURI().getNode());
		setVisibility(Visibility.notFocused);
		model.init(IconsBundle.bundle.rosterIcon(), roomName, roomName);
		model.setCloseable(true);
		model.setCloseConfirmationMessage(RoomMessages.msg.confirmCloseRoom());
		model.setCloseConfirmationTitle(RoomMessages.msg.confirmCloseRoomTitle(roomName));

		room.addMessageReceivedHandler(new MessageHandler() {

			@Override
			public void onMessage(final MessageEvent event) {
				final Message message = event.getMessage();

				final String from;

				final Occupant occupant = room.getOccupantByOccupantUri(message.getFrom());
				RosterItem rosterItem;

				// Check if the occupant is found, and if they exist in the
				// roster. We can then use their nickname
				if ((occupant != null) && ((rosterItem = roster.getItemByJID(occupant.getJID())) != null)) {
					if ((rosterItem.getName() != null) && (!rosterItem.getName().equals(""))) {
						from = rosterItem.getName();
					} else {
						from = rosterItem.getJID().getShortName();
					}
				} else {
					from = message.getFrom().getResource();
				}
				
				// If we are suppressing server messages
				if(suppressInfoMessages && Empty.is(from)) {
					return;
				}

				final Delay delay = DelayHelper.getDelay(message);
				if (!room.isComingFromMe(message) || (delay != null)) {
					final String messageBody = message.getBody();
					if (Empty.not(messageBody)) {
						final ChatMessage chatMessage = new ChatMessage(from, messageBody, ChatMessage.MessageType.incoming,
								occupant == null ? null : avatarConfig.getUrl(occupant.getJID(), display.getAvatarSize()));
						if (occupant != null) {
							chatMessage.color = ColorHelper.getColor(occupant.getJID());
						} else {
							chatMessage.color = ColorHelper.getColor(message.getFrom());
						}
						if (delay != null) {
							chatMessage.setDate(delay.getStamp());
						} else {
							chatMessage.setDate(new Date());
						}
						addMessage(chatMessage);
						fireUserMessage(roomName, from, messageBody);
					}
				}
			}
		});

		if (ChatStates.locked.equals(room.getChatState())) {
			addMessage(new ChatMessage(RoomMessages.msg.waitingForUnlockRoom()));
		}

		display.getAction().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				sendMessage(room, display);
			}

		});
		display.getTextBox().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13) {
					event.stopPropagation();
					event.preventDefault();
					sendMessage(room, display);
				}
			}
		});
		room.addChatStateChangedHandler(true, new StateChangedHandler() {
			@Override
			public void onStateChanged(StateChangedEvent event) {
				if(event.is(ChatStates.ready)) {
					if(connectedState.equals(ConnectedState.never)) {
						connectedState = ConnectedState.hasConnected;
					} else {
						connectedState = ConnectedState.hasReconnected;
						
						suppressInfoMessages = true;
						
						if(suppressMessagesTimer != null) {
							suppressMessagesTimer.cancel();
						}
						
						suppressMessagesTimer = new Timer() {
							@Override
							public void run() {
								suppressInfoMessages = false;
								suppressMessagesTimer = null;
							}
						};
						
						// TODO: Un-hardcode this time
						suppressMessagesTimer.schedule(60000);
					}
				} else if (event.is(ChatStates.locked)) {
					if(suppressMessagesTimer != null) {
						suppressMessagesTimer.cancel();
					}
				}
			}
		});
	}

	@Override
	public void addAction(final Action<RoomPage> action) {
		display.createAction(action).addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				action.execute(RoomPresenter.this);
			}
		});
	}

	@Override
	public String getChatName() {
		return room.getID();
	}

	@Override
	public Room getRoom() {
		return room;
	}

	@Override
	public void setVisibility(final Visibility visibility) {
		if (visibility == Visibility.hidden) {
			room.close();
		}
		super.setVisibility(visibility);
	}

	private void fireUserMessage(final String roomName, final String from, String body) {
		body = ChatMessageFormatter.ellipsis(body, 25);
		final String message = Empty.is(from) ? RoomMessages.msg.incommingAdminMessage(roomName, body) : RoomMessages.msg
				.incommingMessage(roomName, from, body);
		fireUserNotification(message);
	}

	void fireUserNotification(final String message) {
		eventBus.fireEvent(new UserMessageEvent(this, message, ROOM_MESSAGE));
	}

}
