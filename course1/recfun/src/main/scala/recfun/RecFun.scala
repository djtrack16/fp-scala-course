package recfun

object RecFun extends RecFunInterface {

  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(s"${pascal(col, row)} ")
      println()
    }
  }

  /**
   * Exercise 1
   * INSIGHT: 
   * If the column is 0 at any point, that is the start of the level, which is 1
   * If the row equals the column, that is the end of the level, which is also 1
   * Otherwise, we recur with the two row/col pairs above the current one
   */
  def pascal(c: Int, r: Int): Int = {
    if(c == 0 || r == c) 1 else pascal(c-1, r-1) + pascal(c,r-1)
  }

  /**
   * Exercise 2
    INSIGHT: as long as there are never more closed parentheses than open
    at any point in the iteration, the brackets are balanced
    If openParens < 0 at any point, return false
  */
  def balance(chars: List[Char]): Boolean = {
    val parensMatch = Map(')' -> -1, '(' -> 1)
    def balanceRecursive(chars: List[Char], openParens: Int): Boolean = {
      if(chars.isEmpty) {
        openParens == 0
      } else if (openParens < 0) {
        false
      } else {
        balanceRecursive(
          chars.tail,
          openParens + (parensMatch.get(chars.head) getOrElse 0)
        )
      }
    }
    balanceRecursive(chars, 0)
  }

  /**
   * Exercise 3
   * If we have no more money left, we found a solution so return 1
   * If we have "negative money" or no more coins, this is not a solution so return 0
   * Otherwise, we have two solutions:
   * Solutions that contain the coin at the start of the list (i.e. coins)
   * Solutions that do not contain the coin at the start of the list (i.e. coins.tail)
   * This will timeout on moderately large lists or amounts of money (or some admixture of both)
   * A more efficient solution would be to use dynamic programming
   */
  def countChange(money: Int, coins: List[Int]): Int =
    if(money == 0) {
      1
    } else if(money < 0 || coins.isEmpty) { 
      0
    } else {
      countChange(money - coins.head, coins) + countChange(money, coins.tail)
    }
}
