package misc

import kotlin.collections.set
import kotlin.fold
import kotlin.text.toDouble

object ErrorHandling {

    // First, we'll define our basic account class
    class BankAccount(val accountNumber: String, val owner: String, private var balance: Double) {
        fun getBalance(): Double {
            return balance
        }

        fun deposit(amount: Double): Double {
            if (amount <= 0) {
                throw kotlin.IllegalArgumentException("Deposit amount must be positive")
            }
            balance += amount
            return balance
        }

        fun withdraw(amount: Double): Double {
            if (amount <= 0) {
                throw kotlin.IllegalArgumentException("Withdrawal amount must be positive")
            }

            if (amount > balance) {
                throw InsufficientFundsException("Cannot withdraw $amount. Current balance is $balance")
            }

            balance -= amount
            return balance
        }
    }

    // Custom exception for insufficient funds
    class InsufficientFundsException(message: String) : Exception(message)

    // Custom exception for account not found
    class AccountNotFoundException(message: String) : Exception(message)

    // Bank service to manage accounts
    class BankService {
        private val accounts = mutableMapOf<String, BankAccount>()

        fun createAccount(accountNumber: String, owner: String, initialDeposit: Double): BankAccount {
            if (accounts.containsKey(accountNumber)) {
                throw kotlin.IllegalStateException("Account number $accountNumber already exists")
            }

            val account = BankAccount(accountNumber, owner, initialDeposit)
            accounts[accountNumber] = account
            return account
        }

        fun findAccount(accountNumber: String): BankAccount {
            return accounts[accountNumber] ?: throw AccountNotFoundException("Account $accountNumber not found")
        }

        fun transferMoney(fromAccountNumber: String, toAccountNumber: String, amount: Double): Boolean {
            try {
                val fromAccount = findAccount(fromAccountNumber)
                val toAccount = findAccount(toAccountNumber)

                fromAccount.withdraw(amount)
                toAccount.deposit(amount)

                return true
            } catch (e: AccountNotFoundException) {
                println("Transfer failed: ${e.message}")
                return false
            } catch (e: InsufficientFundsException) {
                println("Transfer failed: ${e.message}")
                return false
            } catch (e: Exception) {
                println("Transfer failed due to an unexpected error: ${e.message}")
                return false
            }
        }

        // Using Result class for functional error handling
        fun transferMoneyWithResult(fromAccountNumber: String, toAccountNumber: String, amount: Double): Result<Unit> {
            return try {
                val fromAccount = findAccount(fromAccountNumber)
                val toAccount = findAccount(toAccountNumber)

                fromAccount.withdraw(amount)
                toAccount.deposit(amount)

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        // Using nullable types for error handling
        fun findAccountSafely(accountNumber: String): BankAccount? {
            return accounts[accountNumber]
        }

        fun getAccountBalance(accountNumber: String): Double {
            val account = findAccountSafely(accountNumber)
            return account?.getBalance() ?: 0.0
        }

        // Using try-catch as an expression
        fun parseAmount(amountString: String): Double {
            return try {
                amountString.toDouble()
            } catch (e: NumberFormatException) {
                println("Invalid amount format: $amountString")
                0.0
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {

        val bankService = BankService()

        try {
            bankService.createAccount("123456", "Alice", 1000.0)
            bankService.createAccount("789012", "Bob", 500.0)
            println("Accounts created successfully")
        } catch (e: Exception) {
            println("Failed to create accounts: ${e.message}")
        }

        val transferResult = bankService.transferMoney("123456", "789012", 300.0)
        println("Transfer successful: $transferResult")

        val failedTransfer = bankService.transferMoney("123456", "789012", 2000.0)
        println("Transfer successful: $failedTransfer")

        try {
            val nonExistentAccount = bankService.findAccount("999999")
            println("Account found: ${nonExistentAccount.owner}")
        } catch (e: AccountNotFoundException) {
            println("Error: ${e.message}")
        }

        val balance = bankService.getAccountBalance("999999")
        println("Balance for non-existent account: $balance")

        val resultTransfer = bankService.transferMoneyWithResult("123456", "789012", 100.0)

        resultTransfer.fold(
            onSuccess = { println("Transfer completed successfully") },
            onFailure = { error -> println("Transfer failed: ${error.message}") }
        )

        val validAmount = bankService.parseAmount("123.45")
        println("Parsed amount: $validAmount")

        val invalidAmount = bankService.parseAmount("abc")
        println("Parsed invalid amount: $invalidAmount")
    }
}
