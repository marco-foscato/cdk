/* Copyright (C) 1997-2007  The Chemistry Development Kit (CDK) project
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
package org.openscience.cdk.debug;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.test.interfaces.AbstractSingleElectronTest;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.ISingleElectron;

/**
 * Checks the functionality of the {@link DebugSingleElectron}.
 *
 */
class DebugSingleElectronTest extends AbstractSingleElectronTest {

    @BeforeAll
    static void setUp() {
        setTestObjectBuilder(DebugSingleElectron::new);
    }

    @Test
    void testDebugSingleElectron() {
        ISingleElectron radical = new DebugSingleElectron();
        Assertions.assertNull(radical.getAtom());
        Assertions.assertEquals(1, radical.getElectronCount().intValue());
    }

    @Test
    void testDebugSingleElectron_IAtom() {
        IAtom atom = newChemObject().getBuilder().newInstance(IAtom.class, "N");
        ISingleElectron radical = new DebugSingleElectron(atom);
        Assertions.assertEquals(1, radical.getElectronCount().intValue());
        Assertions.assertEquals(atom, radical.getAtom());
        Assertions.assertTrue(radical.contains(atom));
    }
}
