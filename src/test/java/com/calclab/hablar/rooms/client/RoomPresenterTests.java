package com.calclab.hablar.rooms.client;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.hablar.core.client.page.PagePresenter.Visibility;
import com.calclab.hablar.icons.client.AvatarProviderRegistry;
import com.calclab.hablar.rooms.client.occupant.OccupantsDisplay;
import com.calclab.hablar.rooms.client.room.RoomDisplay;
import com.calclab.hablar.rooms.client.room.RoomPresenter;
import com.calclab.hablar.testing.EmiteTester;
import com.calclab.hablar.testing.HablarTester;

public class RoomPresenterTests {

	private RoomPresenter presenter;
	private RoomDisplay display;
	private Room room;

	@Before
	public void setup() {
		final EmiteTester emiteTester = new EmiteTester();
		emiteTester.setSessionReady("some@domain");
		final HablarTester tester = new HablarTester();
		final EmiteEventBus eventBus = Mockito.mock(EmiteEventBus.class);
		display = tester.newDisplay(RoomDisplay.class);
		final OccupantsDisplay occupantsDisplay = tester.newDisplay(OccupantsDisplay.class);
		when(display.createOccupantsDisplay(anyString())).thenReturn(occupantsDisplay);
		room = Mockito.mock(Room.class);
		when(room.getURI()).thenReturn(XmppURI.uri("room@domain"));
		when(room.getChatEventBus()).thenReturn(eventBus);
		presenter = new RoomPresenter(emiteTester.session, emiteTester.roster, tester.getEventBus(), room, display, new AvatarProviderRegistry());
	}

	/**
	 * Fixes issue 288
	 */
	@Test
	public void shouldCloseRoomWhenPageIsClosed() {
		presenter.setVisibility(Visibility.hidden);
		verify(room).close();
	}

}
