# Plexus IO

[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.plexus/plexus-io.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.codehaus.plexus/plexus-io)
[![GitHub CI](https://github.com/codehaus-plexus/plexus-io/actions/workflows/maven.yml/badge.svg)](https://github.com/codehaus-plexus/plexus-io/actions/workflows/maven.yml)
[![Reproducible Builds](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/jvm-repo-rebuild/reproducible-central/master/content/org/codehaus/plexus/plexus-io/badge.json)](https://github.com/jvm-repo-rebuild/reproducible-central/blob/master/content/org/codehaus/plexus/plexus-io/README.md)

Abstractions over files and resources: a uniform view of something to be read whether it is on disk, in an
archive or in memory, plus the selectors and mappers used to filter and rename those resources.

Most people meet it through [`plexus-archiver`](https://github.com/codehaus-plexus/plexus-archiver), which
uses it to decide what goes into an archive and under what name.

## Status

Maintained, conservatively. The API is stable and widely depended on transitively, so changes are mostly
bug fixes and dependency updates.

## Using it

```xml
<dependency>
  <groupId>org.codehaus.plexus</groupId>
  <artifactId>plexus-io</artifactId>
  <version>3.6.0</version>
</dependency>
```

Check the badge above for the current version.

## Requirements

Java 8 or later.

## Documentation

- [Project site](https://codehaus-plexus.github.io/plexus-io/) — including [file mappers](https://codehaus-plexus.github.io/plexus-io/filemappers.html) and [file selectors](https://codehaus-plexus.github.io/plexus-io/fileselectors.html)
- [Javadoc](https://javadoc.io/doc/org.codehaus.plexus/plexus-io)
- [Release notes](https://github.com/codehaus-plexus/plexus-io/releases)

## Contributing

See [CONTRIBUTING.md](https://github.com/codehaus-plexus/.github/blob/master/CONTRIBUTING.md). In short:
`mvn verify` builds, and run `mvn spotless:apply` before pushing or CI will fail on formatting.

Please report security vulnerabilities privately — see
[SECURITY.md](https://github.com/codehaus-plexus/.github/blob/master/SECURITY.md), not a public issue.
