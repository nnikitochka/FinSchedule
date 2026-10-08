package ru.nnedition.finschedule.bot.callback.impl

import ru.nnedition.finschedule.FinSchedule
import ru.nnedition.finschedule.bot.callback.CallbackData
import ru.nnedition.finschedule.bot.menu.Menu

class MenuUpdateCallbackData(menu: Menu) : CallbackData(
    "mnUpd",
    mapOf("mnId" to menu.id)
) {
    companion object {
        @JvmStatic
        fun getMenu(data: CallbackData): Menu? {
            val menuId = data.get("mnId")
            requireNotNull(menuId) { "не найдено айди меню в данных коллбэка" }
            return FinSchedule.getBot().menuRegistry.getMenu(menuId)
        }
    }
}
