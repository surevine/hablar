package com.calclab.hablar.selenium.editbuddy;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.calclab.hablar.selenium.PageObject;

public class EditBuddyPageObject extends PageObject {

    @FindBy(id = "gwt-debug-EditBuddyWidget-nickname")
    private WebElement newNickname;
    @FindBy(id = "gwt-debug-EditBuddyWidget-save")
    private WebElement save;

    public EditBuddyPageObject(final WebDriver webdriver) {
	super(webdriver);
    }

    public WebElement getNewNickName() {
	return newNickname;
    }

    public WebElement getSave() {
	return save;
    }
}
