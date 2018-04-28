import monix.eval.Task
import PwnedPasswordsHandler._
import models._

trait PwnedPasswordsHandler {

  /**
    * Checks wether a password is in the pwnedpasswords database.
    *
    * This Task may fail in case of problems with the API (connectivity, unexpected responses),
    * so you may want to handle that gracefully in order to not crash during registration/password-change.
    */
  def checkPassword(cleartextPassword: String): Task[CheckPasswordResult]
}

object PwnedPasswordsHandler {

  sealed trait CheckPasswordResult
  object CheckPasswordResult {

    /**
      * The given password is not in the pwnedpasswords database.
      *
      * This means it was not observed in a publicly known breach.
      * It DOESN'T mean the password is necessarily safe or even hard to crack in general.
      */
    case object PasswordIsNotPwned extends CheckPasswordResult

    /**
      * The given password is in the pwnedpasswords database.
      *
      * This means it was observed in a publicly known breach or other passwords lists
      * floating around the internet.
      *
      * You should advice your user to choose a different password because it is VERY
      * risky to use this one.
      *
      * @param count Number of times the password occured in breaches and password lists
      */
    case class PasswordIsPwned(count: Int) extends CheckPasswordResult
  }
}

class PwnedPasswordsHandlerImpl(pwnedPasswordsClient: PwnedPasswordsClient) extends PwnedPasswordsHandler {

  override def checkPassword(cleartextPassword: String): Task[CheckPasswordResult] = {
    val passwordSHA1 = PasswordSHA1.fromCleartext(cleartextPassword)

    pwnedPasswordsClient.checkPasswordSHA1(passwordSHA1).map { response =>
      response.findEntryForSHA1Suffix(passwordSHA1.suffix) match {
        case Some(entry) =>
          CheckPasswordResult.PasswordIsPwned(entry.count)

        case None => // password does not match any entry in response
          CheckPasswordResult.PasswordIsNotPwned
      }
    }
  }
}
