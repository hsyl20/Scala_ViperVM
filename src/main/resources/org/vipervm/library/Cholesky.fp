def cholesky = cholesky_dp | cholesky_blocking

def cholesky_dp(m) = {
  val (a11,a21) = m.firstColumn.split(1)
  val a22 = m.dropColumn(1)
  val l11 = a11.first.sqrt
  val l21 = a21.map(x => x/l11)
  val l22b = (l21 × l21).map(a => a._1 * a._2).lowerTriangular.zip(a22).map(a => a._2 - a._1)
  val l22 = cholesky(l22b)

  (l11 :: l21) :: l22
}

def cholesky_blocking(m) = {
  val mb = m.blocking
  cholesky_blocked(mb)
}

def cholesky_blocked(mb) = {
  val (a11,a21) = mb.firstColumn.split(1)
  val a22 = mb.dropColumn(1)
  val l11 = cholesky.call(a11.first.lowerTriangular)
  val l21 = a21.map(x => Blas.trsm(l11,x))
  val l22b = (l21 × l21).map(a => a._1 * a._2).lowerTriangular.zip(a22).map(a => a._2 - a._1)

  val l22 = (cholesky_blocked | cholesky_fusion)(l22b)

  ((l11.toMatrix :: l21) :: l22.blocking).flatten
}

def cholesky_fusion(mb) = {
  cholesky(mb.flatten)
}
