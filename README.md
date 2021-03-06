[![DOI](https://zenodo.org/badge/369148596.svg)](https://zenodo.org/badge/latestdoi/369148596)

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

- GA.xml for the Genetic Algorithm proposed in the paper above,
- RLS.xml for the Randomised Local Search used in the paper above,
- OnePlusOneEA.xml for the One-Plus-One Evolutionary algorithm proposed in the paper above.

To run from the command line, use the command below:

java -cp pspea.jar:lib/opt4j-2.4.jar:lib/junit.jar org.opt4j.start.Opt4JStarter <configFile.xml>

Examples of toy software project scheduling problem instances can be found in the folder problem-instance-examples. An example of output log file generated when using GA.xml to solve the problem instance instance_sample_book.txt is in the folder example-output-log.

More information on the implementation and how to run the code can be found in readme.pdf. Information about the algorithms implemented can be found in the paper and in dirks-report-algorithm.pdf.
