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

package org.openscience.cdk.isomorphism.matchers.smarts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author John May
 */
class RingMembershipAtomTest {

    @Test
    void matches() throws Exception {
        RingMembershipAtom matcher = new RingMembershipAtom(2, mock(IChemObjectBuilder.class));
        IAtom atom = mock(IAtom.class);
        when(atom.getProperty(SMARTSAtomInvariants.KEY))
                .thenReturn(
                        new SMARTSAtomInvariants(mock(IAtomContainer.class), 0, 2, Collections.emptySet(), 0,
                                0, 0, 0));
        Assertions.assertTrue(matcher.matches(atom));
    }

    @Test
    void mismatches() throws Exception {
        RingMembershipAtom matcher = new RingMembershipAtom(2, mock(IChemObjectBuilder.class));
        IAtom atom = mock(IAtom.class);
        when(atom.getProperty(SMARTSAtomInvariants.KEY))
                .thenReturn(
                        new SMARTSAtomInvariants(mock(IAtomContainer.class), 0, 1, Collections.emptySet(), 0,
                                0, 0, 0));
        Assertions.assertFalse(matcher.matches(atom));
    }

    @Test
    void none() throws Exception {
        RingMembershipAtom matcher = new RingMembershipAtom(0, mock(IChemObjectBuilder.class));
        IAtom atom = mock(IAtom.class);
        when(atom.getProperty(SMARTSAtomInvariants.KEY))
                .thenReturn(
                        new SMARTSAtomInvariants(mock(IAtomContainer.class), 0, 0, Collections.emptySet(), 0,
                                0, 0, 0));
        Assertions.assertTrue(matcher.matches(atom));
    }

    @Test
    void any() throws Exception {
        RingMembershipAtom matcher = new RingMembershipAtom(-1, mock(IChemObjectBuilder.class));
        IAtom atom = mock(IAtom.class);
        when(atom.getProperty(SMARTSAtomInvariants.KEY))
                .thenReturn(
                        new SMARTSAtomInvariants(mock(IAtomContainer.class), 0, 5, Collections.emptySet(), 2,
                                0, 0, 0));
        Assertions.assertTrue(matcher.matches(atom));
    }
}
