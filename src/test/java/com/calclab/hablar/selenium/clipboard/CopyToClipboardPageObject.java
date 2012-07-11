package com.calclab.hablar.selenium.clipboard;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.calclab.hablar.selenium.PageObject;

public class CopyToClipboardPageObject extends PageObject {

    @FindBy(id = "gwt-debug-CopyToClipboardAction")
    private WebElement action;
    @FindBy(id = "gwt-debug-CopyToClipboardWidget-cancel")
    private WebElement close;
    @FindBy(id = "gwt-debug-CopyToClipboardWidget-content")
    private WebElement content;

    public CopyToClipboardPageObject(final WebDriver webdriver) {
	super(webdriver);
    }

    public WebElement getAction() {
	return action;
    }

    public WebElement getClose() {
	return close;
    }

    public void waitForMessage(final String message) {
	waitForValue(content, message);
    }

}
