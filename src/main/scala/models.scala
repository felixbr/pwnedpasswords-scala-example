import models.PasswordSHA1._

object models {

  /**
    * Represents a SHA-1 of a password.
    */
  final case class PasswordSHA1(value: String) extends AnyVal {
    def prefix: PasswordSHA1.Prefix = Prefix(value.take(PasswordSHA1.prefixLength))

    def suffix: PasswordSHA1.Suffix = Suffix(value.drop(PasswordSHA1.prefixLength))
  }
  object PasswordSHA1 {

    /**
      * The length of 5 is required by the pwnedpasswords api
      *
      * @see https://haveibeenpwned.com/API/v2#SearchingPwnedPasswordsByRange
      */
    val prefixLength: Int = 5

    /**
      * @see https://gist.github.com/mayoyamasaki/4085712
      */
    def fromCleartext(password: String): PasswordSHA1 = {
      val md   = java.security.MessageDigest.getInstance("SHA-1")
      val sha1 = md.digest(password.getBytes("UTF-8")).map("%02x".format(_)).mkString.toUpperCase

      PasswordSHA1(sha1)
    }

    final case class Prefix(value: String) extends AnyVal
    final case class Suffix(value: String) extends AnyVal
  }
}
