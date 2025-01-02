package com.library.pages;

import com.library.utility.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public  class BasePage {

    public BasePage() {

        PageFactory.initElements(Driver.getDriver(),this);
    }

    @FindBy(xpath = "//span[.='Books']")
    public WebElement books;

    @FindBy(xpath = "//span[.='Users']")
    public WebElement users;

    @FindBy(xpath = "//a[@id='navbarDropdown']")
    public WebElement userIcon;

    @FindBy(xpath = "//a[contains(.,'Library')]")
    public WebElement dashBoard;


    @FindBy(xpath = "//a[@id='navbarDropdown']")
    public WebElement userName;


    }



