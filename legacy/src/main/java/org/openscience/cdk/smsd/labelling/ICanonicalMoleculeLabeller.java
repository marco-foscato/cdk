package org.openscience.cdk.smsd.labelling;

import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * @deprecated This class is part of SMSD and either duplicates functionality elsewhere in the CDK or provides public
 *             access to internal implementation details. SMSD has been deprecated from the CDK with a newer, more recent
 *             version of SMSD is available at <a href="http://github.com/asad/smsd">http://github.com/asad/smsd</a>.
 */
@Deprecated
public interface ICanonicalMoleculeLabeller {

    IAtomContainer getCanonicalMolecule(IAtomContainer container);

    int[] getCanonicalPermutation(IAtomContainer container);
}
