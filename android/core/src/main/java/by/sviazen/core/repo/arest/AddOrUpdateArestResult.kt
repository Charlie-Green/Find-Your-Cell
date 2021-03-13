package by.sviazen.core.repo.arest


sealed class AddOrUpdateArestResult {

    class ArestsIntersect(
        val intersectedId: Int
    ): AddOrUpdateArestResult()

    object NetworkError: AddOrUpdateArestResult()

    class Success(
        val arestPosition: Int
    ): AddOrUpdateArestResult()
}