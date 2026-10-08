package ru.nnedition.finschedule.bot.callback

import ru.nnedition.finschedule.bot.callback.impl.MenuUpdateCallbackHandler
import ru.nnedition.logger.Logger

class CallbackHandlerRegistry {
    private val handlers: MutableMap<String, CallbackHandler> = HashMap()
    fun getHandlers(): MutableCollection<CallbackHandler> {
        return this.handlers.values
    }

    fun getHandler(key: String): CallbackHandler? {
        return this.handlers[key]
    }

    fun register(handler: CallbackHandler) {
        if (this.handlers.containsKey(handler.key)) {
            logger.error("Ошибка при регистрации обработчика коллбэка: обработчик с ключом " + handler.key + " уже зарегистрирован!")
            return
        }

        this.handlers[handler.key] = handler
    }

    fun register(vararg handlers: CallbackHandler) {
        for (handler in handlers) {
            this.register(handler)
        }
    }

    fun unregister(handler: CallbackHandler) {
        this.unregister(handler.key)
    }

    fun unregister(key: String) {
        this.handlers.remove(key)
    }

    fun registerDefaults() {
        this.register(
            MenuUpdateCallbackHandler()
        )
    }

    companion object {
        private val logger = Logger.getLogger(CallbackHandlerRegistry::class.java)
    }
}
