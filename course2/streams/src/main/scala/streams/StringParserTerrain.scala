package streams

/**
 * This component implements a parser to define terrains from a
 * graphical ASCII representation.
 *
 * When mixing in that component, a level can be defined by
 * defining the field `level` in the following form:
 *
 *   val level =
 *     """------
 *       |--ST--
 *       |--oo--
 *       |--oo--
 *       |------""".stripMargin
 *
 * - The `-` character denotes parts which are outside the terrain
 * - `o` denotes fields which are part of the terrain
 * - `S` denotes the start position of the block (which is also considered
     inside the terrain)
 * - `T` denotes the final position of the block (which is also considered
     inside the terrain)
 *
 * In this example, the first and last lines could be omitted, and
 * also the columns that consist of `-` characters only.
 */
trait StringParserTerrain extends GameDef {

  /**
   * A ASCII representation of the terrain. This field should remain
   * abstract here.
   */
  val level: String

  /**
   * This method returns terrain function that represents the terrain
   * in `levelVector`. The vector contains parsed version of the `level`
   * string. For example, the following level
   *
   *   val level =
   *     """ST
   *       |oo
   *       |oo""".stripMargin
   *
   * is represented as
   *
   *   Vector(Vector('S', 'T'), Vector('o', 'o'), Vector('o', 'o'))
   *
   * The resulting function should return `true` if the position `pos` is
   * a valid position (not a '-' character) inside the terrain described
   * by `levelVector`.
   */
  def terrainFunction(levelVector: Vector[Vector[Char]]): Pos => Boolean = {
 /*   def validPosition(pos: Pos): Boolean = {
      val rows = (0 until levelVector.length) 
      if(!rows.contains(pos.row)) false
      else {
        val columns = levelVector(pos.row)
        columns.contains(pos.col) && columns(pos.col) != '-'
      } 
    }
    validPosition
    */
    def valid(pos: Pos) = {
      if (levelVector.isEmpty || pos.row < 0 || pos.col < 0) false
      else {
        pos.row < levelVector.length && pos.col < levelVector(pos.row).length && levelVector(pos.row)(pos.col) != '-'
      }
    }
    valid
  }

  /**
   * This function should return the position of character `c` in the
   * terrain described by `levelVector`. You can assume that the `c`
   * appears exactly once in the terrain.
   *
   * Hint: you can use the functions `indexWhere` and / or `indexOf` of the
   * `Vector` class
   */
  def findChar(c: Char, levelVector: Vector[Vector[Char]]): Pos = {
/*  val chars = for {
      row <- 0 until levelVector.length
      col = levelVector(row).indexWhere(_ == c)
      if col != -1
    } yield Pos(row, col)*/

    val positions = for {
      (v, row) <- levelVector.zipWithIndex
      col = v.indexOf(c)
      if col != -1
    } yield Pos(row, col)
    positions.head
  }

  private lazy val vector: Vector[Vector[Char]] =
    Vector(level.split("\r?\n").map(str => Vector(str: _*)).toIndexedSeq: _*)

  lazy val terrain: Terrain = terrainFunction(vector)
  lazy val startPos: Pos = findChar('S', vector)
  lazy val goal: Pos = findChar('T', vector)

}
