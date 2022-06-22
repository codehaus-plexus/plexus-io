package org.codehaus.plexus.components.io.resources;

import java.nio.charset.Charset;

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

/**
 * Implemented by plexus io resources that support some kind of encoding notion
 */
public interface EncodingSupported
{
    /**
     * Supplies the encoding to be used for decoding filenames/paths
     * @param charset The charset to use
     */
    void setEncoding( Charset charset );
}
