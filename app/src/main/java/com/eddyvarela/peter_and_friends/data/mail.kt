package com.eddyvarela.peter_and_friends.data

import java.util.*

data class Mail
    (var uid: String = "",
     var date: Date = Date(1,1,1,1,1,1),
     var author: String = "",
     var receiver: String = "",
     var title: String = "",
     var body: String = "",
     var imgUrl: String = "")