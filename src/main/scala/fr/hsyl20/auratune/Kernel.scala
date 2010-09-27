/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**
*/

package fr.hsyl20.auratune

import fr.hsyl20.{opencl => cl}
import fr.hsyl20.auratune.datatype._
import scala.collection.mutable.ArrayBuffer

sealed abstract class Area
case object Global extends Area
case object Local extends Area
case object Constant extends Area

sealed abstract class Size
case object NoSize extends Size
case class SizeOf(s:Symbol) extends Size
case class SizeValue(base:Int, dims:Int*) extends Size

sealed abstract class Mode
case object ReadOnly extends Mode
case object WriteOnly extends Mode
case object ReadWrite extends Mode

class Parameter(mode:Mode,typ:DataType,area:Area,size:Size)

class Kernel(program:Program, name:String) {
   private var params: ArrayBuffer[Parameter] = ArrayBuffer.empty
   private var namedParams: Map[Symbol,Parameter] = Map.empty

   private var peers: Map[Device,cl.Kernel] = Map.empty

   def get(device:Device): cl.Kernel = peers.get(device) match {
      case Some(p) => p
      case None => {
         val p = new cl.Kernel(program.get(device), name)
         p.build(List(device.peer))
         peers += (device -> p)
         p
      }
   }

   def input(datatype:DataType, size:Size = NoSize, name:String = "", area:Area = Global): Unit = {
      val p = new Parameter(ReadOnly,datatype,area,size)
      addParam(p, name)
   }

   def output(datatype:DataType, size:Size = NoSize, name:String = "", area:Area = Global): Unit = {
      if (size == NoSize)
         throw new Exception("Size argument is mandatory for output parameters")
      val p = new Parameter(WriteOnly,datatype,area,size)
      addParam(p, name)
   }

   def inout(datatype:DataType, size:Size = NoSize, name:String = "", area:Area = Global): Unit = {
      val p = new Parameter(ReadWrite,datatype,area,size)
      addParam(p, name)
   }

   private def addParam(p:Parameter, name:String): Unit = {
      params += p
      if (name != "")
         namedParams += (Symbol(name) -> p)
   }

   def apply(globalWorkSize:Seq[Long], localWorkSize:Option[Seq[Long]] = None)(args:Data*) : Array[Data] = {
      
      /* Create Data for outputs */
      val outputs = for ((sym,arg) <- this.args if arg.mode != ReadOnly) yield {
         arg.size match {
            case Size(base, dims @ _*) => (sym -> new Data(base * (1 /: dims)(_*_)))
            case SizeOf(s) => {
               val src = args.get(s).getOrElse(throw new Exception("Invalid codelet argument size"))
               (sym -> new Data(src.size))
            }
         }
      }

      val inputs = for ((sym,arg) <- this.args if arg.mode != WriteOnly) yield (sym, args.get(sym).get)

      val task = new Task(this, inputs, outputs, globalWorkSize, localWorkSize)
      Scheduler.schedule(task)
      task.outputs
   }
}
