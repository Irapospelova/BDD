package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private final String balanceBegining = "баланс";
    private final String balanceEnding = "р.";
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$(".list__item div");

    public DashboardPage() {
        heading.shouldBe(Condition.visible);
    }


    private int extractBalance(String text) {

        var begining = text.indexOf(balanceBegining);
        var ending = text.indexOf(balanceEnding);
        var value = text.substring(begining + balanceBegining.length(), ending);
        return Integer.parseInt(value);
    }

    public int getFirstCardBalance(DataHelper.CardInfo cardInfo) {

        var text = cards.findBy(Condition.text(cardInfo.getCardNumber().substring(0001))).getText();
        return extractBalance(text);
    }

   public int getSecondCardBalance(DataHelper.CardInfo cardInfo) {
        var text = cards.findBy(Condition.text(cardInfo.getCardNumber().substring(0002))).getText();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        cards.findBy(Condition.attribute("data-test-id=action-deposit", cardInfo.getTestId()))
                .$("button").click();
        return new TransferPage();
    }

}
