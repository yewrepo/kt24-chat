package extension

import Message

fun List<Message>.isRead(ownerId: Int): List<Message> = this.onEach { message ->
    if (message.ownerId != ownerId) {
        message.isRead = true
    }
}