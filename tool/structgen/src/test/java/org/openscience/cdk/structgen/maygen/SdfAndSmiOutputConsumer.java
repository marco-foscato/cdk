/*
 * Copyright (c) 2021 Mehmet Aziz Yirik <mehmetazizyirik@outlook.com> <0000-0001-7520-7215@orcid.org>
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

package org.openscience.cdk.structgen.maygen;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmiFlavor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.ILoggingTool;
import org.openscience.cdk.tools.LoggingToolFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

final class SdfAndSmiOutputConsumer implements Maygen.Consumer {

    private final ILoggingTool logger = LoggingToolFactory.createLoggingTool(SdfAndSmiOutputConsumer.class);

    private BufferedWriter wtr;
    private SDFWriter sdfw;
    private final SmilesGenerator smigen = new SmilesGenerator(SmiFlavor.Default);
    private final File dir;
    private boolean coordinates;

    public SdfAndSmiOutputConsumer(File dir) {
        this.dir = dir;
        if (dir == null) {
            sdfw = new SDFWriter(System.out);
            wtr = new BufferedWriter(new OutputStreamWriter(System.out,
                    StandardCharsets.UTF_8));
        } else {
            dir.mkdirs();
        }
        setupOptions();
    }

    public SdfAndSmiOutputConsumer(Writer writer) {
        this.dir = null;
        sdfw = new SDFWriter(writer);
        wtr = new BufferedWriter(writer);
        setupOptions();
    }

    @Override
    public void configure(String name) {
        if (dir != null) {
            if (sdfw != null) {
                try {
                    sdfw.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (wtr != null) {
                try {
                    wtr.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
            try {
                sdfw = new SDFWriter(new FileOutputStream(new File(dir, name + ".sdf")));
                wtr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, name + ".smi")),
                        StandardCharsets.UTF_8));
                setupOptions();
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void setupOptions() {
        if (sdfw == null)
            return;
        try {
            sdfw.getSetting(MDLV2000Writer.OptProgramName)
                .setSetting("MAYGEN");
            sdfw.getSetting(MDLV2000Writer.OptWriteDefaultProperties)
                .setSetting("false");
        } catch (CDKException e) {
            logger.error(e);
        }
    }

    @Override
    public void consume(IAtomContainer mol) {
        try {
            if (coordinates)
                new StructureDiagramGenerator().generateCoordinates(mol);
            sdfw.write(mol);
            String smi = smigen.create(mol);
            wtr.write(smi + "\n");
        } catch (CDKException | IOException e) {
            logger.error(e);
        }
    }

    @Override
    public void close() throws IOException {
        sdfw.close();
        wtr.close();
    }

    public void setCoordinates(boolean b) {
        this.coordinates = b;
    }
}
