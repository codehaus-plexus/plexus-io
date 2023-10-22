package org.codehaus.plexus.components.io.functions;

/*
 * Copyright 2014 The Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.IOException;
import java.io.InputStream;

import org.codehaus.plexus.components.io.resources.PlexusIoResource;
import org.jetbrains.annotations.NotNull;

/**
 * Transform a stream into some other kind of stream. May be used to apply filtering or other
 * kinds of transformations.
 */
public interface InputStreamTransformer {
    /**
     * Transform the supplied input stream into another input stream.
     * <p>
     * The close method will be delegated through the entire call chain
     *
     * @param resource The p-io resource the stream is for
     * @param inputStream The stream to transform
     * @return A transformed stream or possibly the supplied stream
     * @throws IOException
     */
    @NotNull
    InputStream transform(@NotNull PlexusIoResource resource, @NotNull InputStream inputStream) throws IOException;
}
