/*
 * Copyright (c) 2013 European Bioinformatics Institute (EMBL-EBI)
 *                    John May <jwmay@users.sf.net>
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. All we ask is that proper credit is given
 * for our work, which includes - but is not limited to - adding the above
 * copyright notice to the beginning of your source code files, and to any
 * copyright notice that you may distribute with programs based on this work.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 U
 */

package org.openscience.cdk.isomorphism;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.isomorphism.matchers.IQueryBond;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openscience.cdk.interfaces.IChemObject.AROMATIC;

/**
 * @author John May
 */
class BondMatcherTest {

    @Test
    void anyMatch() {
        BondMatcher matcher = BondMatcher.forAny();
        IBond bond1 = mock(IBond.class);
        IBond bond2 = mock(IBond.class);
        IBond bond3 = mock(IBond.class);
        Assertions.assertTrue(matcher.matches(bond1, bond2));
        Assertions.assertTrue(matcher.matches(bond2, bond1));
        Assertions.assertTrue(matcher.matches(bond1, bond3));
        Assertions.assertTrue(matcher.matches(bond1, null));
        Assertions.assertTrue(matcher.matches(null, null));
    }

    @Test
    void aromaticMatch() {
        BondMatcher matcher = BondMatcher.forOrder();
        IBond bond1 = mock(IBond.class);
        IBond bond2 = mock(IBond.class);
        when(bond1.getFlag(AROMATIC)).thenReturn(true);
        when(bond2.getFlag(AROMATIC)).thenReturn(true);
        when(bond1.getOrder()).thenReturn(IBond.Order.SINGLE);
        when(bond2.getOrder()).thenReturn(IBond.Order.DOUBLE);
        Assertions.assertTrue(matcher.matches(bond1, bond2));
        Assertions.assertTrue(matcher.matches(bond2, bond1));
    }

    @Test
    void aliphaticMatch() {
        BondMatcher matcher = BondMatcher.forOrder();
        IBond bond1 = mock(IBond.class);
        IBond bond2 = mock(IBond.class);
        when(bond1.getFlag(AROMATIC)).thenReturn(false);
        when(bond2.getFlag(AROMATIC)).thenReturn(false);
        when(bond1.getOrder()).thenReturn(IBond.Order.SINGLE);
        when(bond2.getOrder()).thenReturn(IBond.Order.SINGLE);
        Assertions.assertTrue(matcher.matches(bond1, bond2));
        Assertions.assertTrue(matcher.matches(bond2, bond1));
    }

    @Test
    void aromaticStrictMatch() {
        BondMatcher matcher = BondMatcher.forStrictOrder();
        IBond bond1 = mock(IBond.class);
        IBond bond2 = mock(IBond.class);
        when(bond1.getFlag(AROMATIC)).thenReturn(true);
        when(bond2.getFlag(AROMATIC)).thenReturn(true);
        when(bond1.getOrder()).thenReturn(IBond.Order.SINGLE);
        when(bond2.getOrder()).thenReturn(IBond.Order.DOUBLE);
        Assertions.assertTrue(matcher.matches(bond1, bond2));
        Assertions.assertTrue(matcher.matches(bond2, bond1));
    }

    @Test
    void aliphaticStrictMatch() {
        BondMatcher matcher = BondMatcher.forStrictOrder();
        IBond bond1 = mock(IBond.class);
        IBond bond2 = mock(IBond.class);
        when(bond1.getFlag(AROMATIC)).thenReturn(false);
        when(bond2.getFlag(AROMATIC)).thenReturn(false);
        when(bond1.getOrder()).thenReturn(IBond.Order.SINGLE);
        when(bond2.getOrder()).thenReturn(IBond.Order.SINGLE);
        Assertions.assertTrue(matcher.matches(bond1, bond2));
        Assertions.assertTrue(matcher.matches(bond2, bond1));
    }

    @Test
    void aliphaticMismatch_aromatic() {
        BondMatcher matcher = BondMatcher.forOrder();
        IBond bond1 = mock(IBond.class);
        IBond bond2 = mock(IBond.class);
        when(bond1.getFlag(AROMATIC)).thenReturn(true);
        when(bond2.getFlag(AROMATIC)).thenReturn(false);
        when(bond1.getOrder()).thenReturn(IBond.Order.SINGLE);
        when(bond2.getOrder()).thenReturn(IBond.Order.SINGLE);
        Assertions.assertTrue(matcher.matches(bond1, bond2));
        Assertions.assertTrue(matcher.matches(bond2, bond1));
    }

    @Test
    void aliphaticStrictMismatch_aromatic() {
        BondMatcher matcher = BondMatcher.forStrictOrder();
        IBond bond1 = mock(IBond.class);
        IBond bond2 = mock(IBond.class);
        when(bond1.getFlag(AROMATIC)).thenReturn(true);
        when(bond2.getFlag(AROMATIC)).thenReturn(false);
        when(bond1.getOrder()).thenReturn(IBond.Order.SINGLE);
        when(bond2.getOrder()).thenReturn(IBond.Order.SINGLE);
        Assertions.assertFalse(matcher.matches(bond1, bond2));
        Assertions.assertFalse(matcher.matches(bond2, bond1));
    }

    @Test
    void aliphaticMismatch_order() {
        BondMatcher matcher = BondMatcher.forOrder();
        IBond bond1 = mock(IBond.class);
        IBond bond2 = mock(IBond.class);
        when(bond1.getFlag(AROMATIC)).thenReturn(false);
        when(bond2.getFlag(AROMATIC)).thenReturn(false);
        when(bond1.getOrder()).thenReturn(IBond.Order.SINGLE);
        when(bond2.getOrder()).thenReturn(IBond.Order.DOUBLE);
        Assertions.assertFalse(matcher.matches(bond1, bond2));
        Assertions.assertFalse(matcher.matches(bond2, bond1));
    }

    @Test
    void aliphaticStrictMismatch_order() {
        BondMatcher matcher = BondMatcher.forStrictOrder();
        IBond bond1 = mock(IBond.class);
        IBond bond2 = mock(IBond.class);
        when(bond1.getFlag(AROMATIC)).thenReturn(false);
        when(bond2.getFlag(AROMATIC)).thenReturn(false);
        when(bond1.getOrder()).thenReturn(IBond.Order.SINGLE);
        when(bond2.getOrder()).thenReturn(IBond.Order.DOUBLE);
        Assertions.assertFalse(matcher.matches(bond1, bond2));
        Assertions.assertFalse(matcher.matches(bond2, bond1));
    }

    @Test
    void queryMatch() {
        BondMatcher matcher = BondMatcher.forQuery();
        IQueryBond bond1 = mock(IQueryBond.class);
        IBond bond2 = mock(IBond.class);
        IBond bond3 = mock(IBond.class);
        when(bond1.matches(bond2)).thenReturn(true);
        when(bond1.matches(bond3)).thenReturn(false);
        Assertions.assertTrue(matcher.matches(bond1, bond2));
        Assertions.assertFalse(matcher.matches(bond1, bond3));
    }
}
