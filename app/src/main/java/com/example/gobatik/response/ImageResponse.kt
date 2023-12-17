package com.example.gobatik.response

data class ImageResponse(
	val data: Data,
	val status: Status
)

data class Status(
	val code: Int,
	val message: String
)

data class Data(
	val batik_name: String,
	val batik_desc: String
)

