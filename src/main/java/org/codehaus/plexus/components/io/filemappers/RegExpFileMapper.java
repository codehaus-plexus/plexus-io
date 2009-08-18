package org.codehaus.plexus.components.io.filemappers;

/*
 * Copyright 2007 The Codehaus Foundation.
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Implementation of a file mapper, which uses regular expressions.
 */
public class RegExpFileMapper extends AbstractFileMapper {
    /**
     * The regexp mappers role-hint: "regexp".
     */
    public static final String ROLE_HINT = "regexp";

    private Pattern pattern;
	private String replacement;
	private boolean replaceAll;

	/**
	 * Sets the regular expression pattern.
	 */
	public void setPattern(String pPattern)
	{
		pattern = Pattern.compile(pPattern);
	}

	/**
	 * Returns the regular expression pattern.
	 */
	public String getPattern()
	{
		return pattern == null ? null : pattern.pattern();
	}

	/**
	 * Sets the replacement string.
	 */
	public void setReplacement(String pReplacement)
	{
		replacement = pReplacement;
	}

	/**
	 * Returns the replacement string.
	 */
	public String getReplacement()
	{
		return replacement;
	}

	/**
	 * Returns, whether to replace the first occurrency of the pattern
	 * (default), or all.
	 */
	public boolean getReplaceAll()
	{
		return replaceAll;
	}

	/**
	 * Sets, whether to replace the first occurrency of the pattern
	 * (default), or all.
	 */
	public void setReplaceAll(boolean pReplaceAll)
	{
		replaceAll = pReplaceAll;
	}

	public String getMappedFileName(String pName)
	{
		final String name = super.getMappedFileName(pName);
		if (pattern == null)
		{
			throw new IllegalStateException("The regular expression pattern has not been set.");
		}
		if (replacement == null)
		{
			throw new IllegalStateException("The pattern replacement string has not been set.");
		}
		final Matcher matcher = pattern.matcher(name);
		if (!matcher.find())
		{
			return name;
		}
		if (!getReplaceAll())
		{
			return matcher.replaceFirst(replacement);
		}
		return matcher.replaceAll(replacement);
	}
}
