package quickcheck

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = oneOf(
    const(empty),
    for {
      value <- arbitrary[Int]
      heap <- oneOf(const(empty), genHeap)
    } yield insert(value, heap)
  ) 

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("deleteMin1") = forAll { a: Int =>
    val h = insert(a, empty)
    deleteMin(h) == empty
  }

  property("getSmaller") = forAll { (a: Int, b: Int) => 
    val h = insert(a, insert(b, empty))
    findMin(h) == (if(a < b) a else b)
  }

  property("meldMinimum") = forAll { (h1: H, h2: H) =>
    if(isEmpty(h1) || isEmpty(h2)) {
      true
    } else {
      val newMin = findMin(meld(h1, h2))
      newMin == findMin(h1) || newMin == findMin(h2)
    }
  }

  property("recursiveMin") = forAll { (h: H) =>
    def recur(h: H, prevMin: Int, prev: Boolean = true): Boolean = {
      if (isEmpty(h)) {
        true
      } else {
        val currMin = findMin(h)
        val newH = deleteMin(h)
        if(prev) prevMin <= currMin && recur(newH, currMin) else recur(newH, currMin)
      }
    }

    recur(h, -1, false)
  }

  property("compareMins") = forAll { (h: H) =>
    if(isEmpty(h)) {
      true
    } else {
      val oldMin = findMin(h)
      val newH = deleteMin(h)
      if(isEmpty(newH)) {
        true
      } else {
        val newMin = findMin(newH)
        newMin >= oldMin
      }
    }
  }

  property("deleteMinThenInsert") = forAll { h: H => 
    if(isEmpty(h)) true else {
      val oldMin = findMin(h)
      val reinserted = insert(oldMin, deleteMin(h))
      findMin(reinserted) == oldMin
    }
  }

  // two heaps should be equal if their minimums are the same at every step/iteration and the heaps are the same size
  property("equalityTest") = forAll { (h1: H, h2: H) =>
    def equals(h1: H, h2: H): Boolean = {
      if(isEmpty(h1) && isEmpty(h2)) {
        true
      } else if( (isEmpty(h1) && !isEmpty(h2)) || (!isEmpty(h1) && isEmpty(h2)) ) {
        false
      } else {
        findMin(h1) == findMin(h2) && equals(deleteMin(h1), deleteMin(h2))
      }
    }

    val meld1 = meld(h1, h2)
    val meld2 = if(isEmpty(h1)) h2 else meld(deleteMin(h1), insert(findMin(h1), h2))
    equals(meld1, meld2)
  }
}
