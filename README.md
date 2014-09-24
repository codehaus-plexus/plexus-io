Upgrade notes
==============

In 2.1, all subclasses of AbstractPlexusIoResource now have to call appropriate parent
constructor, there are no more setters.

AbstractPlexusIOResourceWithAttributes no longer exists, extend  AbstractPlexusIoResource and
implement interface PlexusIoResourceWithAttributes instead.
