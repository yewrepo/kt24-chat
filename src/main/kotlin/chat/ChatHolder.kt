package chat

data class ChatHolder(
    var lastId: Int,
    val chat: Chat
) {

    fun chatId() = chat.id

    fun hasUserId(ownerId: Int): Boolean {
        return chat.firstUserId == ownerId || chat.secondUserId == ownerId
    }

    fun hasUnread(ownerId: Int): Int {
        return chat.messages
            .filter { message -> message.ownerId != ownerId }
            .toList()
            .filter { message -> !message.isRead }
            .toList().size
    }

    companion object {
        @JvmStatic
        fun create(chat: Chat): ChatHolder = ChatHolder(0, chat)
    }
}