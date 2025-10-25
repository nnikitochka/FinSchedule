package ru.nnedition.finschedule.bot;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.bot.event.CallbackQueryEvent;
import ru.nnedition.finschedule.bot.event.CommandReceiveEvent;
import ru.nnedition.finschedule.bot.event.MessageReceiveEvent;
import ru.nnedition.finschedule.utils.UpdateParser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class UpdateConsumer implements LongPollingUpdateConsumer {
    private final TelegramBot bot;
    public UpdateConsumer(@NotNull TelegramBot bot) {
        this.bot = bot;
        this.updateThread = new Thread(() -> {
            while (!updateThread.isInterrupted()) {
                System.out.println("Ожидание нового апдейта...");
                final Update update;
                try {
                    update = this.queue.take();
                } catch (InterruptedException e) {
                    break;
                }

                if (update.hasMessage() && update.getMessage().hasText()) {
                    var message = update.getMessage();

                    if (message.isCommand()) {
                        var text = message.getText();
                        var parts = text.substring(1).split(" ");
                        var commandLabel = parts[0];
                        var args = Arrays.copyOfRange(parts, 1, parts.length);
                        this.bot.onCommandReceived(new CommandReceiveEvent(
                                update.getUpdateId(),
                                message.getMessageId(),
                                message.getDate().longValue(),
                                message.getFrom(),
                                message.getChat(),
                                message.getText(),
                                commandLabel,
                                args
                        ));
                    } else {
                        this.bot.onMessageReceived(new MessageReceiveEvent(
                                update.getUpdateId(),
                                message.getMessageId(),
                                message.getDate().longValue(),
                                message.getFrom(),
                                message.getChat(),
                                message.getText()
                        ));
                    }
                    continue;
                }

                if (update.hasCallbackQuery()) {
                    this.bot.onCallbackQuery(new CallbackQueryEvent(
                            update.getUpdateId(),
                            update.getCallbackQuery()
                    ));
                    continue;
                }

                System.out.println(update);
                UpdateParser.printFormated(update);
            }
        }, "UpdateConsumerThread");
    }

    private Thread updateThread = null;
    LinkedBlockingQueue<Update> queue = new LinkedBlockingQueue<>();

    public void start() {
        this.updateThread.start();
    }

    @Override
    public void consume(List<Update> updates) {
        this.queue.addAll(updates);
    }

    public void interrupt() {
        this.updateThread.interrupt();
    }
}
