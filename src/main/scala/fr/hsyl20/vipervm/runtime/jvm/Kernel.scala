/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.runtime.jvm

import fr.hsyl20.vipervm.runtime.{Kernel,Device,Event,Buffer}

/**
 * Kernel that can be executed on the JVM (Scala,Java,Groovy...)
 */

/*abstract class JVMKernel extends Kernel {
  /* We use implicit conversions to cast from generic types
   * to backend types. We can do this because it is the
   * of the responsability of the scheduler to provide backends
   * with appropriate device and buffer types.
   */
  private implicit def dev2dev(d:Device): JVMDevice =
    d.asInstanceOf[JVMDevice]

  private implicit def buf2buf(b:Buffer): JVMBuffer =
    b.asInstanceOf[JVMBuffer]
}*/
