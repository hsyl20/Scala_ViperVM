package org.vipervm.platform

trait Prototyped {

  val prototype:List[Parameter[_]]

  implicit def paramExtractor(params:Seq[Any]) = new {
    def apply[A:Manifest](param:Parameter[A]) = try {
      params(prototype.indexOf(param)).asInstanceOf[A]
    }
    catch {
      case e => throw new Exception("Invalid parameter for \"%s\" at position %d. You should pass a %s that corresponds to the following description: %s)".format(param.name, prototype.indexOf(param), manifest[A].toString, param.description))
    }
  }

}
