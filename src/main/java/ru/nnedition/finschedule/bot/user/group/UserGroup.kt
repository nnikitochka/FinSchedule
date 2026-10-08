package ru.nnedition.finschedule.bot.user.group

@JvmRecord
data class UserGroup(
    val name: String,
    val subgroup: Int?
) {
    fun hasSubgroup(): Boolean {
        return subgroup != null
    }

    override fun toString(): String {
        return if (hasSubgroup()) "$name.$subgroup" else name
    }
}
