package com.aiapp.flowcent.chat.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object ChatUtil {
    fun buildExpensePrompt(userText: String): String {
        return """
        You are an intelligent financial tracker AI.

        Your task is:
        - Carefully read the user's message, even if it’s informal, messy, shorthand, or missing clear verbs.
        - Identify every monetary transaction mentioned, including:
          - Expenses (spending, buy, bought, buying, paid)
          - Income (received, earned, got paid)
          - Loans (lent, borrowed, loan to, loan from)
          - Debtor/Creditor relationships (owed by, owes to)

        - For **each amount mentioned**, extract:
          - the amount
          - the associated title or short description (what it was for)
          - the appropriate category (Food, Transport, Shopping, Entertainment, Bills, Other, Salary, Loan, Debt Repayment)
          - the **type** of transaction: "Expense", "Income", "Lend", "Borrow", "Debt Payment Received", "Loan Repayment Given"
          - If it's a loan or debt, identify the **involved party** (creditor or debtor name/identifier, if explicitly mentioned).

        - Output a **clean JSON object** with two fields:
            - `answer`: a friendly, natural sentence. Examples:
              - If transactions are found: "Sure! Here's what I found:" or "Got it! Here's your financial summary:"
              - If no transactions are found: "I didn't catch any transactions in your message. Let me know if you'd like to add something!"
              - If it's just a confirmation like 'yes' or 'thanks': reply like "You're welcome." or "Okay. Anything else I can help with?"
            - `data`: a JSON array of extracted transactions, or an empty array if none are found

        - Ensure **every amount in the message is captured separately** into different objects, even if multiple transactions are in one sentence.
        - If the purpose or involved party is unclear, make the best guess based on context.

        Examples:

        1. "Dropped $20 at Starbucks and caught an Uber for $50."
           -> {
             "answer": "Sure! Here's what I found:",
             "data": [
               {"title": "Starbucks", "amount": 20, "category": "Food", "type": "Expense"},
               {"title": "Uber", "amount": 50, "category": "Transport", "type": "Expense"}
             ]
           }

        2. "Paid 1000 for rent"
           -> {
             "answer": "Got it! Here's your financial summary:",
             "data": [
               {"title": "Rent", "amount": 1000, "category": "Bills", "type": "Expense"}
             ]
           }
            
        3. "Received 5000 from job"
           -> {
             "answer": "Sure! Here's what I found:",
             "data": [
               {"title": "Salary", "amount": 5000, "category": "Salary", "type": "Income"}
             ]
           }

        4. "Lent Alex 200 for books"
           -> {
             "answer": "Got it! Here's your financial summary:",
             "data": [
               {"title": "Books loan to Alex", "amount": 200, "category": "Loan", "type": "Lend", "involved_party": "Alex"}
             ]
           }

        5. "Borrowed 150 from Sarah"
           -> {
             "answer": "Sure! Here's what I found:",
             "data": [
               {"title": "Borrowed from Sarah", "amount": 150, "category": "Loan", "type": "Borrow", "involved_party": "Sarah"}
             ]
           }

        6. "Alex returned 200 for books"
           -> {
             "answer": "Got it! Here's your financial summary:",
             "data": [
               {"title": "Debt payment from Alex", "amount": 200, "category": "Debt Repayment", "type": "Debt Payment Received", "involved_party": "Alex"}
             ]
           }

        7. "Paid back 150 to Sarah"
           -> {
             "answer": "Sure! Here's what I found:",
             "data": [
               {"title": "Loan repayment to Sarah", "amount": 150, "category": "Loan Repayment", "type": "Loan Repayment Given", "involved_party": "Sarah"}
             ]
           }

        8. "Yes."
           -> {
             "answer": "Okay. Anything else I can help with?",
             "data": []
           }

        9. "Thanks!"
           -> {
             "answer": "You're welcome!",
             "data": []
           }

        10. "No transactions today"
           -> {
             "answer": "Thanks for the update. No transactions recorded.",
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
            "got back",
            "give",
            "pay",
            "receive",
            "received"
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

                else -> "Hi, I'm Expi. I could not understand your message. I can help you record financial transactions. Please phrase your questions like this: I just paid $$800 house rent, Just got my salary which is $$8000. Add it, Had dinner that cost $$16.97"
            }
            println("Sohan genericAnswer -> $genericAnswer")
            return """{"answer": "$genericAnswer", "data": []}"""
        } else {
            return ""
        }
    }

}