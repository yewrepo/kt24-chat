data class Message(
    val id: Int = 0,
    val ownerId: Int = 0,
    val text: String,
    var isRead: Boolean = false
)
