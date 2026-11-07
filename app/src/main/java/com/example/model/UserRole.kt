package com.example.model

enum class AppRole(
    val title: String,
    val subtitle: String,
    val brandLabel: String,
    val defaultUserName: String,
    val defaultCompany: String
) {
    FIND_YANTRA(
        title = "FindYantra",
        subtitle = "Rent Heavy Equipment",
        brandLabel = "findyantra",
        defaultUserName = "Sanjay Deshmukh",
        defaultCompany = "Deshmukh Infrastructure"
    ),
    YANTRA_FLEET(
        title = "YantraFleet",
        subtitle = "Fleet & Operator Management",
        brandLabel = "yantrafleet",
        defaultUserName = "Ajay Patil",
        defaultCompany = "PATIL EARTHMOVERS"
    ),
    YANTRA_STORE(
        title = "YantraStore",
        subtitle = "Parts, Materials & Services",
        brandLabel = "yantrastore",
        defaultUserName = "Deccan Heavy Works",
        defaultCompany = "Deccan Heavy Spares & Workshop"
    )
}
