# SoftwareProjectScheduling

This repository contains the code used for the paper:

MINKU, L. L.; SUDHOLT, D.; YAO, X. . "Improved Evolutionary Algorithm Design for the Project Scheduling Problem Based on Runtime Analysis", IEEE Transactions on Software Engineering, IEEE, v. 40, n. 1, p. 83-102, January 2014.

Author of code:

Leandro L. Minku, University of Birmingham, UK.

The code requires the following libraries, which are available in the lib folder under their respective licenses:
- Opt4j framework for meta-heuristic optimisation version 2.4,
- mockito framework for unit tests in Java version 1.8.5,
- junit.

To run via GUI, use the main class org.opt4j.start.Opt4J. This will open the Opt4j configurator, which enables to create different types of evolutionary algorithms. Examples of configurations for running the algorithms investigated in the paper above can be found in the folder config-file-examples:

- GA.sh for the Genetic Algorithm proposed in the paper above,
- RLS.sh for the Randomised Local Search used in the paper above,
- OnePlusOneEA.sh for the One-Plus-One Evolutionary algorithm proposed in the paper above.

To run from the command line, use the command below:

java -cp pspea.jar:opt4j-2.4.jar:junit.jar org.opt4j.start.Opt4JStarter <configFile.xml>

Examples of toy software project scheduling problem instances can be found in the folder problem-instance-examples.

More information on running the code can be found in the readme.pdf file. Information about the algorithms implemented can be found in the paper and in Dirk's report project-scheduling.pdf.
