package com.calclab.hablar.selenium.vcard;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.calclab.hablar.selenium.PageObject;

public class OtherVCardPageObject extends PageObject implements VCardPageObject {

    @FindBy(id = "gwt-debug-OtherVCardWidget-name")
    WebElement name;

    @FindBy(id = "gwt-debug-OtherVCardWidget-nickName")
    WebElement nickName;

    @FindBy(id = "gwt-debug-OtherVCardWidget-familyName")
    WebElement familyName;

    @FindBy(id = "gwt-debug-OtherVCardWidget-givenName")
    WebElement givenName;

    @FindBy(id = "gwt-debug-OtherVCardWidget-middleName")
    WebElement middleName;

    @FindBy(id = "gwt-debug-OtherVCardWidget-organizationName")
    WebElement organizationName;

    @FindBy(id = "gwt-debug-OtherVCardWidget-email")
    WebElement email;

    @FindBy(id = "gwt-debug-OtherVCardWidget-homepage")
    WebElement homepage;

    public OtherVCardPageObject(final WebDriver webdriver) {
	super(webdriver);
    }

    @Override
    public WebElement getEmail() {
	return email;
    }

    @Override
    public WebElement getFamilyName() {
	return familyName;
    }

    @Override
    public WebElement getGivenName() {
	return givenName;
    }

    @Override
    public WebElement getHomepage() {
	return homepage;
    }

    @Override
    public WebElement getMiddleName() {
	return middleName;
    }

    @Override
    public WebElement getName() {
	return name;
    }

    @Override
    public WebElement getNickName() {
	return nickName;
    }

    @Override
    public WebElement getOrganizationName() {
	return organizationName;
    }
}
