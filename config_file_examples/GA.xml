<configuration>
  <module class="PSP.PSPLoggerModule">
    <property name="filename">outputGALog.csv</property>
    <property name="loggingPerEvaluation">false</property>
    <property name="evaluationStep">100</property>
    <property name="loggingPerIteration">true</property>
    <property name="iterationStep">1</property>
  </module>
  <module class="PSP.PSPModule">
    <property name="pspInstanceFileName">problem-instance-examples/instance_sample7.txt</property>
    <property name="k">4</property>
    <property name="initGenotypeValue">-1</property>
    <property name="wCost">0.01</property>
    <property name="wDuration">0.1</property>
    <property name="skillConstraintMode">EMPLOYEES_ALL_SKILLS</property>
    <property name="normalisation">true</property>
  </module>
  <module class="PSP.PlusCrossoverModule">
    <property name="booleanType">RATE</property>
    <property name="booleanRate">0.5</property>
    <property name="booleanXPoints">1</property>
    <property name="doubleType">SBX</property>
    <property name="alpha">0.5</property>
    <property name="nu">15.0</property>
    <property name="integerType">RATE</property>
    <property name="integerRate">0.0</property>
    <property name="integerXPoints">1</property>
    <property name="permutationType">ONEPOINT</property>
    <property name="rotation">false</property>
    <property name="pspCrossoverType">MIXED_EMPLOYEE_TASK</property>
  </module>
  <module class="org.opt4j.common.archive.ArchiveModule">
    <property name="type">POPULATION</property>
    <property name="capacity">100</property>
    <property name="divisions">7</property>
  </module>
  <module class="org.opt4j.common.random.RandomModule">
    <property name="type">MERSENNE_TWISTER</property>
    <property name="usingSeed">true</property>
    <property name="seed">7</property>
  </module>
  <module class="org.opt4j.operator.mutate.BasicMutateModule">
    <property name="doubleType">POLYNOMIAL</property>
    <property name="eta">20.0</property>
    <property name="sigma">0.1</property>
    <property name="mutationRateType">CONSTANT</property>
    <property name="mutationRate">0.02</property>
    <property name="permutationType">MIXED</property>
  </module>
  <module class="org.opt4j.optimizer.ea.EvolutionaryAlgorithmModule">
    <property name="generations">79</property>
    <property name="alpha">64</property>
    <property name="mu">64</property>
    <property name="lambda">64</property>
    <property name="crossoverRate">0.75</property>
  </module>
  <module class="org.opt4j.optimizer.ea.Nsga2Module">
    <property name="tournament">1</property>
  </module>
</configuration>
