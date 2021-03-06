package com.rocklobstre.coolRecyclerview.domain.model

import com.google.gson.annotations.SerializedName

/**
 * @author Matin Salehi on 30/12/2017.
 */
data class User (
        @SerializedName("id") val id: Int,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String,
        @SerializedName("avatar") val avatar: String?
)