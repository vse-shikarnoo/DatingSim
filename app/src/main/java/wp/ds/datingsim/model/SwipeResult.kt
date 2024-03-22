package wp.ds.datingsim.model

enum class SwipeResult(val rgb:String) {
    LIKE("#00FF00"),
    DISLIKE("#FF0000"),
    SUPERLIKE("#0000FF"),
    INFO("#FFFF00"),
    NULL("#000000")
}