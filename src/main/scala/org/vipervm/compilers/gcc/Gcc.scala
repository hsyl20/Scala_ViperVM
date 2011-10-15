/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.compilers

import java.io._
import org.apache.commons.io.FileUtils

class GCC {
  val compileCmd = "gcc -c -o %s %s"
  val extractCmd = "objcopy -O binary --only-section=.text %s"

  def compile(code:String) = {
    val inFile = File.createTempFile("vipervm-"+this.hashCode.toString+"-",".c")
    val outFile = File.createTempFile("vipervm-"+this.hashCode.toString+"-",".o")

    val cc = compileCmd.format(outFile,inFile)
    val extract = extractCmd.format(outFile)

    val stream = new FileWriter(inFile)
    stream.write(code)
    stream.close

    Runtime.getRuntime.exec(cc).waitFor
    Runtime.getRuntime.exec(extract).waitFor

    val binary = FileUtils.readFileToByteArray(outFile)
    FileUtils.deleteQuietly(inFile)
    FileUtils.deleteQuietly(outFile)

    binary
  }
}
