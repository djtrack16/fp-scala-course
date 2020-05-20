package funsets

/**
 * 2. Purely Functional Sets.
 */
trait FunSets extends FunSetsInterface {
  /**
   * We represent a set by its characteristic function, i.e.
   * its `contains` predicate.
   */
  override type FunSet = Int => Boolean

  /**
    * INSIGHT:
    * You don't need to specify return types in the below parameters because they 'elem' type is inferred.
    * But we can specify type Int anyway (if we want to)
    *
    */
  /**
   * Indicates whether a set contains a given element.
   */
  def contains(s: FunSet, elem: Int): Boolean = s(elem)

  /**
   * Returns the set of the one given element.
   */
  def singletonSet(elem: Int): FunSet = (x: Int) => x == elem


  /**
   * Returns the union of the two given sets,
   * the sets of all elements that are in either `s` or `t`.
   */
  def union(s: FunSet, t: FunSet): FunSet = elem => s(elem) || t(elem)

  /**
   * Returns the intersection of the two given sets,
   * the set of all elements that are both in `s` and `t`.
   */
  def intersect(s: FunSet, t: FunSet): FunSet = elem => s(elem) && t(elem)
  /**
   * Returns the difference of the two given sets,
   * the set of all elements of `s` that are not in `t`.
   */
  def diff(s: FunSet, t: FunSet): FunSet = (elem: Int) => s(elem) && !t(elem)

  /**
   * Returns the subset of `s` for which `p` holds.
   */
  def filter(s: FunSet, p: Int => Boolean): FunSet = (elem: Int) => s(elem) && p(elem)


  /**
   * The bounds for `forall` and `exists` are +/- 1000.
   */
  val bound = 1000

  /**
   * Returns whether all bounded integers within `s` satisfy `p`.
   */
  def forall(s: FunSet, p: Int => Boolean): Boolean = {
    def iter(a: Int): Boolean = {
      if (a > bound) true // true
      else if (contains(s,a) && !p(a)) false // false p(a)
      else iter(a+1)
    }
    iter(-bound)
  }

  /**
   * Returns whether there exists a bounded integer within `s`
   * that satisfies `p`.
   *   def exists(s: FunSet, p: Int => Boolean): Boolean = {
        def iter(a: Int): Boolean = {
          if (a > bound) false
          else if (contains(s,a) && p(a)) true
          else iter(a+1)
        }
        iter(-bound)
      }
   * This is 'forany' (written another way) which is the same as negating the result 'forall' and negating 'p'
   * AKA not all elements in S satisfies P, which means at least 1 element in S satisfies P
   */
  def exists(s: FunSet, p: Int => Boolean): Boolean = !forall(s, elem => !p(elem))

  /**
   * Returns a set transformed by applying `f` to each element of `s`.
   * Could write using 'exists' also.
   */
  def map(s: FunSet, f: Int => Int): FunSet = (a: Int) => !forall(s, b => !(f(b) == a))

  /**
   * Displays the contents of a set
   */
  def toString(s: FunSet): String = {
    val xs = for (i <- -bound to bound if contains(s, i)) yield i
    xs.mkString("{", ",", "}")
  }

  /**
   * Prints the contents of a set on the console.
   */
  def printSet(s: FunSet): Unit = {
    println(toString(s))
  }
}

object FunSets extends FunSets
