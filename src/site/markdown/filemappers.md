# File Mappers

A file mapper is a plexus component, which allows to convert file names. File mappers are used when creating files. For example, the [XML Maven Plugin](https://www.mojohaus.org/xml-maven-plugin) allows to specify a file mapper when creating files by XSLT transformation.

File mappers are implementing the interface [FileMapper](./apidocs/org/codehaus/plexus/components/io/filemappers/FileMapper.html). The idea of file mappers is borrowed from the [Ant FileMapper](https://ant.apache.org/manual/Types/mapper.html), which serves the same purpose within Ant.

Available file mappers are

- The [Identity Mapper](#Identity_Mapper); it uses the role hints "default", or "identity".
- The [File Extension Mapper](#File_Extension_Mapper); its role hint is "fileExtension".
- The [Flattening File Mapper](#Flattening_File_Mapper) with the role hint of "flatten".
- The [Merging File Mapper](#Merging_File_Mapper); its role hint is "merge".
- The [Suffix File Mapper](#Suffix_File_Mapper); its role hint is "suffix".

## <a id="Identity_Mapper"></a>Identity Mapper

The [identity mapper](./apidocs/org/codehaus/plexus/components/io/filemappers/IdentityMapper.html) maps any file name to itself. This may be handy, where you want to avoid the value null for file mappers. The identity takes no configuration parameters.

For example, to use the identity mapper within the XML Maven Plugins `transform` goal, you would use the following configuration snipped:

```
<fileMapper implementation="org.codehaus.plexus.components.io.filemappers.IdentityMapper"/>
```

The identity mapper uses the role hints "identity", or "default".

## <a id="File_Extension_Mapper"></a>File Extension Mapper

The [file extension mapper](./apidocs/org/codehaus/plexus/components/io/filemappers/FileExtensionMapper.html) changes the extension of the created files. For example, if you would use the XML Maven Plugin to convert Docbook into FOP or PDF files, then you would want the generated files to have the extension ".fo", or ".pdf".

A configuration snippet for using the identity mapper within the XML Maven Plugins `transform` goal would look like this:

```
<fileMapper implementation="org.codehaus.plexus.components.io.filemappers.FileExtensionMapper">
  <targetExtension>.pdf</targetExtension>
</fileMapper>
```

The file extension mapper uses the role hints "fileExtension".

## <a id="Flattening_File_Mapper"></a>Flattening File Mapper

The [flattening file mapper](./apidocs/org/codehaus/plexus/components/io/filemappers/FlattenFileMapper.html) is used to flatten a directory structure: It removes all directory components. For example, it would convert the name `META-INF/MANIFEST.MF` to `MANIFEST.MF`.

The flattening file mapper takes no configuration parameters. Consequently, a typical configuration snippet would look like this:

```
<fileMapper implementation="org.codehaus.plexus.components.io.filemappers.FlattenFileMapper"/>
```

The flattening file mapper uses the role hint "flatten".

## <a id="Merging_File_Mapper"></a>Merging File Mapper

The [merging file mapper](./apidocs/org/codehaus/plexus/components/io/filemappers/MergeFileMapper.html) merges all possible file names into one file name. In other words, it performs a constant mapping. For example, a merging file mapper, which maps all possible file names to `theOneAndOnlyFile` would be configured as follows:

```
<fileMapper implementation="org.codehaus.plexus.components.io.filemappers.MergeFileMapper">
  <targetName>theOneAndOnlyFile</targetName>
</fileMapper>
```

The merging file mapper uses the role hint "merge".

## <a id="Suffix_File_Mapper"></a>Suffix File Mapper

The [suffix file mapper](./apidocs/org/codehaus/plexus/components/io/filemappers/SuffixFileMapper.html) adds the given suffix to the filename. The suffix will be added before the file extension. Examples :

```
theFile.txt => theFileNiceSuffix.txt
dir/file.java => dir/fileNiceSuffix.java
fileWithoutExtension => fileWithoutExtensionNiceSuffix
dir/archive.tar.gz => dir/archiveNiceSuffix.tar.gz
```

It would be configured as follows:

```
<fileMapper implementation="org.codehaus.plexus.components.io.filemappers.SuffixFileMapper">
  <suffix>NiceSuffix</suffix>
</fileMapper>
```

The suffix file mapper uses the role hint "suffix".
