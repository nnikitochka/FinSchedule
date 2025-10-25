package ru.nnedition.finschedule.bot.menu.admin;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.nnedition.finschedule.FinSchedule;
import ru.nnedition.finschedule.bot.callback.CallbackData;
import ru.nnedition.finschedule.bot.callback.CallbackHandler;
import ru.nnedition.finschedule.bot.menu.Menu;
import ru.nnedition.finschedule.bot.menu.context.EditMenuContext;
import ru.nnedition.finschedule.bot.menu.context.MenuContext;
import ru.nnedition.finschedule.bot.monitor.ResourceMonitor;
import ru.nnedition.finschedule.utils.SendingUtils;

import java.util.Map;

public final class AdminMenu extends Menu {
    @Override
    public boolean open(MenuContext context) {
        SendMessage message = SendMessage.builder()
                .chatId(context.getChat().getId())
                .text(ResourceMonitor.getText())
                .replyMarkup(this.getKeyboard())
                .build();

        var msg = SendingUtils.tryExecute(message);

        return true;
    }

    @Override
    public boolean update(EditMenuContext context) {
        final var edit = EditMessageText.builder()
                .chatId(context.getChat().getId())
                .messageId(context.getMessageId())
                .text(ResourceMonitor.getText())
                .replyMarkup(this.keyboard)
                .build();

        return SendingUtils.tryEdit(edit);
    }

    private final InlineKeyboardMarkup keyboard = getKeyboard();

    private InlineKeyboardMarkup getKeyboard() {
        var updateButton = InlineKeyboardButton.builder()
                .text(FinSchedule.getConfig().refreshButton)
                .callbackData(new CallbackData("menuUpdate", Map.of("menuId", this.getId())).serialize())
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(
                        updateButton
                ))
                .build();
    }
}
