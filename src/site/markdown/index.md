# Plexus IO

Plexus IO is a set of plexus components, which are designed for use in I/O operations. These I/O operations are doing nothing spectacular. For example, [Commons IO](http://jakarta.apache.org/commons/io) is a much more powerful library in the same area. However, the implementation as a plexus component allows reuse in Maven.

The following component groups are currently available:

- [File Mappers](./filemappers.html)
- [File Selectors](./fileselectors.html)

Plexus IO components are typically very simple components, who could very well live as part of the [Plexus Utils](http://plexus.codehaus.org/plexus-utils). They do not, because Plexus Utils is a dependency of the [Plexus Component API](http://plexus.codehaus.org/plexus-containers/plexus-container-default), which is in turn a dependency of the Plexus IO test suite (of course, a Plexus container is required to test components, even if they are POJO's). In other words, Plexus Utils cannot contain components, because that would introduce a circular dependency.
