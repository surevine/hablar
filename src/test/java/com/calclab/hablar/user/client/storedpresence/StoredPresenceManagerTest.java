package com.calclab.hablar.user.client.storedpresence;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.xep.storage.client.PrivateStorageManager;
import com.calclab.emite.xep.storage.client.events.PrivateStorageResponseEvent;
import com.calclab.emite.xep.storage.client.events.PrivateStorageResponseHandler;
import com.calclab.emite.xtesting.XmppSessionTester;

public class StoredPresenceManagerTest {

    private static final String SUCCESS_RESULT = "<iq type=\"result\" from=\"test@domain/here\" to=\"test@domain/here\" id=\"1001\"/>";
    private static final String RETRIEVED = "<iq type=\"result\" from=\"test@domain/here\" to=\"test@domain/here\" id=\"1001\">"
	    + "<query xmlns=\"jabber:iq:private\">" + StoredPresencesTest.sample + "</query></iq>";
    private StoredPresenceManager manager;
    private XmppSessionTester session;

    @Before
    public void before() {
	session = new XmppSessionTester("test@domain");
	manager = new StoredPresenceManager(new PrivateStorageManager(session));
    }

    @Test
    public void shouldAddOnce() {
    
    final PrivateStorageResponseHandler listener = Mockito.mock(PrivateStorageResponseHandler.class);
	
	manager.add(new StoredPresence("Sleeping...", Show.away), listener);
	session.answer(SUCCESS_RESULT);
	manager.add(new StoredPresence("Sleeping...", Show.away), listener);
	manager.get(listener);
	session.answer(RETRIEVED);
	
	verify(listener,times(1)).onStorageResponse(any(PrivateStorageResponseEvent.class));
    }
}
