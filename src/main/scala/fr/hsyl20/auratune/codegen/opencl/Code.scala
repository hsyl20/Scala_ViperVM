package fr.hsyl20.auratune.codegen

/*
class Code {

   enterScope


   /********************************************/
   /* Symbols */

   var symbols: List[mutable.Map[Symbol,Sym]] = Nil

   def getSymbol(s:Symbol, syms:List[mutable.Map[Symbol,Sym]] = symbols): Option[Sym] = {
      syms.headOption flatMap (_.get(s) orElse getSymbol(s, syms.tail))
   }

   implicit def symbol2sym(s:Symbol): Sym = getSymbol(s) match {
      case Some(s) => s
      case None => SymUnknown
   }

   /********************************************/
   /* Scopes */

   def enterScope: Unit = {
      symbols = new mutable.HashMap :: symbols
   }

   def leaveScope: Unit = {
      symbols = symbols.tail
   }

   /********************************************/
   /* C headers */

   var includes: List[String] = Nil

   protected def include(s:String): Unit = {
      if (!includes.contains(s)) includes = s :: includes
   }

}
*/
