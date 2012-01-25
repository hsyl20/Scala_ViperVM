package org.vipervm.fp

class REPL {
  val console = new jline.ConsoleReader

  val prompt = "vvm> "

  repl

  def repl:Unit = {
    val s = console.readLine(prompt)
    val idx = s.indexOf(" ",0)
    val (cmd,args) = if (idx > 0) s.splitAt(idx) else (s,"")
    cmd match {
      case ":quit" =>
      case ":parse" => {
        println(Parser.parse(args))
        repl
      }
      case ":eval" => {
        val e = Parser.parse(args)
        e match {
          case Parser.Success(t,_) => {
	    println(Printer.print(Term.eval(new Context, t), new Context))
	  }
          case Parser.NoSuccess(msg,_) => println(msg)
        }
        repl
      }
      case _ => {
        val e = Parser.parse(s)
        e match {
          case Parser.Success(t,_) => println(Printer.print(Term.eval(new Context,t), new Context))
          case Parser.NoSuccess(msg,_) => println(msg)
        }
        repl
      }
    }
  }

}

object REPL {
  def main(args:Array[String]):Unit = {
    val r = new REPL
  }
}
