package com.calclab.hablar.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ByIdOrName;
import org.testng.Assert;

public abstract class PageObject {

    private static final long[] POLL_INTERVALS = { 10, 20, 30, 40, 50, 50, 50, 50, 100 };
    private final WebDriver webDriver;

    public PageObject(final WebDriver webDriver) {
	this.webDriver = webDriver;
    }

    protected WebElement findElement(final By by) {
	return (WebElement) getWebDriver().findElement(by);
    }

    private WebDriver getWebDriver() {
	return webDriver;
    }

    public void waitFor(final WebElement element) {
	final String id = element.getAttribute("id");
	System.out.println("WAIT FOR: " + id);
	waitFor(id, new Runnable() {
	    @Override
	    public void run() {
		Assert.assertTrue(element.isDisplayed());
	    }
	});
    }

    /**
     * Thanks to:
     * http://groups.google.com/group/webdriver/browse_frm/thread/6e705242
     * cc6d75ed/f5f8dca438397254?lnk=gst#f5f8dca438397254
     * 
     * @param waitForWhat
     * @param runnable
     */
    protected void waitFor(final String waitForWhat, final Runnable runnable) {
	int i = 0;
	boolean success = false;
	final long timeout = System.currentTimeMillis() + 9000;
	while ((i < POLL_INTERVALS.length) && !success) {
	    try {
		runnable.run();
		success = true;
	    } catch (final Throwable e) {
		if (++i == POLL_INTERVALS.length) {
		    if (System.currentTimeMillis() > timeout) {
			throw new RuntimeException("Timeout while waiting for " + waitForWhat, e);
		    } else {
			--i;
		    }
		}
		try {
		    Thread.sleep(POLL_INTERVALS[i]);
		} catch (final InterruptedException e2) {
		    Thread.currentThread().interrupt();
		    throw new RuntimeException("Got interrupted while waiting for " + waitForWhat, e2);
		}
	    }
	}
    }

    protected void waitFor(final WebElement element, final String text) {
	System.out.println("WAIT FOR: " + text);
	waitFor(text, new Runnable() {
	    @Override
	    public void run() {
		final String elText = element.getText();
		System.out.println("Element text: " + elText);
		Assert.assertTrue(elText.contains(text));
	    }
	});
    }

    protected void waitForId(final String id) {
	System.out.println("WAIT FOR: " + id);
	waitFor(id, new Runnable() {
	    @Override
	    public void run() {
		Assert.assertTrue(((WebElement) getWebDriver().findElement(new ByIdOrName(id))).isDisplayed());
	    }
	});
    }

    protected void waitForValue(final WebElement element, final String text) {
	System.out.println("WAIT FOR: " + text);
	waitFor(text, new Runnable() {
	    @Override
	    public void run() {
		final String elValue = element.getAttribute("value");
		System.out.println("Element value: " + elValue);
		Assert.assertTrue(elValue.contains(text));
	    }
	});
    }
}
