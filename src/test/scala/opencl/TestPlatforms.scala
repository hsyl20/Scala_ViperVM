import org.vipervm.bindings.opencl._
import org.junit._

class TestPlatforms {
   
   @Test def platforms {
      println("Platforms:")
      for (p <- OpenCL.platforms) {
         println(p.name + " - " + p.vendor)
         println("  Version: " + p.version)
         println("  Profile: " + p.profile)
         println("  Extensions:")
         for (e <- p.extensions)
            println("      " + e)

         println("  Devices:")
         for (d <- p.devices()) {
            println("   " + d.name + " - " + d.vendor)
            println("     Device type: " + d.deviceType)
            println("     Vendor ID: " + d.vendorID)
            println("     Max compute units: " + d.maxComputeUnits)
            println("     Max work item dimensions: " + d.maxWorkItemDimensions)
            print("     Max work item sizes: ")
            for (s <- d.maxWorkItemSizes)
               print(s + " ")
            println()

            println("     Max work group size: " + d.maxWorkGroupSize)
            println("     Preferred Char vector width: " + d.preferredCharVectorWidth)
            println("     Preferred Short vector width: " + d.preferredShortVectorWidth)
            println("     Preferred Int vector width: " + d.preferredIntVectorWidth)
            println("     Preferred Long vector width: " + d.preferredLongVectorWidth)
            println("     Preferred Float vector width: " + d.preferredFloatVectorWidth)
            println("     Preferred Double vector width: " + d.preferredDoubleVectorWidth)

            println("     Max clock frequency: " + d.maxClockFrequency + " Hz")
            println("     Address width: " + d.addressBits + " bits")
            println("     Max memory allocation size: " + d.maxMemAllocSize + " bytes")
            println("     Image support: " + d.imageSupport)

            if (d.imageSupport) {
               println("     Max 2D image width: " + d.max2DImageWidth + " pixels")
               println("     Max 2D image height: " + d.max2DImageHeight + " pixels")
               println("     Max 3D image width: " + d.max3DImageWidth + " pixels")
               println("     Max 3D image height: " + d.max3DImageHeight + " pixels")
               println("     Max 3D image depth: " + d.max3DImageDepth + " pixels")
               println("     Max samplers: " + d.maxSamplers)
            }

            println("     Max parameter size: " + d.maxParameterSize + " bytes")
            println("     Memory base address alignment: " + d.memBaseAddrAlign + " bytes")
            println("     Minimal data type alignment size: " + d.minDataTypeAlignSize + " bytes")

            println("     Single floating-point configuration: " + d.singleFloatingPointConfig) //FIXME

            println("     Global memory cache type: " + d.globalMemCacheType) //FIXME
            println("     Global memory cacheline size: " + d.globalMemCacheLineSize + " bytes")
            println("     Global memory cache size: " + d.globalMemCacheSize + " bytes")
            println("     Global memory size: " + d.globalMemSize + " bytes")

            println("     Max constant buffer size: " + d.maxConstantBufferSize + " bytes")
            println("     Max constant arguments: " + d.maxConstantArgs)

            println("     Local memory type: " + d.localMemType) //FIXME
            println("     Local memory size: " + d.localMemSize + " bytes")

            println("     Error correction support: " + d.errorCorrectionSupport)
            println("     Profiling timer resolution: " + d.profilingTimerResolution + " ns")
            println("     Endianness: " + d.endianness)
            println("     Available: " + d.available)
            println("     Compiler available: " + d.compilerAvailable)

            println("     Execution capabilities: " + d.executionCapabilities) //FIXME
            println("     Queue properties: " + d.queueProperties) //FIXME

            println("     Driver version: " + d.driverVersion)
            println("     Profile: " + d.profile)
            println("     Version: " + d.version)
            println("     Extensions:")
            for (e <- d.extensions)
               println("         " + e)

         }
      }
   }

}

