import PwnedPasswordsClient.CheckPasswordSHA1Response.PasswordEntry
import PwnedPasswordsClient._
import cats.instances.all._
import cats.syntax.all._
import models._
import monix.eval.Task
import scalaj.http._

trait PwnedPasswordsClient {

  /**
    * Check the Pwned Passwords database remotely wether it contains the password hash.
    *
    * The hash isn't sent completely but only the prefix (5 chars).
    * The response contains a list of password entries matching the prefix and you can
    * check wether they contain the suffix of the given hash.
    *
    * @param passwordSHA1 SHA-1 hash of the password you want to check
    */
  def checkPasswordSHA1(passwordSHA1: PasswordSHA1): Task[CheckPasswordSHA1Response]
}

object PwnedPasswordsClient {

  final case class CheckPasswordSHA1Response(passwordEntries: List[PasswordEntry]) {
    def findEntryForSHA1Suffix(suffix: PasswordSHA1.Suffix): Option[PasswordEntry] =
      passwordEntries.find(_.suffix == suffix)
  }
  object CheckPasswordSHA1Response {
    final case class PasswordEntry(suffix: PasswordSHA1.Suffix, count: Int)
  }

  val apiRoot = "https://api.pwnedpasswords.com"

  def checkPasswordSHA1RangePath(sha1Prefix: PasswordSHA1.Prefix): String = s"/range/${sha1Prefix.value}"
}

class PwnedPasswordsClientImpl extends PwnedPasswordsClient {

  override def checkPasswordSHA1(passwordSHA1: PasswordSHA1): Task[CheckPasswordSHA1Response] =
    for {
      responseBody <- queryPwnedPasswordsApi(passwordSHA1.prefix)
      response     <- parseResponse(responseBody)
    } yield response

  private def parseResponse(body: String): Task[CheckPasswordSHA1Response] = {

    def parseResponseLine(line: String): Either[String, PasswordEntry] =
      line.split(':') match {
        case Array(suffixStr, count) if count.forall(Character.isDigit) =>
          Right(PasswordEntry(PasswordSHA1.Suffix(suffixStr), count.toInt))

        case problematicLine =>
          Left(
            s"""Failed to parse line:
               |$problematicLine
               |in body:
               |$body
               |""".stripMargin
          )
      }

    // Use traverse to collect all parsed lines into one Either; Left is the first parse failure message
    val passwordEntries: Either[String, List[PasswordEntry]] =
      body.lines.toList.traverse(parseResponseLine)

    // We don't expect failures here, so if there are, fail the Task completely
    passwordEntries match {
      case Right(entries) =>
        Task(CheckPasswordSHA1Response(entries))

      case Left(errorMessage) =>
        Task.raiseError(new Exception(errorMessage))
    }
  }

  private def queryPwnedPasswordsApi(sha1Prefix: PasswordSHA1.Prefix): Task[String] = Task {
    Http(apiRoot + checkPasswordSHA1RangePath(sha1Prefix))
      .timeout(connTimeoutMs = 5000, readTimeoutMs = 10000) // working around my shitty internet <_<
      .asString
      .body
  }
}
