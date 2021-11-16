import exception.ChatNotFoundException
import exception.MessageNotFoundException
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class ChatServiceTest {

    @Test
    fun addMessageTest() {
        val firstUser = 1
        val secondUser = 2
        val thirdUser = 3

        ChatService.addMessage(firstUser, secondUser, Message(ownerId = 1, text = "Hello 2"))
        ChatService.addMessage(secondUser, firstUser, Message(ownerId = 2, text = "Hello 1"))

        ChatService.addMessage(secondUser, thirdUser, Message(ownerId = 2, text = "test"))
        ChatService.addMessage(secondUser, thirdUser, Message(ownerId = 2, text = "test"))
        ChatService.addMessage(thirdUser, secondUser, Message(ownerId = 2, text = "test"))

        assertEquals(ChatService.getChats(firstUser).size, 1)
        assertEquals(ChatService.getChats(secondUser).size, 2)
    }

    @Test
    fun removeMessagesTest() {
        val firstMessageId = ChatService.addMessage(1, 2, Message(ownerId = 1, text = "Hello secondUser"))
        val secondMessageId = ChatService.addMessage(2, 1, Message(ownerId = 2, text = "Hello firstUser"))
        val chat = ChatService.getChatByUsers(1, 2)
        ChatService.removeMessage(firstMessageId, chat!!.id)
        assertEquals(chat.messages.size, 1)
        ChatService.removeMessage(secondMessageId, chat.id)
        assertEquals(ChatService.chatCount(), 0)
    }

    @Test(expected = MessageNotFoundException::class)
    fun removeMessagesTest_shouldThrow() {
        ChatService.addMessage(1, 2, Message(ownerId = 1, text = "Hello secondUser"))
        val chat = ChatService.getChatByUsers(1, 2)
        ChatService.removeMessage(24, chat!!.id)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getChatTestTest_shouldThrow() {
        ChatService.addMessage(1, 2, Message(ownerId = 1, text = "Hello 2"))
        val chats = ChatService.getChats(1)

        ChatService.getMessages(22, chats[0].id, 0, 5)
    }

    @Test
    fun getUserChatsCount() {
        ChatService.addMessage(1, 2, Message(ownerId = 1, text = "Hello secondUser"))
        ChatService.addMessage(2, 1, Message(ownerId = 2, text = "Hello firstUSer"))
        assertEquals(ChatService.getChats(1).size, 1)

        ChatService.addMessage(1, 3, Message(ownerId = 1, text = "Hello!"))
        assertEquals(ChatService.getChats(1).size, 2)
    }

    @Test
    fun getUnreadChatsCount() {
        val ownerId = 1
        val companionId = 2
        for (i in 0..20) {
            ChatService.addMessage(ownerId, companionId, Message(ownerId = 1, text = "Hello $i"))
            ChatService.addMessage(companionId, ownerId, Message(ownerId = 2, text = "Hello $i"))
        }
        val chats = ChatService.getChats(ownerId)

        ChatService.getMessages(ownerId, chats[0].id, 0, 5)
        assertEquals(ChatService.getUnreadChatsCount(ownerId), 1)

        ChatService.getMessages(ownerId, chats[0].id, 0, 99)
        assertEquals(ChatService.getUnreadChatsCount(ownerId), 0)

        ChatService.getMessages(companionId, ChatService.getChats(companionId)[0].id, 0, 5)
        assertEquals(ChatService.getUnreadChatsCount(companionId), 1)

        ChatService.getMessages(companionId, ChatService.getChats(companionId)[0].id, 0, 42)
        assertEquals(ChatService.getUnreadChatsCount(companionId), 0)
    }

    @Before
    fun reset() {
        ChatService.clear()
    }
}