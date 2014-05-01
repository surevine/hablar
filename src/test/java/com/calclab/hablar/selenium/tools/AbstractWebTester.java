package com.calclab.hablar.selenium.tools;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.google.gwt.user.client.ui.UIObject;

public abstract class AbstractWebTester {
    private final WebDriver driver;
    private final String baseUrl;

    public AbstractWebTester(final WebDriver driver, final String baseUrl) {
	this.driver = driver;
	this.baseUrl = baseUrl;

	driver.navigate().to(baseUrl);
    }

    public void close() {
	driver.close();
    }

    public WebElement getById(final String id) {
	return driver.findElement(By.id(UIObject.DEBUG_ID_PREFIX + id));
    }

    public void home() {
	assert baseUrl != null;
	driver.get(baseUrl);
    }

    public boolean isElementPresent(final String id) {
    	return !driver.findElements(By.id(id)).isEmpty();
    }

    public boolean isTextPresent(final String text) {
    	return !driver.findElements(By.xpath("//*[contains(.,'"+text+"')]")).isEmpty();
    }

    public void moveMouseAt(final int x, final int y) {
    	new Actions(driver).moveToElement(driver.findElement(By.tagName("body")), x, y);
    }

}