package calculator

object Polynomial extends PolynomialInterface {
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = {
    Signal {
      val squared = b()
      squared*squared - 4*a()*c()
    }
  }

  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = {
    Signal {
      if(delta() < 0) {
        Set()
      } else {
        val denom = 2*a()
        val r1 = (-b() + Math.sqrt(delta()) ) / denom
        val r2 = (-b() - Math.sqrt(delta()) ) / denom
        Set(r1, r2)
      }
    }
  }
}
