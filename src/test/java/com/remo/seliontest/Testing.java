package com.remo.seliontest;

import com.paypal.selion.annotations.WebTest;
import com.paypal.selion.platform.dataprovider.DataProviderFactory;
import com.paypal.selion.platform.dataprovider.DataResource;
import com.paypal.selion.platform.dataprovider.SeLionDataProvider;
import com.paypal.selion.platform.dataprovider.impl.FileSystemResource;
import com.paypal.selion.platform.dataprovider.impl.InputStreamResource;
import com.paypal.selion.platform.grid.Grid;
import com.remo.LoginInfoPage;
import com.remo.seliontest.dataobjects.LoginData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.remo.HomePage;

import java.io.FileInputStream;
import java.io.IOException;

public class Testing {
    private static final String URL = "https://www.gsmarena.com/";

    @DataProvider(name = "loginFailProvider")
    public Object[][] getLoginFailProvider() throws IOException {
        DataResource resource = new InputStreamResource(new FileInputStream("src/test/resources/testdata/loginFail.json"), LoginData.class, "json");
        SeLionDataProvider dataProvider = DataProviderFactory.getDataProvider(resource);
        return dataProvider.getAllData();
    }

    @DataProvider(name = "loginValidProvider")
    public Object[][] getLoginValidProvider() throws IOException {
        DataResource resource = new InputStreamResource(new FileInputStream("src/test/resources/testdata/loginValid.json"), LoginData.class, "json");
        SeLionDataProvider dataProvider = DataProviderFactory.getDataProvider(resource);
        return dataProvider.getAllData();
    }

    @Test(dataProvider = "loginFailProvider")
    @WebTest
    public void loginFail(LoginData data) {
        Grid.driver().get(URL);
        HomePage homePage = new HomePage();
        Assert.assertEquals(homePage.getActualPageTitle(), homePage.getExpectedPageTitle());
        homePage.validatePage();
        homePage.clickLoginButton();
        homePage.setEmailTextFieldValue(data.getEmail());
        homePage.setPasswordTextFieldValue(data.getPassword());
        homePage.clickSubmitButton();

        LoginInfoPage loginInfoPage = new LoginInfoPage();
        Assert.assertTrue(loginInfoPage
                .isTextPresentForTitleLabel("Login result"));
        Assert.assertTrue(loginInfoPage
                .isTextPresentForInfoLabel("Login failed.\nReason: User record not found."));
    }

    @Test(dataProvider = "loginValidProvider")
    @WebTest
    public void loginValid(LoginData data) {
        Grid.driver().get(URL);
        HomePage homePage = new HomePage();
        Assert.assertEquals(homePage.getActualPageTitle(), homePage.getExpectedPageTitle());
        homePage.validatePage();
        homePage.clickLoginButton();
        homePage.setEmailTextFieldValue(data.getEmail());
        homePage.setPasswordTextFieldValue(data.getPassword());
        homePage.clickSubmitButton();

        LoginInfoPage loginInfoPage = new LoginInfoPage();
        Assert.assertTrue(loginInfoPage
                .isTextPresentForTitleLabel("Login result"));
        Assert.assertTrue(loginInfoPage
                .isTextPresentForInfoLabel("Login successful.\nStand-by for redirect."));
    }
}