import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class OrderingACardTest {

    @BeforeEach
    @Test
    void shouldSendARequest() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick().sendKeys("Del");
        form.$("[data-test-id=date] input").setValue(getDate());
        form.$("[data-test-id=name] input").setValue("Филипп Преображенский");
        form.$("[data-test-id=phone] input").setValue("+79788956483");
        form.$("[data-test-id=agreement]").click();
        form.$("[class=button__text]").click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));

    }

    public String getDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, +3);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(calendar.getTime());
    }
}
