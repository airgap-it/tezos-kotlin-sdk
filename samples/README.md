# Tezos Kotlin SDK: Samples

An interactive playground to try out the library's features.

## Run

The samples are provided as unit tests. They can be run from the console using the Gradle wrapper, or directly in your
favourite IDE, as long as it supports it.

To run them from the console, simply call:

```shell
$ ./gradlew samples:test
```

You can also specify a single sample to run with:

```shell
$ ./gradlew samples:test --tests SampleClass.sampleMethod # e.g. BasicSamples.explicit
```

See also [how to filter test suites in Gradle](https://docs.gradle.org/current/userguide/java_testing.html#test_filtering)
to learn how to run the samples in more customized way.
