package com.calclab.hablar.selenium.search;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.FindBy;

import com.calclab.hablar.core.client.Idify;
import com.calclab.hablar.selenium.PageObject;

public class SearchPageObject extends PageObject {
    public static final String GWT_DEBUG_SEARCH_LOGIC_REMOVE_ITEM = "gwt-debug-SearchLogic-remove-item";

    public static final String GWT_DEBUG_SEARCH_LOGIC_ADD_ITEM = "gwt-debug-SearchLogic-add-item";

    @FindBy(id = "gwt-debug-SearchWidget-term")
    private WebElement term;

    @FindBy(id = "gwt-debug-HeaderWidget-HablarSearch-1")
    private WebElement header;

    @FindBy(id = "gwt-debug-SearchWidget-message")
    private WebElement message;

    @FindBy(id = "gwt-debug-SearchWidget-search")
    private WebElement searchButton;

    @FindBy(id = "gwt-debug-HablarLogic-searchAction")
    private WebElement searchAction;

    @FindBy(id = GWT_DEBUG_SEARCH_LOGIC_REMOVE_ITEM)
    private WebElement searchRemoveBuddyAction;

    @FindBy(id = GWT_DEBUG_SEARCH_LOGIC_ADD_ITEM)
    private WebElement searchAddBuddyAction;

    @FindBy(id = "gwt-debug-SearchLogic-chat")
    private WebElement searchChatAction;

    public SearchPageObject(final WebDriver webdriver) {
	super(webdriver);
    }

    private WebElement findJid(final String jid) {
	return findElement(new ByIdOrName("gwt-debug-" + Idify.uriId(jid) + "-search-menu"));
    }

    public WebElement getAction() {
	return searchAction;
    }

    public WebElement getAddBuddyAction() {
	return searchAddBuddyAction;
    }

    public WebElement getChat(final String jid) {
	return findElement(new ByIdOrName("gwt-debug-HeaderWidget-Chat-" + Idify.uriId(jid)));
    }

    public WebElement getChatAction() {
	return searchChatAction;
    }

    public WebElement getHeader() {
	return header;
    }

    public WebElement getRemoveBuddyAction() {
	return searchRemoveBuddyAction;
    }

    public WebElement getResultMenu(final String jid) {
	return findJid(jid);
    }

    public WebElement getSearchButton() {
	return searchButton;
    }

    public WebElement getTerm() {
	return term;
    }

    public WebElement Message() {
	return message;
    }

    public void waitForMenuAddAction() {
	waitForId(GWT_DEBUG_SEARCH_LOGIC_ADD_ITEM);
    }

    public void waitForMenuRemoveAction() {
	waitForId(GWT_DEBUG_SEARCH_LOGIC_REMOVE_ITEM);
    }

    public void waitForResult(final String resultsMsg) {
	waitFor(message, resultsMsg);
    }

    public void waitForResultMenu(final String jid) {
	waitForId("gwt-debug-" + Idify.uriId(jid) + "-search-menu");
    }

}
