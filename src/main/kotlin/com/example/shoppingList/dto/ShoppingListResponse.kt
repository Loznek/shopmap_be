import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListResponse(
    val id: Int,
    val userId: Int,
    val name: String,
    val items: List<ShoppingListItemResponse>
)

@Serializable
data class ShoppingListItemResponse(
    val itemId: Int,
    val shoppingItemName: String,
    val attributes: String
)