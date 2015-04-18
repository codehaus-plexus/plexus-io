JDK Versions and compatibility
==============================

plexus-io supports java 1.5+ and has a number of jdk-level specific features. Since it requires 1.7+ to build,
you can force plexus-io to use "downgrade" features even when running on 1.7, using the system property -Djava.language.downgrade=<anything>

It is therefore recommended to run mvn -Djava.language.downgrade=true clean install before relasing, to assess
 that features still work with older jdk versions.


Version 2.6
===========
PlexusIoZipFileResourceCollection removed from plexus io and added to plexus-archiver 2.10. Older versions
of plexus-archiver may not be compatible with this.


Version 2.1
===========

In 2.1, all subclasses of AbstractPlexusIoResource now have to call appropriate parent
constructor, there are no more setters.

AbstractPlexusIOResourceWithAttributes no longer exists, extend  AbstractPlexusIoResource and
implement interface PlexusIoResourceWithAttributes instead.
