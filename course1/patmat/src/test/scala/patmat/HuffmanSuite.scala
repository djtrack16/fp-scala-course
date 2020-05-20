package patmat

import org.junit._
import org.junit.Assert.assertEquals

class HuffmanSuite {
  import Huffman._

  trait TestTrees {
    val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
    val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
    val charsList = List('a', 'b', 'c', 'b', 'd', 'e', 'a', 'a', 'd', 'd', 'e')
  }


  @Test def `weight of a tree (10pts)`: Unit =
    new TestTrees {
      assertEquals(5, weight(t1))
      assertEquals(9, weight(t2))
    }


  @Test def `chars of a tree (10pts)`: Unit =
    new TestTrees {
      assertEquals(List('a','b','d'), chars(t2))
      assertEquals(List('a','b'), chars(t1))
    }

  @Test def `times of a larger tree`: Unit = {
    new TestTrees {
      assertEquals(times(charsList), List(('e',2), ('a',3), ('b',2), ('c',1), ('d',3)))
    }
  }


  @Test def `string2chars hello world`: Unit =
    assertEquals(List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'), string2Chars("hello, world"))


  @Test def `make ordered leaf list for some frequency table (15pts)`: Unit =
    assertEquals(List(Leaf('e',1), Leaf('t',2), Leaf('x',3)), makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))))


  @Test def `combine of some leaf list (15pts)`: Unit = {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assertEquals(List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)), combine(leaflist))
  }

  @Test def `insertion sort of leaf list works with all leaves`: Unit = {
    val leaflist = List(Leaf('e', 4), Leaf('t', 2), Leaf('x', 1))
    assertEquals(List(Leaf('x', 1), Leaf('t', 2), Leaf('e', 4)), isort(leaflist))
  }

  @Test def `insertion sort of leaf list works with 1 fork (15pts)`: Unit = {
    val leaflist = List(Fork(Leaf('e', 4), Leaf('t', 2), List('e', 't'), 6), Leaf('x', 1), Leaf('y', 5), Leaf('u', 10))
    assertEquals(List(Leaf('x', 1), Leaf('y', 5), Fork(Leaf('e', 4), Leaf('t', 2), List('e', 't'), 6), Leaf('u', 10)), isort(leaflist))
  }

  @Test def `decode and encode a very short text should be identity (10pts)`: Unit =
    new TestTrees {
      assertEquals("ab".toList, decode(t1, encode(t1)("ab".toList)))
    }
  
  @Test def `encode a text`: Unit =
    new TestTrees {
      assertEquals(List(0,0,0,1,1), encode(t2)("abd".toList))
    }

  @Test def `decode a very short list of bits`: Unit =
    new TestTrees {
      assertEquals("abd".toList, decode(t2, List(0,0,0,1,1))) 
    }

  @Test def `code Table should convert for small list`: Unit =
    new TestTrees {
      assertEquals(List(('a',List(0, 0)), ('b',List(0, 1)), ('d',List(1))), convert(t2)) 
    }


  @Rule def individualTestTimeout = new org.junit.rules.Timeout(10 * 1000)
}
