package com.calclab.hablar.core.mock;

import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.calclab.hablar.core.client.mvp.Display;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.core.client.page.Page;
import com.calclab.hablar.core.client.page.PageState;
import com.calclab.hablar.icons.client.IconsBundle;
import com.calclab.hablar.testing.HablarGWTMockUtilities;
import com.calclab.hablar.testing.display.DisplayMocker;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.client.ui.Widget;

public class HablarMocks {

	public static void disarm() {
		HablarGWTMockUtilities.disarm();
		HablarGWTMockUtilities.addProvider(IconsBundle.class, new HablarGWTMockUtilities.Provider<IconsBundle>() {
			@Override
			public IconsBundle get(Class<? extends IconsBundle> clazz) {
				return Mockito.mock(clazz, new ReturnsSingletonMocks());
			}
		});
	}

	public static Display getDisplay() {
		Display display = DisplayMocker.mock(Display.class);
		Widget widget = getWidget();
		when(display.asWidget()).thenReturn(widget);
		return display;
	}

	@SuppressWarnings("unchecked")
	public static Page<Display> getPage(HablarEventBus eventBus) {
		Page<Display> page = mock(Page.class);
		Display display = getDisplay();
		when(page.getDisplay()).thenReturn(display);
		PageState state = new PageState(eventBus, page);
		when(page.getState()).thenReturn(state);
		return page;
	}

	public static Widget getWidget() {
		Widget widget = mock(Widget.class);
		return widget;
	}

	public static <T> T mock(Class<T> widgetType) {
		return Mockito.mock(widgetType);
	}

	static class ReturnsSingletonMocks implements Answer<Object> {
		private final HashMap<Method, Object> mocks = new HashMap<Method, Object>();

		@Override
		public Object answer(final InvocationOnMock invocation) throws Throwable {
			return getMock(invocation.getMethod());
		}

		public <T> T mock(final Class<T> classToMock) {
			final T mock = Mockito.mock(classToMock, this);
			return mock;
		}

		private Object getMock(final Method method) {
			Object mock = mocks.get(method);
			if (mock == null) {
				final Class<?> mockType = method.getReturnType();
				mock = mock(mockType);
				mocks.put(method, mock);
			}
			return mock;
		}
	}
}