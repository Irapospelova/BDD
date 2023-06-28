package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class TransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        LoginPage loginPage = open("http://localhost:9999/", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferFromFirstToSecond() {

        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getFirstCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getSecondCardBalance(secondCardInfo);
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceOfFirstCard = firstCardBalance - amount;
        var expectedBalanceOfSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        var firstCardActualBalance = dashboardPage.getFirstCardBalance(firstCardInfo);
        var secondCardActualBalance = dashboardPage.getSecondCardBalance(secondCardInfo);
        assertEquals(expectedBalanceOfFirstCard, firstCardActualBalance);
        assertEquals(expectedBalanceOfSecondCard, secondCardActualBalance);


    }

    @Test
    void transferShouldFail() {

        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getFirstCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getSecondCardBalance(secondCardInfo);
        var amount = generateInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeAnyTransfer(String.valueOf(amount), secondCardInfo);
        transferPage.transferFail("Сумма перевода превышает остаток средств на карте");
        var firstCardActualBalance = dashboardPage.getFirstCardBalance(firstCardInfo);
        var secondCardActualBalance = dashboardPage.getSecondCardBalance(secondCardInfo);
        assertEquals(firstCardBalance, firstCardActualBalance);
        assertEquals(secondCardBalance, secondCardActualBalance);

    }

}

