package com.calclab.hablar.testing;

import org.mockito.Mockito;

import com.calclab.emite.im.client.roster.XmppRosterLogic;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.hablar.chat.client.ChatManagerTester;
import com.calclab.hablar.chat.client.PresenceManagerTester;
import com.calclab.hablar.core.client.mvp.DefaultEventBus;
import com.calclab.hablar.search.client.SearchManagerTester;
import com.calclab.hablar.testing.display.DisplayStubFactory;
import com.calclab.hablar.testing.display.ReturnsSingletonMocks;

public class EmiteTester {
    private static DisplayStubFactory factory = DisplayStubFactory.instance;
    public final XmppSessionTester session;
    public final ChatManagerTester chatManager;
    public final SearchManagerTester searchManager;
    public final PresenceManagerTester presenceManager;
    public final DefaultEventBus eventBus;
    public final XmppRosterLogic roster;

    public EmiteTester() {
	eventBus = new DefaultEventBus();
	session = new XmppSessionTester();
	chatManager = new ChatManagerTester(session);
	roster = new XmppRosterLogic(session);
	searchManager = new SearchManagerTester();
	presenceManager = new PresenceManagerTester(session);
    }

    public <T> T mockDisplay(final Class<T> classToMock) {
	final T mock = Mockito.mock(classToMock, new ReturnsSingletonMocks(factory));
	return mock;
    }

    public void setSessionReady(final String uri) {
	session.setLoggedIn(uri);
	session.setReady();
    }

}
