package fr.hsyl20.auratune.codegen.opencl

trait CLAddressSpaces {
   val Global   = AddressSpace("global")
   val Local    = AddressSpace("local")
   val Constant = AddressSpace("constant")
   val Private  = AddressSpace("private")
}
