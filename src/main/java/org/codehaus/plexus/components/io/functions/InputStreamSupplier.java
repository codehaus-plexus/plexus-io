package org.codehaus.plexus.components.io.functions;

import java.io.IOException;
import java.io.InputStream;

/*
 * Supplies a T.
 *
 * Someday this will extends java.util.function.Supplier
 */
public interface InputStreamSupplier
{
    InputStream get() throws IOException;
}
