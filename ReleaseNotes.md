Plexus-IO Release Notes
========================================================================

Plexus IO 3.1.0
---------------

Plexus IO 3.1.0 requires Java 7.

### New Features

 * [Pull Request #14][pr-14] - Add new FileMapper for giving a suffix to filename:
   `SuffixFileMapper`

Plexus IO 3.0.1
---------------

Plexus IO 3.0.1 requires Java 7.

### Tasks

 * [Issue #9][issue-9], [Issue #10][issue-10] - Updated dependencies:
 `plexus-utils` to 3.1.0 and `commons-io` to 2.6

Plexus IO 3.0.0
---------------

Plexus IO 3.0.0 requires Java 7 and introduces backward incompatible changes:

 * `Java7FileAttributes` is renamed to `FileAttributes`, replacing
   the old `FileAttributes` implementation
 * `Java7AttributeUtils` is renamed to `AttributeUtils`
 * `PlexusIoResourceAttributeUtils#getFileAttributesByPath( File, boolean, boolean )`
   is deleted

### Improvements

 * [Pull Request #5][pr-5] - The required Java version is upgraded to 7.
   Classes that use native tools like `ls` are removed and the pure Java
   implementations are used instead.

### Tasks

 * [Issue #8][issue-8] - Update of `plexus-utils` to 3.0.24 and
   `commons-io` to 2.5

Plexus IO 2.7.1
---------------

### Improvements

 * [Pull Request #3][pr-3] - Introduce new constant to indicate uknown
   octal mode for resources - `PlexusIoResourceAttributes.UNKNOWN_OCTAL_MODE`

### Bugs

 * [Issue #2][issue-2] - JAR entry not found when JAR is updated inline and
   cached by URLConnection

Plexus IO 2.7
---------------

### Improvements

 * Added TIME_STYLE=long-iso to ls screen scrape
 * [Pull Request #1][pr-1] - Add concurrent access flag
   to the PlexusIoProxyResourceCollection -
   `PlexusIoResourceCollection#isConcurrentAccessSupported()`

Plexus IO 2.6.1
---------------

### Improvement

 * Performance improvement affecting mac/linux users
   with lots of small files in large archives.

Plexus IO 2.5
-------------

 * Proper support for closeable on zip archives.
 * Removed zip supporting PlexusIoZipFileResourceCollection; which now exists in plexus-archiver. (Drop in replacement,
   just change/add jar file).

Plexus IO 2.4.1
---------------

### Bugs

 * PLXCOMP-279 - PlexusIoProxyResourceCollection does not provide Closeable iterator
 * Fixed PLXCOMP-280 - SimpleResourceAttributes has incorrect value for
   default file mode

Plexus IO 2.4
-------------

### Improvements

 * PLXCOMP-274 - Simplify use of proxies
 * PLXCOMP-275 - Avoid leaky abstractions
 * PLXCOMP-276 - Reduce number of ways to create a PlexusIoResource

Plexus IO 2.3.5
---------------

### Bugs

 * PLXCOMP-278 - Symlink attribute was not preserved through merged/overridden attributes

Plexus IO 2.3.4
---------------

### Bugs

 * PLXCOMP-270 - Escaping algoritghm leaks through to system classloader
 * PLXCOMP-272 - Overriding dirmode/filemode breaks symlinks

Plexus IO 2.3.3
---------------

### Bugs

 * PLXCOMP-267 - StreamTransformers are consistently applied to all collections

Plexus IO 2.3.2
---------------

### Bugs

 * PLXCOMP-265 - Locale in shell influences "ls" parsing for screenscraper

Plexus IO 2.3.1
---------------

### Bugs

 * PLXCOMP-264 - Thread safety issue in streamconsumer

Plexus IO 2.3
-------------

### New Features

 * PLXCOMP-261 - Make plexus io collections support on-the-fly filtering

### Improvements

 * PLXCOMP-260 - Make plexus io collections iterable

Plexus IO 2.2
-------------

### Bugs

 * PLXCOMP-251 - Date parsing in "ls" screenscraping has locale dependencies
 * PLXCOMP-254 - Fix File.separatorChar normalization when prefixes are used

Plexus IO 2.1.4
---------------

### Improvements

 * PLXCOMP-250 - Upgrade maven-enforcer-plugin to 1.3.1

### Bugs

 * PLXCOMP-107 - Fail to unzip archive, which contains file with name
   'How_can_I_annotate_a_part_in_the_AAM%3F.Help'

Plexus IO 2.1.3
---------------

### Bugs

 * PLXCOMP-247 - Bug with windows AND java5

Plexus IO 2.1.2
---------------

### Bugs

 * PLXCOMP-244 - Don't try to set attributes of symbolic links
 * PLXCOMP-245 - Archives created on windows get zero permissions,
   creates malformed permissions on linux

Plexus IO 2.1.1
---------------

### Bugs

 * PLXCOMP-243 - Restore JDK1.5 compatibility

Plexus IO 2.1
-------------

### Improvements

 * PLXCOMP-64 - add symlink support to tar unarchiver
 * PLXCOMP-117 - add symbolic links managment

### Bugs

 * PLXCOMP-113 - zip unarchiver doesn't support symlinks (and trivial to fix)
 * PLXCOMP-241 - ResourcesTest.compare test failure
 * PLXCOMP-248 - Use java7 setAttributes and ignore useJvmChmod flag when applicable

Plexus IO 2.0.12
----------------

### Bugs

 * PLXCOMP-249 - Add support for java7 chmod

[issue-2]: https://github.com/codehaus-plexus/plexus-io/issues/2
[issue-8]: https://github.com/codehaus-plexus/plexus-io/issues/8
[issue-9]: https://github.com/codehaus-plexus/plexus-io/issues/9
[issue-10]: https://github.com/codehaus-plexus/plexus-io/issues/10
[pr-1]: https://github.com/codehaus-plexus/plexus-io/pull/1
[pr-3]: https://github.com/codehaus-plexus/plexus-io/pull/3
[pr-5]: https://github.com/codehaus-plexus/plexus-io/pull/5
[pr-14]: https://github.com/codehaus-plexus/plexus-io/pull/14
