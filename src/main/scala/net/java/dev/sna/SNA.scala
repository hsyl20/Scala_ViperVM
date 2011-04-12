/* 
 * This file has been modified from SNA.
 *
 * SNA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 
 * of the License, or (at your option) any later version.
 *
 * SNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SNA.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.java.dev.sna

import com.sun.jna.Function
import scala.reflect.Manifest

private[sna] class SNAF0[R](val jnaFun: Function, val manif: Manifest[R]) extends Function0[R] {
	def apply(): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object]()).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure, Array[Object]()).asInstanceOf[R]
		}
}

private[sna] class SNAF1[T, R](val jnaFun: Function, val manif: Manifest[R]) extends Function1[T, R] {
	def apply(t1: T): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object](t1.asInstanceOf[Object])).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure,
					Array[Object](t1.asInstanceOf[Object])).asInstanceOf[R]
		}
}

private[sna] class SNAF2[T1, T2, R](val jnaFun: Function, val manif: Manifest[R]) extends Function2[T1, T2, R] {
	def apply(t1: T1, t2: T2): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object])).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure,
					Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object])).asInstanceOf[R]
		}
}

private[sna] class SNAF3[T1, T2, T3, R](val jnaFun: Function, val manif: Manifest[R]) extends Function3[T1, T2, T3, R] {
	def apply(t1: T1, t2: T2, t3: T3): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object])).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure,
					Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object])).asInstanceOf[R]
		}
}

private[sna] class SNAF4[T1, T2, T3, T4, R](val jnaFun: Function, val manif: Manifest[R]) extends Function4[T1, T2, T3, T4, R] {
	def apply(t1: T1, t2: T2, t3: T3, t4: T4): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object])).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure,
					Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object])).asInstanceOf[R]
		}
}

private[sna] class SNAF5[T1, T2, T3, T4, T5, R](val jnaFun: Function, val manif: Manifest[R]) extends Function5[T1, T2, T3, T4, T5, R] {
	def apply(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object])).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure,
					Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object])).asInstanceOf[R]
		}
}

private[sna] class SNAF6[T1, T2, T3, T4, T5, T6, R](val jnaFun: Function, val manif: Manifest[R]) extends Function6[T1, T2, T3, T4, T5, T6, R] {
	def apply(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object], t6.asInstanceOf[Object])).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure,
					Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object], t6.asInstanceOf[Object])).asInstanceOf[R]
		}
}

private[sna] class SNAF7[T1, T2, T3, T4, T5, T6, T7, R](val jnaFun: Function, val manif: Manifest[R]) extends Function7[T1, T2, T3, T4, T5, T6, T7, R] {
	def apply(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, t7: T7): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object], t6.asInstanceOf[Object], t7.asInstanceOf[Object])).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure,
					Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object], t6.asInstanceOf[Object], t7.asInstanceOf[Object])).asInstanceOf[R]
		}
}

private[sna] class SNAF8[T1, T2, T3, T4, T5, T6, T7, T8, R](val jnaFun: Function, val manif: Manifest[R]) extends Function8[T1, T2, T3, T4, T5, T6, T7, T8, R] {
	def apply(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, t7: T7, t8: T8): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object], t6.asInstanceOf[Object], t7.asInstanceOf[Object], t8.asInstanceOf[Object])).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure,
					Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object], t6.asInstanceOf[Object], t7.asInstanceOf[Object], t8.asInstanceOf[Object])).asInstanceOf[R]
		}
}

private[sna] class SNAF9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](val jnaFun: Function, val manif: Manifest[R]) extends Function9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] {
	def apply(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, t7: T7, t8: T8, t9: T9): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object], t6.asInstanceOf[Object], t7.asInstanceOf[Object], t8.asInstanceOf[Object], t9.asInstanceOf[Object])).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure,
					Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object], t6.asInstanceOf[Object], t7.asInstanceOf[Object], t8.asInstanceOf[Object], t9.asInstanceOf[Object])).asInstanceOf[R]
		}
}

private[sna] class SNAF10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](val jnaFun: Function, val manif: Manifest[R]) extends Function10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] {
	def apply(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, t7: T7, t8: T8, t9: T9, t10:T10): R =
		if (manif == Manifest.Unit) {
			jnaFun.invoke(Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object], t6.asInstanceOf[Object], t7.asInstanceOf[Object], t8.asInstanceOf[Object], t9.asInstanceOf[Object], t10.asInstanceOf[Object])).asInstanceOf[R]
		} else {
			jnaFun.invoke(manif.erasure,
					Array[Object](t1.asInstanceOf[Object], t2.asInstanceOf[Object], t3.asInstanceOf[Object], t4.asInstanceOf[Object], t5.asInstanceOf[Object], t6.asInstanceOf[Object], t7.asInstanceOf[Object], t8.asInstanceOf[Object], t9.asInstanceOf[Object], t10.asInstanceOf[Object])).asInstanceOf[R]
		}
}

trait SNA {
	
	protected var snaLibrary = ""

	protected def SNAS[R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF0[R](Function.getFunction(snaLibrary, s), manif)
	}

	protected def SNAS[T1, R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF1[T1, R](Function.getFunction(snaLibrary, s), manif)
	}

	protected def SNAS[T1, T2, R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF2[T1, T2, R](Function.getFunction(snaLibrary, s), manif)
	}

	protected def SNAS[T1, T2, T3, R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF3[T1, T2, T3, R](Function.getFunction(snaLibrary, s), manif)
	}

	protected def SNAS[T1, T2, T3, T4, R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF4[T1, T2, T3, T4, R](Function.getFunction(snaLibrary, s), manif)
	}

	protected def SNAS[T1, T2, T3, T4, T5, R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF5[T1, T2, T3, T4, T5, R](Function.getFunction(snaLibrary, s), manif)
	}

	protected def SNAS[T1, T2, T3, T4, T5, T6, R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF6[T1, T2, T3, T4, T5, T6, R](Function.getFunction(snaLibrary, s), manif)
	}

	protected def SNAS[T1, T2, T3, T4, T5, T6, T7, R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF7[T1, T2, T3, T4, T5, T6, T7, R](Function.getFunction(snaLibrary, s), manif)
	}

	protected def SNAS[T1, T2, T3, T4, T5, T6, T7, T8, R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF8[T1, T2, T3, T4, T5, T6, T7, T8, R](Function.getFunction(snaLibrary, s), manif)
	}

	protected def SNAS[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](Function.getFunction(snaLibrary, s), manif)
	}

	protected def SNAS[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](s:String)(implicit manif: Manifest[R]) = {
		new SNAF10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](Function.getFunction(snaLibrary, s), manif)
	}
}

