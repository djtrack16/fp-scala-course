package funsets

import org.junit._

/**
 * This class is a test suite for the methods in object FunSets.
 *
 * To run this test suite, start "sbt" then run the "test" command.
 */
class FunSetSuite {

  import FunSets._

  @Test def `contains is implemented`: Unit = {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(1)

    val s5 = singletonSet(-1)
    val s6 = singletonSet(-2)
    val s7 = singletonSet(-3)
    val s8 = singletonSet(-4)

    val s1000 = singletonSet(1000)
    val s999 = singletonSet(999)
    val sneg999 = singletonSet(-999)
  }

  /**
   * This test is currently disabled (by using @Ignore) because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", remvoe the
   * @Ignore annotation.
   */
  @Test def `singleton set one contains one`: Unit = {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "S1 contains 1")
      assert(!contains(s1, 2), "S1 does not contain 2")
    }
  }

  @Test def `union contains all elements of each set`: Unit = {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  @Test def `intersection contains common elements of all sets`: Unit = {
    new TestSets {
      var s = intersect(s1, s2)
      assert(!contains(s, 1), "Intersection 1")
      assert(!contains(s, 2), "Intersection 2")
      assert(!contains(s, 3), "Intersection 3")

      s = intersect(s1, s4)
      assert(contains(s, 1), "Intersection of s1 and s4")
    }
  }

  @Test def `diff contains elements in s that are not in t`: Unit = {
    new TestSets {
      var s = diff(s2, s1)
      assert(!contains(s, 1), "Diff should not contain 1")
      assert(contains(s, 2), "Diff should contain 2")

      s = diff(s1, s4)
      assert(!contains(s, 1), "Diff of s1 and s4 should not contain 1")
    }
  }

  @Test def `filter contains elements in s that satisfy the function P`: Unit = {
    new TestSets {
      var s = union(s1, s2)
      var filtered = filter(s, x => x > 1)
      assert(!contains(filtered, 1), "Filtered should contain not 1")
      assert(contains(filtered, 2), "Filtered should contain 2")

      filtered = filter(s, x => x == 0)
      assert(!contains(filtered, 1), "Filtered should not contain 1")
      assert(!contains(filtered, 2), "Filtered should not contain 2")
    }
  }

  @Test def `forall returns true if all bounded integers in s satisfy the function P`: Unit = {
    new TestSets {
      var s = union(s999, s1000)
      assert(forall(s, x => x > 990), "forall for 999 and 1000 should be true")
      assert(!forall(s, x => x > 999), "forall for 999 and 100 should be false")

      s = union(s999, sneg999)
      assert(!forall(s, x => x == 2), "forall for 999 and -999 should be false")
    }
  }

  @Test def `exists returns true if at least 1 bounded integer in s satisfies the function P`: Unit = {
    new TestSets {
      var s = diff(s999, s1000)
      assert(exists(s, x => x == 999), "exists for 999 and 1000 should be true")
      assert(!exists(s, x => x < 0), "forall for 999 and 100 should be false if all > 0")

      s = intersect(s4, s1)
      assert(exists(s, x => x > 0), "forall for 1 and 1 should be true")
    }
  }

  @Test def `map applies the function F to all elements in s and returns that transformation`: Unit = {
    new TestSets {
      var s = union(s1, s2)
      var mapped = map(s, x => x + 10)
      assert(contains(mapped, 11), "Mapped should contain 11")
      assert(contains(mapped, 12), "Mapped should contain 12")

      var t = union(filter(s, y => y < 2), s1000)
      mapped = map(t, x => x - 10)
      assert(contains(mapped, -9), "Mapped should contain 1 - 10")
      assert(contains(mapped, 990), "Filtered should contain 1000 - 10")
    }
  }

  @Rule def individualTestTimeout = new org.junit.rules.Timeout(10 * 1000)
}
