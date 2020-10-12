package ir.teamtea.headersample.model

sealed class UiItems {
    data class XItems (var item : Item) : UiItems()
    data class XSeparator (val text : String) : UiItems()
}