import com.google.gson.Gson;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends TelegramLongPollingBot {
    public String getBotUsername() {
        return "InformerCurrency_Bot";
    }

    public String getBotToken() {
        return "1724676619:AAGkYwVc_q3u8ljYnoFSd_krnHjXo5DS1a8";
    }

    boolean sumToValyuta = true;
    double rate1;
    String Ccy;
    String Ccy1;


    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = send(update.getMessage());
        ReplyKeyboardMarkup replyKeyboardFirst = new ReplyKeyboardMarkup();
        replyKeyboardFirst.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton usdToUzs = new KeyboardButton("USD -> UZS");
        KeyboardButton eurToUzs = new KeyboardButton("EUR -> UZS");
        KeyboardButton cnyToUzs = new KeyboardButton("CNY -> UZS");
        KeyboardButton uzsToUsd = new KeyboardButton("UZS -> USD");
        KeyboardButton uzsToEur = new KeyboardButton("UZS -> EUR");
        KeyboardButton uzsToCny = new KeyboardButton("UZS -> CNY");
        row1.add(usdToUzs);
        row1.add(uzsToUsd);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(eurToUzs);
        row2.add(uzsToEur);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(cnyToUzs);
        row3.add(uzsToCny);
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);

        replyKeyboardFirst.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardFirst);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage send(Message message) {
        SendMessage sendMessage = new SendMessage();
        Long chatId = message.getChatId();
        String text = message.getText();
        sendMessage.setChatId(chatId);
        double sum;
        Currency currency;
        switch (text) {
            case "/start":
                sendMessage.setText("Valyuta konvertori botiga xush kelibsiz.\nValyutani tanlang.");
                break;
            case "UZS -> USD":
                sumToValyuta = true;
                Ccy = "usd";
                rate1 = 1;
                sendMessage.setText("Summani kiriting\n(1 USD=" + Objects.requireNonNull(getRate(Ccy.toUpperCase())).getRate() + " UZS)");
                break;
            case "UZS -> EUR":
                sumToValyuta = true;
                Ccy = "eur";
                rate1 = 1;
                sendMessage.setText("Summani kiriting\n(1 EUR=" + Objects.requireNonNull(getRate(Ccy.toUpperCase())).getRate() + " UZS)");
                break;
            case "UZS -> CNY":
                sumToValyuta = true;
                Ccy = "cny";
                rate1 = 1;
                sendMessage.setText("Summani kiriting\n(1 CNY=" + Objects.requireNonNull(getRate(Ccy.toUpperCase())).getRate() + " UZS)");
                break;
            case "USD -> UZS":
                sumToValyuta = false;
                Ccy = "usd";
                rate1 = 1;
                sendMessage.setText("Summani kiriting\n(1 USD=" + Objects.requireNonNull(getRate(Ccy.toUpperCase())).getRate() + " UZS)");
                break;
            case "EUR -> UZS":
                sumToValyuta = false;
                Ccy = "eur";
                rate1 = 1;
                sendMessage.setText("Summani kiriting\n(1 EUR=" + Objects.requireNonNull(getRate(Ccy.toUpperCase())).getRate() + " UZS)");
                break;
            case "CNY -> UZS":
                sumToValyuta = false;
                Ccy = "cny";
                rate1 = 1;
                sendMessage.setText("Summani kiriting\n(1 CNY=" + Objects.requireNonNull(getRate(Ccy.toUpperCase())).getRate() + " UZS)");
                break;
            case "yuan->usd":
                sumToValyuta = true;
                Ccy1 = "cny";
                Ccy = "usd";
                rate1 = Double.parseDouble(Objects.requireNonNull(getRate(Ccy1.toUpperCase())).getRate());
                sendMessage.setText("Summani kiriting\n(1 CNY=" + Double.parseDouble(Objects.requireNonNull(getRate(Ccy1.toUpperCase())).getRate()) / Double.parseDouble(Objects.requireNonNull(getRate(Ccy.toUpperCase())).getRate()) + " USD)");
                break;
            case "usd->yuan":
                sumToValyuta = false;
                Ccy1 = "cny";
                Ccy = "usd";
                rate1 = Double.parseDouble(Objects.requireNonNull(getRate(Ccy1.toUpperCase())).getRate());
                sendMessage.setText("Summani kiriting\n(1 USD=" + Double.parseDouble(Objects.requireNonNull(getRate(Ccy.toUpperCase())).getRate()) / Double.parseDouble(Objects.requireNonNull(getRate(Ccy1.toUpperCase())).getRate()) + " CNY)");
                break;
            default:
                try {
                    sum = Double.parseDouble(text);
                    currency = getRate(Ccy.toUpperCase());
                    double rate = Double.parseDouble(Objects.requireNonNull(currency).getRate());
                    double result;
                    if (sumToValyuta) {
                        result = sum * rate1 / rate;
                        sendMessage.setText(result + " " + currency.getCcyNmUZ());
                    }
                    if (!sumToValyuta) {
                        result = sum * rate / rate1;
                        sendMessage.setText(String.valueOf(result));
                    }


                } catch (Exception e) {
                    sendMessage.setText("Неверный ввод");
                }
                break;

        }
        return sendMessage;
    }

    private Currency getRate(String ccy) {
        Gson gson = new Gson();
        try {

            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
            URLConnection urlConnection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            Currency[] currencies = gson.fromJson(reader, Currency[].class);
            for (Currency currency : currencies) {
                if (currency.getCcy().equals(ccy)) {
                    return currency;
                }
            }


        } catch (
                IOException malformedURLException) {
            malformedURLException.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            api.registerBot(new Main());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }
}
