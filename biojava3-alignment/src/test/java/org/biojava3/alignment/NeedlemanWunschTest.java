/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on June 17, 2010
 * Author: Mark Chapman
 */

package org.biojava3.alignment;

import static org.junit.Assert.*;

import org.biojava3.alignment.routines.GuanUberbacher;
import org.biojava3.alignment.template.GapPenalty;
import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.compound.DNACompoundSet;
import org.biojava3.core.sequence.compound.NucleotideCompound;
import org.junit.Before;
import org.junit.Test;

public class NeedlemanWunschTest {

    private ProteinSequence query, target;
    private GapPenalty gaps;
    private SubstitutionMatrix<AminoAcidCompound> blosum62;
    private NeedlemanWunsch<ProteinSequence, AminoAcidCompound> alignment, self;

    @Before
    public void setup() {
        query = new ProteinSequence("ARND");
        target = new ProteinSequence("RDG");
        gaps = new SimpleGapPenalty((short) 10, (short) 1);
        blosum62 = SubstitutionMatrixHelper.getBlosum62();
        alignment = new NeedlemanWunsch<ProteinSequence, AminoAcidCompound>(query, target, gaps, blosum62);
        self = new NeedlemanWunsch<ProteinSequence, AminoAcidCompound>(query, query, gaps, blosum62);
    }

    @Test
    public void testNeedlemanWunsch() {
        NeedlemanWunsch<ProteinSequence, AminoAcidCompound> nw =
                new NeedlemanWunsch<ProteinSequence, AminoAcidCompound>();
        nw.setQuery(query);
        nw.setTarget(target);
        nw.setGapPenalty(gaps);
        nw.setSubstitutionMatrix(blosum62);
        assertEquals(nw.getScore(), alignment.getScore());
    }

    @Test
    public void testGetQuery() {
        assertEquals(alignment.getQuery(), query);
        assertEquals(self.getQuery(), query);
    }

    @Test
    public void testGetTarget() {
        assertEquals(alignment.getTarget(), target);
        assertEquals(self.getTarget(), query);
    }

    @Test
    public void testGetGapPenalty() {
        assertEquals(alignment.getGapPenalty(), gaps);
        assertEquals(self.getGapPenalty(), gaps);
    }

    @Test
    public void testGetSubstitutionMatrix() {
        assertEquals(alignment.getSubstitutionMatrix(), blosum62);
        assertEquals(self.getSubstitutionMatrix(), blosum62);
    }

    @Test
    public void testIsStoringScoreMatrix() {
        assertFalse(alignment.isStoringScoreMatrix());
        assertFalse(self.isStoringScoreMatrix());
    }

    @Test
    public void testGetScoreMatrix() {
        short[][][] scores = alignment.getScoreMatrix();
        assertEquals(scores[2][1][0], -6);
        scores = self.getScoreMatrix();
        assertEquals(scores[3][4][2], 4);
    }

    @Test
    public void testGetScoreMatrixAsString() {
        assertEquals(alignment.getScoreMatrixAsString(), String.format(
                "Substitution%n" +
                "        R   D   G%n" +
                "    0  -\u221E  -\u221E  -\u221E%n" +
                "A  -\u221E  -1 -13 -12%n" +
                "R  -\u221E  -6  -3 -14%n" +
                "N  -\u221E -12  -5  -3%n" +
                "D  -\u221E -15  -6  -6%n" +
                "%nDeletion%n" +
                "        R   D   G%n" +
                "  -10  -\u221E  -\u221E  -\u221E%n" +
                "A -11  -\u221E  -\u221E  -\u221E%n" +
                "R -12 -12 -24 -23%n" +
                "N -13 -13 -14 -24%n" +
                "D -14 -14 -15 -14%n" +
                "%nInsertion%n" +
                "        R   D   G%n" +
                "  -10 -11 -12 -13%n" +
                "A  -\u221E  -\u221E -12 -13%n" +
                "R  -\u221E  -\u221E -17 -14%n" +
                "N  -\u221E  -\u221E -23 -16%n" +
                "D  -\u221E  -\u221E -26 -17%n"));
        assertEquals(self.getScoreMatrixAsString(), String.format(
                "Substitution%n" +
                "        A   R   N   D%n" +
                "    0  -\u221E  -\u221E  -\u221E  -\u221E%n" +
                "A  -\u221E   4 -12 -14 -15%n" +
                "R  -\u221E -12   9  -7 -10%n" +
                "N  -\u221E -14  -7  15  -1%n" +
                "D  -\u221E -15 -10  -1  21%n" +
                "%nDeletion%n" +
                "        A   R   N   D%n" +
                "  -10  -\u221E  -\u221E  -\u221E  -\u221E%n" +
                "A -11  -\u221E  -\u221E  -\u221E  -\u221E%n" +
                "R -12  -7 -23 -25 -26%n" +
                "N -13  -8  -2 -18 -21%n" +
                "D -14  -9  -3   4 -12%n" +
                "%nInsertion%n" +
                "        A   R   N   D%n" +
                "  -10 -11 -12 -13 -14%n" +
                "A  -\u221E  -\u221E  -7  -8  -9%n" +
                "R  -\u221E  -\u221E -23  -2  -3%n" +
                "N  -\u221E  -\u221E -25 -18   4%n" +
                "D  -\u221E  -\u221E -26 -21 -12%n"));
    }

    @Test
    public void testGetComputationTime() {
        assertTrue(alignment.getComputationTime() > 0);
        assertTrue(self.getComputationTime() > 0);
    }

    @Test
    public void testGetProfile() {
        assertEquals(alignment.getProfile().toString(), String.format("ARND%n-RDG%n"));
        assertEquals(self.getProfile().toString(), String.format("ARND%nARND%n"));
    }

    @Test
    public void testGetMaxScore() {
        assertEquals(alignment.getMaxScore(), 21);
        assertEquals(self.getMaxScore(), 21);
    }

    @Test
    public void testGetMinScore() {
        assertEquals(alignment.getMinScore(), -27);
        assertEquals(self.getMinScore(), -28);
    }

    @Test
    public void testGetScore() {
        assertEquals(alignment.getScore(), -6);
        assertEquals(self.getScore(), 21);
    }

    @Test
    public void testGetPair() {
        assertEquals(alignment.getPair().toString(), String.format("ARND%n-RDG%n"));
        assertEquals(self.getPair().toString(), String.format("ARND%nARND%n"));
    }
    /**
     * @author Daniel Cameron
     */
    @Test
	public void should_align_all_anchored() {
    	DNASequence query = new DNASequence("ACG", AmbiguityDNACompoundSet.getDNACompoundSet());
		DNASequence target = new DNASequence("CGT", AmbiguityDNACompoundSet.getDNACompoundSet());
		NeedlemanWunsch<DNASequence, NucleotideCompound> aligner = new NeedlemanWunsch<DNASequence, NucleotideCompound>(query, target, new SimpleGapPenalty((short)0, (short)10), SubstitutionMatrixHelper.getNuc4_4());
		aligner.setAnchors(new int[] { 0, 1, 2} );
		assertEquals(String.format("ACG%nCGT%n"), aligner.getPair().toString());
    }
    /**
     * @author Daniel Cameron
     */
    @Test
	public void should_align_starting_anchor() {
    	DNASequence query = new DNASequence("AAT", AmbiguityDNACompoundSet.getDNACompoundSet());
		DNASequence target = new DNASequence("AATT", AmbiguityDNACompoundSet.getDNACompoundSet());
		NeedlemanWunsch<DNASequence, NucleotideCompound> aligner = new NeedlemanWunsch<DNASequence, NucleotideCompound>(query, target, new SimpleGapPenalty((short)0, (short)10), SubstitutionMatrixHelper.getNuc4_4());
		aligner.setAnchors(new int[] { 1, -1, -1} );
		assertEquals(String.format("-AAT%nAATT%n"), aligner.getPair().toString());
    }
    /**
     * @author Daniel Cameron
     */
    @Test
	public void should_align_ending_anchor() {
    	DNASequence query = new DNASequence("AAG", AmbiguityDNACompoundSet.getDNACompoundSet());
		DNASequence target = new DNASequence("AATT", AmbiguityDNACompoundSet.getDNACompoundSet());
		NeedlemanWunsch<DNASequence, NucleotideCompound> aligner = new NeedlemanWunsch<DNASequence, NucleotideCompound>(query, target, new SimpleGapPenalty((short)0, (short)10), SubstitutionMatrixHelper.getNuc4_4());
		aligner.addAnchor(2, 3);
		assertEquals(String.format("AA-G%nAATT%n"), aligner.getPair().toString());
    }
    /**
     * @author Daniel Cameron
     */
    @Test
	public void should_align_middle_anchor() {
    	DNASequence query = new DNASequence("ACTTT", AmbiguityDNACompoundSet.getDNACompoundSet());
		DNASequence target = new DNASequence("ACGTTT", AmbiguityDNACompoundSet.getDNACompoundSet());
		NeedlemanWunsch<DNASequence, NucleotideCompound> aligner = new NeedlemanWunsch<DNASequence, NucleotideCompound>(query, target, new SimpleGapPenalty((short)0, (short)10), SubstitutionMatrixHelper.getNuc4_4());
		aligner.setAnchors(new int[] { -1, 2, -1} );
		assertEquals(String.format("A-CTTT%nACGTTT%n"), aligner.getPair().toString());
    }
    /**
     * @author Daniel Cameron
     */
    @Test
	public void should_align_multiple_anchors() {
    	DNASequence query = new DNASequence("ACGT", AmbiguityDNACompoundSet.getDNACompoundSet());
		DNASequence target = new DNASequence("ATACGT", AmbiguityDNACompoundSet.getDNACompoundSet());
		NeedlemanWunsch<DNASequence, NucleotideCompound> aligner = new NeedlemanWunsch<DNASequence, NucleotideCompound>(query, target, new SimpleGapPenalty((short)0, (short)10), SubstitutionMatrixHelper.getNuc4_4());
		aligner.addAnchor(0, 0);
		aligner.addAnchor(1, 1);
		aligner.addAnchor(2, 2);
		aligner.addAnchor(3, 5);
		assertEquals(String.format("ACG--T%nATACGT%n"), aligner.getPair().toString());
    }
    /**
     * @author Daniel Cameron
     */
    @Test
	public void anchors_should_not_change_score() {
    	DNASequence query = new DNASequence("ACGT", AmbiguityDNACompoundSet.getDNACompoundSet());
		DNASequence target = new DNASequence("ACGT", AmbiguityDNACompoundSet.getDNACompoundSet());
		NeedlemanWunsch<DNASequence, NucleotideCompound> aligner = new NeedlemanWunsch<DNASequence, NucleotideCompound>(query, target, new SimpleGapPenalty((short)5, (short)10), SubstitutionMatrixHelper.getNuc4_4());
		NeedlemanWunsch<DNASequence, NucleotideCompound> anchored = new NeedlemanWunsch<DNASequence, NucleotideCompound>(query, target, new SimpleGapPenalty((short)5, (short)10), SubstitutionMatrixHelper.getNuc4_4());
		anchored.addAnchor(0, 0);
		anchored.addAnchor(1, 1);
		anchored.addAnchor(2, 2);
		anchored.addAnchor(3, 3);
		assertEquals(aligner.getScore(), anchored.getScore());
    }
    /**
     * @author Daniel Cameron
     */
	@Test
	public void testAnchoredDNAAlignment() {
		DNASequence query = new DNASequence(  "ACGTACCGGTTTT", DNACompoundSet.getDNACompoundSet());
		DNASequence target = new DNASequence("TACGTCCGGTTACGTACGTT", DNACompoundSet.getDNACompoundSet());
		NeedlemanWunsch<DNASequence, NucleotideCompound> aligner = new NeedlemanWunsch<DNASequence, NucleotideCompound>(query, target, new SimpleGapPenalty((short)5, (short)2), SubstitutionMatrixHelper.getNuc4_4());
		assertEquals(String.format("-ACGTACCGGTT-------TT%nTACGT-CCGGTTACGTACGTT%n"), aligner.getPair().toString());
	}
}
