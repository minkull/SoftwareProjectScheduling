<configuration>
  <module class="PSP.PSPLoggerModule">
    <property name="filename">outputOnePlusOneEALog.csv</property>
    <property name="loggingPerEvaluation">false</property>
    <property name="evaluationStep">5064</property>
    <property name="loggingPerIteration">true</property>
    <property name="iterationStep">1</property>
  </module>
  <module class="PSP.PSPModule">
    <property name="pspInstanceFileName">problem-instance-examples/instance_sample7.txt</property>
    <property name="k">4</property>
    <property name="initGenotypeValue">-1</property>
    <property name="wCost">0.003</property>
    <property name="wDuration">0.1</property>
    <property name="skillConstraintMode">UNION_EMPLOYEES_ALL_SKILLS</property>
    <property name="normalisation">true</property>
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
  <module class="org.opt4j.optimizer.ea.ElitismSelectorModule"/>
  <module class="org.opt4j.optimizer.ea.EvolutionaryAlgorithmModule">
    <property name="generations">5064</property>
    <property name="alpha">1</property>
    <property name="mu">1</property>
    <property name="lambda">1</property>
    <property name="crossoverRate">0.0</property>
  </module>
</configuration>
