package ru.nnedition.finschedule.bot.callback

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

open class CallbackData @JvmOverloads constructor(
    @JvmField
    val key: String,
    context: Map<String, String>? = null
) {
    private val context: MutableMap<String, String> = if (context != null) HashMap(context)
    else HashMap()

    fun getContext(): MutableMap<String, String> {
        return HashMap(this.context)
    }

    fun get(key: String): String? {
        return this.context[key]
    }

    /**
     * Сериализует callback данные в строку формата: id:key1=value1&key2=value2
     */
    fun serialize(): String {
        if (this.context.isEmpty()) {
            return this.key
        }

        val sb: StringBuilder = StringBuilder(this.key).append(KEY_SEPARATOR)
        this.context.forEach { (key: String?, value: String?) ->
            if (!sb.toString().endsWith(KEY_SEPARATOR)) {
                sb.append(CONTEXT_SEPARATOR)
            }
            sb.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                .append(CONTEXT_EQUALIZER)
                .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
        }

        return sb.toString()
    }

    override fun toString(): String {
        return "CallbackData(" +
                "key=\"" + key + '\"' +
                ", context=" + context +
                ')'
    }

    companion object {
        private const val KEY_SEPARATOR = ":"
        private const val CONTEXT_SEPARATOR = "&"
        private const val CONTEXT_EQUALIZER = "="

        /**
         * Десериализует строку в CallbackData
         */
        @JvmStatic
        fun parse(data: String): CallbackData {
            require(!(data == null || data.isEmpty())) { "Callback data cannot be empty" }

            val parts: Array<String> = data.split(KEY_SEPARATOR.toRegex(), limit = 2).toTypedArray()
            val id = parts[0]
            val context: MutableMap<String, String> = HashMap()

            if (parts.size > 1 && !parts[1].isEmpty()) {
                val pairs: Array<String> =
                    parts[1].split(CONTEXT_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (pair in pairs) {
                    val keyValue: Array<String> = pair.split(CONTEXT_EQUALIZER.toRegex(), limit = 2).toTypedArray()
                    if (keyValue.size == 2) {
                        val key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8)
                        val value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                        context.put(key, value)
                    }
                }
            }

            return CallbackData(id, context)
        }
    }
}
