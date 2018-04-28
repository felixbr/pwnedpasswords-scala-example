import PwnedPasswordsHandler.CheckPasswordResult
import monix.eval.Task

import scala.concurrent.duration._
import scala.io.StdIn

/**
  * Entry point for the application.
  *
  * Use `sbt run` from the commandline to run it.
  */
object Main extends App {

  // scheduler for demo use case
  import monix.execution.Scheduler.Implicits.global

  // wire up dependencies
  val pwnedPasswordsClient  = new PwnedPasswordsClientImpl
  val pwnedPasswordsHandler = new PwnedPasswordsHandlerImpl(pwnedPasswordsClient)

  def readPasswordInput(): Task[String] = Task {
    StdIn.readLine("Enter password to check: ")
  }

  def reportResult(result: CheckPasswordResult): Task[Unit] = Task {
    val message = result match {
      case CheckPasswordResult.PasswordIsNotPwned =>
        s"Not Pwned: The password wasn't found in the Pwned Passwords database"

      case CheckPasswordResult.PasswordIsPwned(count) =>
        s"PWNED: The password has previously appeared $count times in data breaches. Don't use it!"
    }

    println(message + "\n")
  }

  def main: Task[Unit] =
    for {
      input <- readPasswordInput()
      _ = if (input.isEmpty) System.exit(0) // yolo abort
      result <- pwnedPasswordsHandler.checkPassword(input)
      _      <- reportResult(result)
      _      <- main // infinite loop
    } yield ()

  // run the main loop
  main.runSyncUnsafe(Duration.Inf)
}
