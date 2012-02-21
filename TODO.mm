<map version="0.9.0">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1329316194019" ID="ID_1720803860" MODIFIED="1329827047332" TEXT="ViperVM TODO List">
<node CREATED="1329316798229" FOLDED="true" ID="ID_288495963" MODIFIED="1329474065116" POSITION="left" TEXT="Frontends and DSLs">
<node CREATED="1329316822389" ID="ID_579801226" MODIFIED="1329316829522" TEXT="Scala DSL"/>
<node CREATED="1329317138325" ID="ID_819228666" MODIFIED="1329317166658" TEXT="Lisp-like DSL"/>
</node>
<node CREATED="1329316938293" FOLDED="true" ID="ID_964565510" MODIFIED="1329845629268" POSITION="right" TEXT="Architectures">
<node CREATED="1329316943221" ID="ID_1399455359" MODIFIED="1329316946578" TEXT="Support CUDA"/>
<node COLOR="#ff4b08" CREATED="1329317484357" ID="ID_781006358" MODIFIED="1329317542731" TEXT="Support OpenCL"/>
<node CREATED="1329316947525" ID="ID_892034661" MODIFIED="1329316949954" TEXT="Support CELL"/>
<node CREATED="1329316951589" ID="ID_1149291227" MODIFIED="1329316964162" TEXT="Support JVM"/>
<node CREATED="1329316964933" ID="ID_698351750" MODIFIED="1329316981970" TEXT="Support native host kernels"/>
</node>
<node CREATED="1329316987525" FOLDED="true" ID="ID_1117605643" MODIFIED="1329827107453" POSITION="left" TEXT="Profiling">
<node CREATED="1329316997349" ID="ID_382790587" MODIFIED="1329318783659" TEXT="Generate profiling traces"/>
<node CREATED="1329317015877" ID="ID_351385300" MODIFIED="1329317022354" TEXT="Create profiler GUI"/>
</node>
<node CREATED="1329317311749" FOLDED="true" ID="ID_1191010253" MODIFIED="1329845634724" POSITION="right" TEXT="Fault-tolerance">
<node CREATED="1329317316917" ID="ID_984744930" MODIFIED="1329317325682" TEXT="Automatic check-pointing mechanism"/>
</node>
<node CREATED="1329317592373" FOLDED="true" ID="ID_1466197748" MODIFIED="1329827124199" POSITION="left" TEXT="Program graph">
<node CREATED="1329316810597" ID="ID_243803651" MODIFIED="1329316818082" TEXT="Support conditionals"/>
<node CREATED="1329317801238" ID="ID_434231529" MODIFIED="1329317815814" TEXT="Support data-slicing">
<arrowlink DESTINATION="ID_1635782699" ENDARROW="Default" ENDINCLINATION="641;0;" ID="Arrow_ID_476923513" STARTARROW="None" STARTINCLINATION="641;0;"/>
</node>
<node CREATED="1329318375365" ID="ID_492162548" MODIFIED="1329318378226" TEXT="Support tuples"/>
<node CREATED="1329827116754" ID="ID_1783064985" MODIFIED="1329827119574" TEXT="Support records"/>
</node>
<node CREATED="1329318670037" FOLDED="true" ID="ID_1848624900" MODIFIED="1329827131585" POSITION="left" TEXT="Program graph engine">
<node CREATED="1329318680437" ID="ID_1096600373" MODIFIED="1329318752260" TEXT="Compile all kernels before execution"/>
<node CREATED="1329474149226" ID="ID_19630002" MODIFIED="1329474165707" TEXT="Optimizations">
<node CREATED="1329474166710" ID="ID_312681417" MODIFIED="1329474176677" TEXT="Common sub-expressions elimination"/>
</node>
</node>
<node CREATED="1329317241557" FOLDED="true" ID="ID_855131793" MODIFIED="1329845636028" POSITION="right" TEXT="Networking">
<node CREATED="1329317244725" ID="ID_1002094006" MODIFIED="1329317258978" TEXT="Distributed ViperVM">
<node CREATED="1329317271573" ID="ID_1747052728" MODIFIED="1329317305026" TEXT="MPI backend"/>
<node CREATED="1329317279941" ID="ID_894391285" MODIFIED="1329317302258" TEXT="MX backend"/>
<node CREATED="1329317293493" ID="ID_274566651" MODIFIED="1329317299218" TEXT="NewMad backend"/>
</node>
</node>
<node CREATED="1329316636806" FOLDED="true" ID="ID_183653783" MODIFIED="1329827156622" POSITION="left" TEXT="Scheduling">
<node CREATED="1329316645829" ID="ID_1020675289" MODIFIED="1329316665953" TEXT="Add new schedulers">
<node CREATED="1329316665954" ID="ID_2048019" MODIFIED="1329316672946" TEXT="Convert StarPU schedulers"/>
</node>
<node CREATED="1329316675989" FOLDED="true" ID="ID_1219835212" MODIFIED="1329827143112" TEXT="Improve existing schedulers">
<node CREATED="1329316688277" ID="ID_1662506739" MODIFIED="1329316710774" TEXT="Better support for multi-devices">
<node CREATED="1329316710775" ID="ID_497363439" MODIFIED="1329316718738" TEXT="Load-balancing"/>
<node CREATED="1329316720853" ID="ID_597719241" MODIFIED="1329316729941" TEXT="Data-aware scheduling"/>
</node>
<node CREATED="1329316734037" ID="ID_1210385147" MODIFIED="1329316741602" TEXT="Better memory management">
<node CREATED="1329316743045" ID="ID_1804125835" MODIFIED="1329316758674" TEXT="Support for out-of-memory exception"/>
</node>
<node CREATED="1329317205493" ID="ID_570193014" MODIFIED="1329318783659" TEXT="Use profiling informations">
<arrowlink DESTINATION="ID_382790587" ENDARROW="Default" ENDINCLINATION="912;0;" ID="Arrow_ID_179783243" STARTARROW="None" STARTINCLINATION="912;0;"/>
</node>
</node>
</node>
<node CREATED="1329316849893" FOLDED="true" ID="ID_1383982615" MODIFIED="1329474185459" POSITION="left" TEXT="Various">
<node CREATED="1329316856693" ID="ID_1792059412" MODIFIED="1329316877154" TEXT="Release entities on JVM shutdown (addShutdownHook)"/>
</node>
<node CREATED="1329317674661" FOLDED="true" ID="ID_1674903189" MODIFIED="1329845623260" POSITION="left" TEXT="Data">
<node CREATED="1329317685733" ID="ID_1066723769" MODIFIED="1329317695842" TEXT="Support new data types">
<node CREATED="1329317696853" ID="ID_1860805070" MODIFIED="1329317707154" TEXT="Matrix 3D"/>
<node CREATED="1329317708213" ID="ID_1204560052" MODIFIED="1329317731269" TEXT="Graph"/>
</node>
<node CREATED="1329317739269" FOLDED="true" ID="ID_1635782699" MODIFIED="1329495007850" TEXT="Support data slicing">
<node CREATED="1329317779781" ID="ID_535111830" MODIFIED="1329317790194" TEXT="Matrix cell"/>
<node CREATED="1329317792165" ID="ID_195068225" MODIFIED="1329317794450" TEXT="Sub-matrix"/>
</node>
<node CREATED="1329321092694" FOLDED="true" ID="ID_1538067226" MODIFIED="1329845622580" TEXT="Data initialization">
<node CREATED="1329321098934" ID="ID_1001371722" MODIFIED="1329321110581" TEXT="Initialize data only when needed"/>
<node CREATED="1329321112390" ID="ID_1431621647" MODIFIED="1329321133283" TEXT="Support different data sources (file, memory, computation...)"/>
</node>
<node CREATED="1329395708268" ID="ID_167131347" MODIFIED="1329395721020" TEXT="Support indianness (automatic conversions)"/>
<node CREATED="1329845612324" ID="ID_815894984" MODIFIED="1329845615093" TEXT="Prefetching"/>
</node>
<node CREATED="1329827048375" FOLDED="true" ID="ID_1322233537" MODIFIED="1329845625044" POSITION="right" TEXT="Kernels">
<node CREATED="1329317037701" ID="ID_1435417967" MODIFIED="1329827039899" TEXT="Generic kernels">
<node CREATED="1329317043093" ID="ID_1897243431" MODIFIED="1329317053746" TEXT="Support data-parallel kernels"/>
<node CREATED="1329317056565" ID="ID_1670560709" MODIFIED="1329317066626" TEXT="Compilers">
<node CREATED="1329317069221" ID="ID_1445870281" MODIFIED="1329317070882" TEXT="C"/>
<node CREATED="1329317072181" ID="ID_597193201" MODIFIED="1329317073282" TEXT="CUDA"/>
<node CREATED="1329317098229" ID="ID_1480963145" MODIFIED="1329317099714" TEXT="PTX"/>
<node CREATED="1329317074533" ID="ID_1043434822" MODIFIED="1329317076210" TEXT="OpenCL"/>
<node CREATED="1329317104869" ID="ID_1050777950" MODIFIED="1329317125778" TEXT="x86, x86-64, SSE"/>
</node>
</node>
<node CREATED="1329318402773" FOLDED="true" ID="ID_1917565172" MODIFIED="1329474189116" TEXT="Kernel library">
<node CREATED="1329318410293" ID="ID_486121241" MODIFIED="1329318434722" TEXT="Dense Linear Algebra"/>
<node CREATED="1329318419605" ID="ID_962026327" MODIFIED="1329318448565" TEXT="Sparse Linear Algebra"/>
<node CREATED="1329321556870" ID="ID_1944826975" MODIFIED="1329321563396" TEXT="Cache compiled kernels"/>
</node>
<node CREATED="1329827065919" ID="ID_1336352714" MODIFIED="1329827077404" TEXT="Support kernel modifying data inplace">
<node CREATED="1329316893733" ID="ID_138603183" MODIFIED="1329827021463" TEXT="Data-duplication">
<node CREATED="1329316901365" ID="ID_1002679536" MODIFIED="1329316908706" TEXT="Support knem"/>
<node CREATED="1329316910277" ID="ID_1569518605" MODIFIED="1329316932162" TEXT="Duplication kernels (GPU,CPU)"/>
</node>
</node>
</node>
</node>
</map>
