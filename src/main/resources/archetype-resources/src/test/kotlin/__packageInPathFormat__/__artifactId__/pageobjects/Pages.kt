package ${groupId}.${artifactId}.pageobjects

enum class PageUrls(val subUrl: String) {
    HOME("/"),
    TERMS("/discolsure"),
    CONTACT("/contact");


    fun getFullUrl(baseUrl: String): String? {
        return baseUrl + subUrl
    }
}
