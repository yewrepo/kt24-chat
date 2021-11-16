import chat.Chat
import chat.ChatHolder
import exception.ChatNotFoundException
import exception.MessageNotFoundException
import extension.clearIfNoMessages
import extension.containsChat
import extension.getChat
import extension.isRead
import kotlin.streams.toList

object ChatService {

    private var chatId: Int = 0
    private val chats = mutableListOf<ChatHolder>()

    fun getChats(ownerId: Int): List<Chat> {
        return chats.filter { it.chat.firstUserId == ownerId || it.chat.secondUserId == ownerId }
            .stream()
            .map { it.chat }
            .toList()
    }

    fun chatCount(): Int = chats.size

    fun getUnreadChatsCount(ownerId: Int): Int {
        return chats.filter { it.hasUnread(ownerId) > 0 }
            .stream()
            .toList().size
    }

    fun removeMessage(messageId: Int, chatId: Int): Boolean {
        val chat = chats.getChat(chatId)
        if (chat != null) {
            val index = chat.messages.indexOfFirst { it.id == messageId }
            if (index >= 0) {
                chat.messages.removeAt(index)
                chats.clearIfNoMessages(chat.id)
                return true
            } else throw MessageNotFoundException()
        } else throw ChatNotFoundException()
    }

    fun addMessage(firstUserId: Int, secondUserId: Int, message: Message): Int {
        var chat = chats.getChat(firstUserId, secondUserId)
        if (chat == null) {
            chat = addChat(firstUserId, secondUserId)
        }
        return addMessage(chat.id, message)
    }

    fun getMessages(ownerId: Int, chatId: Int, startId: Int, count: Int): List<Message> {
        chats.find { holder ->
            holder.chatId() == chatId && holder.hasUserId(ownerId)
        }?.let { holder ->
            val chat = holder.chat
            val index = chat.messages.indexOfFirst { message ->
                message.id == startId
            }
            if (index >= 0) {
                val lastIndex = Math.min(index + count, chat.messages.size) - 1
                return chat.messages.slice(IntRange(index, lastIndex)).isRead(ownerId)
            } else throw MessageNotFoundException()
        } ?: throw ChatNotFoundException()
    }

    fun getChatByUsers(firstUserId: Int, secondUserId: Int): Chat? {
        return chats.getChat(firstUserId, secondUserId)
    }

    private fun addMessage(chatId: Int, message: Message): Int {
        chats.find { holder ->
            holder.chatId() == chatId
        }?.let { holder ->
            val id = holder.lastId++
            val chat = holder.chat
            val copied = message.copy(id = id)
            chat.messages.add(copied)
            return id
        } ?: throw ChatNotFoundException()
    }

    private fun addChat(ownerId: Int, companionId: Int): Chat {
        return if (chats.containsChat(ownerId, companionId)) {
            chats.getChat(ownerId, companionId)!!
        } else {
            val chatHolder = ChatHolder.create(Chat(chatId++, ownerId, companionId))
            chats.add(chatHolder)
            chatHolder.chat
        }
    }

    fun clear() {
        chatId = 0
        chats.clear()
    }


}