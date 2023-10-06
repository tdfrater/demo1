package tfrater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
// Fasta sequence class in charge of handling all the processes for for a FastaSequence object
public class FastaSequence 
{
	// holds all the necessary instance variables for the FastaSequence class
	private final String fasta;
	private final String header;
	private final String sequence;
	private final int numA;
	private final int numT;
	private final int numG;
	private final int numC;
	private final int gcRatio;
	
	//Establishes what each instance variable represents
	public FastaSequence(String fasta)
	{
		this.fasta = fasta;
		//I decided it would be easiest to read in an entire header and sequence combo
		//I split the string using the regular expression below. It splits based on seeing four capital letters in a row
 		this.header = fasta.split("([A-Z]{4})",2)[0].trim().substring(1);
 		//The regular expression below checks for four capital letters but keeps the delimeter using ?=
		this.sequence = fasta.split("(?=[A-Z]{4})",2)[1].trim();
		//The following section loops through the sequence and counts each codon
		String options = "ATGC";
		int countA = 0;
		int countT = 0;
		int countG = 0;
		int countC = 0;
		for(int x = 0; x < sequence.length();x ++) {
			if(sequence.charAt(x) == options.charAt(0))
				countA ++;
			else if(sequence.charAt(x) == options.charAt(1))
				countT ++;
			else if(sequence.charAt(x) == options.charAt(2))
				countG ++;
			else if (sequence.charAt(x) == options.charAt(3))
				countC ++;
		}
		this.numA = countA; 
		this.numT = countT;
		this.numG = countG;
		this.numC = countC;
		this.gcRatio = (numG / numC);
	}
	
	public String getFasta()
	{
		return fasta;
	}
	
	public String getHeader()
	{
		return header;
	}
	
	public String getSequence()
	{
		return sequence;
	}
	
	public int getnumA()
	{
		return numA;
	}
	
	public int getnumT()
	{
		return numT;
	}
	
	public int getnumG()
	{
		return numG;
	}
	
	public int getnumC()
	{
		return numC;
	}
	
	public int getGCRatio()
	{
		return gcRatio;
	}
	
	public static List<FastaSequence> readFastaFile(String filepath) throws Exception
	{
		List<FastaSequence> seqList = new LinkedList<FastaSequence>();
		String fasta = "";
		String nextLine = "";
		int lineNum = 0;
		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
		while (( nextLine = reader.readLine()) != null) {
			if(nextLine.startsWith(">")) {
				if(lineNum == 0) {
					fasta += nextLine;
					lineNum ++;
				} else {
					seqList.add(new FastaSequence(fasta));
					fasta="";
					fasta += nextLine;
					lineNum ++;
				}
			} else {
				fasta += nextLine;
				lineNum ++;
			}
		}
		seqList.add(new FastaSequence(fasta));
		reader.close();
		return seqList;
	}
	
	
	
	//Takes the FastaSequence list and name of output file then writes that to a tsv 
	public static void writeTableSummary( List<FastaSequence> list, String outputFile) throws Exception 
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		writer.write("sequenceID"+"\t"+"numA"+"\t"+"numC"+"\t"+"numG"+"\t"+"numT"+"\t"+"sequence");
		writer.newLine();
		for(int x = 0; x < list.size();x ++) {
			writer.write(list.get(x).getHeader()+"\t"+list.get(x).getnumA()+"\t"+list.get(x).getnumC()+"\t"+list.get(x).getnumG()+"\t"+list.get(x).getnumT()+"\t"+list.get(x).getSequence());
			writer.newLine();
		}
		writer.close();
	}
	
	//Main method
	public static void main(String[] args) 
	{
		try {
			//The try and catch where I call the readFastaFile method along with the writeTableSummary
			List<FastaSequence> fastaList = readFastaFile("C:\\Users\\theod\\Downloads\\rcsb_pdb_1JM6.fasta");
			writeTableSummary(fastaList,"C:\\Users\\theod\\Downloads\\fastaParserOutput.tsv");
		}
		catch (Exception e) 
		{
			System.out.println("There was an error in reading in your fasta file. I would check the filepath");
			e.printStackTrace();
		}
	}
}
