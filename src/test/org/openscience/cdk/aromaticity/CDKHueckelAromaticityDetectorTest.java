/* $Revision$
 * $Author$
 * $Date$
 *
 * Copyright (C) 1997-2007  The Chemistry Development Kit (CDK) project
 *
 * Contact: cdk-devel@list.sourceforge.net
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
 */
package org.openscience.cdk.aromaticity;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.*;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.ringsearch.AllRingsFinder;
import org.openscience.cdk.ringsearch.SSSRFinder;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.RingSetManipulator;

import javax.vecmath.Point2d;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author steinbeck
 * @author egonw
 * @cdk.module test-standard
 * @cdk.created 2002-10-06
 */
public class CDKHueckelAromaticityDetectorTest extends NewCDKTestCase {


    public CDKHueckelAromaticityDetectorTest() {
        super();
    }

    @Test
    public void testDetectAromaticity_IAtomContainer() throws Exception {
        IMolecule mol = makeAromaticMolecule();

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        boolean isAromatic = CDKHueckelAromaticityDetector.detectAromaticity(mol);
        Assert.assertTrue("Molecule is expected to be marked aromatic!", isAromatic);

        int numberOfAromaticAtoms = 0;
        for (int i = 0; i < mol.getAtomCount(); i++) {
            if (((IAtom) mol.getAtom(i)).getFlag(CDKConstants.ISAROMATIC))
                numberOfAromaticAtoms++;
        }
        Assert.assertEquals(6, numberOfAromaticAtoms);

        int numberOfAromaticBonds = 0;
        for (int i = 0; i < mol.getBondCount(); i++) {
            if (((IBond) mol.getBond(i)).getFlag(CDKConstants.ISAROMATIC))
                numberOfAromaticBonds++;
        }
        Assert.assertEquals(6, numberOfAromaticBonds);

    }

    @Test public void testCDKHueckelAromaticityDetector() {
        // For autogenerated constructor
        CDKHueckelAromaticityDetector detector = new CDKHueckelAromaticityDetector();
        Assert.assertNotNull(detector);
    }

    @Test public void testNMethylPyrrol() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IMolecule mol = sp.parseSmiles("c1ccn(C)c1");
        Assert.assertTrue("Expected the molecule to be aromatic.", CDKHueckelAromaticityDetector.detectAromaticity(mol));

        IRingSet ringset = (new SSSRFinder(mol)).findSSSR();
        int numberOfAromaticRings = 0;
        RingSetManipulator.markAromaticRings(ringset);
        for (int i = 0; i < ringset.getAtomContainerCount(); i++) {
            if (ringset.getAtomContainer(i).getFlag(CDKConstants.ISAROMATIC))
                numberOfAromaticRings++;
        }
        Assert.assertEquals(1, numberOfAromaticRings);
    }

    @Test public void testPyridine() throws Exception {
        IMolecule mol = new Molecule();
        mol.addAtom(new Atom("N"));
        mol.addAtom(new Atom("C"));
        mol.addBond(0,1,IBond.Order.SINGLE);
        mol.addAtom(new Atom("C"));
        mol.addBond(1,2,IBond.Order.DOUBLE);
        mol.addAtom(new Atom("C"));
        mol.addBond(2,3,IBond.Order.SINGLE);
        mol.addAtom(new Atom("C"));
        mol.addBond(3,4,IBond.Order.DOUBLE);
        mol.addAtom(new Atom("C"));
        mol.addBond(4,5,IBond.Order.SINGLE);
        mol.addBond(0,5,IBond.Order.DOUBLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue("Expected the molecule to be aromatic.", CDKHueckelAromaticityDetector.detectAromaticity(mol));

        Iterator<IAtom> atoms = mol.atoms().iterator();
        while (atoms.hasNext()) {
        	Assert.assertTrue(atoms.next().getFlag(CDKConstants.ISAROMATIC));
        }

        IRingSet ringset = (new SSSRFinder(mol)).findSSSR();
        int numberOfAromaticRings = 0;
        RingSetManipulator.markAromaticRings(ringset);
        for (int i = 0; i < ringset.getAtomContainerCount(); i++) {
            if (ringset.getAtomContainer(i).getFlag(CDKConstants.ISAROMATIC))
                numberOfAromaticRings++;
        }
        Assert.assertEquals(1, numberOfAromaticRings);
    }

    @Test public void testCyclopentadienyl() throws Exception {
        IMolecule mol = new Molecule();
        mol.addAtom(new Atom("C"));
        mol.getAtom(0).setFormalCharge(-1);
        for (int i=1; i<5; i++) {
        	mol.addAtom(new Atom("C"));
        	mol.getAtom(i).setHybridization(IAtomType.Hybridization.SP2);
        	mol.addBond(i-1,i, IBond.Order.SINGLE);
        }
        mol.addBond(0,4,IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue("Expected the molecule to be aromatic.", CDKHueckelAromaticityDetector.detectAromaticity(mol));

        Iterator<IAtom> atoms = mol.atoms().iterator();
        while (atoms.hasNext()) {
        	Assert.assertTrue(atoms.next().getFlag(CDKConstants.ISAROMATIC));
        }
    }

    @Test public void testPyridineOxide() throws Exception {
		Molecule molecule = MoleculeFactory.makePyridineOxide();
		AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
		Assert.assertTrue(CDKHueckelAromaticityDetector.detectAromaticity(molecule));
	}

    @Test public void testPyridineOxide_SP2() throws Exception {
		Molecule molecule = MoleculeFactory.makePyridineOxide();
		Iterator<IBond> bonds = molecule.bonds().iterator();
		while (bonds.hasNext()) bonds.next().setOrder(CDKConstants.BONDORDER_SINGLE);
		for (int i=0; i<6; i++) {
			molecule.getAtom(i).setHybridization(IAtomType.Hybridization.SP2);
		}
		AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
		Assert.assertTrue(CDKHueckelAromaticityDetector.detectAromaticity(molecule));
	}

    @Test public void testFuran() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IMolecule mol = sp.parseSmiles("c1cocc1");
        Assert.assertTrue("Molecule is not detected aromatic", CDKHueckelAromaticityDetector.detectAromaticity(mol));

        IRingSet ringset = (new SSSRFinder(mol)).findSSSR();
        int numberOfAromaticRings = 0;
        RingSetManipulator.markAromaticRings(ringset);
        for (int i = 0; i < ringset.getAtomContainerCount(); i++) {
            if (ringset.getAtomContainer(i).getFlag(CDKConstants.ISAROMATIC))
                numberOfAromaticRings++;
        }
        Assert.assertEquals(1, numberOfAromaticRings);
    }

    /**
     * A unit test for JUnit The special difficulty with Azulene is that only the
     * outermost larger 10-ring is aromatic according to Hueckel rule.
     */
    @Test public void testAzulene() throws Exception {
        boolean[] testResults =
                {true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true
                };
        Molecule molecule = MoleculeFactory.makeAzulene();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        Assert.assertTrue("Expected the molecule to be aromatic.", CDKHueckelAromaticityDetector.detectAromaticity(molecule));
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals("Atom " + f + " is not correctly marked",
                    testResults[f], molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }


    /**
     * A unit test for JUnit. The N has to be counted correctly.
     */
    @Test public void testIndole() throws Exception {
        Molecule molecule = MoleculeFactory.makeIndole();
        boolean testResults[] = {
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true
        };
        //boolean isAromatic = false;
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        Assert.assertTrue("Expected the molecule to be aromatic.", CDKHueckelAromaticityDetector.detectAromaticity(molecule));
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals(
                    "Atom " + f + " is not correctly marked",
                    testResults[f],
                    molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC)
            );
        }
    }

    /**
     * A unit test for JUnit. The N has to be counted correctly.
     */
    @Test public void testPyrrole() throws Exception {
        Molecule molecule = MoleculeFactory.makePyrrole();
        boolean testResults[] = {
                true,
                true,
                true,
                true,
                true
        };
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        Assert.assertTrue("Expected the molecule to be aromatic.", CDKHueckelAromaticityDetector.detectAromaticity(molecule));
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals(
                    "Atom " + f + " is not correctly marked",
                    testResults[f],
                    molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC)
            );
        }
    }


    /**
     * A unit test for JUnit
     */
    @Test public void testThiazole() throws Exception {
        Molecule molecule = MoleculeFactory.makeThiazole();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        Assert.assertTrue("Molecule is not detected as aromatic", CDKHueckelAromaticityDetector.detectAromaticity(molecule));

        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertTrue(
                "Atom " + f + " is not correctly marked",
                molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC)
            );
        }
    }


    /**
     * A unit test for JUnit
     */
    @Test public void testTetraDehydroDecaline() throws Exception {
        boolean isAromatic = false;
        //boolean testResults[] = {true, false, false};
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IMolecule mol = sp.parseSmiles("C1CCCc2c1cccc2");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue("Expected the molecule to be aromatic.", CDKHueckelAromaticityDetector.detectAromaticity(mol));
        IRingSet rs = (new AllRingsFinder()).findAllRings(mol);
        RingSetManipulator.markAromaticRings(rs);
        IRing r = null;
        int aromacount = 0;
        Iterator<IAtomContainer> rings = rs.atomContainers().iterator();
        while (rings.hasNext()) {
            r = (IRing) rings.next();
            isAromatic = r.getFlag(CDKConstants.ISAROMATIC);

            if (isAromatic) aromacount++;            
        }
        Assert.assertEquals(1, aromacount);
    }

    /**
     * This is a bug reported for JCP.
     *
     * @cdk.bug 956924
     */
    @Test public void testSFBug956924() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IMolecule mol = sp.parseSmiles("[cH+]1cccccc1"); // tropylium cation
        Assert.assertEquals(IAtomType.Hybridization.PLANAR3, mol.getAtom(0).getHybridization());
        for (int f = 1; f < mol.getAtomCount(); f++) {
            Assert.assertEquals(IAtomType.Hybridization.SP2, mol.getAtom(f).getHybridization());
        }
        Assert.assertTrue(CDKHueckelAromaticityDetector.detectAromaticity(mol));
        Assert.assertEquals(7, mol.getAtomCount());
        for (int f = 0; f < mol.getAtomCount(); f++) {
            Assert.assertNotNull(mol.getAtom(f));
            Assert.assertTrue(mol.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * This is a bug reported for JCP.
     *
     * @cdk.bug 956923
     */
    @Test public void testSFBug956923() throws Exception {
        boolean testResults[] = {false, false, false, false, false, false, false, false};
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IMolecule mol = sp.parseSmiles("O=c1cccccc1"); // tropone
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertFalse(CDKHueckelAromaticityDetector.detectAromaticity(mol));
        Assert.assertEquals(testResults.length, mol.getAtomCount());
        for (int f = 0; f < mol.getAtomCount(); f++) {
            Assert.assertEquals(testResults[f], mol.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * A unit test for JUnit
     */
    @Test public void testPorphyrine() throws Exception {
        boolean isAromatic = false;
        boolean testResults[] = {
                false,
                false,
                false,
                false,
                false,
                true,
                true,
                true,
                true,
                true,
                false,
                true,
                true,
                true,
                false,
                true,
                true,
                false,
                false,
                true,
                true,
                false,
                false,
                false,
                true,
                true,
                false,
                false,
                false,
                true,
                true,
                false,
                false,
                false,
                false,
                true,
                true,
                true,
                true,
                false,
                false,
                false
        };

        String filename = "data/mdl/porphyrin.mol";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLV2000Reader reader = new MDLV2000Reader(ins);
        IMolecule molecule = (IMolecule) reader.read(DefaultChemObjectBuilder.getInstance().newMolecule());

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        isAromatic = CDKHueckelAromaticityDetector.detectAromaticity(molecule);
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals(testResults[f], molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
        Assert.assertTrue(isAromatic);
    }


    /**
     * A unit test for JUnit
     *
     * @cdk.bug 698152
     */
    @Test public void testBug698152() throws Exception {
        //boolean isAromatic = false;
        boolean[] testResults = {true,
                true,
                true,
                true,
                true,
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false};

        String filename = "data/mdl/bug698152.mol";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLV2000Reader reader = new MDLV2000Reader(ins);
        IMolecule molecule = (IMolecule) reader.read(DefaultChemObjectBuilder.getInstance().newMolecule());

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        CDKHueckelAromaticityDetector.detectAromaticity(molecule);
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals(testResults[f], molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * A test for the fix of bug #716259, where a quinone ring
     * was falsely detected as aromatic.
     *
     * @cdk.bug 716259
     */
    @Test public void testBug716259() throws Exception {
        IMolecule molecule = null;
        //boolean isAromatic = false;
        boolean[] testResults = {
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false
        };

        String filename = "data/mdl/bug716259.mol";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLV2000Reader reader = new MDLV2000Reader(ins);
        molecule = (IMolecule) reader.read(DefaultChemObjectBuilder.getInstance().newMolecule());

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        CDKHueckelAromaticityDetector.detectAromaticity(molecule);
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals(testResults[f], molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }

    }


    /**
     * A unit test for JUnit
     */
    @Test public void testQuinone() throws Exception {
        Molecule molecule = MoleculeFactory.makeQuinone();
        boolean[] testResults = {false, false, false, false, false, false, false, false};

        CDKHueckelAromaticityDetector.detectAromaticity(molecule);
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals(testResults[f], molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }

    }

    /**
     * @cdk.bug 1328739
     */
    @Test public void testBug1328739() throws Exception {
        String filename = "data/mdl/bug1328739.mol";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLV2000Reader reader = new MDLV2000Reader(ins);
        IMolecule molecule = (IMolecule) reader.read(DefaultChemObjectBuilder.getInstance().newMolecule());

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        CDKHueckelAromaticityDetector.detectAromaticity(molecule);

        Assert.assertEquals(15, molecule.getBondCount());
        Assert.assertTrue(molecule.getBond(0).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(1).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(2).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(3).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(4).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(6).getFlag(CDKConstants.ISAROMATIC));
    }

    /**
     * A unit test for JUnit
     */
    @Test public void testBenzene() throws Exception {
        Molecule molecule = MoleculeFactory.makeBenzene();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        CDKHueckelAromaticityDetector.detectAromaticity(molecule);
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertTrue(molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    @Test public void testCyclobutadiene() throws Exception {
        // anti-aromatic
        Molecule molecule = MoleculeFactory.makeCyclobutadiene();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);

        Assert.assertFalse(CDKHueckelAromaticityDetector.detectAromaticity(molecule));
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertFalse(molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    private IMolecule makeAromaticMolecule() {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom a1 = mol.getBuilder().newAtom("C");
        a1.setPoint2d(new Point2d(329.99999999999994, 971.0));
        mol.addAtom(a1);
        IAtom a2 = mol.getBuilder().newAtom("C");
        a2.setPoint2d(new Point2d(298.8230854637602, 989.0));
        mol.addAtom(a2);
        IAtom a3 = mol.getBuilder().newAtom("C");
        a3.setPoint2d(new Point2d(298.8230854637602, 1025.0));
        mol.addAtom(a3);
        IAtom a4 = mol.getBuilder().newAtom("C");
        a4.setPoint2d(new Point2d(330.0, 1043.0));
        mol.addAtom(a4);
        IAtom a5 = mol.getBuilder().newAtom("C");
        a5.setPoint2d(new Point2d(361.1769145362398, 1025.0));
        mol.addAtom(a5);
        IAtom a6 = mol.getBuilder().newAtom("C");
        a6.setPoint2d(new Point2d(361.1769145362398, 989.0));
        mol.addAtom(a6);
        IAtom a7 = mol.getBuilder().newAtom("C");
        a7.setPoint2d(new Point2d(392.3538290724796, 971.0));
        mol.addAtom(a7);
        IAtom a8 = mol.getBuilder().newAtom("C");
        a8.setPoint2d(new Point2d(423.5307436087194, 989.0));
        mol.addAtom(a8);
        IAtom a9 = mol.getBuilder().newAtom("C");
        a9.setPoint2d(new Point2d(423.5307436087194, 1025.0));
        mol.addAtom(a9);
        IAtom a10 = mol.getBuilder().newAtom("C");
        a10.setPoint2d(new Point2d(392.3538290724796, 1043.0));
        mol.addAtom(a10);
        IBond b1 = mol.getBuilder().newBond(a1, a2, IBond.Order.DOUBLE);
        mol.addBond(b1);
        IBond b2 = mol.getBuilder().newBond(a2, a3, IBond.Order.SINGLE);
        mol.addBond(b2);
        IBond b3 = mol.getBuilder().newBond(a3, a4, IBond.Order.DOUBLE);
        mol.addBond(b3);
        IBond b4 = mol.getBuilder().newBond(a4, a5, IBond.Order.SINGLE);
        mol.addBond(b4);
        IBond b5 = mol.getBuilder().newBond(a5, a6, IBond.Order.DOUBLE);
        mol.addBond(b5);
        IBond b6 = mol.getBuilder().newBond(a6, a1, IBond.Order.SINGLE);
        mol.addBond(b6);
        IBond b7 = mol.getBuilder().newBond(a6, a7, IBond.Order.SINGLE);
        mol.addBond(b7);
        IBond b8 = mol.getBuilder().newBond(a7, a8, IBond.Order.SINGLE);
        mol.addBond(b8);
        IBond b9 = mol.getBuilder().newBond(a8, a9, IBond.Order.SINGLE);
        mol.addBond(b9);
        IBond b10 = mol.getBuilder().newBond(a9, a10, IBond.Order.SINGLE);
        mol.addBond(b10);
        IBond b11 = mol.getBuilder().newBond(a10, a5, IBond.Order.SINGLE);
		  mol.addBond(b11);
		  return mol;
	}

    /**
     * @cdk.bug 1957684
     */
    @Test
    public void test3Amino2MethylPyridine() throws CDKException {

        IMolecule mol = new Molecule();
        IAtom a1 = mol.getBuilder().newAtom("N");
        a1.setPoint2d(new Point2d(3.7321, 1.345));
        mol.addAtom(a1);
        IAtom a2 = mol.getBuilder().newAtom("N");
        a2.setPoint2d(new Point2d(4.5981, -1.155));
        mol.addAtom(a2);
        IAtom a3 = mol.getBuilder().newAtom("C");
        a3.setPoint2d(new Point2d(2.866, -0.155));
        mol.addAtom(a3);
        IAtom a4 = mol.getBuilder().newAtom("C");
        a4.setPoint2d(new Point2d(3.7321, 0.345));
        mol.addAtom(a4);
        IAtom a5 = mol.getBuilder().newAtom("C");
        a5.setPoint2d(new Point2d(2.866, -1.155));
        mol.addAtom(a5);
        IAtom a6 = mol.getBuilder().newAtom("C");
        a6.setPoint2d(new Point2d(2.0, 0.345));
        mol.addAtom(a6);
        IAtom a7 = mol.getBuilder().newAtom("C");
        a7.setPoint2d(new Point2d(4.5981, -0.155));
        mol.addAtom(a7);
        IAtom a8 = mol.getBuilder().newAtom("C");
        a8.setPoint2d(new Point2d(3.7321, -1.655));
        mol.addAtom(a8);
        IAtom a9 = mol.getBuilder().newAtom("H");
        a9.setPoint2d(new Point2d(2.3291, -1.465));
        mol.addAtom(a9);
        IAtom a10 = mol.getBuilder().newAtom("H");
        a10.setPoint2d(new Point2d(2.31, 0.8819));
        mol.addAtom(a10);
        IAtom a11 = mol.getBuilder().newAtom("H");
        a11.setPoint2d(new Point2d(1.4631, 0.655));
        mol.addAtom(a11);
        IAtom a12 = mol.getBuilder().newAtom("H");
        a12.setPoint2d(new Point2d(1.69, -0.1919));
        mol.addAtom(a12);
        IAtom a13 = mol.getBuilder().newAtom("H");
        a13.setPoint2d(new Point2d(5.135, 0.155));
        mol.addAtom(a13);
        IAtom a14 = mol.getBuilder().newAtom("H");
        a14.setPoint2d(new Point2d(3.7321, -2.275));
        mol.addAtom(a14);
        IAtom a15 = mol.getBuilder().newAtom("H");
        a15.setPoint2d(new Point2d(4.269, 1.655));
        mol.addAtom(a15);
        IAtom a16 = mol.getBuilder().newAtom("H");
        a16.setPoint2d(new Point2d(3.1951, 1.655));
        mol.addAtom(a16);
        IBond b1 = mol.getBuilder().newBond(a1, a4, IBond.Order.SINGLE);
        mol.addBond(b1);
        IBond b2 = mol.getBuilder().newBond(a1, a15, IBond.Order.SINGLE);
        mol.addBond(b2);
        IBond b3 = mol.getBuilder().newBond(a1, a16, IBond.Order.SINGLE);
        mol.addBond(b3);
        IBond b4 = mol.getBuilder().newBond(a2, a7, IBond.Order.DOUBLE);
        mol.addBond(b4);
        IBond b5 = mol.getBuilder().newBond(a2, a8, IBond.Order.SINGLE);
        mol.addBond(b5);
        IBond b6 = mol.getBuilder().newBond(a3, a4, IBond.Order.DOUBLE);
        mol.addBond(b6);
        IBond b7 = mol.getBuilder().newBond(a3, a5, IBond.Order.SINGLE);
        mol.addBond(b7);
        IBond b8 = mol.getBuilder().newBond(a3, a6, IBond.Order.SINGLE);
        mol.addBond(b8);
        IBond b9 = mol.getBuilder().newBond(a4, a7, IBond.Order.SINGLE);
        mol.addBond(b9);
        IBond b10 = mol.getBuilder().newBond(a5, a8, IBond.Order.DOUBLE);
        mol.addBond(b10);
        IBond b11 = mol.getBuilder().newBond(a5, a9, IBond.Order.SINGLE);
        mol.addBond(b11);
        IBond b12 = mol.getBuilder().newBond(a6, a10, IBond.Order.SINGLE);
        mol.addBond(b12);
        IBond b13 = mol.getBuilder().newBond(a6, a11, IBond.Order.SINGLE);
        mol.addBond(b13);
        IBond b14 = mol.getBuilder().newBond(a6, a12, IBond.Order.SINGLE);
        mol.addBond(b14);
        IBond b15 = mol.getBuilder().newBond(a7, a13, IBond.Order.SINGLE);
        mol.addBond(b15);
        IBond b16 = mol.getBuilder().newBond(a8, a14, IBond.Order.SINGLE);
        mol.addBond(b16);


        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        boolean isAromatic = CDKHueckelAromaticityDetector.detectAromaticity(mol);
        Assert.assertTrue(isAromatic);

        Iterator<IAtom> atoms = mol.atoms().iterator();
        int nCarom = 0;
        int nCalip = 0;
        int nNarom = 0;
        int nNaliph = 0;
        while (atoms.hasNext()) {
            IAtom atom = atoms.next();
            if (atom.getSymbol().equals("C")) {
                if (atom.getFlag(CDKConstants.ISAROMATIC)) nCarom++;
                else nCalip++;
            } else if (atom.getSymbol().equals("N")) {
                if (atom.getFlag(CDKConstants.ISAROMATIC)) nNarom++;
                else nNaliph++;
            }

        }
        Assert.assertEquals(5, nCarom);
        Assert.assertEquals(1, nCalip);
        Assert.assertEquals(1, nNarom);
        Assert.assertEquals(1, nNaliph);
    }

}

