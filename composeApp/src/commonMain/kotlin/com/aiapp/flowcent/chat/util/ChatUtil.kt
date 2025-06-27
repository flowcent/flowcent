package com.aiapp.flowcent.chat.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object ChatUtil {
    fun buildExpensePrompt(userText: String): String {
        return """
        You are an intelligent expense tracker AI.

        Your task is:
        - Carefully read the user's message, even if it’s informal, messy, shorthand, or missing clear verbs.
        - Identify every monetary transaction (expenses, purchases, spending, buy, bought, buying) mentioned.
        - For **each amount mentioned**, extract:
          - the amount
          - the associated title or short description (what it was for)
          - the appropriate category (Food, Transport, Shopping, Entertainment, Bills, Other)

        - Output a **clean JSON object** with two fields:
            - `answer`: a friendly, natural sentence. Examples:
              - If expenses are found: "Sure! Here's what I found:" or "Got it! Here's your expense summary:"
              - If no expenses are found: "I didn't catch any expenses in your message. Let me know if you'd like to add something!"
              - If it's just a confirmation like 'yes' or 'thanks': reply like "You're welcome." or "Okay. Anything else I can help with?"
            - `data`: a JSON array of extracted expenses, or an empty array if none are found

        - Ensure **every amount in the message is captured separately** into different objects, even if multiple expenses are in one sentence.
        - If the expense purpose is unclear, make the best guess based on context.

        Examples:

        1. "Dropped $20 at Starbucks and caught an Uber for $50."
           -> {
             "answer": "Sure! Here's what I found:",
             "data": [
               {"title": "Starbucks", "amount": 20, "category": "Food"},
               {"title": "Uber", "amount": 50, "category": "Transport"}
             ]
           }

        2. "Paid 1000 for rent"
           -> {
             "answer": "Got it! Here's your expense summary:",
             "data": [
               {"title": "Rent", "amount": 1000, "category": "Bills"}
             ]
           }

        3. "Yes."
           -> {
             "answer": "Okay. Anything else I can help with?",
             "data": []
           }

        4. "Thanks!"
           -> {
             "answer": "You're welcome!",
             "data": []
           }

        5. "No expenses today"
           -> {
             "answer": "Thanks for the update. No expenses recorded.",
             "data": []
           }

        IMPORTANT:
        - Always return a **valid JSON object** with `answer` and `data`.
        - Never explain your logic.
        - Do not include markdown or code block formatting.
        - Be concise and helpful in your response.

        User: "$userText"
    """.trimIndent()
    }


    // Function to clean markdown code blocks from the response
    fun cleanJsonFromMarkdown(text: String): String {
        return text.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
    }

    private fun containsExpense(text: String): Boolean {
        val expenseRegex =
            Regex("""(?i)(\$|৳|₹|€)?\s?\d{1,3}(,\d{3})*(\.\d+)?\s?(taka|tk|usd|eur|rs|bucks|dollars|pounds)?""")
        val expenseKeywords = listOf(
            "spent",
            "paid",
            "bought",
            "shopping",
            "donated",
            "cost",
            "charged",
            "bill",
            "price",
            "fare",
            "ticket",
            "just had",
        )

        val numberMatch = expenseRegex.containsMatchIn(text)
        val keywordMatch = expenseKeywords.any { it in text.lowercase() }

        println("Sohan numberMatch $numberMatch")
        println("Sohan keywordMatch $keywordMatch")

        return numberMatch || keywordMatch
    }

    fun checkInvalidExpense(userText: String): String {
        println("Sohan containsExpense(userText)-> ${containsExpense(userText)}")
        if (!containsExpense(userText)) {
            // No expense found — return generic JSON directly
            val genericAnswer = when {
                userText.lowercase().contains("thank") -> "You're welcome!"
                userText.lowercase() in listOf(
                    "yes",
                    "yes.",
                    "okay",
                    "ok",
                    "sure"
                ) -> "Okay. Anything else I can help with?"

                userText.lowercase() in listOf(
                    "hello",
                    "hi.",
                    "hey",
                    "yo",
                    "bro",
                    "brother",
                    "dude",
                    "buddy"
                ) -> "Hi, I'm Expi. How can I help you?"

                else -> "Thanks for the update. No expenses recorded."
            }
            println("Sohan genericAnswer -> $genericAnswer")
            return """{"answer": "$genericAnswer", "data": []}"""
        } else {
            return ""
        }
    }


    /**
     * Formats the current local time into a string with the format "dd-MM-yyyy HH:mm:ss".
     *
     * @return A string representing the current time in "dd-MM-yyyy HH:mm:ss" format.
     */
    fun getCurrentFormattedDateTime(): String {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.monthNumber.toString().padStart(2, '0')
        val year = localDateTime.year.toString()
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val second = localDateTime.second.toString().padStart(2, '0')

        return "$day-$month-$year $hour:$minute:$second"
    }


}