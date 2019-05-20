package com.eddyvarela.peter_and_friends.data



data class Post(
    var iod: String = "",
    var time:String="",
    var author: String = "",
    var title: String = "",
    var description: String = "",
    var dateOfJob:String = "",
    var imgURL:String = "",
    var payAmt:String =""
)

/*
* Eventually we are going to have more data features
*
* location and maps for each post
* Date for job request vs Date of posting
* imgURL
* tags for searching
* pay amt
*
*/
