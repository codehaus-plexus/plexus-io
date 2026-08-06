# File Selectors

A file selector is a plexus component, which allows to select certain files out of a given set. For example, the [Plexus Archiver](http://plexus.codehaus.org/plexus-archiver) uses file selectors to select the files being archived out of a base directory. Its counterpart, the Plexus Unarchiver allows to restrict the files to unarchive.

File mappers are implementing the interface [FileSelector](./apidocs/org/codehaus/plexus/components/io/fileselectors/FileSelector.html).

Available file selectors are

- The [All Files Selector](#All_Files_Selector); it uses the role hints "default", or "all".
- The [Standard File Selector](#Standard_File_Selector); its role hint is "standard".

## <a id="All_Files_Selector"></a>All Files Selector

The [selector for all files](./apidocs/org/codehaus/plexus/components/io/fileselectors/AllFilesFileSelector.html) doesn't exclude any files. It is mainly useful when you want to avoid the value null for a file selector.

A configuration snippet for using the selector for all files would look like this:

```
<fileSelector implementation="org.codehaus.plexus.components.io.fileselectors.AllFilesFileSelector"/>
```

The selector for all files uses the role hints "all", or "default".

## <a id="Standard_File_Selector"></a>Standard File Selector

The [standard file selector](./apidocs/org/codehaus/plexus/components/io/fileselectors/IncludeExcludeFileSelector) selects files based on include/exclude patterns.

A configuration snippet for using the standard file selector would look like this:

```
<fileSelector implementation="org.codehaus.plexus.components.io.filemappers.FileExtensionMapper">
  <includes>
    <include>**/*.gif</include>
    <include>**/*.png</include>
    <include>**/*.jpg</include>
    <include>**/*.jpeg</include>
  </includes>
  <excludes>
    <exclude>bar/</exclude>
  </excludes>
  <useDefaultExcludes>true</useDefaultExcludes>
  <caseSensitive>false</caseSensitive>
</fileSelector>
```

This would include all image files, with the exception of those in the directory `bar`. The default excludes (for example `CVS/`) would apply and file names would be treated case insensitive.
