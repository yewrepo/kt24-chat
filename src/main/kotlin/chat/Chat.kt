package chat

import Message

data class Chat(
    val id: Int,
    val firstUserId: Int,
    val secondUserId: Int,
    val messages: MutableList<Message> = mutableListOf()
)