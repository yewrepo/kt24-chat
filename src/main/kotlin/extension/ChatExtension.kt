package extension

import chat.Chat
import chat.ChatHolder

fun MutableList<ChatHolder>.clearIfNoMessages(chatId: Int) = this.removeIf {
    it.chat.id == chatId && it.chat.messages.size == 0
}

fun MutableList<ChatHolder>.containsChat(firstUserId: Int, secondUserId: Int): Boolean {
    this.forEach { holder ->
        if (holder.hasUserId(firstUserId) && holder.hasUserId(secondUserId)
        ) {
            return true
        }
    }
    return false
}

fun MutableList<ChatHolder>.getChat(firstUserId: Int, secondUserId: Int): Chat? = this.find { holder ->
    holder.hasUserId(firstUserId) && holder.hasUserId(secondUserId)
}?.chat

fun MutableList<ChatHolder>.getChat(chatId: Int): Chat? = this.find { holder ->
    holder.chat.id == chatId
}?.chat