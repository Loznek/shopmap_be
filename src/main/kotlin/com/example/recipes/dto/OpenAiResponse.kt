package com.example.recipes.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpenAiRequest(
    val model: String,
    val messages: List<OpenAiMessage>,
    val temperature: Double = 0.2
)

@Serializable
data class OpenAiMessage(
    val role: String,
    val content: String
)


@Serializable
data class OpenAiResponse(
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val message: Message
)

@Serializable
data class Message(
    val content: String
)