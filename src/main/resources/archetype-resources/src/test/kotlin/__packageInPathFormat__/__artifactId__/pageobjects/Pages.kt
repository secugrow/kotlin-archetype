package at.co.boris.secuton.pageobjects

enum class PageUrls(val subUrl: String) {
    HOME("/"),
    TEAM("/team"),
    TERMS("/discolsure"),
    CONTACT("/contact");


    fun getFullUrl(baseUrl: String): String? {
        return baseUrl + subUrl
    }
}