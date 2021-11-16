data class Chat(
    val id: Int,
    val ownerId: Int,
    val messages: MutableList<Message> = mutableListOf()
)
