/*
 * =====================================
 *  Copyright (c) 2022 NextMove Software
 * =====================================
 */
package org.openscience.cdk.test.interfaces;

import javax.vecmath.Vector3d;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.ICrystal;

/**
 * Checks the functionality of {@link org.openscience.cdk.interfaces.ICrystal} implementations.
 *
 */
public abstract class AbstractCrystalTest extends AbstractAtomContainerTest {

    @Test
    @Override
    public void testAdd_IAtomContainer() {
        ICrystal crystal = (ICrystal) newChemObject();

        IAtomContainer acetone = crystal.getBuilder().newInstance(IAtomContainer.class);
        IAtom c1 = crystal.getBuilder().newInstance(IAtom.class, "C");
        IAtom c2 = crystal.getBuilder().newInstance(IAtom.class, "C");
        IAtom o = crystal.getBuilder().newInstance(IAtom.class, "O");
        IAtom c3 = crystal.getBuilder().newInstance(IAtom.class, "C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = crystal.getBuilder().newInstance(IBond.class, c1, c2, IBond.Order.SINGLE);
        IBond b2 = crystal.getBuilder().newInstance(IBond.class, c1, o, IBond.Order.DOUBLE);
        IBond b3 = crystal.getBuilder().newInstance(IBond.class, c1, c3, IBond.Order.SINGLE);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);

        crystal.add(acetone);
        Assertions.assertEquals(4, crystal.getAtomCount());
        Assertions.assertEquals(3, crystal.getBondCount());
    }

    @Test
    @Override
    public void testAddAtom_IAtom() {
        ICrystal crystal = (ICrystal) newChemObject();
        IAtom c1 = crystal.getBuilder().newInstance(IAtom.class, "C");
        crystal.addAtom(c1);
        Assertions.assertEquals(1, crystal.getAtomCount());
    }

    @Test
    public void testSetA_Vector3d() {
        ICrystal crystal = (ICrystal) newChemObject();

        crystal.setA(new Vector3d(1.0, 2.0, 3.0));
        Vector3d a = crystal.getA();
        Assertions.assertEquals(1.0, a.x, 0.001);
        Assertions.assertEquals(2.0, a.y, 0.001);
        Assertions.assertEquals(3.0, a.z, 0.001);
    }

    @Test
    public void testGetA() {
        ICrystal crystal = (ICrystal) newChemObject();

        crystal.setA(new Vector3d(1.0, 2.0, 3.0));
        Vector3d a = crystal.getA();
        Assertions.assertNotNull(a);
    }

    @Test
    public void testGetB() {
        ICrystal crystal = (ICrystal) newChemObject();

        crystal.setB(new Vector3d(1.0, 2.0, 3.0));
        Vector3d a = crystal.getB();
        Assertions.assertNotNull(a);
    }

    @Test
    public void testGetC() {
        ICrystal crystal = (ICrystal) newChemObject();

        crystal.setC(new Vector3d(1.0, 2.0, 3.0));
        Vector3d a = crystal.getC();
        Assertions.assertNotNull(a);
    }

    @Test
    public void testSetB_Vector3d() {
        ICrystal crystal = (ICrystal) newChemObject();

        crystal.setB(new Vector3d(1.0, 2.0, 3.0));
        Vector3d b = crystal.getB();
        Assertions.assertEquals(1.0, b.x, 0.001);
        Assertions.assertEquals(2.0, b.y, 0.001);
        Assertions.assertEquals(3.0, b.z, 0.001);
    }

    @Test
    public void testSetC_Vector3d() {
        ICrystal crystal = (ICrystal) newChemObject();

        crystal.setC(new Vector3d(1.0, 2.0, 3.0));
        Vector3d c = crystal.getC();
        Assertions.assertEquals(1.0, c.x, 0.001);
        Assertions.assertEquals(2.0, c.y, 0.001);
        Assertions.assertEquals(3.0, c.z, 0.001);
    }

    @Test
    public void testSetSpaceGroup_String() {
        ICrystal crystal = (ICrystal) newChemObject();
        String spacegroup = "P 2_1 2_1 2_1";
        crystal.setSpaceGroup(spacegroup);
        Assertions.assertEquals(spacegroup, crystal.getSpaceGroup());
    }

    @Test
    public void testGetSpaceGroup() {
        ICrystal crystal = (ICrystal) newChemObject();
        String spacegroup = "P 2_1 2_1 2_1";
        crystal.setSpaceGroup(spacegroup);
        Assertions.assertNotNull(crystal.getSpaceGroup());
        Assertions.assertEquals(spacegroup, crystal.getSpaceGroup());
    }

    @Test
    public void testSetZ_Integer() {
        ICrystal crystal = (ICrystal) newChemObject();
        int z = 2;
        crystal.setZ(z);
        Assertions.assertEquals(z, crystal.getZ().intValue());
    }

    @Test
    public void testGetZ() {
        testSetZ_Integer();
    }

    /**
     * Method to test whether the class complies with RFC #9.
     */
    @Test
    @Override
    public void testToString() {
        ICrystal crystal = (ICrystal) newChemObject();
        String description = crystal.toString();
        for (int i = 0; i < description.length(); i++) {
            Assertions.assertTrue(description.charAt(i) != '\n');
            Assertions.assertTrue(description.charAt(i) != '\r');
        }
    }

    @Test
    @Override
    public void testClone() throws Exception {
        ICrystal crystal = (ICrystal) newChemObject();
        Object clone = crystal.clone();
        Assertions.assertTrue(clone instanceof ICrystal);
    }

    @Test
    public void testClone_Axes() throws Exception {
        ICrystal crystal1 = (ICrystal) newChemObject();
        Vector3d axes = new Vector3d(1.0, 2.0, 3.0);
        crystal1.setA(axes);
        ICrystal crystal2 = crystal1.clone();

        // test cloning of axes
        crystal1.getA().x = 5.0;
        Assertions.assertEquals(1.0, crystal2.getA().x, 0.001);
    }

    @Test
    public void testSetZeroAxes() {
        ICrystal crystal = (ICrystal) newChemObject();

        crystal.setA(new Vector3d(1.0, 2.0, 3.0));
        Vector3d a = crystal.getA();
        Assertions.assertNotNull(a);
    }

}
