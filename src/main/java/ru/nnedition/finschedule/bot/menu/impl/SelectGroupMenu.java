package ru.nnedition.finschedule.bot.menu.impl;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
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
import ru.nnedition.finschedule.schedule.groups.Group;
import ru.nnedition.finschedule.schedule.groups.GroupsData;
import ru.nnedition.finschedule.utils.SendingUtils;

import java.util.*;

public class SelectGroupMenu extends Menu {
    public SelectGroupMenu() {
        final var selectGroupHandler = new CallbackHandler("selectGroup") {
            @Override
            public void handle(CallbackData data, String callbackId, User from, Message message) {
                final var selectedGroup = data.get("grName");
                if (selectedGroup != null) {
                    final var group = FinSchedule.getSchedule().getGroupOrCreate(selectedGroup);
                    FinSchedule.getBot().getUsersManager().setUserGroup(from, group);
                }

                final var answer = AnswerCallbackQuery.builder()
                        .text("Вы выбрали группу: " + selectedGroup)
                        .callbackQueryId(callbackId)
                        .build();

                SendingUtils.tryExecute(answer);
            }
        };

        final var selectGroupPage = new CallbackHandler("selectGroupPage") {
            @Override
            public void handle(CallbackData data, String callbackId, User from, Message message) {
                final var pageStr = data.get("page");
                int page;
                try {
                    assert pageStr != null;
                    page = Integer.parseInt(pageStr);
                } catch (NumberFormatException e) {
                    return;
                }

                final var menu = FinSchedule.getBot().getMenuRegistry().getMenu(SelectGroupMenu.class);

                final var editContext = new EditContext(
                        message.getChat(),
                        from,
                        message.getMessageId(),
                        page
                );
                menu.edit(editContext);
            }
        };

        FinSchedule.getBot().getCallbackHandlerRegistry().register(
                selectGroupHandler,
                selectGroupPage
        );
    }

    @Override
    public boolean open(MenuContext context) {
        SendMessage message = SendMessage.builder()
                .chatId(context.getChat().getId())
                .text(FinSchedule.getConfig().selectGroup)
                .replyMarkup(this.getKeyboard(1))
                .build();

        var msg = SendingUtils.tryExecute(message);

        return true;
    }

    @Override
    public boolean edit(EditMenuContext context) {
        int page = 1;
        if (context instanceof EditContext editContext) {
            page = editContext.getPage();
        }

        final var edit = EditMessageText.builder()
                .chatId(context.getChat().getId())
                .messageId(context.getMessageId())
                .text(FinSchedule.getConfig().selectGroup)
                .replyMarkup(this.getKeyboard(page))
                .build();

        return SendingUtils.tryEdit(edit);
    }

    private InlineKeyboardMarkup getKeyboard(final int page) {
        Map<Integer, List<String>> groups = new HashMap<>();
        List<List<String>> chunked = new ArrayList<>();

        List<String> reversed = new ArrayList<>(FinSchedule.getSchedule().getGroups().stream().map(Group::name).toList());
        Collections.reverse(reversed);

        for (int i = 0; i < reversed.size(); i += 8) {
            int end = Math.min(i + 8, reversed.size());
            chunked.add(reversed.subList(i, end));
        }

        for (int i = 0; i < chunked.size(); i++) {
            groups.put(i + 1, chunked.get(i));
        }

        final List<InlineKeyboardRow> rows = new ArrayList<>();
        groups.get(page).forEach(group -> {
            final var groupButton = InlineKeyboardButton.builder()
                    .text(group)
                    .callbackData(new CallbackData("selectGroup", Map.of("grName", group)).serialize())
                    .build();

            rows.add(new InlineKeyboardRow(Collections.singletonList(groupButton)));
        });

        final var navigationRow = new InlineKeyboardRow();
        if (page > 1) {
            final var callbackData = new CallbackData(
                    "selectGroupPage",
                    Map.of("page", String.valueOf(page-1))
            ).serialize();

            navigationRow.add(
                    InlineKeyboardButton.builder()
                            .text(FinSchedule.getConfig().prevButton)
                            .callbackData(callbackData)
                            .build()
            );
        }
        if (page < groups.size()) {
            final var callbackData = new CallbackData(
                    "selectGroupPage",
                    Map.of("page", String.valueOf(page+1))
            ).serialize();

            navigationRow.add(
                    InlineKeyboardButton.builder()
                            .text(FinSchedule.getConfig().nextButton)
                            .callbackData(callbackData)
                            .build()
            );
        }

        rows.add(navigationRow);

        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public static class EditContext extends EditMenuContext {
        private final int page;

        public EditContext(
                @NotNull final Chat chat,
                @NotNull final User sender,
                final int messageId,
                final int page
        ) {
            super(chat, sender, messageId);
            this.page = page;
        }

        public int getPage() {
            return this.page;
        }
    }
}
