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
package com.diffplug.common.base;

import java.util.function.Function;
import java.util.function.Predicate;

import junit.framework.TestCase;

import com.diffplug.common.annotations.GwtCompatible;
import com.diffplug.common.annotations.GwtIncompatible;
import com.diffplug.common.base.Equivalence.Wrapper;
import com.diffplug.common.collect.ImmutableList;
import com.diffplug.common.testing.EqualsTester;
import com.diffplug.common.testing.EquivalenceTester;
import com.diffplug.common.testing.NullPointerTester;
import com.diffplug.common.testing.SerializableTester;

/**
 * Unit test for {@link Equivalence}.
 *
 * @author Jige Yu
 */
@GwtCompatible(emulated = true)
public class EquivalenceTest extends TestCase {
	@SuppressWarnings("unchecked") // varargs
	public void testPairwiseEquivalent() {
		EquivalenceTester.of(Equivalence.equals().<String> pairwise())
				.addEquivalenceGroup(ImmutableList.<String> of())
				.addEquivalenceGroup(ImmutableList.of("a"))
				.addEquivalenceGroup(ImmutableList.of("b"))
				.addEquivalenceGroup(ImmutableList.of("a", "b"), ImmutableList.of("a", "b"))
				.test();
	}

	public void testPairwiseEquivalent_equals() {
		new EqualsTester()
				.addEqualityGroup(Equivalence.equals().pairwise(), Equivalence.equals().pairwise())
				.addEqualityGroup(Equivalence.identity().pairwise())
				.testEquals();
	}

	private enum LengthFunction implements Function<String, Integer> {
		INSTANCE;

		@Override
		public Integer apply(String input) {
			return input.length();
		}
	}

	private static final Equivalence<String> LENGTH_EQUIVALENCE = Equivalence.equals()
			.onResultOf(LengthFunction.INSTANCE);

	public void testWrap() {
		new EqualsTester()
				.addEqualityGroup(
						LENGTH_EQUIVALENCE.wrap("hello"),
						LENGTH_EQUIVALENCE.wrap("hello"),
						LENGTH_EQUIVALENCE.wrap("world"))
				.addEqualityGroup(
						LENGTH_EQUIVALENCE.wrap("hi"),
						LENGTH_EQUIVALENCE.wrap("yo"))
				.addEqualityGroup(
						LENGTH_EQUIVALENCE.wrap(null),
						LENGTH_EQUIVALENCE.wrap(null))
				.addEqualityGroup(Equivalence.equals().wrap("hello"))
				.addEqualityGroup(Equivalence.equals().wrap(null))
				.testEquals();
	}

	public void testWrap_get() {
		String test = "test";
		Wrapper<String> wrapper = LENGTH_EQUIVALENCE.wrap(test);
		assertSame(test, wrapper.get());
	}

	@GwtIncompatible("SerializableTester")
	public void testSerialization() {
		SerializableTester.reserializeAndAssert(LENGTH_EQUIVALENCE.wrap("hello"));
		SerializableTester.reserializeAndAssert(Equivalence.equals());
		SerializableTester.reserializeAndAssert(Equivalence.identity());
	}

	private static class IntValue {
		private final int value;

		IntValue(int value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "value = " + value;
		}
	}

	public void testOnResultOf() {
		EquivalenceTester.of(Equivalence.equals().onResultOf(Functions.toStringFunction()))
				.addEquivalenceGroup(new IntValue(1), new IntValue(1))
				.addEquivalenceGroup(new IntValue(2))
				.test();
	}

	public void testOnResultOf_equals() {
		new EqualsTester()
				.addEqualityGroup(
						Equivalence.identity().onResultOf(Functions.toStringFunction()),
						Equivalence.identity().onResultOf(Functions.toStringFunction()))
				.addEqualityGroup(Equivalence.equals().onResultOf(Functions.toStringFunction()))
				.addEqualityGroup(Equivalence.identity().onResultOf(Functions.identity()))
				.testEquals();
	}

	public void testEquivalentTo() {
		Predicate<Object> equalTo1 = Equivalence.equals().equivalentTo("1");
		assertTrue(equalTo1.test("1"));
		assertFalse(equalTo1.test("2"));
		assertFalse(equalTo1.test(null));
		Predicate<Object> isNull = Equivalence.equals().equivalentTo(null);
		assertFalse(isNull.test("1"));
		assertFalse(isNull.test("2"));
		assertTrue(isNull.test(null));

		new EqualsTester()
				.addEqualityGroup(equalTo1, Equivalence.equals().equivalentTo("1"))
				.addEqualityGroup(isNull)
				.addEqualityGroup(Equivalence.identity().equivalentTo("1"))
				.testEquals();
	}

	public void testEqualsEquivalent() {
		EquivalenceTester.of(Equivalence.equals())
				.addEquivalenceGroup(new Integer(42), 42)
				.addEquivalenceGroup("a")
				.test();
	}

	public void testIdentityEquivalent() {
		EquivalenceTester.of(Equivalence.identity())
				.addEquivalenceGroup(new Integer(42))
				.addEquivalenceGroup(new Integer(42))
				.addEquivalenceGroup("a")
				.test();
	}

	public void testEquals() {
		new EqualsTester()
				.addEqualityGroup(Equivalence.equals(), Equivalence.equals())
				.addEqualityGroup(Equivalence.identity(), Equivalence.identity())
				.testEquals();
	}

	@GwtIncompatible("NullPointerTester")
	public void testNulls() {
		new NullPointerTester().testAllPublicStaticMethods(Equivalence.class);
		new NullPointerTester().testAllPublicInstanceMethods(Equivalence.equals());
		new NullPointerTester().testAllPublicInstanceMethods(Equivalence.identity());
	}
}
