package com.eddyvarela.peter_and_friends.data

data class UserInformation (
    var iod: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var userName: String = "",
    var profilePicture: String = "",
    var description: String = "",
    var address: String = "",
    var rating: Int = 0
)