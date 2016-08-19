/*
 * Original Guava code is copyright (C) 2015 The Guava Authors.
 * Modifications from Guava are copyright (C) 2016 DiffPlug.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diffplug.common.util.concurrent;

import com.diffplug.common.annotations.Beta;
import com.diffplug.common.annotations.GwtCompatible;

/**
 * Static utility methods pertaining to the {@link Runnable} interface.
 *
 * @since 16.0
 */
@Beta
@GwtCompatible
public final class Runnables {

	private static final Runnable EMPTY_RUNNABLE = new Runnable() {
		@Override
		public void run() {}
	};

	/**
	 * Returns a {@link Runnable} instance that does nothing when run.
	 */
	public static Runnable doNothing() {
		return EMPTY_RUNNABLE;
	}

	private Runnables() {}
}