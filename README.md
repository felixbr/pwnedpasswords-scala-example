## Pwned Passwords Scala Example

This project is an example integration of the [Pwned Passwords API](https://haveibeenpwned.com/Passwords)
provided by [Troy Hunt](https://twitter.com/troyhunt).

At the moment this isn't a ready-to-use library, but it should give you a decent idea on how to
integrate the API yourself using libraries of your choice.

Read more about the API and why checking passwords with it is a good idea:
  - https://www.troyhunt.com/ive-just-launched-pwned-passwords-version-2/
  - https://haveibeenpwned.com/API/v2

### Commands

#### sbt

  ```bash
  sbt run
  ```

Starting the project will enter an infinite loop where you can enter a password to check and it will
return wether it has been pwned after calling the api.

Enter an empty password to exit the program.